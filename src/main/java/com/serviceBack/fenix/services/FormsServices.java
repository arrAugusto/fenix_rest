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
import com.serviceBack.fenix.models.Options_view_kimbo;
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
                getForms.setId(rs.getString("id"));
                getForms.setNombre(rs.getString("nombre"));
                getForms.setStatus(rs.getString("status"));
                getForms.setDescription(rs.getString("description"));
                getForms.setAyuda(rs.getString("ayuda"));
                getForms.setFecha(rs.getString("fecha"));
                getForms.setGrupo(rs.getString("group"));
                getForms.setUrl(rs.getString("URL"));
                getForms.setImage(rs.getString("image"));

                return getForms;
            }
        });
    }

    @Override
    public List<SideNav> SideNavService() {
        String usuario = "DEVAGOMEZ";

        return jdbcTemplate.query(stored.STORED_PROCEDURE_CALL_GET_SIDE_NAV, new Object[]{usuario}, new RowMapper<SideNav>() {
            @Override
            public SideNav mapRow(ResultSet rs, int rowNum) throws SQLException {
                SideNav sideNav = new SideNav();
                sideNav.setId(rs.getString("id"));
                sideNav.setNombre(rs.getString("nombre"));
                sideNav.setStatus(rs.getString("status"));
                sideNav.setDescription(rs.getString("description"));
                sideNav.setPaquete(rs.getString("paquete"));
                sideNav.setGrupo(rs.getString("group"));
                return sideNav;
            }

        });

    }

    //Formularios vistas view
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
                System.out.println("rs.getInt(\"type\")> " + rs.getString("type"));
                if (rs.getString("type").toUpperCase().equals("SELECT")) {
                    getFormUser.setOptions_view_kimbo(findOptions(getFormUser.getId()));
                }
                // Ejemplo de cómo establecer el valor del atributo "id_bodega_afiliada"
                getFormUser.setId_bodega_afiliada(rs.getString("id_bodega_afiliada"));

                // Continúa para establecer los valores de los demás atributos...
                getFormUser.setId_group_view(rs.getString("id_group_view"));
                getFormUser.setId_get_formulario(rs.getString("id_get_formulario"));
                getFormUser.setType_input(rs.getString("type_input"));
                getFormUser.setTag(rs.getString("tag"));
                getFormUser.setLabel(rs.getString("label"));
                getFormUser.setIcon(rs.getString("icon"));
                getFormUser.setSize(rs.getString("size"));
                getFormUser.setRequired(rs.getBoolean("required"));
                getFormUser.setDisabled(rs.getBoolean("disabled"));
                getFormUser.setPattern(rs.getString("pattern"));
                getFormUser.setVisible(rs.getString("visible"));
                getFormUser.setRead_only(rs.getString("read_only"));
                getFormUser.setEditable(rs.getString("editable"));
                getFormUser.setId_icon(rs.getString("id_icon"));
                getFormUser.setType(rs.getString("type"));
                getFormUser.setEstado(rs.getString("estado"));

                return getFormUser;
            }
        });

    }

    public List<Options_view_kimbo> findOptions(int id_kimbo_view) {
        return jdbcTemplate.query(stored.STORE_PROCEDURE_CALL_GET_OPTION_VIEW_KIMBO, new Object[]{id_kimbo_view}, new RowMapper<Options_view_kimbo>() {
            public Options_view_kimbo mapRow(ResultSet rs, int rowNum) throws SQLException {
                Options_view_kimbo option = new Options_view_kimbo();
                option.setId(rs.getInt("id"));
                option.setId_view_kimbo(rs.getInt("id_view_kimbo"));
                option.setValueOption(rs.getString("valueOf"));
                option.setTextValue(rs.getString("text_value"));
                return option;
            }
        });
    }
}
