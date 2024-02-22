/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.services;

import com.serviceBack.fenix.interfaces.ClientesInterfaces;
import com.serviceBack.fenix.interfaces.FormsInterfaces;
import com.serviceBack.fenix.models.GetForms;
import com.serviceBack.fenix.models.GetNit;
import com.serviceBack.fenix.models.NuevoCliente;
import com.serviceBack.fenix.models.UpdateCliente;
import commons.StoredProcedures;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @author agr12
 */
@Service
public class FormsServices implements FormsInterfaces {

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored;

    public FormsServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
    }

    @Override
    public List<GetForms> FormsService() {
        String queryGetForms = stored.STORED_PROCEDURE_CALL_GET_FORMS;
        return jdbcTemplate.query(queryGetForms, new RowMapper<GetForms>() {
            @Override
            public GetForms mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetForms getForms = new GetForms();
                getForms.setProfile(rs.getString("profile"));
                getForms.setStatus(rs.getString("status"));
                getForms.setTypeTRX(rs.getString("type_transaction"));
                getForms.setAyuda(rs.getString("ayuda"));
                getForms.setLevel(rs.getString("level"));
                getForms.setType(rs.getString("type"));
                getForms.setNameModule(rs.getString("name_module"));
                getForms.setDescription(rs.getString("description"));
                getForms.setUrl(rs.getString("url"));
                getForms.setImage(rs.getString("image"));
                return getForms;
            }
        });
    }

}
