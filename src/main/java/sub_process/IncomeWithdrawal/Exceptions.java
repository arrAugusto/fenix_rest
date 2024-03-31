/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sub_process.IncomeWithdrawal;

import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.Utils.Send;
import commons.StoredProcedures;
import java.sql.SQLException;
import static org.hibernate.bytecode.BytecodeLogging.LOGGER;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author agr12
 */
public class Exceptions {

    private final ResponseService response;
    private final StoredProcedures stored; // Nueva variable de instancia
    private final Send sendMail;

    @Autowired
    public Exceptions() {
        this.response = new ResponseService();
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
        this.sendMail = new Send();

    }

    public void handleSQLException(SQLException e) {
        response.setCodeResponse("500");
        response.setMessageResponse("Error interno en el servidor " + e.getMessage());
        response.setData("Error");
        logAndSendAlert(e);
    }

    public void handleGenericException(Exception e) {
        response.setCodeResponse("500");
        response.setMessageResponse("Error interno en el servidor " + e.getMessage());
        response.setData("Error");
        logAndSendAlert(e);
    }

    private void logAndSendAlert(Exception e) {
        StackTraceElement callerInfo = Thread.currentThread().getStackTrace()[1];
        String fileName = callerInfo.getFileName();
        String className = callerInfo.getClassName();
        int lineNumber = callerInfo.getLineNumber();

        String errorMessage = "Error interno del servidor: [fileName: '" + fileName + "', className: '" + className
                + "', lineNumber: " + lineNumber + ", errorMessage: '" + e.getMessage() + "']";

        sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage);
        LOGGER.info(errorMessage);
    }
}
