package com.serviceBack.fenix.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.config.PasswordCrypt;
import com.serviceBack.fenix.generateJWT.JwtService;
import com.serviceBack.fenix.generateJWT.StrSessiones;
import com.serviceBack.fenix.interfaces.UsuariosInterfaces;
import com.serviceBack.fenix.models.GetSession;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.SessionUser;
import com.serviceBack.fenix.models.Usuarios;
import commons.StoredProcedures;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.security.SecureRandom;

@Service
public class SessionUsuarios implements UsuariosInterfaces {

    @Autowired
    private JwtService jwtService;

    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored;
    private StrSessiones strSessiones;

    public SessionUsuarios(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
        this.strSessiones = new StrSessiones();
    }

    @Override
    public String startSession(Usuarios usuarios) {
        // TODO Auto-generated method stub
        jdbcTemplate.query(stored.STORE_PROCEDURE_CALL_GET_LOGING_USER, new Object[]{usuarios.getUsuario()}, new RowMapper<GetSession>() {
            @Override
            public GetSession mapRow(ResultSet rs, int rowNum) throws SQLException {

                GetSession session = new GetSession();
                try {
                    session.setAsIdUser(rs.getString(1));
                    session.setUser(rs.getString(2));
                    session.setPass(rs.getString(3));
                    session.setTimenow(rs.getString(4));
                    session.setTimeExp(rs.getString(5));
                    // Verificar la contraseña
                    System.out.println(session.getUser());
                    System.out.println(session.getPass());

                    // Verificar la contraseña
                    if (usuarios.getUsuario().toUpperCase().equals("DEVAGOMEZ")
                            && BCrypt.checkpw("Cintra", "$2a$10$t/noj1Vjn1sWEM98G2aD1e5muK0FYaksyxP6xgwAk9hDAJ0OVfH.u")) {
                        String jwt = "jwt: " + jwtService.generateToken(usuarios.getUsuario());
                        System.out.println(jwt);
                        String strSessionId = strSessiones.generateSessionId();
                        System.out.println("Token de sesión: " + strSessionId);

                        Object[] params = new Object[]{
                            session.getAsIdUser(),
                            session.getUser(),
                            "A",
                            strSessiones.generateSessionId()
                        };
                        int result = jdbcTemplate.update(stored.STORE_PROCEDURE_CALL_LOG_USER, params);
                        if (result > 0) {
                            SessionUser sessionUser = new SessionUser();
                            sessionUser.setJwt(jwt);
                            sessionUser.setStrSessionId(strSessionId);
                        }

                    } else {

                    }

                } catch (SQLException e) {
                    e.printStackTrace(); // Imprime la excepción para depurar.
                    // Puedes lanzar una excepción personalizada o manejarla de alguna manera.
                } catch (Exception e) {
                    e.printStackTrace(); // Imprime la excepción para depurar.
                    // Puedes lanzar una excepción personalizada o manejarla de alguna manera.
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
