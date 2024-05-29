/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commons;

/**
 *
 * @author agr12
 */
public class StoredProcedures {
    
    //INICIO DE SESSION
    public final String STORED_PROCEDURE_CALL_GET_LOGING_USER = "CALL GET_LOGIN_USER(?)";//Login

    //LOG INICIO DE SESSION
    public final String STORED_PROCEDURE_CALL_LOG_USER = "CALL INSERT_INICIO_SESSION(?,?,?,?)";//Crear usuarios
    
    //INGRESOS
    public final String STORED_PROCEDURE_CALL_INSERT_INGRESO = "CALL INSERT_INGRESO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//Crear ingresos

    //CHECKAR TRANSACCION INGRESO
    public final String STORED_PROCEDURE_CALL_CHECK_INCOME = "CALL CHECK_INCOME(?)";//Validación de ingresos

    //ACTUALIZACION DE INGRESOS EXITOSOS
    public final String STORED_PROCEDURE_CALL_UPDATE_INGRESO_EXITOSO = "CALL UPDATE_INGRESO_EXITOSO(?)";//Actualización de ingresos exitosos
    

    //ACTUALIZACION DE INGRESOS EXITOSOS
    public final String STORED_PROCEDURE_CALL_CHECK_ID_TRANSACCION = "CALL CHECK_ID_TRANSACCION(?)";//Validar id_transaccion


    //INSERTAR NUEVO ITEM DE INGRESO {{ CALL INSERT_ITEM(3, 1, 'Producto A', 'Caja', '10.00', 'Sin comentarios', 'Activo'); }}
    public final String STORED_PROCEDURE_CALL_INSERT_ITEMS = "CALL INSERT_ITEM(?,?,?,?,?,?,?)";//Insertar nuevo itetm de ingreso 
    
    //VALIDAR INFORMACION DEL INGRESO
    public final String STORED_PROCEDURE_CALL_CHECK_INCOME_VALID = "CALL CHECK_INCOME_VALID(?)";//Validar info ingreso
    
    //VALIDAR LA SUMA TOTAL DE BULTOS
    public final String STORED_PROCEDURE_CALL_CHECK_TOTAL_BULTOS_ITEMS = "CALL CHECK_TOTAL_BULTOS_ITEMS(?)";//Suma total de bultos items
    
    //ROLLBACK O DELETE SI EXISTE UN ERROR EN EL INSERT DE LOS DATOS
    public final String STORE_PROCEDURE_DELETE_ITEMS_INCOME = "CALL DELETE_ITEMS_INCOME(?)";//Delete items id_ingreso
    
    //GET MODIFICACION DE TRANSACCIONES DE INGRESO
    public final String STORED_PROCEDURE_UPDATE_ITEMS_INCOME = "CALL UPDATE_ITEMS_INCOME(?)";//ITEMS REGISTRADOS
    
    //MOSTRAR NUEVO ITEM DE INGRESO
    public final String STORED_PROCEDURE_CALL_INSERT_DETAILS_ARRIBO_BODEGA = "CALL INSERT_DETAILS_ARRIBO_BODEGA(?,?,?,?,?,?)";//Insertar detalles en arribo
    
    //MOSTRAR NUEVO UBICACION DE INGRESO
    public final String STORED_PROCEDURE_CALL_INSERT_GEOPOSICION_UBICACION = "CALL INSERT_GEO_UBICACION(?,?,?,?,?,?,?)"; //Ubicación inicial de mercaderia

    //MODIFICAR NUEVO UBICACION DE INGRESO
    public final String STORED_PROCEDURE_CALL_UPDATE_GEO_UBICACION = "CALL UPDATE_GEO_UBICACION(?,?,?,?,?,?)";//Cambio de ubicación
    
    //MODIFICAR NUEVO UBICACION DE INGRESO
    public final String STORED_PROCEDURE_CALL_GET_REQUIRED_ASIST = "CALL GET_REQUIRED_ASIST(?)";//Revisar si necesita asistencia humana
    
