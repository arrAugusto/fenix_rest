package com.serviceBack.fenix.services;

import com.serviceBack.fenix.Utils.CreateToPDFWithPDF;
import com.serviceBack.fenix.Utils.GenericSQL;
import com.serviceBack.fenix.interfaces.HtmlPdfInterfaces;
import com.serviceBack.fenix.models.Comprobante;
import com.serviceBack.fenix.models.ConfigFirmas;
import com.serviceBack.fenix.models.pdf.PDF_Income_Title;
import commons.Const_env;
import commons.StoredProcedures;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GeneratePDFTOIncome implements HtmlPdfInterfaces {

    private final Const_env const_env;
    private final GenericSQL genericSQL;
    private final StoredProcedures stored;
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = Logger.getLogger(AuthTransactionService.class.getName());

    @Autowired
    public GeneratePDFTOIncome(GenericSQL genericSQL) {
        this.genericSQL = genericSQL;
        this.const_env = new Const_env();
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor        
        this.jdbcTemplate = new JdbcTemplate();
    }

    @Override
    public byte[] generatePdfFromHtml(String id_transaction) {

        ResultSet resultSet = null;
        CreateToPDFWithPDF createPDF = new CreateToPDFWithPDF(); // Crear instancia de la clase de PDF

        TransoformGetConfig transformConfig = new TransoformGetConfig();
        try {
            Object[] paramsCheck = {
                id_transaction,
                "1"
            };

            resultSet = genericSQL.select(stored.STORED_PROCEDURE_GET_CHECK_VALID_COMPROBANTE, paramsCheck);

            Comprobante valid = transformConfig.transformDataValidTransaction(resultSet);
            if (valid != null) {
                System.out.println("valid.getIdTransaction()> " + valid.getIdTransaction());
                System.out.println("valid.getComprobante()> " + valid.getComprobante());
                // Generar el PDF utilizando la lista completa de PDF_Income_Title
                byte[] pdfData = createPDF.createPDFWithHTML(valid.getComprobante());
                // Devolver el PDF generado en formato byte[]
                System.out.println("PDF ya existe....");
                return pdfData;
            }

        } catch (Exception e) {

        }
        Object[] paramsConfig = {
            const_env.CALL_INGRESO_NORMAL
        };

        try {
            logger.info("Ejecutando consulta para obtener la configuración de firmas.");
            resultSet = genericSQL.select(stored.STORED_PROCEDURE_CALL_GET_CONFIG_FIRMAS, paramsConfig);

            List< ConfigFirmas> data = transformConfig.transformData(resultSet);
            logger.info("Se obtuvo la configuración de firmas: " + data.size() + " registros encontrados.");
            System.out.println(data.toString());
            Object[] param_id_transaction = {
                id_transaction
            };
            List< PDF_Income_Title> pdfIncome = new ArrayList<>(); // Inicializar la lista

            for (ConfigFirmas configFirma : data) {
                String sqlQuery = configFirma.getSql_required();
                // Verificar si la consulta SQL está vacía o nula
                if (sqlQuery == null || sqlQuery.trim().isEmpty()) {
                    logger.warning("La consulta SQL está vacía o nula para la configuración de firmas: " + configFirma.toString());
                    continue;

                }

                // Imprimir consulta y parámetros para depuración
                System.out.println("Ejecutando consulta: " + sqlQuery);
                System.out.println("Parámetros de la consulta: " + Arrays.toString(param_id_transaction));

                // Ejecutar la consulta SQL
                ResultSet resultSetConfig = genericSQL.select(sqlQuery, param_id_transaction);

                if (resultSetConfig == null) {
                    logger.warning("El ResultSet para la consulta " + sqlQuery + " es nulo.");
                    continue;
                }

                // Verificar si el ResultSet tiene datos
                if (!resultSetConfig.isBeforeFirst()) {
                    logger.warning("El ResultSet para la consulta " + sqlQuery + " está vacío.");
                    resultSetConfig.close(); // Cerrar el ResultSet vacío
                    continue;
                }

                // Procesar el ResultSet
                List< PDF_Income_Title> pdfIncomeList = transformConfig.transformDataConfig(resultSetConfig, configFirma.getInfo_print());
                pdfIncome.addAll(pdfIncomeList);
                resultSetConfig.close();
            }

            // Verificar si la lista de pdfIncome no está vacía y generar el PDF
            if (!pdfIncome.isEmpty()) {
                // Generar el PDF utilizando la lista completa de PDF_Income_Title
                byte[] pdfData = createPDF.createToNewPDF(pdfIncome, id_transaction, genericSQL, stored);

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
                } catch (SQLException e) {
                    logger.severe("Error al cerrar el ResultSet: " + e.getMessage());
                }
            }
        }
    }
}
