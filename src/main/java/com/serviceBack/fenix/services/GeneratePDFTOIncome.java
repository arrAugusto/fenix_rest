package com.serviceBack.fenix.services;

import com.serviceBack.fenix.Utils.GenericSQL;
import com.serviceBack.fenix.interfaces.HtmlPdfInterfaces;
import com.serviceBack.fenix.models.ConfigFirmas;
import com.serviceBack.fenix.models.pdf.PDF_Income_Title;
import commons.Const_env;
import commons.StoredProcedures;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratePDFTOIncome implements HtmlPdfInterfaces {

    private final Const_env const_env;
    private final GenericSQL genericSQL;
    private final StoredProcedures stored;
    private static final Logger logger = Logger.getLogger(AuthTransactionService.class.getName());

    @Autowired
    public GeneratePDFTOIncome(GenericSQL genericSQL) {
        this.genericSQL = genericSQL;
        this.const_env = new Const_env();
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor        
    }

    @Override
    public byte[] generatePdfFromHtml(String id_transaction) {
        PDF_Income_Title pdfTitle = new PDF_Income_Title();
        ResultSet resultSet = null;

        Object[] paramsConfig = {const_env.CALL_INGRESO_NORMAL};
        try {
            logger.info("Ejecutando consulta para obtener la configuración de firmas.");
            resultSet = genericSQL.select(stored.STORED_PROCEDURE_CALL_GET_CONFIG_FIRMAS, paramsConfig);

            TransoformGetConfig transformConfig = new TransoformGetConfig();
            List<ConfigFirmas> data = transformConfig.transformData(resultSet);
            logger.info("Se obtuvo la configuración de firmas: " + data.size() + " registros encontrados.");

            Object[] param_id_transaction = {id_transaction};
            List<PDF_Income_Title> pdfIncome = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                ResultSet resultSetConfig = null;

                System.out.println("param_id_transaction> " + param_id_transaction);
                resultSetConfig = genericSQL.select(data.get(i).getSql_required(), param_id_transaction);

                if (resultSetConfig == null) {
                    logger.warning("El ResultSet para la consulta " + data.get(i).getSql_required() + " es nulo.");
                    continue;
                }

                if (!resultSetConfig.isBeforeFirst()) {
                    logger.warning("El ResultSet para la consulta " + data.get(i).getSql_required() + " está vacío.");
                    resultSetConfig.close(); // Asegurarse de cerrar el ResultSet vacío
                    continue;
                }

                pdfIncome = transformConfig.transformDataConfig(resultSetConfig);

                // Cerrar el ResultSet después de procesarlo
                if (resultSetConfig != null) {
                    resultSetConfig.close();
                }
            }
            if (pdfIncome != null) {
                for (int i = 0; i < pdfIncome.size(); i++) {
                    System.out.println(pdfIncome.get(i).getClave()+" "+pdfIncome.get(i).getTextValue());
                }

            }

            // Aquí agregarías la lógica para generar el PDF a partir de los datos obtenidos
            return null; // Aquí deberías devolver el PDF generado en formato byte[]
        } catch (Exception e) {
            logger.severe("Error al generar el PDF: " + e.getMessage());
            e.printStackTrace();
            return null; // Retorna null en caso de error
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    logger.severe("Error al cerrar el ResultSet: " + e.getMessage());
                }
            }
        }
    }
}
