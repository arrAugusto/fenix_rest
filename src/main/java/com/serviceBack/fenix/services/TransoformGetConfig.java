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

    public List<PDF_Income_Title> transformDataConfig(ResultSet rs, String titleTransaction) {
        List<PDF_Income_Title> pdfTitles = new ArrayList<>();

        if (rs == null) {
            logger.severe("El ResultSet es nulo. No se puede procesar.");
            return pdfTitles; // Retorna una lista vacía o maneja la situación de forma adecuada
        }
        try {
            ResultSetMetaData metaData = rs.getMetaData(); // Obtener los metadatos del ResultSet
            int columnCount = metaData.getColumnCount(); // Obtener la cantidad de columnas
            while (rs.next()) {
                // Crear un nuevo PDF_Income_Title por cada fila del ResultSet
                PDF_Income_Title pdfTitle = new PDF_Income_Title();
                pdfTitle.setTitleTransaction(titleTransaction); // Asignar el título de la transacción

                // Lista para almacenar los detalles de esta fila
                List<PDF_Income_Title.Detail> details = new ArrayList<>();

                // Iterar sobre todas las columnas de la fila actual
                for (int i = 1; i <= columnCount; i++) {
                    // Crear un nuevo Detail para cada columna
                    PDF_Income_Title.Detail detail = new PDF_Income_Title.Detail();

                    // Obtener el nombre y el valor de cada columna
                    String columnName = metaData.getColumnName(i); // Nombre de la columna
                    String columnValue = rs.getString(i); // Valor de la columna

                    // Establecer la clave y el valor en el objeto Detail
                    detail.setClave(columnName);
                    detail.setTextValue(columnValue);

                    // Agregar el Detail a la lista de detalles
                    details.add(detail);
                }

                // Asignar la lista de detalles al PDF_Income_Title
                pdfTitle.setDetails(details);

                // Agregar el PDF_Income_Title a la lista de resultados
                pdfTitles.add(pdfTitle);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al procesar el ResultSet", e);
        }

        return pdfTitles; // Retornar la lista de objetos PDF_Income_Title
    }
}
