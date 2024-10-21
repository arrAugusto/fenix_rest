package com.serviceBack.fenix.services;

import com.serviceBack.fenix.Utils.GenericSQL;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.Utils.SecureUniqueCodeGenerator;
import com.serviceBack.fenix.Utils.Send;
import com.serviceBack.fenix.models.AuthTransaction;
import com.serviceBack.fenix.models.ConfigFirmas;
import com.serviceBack.fenix.models.ResponseValidFirma;
import commons.Const_env;
import commons.GenericResponse;
import commons.MessageControll;
import commons.StoredProcedures;
import commons.UXMessages;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.serviceBack.fenix.interfaces.HtmlPdfInterfaces;
import com.serviceBack.fenix.models.Comprobante;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import com.serviceBack.fenix.interfaces.AuthTransactionInterface;

/**
 * Servicio para manejar las transacciones de autenticación.
 */
@Service
public class AuthTransactionService implements AuthTransactionInterface {

    private String MENSAJE_MAIL = "";
    private String SUBJECT = "";

    @Autowired
    private HtmlPdfInterfaces htmlPdfService;

    private final GenericSQL genericSQL;
    private final StoredProcedures stored;
    private final Const_env const_env;
    private final UXMessages uxMessages;
    private final Send sendMail;
    private final GenericResponse generiResponse;
    private final MessageControll messageControll;
    private static final Logger logger = Logger.getLogger(AuthTransactionService.class.getName());

    @Autowired
    public AuthTransactionService(GenericSQL genericSQL) {
        this.genericSQL = genericSQL;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
        this.const_env = new Const_env();
        this.uxMessages = new UXMessages();
        this.sendMail = new Send();
        this.generiResponse = new GenericResponse();
        this.messageControll = new MessageControll();
    }

