package com.serviceBack.fenix.services;

import com.serviceBack.fenix.services.subService.RegisterProducts;
import com.serviceBack.fenix.Utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
import com.serviceBack.fenix.Utils.ResponseService;
import static com.serviceBack.fenix.Utils.SecureUniqueCodeGenerator.generateUniqueCode;
import com.serviceBack.fenix.Utils.Send;
import com.serviceBack.fenix.Utils.SendMailIngresos;
import com.serviceBack.fenix.models.ingresos.Detalle_Mercancias;
import com.serviceBack.fenix.models.ingresos.DetallesIngreso;
import com.serviceBack.fenix.models.ingresos.GeoUbicacion;
import com.serviceBack.fenix.models.ingresos.GetDetalleIngreso;
import com.serviceBack.fenix.models.ingresos.ItemsFail;
import com.serviceBack.fenix.models.Product;
import com.serviceBack.fenix.models.ingresos.GetDataIngresoArribo;
import com.serviceBack.fenix.models.ingresos.IngresosPendientes;
import com.serviceBack.fenix.services.subService.RegisterLocation;
import commons.CommonsLogic;
import commons.GenericResponse;
import commons.MessageControll;

import commons.StoredProcedures;
import commons.UXMessages;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import sub_process.IncomeWithdrawal.Exceptions;
import sub_process.IncomeWithdrawal.PrepareIncomeStatment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IngresosServices implements IngresosInterfaces {

    private String MENSAJE_MAIL = "";
    private String SUBJECT = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(Exceptions.class);
    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored; // Nueva variable de instancia
    private final Send sendMail;
    private final CommonsLogic commmons;
    private final UXMessages uxMessages;
    private final ResponseService response;
    private final PrepareIncomeStatment prepareIncomeStatment;
    private final GenericResponse generiResponse;
    private final MessageControll messageControll;
    private Exceptions exceptions;
    @Autowired
    private SendMailIngresos sendMailIng;

    @Autowired
    public IngresosServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
        this.sendMail = new Send();
        this.response = new ResponseService();
        this.prepareIncomeStatment = new PrepareIncomeStatment(this.jdbcTemplate); // Inicializa prepare directamente aquí
        this.generiResponse = new GenericResponse();
        this.messageControll = new MessageControll();
        this.exceptions = exceptions; // Inyección a través del constructor
        this.commmons = new CommonsLogic();
        this.uxMessages = new UXMessages();

    }
    @Autowired
    private RegisterProducts registerProducts;
    @Autowired
    private RegisterLocation registerLocation;

    /*
        * INGRESOS Y RETIROS REGISTRO DE TRANSACCIONES
     */
    @Override
    public ResponseService incomeWithdrawalService(IncomeAndWithDrawal ingreso) {
        this.MENSAJE_MAIL = this.uxMessages.INGRESO_EXITOSO + ingreso.getNumero_factura();
        this.SUBJECT = this.uxMessages.SUBJECT_DEFAULT;
        String count_trx = getIncomeBasic(stored.STORED_PROCEDURE_CALL_GET_COUNT_TRX_KIMBO, ingreso.getNumero_factura(), "cantTrx");

        if (!count_trx.equals("0")) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_14, messageControll.MESSAGE_FENIX_DEFAULT);
        }

        //revisando si existe la transaccion
        String cantidad_registros = getIncomeBasic(stored.STORED_PROCEDURE_CALL_CHECK_INCOME, ingreso.getNumero_factura(), "cantidad_registros");
        if (Integer.parseInt(cantidad_registros) > 0) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_10, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        String id_transaccion = getIncomeBasic(stored.STORED_PROCEDURE_CALL_CHECK_ID_TRANSACCION, ingreso.getId_transaccion(), "cantId_transaccion");
        if (Integer.parseInt(id_transaccion) > 0) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_12, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        System.out.println(ingreso.toString());
        //Create income

        try {
            PreparedStatement preparedStatement = prepareIncomeStatment.IncomeSQLPrepare(stored.STORED_PROCEDURE_CALL_INSERT_INGRESO, ingreso);//Inserting income

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_11, messageControll.MESSAGE_FENIX_DEFAULT);
            }

            genericincomeItems(stored.STORED_PROCEDURE_CALL_UPDATE_INGRESO_EXITOSO, ingreso.getId_transaccion());
            switch (ingreso.getCodigo_transaccion()) {
                case CommonsLogic.TRANSACTION_02_INCOME_DETAILS://if transaction 02 details registrer
                    try {
                    registerProducts.registerProduct(ingreso);
                    this.MENSAJE_MAIL = this.uxMessages.DETALLE_EXITOSO.concat(" ").concat(ingreso.getNombre());
                    this.SUBJECT = this.uxMessages.SUBJECT_DETALLE_EXITOSO;
                    break;

                } catch (Error e) {
                    System.out.println("error :" + e.getMessage());
                }
                case CommonsLogic.TRANSACTION_03_REGISTER_LOCATION://if transaction 03 register location
                    registerLocation.registerLocation(ingreso);
                    this.MENSAJE_MAIL = this.uxMessages.UBACION_EXITOSA.concat(" ").concat(ingreso.getNombre());
                    this.SUBJECT = this.uxMessages.SUBJECT_UBACION_EXITOSO;

                    break;
                default:
                    this.MENSAJE_MAIL = this.uxMessages.INGRESO_EXITOSO;
                    this.SUBJECT = this.uxMessages.SUBJECT_INGRESO_EXITOSO;

                    break;
            }
            // Enviar correo electrónico de alerta
            sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, this.MENSAJE_MAIL, this.SUBJECT);

            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_00, messageControll.MESSAGE_FENIX_DEFAULT);

        } catch (SQLException e) {
            exceptions.handleSQLException(e);
        } catch (Exception e) {
            exceptions.handleGenericException(e);
        }

        return response;
    }

    /*
            * CREAR ITEMS INCOME
     */
    @Override
    public ItemsFail incomeItemsService(DetallesIngreso detalles) {
        String totalBultos = getIncomeBasic(stored.STORED_PROCEDURE_CALL_CHECK_INCOME_VALID, detalles.getId_transaccion(), "bultos");
        String totalBultosItems = getIncomeBasic(stored.STORED_PROCEDURE_CALL_CHECK_TOTAL_BULTOS_ITEMS, detalles.getId_transaccion(), "total_bultos_items");
        System.out.println("detalles.getId_transaccion()> " + detalles.getId_transaccion());
        System.out.println("totalBultos> " + totalBultos);
        System.out.println("totalBultosItems > " + totalBultosItems);

        int totalBultosValue = 0;
        int totalBultosItemsValue = 0;

        try {
            totalBultosValue = (int) Double.parseDouble(totalBultos.trim());
            totalBultosItemsValue = (int) Double.parseDouble(totalBultosItems.trim());

            System.out.println("totalBultos: " + totalBultosValue);
            System.out.println("totalBultosItems: " + totalBultosItemsValue);
        } catch (NumberFormatException e) {
            System.err.println("Error al analizar la entrada: " + e.getMessage());
        }

        ItemsFail itemsResponse = new ItemsFail();

        if ((totalBultosItemsValue != totalBultosValue)) {
            genericincomeItems(stored.STORED_PROCEDURE_DELETE_ITEMS_INCOME, detalles.getId_transaccion());
        } else {
            if (totalBultosItemsValue == 0.00) {
                return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_02, messageControll.MESSAGE_FENIX_DEFAULT);
            }
        }
        if (totalBultosValue == 0) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_03, messageControll.MESSAGE_FENIX_DEFAULT);
        }

        int errores = 0;
        int bultosItems = 0;
        for (int i = 0; i < detalles.getItems().size(); i++) {
            bultosItems += detalles.getItems().get(i).getBultos();
        }
        String messageItemsOk = "";
        if (totalBultosValue == bultosItems) {
            for (int i = 0; i < detalles.getItems().size(); i++) {
                bultosItems = bultosItems + detalles.getItems().get(i).getBultos();
                try (
                        PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
                                .prepareStatement(stored.STORED_PROCEDURE_CALL_INSERT_ITEMS)) {
                            preparedStatement.setInt(1, Integer.parseInt(detalles.getId_transaccion()));
                            preparedStatement.setInt(2, detalles.getIdUsuarioOperativo());
                            preparedStatement.setString(3, detalles.getItems().get(i).getCliente());
                            preparedStatement.setInt(4, detalles.getItems().get(i).getBultos());
                            preparedStatement.setDouble(5, detalles.getItems().get(i).getValorUnitario());
                            preparedStatement.setString(6, detalles.getItems().get(i).getDetalle());
                            preparedStatement.setString(7, "A");
                            LOGGER.info(preparedStatement.toString());
                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected == 0) {
                                itemsResponse.setItemsFail(detalles.getItems());
                            } else {
                                errores += errores;
                                messageItemsOk += "\n" + (i + 1) + " : " + detalles.getItems().get(i).toString();
                            }
                            LOGGER.info("Rows affected: " + rowsAffected);
                        } catch (Exception e) {
                            LOGGER.info("error " + e);
                        }
            }
            if (errores > 0) {
                genericincomeItems(stored.STORED_PROCEDURE_DELETE_ITEMS_INCOME, detalles.getId_transaccion());
                String messageItemsFail = "";
                for (int i = 0; i < itemsResponse.getItemsFail().size(); i++) {
                    messageItemsFail += "\n" + (i + 1) + " : " + itemsResponse.getItemsFail().get(i).toString();
                }
                // Construir el mensaje de error
                String errorMessage = "LOS ITEMS NO FUERON REGISTRADOS EN LA BASE DATOS, CORRIGA E INTENTE DE NUEVO.\nData : \n\n" + detalles.toString() + "\n" + messageItemsFail;
                sendMailIng.sendMail(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage, "ERROR");
                LOGGER.info("Send mail" + errorMessage);
                return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_05, errorMessage);
            } else {
                genericincomeItems(stored.STORED_PROCEDURE_UPDATE_ITEMS_INCOME, detalles.getId_transaccion());
                String messageItemsLoads = "Se insertaron todos los itmes exitosamente." + "\nData : \n\n" + detalles.toString() + "\n" + messageItemsOk + "";
                sendMailIng.sendMail(stored.mailTO, stored.mailFROM, stored.PWD, messageItemsLoads, "ERROR");
                LOGGER.info("Send mail" + messageItemsLoads);
                return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_00, messageItemsLoads);
            }

        } else {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_04, messageControll.MESSAGE_FENIX_DEFAULT);
        }
    }

    //Ingreso de detalle de mercaderias
    @Override
    public ItemsFail incomeDetailsService(Detalle_Mercancias arribo_detalles) {
        //Creando objeto de inserccion
        Object[] params = {
            arribo_detalles.getId_transaction(),
            arribo_detalles.getBultos(),
            arribo_detalles.getDetalle(),
            arribo_detalles.getAverias(),
            arribo_detalles.getArea(),
            arribo_detalles.getCodigolectura()
        };
        //Insertando el detalle de mercaderia
        int filasAfectadas = jdbcTemplate.update(stored.STORED_PROCEDURE_CALL_INSERT_DETAILS_ARRIBO_BODEGA, params);
        //Si no se inserto el registro retornar error
        if (filasAfectadas == 0) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_09, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        //Retorno de exito
        return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_00, messageControll.MESSAGE_FENIX_DEFAULT);
    }

    //NUEVA GEOPOSICION
    @Override
    public ItemsFail incomeGeoUbicacionService(GeoUbicacion geoUbicacion) {
        //Creando objeto de inserccion
        Object[] params = {
            geoUbicacion.getP_id_arribo(),
            geoUbicacion.getP_pasicionx(),
            geoUbicacion.getP_posiciony(),
            geoUbicacion.getP_geoposicion(),
            geoUbicacion.getP_codigo_lectura(),
            geoUbicacion.getP_descripcion(),
            geoUbicacion.getP_caracteristicas()

        };
        //Insertando el detalle de mercaderia
        int filasAfectadas = jdbcTemplate.update(stored.STORED_PROCEDURE_CALL_INSERT_GEOPOSICION_UBICACION, params);
        //Si no se inserto el registro retornar error
        if (filasAfectadas == 0) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_09, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        //Retorno de exito
        return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_00, messageControll.MESSAGE_FENIX_DEFAULT);
    }

