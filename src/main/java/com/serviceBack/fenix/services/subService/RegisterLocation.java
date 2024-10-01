package com.serviceBack.fenix.services.subService;

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
public class RegisterLocation {

    @Autowired
    private FormsInterfaces service;

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored;

    @Autowired
    public RegisterLocation(JdbcTemplate jdbcTemplate) {
        this.stored = new StoredProcedures(); // Inicializa StoredProcedures
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerLocation(IncomeAndWithDrawal ingreso) {
        System.out.println("registerLocation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
        // Obtiene la configuración necesaria para el registro de ubicaciones
        List<GetFormUser> config = this.service.FormUserService("58", null);
        try {
            // Array de nombres de las columnas en la tabla, excluyendo `fecha_ultima_auditoria`
            String[] attributes = {
                "id_transacction_ubicacion", "id_transacction_ingreso", "gps_location", "seccion", "pasillo", "columna",
                "fila", "nivel", "posicion", "capacidad", "peso_maximo",
                "ocupacion_actual", "peso_actual", "estado", "tipo",
                "temperatura", "humedad", "descripcion"
            };

            // Uso de un bucle para crear el array de parámetros
            Object[] params = new Object[attributes.length];

            // Asigna directamente el valor de `ubicacion` a los parámetros correspondientes
            params[0] = ingreso.getId_transaccion(); // id_transacction_ubicacion
            params[1] = ingreso.getId_transaccion_foreing();   // id_transacction_ingreso

            // Asignar `gps_location` como un valor de tipo `POINT` utilizando `ST_GeomFromText`
            if (ingreso.getGps_location() != null && !ingreso.getGps_location().isEmpty()) {
                // Suponiendo que `ingreso.getGps_location()` tiene el formato "latitud, longitud"
                String[] gpsValues = ingreso.getGps_location().split(",");
                if (gpsValues.length == 2) {
                    // Formato del valor POINT para MySQL: latitud y longitud en formato POINT(longitud latitud)
                    String pointValue = String.format("POINT(%s %s)", gpsValues[1].trim(), gpsValues[0].trim()); // LONGITUD primero, LATITUD después
                    params[2] = pointValue; // Se pasa como una cadena al parámetro
                } else {
                    params[2] = null;
                }
            } else {
                params[2] = null;
            }

            // Completar los parámetros restantes usando `getValidatedValue`
            for (int i = 3; i < attributes.length; i++) { // Comenzar desde el índice 3 para omitir gps_location
                params[i] = getValidatedValue(attributes[i], config, ingreso);
            }

            // Verificar los parámetros antes de ejecutar la consulta (opcional, solo para depuración)
            for (Object param : params) {
                System.out.println("Parameter: " + param);
            }

            // Construcción de la consulta de inserción usando `ST_GeomFromText`
            String insertQuery = "INSERT INTO kimbo_database.ubicaciones_mercancias (" +
                                 String.join(", ", attributes) + ") VALUES (?, ?, ST_GeomFromText(?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Ejecuta la consulta de inserción con JdbcTemplate
            jdbcTemplate.update(insertQuery, params);

        } catch (Exception e) {
            // Manejo de errores: captura el error y registra el mensaje
            System.err.println("Error al intentar registrar la ubicación: " + e.getMessage());
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
            } else {
                return fieldValue.toString();  // Para cualquier otro tipo, llamar a `toString()`
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error accediendo al campo: " + e.getMessage());
            return "null";  // En caso de error, retornar "null"
        }
    }
}
