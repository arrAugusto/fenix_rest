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
    
    //LOG INICIO DE SESSION
    public final String STORE_PROCEDURE_CALL_LOG_USER = "CALL INSERT_INICIO_SESSION(?,?,?,?)";
    
    //INICIO DE SESSION
    public final String STORE_PROCEDURE_CALL_GET_LOGING_USER = "CALL GET_LOGIN_USER(?)";
    
    //ACTUALIZACION DE INGRESOS OPERATIVA
    public final String STORE_PROCEDURE_CALL_INSERT_INGRESO = "CALL INSERT_INGRESO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    //REGISTRAR NUEVO OTP CODIGO
    public final String STORE_PROCEDURE_CALL_INSERT_OTP_CODE = "CALL INSERT_OTP_CODE_INGRESO";
    
    //INSERTAR NUEVO ITEM DE INGRESO
    public final String STORE_PROCEDURE_CALL_INSERT_ITEMS = "CALL INSERT_ITEMS(?,?,?,?,?,?,?,?,?,?)";
    
    //GET INGRESO TRANSACCION BASICA
    public final String STORE_PROCEDURE_CALL_GET_TRANSACCION_INGRESO_INF = "CALL GET_TRANSACCION_INGRESO_INF(?)";
    
    //MOSTRAR NUEVO ITEM DE INGRESO
    public final String STORE_PROCEDURE_CALL_GET_ITEMS = "CALL GET_ITEMS_INGRESO";
    
    //MOSTRAR NUEVO ITEM DE INGRESO
    public final String STORED_PROCEDURE_CALL_INSERT_DETAILS_ARRIBO_BODEGA = "CALL INSERT_DETAILS_ARRIBO_BODEGA(?,?,?,?,?,?,?,?)";
    
    //MOSTRAR NUEVO UBICACION DE INGRESO
    public final String STORED_PROCEDURE_CALL_INSERT_GEOPOSICION_UBICACION = "CALL INSERT_GEOPOSICION_UBICACION(?,?,?,?,?)";

    //MODIFICAR NUEVO UBICACION DE INGRESO
    public final String STORED_PROCEDURE_CALL_UPDATE_GEO_UBICACION = "CALL UPDATE_GEO_UBICACION(?,?,?,?)";

    //GET ID INGRESO
    public final String STORED_PROCEDURE_CALL_GET_ID_INGRESO = "CALL GET_ID_INGRESO(?)";

    //GET ID ARRIBO
    public final String STORED_PROCEDURE_CALL_GET_ID_GEO_UBICACION = "CALL GET_ID_GEO_UBICACION(?)";

    
    //ROLLBACK O DELETE SI EXISTE UN ERROR EN EL INSERT DE LOS DATOS
    public final String STORE_PROCEDURE_DELETE_ITEMS_INCOME = "CALL DELETE_ITEMS_INCOME(?)";
    
    //GET MODIFICACION DE TRANSACCIONES DE INGRESO
    public final String STORED_PROCEDURE_UPDATE_TRANSACTION_INCOME = "CALL UPDATE_TRANSACTION_INCOME(?,?,?)";
    
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

    //MOSTRAR FORMULARIOS
    public final String STORED_PROCEDURE_CALL_GET_FORMS = "CALL GET_CARDS_FORMS(?)";

    //MOSTRAR SIDE NAV
    public final String STORED_PROCEDURE_CALL_GET_SIDE_NAV = "CALL GET_SIDE_NAV(?)";

    
    //credenciales sendmail, por el momento se pondra aqui y luego pasara  a un archivo .ini o bien a una base de datos de contraseñas
    public final String mailTO = "argomez120@gmail.com";

    public final String mailFROM = "agomezc6@miumg.edu.gt";

    public final String PWD = "zxvi pyfq vwrb qkab";

}
