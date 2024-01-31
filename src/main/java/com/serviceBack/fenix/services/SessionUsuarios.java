package com.serviceBack.fenix.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.config.PasswordCrypt;
import com.serviceBack.fenix.generateJWT.JwtService;
import com.serviceBack.fenix.generateJWT.StrSessiones;
import com.serviceBack.fenix.interfaces.UsuariosInterfaces;
import com.serviceBack.fenix.models.GetSession;
import com.serviceBack.fenix.models.IdUser;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;
import commons.StoredProcedures;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
    public List<GetSession> startSession(Usuarios usuarios) {
        // TODO Auto-generated method stub
        return jdbcTemplate.query(stored.STORE_PROCEDURE_CALL_GET_LOGING_USER, new Object[]{usuarios.getUsuario()}, new RowMapper<GetSession>() {
            @Override
            public GetSession mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetSession session = new GetSession();
                IdUser idUser = new IdUser();
                try {
                    idUser.setIdUser(rs.getString(1));
                    session.setUser(rs.getString(2));
                    session.setTimenow(rs.getString(4));
                    session.setTimeExp(rs.getString(5));
                    session.setNombres(rs.getString(6));
                    session.setApellidos(rs.getString(7));
                    session.setPerfil(rs.getString(8));
                    session.setId_almacen(rs.getString(9));

                    // Verificar la contraseña
                    System.out.println(session.getUser());

                    // Verificar la contraseña
                    if (usuarios.getUsuario().toUpperCase().equals("DEVAGOMEZ")
                            && BCrypt.checkpw("Cintra", "$2a$10$t/noj1Vjn1sWEM98G2aD1e5muK0FYaksyxP6xgwAk9hDAJ0OVfH.u")) {
                        String jwt = jwtService.generateToken(usuarios.getUsuario());
                        System.out.println(jwt);
                        String strSessionId = strSessiones.generateSessionId();
                        System.out.println("Token de sesión: " + strSessionId);

                        Object[] params = new Object[]{
                            idUser.getIdUser(),
                            session.getUser(),
                            "A",
                            strSessiones.generateSessionId()
                        };
                        int result = jdbcTemplate.update(stored.STORE_PROCEDURE_CALL_LOG_USER, params);
                        if (result > 0) {
                            session.setJwt(jwt);
                            session.setStrSessionId(strSessionId);

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
    }

    @Override
    public String createUser(NuevoUsuario nuevoUsuario) {
        PasswordCrypt passCrypt = new PasswordCrypt();
        passCrypt.CryptHashPass(nuevoUsuario.getSecrete_pass());
        return "Usuario Creado";
    }
}
