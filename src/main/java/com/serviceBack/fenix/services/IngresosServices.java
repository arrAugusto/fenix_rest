package com.serviceBack.fenix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.utils.ResponseService;

import org.springframework.dao.DataAccessException;

@Service
public class IngresosServices implements IngresosInterfaces {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IngresosServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseService createIngresos(Ingresos ingresos) {
        ResponseService response = new ResponseService();

        String sqlQuery = "SELECT * FROM corebyte.transaccion_ingresos";
        System.out.println(sqlQuery);

        try {
            jdbcTemplate.query(sqlQuery, rs -> {
                // Verificar si hay al menos una fila en el ResultSet
                if (rs.next()) {
                    // TODO: Implementa tu lógica para extraer datos del ResultSet
                    System.out.println("conectado a db corebyte canal digital>>> " + rs.getString("canal_digital"));
                    return rs.getString("canal_digital");  // Utiliza 1 en lugar de 0
                } else {
                    // Manejar el caso donde no hay filas en el ResultSet
                    System.out.println("No hay filas en el ResultSet");
                    return null;  // Otra opción sería lanzar una excepción indicando que no hay datos
                }
            });
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
}
