package com.serviceBack.fenix.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GenericSQL {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenericSQL(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insert(String sqlQuery, Object[] params) {
        int rowsAffected = jdbcTemplate.update(sqlQuery, params);
        return rowsAffected > 0;
    }
}
