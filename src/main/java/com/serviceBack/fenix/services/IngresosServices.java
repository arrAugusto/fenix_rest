package com.serviceBack.fenix.services;

import com.serviceBack.fenix.controllers.Code_OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Util.ResponseService;
import commons.StoredProcedures;

import org.springframework.dao.DataAccessException;

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
        // Crear un objeto StringBuilder
        StringBuilder queryString = new StringBuilder();

        // Agregar cada parte de la cadena utilizando el método append
        queryString.append(stored.CALL_UPDATE_INGRESO).append("('")
                .append(ingreso.getUsuario()).append("',")
                .append("").append(ingreso.getIdTransaccion()).append(",")
                .append("'").append(ingreso.getNit()).append("',")
                .append("'").append(ingreso.getCanalDigital()).append("',")
                .append("'").append(ingreso.getFechaOperativa()).append("',")
                .append("'").append(ingreso.getDocumento()).append("',")
                .append("'").append(ingreso.getCodigoQR()).append("',")
                .append("'").append(ingreso.getBultos()).append("',")
                .append("'").append(ingreso.getCif()).append("',")
                .append("'").append(ingreso.getImpuestos()).append("')");

        System.out.println("queryString> " + queryString.toString());
        // Obtener la cadena final
        try {
            jdbcTemplate.update(queryString.toString());
        } catch (DataAccessException e) {
            System.out.println("DataAccessException: " + e.getMessage());
            e.printStackTrace();  // Imprime la traza completa del error para diagnóstico
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();  // Imprime la traza completa del error para diagnóstico
        }
        response.setCodeResponse("00");
        response.setMessageResponse("Ingreso creado exitosamente");
        response.setData("Ok");

        return response;
    }

    @Override
    public String createOTPIng(Code_OTP code_OTP) {
        ResponseService response = new ResponseService();

        StoredProcedures stored = new StoredProcedures();
        // Crear un objeto StringBuilder
        StringBuilder queryString = new StringBuilder();
        queryString.append(stored.CALL_INSERT_OTP_CODE).append("('").append(code_OTP.getIdTransaccion()).append("','").append(code_OTP.getArea()).append("')");
        System.out.println("queryString>> " + queryString.toString());
        response.setCodeResponse("00");
        response.setMessageResponse("Ingreso creado exitosamente");
        response.setData("Ok");

        return "hola";
    }
}
