/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.services;

import com.serviceBack.fenix.interfaces.ClientesInterfaces;
import com.serviceBack.fenix.interfaces.FormsInterfaces;
import com.serviceBack.fenix.models.GetFormUser;
import com.serviceBack.fenix.models.GetForms;
import com.serviceBack.fenix.models.GetNit;
import com.serviceBack.fenix.models.NuevoCliente;
import com.serviceBack.fenix.models.SideNav;
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
    public List<GetForms> MenuService(String id) {
        String queryGetForms = stored.STORED_PROCEDURE_CALL_GET_FORMS;
        return jdbcTemplate.query(queryGetForms, new Object[]{id}, new RowMapper<GetForms>() {
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

    @Override
    public List<SideNav> SideNavService() {
        String queryGetSideNav = stored.STORED_PROCEDURE_CALL_GET_SIDE_NAV;
        String usuario = "DEVAGOMEZ";

        return jdbcTemplate.query(queryGetSideNav, new Object[]{usuario}, new RowMapper<SideNav>() {
            @Override
            public SideNav mapRow(ResultSet rs, int rowNum) throws SQLException {
                SideNav sideNav = new SideNav();
                sideNav.setId(rs.getString("id"));
                sideNav.setNombre(rs.getString("nombre"));
                sideNav.setStatus(rs.getString("status"));
                sideNav.setDescription(rs.getString("descripcion"));
                sideNav.setPaquete(rs.getString("paquete"));
                return sideNav;
            }

        });

    }

    @Override
    public List<GetFormUser> FormUserService(String id_form) {
        return jdbcTemplate.query(stored.STORED_PROCEDURE_CALL_GET_STORED_GROUP_FORM, new Object[]{id_form}, new RowMapper<GetFormUser>() {
            @Override
            public GetFormUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetFormUser getFormUser = new GetFormUser();

                // Suponiendo que tienes un ResultSet rs con los resultados de una consulta
                // Selecciona el nombre de la columna en la base de datos que coincide con el nombre del atributo en getFormUser
                // Ejemplo de cómo establecer el valor del atributo "id"
                getFormUser.setId(rs.getInt("id"));

                // Ejemplo de cómo establecer el valor del atributo "stored_group"
                getFormUser.setStored_group(rs.getString("stored_group"));

                // Continúa para establecer los valores de los demás atributos...
                getFormUser.setId_icon(rs.getString("id_icon"));
                getFormUser.setType(rs.getString("type"));
                getFormUser.setTag(rs.getString("tag"));
                getFormUser.setLabel(rs.getString("label"));
                getFormUser.setIcon(rs.getString("icon"));
                getFormUser.setSize(rs.getString("size"));
                getFormUser.setRequired(rs.getString("required"));
                getFormUser.setDisabled(rs.getString("disabled"));
                getFormUser.setPattern(rs.getString("pattern"));
                getFormUser.setVisible(rs.getString("visible"));
                getFormUser.setRead_only(rs.getString("read_only"));
                getFormUser.setEditable(rs.getString("editable"));
                return getFormUser;
            }
        });

    }

}
