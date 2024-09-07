package sub_process.IncomeWithdrawal;

import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.Utils.Send;
import commons.StoredProcedures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class Exceptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Exceptions.class);

    private final ResponseService response;
    private final StoredProcedures stored;
    private final Send sendMail;

    @Autowired
    public Exceptions(ResponseService response, StoredProcedures stored, Send sendMail) {
        this.response = response;
        this.stored = stored;
        this.sendMail = sendMail;
    }

    public void handleSQLException(SQLException e) {
        response.setCodeResponse("500");
        response.setMessageResponse("Error interno en el servidor: " + e.getMessage());
        response.setData("Error");
        logAndSendAlert(e);
    }

    public void handleGenericException(Exception e) {
        response.setCodeResponse("500");
        response.setMessageResponse("Error interno en el servidor: " + e.getMessage());
        response.setData("Error");
        logAndSendAlert(e);
    }

    private void logAndSendAlert(Exception e) {
        StackTraceElement callerInfo = Thread.currentThread().getStackTrace()[2]; // Ajuste para el nivel de llamada correcto
        String fileName = callerInfo.getFileName();
        String className = callerInfo.getClassName();
        int lineNumber = callerInfo.getLineNumber();

        String errorMessage = "Error interno del servidor: [fileName: '" + fileName + "', className: '" + className
                + "', lineNumber: " + lineNumber + ", errorMessage: '" + e.getMessage() + "']";

        sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage, "ERROR INTERNO");
        LOGGER.error(errorMessage, e);
    }
}
