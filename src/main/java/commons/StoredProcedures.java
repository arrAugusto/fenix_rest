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

    //ACTUALIZACION DE INGRESOS OPERATIVA
    public final String STORE_PROCEDURE_CALL_INSERT_INGRESO = "CALL INSERT_INGRESO";

    //REGISTRAR NUEVO OTP CODIGO
    public final String STORE_PROCEDURE_CALL_INSERT_OTP_CODE = "CALL INSERT_OTP_CODE_INGRESO";
    
    //credenciales sendmail, por el momento se pondra aqui y luego pasara  a un archivo .ini o bien a una base de datos de contraseñas
    public final String mailTO = "";

    public final String mailFROM = "";

    public final String PWD = "";

}
