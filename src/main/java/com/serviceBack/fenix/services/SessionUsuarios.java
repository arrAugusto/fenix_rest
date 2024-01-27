package com.serviceBack.fenix.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.config.PasswordCrypt;
import com.serviceBack.fenix.generateJWT.JwtService;
import com.serviceBack.fenix.interfaces.UsuariosInterfaces;
import com.serviceBack.fenix.models.GetNit;
import com.serviceBack.fenix.models.GetSession;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;
import commons.StoredProcedures;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Service
public class SessionUsuarios implements UsuariosInterfaces {

    @Autowired
    private JwtService jwtService;
    private StoredProcedures stored;
    private JdbcTemplate jdbcTemplate;

    @Override
    public String startSession(Usuarios usuarios) {
        // TODO Auto-generated method stub
        jdbcTemplate.query(stored.STORE_PROCEDURE_CALL_GET_LOGING_USER, new Object[]{usuarios.getUsuario()}, new RowMapper<GetSession>() {
            @Override
            public GetSession mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetSession session = new GetSession();
                session.setAsIdUser(rs.getString(1));
                session.setUser(rs.getString(2));
                session.setPass(rs.getString(3));
                session.setTimenow(rs.getString(4));
                session.setTimeExp(rs.getString(5));
                // Verificar la contraseña
                if (usuarios.getUsuario().toUpperCase().equals("DEVAGOMEZ")
                        && BCrypt.checkpw("Cintra", "$2a$10$t/noj1Vjn1sWEM98G2aD1e5muK0FYaksyxP6xgwAk9hDAJ0OVfH.u")) {

                    String jwt = "jwt: " + jwtService.generateToken(usuarios.getUsuario());

                } else {
                    return session;
                }
                return session;

            }

        });

        return null;

    }

    @Override
    public String createUser(NuevoUsuario nuevoUsuario) {
        PasswordCrypt passCrypt = new PasswordCrypt();
        passCrypt.CryptHashPass(nuevoUsuario.getSecrete_pass());
        return "Usuario Creado";
    }
}