    @Override
    public ResponseService authTransaction(AuthTransaction authTransaction) {
        logger.info("Iniciando proceso de autenticación para la transacción ID: " + authTransaction.getIdTransaction());
        ResponseService responseService = new ResponseService();

        try {
            // Establecer mensajes iniciales de éxito
            this.MENSAJE_MAIL = this.uxMessages.FIRMA_EXITOSA;
            this.SUBJECT = this.uxMessages.SUBJECT_FIRMA_EXITOSA;

            // Generar código de autenticación
            SecureUniqueCodeGenerator security = new SecureUniqueCodeGenerator();
            authTransaction.setAuth_transaccion(security.generateSecureRandomSignature());
            authTransaction.setUsuario_firma("1");
            logger.info("Código de autenticación generado: " + authTransaction.getAuth_transaccion());

            // Creando objeto de inserción
            Object[] params = {
                authTransaction.getIdTransaction(),
                authTransaction.getAuth_transaccion(),
                authTransaction.getUsuario_firma(),
                authTransaction.getModuloFirma()
            };

            // Ejecutar la inserción en la base de datos
            logger.info("Ejecutando inserción en la base de datos para la transacción.");

            // Guardar el resultado de checkTransaction para evitar múltiples llamadas

            boolean isInserted = genericSQL.insert(stored.STORED_PROCEDURE_CALL_INSERT_AUTH_TRANSACTION, params);


            if (isInserted) {
                logger.info("¡Transacción de autenticación insertada con éxito!");

                Object[] paramsConfig = {const_env.CALL_INGRESO_NORMAL};
                ResultSet configFirma = null;

                try {
                    logger.info("Ejecutando consulta para obtener la configuración de firmas.");
                    configFirma = genericSQL.select(stored.STORED_PROCEDURE_CALL_GET_CONFIG_FIRMAS, paramsConfig);
                    TransoformGetConfig transformConfig = new TransoformGetConfig();

                    List<ConfigFirmas> data = transformConfig.transformData(configFirma);
                    logger.info("Se obtuvo la configuración de firmas: " + data.size() + " registros encontrados.");

                    // Validar las firmas requeridas
                    ResponseValidFirma response = validFirma(data, authTransaction.getIdTransaction());

                    if (response.isValid()) {
                        //Creando pdf
                        ResponseService pdfResponse = htmlPdfService.getPDFTransaction(authTransaction.getIdTransaction());

                        Comprobante comprobante = htmlPdfService.getDataPDF(authTransaction.getIdTransaction(), "1");

                        System.out.println("pdfResponse> " + pdfResponse);
                        // Si todas las firmas están completas, enviar alerta de éxito
                        logger.info("Todas las firmas requeridas se han completado.");
                        this.MENSAJE_MAIL = "Todas las firmas requeridas se han completado. Enviando alerta.";
                        this.SUBJECT = "DOCUMENTO FIRMADO CON EXITO";
                        finishTransaction(authTransaction.getIdTransaction());
                        sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, this.MENSAJE_MAIL, this.SUBJECT, comprobante.getUrl_comprobante());
                        // Código y mensaje personalizados (antes era generiResponse.GenericResponsError)
                        responseService.setCodeResponse(messageControll.MESSAGE_FENIX_00);
                        responseService.setMessageResponse(messageControll.MESSAGE_FENIX_DEFAULT);
                        responseService.setData(Arrays.asList(pdfResponse.getData()));
                        return responseService;
                    }

                    // Si faltan firmas, construir mensaje de error
                    StringBuilder messageErrorBuilder = new StringBuilder()
                            .append(response.getDiferencia())
                            .append(response.getDiferencia() > 1 ? this.uxMessages.FIRMA_FALTANTES : this.uxMessages.FIRMA_FALTANTE)
                            .append(this.uxMessages.FIRMA_FOOTER);

                    String messageError = messageErrorBuilder.toString();
                    logger.warning("Faltan firmas para completar la transacción: " + messageError);

                    // Enviar correo indicando firmas faltantes
                    this.MENSAJE_MAIL = messageError;
                    this.SUBJECT = this.uxMessages.SUBJECT_FIRMA_FALTANTE;
                    sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, this.MENSAJE_MAIL, this.SUBJECT, "");

                    // Código y mensaje personalizados para firmas faltantes
                    responseService.setCodeResponse("00");
                    responseService.setMessageResponse("Faltan firmas para completar la transacción: " + messageError);
                    return responseService;

                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error al ejecutar la consulta SELECT en configFirma: " + e.getMessage(), e);
                    responseService.setCodeResponse("99");
                    responseService.setMessageResponse("Error al obtener la configuración de firmas: " + e.getMessage());
                    return responseService;
                } finally {
                    if (configFirma != null) {
                        try {
                            logger.info("Cerrando ResultSet de configuración de firmas.");
                            configFirma.close();
                        } catch (SQLException e) {
                            logger.log(Level.SEVERE, "Error al cerrar ResultSet configFirma: " + e.getMessage(), e);
                        }
                    }
                }
            } else {
                // Si la inserción falló, enviar mensaje de advertencia
                logger.warning("Falló la inserción de la transacción de autenticación.");
                responseService.setCodeResponse("02");
                responseService.setMessageResponse("Error al insertar la transacción de autenticación.");
                return responseService;
            }

        } catch (DataAccessException e) {
            // Manejar excepción de acceso a datos
            logger.log(Level.SEVERE, "Error de acceso a datos: " + e.getMessage(), e);
            this.MENSAJE_MAIL = this.uxMessages.ERROR_INTERNO;
            this.SUBJECT = this.uxMessages.SUBJECT_ERROR_INTERNO + e.getMessage();
            sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, this.MENSAJE_MAIL, this.SUBJECT, "");

            // Responder con error de acceso a datos
            responseService.setCodeResponse("99");
            responseService.setMessageResponse("Error de acceso a datos: " + e.getMessage());
            return responseService;

        } catch (Exception e) {
            // Manejar excepción general
            logger.log(Level.SEVERE, "Error interno: " + e.getMessage(), e);
            this.MENSAJE_MAIL = this.uxMessages.ERROR_INTERNO;
            this.SUBJECT = this.uxMessages.SUBJECT_ERROR_INTERNO + e.getMessage();
            sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, this.MENSAJE_MAIL, this.SUBJECT, "");

            // Responder con error general
            responseService.setCodeResponse("99");
            responseService.setMessageResponse("Error interno: " + e.getMessage());
            return responseService;
        }
    }

    public ResponseValidFirma validFirma(List<ConfigFirmas> data, String id_transaccion) {
        logger.info("Iniciando validación de firmas para la transacción ID: " + id_transaccion);
        int count = 0;
        ResponseValidFirma response = new ResponseValidFirma();

        for (ConfigFirmas config : data) {
            logger.info("Validando firma del módulo: " + config.getModulo());
            Object[] paramsValid = {config.getModulo(), id_transaccion};

            ResultSet rsValid = null;
            try {
                rsValid = genericSQL.select(stored.STORED_PROCEDURE_CALL_GET_VALID_FIRMA, paramsValid);
                if (rsValid != null && rsValid.next()) {
                    if (config.getModulo().equals(rsValid.getString("modulo"))) {
                        count++;
                        logger.info("Firma validada para el módulo: " + config.getModulo());
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error al validar firmas: " + e.getMessage(), e);
            } finally {
                if (rsValid != null) {
                    try {
                        logger.info("Cerrando ResultSet en validFirma.");
                        rsValid.close();
                    } catch (SQLException e) {
                        logger.log(Level.SEVERE, "Error al cerrar ResultSet en validFirma: " + e.getMessage(), e);
                    }
                }
            }
        }
        response.setDiferencia(data.size() - count);
        response.setValid(count == data.size());
        logger.info("Validación de firmas completada. Total de firmas válidas: " + count);
        return response;
    }

    /**
     * Finaliza la transacción generando una firma (hash) basada en
     * id_transaccion y la fecha de finalización. Luego, guarda la firma en la
     * base de datos y la retorna.
     *
     * @param idTransaction El identificador de la transacción.
     * @return El hash generado como firma de la transacción.
     */
    public byte[] finishTransaction(String idTransaction) {
        try {
            // Convertir idTransaction a tipo long
            long idTransaccion = Long.parseLong(idTransaction);

            // Obtener la fecha y hora actual del sistema (fecha de finalización)
            Timestamp fechaFinalizacion = new Timestamp(System.currentTimeMillis());

            // Generar el hash a partir de idTransaccion y fechaFinalizacion
            byte[] hashFirma = generateHash(idTransaccion, fechaFinalizacion.toString());

            // Guardar el resultado de checkTransaction para evitar múltiples llamadas
            int transactionCheck = checkTransaction(idTransaction);
            System.out.println("checkTransaction(idTransaction, modulo) > " + transactionCheck);

            // Condición para verificar que no existe ninguna transacción con el mismo idTransaction y modulo
            if (transactionCheck == 0) {
                System.out.println("No existen transacciones previas, procediendo a guardar la firma.");

                // Guardar el hash en la base de datos en la tabla transaction_signature
                saveTransactionSignature(idTransaccion, hashFirma);
            } else {
                System.out.println("Ya existe una o más transacciones, no se guarda la firma.");
            }

            // Retornar el hash generado
            return hashFirma;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al finalizar la transacción: " + e.getMessage());
            return null;  // Retornar null en caso de error
        }
    }

    /**
     * Genera un hash utilizando id_transaccion y la fecha de finalización.
     *
     * @param idTransaccion El identificador de la transacción.
     * @param fechaFinalizacion La fecha de finalización de la transacción.
     * @return Un array de bytes con el hash generado.
     * @throws NoSuchAlgorithmException Si el algoritmo SHA-256 no está
     * disponible.
     */
    private byte[] generateHash(long idTransaccion, String fechaFinalizacion) throws NoSuchAlgorithmException {
        // Crear la cadena de datos combinando id_transaccion y fecha de finalización
        String data = "idTransaccion:" + idTransaccion + "|fechaFinalizacion:" + fechaFinalizacion;

        // Seleccionar el algoritmo SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Generar el hash como un array de bytes
        return digest.digest(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Guarda el hash de la transacción en la tabla transaction_signature.
     *
     * @param idTransaccion El identificador de la transacción.
     * @param hashFirma El hash generado para la firma.
     */
    private void saveTransactionSignature(long idTransaccion, byte[] hashFirma) {
        // Consulta SQL para la inserción
        String sql = "INSERT INTO transaction_signature (id_transaccion, hash_firma) VALUES (?, ?)";

        // Preparar los parámetros para la consulta
        Object[] params = new Object[]{idTransaccion, hashFirma};

        // Llamar al método genérico insert de genericSQL
        boolean isInserted = genericSQL.insert(sql, params);

        // Verificar si la inserción fue exitosa
        if (isInserted) {
            System.out.println("Firma de transacción guardada exitosamente.");
        } else {
            System.out.println("Error al guardar la firma de transacción.");
        }
    }

    public int checkTransaction(String idTransaccion) {
        // Primero, verifica si el id_transaccion ya existe
        String checkSql = stored.STORED_PROCEDURE_GET_AUTH_TRANSACTION_SIGN;
        // Creando objeto de inserción
        Object[] paramsCheck = {
            idTransaccion
        };

        // Ejecutar la consulta y obtener el ResultSet
        ResultSet rs = genericSQL.select(checkSql, paramsCheck);
        int count = 0;

        try {
            if (rs.next()) {
                count = rs.getInt("count");  // Obtener el conteo de registros
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al verificar el id_transaccion.");
        }
        System.out.println("count> " + count);
        return count;
    }

}
