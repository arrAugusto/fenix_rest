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
    public final String STORE_PROCEDURE_CALL_GET_LOGING_USER = "CALL GET_LOGIN_USER(?)";
    
    //ACTUALIZACION DE INGRESOS OPERATIVA
    public final String STORE_PROCEDURE_CALL_INSERT_INGRESO = "CALL INSERT_INGRESO";

    //REGISTRAR NUEVO OTP CODIGO
    public final String STORE_PROCEDURE_CALL_INSERT_OTP_CODE = "CALL INSERT_OTP_CODE_INGRESO";
    
    //INSERTAR NUEVO ITEM DE INGRESO
    public final String STORE_PROCEDURE_CALL_INSERT_ITEM = "CALL INSERT_ITEM";
    
    //MOSTRAR NUEVO ITEM DE INGRESO
    public final String STORE_PROCEDURE_CALL_GET_ITEMS = "CALL GET_ITEMS_INGRESO";

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
