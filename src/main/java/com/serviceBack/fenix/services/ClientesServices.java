/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.services;

import com.serviceBack.fenix.interfaces.ClientesInterfaces;
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
public class ClientesServices implements ClientesInterfaces {

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored;

    public ClientesServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
    }

    @Override
    public List<GetNit> getNitService(String id_nit) {
        String queryGet = stored.STORE_PROCEDURE_CALL_GET_NIT.concat("(?)");
        return jdbcTemplate.query(queryGet, new Object[]{id_nit}, new RowMapper<GetNit>() {
            @Override
            public GetNit mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetNit nit = new GetNit();
                nit.setId_nit(Integer.parseInt(rs.getString("id")));
                nit.setNit(rs.getString("nit"));
                nit.setTipo(rs.getString("nit"));
                nit.setNombre(rs.getString("nit"));
                nit.setDireccion(rs.getString("nit"));
                nit.setEmail(rs.getString("nit"));
                nit.setFechaRegistro(rs.getString("nit"));
                nit.setEstado(rs.getString("nit"));
                return nit;
            }

        });
    }

    @Override
    public String newClientService(NuevoCliente nuevoCliente) {
        String queryInsert = stored.STORE_PROCEDURE_CALL_NEW_NIT.concat("(?,?,?,?,?,?)");
        Object[] params = new Object[]{
            nuevoCliente.getNit_cui(),
            nuevoCliente.getTipo_doc(),
            nuevoCliente.getNombre(),
            nuevoCliente.getDireccion(),
            nuevoCliente.getEmail(),
            nuevoCliente.getEstado()
        };
        int result = jdbcTemplate.update(queryInsert, params);
        if (result > 0) {
            System.out.println("Ok");
        }
        return null;
    }

    @Override
    public String updateClienteService(UpdateCliente updateCliente) {
        String queryInsert = stored.STORE_PROCEDURE_CALL_UPDATE_NIT.concat("(?,?,?,?)");
        Object[] params = new Object[]{
            updateCliente.getId_cliente(),
            updateCliente.getNombre(),
            updateCliente.getDireccion(),
            updateCliente.getEmail()
        };
        int result = jdbcTemplate.update(queryInsert, params);
        if (result > 0) {
            System.out.println("Ok");
        }
        return null;
    }

}
