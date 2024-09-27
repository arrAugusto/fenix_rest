package com.serviceBack.fenix.services;

import com.serviceBack.fenix.interfaces.FormsInterfaces;
import com.serviceBack.fenix.models.GetFormUser;
import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
import commons.StoredProcedures;
import java.lang.reflect.Field;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegisterProducts {

    @Autowired
    private FormsInterfaces service;

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored;

    @Autowired
    public RegisterProducts(JdbcTemplate jdbcTemplate) {
        this.stored = new StoredProcedures(); // Inicializa StoredProcedures
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerProduct(IncomeAndWithDrawal ingreso) {
        List<GetFormUser> config = this.service.FormUserService("57");

        try {
            // Asigna los valores desde el mapa 'ingreso' y config
            Object[] params = new Object[]{
                "null".equals(dynamicsGetValue("id_transaction_product", config, ingreso)) ? null : dynamicsGetValue("id_transaction_product", config, ingreso),
                "null".equals(dynamicsGetValue("id_transaction_ingreso", config, ingreso)) ? null : dynamicsGetValue("id_transaction_ingreso", config, ingreso),
                "null".equals(dynamicsGetValue("bultos_total", config, ingreso)) ? null : dynamicsGetValue("bultos_total", config, ingreso),
                "null".equals(dynamicsGetValue("bultos_producto", config, ingreso)) ? null : dynamicsGetValue("bultos_producto", config, ingreso),
                "null".equals(dynamicsGetValue("saldo_bultos", config, ingreso)) ? null : dynamicsGetValue("saldo_bultos", config, ingreso),
                "null".equals(dynamicsGetValue("peso_mercaderia", config, ingreso)) ? null : dynamicsGetValue("peso_mercaderia", config, ingreso),
                "null".equals(dynamicsGetValue("fecha_registro", config, ingreso)) ? null : dynamicsGetValue("fecha_registro", config, ingreso),
                "null".equals(dynamicsGetValue("nombre_cliente", config, ingreso)) ? null : dynamicsGetValue("nombre_cliente", config, ingreso),
                "null".equals(dynamicsGetValue("detalle_producto", config, ingreso)) ? null : dynamicsGetValue("detalle_producto", config, ingreso),
                "null".equals(dynamicsGetValue("estado", config, ingreso)) ? null : dynamicsGetValue("estado", config, ingreso),
                "null".equals(dynamicsGetValue("generico_1", config, ingreso)) ? null : dynamicsGetValue("generico_1", config, ingreso),
                "null".equals(dynamicsGetValue("generico_2", config, ingreso)) ? null : dynamicsGetValue("generico_2", config, ingreso),
                "null".equals(dynamicsGetValue("generico_3", config, ingreso)) ? null : dynamicsGetValue("generico_3", config, ingreso),
                "null".equals(dynamicsGetValue("generico_4", config, ingreso)) ? null : dynamicsGetValue("generico_4", config, ingreso),
                "null".equals(dynamicsGetValue("generico_5", config, ingreso)) ? null : dynamicsGetValue("generico_5", config, ingreso),
                "null".equals(dynamicsGetValue("generico_6", config, ingreso)) ? null : dynamicsGetValue("generico_6", config, ingreso),
                "null".equals(dynamicsGetValue("generico_7", config, ingreso)) ? null : dynamicsGetValue("generico_7", config, ingreso),
                "null".equals(dynamicsGetValue("generico_8", config, ingreso)) ? null : dynamicsGetValue("generico_8", config, ingreso),
                "null".equals(dynamicsGetValue("generico_9", config, ingreso)) ? null : dynamicsGetValue("generico_9", config, ingreso),
                "null".equals(dynamicsGetValue("generico_10", config, ingreso)) ? null : dynamicsGetValue("generico_10", config, ingreso),
                "null".equals(dynamicsGetValue("UUID_code_register", config, ingreso)) ? null : dynamicsGetValue("UUID_code_register", config, ingreso)
            };

            // Ejecuta la consulta de inserción
            jdbcTemplate.update(queryTableDynamics(), params);

        } catch (Exception e) {
            // Manejo de errores: captura el error y registra el mensaje
            System.err.println("Error al intentar registrar el producto: " + e.getMessage());

            // Depuración adicional: puedes imprimir el stack trace
            e.printStackTrace();

            // Opcional: muestra los parámetros para verificar qué valores se están insertando
        }
    }

    private String dynamicsGetValue(String attribute, List<GetFormUser> config, IncomeAndWithDrawal ingreso) {
        try {
            // Obtener el campo a través de la reflexión
            String fieldName = config.stream()
                    .filter(item -> attribute.equals(item.getSub_name_column()))
                    .map(GetFormUser::getTag)
                    .findFirst()
                    .orElse(null);  // Retorna null si no encuentra el campo en la configuración

            if (fieldName == null) {
                return "null";  // Si no se encuentra el campo, retornar "null"
            }

            Field field = ingreso.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);  // Permitir acceso al campo privado

            // Obtener el valor del campo dinámicamente
            String valorDocumento = (String) field.get(ingreso);
            System.out.println("valorDocumento: " + valorDocumento);
            return valorDocumento != null ? valorDocumento : "null";  // Si el valor es null, retorna "null"

        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error accediendo al campo: " + e.getMessage());
            return "null";  // En caso de error, retornar "null"
        }
    }

    public String queryTableDynamics() {
        String query = "INSERT INTO `kimbo_database`.`inventory_balance` (\n"
                + "`id_transaction_product`,\n"
                + "`id_transaction_ingreso`,\n"
                + "`bultos_total`,\n"
                + "`bultos_producto`,\n"
                + "`saldo_bultos`,\n"
                + "`peso_mercaderia`,\n"
                + "`fecha_registro`,\n"
                + "`nombre_cliente`,\n"
                + "`detalle_producto`,\n"
                + "`estado`,\n"
                + "`generico_1`,\n"
                + "`generico_2`,\n"
                + "`generico_3`,\n"
                + "`generico_4`,\n"
                + "`generico_5`,\n"
                + "`generico_6`,\n"
                + "`generico_7`,\n"
                + "`generico_8`,\n"
                + "`generico_9`,\n"
                + "`generico_10`,\n"
                + "`UUID_code_register`\n"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        return query;

    }

}
