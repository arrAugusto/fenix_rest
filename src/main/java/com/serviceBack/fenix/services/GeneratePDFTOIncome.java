package com.serviceBack.fenix.services;

import com.serviceBack.fenix.Utils.CreateToPDFWithPDF;
import com.serviceBack.fenix.Utils.DomainsEnv;
import com.serviceBack.fenix.Utils.GenericSQL;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.interfaces.HtmlPdfInterfaces;
import com.serviceBack.fenix.models.Comprobante;
import com.serviceBack.fenix.models.ConfigFirmas;
import com.serviceBack.fenix.models.pdf.PDF_Income_Title;
import commons.Const_env;
import commons.StoredProcedures;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GeneratePDFTOIncome implements HtmlPdfInterfaces {

    private final Const_env const_env;
    private final GenericSQL genericSQL;
    private final StoredProcedures stored;
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = Logger.getLogger(AuthTransactionService.class.getName());

    @Autowired
    public GeneratePDFTOIncome(GenericSQL genericSQL) {
        this.genericSQL = genericSQL;
        this.const_env = new Const_env();
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor        
        this.jdbcTemplate = new JdbcTemplate();
    }

    public byte[] generatePdfFromHtml(String id_transaction) {
        ResultSet resultSet = null;
        CreateToPDFWithPDF createPDF = new CreateToPDFWithPDF(); // Crear instancia de la clase de PDF

        TransoformGetConfig transformConfig = new TransoformGetConfig();
        try {
            Object[] paramsCheck = {
                id_transaction,
                "1"
            };

            resultSet = genericSQL.select(stored.STORED_PROCEDURE_GET_CHECK_VALID_COMPROBANTE, paramsCheck);

            Comprobante valid = transformConfig.transformDataValidTransaction(resultSet);
            if (valid != null) {
                System.out.println("PDF ya existe....");
                // Generar el PDF utilizando la lista completa de PDF_Income_Title
                byte[] pdfData = createPDF.createPDFWithHTML(valid.getComprobante());

                // Devolver el PDF generado en formato byte[]
                return pdfData;
            }

        } catch (Exception e) {

        }
        Object[] paramsConfig = {
            const_env.CALL_INGRESO_NORMAL
        };

        try {
            logger.info("Ejecutando consulta para obtener la configuración de firmas.");
            resultSet = genericSQL.select(stored.STORED_PROCEDURE_CALL_GET_CONFIG_FIRMAS, paramsConfig);

            List< ConfigFirmas> data = transformConfig.transformData(resultSet);
            logger.info("Se obtuvo la configuración de firmas: " + data.size() + " registros encontrados.");
            System.out.println(data.toString());
            Object[] param_id_transaction = {
                id_transaction
            };
            List< PDF_Income_Title> pdfIncome = new ArrayList<>(); // Inicializar la lista

            for (ConfigFirmas configFirma : data) {
                String sqlQuery = configFirma.getSql_required();
                // Verificar si la consulta SQL está vacía o nula
                if (sqlQuery == null || sqlQuery.trim().isEmpty()) {
                    logger.warning("La consulta SQL está vacía o nula para la configuración de firmas: " + configFirma.toString());
                    continue;

                }

                // Imprimir consulta y parámetros para depuración
                System.out.println("Ejecutando consulta: " + sqlQuery);
                System.out.println("Parámetros de la consulta: " + Arrays.toString(param_id_transaction));

                // Ejecutar la consulta SQL
                ResultSet resultSetConfig = genericSQL.select(sqlQuery, param_id_transaction);

                if (resultSetConfig == null) {
                    logger.warning("El ResultSet para la consulta " + sqlQuery + " es nulo.");
                    continue;
                }

                // Verificar si el ResultSet tiene datos
                if (!resultSetConfig.isBeforeFirst()) {
                    logger.warning("El ResultSet para la consulta " + sqlQuery + " está vacío.");
                    resultSetConfig.close(); // Cerrar el ResultSet vacío
                    continue;
                }

                // Procesar el ResultSet
                List< PDF_Income_Title> pdfIncomeList = transformConfig.transformDataConfig(resultSetConfig, configFirma.getInfo_print());
                pdfIncome.addAll(pdfIncomeList);
                resultSetConfig.close();
            }

            // Verificar si la lista de pdfIncome no está vacía y generar el PDF
            if (!pdfIncome.isEmpty()) {
                // Generar el PDF utilizando la lista completa de PDF_Income_Title
                byte[] pdfData = createPDF.createToNewPDF(pdfIncome, id_transaction, genericSQL, stored);

                // Devolver el PDF generado en formato byte[]
                return pdfData;
            } else {
                logger.warning("La lista de ingresos PDF está vacía, no se generará ningún PDF.");
                return null;
            }

        } catch (Exception e) {
            logger.severe("Error al generar el PDF: " + e.getMessage());
            e.printStackTrace();
            return null; // Retorna null en caso de error
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.severe("Error al cerrar el ResultSet: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public ResponseService<Comprobante> getPDFTransaction(String idTransaccion) {
        ResponseService<Comprobante> response = new ResponseService<>(); // Asegúrate de tipar el ResponseService correctamente
        TransoformGetConfig transformConfig = new TransoformGetConfig();

        try {
            generatePdfFromHtml(idTransaccion);

            Comprobante valid = getDataPDF(idTransaccion, "1");
            if (valid != null) {
                // Devolver el PDF generado en formato byte[] (si es necesario)
                response.setCodeResponse("00");  // Código de éxito
                response.setMessageResponse("ÉXITO");  // Mensaje de éxito
                response.setData(Arrays.asList(valid));  // Agregar el comprobante a la lista de data
            } else {
                // Si no hay comprobante válido
                response.setCodeResponse("01");
                response.setMessageResponse("No se encontró un comprobante válido para la transacción proporcionada.");
            }

        } catch (Exception e) {
            // Manejar la excepción adecuadamente
            response.setCodeResponse("99");
            response.setMessageResponse("Error al procesar la transacción: " + e.getMessage());
            e.printStackTrace();  // Opcional: para ver el error completo en la consola
        }

        return response;
    }

    @Override
    public byte[] view_pdfGenerated(String validator) {
        ResponseService<Comprobante> response = new ResponseService<>();
        TransoformGetConfig transformConfig = new TransoformGetConfig();
        CreateToPDFWithPDF errorsPDF = new CreateToPDFWithPDF();

        try {
            // Parámetros para la consulta
            Object[] paramsCheck = {
                validator,
                "1"
            };

            // Ejecutar la consulta en la base de datos
            ResultSet resultSet = null;
            try {
                resultSet = genericSQL.select(stored.STORED_PROCEDURE_GET_PDF_COMPROBANTE, paramsCheck);
                System.out.println("resultSet> " + resultSet);

                // Transformar los datos obtenidos del ResultSet a un objeto Comprobante
                Comprobante valid = transformConfig.transformDataValidTransaction(resultSet);
                if (valid.getIdTransaction() == null) {
                    return errorsPDF.generateErrorPDF("Error al generar PDF", "No se pudo generar el PDF para la transacción solicitada. \n" + validator);
                }

                // Generar el PDF con los datos de la transacción válida
                return generatePdfFromHtml(valid.getIdTransaction());

            } finally {
                // Asegurar que el ResultSet se cierra adecuadamente
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            // Manejar la excepción adecuadamente
            response.setCodeResponse("99");
            response.setMessageResponse("Error al procesar la transacción: " + e.getMessage());
            e.printStackTrace();  // Para ver el error completo en la consola

            // Retornar PDF con mensaje de error
            return errorsPDF.generateErrorPDF("Error al generar PDF", "No se pudo generar el PDF para la transacción solicitada.");
        }
    }
    
    @Override
    public Comprobante getDataPDF(String idTransaccion, String estado) {
        TransoformGetConfig transformConfig = new TransoformGetConfig();
        Object[] paramsCheck = {
            idTransaccion,
            estado
        };
        System.out.println("stored.STORED_PROCEDURE_GET_CHECK_VALID_COMPROBANTE> " + stored.STORED_PROCEDURE_GET_CHECK_VALID_COMPROBANTE);
        ResultSet resultSet = genericSQL.select(stored.STORED_PROCEDURE_GET_CHECK_VALID_COMPROBANTE, paramsCheck);

        // Transformar los datos obtenidos del ResultSet a un objeto Comprobante
        Comprobante valid = transformConfig.transformDataValidTransaction(resultSet);

        return valid;
    }
}