    public final String STORED_PROCEDURE_CALL_GET_ALL_DATA_ARRIBO = "CALL GET_ALL_DATA_ARRIBO(?)";//All data de arribo para errores o mostrar mas data especifica del ingreso y detalles

    public String STORED_PROCEDURE_CALL_GET_ID_ARRIBO = "CALL GET_ID_ARRIBO(?)";//Id de arribo
    
    //MOSTRAR FORMULARIO DE ACCIONES EN INGRESOS
    public final String STORED_PROCEDURE_CALL_GET_STORED_GROUP_FORM = "CALL GET_STORED_GROUP_FORM(?)";//Required forms
    
    //MOSTRAR SIDE NAV
    public final String STORED_PROCEDURE_CALL_GET_SIDE_NAV = "CALL GET_SIDE_NAV(?)";//OBTENER MENU SIDENAV
    
    //MOSTRAR FORMULARIOS
    public final String STORED_PROCEDURE_CALL_GET_FORMS = "CALL GET_CARDS_FORMS(?)";//Obtener grupos de formularios
    
    
    
    //REGISTRAR NUEVO OTP CODIGO
    public final String STORE_PROCEDURE_CALL_INSERT_OTP_CODE = "CALL INSERT_OTP_CODE_INGRESO";
    
    
    //GET INGRESO TRANSACCION BASICA
    public final String STORE_PROCEDURE_CALL_GET_TRANSACCION_INGRESO_INF = "CALL GET_TRANSACCION_INGRESO_INF(?)";
    
    //MOSTRAR NUEVO ITEM DE INGRESO
    public final String STORE_PROCEDURE_CALL_GET_ITEMS = "CALL GET_ITEMS_INGRESO";
    
    


    //GET ID INGRESO
    public final String STORED_PROCEDURE_CALL_GET_ID_INGRESO = "CALL GET_ID_INGRESO(?)";

    //GET ID ARRIBO
    public final String STORED_PROCEDURE_CALL_GET_ID_GEO_UBICACION = "CALL GET_ID_GEO_UBICACION(?)";

    
    

    //GET TOTAL BULTOS ITEMS
    public final String STORE_PROCEDURE_CALL_GET_ITEMS_TOTAL_BULTOS ="CALL GET_ITEMS_TOTAL_BULTOS(?)";

    //NEW PRODUCT
    public final String STORED_PROCEDURE_CALL_INSERT_PRODUCT = "CALL INSERT_PRODUCT(?,?,?,?,?,?,?,?)";
    
    //GET NIT {CALL GET_NIT('51611');}
    public final String STORE_PROCEDURE_CALL_GET_NIT = "CALL GET_NIT";

    //NUEVO NIT {CALL INSERT_NIT_CLIENT('2330675310101', 'CUI', 'LUIS MIGUEL CORADO', 'GUATEMALA VILLA NUEVA', 'agomezc6@miumg.edu.gt', 'A');}
    public final String STORE_PROCEDURE_CALL_NEW_NIT = "CALL INSERT_NIT_CLIENT";
    
    //MODIFICAR NIT {CALL INSERT_NIT_CLIENT('2330675310101', 'CUI', 'LUIS MIGUEL CORADO', 'GUATEMALA VILLA NUEVA', 'agomezc6@miumg.edu.gt', 'A');}
    public final String STORE_PROCEDURE_CALL_UPDATE_NIT = "CALL UPDATE_CLIENTE";


    


    
    //credenciales sendmail, por el momento se pondra aqui y luego pasara  a un archivo .ini o bien a una base de datos de contraseñas
    public final String mailTO = "argomez120@gmail.com";

    public final String mailFROM = "agomezc6@miumg.edu.gt";

    public final String PWD = "zxvi pyfq vwrb qkab";

}
