package com.serviceBack.fenix.services;

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
import commons.GenericResponse;
import commons.MessageControll;

import commons.StoredProcedures;
import java.sql.PreparedStatement;
import java.util.List;
import static org.hibernate.bytecode.BytecodeLogging.LOGGER;
import org.springframework.jdbc.core.RowMapper;
import sub_process.IncomeWithdrawal.Exceptions;
import sub_process.IncomeWithdrawal.PrepareIncomeStatment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class IngresosServices implements IngresosInterfaces {

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored; // Nueva variable de instancia
    private final Send sendMail;
    private final SendMailIngresos sendMailIng;
    private final ResponseService response;
    private final PrepareIncomeStatment prepareIncomeStatment;
    private final Exceptions exceptions;
    private final GenericResponse generiResponse;
    private final MessageControll messageControll;

    @Autowired
    public IngresosServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
        this.sendMail = new Send();
        this.sendMailIng = new SendMailIngresos();
        this.response = new ResponseService();
        this.prepareIncomeStatment = new PrepareIncomeStatment(this.jdbcTemplate); // Inicializa prepare directamente aquí
        this.exceptions = new Exceptions();
        this.generiResponse = new GenericResponse();
        this.messageControll = new MessageControll();
    }

    /*
        * INGRESOS Y RETIROS REGISTRO DE TRANSACCIONES
     */
    @Override
    public ResponseService incomeWithdrawalService(IncomeAndWithDrawal ingreso) {
        //revisando si existe la transaccion
        String cantidad_registros = getIncomeBasic(stored.STORED_PROCEDURE_CALL_CHECK_INCOME, ingreso.getP_numero_factura(), "cantidad_registros");
        if (Integer.parseInt(cantidad_registros) > 0) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_10, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        String id_transaccion = getIncomeBasic(stored.STORED_PROCEDURE_CALL_CHECK_ID_TRANSACCION, Integer.toString(ingreso.getP_id_transaccion()), "cantId_transaccion");
        if (Integer.parseInt(id_transaccion) > 0) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_12, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        
        try {
            PreparedStatement preparedStatement = prepareIncomeStatment.IncomeSQLPrepare(stored.STORED_PROCEDURE_CALL_INSERT_INGRESO, ingreso);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_11, messageControll.MESSAGE_FENIX_DEFAULT);
            }

            genericincomeItems(stored.STORED_PROCEDURE_CALL_UPDATE_INGRESO_EXITOSO, Integer.toString(ingreso.getP_id_transaccion()));
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
        String totalBultos = getIncomeBasic(stored.STORED_PROCEDURE_CALL_CHECK_INCOME_VALID, detalles.getId_ingreso(), "bultos");;//Bultos Ingreso
        String totalBultosItems = getIncomeBasic(stored.STORED_PROCEDURE_CALL_CHECK_TOTAL_BULTOS_ITEMS, detalles.getId_ingreso(), "total_bultos_items");//bultos items

        ItemsFail itemsResponse = new ItemsFail();

        if (totalBultos.equals("NODATA")) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_03, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        
        System.out.println("totalBultosItems> "+totalBultosItems);
        System.out.println("totalBultos> "+totalBultos);
        
        if (totalBultosItems.equals(totalBultos)) {
            genericincomeItems(stored.STORE_PROCEDURE_DELETE_ITEMS_INCOME, detalles.getId_ingreso());
        } else {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_02, messageControll.MESSAGE_FENIX_DEFAULT);
        }

        int errores = 0;
        int bultosItems = 0;
        for (int i = 0; i < detalles.getItems().size(); i++) {
            bultosItems += detalles.getItems().get(i).getBultos();
        }
        String messageItemsOk = "";
        if (Integer.parseInt(totalBultos) == bultosItems) {
            for (int i = 0; i < detalles.getItems().size(); i++) {
                bultosItems = bultosItems + detalles.getItems().get(i).getBultos();
                try (
                        PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
                                .prepareStatement(stored.STORED_PROCEDURE_CALL_INSERT_ITEMS)) {
                            preparedStatement.setInt(1, Integer.parseInt(detalles.getId_ingreso()));
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
                genericincomeItems(stored.STORE_PROCEDURE_DELETE_ITEMS_INCOME, detalles.getId_ingreso());
                String messageItemsFail = "";
                for (int i = 0; i < itemsResponse.getItemsFail().size(); i++) {
                    messageItemsFail += "\n" + (i + 1) + " : " + itemsResponse.getItemsFail().get(i).toString();
                }
                // Construir el mensaje de error
                String errorMessage = "LOS ITEMS NO FUERON REGISTRADOS EN LA BASE DATOS, CORRIGA E INTENTE DE NUEVO.\nData : \n\n" + detalles.toString() + "\n" + messageItemsFail;
                sendMailIng.sendMail(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage);
                LOGGER.info("Send mail" + errorMessage);
                return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_05, errorMessage);
            } else {
                genericTransactionIncome(stored.STORED_PROCEDURE_UPDATE_TRANSACTION_INCOME, detalles.getId_ingreso(), "01", "ITEMS REGISTRADOS");
                String messageItemsLoads = "Se insertaron todos los itmes exitosamente." + "\nData : \n\n" + detalles.toString() + "\n" + messageItemsOk + "";
                sendMailIng.sendMail(stored.mailTO, stored.mailFROM, stored.PWD, messageItemsLoads);
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
            arribo_detalles.getIdItem(),
            arribo_detalles.getBultos(),
            arribo_detalles.getFechaRegistro(),
            arribo_detalles.getDetalle(),
            arribo_detalles.getAverias(),
            arribo_detalles.getArea(),
            arribo_detalles.getCodigolectura(),
            true
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
            geoUbicacion.getP_codigo_lectura()
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

    //MODIFICAR DE GEOUBICACION
    @Override
    public ItemsFail incomeModGeoUbicacionService(GeoUbicacion geoUbicacion) {
        String id_arribo = "";
        if (Integer.toString(geoUbicacion.getP_id_arribo()).isEmpty()) {
            id_arribo = getIncomeBasic(stored.STORED_PROCEDURE_CALL_GET_ID_GEO_UBICACION, geoUbicacion.getP_codigo_lectura(), "id");

        } else {
            id_arribo = Integer.toString(geoUbicacion.getP_id_arribo());
        }

        //Creando objeto de inserccion
        Object[] params = {
            id_arribo,
            geoUbicacion.getP_pasicionx(),
            geoUbicacion.getP_posiciony(),
            geoUbicacion.getP_geoposicion()
        };
        //Insertando el detalle de mercaderia
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
        String queryGetItem = stored.STORE_PROCEDURE_CALL_GET_ITEMS + "(?)";
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
                    response.setData("Error");
                    // Obtener información sobre la clase y la línea
                    StackTraceElement callerInfo = Thread.currentThread().getStackTrace()[1];
                    String fileName = callerInfo.getFileName();
                    String className = callerInfo.getClassName();
                    int lineNumber = callerInfo.getLineNumber();

                    // Construir el mensaje de error
                    String errorMessage = "Error interno del servidor: [fileName: '" + fileName + "', className: '" + className
                            + "', lineNumber: " + lineNumber + ", errorMessage: '" + e.getMessage() + "']";

                    // Enviar correo electrónico de alerta
                    sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage);
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
}
