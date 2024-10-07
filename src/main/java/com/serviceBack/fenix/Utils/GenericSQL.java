package com.serviceBack.fenix.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class GenericSQL {

    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(GenericSQL.class);

    @Autowired
    public GenericSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Método genérico para ejecutar una consulta de inserción utilizando
     * PreparedStatement.
     *
     * @param sqlQuery La consulta SQL en formato de String.
     * @param params Los parámetros a insertar en la consulta.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insert(String sqlQuery, Object[] params) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            ps = connection.prepareStatement(sqlQuery);
            setParameters(ps, params);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta de inserción: " + e.getMessage(), e);
            return false;
        } finally {
            closeResources(null, ps, connection);
        }
    }

    /**
     * Método genérico para ejecutar una consulta SELECT y devolver el ResultSet
     * sin cerrarlo. El cierre del ResultSet se debe hacer manualmente en el
     * lugar de uso.
     *
     * @param sqlQuery La consulta SQL en formato de String.
     * @param params Los parámetros a insertar en la consulta.
     * @return El ResultSet de la consulta ejecutada.
     */
    public ResultSet select(String sqlQuery, Object[] params) {
        try {
            Connection connection = DataSourceUtils.getConnection(dataSource);
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
            setParameters(ps, params);
            return ps.executeQuery();  // Devuelve el ResultSet, el cual debe ser cerrado manualmente
        } catch (SQLException e) {
            logger.error("Error al ejecutar la consulta SELECT: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Método auxiliar para establecer los parámetros en el PreparedStatement.
     *
     * @param ps El PreparedStatement.
     * @param params Los parámetros a insertar en la consulta.
     * @throws SQLException Si ocurre algún error al establecer los parámetros.
     */
    private void setParameters(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    /**
     * Cierra el ResultSet, PreparedStatement y Connection si no son nulos.
     *
     * @param rs El ResultSet a cerrar.
     * @param ps El PreparedStatement a cerrar.
     * @param connection La conexión a cerrar.
     */
    public void closeResources(ResultSet rs, PreparedStatement ps, Connection connection) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("Error al cerrar ResultSet: " + e.getMessage(), e);
        }

        try {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        } catch (SQLException e) {
            logger.error("Error al cerrar PreparedStatement: " + e.getMessage(), e);
        }

        try {
            if (connection != null && !connection.isClosed()) {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        } catch (SQLException e) {
            logger.error("Error al cerrar Connection: " + e.getMessage(), e);
        }
    }
}