// GET INGRESOS
    @Override
    public ResponseService getIngresosPendientes() {
        ResponseService response = new ResponseService();

        try {
            // Ejecutar la consulta y mapear los resultados a una lista de IngresosPendientes
            List<IngresosPendientes> ingresosPendientes = jdbcTemplate.query(
                    stored.STORED_PROCEDURE_CALL_GET_INGRESOS_INIT_PENDIENTES,
                    new RowMapper<IngresosPendientes>() {
                @Override
                public IngresosPendientes mapRow(ResultSet rs, int rowNum) throws SQLException {
                    IngresosPendientes ingresoPendiente = new IngresosPendientes();
                    ingresoPendiente.setId_transaccion(rs.getString("id_transaccion"));
                    ingresoPendiente.setNumeroFactura(rs.getString("numero_factura"));
                    ingresoPendiente.setFecha(rs.getString("fecha")); // O usa `rs.getDate()` si el campo es una fecha
                    ingresoPendiente.setBultos(rs.getInt("bultos"));
                    ingresoPendiente.setValor(rs.getDouble("valor"));
                    ingresoPendiente.setEstado(rs.getString("estado") != null ? rs.getString("estado") : "Pendiente");

                    return ingresoPendiente;
                }
            }
            );

            // Establecer la lista de ingresos pendientes en la respuesta
            response.setData(ingresosPendientes);
            response.setCodeResponse("00"); // Código de éxito
            response.setMessageResponse("Consulta exitosa");

        } catch (DataAccessException e) {
            // Manejo de excepción de acceso a datos
            response.setCodeResponse("99"); // Código de error
            response.setMessageResponse("Error al consultar ingresos pendientes: " + e.getMessage());
            e.printStackTrace(); // Opcional: imprimir la traza de la excepción para depuración
        }

        return response; // Retornar la respuesta con los datos mapeados o el error
    }

    //MODIFICAR DE GEOUBICACION
    @Override
    public ItemsFail incomeModGeoUbicacionService(GeoUbicacion geoUbicacion) {
        String asistencia = getIncomeBasic(stored.STORED_PROCEDURE_CALL_GET_REQUIRED_ASIST, geoUbicacion.getP_codigo_lectura(), "asistencia");

        System.out.println("asistencia> " + asistencia);
        if (Integer.parseInt(asistencia) > 1) {
            String id_arribo = getIncomeBasic(stored.STORED_PROCEDURE_CALL_GET_ID_ARRIBO, geoUbicacion.getP_codigo_lectura(), "id");
            if (!id_arribo.equals("NODATA")) {
                //getAllDataIngreso
                List<GetDataIngresoArribo> allData = getAllDataIngreso(Integer.parseInt(id_arribo));
                String errorMessage = "REVISA LAS TAREAS DE UBICACION Y CONFIRMA APROBANDO EL CAMBIO.\nData : \n\n" + allData.toString();
                System.out.println("errorMessage> " + errorMessage);
                sendMailIng.sendMail(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage, "ERROR");
                LOGGER.info("Send mail" + errorMessage);
            }
        }
        //Creando objeto de inserccion
        Object[] params = {
            geoUbicacion.getP_pasicionx(),
            geoUbicacion.getP_posiciony(),
            geoUbicacion.getP_geoposicion(),
            geoUbicacion.getP_descripcion(),
            geoUbicacion.getP_caracteristicas(),
            geoUbicacion.getP_codigo_lectura()
        };
        //Insertando el detalle de mercaderia
        System.out.println("stored.STORED_PROCEDURE_CALL_UPDATE_GEO_UBICACION> " + stored.STORED_PROCEDURE_CALL_UPDATE_GEO_UBICACION);
        System.out.println("params > " + params.toString());
        int filasAfectadas = jdbcTemplate.update(stored.STORED_PROCEDURE_CALL_UPDATE_GEO_UBICACION, params);
        //Si no se inserto el registro retornar error
        if (filasAfectadas == 0) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_09, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        //Retorno de exito
        return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_00, messageControll.MESSAGE_FENIX_DEFAULT);
    }

    /*
                           
     */
    @Override
    public List<GetDetalleIngreso> getItems(String idTransaccion) {
        String queryGetItem = stored.STORED_PROCEDURE_CALL_GET_ITEMS + "(?)";
        return jdbcTemplate.query(queryGetItem, new Object[]{idTransaccion}, new RowMapper<GetDetalleIngreso>() {
            @Override
            public GetDetalleIngreso mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    GetDetalleIngreso detalleIngreso = new GetDetalleIngreso();
                    detalleIngreso.setIdTrasaccionItem(rs.getString("idTransaccion"));
                    detalleIngreso.setFechaOperativaItem(rs.getString("fecha_operativa"));
                    detalleIngreso.setDocumentoItem(rs.getString("documento"));
                    detalleIngreso.setCodigoQRItem(rs.getString("codigo_QR"));
                    detalleIngreso.setTotalBultosItem(Integer.parseInt(rs.getString("total_bultos")));
                    detalleIngreso.setTotalCifItem(Float.parseFloat(rs.getString("total_cif")));
                    detalleIngreso.setTotalImpuestosItem(Float.parseFloat(rs.getString("total_impuestos")));
                    detalleIngreso.setBultosItems(Integer.parseInt(rs.getString("bultos")));
                    detalleIngreso.setCliente(rs.getString("cliente"));
                    // Set other properties as needed
                    return detalleIngreso;
                } catch (Exception e) {
                    LOGGER.error("Error al procesar el resultado del ResultSet", e);
                    // Resto del código...

                    response.setCodeResponse("500");
                    response.setMessageResponse("Error interno en el servidor " + e.getMessage());
                    // Obtener información sobre la clase y la línea
                    StackTraceElement callerInfo = Thread.currentThread().getStackTrace()[1];
                    String fileName = callerInfo.getFileName();
                    String className = callerInfo.getClassName();
                    int lineNumber = callerInfo.getLineNumber();

                    // Construir el mensaje de error
                    String errorMessage = "Error interno del servidor: [fileName: '" + fileName + "', className: '" + className
                            + "', lineNumber: " + lineNumber + ", errorMessage: '" + e.getMessage() + "']";

                    // Enviar correo electrónico de alerta
                    sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage, "ERROR INTERNO");
                    // Registrar el error en el log
                    LOGGER.info(errorMessage);

                    return null;
                }
            }
        });
    }

    @Override
    public String createProduct(Product product) {
        String uniqueCode = generateUniqueCode();
        Object[] params = new Object[]{
            product.getIdItem(),
            product.getIdUser(),
            product.getUser(),
            product.getBultosProd(),
            product.getDescriptionProd(),
            product.getTypeProd(),
            product.getStatus(),
            uniqueCode
        };
        int result = jdbcTemplate.update(stored.STORED_PROCEDURE_CALL_INSERT_PRODUCT, params);
        if (result > 0) {
            System.out.println("uniqueCode> " + uniqueCode);
            QRCodeGenerator qrCode = new QRCodeGenerator();

            String filePath = "C:\\Users\\agr12\\Desktop\\codes/product_qrcode.png"; // Nombre del archivo de la imagen QR

            int width = 300; // Ancho de la imagen QR
            int height = 300; // Altura de la imagen QRs

            qrCode.generateQRCode(uniqueCode, filePath, width, height);
            System.out.println("Código QR generado con éxito en: " + filePath);
            return "Ok";
        }
        return null;
    }

    public String getIncomeBasic(String query, String idTransaccion, String nameColumn) {
        return jdbcTemplate.query(query, new Object[]{idTransaccion}, new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getString(nameColumn);
                }
                return "NODATA"; // O maneja el caso donde no hay resultados
            }
        });
    }

    public int genericincomeItems(String query, String id) {
        int rowsAffected = jdbcTemplate.update(query, id);
        return rowsAffected;
    }

    public int genericTransactionIncome(String query, String id, String codeResp, String messageResp) {
        int rowsAffected = jdbcTemplate.update(query, id, codeResp, messageResp);
        return rowsAffected;
    }

    public List<GetDataIngresoArribo> getAllDataIngreso(int id_arribo) {
        return jdbcTemplate.query(stored.STORED_PROCEDURE_CALL_GET_ALL_DATA_ARRIBO, new Object[]{id_arribo}, new ResultSetExtractor<List<GetDataIngresoArribo>>() {
            @Override
            public List<GetDataIngresoArribo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<GetDataIngresoArribo> dataList = new ArrayList<>();

                while (rs.next()) {
                    GetDataIngresoArribo data = new GetDataIngresoArribo();
                    data.setNombre_empresa(rs.getString("nombre_empresa"));
                    data.setNumero_factura(rs.getString("numero_factura"));
                    data.setFecha_ingreso_bodega(rs.getString("fecha_ingreso_bodega"));
                    data.setItem(rs.getString("item"));
                    data.setBultos(rs.getString("bultos"));
                    data.setDetalle(rs.getString("detalle"));
                    data.setAverias(rs.getString("averias"));
                    data.setCodigo_lectura(rs.getString("codigo_lectura"));
                    data.setPosicion_X(rs.getString("posicion_X"));
                    data.setPosicion_Y(rs.getString("posicion_Y"));
                    data.setCoordenadas(rs.getString("coordenadas"));
                    data.setDescripcion(rs.getString("descripcion"));
                    data.setFecha_actualizacion(rs.getString("fecha_actualizacion"));
                    dataList.add(data);
                }
                return dataList;
            }
        });
    }
}
