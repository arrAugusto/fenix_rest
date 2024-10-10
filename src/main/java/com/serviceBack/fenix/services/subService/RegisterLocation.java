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

/**
 * Clase RegisterLocation: se encarga de registrar la ubicación de ingresos y retiros.
 */
@Service
public class RegisterLocation {

    @Autowired
    private FormsInterfaces service;

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored;

    /**
     * Constructor para inicializar `JdbcTemplate` y `StoredProcedures`.
     * 
     * @param jdbcTemplate objeto de `JdbcTemplate` inyectado automáticamente por Spring.
     */
    @Autowired
    public RegisterLocation(JdbcTemplate jdbcTemplate) {
        this.stored = new StoredProcedures(); // Inicializa StoredProcedures
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Método para registrar la ubicación de un ingreso o retiro en la base de datos.
     * 
     * @param ingreso Objeto `IncomeAndWithDrawal` que contiene los datos de la transacción.
     */
    public void registerLocation(IncomeAndWithDrawal ingreso) {
        System.out.println("registerLocation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
        
        // Obtiene la configuración necesaria para el registro de ubicaciones
        List<GetFormUser> config = this.service.FormUserService("58", null);

        try {
            // Array de nombres de las columnas en la tabla, excluyendo `fecha_ultima_auditoria`
            String[] attributes = {
                "id_transacction_ubicacion", "id_transacction_ingreso", "id_transacction_product", "gps_location",
                "seccion", "pasillo", "columna", "fila", "nivel", "posicion", "capacidad", "peso_maximo",
                "ocupacion_actual", "peso_actual", "estado", "tipo", "temperatura", "humedad", "descripcion"
            };

            // Uso de un bucle para crear el array de parámetros
            Object[] params = new Object[attributes.length];  // Debe coincidir con el número de columnas

            // Asigna directamente el valor de `ubicacion` a los parámetros correspondientes
            params[0] = ingreso.getId_transaccion();             // id_transacction_ubicacion
            params[1] = ingreso.getId_transaccion_foreing();     // id_transacction_ingreso
            params[2] = ingreso.getNombre();                    // id_transacction_product (mantener como `getNombre` según tu implementación)

            // Asignar `gps_location` como un valor de tipo `POINT` utilizando `ST_GeomFromText`
            if (ingreso.getGps_location() != null && !ingreso.getGps_location().isEmpty()) {
                String[] gpsValues = ingreso.getGps_location().split(",");
                if (gpsValues.length == 2) {
                    // Formato del valor POINT para MySQL: LONGITUD primero, LATITUD después
                    String pointValue = String.format("POINT(%s %s)", gpsValues[1].trim(), gpsValues[0].trim());
                    params[3] = pointValue; // Se pasa como un solo parámetro en el índice 3 (gps_location)
                } else {
                    params[3] = null; // Dejar nulo si el formato no es correcto
                }
            } else {
                params[3] = null;
            }

            // Completar los parámetros restantes usando `getValidatedValue`
            for (int i = 4; i < attributes.length; i++) { // Comenzar desde el índice 4 para omitir gps_location y los primeros 3 parámetros
                params[i] = getValidatedValue(attributes[i], config, ingreso);
            }

            // Verificar los parámetros antes de ejecutar la consulta (opcional, solo para depuración)
            for (Object param : params) {
                System.out.println("Parameter: " + param);
            }

            // Construcción de la consulta de inserción utilizando `buildInsertQuery`
            String insertQuery = buildInsertQueryWithGeom("ubicaciones_mercancias", attributes);

            // Ejecuta la consulta de inserción con JdbcTemplate
            jdbcTemplate.update(insertQuery, params);

        } catch (Exception e) {
            // Manejo de errores: captura el error y registra el mensaje
            System.err.println("Error al intentar registrar la ubicación: " + e.getMessage());
            e.printStackTrace(); // Depuración adicional
        }
    }

    /**
     * Método auxiliar para construir dinámicamente la consulta SQL de inserción incluyendo `ST_GeomFromText` para `gps_location`.
     * 
     * @param tableName Nombre de la tabla.
     * @param columns   Array de nombres de columnas.
     * @return Consulta SQL de inserción generada dinámicamente.
     */
    private String buildInsertQueryWithGeom(String tableName, String[] columns) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tableName + " (");

        // Añade las columnas separadas por comas
        queryBuilder.append(String.join(", ", columns)).append(") VALUES (");

        // Generar marcadores de '?' separados por coma, pero con `ST_GeomFromText(?)` para `gps_location`
        for (int i = 0; i < columns.length; i++) {
            if (i == 3) { // Si es la columna `gps_location`
                queryBuilder.append("ST_GeomFromText(?)"); // Usar `ST_GeomFromText(?)`
            } else {
                queryBuilder.append("?");
            }
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");

        return queryBuilder.toString();
    }

    /**
     * Método para obtener y validar el valor de un atributo específico de `IncomeAndWithDrawal`.
     * 
     * @param attribute Nombre del atributo.
     * @param config    Configuración obtenida desde `FormUserService`.
     * @param ingreso   Objeto `IncomeAndWithDrawal` para obtener el valor.
     * @return Valor validado como `String`.
     */
    private String getValidatedValue(String attribute, List<GetFormUser> config, IncomeAndWithDrawal ingreso) {
        String value = dynamicsGetValue(attribute, config, ingreso);
        return "null".equals(value) ? null : value; // Si es "null", devuelve null (no la cadena "null")
    }

    /**
     * Método para obtener dinámicamente el valor de un atributo utilizando reflexión.
     * 
     * @param attribute Nombre del atributo.
     * @param config    Configuración obtenida desde `FormUserService`.
     * @param ingreso   Objeto `IncomeAndWithDrawal` para obtener el valor.
     * @return Valor del atributo como `String`.
     */
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
