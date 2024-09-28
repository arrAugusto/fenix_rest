package com.serviceBack.fenix.services;

import com.serviceBack.fenix.interfaces.FormsInterfaces;
import com.serviceBack.fenix.models.GetFormUser;
import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
import commons.StoredProcedures;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
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
    System.out.println("ingreso> " + ingreso.getConfig_form());
    // Obtiene la configuración necesaria para el registro de productos
    List<GetFormUser> config = this.service.FormUserService("57");
    System.out.println(ingreso.getId_transaccion());
    try {
        // Array de nombres de las columnas en la tabla, incluyendo `fecha_registro`
        String[] attributes = {
            "id_transaction_product", "id_transaction_ingreso", "bultos_total", "bultos_producto",
            "saldo_bultos", "peso_mercaderia", "nombre_cliente", "detalle_producto",
            "estado", "generico_1", "generico_2", "generico_3", "generico_4", "generico_5",
            "generico_6", "generico_7", "generico_8", "generico_9", "generico_10", "UUID_code_register",
            "fecha_registro" // Incluir `fecha_registro` al final
        };

        // Uso de un bucle para crear el array de parámetros, excepto el último que será la fecha
        Object[] params = new Object[attributes.length];
        
        // 1. Asigna directamente el valor de ingreso.getId_transaccion() a `id_transaction_product`
        params[0] = ingreso.getId_transaccion();  // Asignar el valor de id_transaction_product directamente
        
        // 2. Completar los parámetros restantes usando `getValidatedValue`
        for (int i = 1; i < attributes.length - 1; i++) { // Llenar hasta antes de `fecha_registro`
            params[i] = getValidatedValue(attributes[i], config, ingreso);
        }

        // 3. Asignar la fecha y hora actual al último parámetro (fecha_registro)
        params[attributes.length - 1] = new Timestamp(new Date().getTime());

        // Verificar los parámetros antes de ejecutar la consulta (opcional, solo para depuración)
        for (Object param : params) {
            System.out.println("Parameter: " + param);
        }

        // Construcción de la consulta de inserción
        String insertQuery = buildInsertQuery("kimbo_database.inventory_balance", attributes);

        // Ejecuta la consulta de inserción
        jdbcTemplate.update(insertQuery, params);

    } catch (Exception e) {
        // Manejo de errores: captura el error y registra el mensaje
        System.err.println("Error al intentar registrar el producto: " + e.getMessage());
        e.printStackTrace(); // Depuración adicional
    }
}

    // Método para construir dinámicamente la consulta SQL de inserción
    private String buildInsertQuery(String tableName, String[] columns) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tableName + " (");

        // Añade las columnas separadas por comas
        queryBuilder.append(String.join(", ", columns)).append(") VALUES (");

        // Generar marcadores de '?' separados por coma
        String placeholders = String.join(", ", java.util.Collections.nCopies(columns.length, "?"));
        queryBuilder.append(placeholders).append(")");

        return queryBuilder.toString();
    }

    // Método para obtener y validar el valor de un atributo específico
    private String getValidatedValue(String attribute, List<GetFormUser> config, IncomeAndWithDrawal ingreso) {
        String value = dynamicsGetValue(attribute, config, ingreso);
        return "null".equals(value) ? null : value; // Si es "null", devuelve null (no la cadena "null")
    }

    private String dynamicsGetValue(String attribute, List<GetFormUser> config, IncomeAndWithDrawal ingreso) {
        try {
            // Obtener el nombre del campo asociado a la columna
            String fieldName = config.stream()
                    .filter(item -> attribute.equals(item.getSub_name_column()))
                    .map(GetFormUser::getTag)
                    .findFirst()
                    .orElse(null);

            if (fieldName == null) {
                return "null";  // Si no se encuentra el campo, retornar "null"
            }

            // Obtener el campo a través de reflexión
            Field field = ingreso.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);  // Permitir acceso al campo privado

            // Obtener el valor del campo dinámicamente
            Object fieldValue = field.get(ingreso); // Se obtiene como Object

            // Convertir el valor a String según su tipo
            if (fieldValue == null) {
                return "null";  // Valor nulo, devolver "null"
            } else if (fieldValue instanceof String) {
                return (String) fieldValue;
            } else if (fieldValue instanceof Integer || fieldValue instanceof Long || fieldValue instanceof Double) {
                return String.valueOf(fieldValue);  // Convertir números a String
            } else if (fieldValue instanceof Date) {
                return new Timestamp(((Date) fieldValue).getTime()).toString();  // Convertir Date a String
            } else {
                return fieldValue.toString();  // Para cualquier otro tipo, llamar a `toString()`
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error accediendo al campo: " + e.getMessage());
            return "null";  // En caso de error, retornar "null"
        }
    }

}
