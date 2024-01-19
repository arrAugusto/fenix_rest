package com.serviceBack.fenix.services;

import com.serviceBack.fenix.controllers.Code_OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.Utils.Send;
import commons.StoredProcedures;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.hibernate.bytecode.BytecodeLogging.LOGGER;

@Service
public class IngresosServices implements IngresosInterfaces {

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored; // Nueva variable de instancia

    @Autowired
    public IngresosServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
    }

    @Override
    public ResponseService createIngresos(Ingresos ingreso) {
        ResponseService response = new ResponseService();

        Send sendMail = new Send();
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

            boolean queryResult = preparedStatement.execute();

            ResponseService res = resultCheck(response, queryResult, preparedStatement);
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
    public String createOTPIng(Code_OTP code_OTP) {
        ResponseService response = new ResponseService();

        StoredProcedures stored = new StoredProcedures();
        // Crear un objeto StringBuilder
        StringBuilder queryString = new StringBuilder();
        queryString.append(stored.STORE_PROCEDURE_CALL_INSERT_OTP_CODE).append("('").append(code_OTP.getIdTransaccion())
                .append("','").append(code_OTP.getArea()).append("')");
        System.out.println("queryString>> " + queryString.toString());
        response.setCodeResponse("00");
        response.setMessageResponse("Ingreso creado exitosamente");
        response.setData("Ok");

        return "hola";
    }

    public ResponseService resultCheck(ResponseService response, boolean queryResult, PreparedStatement preparedStatement) throws SQLException {
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
}
