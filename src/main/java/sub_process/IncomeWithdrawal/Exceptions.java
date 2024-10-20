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
    private static final String INTERNAL_ERROR_CODE = "500";
    private static final String INTERNAL_ERROR_MSG = "Error interno en el servidor: ";

    private final ResponseService response;
    private final StoredProcedures stored;
    private final Send sendMail;

    @Autowired
    public Exceptions(ResponseService response, StoredProcedures stored, Send sendMail) {
        this.response = response;
        this.stored = stored;
        this.sendMail = sendMail;
    }

    /**
     * Maneja excepciones de SQL.
     *
     * @param e La excepción de tipo SQLException.
     */
    public void handleSQLException(SQLException e) {
        handleException(e, "SQL Exception");
    }

    /**
     * Maneja excepciones genéricas.
     *
     * @param e La excepción de tipo Exception.
     */
    public void handleGenericException(Exception e) {
        handleException(e, "Generic Exception");
    }

    /**
     * Método privado para manejar todas las excepciones.
     *
     * @param e La excepción capturada.
     * @param errorPrefix El prefijo que describe el tipo de error.
     */
    private void handleException(Exception e, String errorPrefix) {
        response.setCodeResponse(INTERNAL_ERROR_CODE);
        response.setMessageResponse(INTERNAL_ERROR_MSG + e.getMessage());
        logAndSendAlert(e, errorPrefix);
    }

    /**
     * Registra el error y envía una alerta por correo electrónico.
     *
     * @param e La excepción que se está manejando.
     * @param errorPrefix Prefijo que describe el error.
     */
    private void logAndSendAlert(Exception e, String errorPrefix) {
        StackTraceElement callerInfo = Thread.currentThread().getStackTrace()[2]; // Obtener la información de la llamada anterior
        String fileName = callerInfo.getFileName();
        String className = callerInfo.getClassName();
        int lineNumber = callerInfo.getLineNumber();

        String errorMessage = String.format("Error interno del servidor: [fileName: '%s', className: '%s', lineNumber: %d, errorMessage: '%s']",
                fileName, className, lineNumber, e.getMessage());

        String alertSubject = errorPrefix + ": ERROR INTERNO";

        // Validar que los valores críticos no sean nulos antes de enviar el correo
        if (stored.mailTO != null && stored.mailFROM != null && stored.PWD != null) {
            sendMail.alertas(stored.mailTO, stored.mailFROM, stored.PWD, errorMessage, alertSubject, "");
        } else {
            LOGGER.warn("No se pudo enviar el correo de alerta. Faltan valores en stored: mailTO, mailFROM o PWD.");
        }

        // Registrar en los logs de error
        LOGGER.error(errorMessage, e);
    }
}
