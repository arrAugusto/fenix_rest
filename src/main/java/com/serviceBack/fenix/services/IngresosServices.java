package com.serviceBack.fenix.services;

import com.serviceBack.fenix.controllers.Code_OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.Utils.Send;
import com.serviceBack.fenix.Utils.SendMailIngresos;
import com.serviceBack.fenix.models.DetallesIngreso;
import com.serviceBack.fenix.models.GetDetalleIngreso;
import com.serviceBack.fenix.models.ItemsFail;
import commons.StoredProcedures;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static org.hibernate.bytecode.BytecodeLogging.LOGGER;
import org.springframework.jdbc.core.RowMapper;

@Service
public class IngresosServices implements IngresosInterfaces {

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored; // Nueva variable de instancia
    private final Send sendMail;
    private final SendMailIngresos sendMailIng;
    private final ResponseService response;

    @Autowired
    public IngresosServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
        this.sendMail = new Send();
        this.sendMailIng = new SendMailIngresos();
        this.response = new ResponseService();
    }

    @Override
    public ResponseService createIngresos(Ingresos ingreso) {

        String query = stored.STORE_PROCEDURE_CALL_INSERT_INGRESO + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
                .prepareStatement(query)) {
            preparedStatement.setString(1, ingreso.getUsuario());
            preparedStatement.setString(2, ingreso.getIdTransaccion());
            preparedStatement.setString(3, ingreso.getNit());
            preparedStatement.setString(4, ingreso.getCanalDigital());
            preparedStatement.setString(5, ingreso.getFechaOperativa());
            preparedStatement.setString(6, ingreso.getDocumento());
            preparedStatement.setString(7, ingreso.getCodigoQR());
            preparedStatement.setInt(8, ingreso.getBultos());
            preparedStatement.setDouble(9, ingreso.getCif());
            preparedStatement.setDouble(10, ingreso.getImpuestos());
            LOGGER.info(preparedStatement.toString());

            boolean queryResult = preparedStatement.execute();

            ResponseService res = resultCheckIngreso(response, queryResult, preparedStatement);
            if (!res.getCodeResponse().equals("00")) {
                return res;
            }
        } catch (SQLException e) {
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

        } catch (Exception e) {

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

        }

        return response;
    }

    @Override
    public ItemsFail crearItems(DetallesIngreso detalles) {
        ItemsFail itemsResponse = new ItemsFail();
        int errores = 0;
        int bultosItems = 0;
        for (int i = 0; i < detalles.getItems().size(); i++) {
            bultosItems += detalles.getItems().get(i).getBultos();
        }
        String messageItemsOk = "";
        if (bultosItems == detalles.getTotalBultos()) {

            String queryItems = stored.STORE_PROCEDURE_CALL_INSERT_ITEM + "(?,?,?,?)";
            for (int i = 0; i < detalles.getItems().size(); i++) {
                bultosItems = bultosItems + detalles.getItems().get(i).getBultos();

                try (PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
                        .prepareStatement(queryItems)) {
                    preparedStatement.setInt(1, Integer.parseInt(detalles.getIdTransaccion()));
                    preparedStatement.setInt(2, detalles.getIdUsuarioOperativo());
                    preparedStatement.setInt(3, detalles.getItems().get(i).getBultos());
                    preparedStatement.setString(4, detalles.getItems().get(i).getCliente());
                    LOGGER.info(preparedStatement.toString());

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected == 0) {
                        itemsResponse.setItemsFail(detalles.getItems());
                    } else {
                        errores += errores;

                        messageItemsOk += "\n" + (i + 1) + " : " + detalles.getItems().get(i).toString();
                    }
                    System.out.println("Rows affected: " + rowsAffected);
                } catch (Exception e) {

                }
            }
            if (errores > 0) {
                String messageItemsFail = "";
                for (int i = 0; i < itemsResponse.getItemsFail().size(); i++) {
                    messageItemsFail += "\n" + (i + 1) + " : " + itemsResponse.getItemsFail().get(i).toString();
                }
                // Construir el mensaje de error
                String errorMessage = "Se hizo insercción en la base de datos pero algunos items no fueron cargados exitosamente.\nData : \n\n" + detalles.toString() + "\n" + messageItemsFail + "";
                sendMailIng.sendMail(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage);
                LOGGER.info("Send mail" + errorMessage);

            } else {
                String messageItemsLoads = "Se insertaron todos los itmes exitosamente." + "\nData : \n\n" + detalles.toString() + "\n" + messageItemsOk + "";
                sendMailIng.sendMail(stored.mailTO, stored.mailFROM, stored.PWD, messageItemsLoads);
                LOGGER.info("Send mail" + messageItemsLoads);
            }
            itemsResponse.setCodeResponse("00");
            itemsResponse.setMessageResponse("Ok");

        } else {
            itemsResponse.setCodeResponse("09");
            itemsResponse.setMessageResponse("La suma de bultos no coincide con el total de bultos.");

        }
        System.out.println(detalles.getItems().toString());
        return itemsResponse;
    }

    public ResponseService resultCheckIngreso(ResponseService response, boolean queryResult, PreparedStatement preparedStatement) throws SQLException {
        if (queryResult) {
            try (ResultSet rs = preparedStatement.getResultSet()) {
                if (rs.next()) {
                    String responseValue = rs.getString("response");
                    switch (responseValue) {
                        case "0":
                            response.setCodeResponse("00");
                            response.setMessageResponse("Ingreso creado exitosamente");
                            response.setData("Ok");
                            break;
                        case "1":
                            response.setCodeResponse("09");
                            response.setMessageResponse("idTransaccion duplicado, por favor vuelva a intentar");
                            response.setData("Error");
                            break;
                        default:
                            response.setCodeResponse("09");
                            response.setMessageResponse("Error al intentar guardar la información intente de nuevo");
                            response.setData("Error");
                            break;
                    }
                }
            }
        } else {

            response.setCodeResponse("09");
            response.setMessageResponse("Error al intentar guardar la información intente de nuevo");
            response.setData("Error");
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
                // Your mapping logic here to convert ResultSet to GetDetalleIngreso object
                // For example:

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
}
