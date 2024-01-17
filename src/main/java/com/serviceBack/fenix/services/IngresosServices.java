package com.serviceBack.fenix.services;

import com.serviceBack.fenix.controllers.Code_OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Utils.ResponseService;
import commons.StoredProcedures;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Service
public class IngresosServices implements IngresosInterfaces {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IngresosServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseService createIngresos(Ingresos ingreso) {
        ResponseService response = new ResponseService();

        StoredProcedures stored = new StoredProcedures();

        String query = stored.STORE_PROCEDURE_CALL_INSERT_INGRESO + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(query)) {
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
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            response.setCodeResponse("500");
            response.setMessageResponse("Error interno en el servidor " + e.getMessage());
            response.setData("Error");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            response.setCodeResponse("500");
            response.setMessageResponse("Error interno en el servidor " + e.getMessage());
            response.setData("Error");
        }

        return response;
    }

    @Override
    public String createOTPIng(Code_OTP code_OTP) {
        ResponseService response = new ResponseService();

        StoredProcedures stored = new StoredProcedures();
        // Crear un objeto StringBuilder
        StringBuilder queryString = new StringBuilder();
        queryString.append(stored.STORE_PROCEDURE_CALL_INSERT_OTP_CODE).append("('").append(code_OTP.getIdTransaccion()).append("','").append(code_OTP.getArea()).append("')");
        System.out.println("queryString>> " + queryString.toString());
        response.setCodeResponse("00");
        response.setMessageResponse("Ingreso creado exitosamente");
        response.setData("Ok");

        return "hola";
    }
}
