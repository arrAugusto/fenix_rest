package com.serviceBack.fenix.services;

import com.serviceBack.fenix.Utils.CreateToPDFWithPDF;
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
        ResultSet resultSet = null;
        CreateToPDFWithPDF createPDF = new CreateToPDFWithPDF(); // Crear instancia de la clase de PDF
        Object[] paramsConfig = {const_env.CALL_INGRESO_NORMAL};

        try {
            logger.info("Ejecutando consulta para obtener la configuración de firmas.");
            resultSet = genericSQL.select(stored.STORED_PROCEDURE_CALL_GET_CONFIG_FIRMAS, paramsConfig);

            TransoformGetConfig transformConfig = new TransoformGetConfig();
            List<ConfigFirmas> data = transformConfig.transformData(resultSet);
            logger.info("Se obtuvo la configuración de firmas: " + data.size() + " registros encontrados.");

            Object[] param_id_transaction = {id_transaction};
            List<PDF_Income_Title> pdfIncome = new ArrayList<>();  // Inicializar la lista

            for (ConfigFirmas configFirma : data) {
                ResultSet resultSetConfig = genericSQL.select(configFirma.getSql_required(), param_id_transaction);

                if (resultSetConfig == null) {
                    logger.warning("El ResultSet para la consulta " + configFirma.getSql_required() + " es nulo.");
                    continue;
                }

                if (!resultSetConfig.isBeforeFirst()) {
                    logger.warning("El ResultSet para la consulta " + configFirma.getSql_required() + " está vacío.");
                    resultSetConfig.close(); // Cerrar el ResultSet vacío
                    continue;
                }

                // Transformar datos de acuerdo al ResultSet
                List<PDF_Income_Title> pdfIncomeList = transformConfig.transformDataConfig(resultSetConfig, configFirma.getInfo_print());
                System.out.println(pdfIncomeList.toString());
                // Agregar los nuevos datos transformados a la lista pdfIncome
                pdfIncome.addAll(pdfIncomeList);

                // Cerrar el ResultSet después de procesarlo
                resultSetConfig.close();
            }

            // Verificar si la lista de pdfIncome no está vacía y generar el PDF
            if (!pdfIncome.isEmpty()) {
                // Generar el PDF utilizando la lista completa de PDF_Income_Title
                byte[] pdfData = createPDF.createToPDFWithPDF(pdfIncome);

                // Devolver el PDF generado en formato byte[]
                return pdfData;
            } else {
                logger.warning("La lista de ingresos PDF está vacía, no se generará ningún PDF.");
                return null;
            }

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
