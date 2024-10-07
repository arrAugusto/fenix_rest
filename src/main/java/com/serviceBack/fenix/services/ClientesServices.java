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
        return jdbcTemplate.query(stored.STORED_PROCEDURE_CALL_GET_NIT, new Object[]{id_nit}, new RowMapper<GetNit>() {
            @Override
            public GetNit mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetNit nit = new GetNit();
                nit.setId_nit(Integer.parseInt(rs.getString("id")));
                nit.setNit(rs.getString("nit"));
                nit.setNombreEmpresa(rs.getString("nombre_empresa"));
                nit.setDireccion(rs.getString("direccion"));
                nit.setTipoDocumento(rs.getString("tipo_documento"));
                nit.setEmail(rs.getString("email"));
                nit.setFechaRegistro(rs.getString("fecha_registro"));
                nit.setEstado(rs.getString("estado"));
                return nit;
            }

        });
    }

    @Override
    public String newClientService(NuevoCliente nuevoCliente) {
        String queryInsert = stored.STORED_PROCEDURE_CALL_NEW_NIT.concat("(?,?,?,?,?,?)");
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
        String queryInsert = stored.STORED_PROCEDURE_CALL_UPDATE_NIT.concat("(?,?,?,?)");
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

    @Override
    public boolean validateNit(String nit) {
        if (nit == null || nit.isEmpty()) {
            return true;
        }

        String nitRegExp = "^[0-9]+(-?[0-9kK])?$";

        if (!nit.matches(nitRegExp)) {
            return false;
        }

        nit = nit.replace("-", "");
        int lastChar = nit.length() - 1;
        String number = nit.substring(0, lastChar);
        String expectedChecker = nit.substring(lastChar).toLowerCase();

        int factor = number.length() + 1;
        int total = 0;

        for (int i = 0; i < number.length(); i++) {
            char character = number.charAt(i);
            int digit = Character.getNumericValue(character);

            total += (digit * factor);
            factor--;
        }

        int modulus = (11 - (total % 11)) % 11;
        String computedChecker = (modulus == 10 ? "k" : Integer.toString(modulus));

        return expectedChecker.equals(computedChecker);
    }
    
    @Override
    public boolean validateCUI(String cui) {
        if (cui == null || cui.isEmpty()) {
            System.out.println("CUI vacío");
            return true;
        }

        String cuiRegExp = "^[0-9]{4}\\s?[0-9]{5}\\s?[0-9]{4}$";

        if (!cui.matches(cuiRegExp)) {
            System.out.println("CUI con formato inválido");
            return false;
        }

        cui = cui.replace(" ", "");
        int depto = Integer.parseInt(cui.substring(9, 11));
        int muni = Integer.parseInt(cui.substring(11, 13));
        String numero = cui.substring(0, 8);
        int verificador = Integer.parseInt(cui.substring(8, 9));

        int[] munisPorDepto = {
            17, 8, 16, 16, 13, 14, 19, 8, 24, 21, 9,
            30, 32, 21, 8, 17, 14, 5, 11, 11, 7, 17
        };

        if (depto == 0 || muni == 0) {
            System.out.println("CUI con código de municipio o departamento inválido.");
            return false;
        }

        if (depto > munisPorDepto.length) {
            System.out.println("CUI con código de departamento inválido.");
            return false;
        }

        if (muni > munisPorDepto[depto - 1]) {
            System.out.println("CUI con código de municipio inválido.");
            return false;
        }

        int total = 0;
        for (int i = 0; i < numero.length(); i++) {
            total += Character.getNumericValue(numero.charAt(i)) * (i + 2);
        }

        int modulo = total % 11;
        System.out.println("CUI con módulo: " + modulo);

        return modulo == verificador;
    }

}
