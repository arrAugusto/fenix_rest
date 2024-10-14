/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.services;

import com.serviceBack.fenix.models.ConfigFirmas;
import com.serviceBack.fenix.models.pdf.PDF_Income_Title;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author agr12
 */
public class TransoformGetConfig {

    private static final Logger logger = Logger.getLogger(AuthTransactionService.class.getName());

    public List<ConfigFirmas> transformData(ResultSet rs) {
        logger.info("Iniciando transformación de datos del ResultSet en ConfigFirmas.");
        List<ConfigFirmas> firmasList = new ArrayList<>();
        if (rs == null) {
            logger.warning("ResultSet es nulo, devolviendo lista vacía.");
            return firmasList; // Retornar lista vacía si ResultSet es null
        }

        try {
            while (rs.next()) { // Iterar a través del ResultSet
                ConfigFirmas config = new ConfigFirmas();
                config.setId(rs.getString("id"));
                config.setModulo(rs.getString("modulo"));
                config.setOptional(rs.getString("optional"));
                config.setEstado(rs.getString("estado"));
                config.setSql_required(rs.getString("sql_required"));
                config.setInfo_print(rs.getString("info_print"));
                config.setTipo_transaccional(rs.getString("tipo_transaccional"));

                firmasList.add(config); // Agregar cada objeto a la lista
            }
            logger.info("Transformación de datos completada con " + firmasList.size() + " registros.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al transformar los datos de ConfigFirmas: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    logger.info("Cerrando ResultSet en transformData.");
                    rs.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error al cerrar ResultSet en transformData: " + e.getMessage(), e);
            }
        }

        return firmasList; // Retornar la lista con los resultados
    }

    public List<PDF_Income_Title> transformDataConfig(ResultSet rs) {
        List<PDF_Income_Title> pdfTitles = new ArrayList<>();

        if (rs == null) {
            logger.severe("El ResultSet es nulo. No se puede procesar.");
            return pdfTitles; // Retorna una lista vacía o maneja la situación de forma adecuada
        }

        try {
            ResultSetMetaData metaData = rs.getMetaData(); // Obtener los metadatos del ResultSet
            int columnCount = metaData.getColumnCount(); // Obtener la cantidad de columnas

            while (rs.next()) {
                // Iterar sobre todas las columnas de cada fila
                for (int i = 1; i <= columnCount; i++) {
                    PDF_Income_Title pdfTitle = new PDF_Income_Title();

                    // Obtener el nombre de la columna y su valor
                    String columnName = metaData.getColumnName(i); // Nombre de la columna
                    String columnValue = rs.getString(i); // Valor de la columna

                    // Asignar los valores al objeto PDF_Income_Title
                    pdfTitle.setClave(columnName);
                    pdfTitle.setTextValue(columnValue);

                    // Agregar el objeto a la lista
                    pdfTitles.add(pdfTitle);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al procesar el ResultSet", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close(); // Cerrar el ResultSet para evitar fugas de memoria
                    logger.info("ResultSet cerrado correctamente.");
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error al cerrar ResultSet", e);
                }
            }
        }

        return pdfTitles; // Retornar la lista de objetos PDF_Income_Title
    }

}
