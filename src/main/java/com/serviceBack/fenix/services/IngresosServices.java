package com.serviceBack.fenix.services;

import com.serviceBack.fenix.Utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Utils.ResponseService;
import static com.serviceBack.fenix.Utils.SecureUniqueCodeGenerator.generateUniqueCode;
import com.serviceBack.fenix.Utils.Send;
import com.serviceBack.fenix.Utils.SendMailIngresos;
import com.serviceBack.fenix.models.DetallesIngreso;
import com.serviceBack.fenix.models.ErrorInfo;
import com.serviceBack.fenix.models.GetDetalleIngreso;
import com.serviceBack.fenix.models.ItemsFail;
import com.serviceBack.fenix.models.Product;
import commons.GenericResponse;
import commons.JsonReader;
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
    public ResponseService incomeWithdrawalService(Ingresos ingreso) {

        String query = stored.STORE_PROCEDURE_CALL_INSERT_INGRESO;

        try {
            PreparedStatement preparedStatement = prepareIncomeStatment.IncomeSQLPrepare(query, ingreso);
            boolean queryResult = preparedStatement.execute();
            ResponseService res = resultCheckIngreso(response, queryResult, preparedStatement);
            if (!res.getCodeResponse().equals("00")) {
                return res;
            }
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
    public ItemsFail crearItems(DetallesIngreso detalles) {
        String totalBultos = getIncomeBasic(stored.STORE_PROCEDURE_CALL_GET_TRANSACCION_INGRESO_INF, detalles.getIdTransaccion(), "total_bultos");
        String totalBultosItems = getIncomeBasic(stored.STORE_PROCEDURE_CALL_GET_ITEMS_TOTAL_BULTOS, detalles.getIdTransaccion(), "bultos");

        ItemsFail itemsResponse = new ItemsFail();

        if (Integer.parseInt(totalBultosItems) != Integer.parseInt(totalBultos)) {
            genericincomeItems(stored.STORE_PROCEDURE_DELETE_ITEMS_INCOME, detalles.getIdTransaccion());
        } else {
            genericTransactionIncome(stored.STORED_PROCEDURE_UPDATE_TRANSACTION_INCOME, detalles.getIdTransaccion(), "01", messageControll.MESSAGE_FENIX_DEFAULT);
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_02, messageControll.MESSAGE_FENIX_DEFAULT);
        }
        if (totalBultos.equals("NODATA")) {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_03, messageControll.MESSAGE_FENIX_DEFAULT);
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
                                .prepareStatement(stored.STORE_PROCEDURE_CALL_INSERT_ITEMS)) {
                            preparedStatement.setInt(1, Integer.parseInt(detalles.getIdTransaccion()));
                            preparedStatement.setInt(2, detalles.getIdUsuarioOperativo());
                            preparedStatement.setInt(3, detalles.getItems().get(i).getBultos());
                            preparedStatement.setInt(4, detalles.getItems().get(i).getBultosFaltantes());
                            preparedStatement.setInt(5, detalles.getItems().get(i).getBultosSobrantes());
                            preparedStatement.setInt(6, detalles.getItems().get(i).getBultos());
                            preparedStatement.setString(7, detalles.getItems().get(i).getCliente());
                            preparedStatement.setString(8, detalles.getItems().get(i).getDetalle());
                            preparedStatement.setString(9, "A");
                            preparedStatement.setString(10, "A");
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
                genericincomeItems(stored.STORE_PROCEDURE_DELETE_ITEMS_INCOME, detalles.getIdTransaccion());
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
                genericTransactionIncome(stored.STORED_PROCEDURE_UPDATE_TRANSACTION_INCOME, detalles.getIdTransaccion(), "01", "ITEMS REGISTRADOS");
                String messageItemsLoads = "Se insertaron todos los itmes exitosamente." + "\nData : \n\n" + detalles.toString() + "\n" + messageItemsOk + "";
                sendMailIng.sendMail(stored.mailTO, stored.mailFROM, stored.PWD, messageItemsLoads);
                LOGGER.info("Send mail" + messageItemsLoads);
                return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_00, messageItemsLoads);
            }

        } else {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_04, messageControll.MESSAGE_FENIX_DEFAULT);
        }
    }

    public ResponseService resultCheckIngreso(ResponseService response, boolean queryResult, PreparedStatement preparedStatement) throws SQLException {
        if (queryResult) {
            try (ResultSet rs = preparedStatement.getResultSet()) {
                if (rs.next()) {
                    String responseValue = rs.getString("response");
                    switch (responseValue) {
                        case "0":
                            generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_00, messageControll.MESSAGE_FENIX_DEFAULT);
                            break;
                        case "1":
                            generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_07, messageControll.MESSAGE_FENIX_DEFAULT);
                            break;
                        default:
                            generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_08, messageControll.MESSAGE_FENIX_DEFAULT);
                            break;
                    }
                }
            }
        } else {
            return generiResponse.GenericResponsError(messageControll.MESSAGE_FENIX_06, messageControll.MESSAGE_FENIX_DEFAULT);
        }

        return response;
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
