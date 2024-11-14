package com.serviceBack.fenix.services;

import com.serviceBack.fenix.models.GetUsuario;
import com.serviceBack.fenix.Utils.ResponseService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.config.PasswordCrypt;
import com.serviceBack.fenix.generateJWT.JwtService;
import com.serviceBack.fenix.generateJWT.StrSessiones;
import com.serviceBack.fenix.interfaces.UsuariosInterfaces;
import com.serviceBack.fenix.models.GetSession;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.ResetPasswordRequest;
import com.serviceBack.fenix.models.UpdateUserRequest;
import com.serviceBack.fenix.models.Usuarios;
import commons.StoredProcedures;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public ResponseService startSession(Usuarios usuarios) {
        ResponseService responseService = new ResponseService();
        String checkStatusSql = "SELECT status FROM usuarios_kimbo WHERE usuario = ?";

        try {
            // Verificar el estado del usuario
            String status = jdbcTemplate.queryForObject(checkStatusSql, new Object[]{usuarios.getUsuario()}, String.class);

            if (!"A".equalsIgnoreCase(status)) {
                // Mensaje de error general para usuarios inactivos
                responseService.setCodeResponse("401");
                responseService.setMessageResponse("Usuario o contraseña incorrectos");
                responseService.setData(null);
                return responseService;
            }

        } catch (EmptyResultDataAccessException e) {
            // Usuario no encontrado, pero el mensaje es generalizado
            responseService.setCodeResponse("401");
            responseService.setMessageResponse("Usuario o contraseña incorrectos");
            responseService.setData(null);
            return responseService;
        }

        // Usuario activo, proceder con el proceso de inicio de sesión
        try {
            jdbcTemplate.query(stored.STORED_PROCEDURE_CALL_GET_LOGING_USER, new Object[]{usuarios.getUsuario()},
                    new RowMapper<GetSession>() {
                @Override
                public GetSession mapRow(ResultSet rs, int rowNum) throws SQLException {
                    GetSession session = new GetSession();

                    // Verificar la contraseña de manera segura
                    if (BCrypt.checkpw(usuarios.getSecrete_pass(), rs.getString("secrete_pass"))) {
                        session.setUser(rs.getString("usuario"));
                        session.setPerfil(rs.getString("perfil"));
                        session.setStatus(rs.getString("status"));

                        // Generación del JWT
                        String jwt = jwtService.generateToken(usuarios, session.getPerfil(), session.getStatus());

                        // Generación del ID de sesión
                        String strSessionId = strSessiones.generateSessionId();

                        Object[] params = new Object[]{
                            Integer.parseInt(rs.getString(1)),
                            usuarios.getUsuario(),
                            "A",
                            strSessionId,
                            usuarios.getChanel(),
                            jwt
                        };

                        // Ejecutar el procedimiento de log
                        jdbcTemplate.update(stored.STORED_PROCEDURE_CALL_INSERT_USER_LOG, params);

                        session.setJwt(jwt);
                        session.setStrSessionId(strSessionId);

                        // Configurar la respuesta exitosa
                        responseService.setCodeResponse("00");
                        responseService.setMessageResponse("Sesión iniciada correctamente");
                        responseService.setData(Arrays.asList(session));
                    } else {
                        throw new RuntimeException("Usuario o contraseña incorrectos");
                    }

                    return session;
                }
            });

        } catch (Exception e) {
            // Manejo de error generalizado para cualquier problema durante la autenticación
            responseService.setCodeResponse("401");
            responseService.setMessageResponse("Usuario o contraseña incorrectos");
            responseService.setData(null);
        }

        return responseService;
    }

    @Override
    public String createUser(NuevoUsuario nuevoUsuario) {
        PasswordCrypt passCrypt = new PasswordCrypt();

        // Encriptar la contraseña
        String encryptedPassword = passCrypt.CryptHashPass(nuevoUsuario.getSecrete_pass());

        // Consulta para insertar el nuevo usuario
        String sql = "INSERT INTO usuarios_kimbo (nombres, apellidos, codigoEmpleado, perfil, status, usuario, secrete_pass, fecha_creacion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

        // Ejecutar el insert
        int result = jdbcTemplate.update(sql, nuevoUsuario.getNombres(), nuevoUsuario.getApellidos(), nuevoUsuario.getCodigoEmpleado(),
                nuevoUsuario.getPerfil(), nuevoUsuario.getStatus(), nuevoUsuario.getUsuario(), encryptedPassword);

        // Verificar si el usuario fue creado exitosamente
        return result > 0 ? "Usuario Creado" : "Error al crear usuario";
    }

    @Override
    public boolean updateUserStatusOrProfile(UpdateUserRequest updateUserRequest) {
        String sql = "UPDATE usuarios_kimbo SET perfil = ?, status = ? WHERE id = ?";
        int result = jdbcTemplate.update(sql, updateUserRequest.getPerfil(), updateUserRequest.getStatus(), updateUserRequest.getId());
        return result > 0;
    }

    @Override
    public boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
        // Verificar si el usuario está activo
        String checkStatusSql = "SELECT COUNT(*) FROM usuarios_kimbo WHERE id = ? AND status = 'A'";
        Integer count = jdbcTemplate.queryForObject(checkStatusSql, new Object[]{resetPasswordRequest.getId()}, Integer.class);

        if (count != null && count > 0) {
            // Si el usuario está activo, encriptar la nueva contraseña y actualizar
            PasswordCrypt passCrypt = new PasswordCrypt();
            String hashedPassword = passCrypt.CryptHashPass(resetPasswordRequest.getNewPassword());

            String sql = "UPDATE usuarios_kimbo SET secrete_pass = ? WHERE id = ?";
            int result = jdbcTemplate.update(sql, hashedPassword, resetPasswordRequest.getId());
            return result > 0;
        } else {
            // Si el usuario no está activo, no realizar el cambio de contraseña
            return false;
        }
    }

    @Override
    public ResponseService getAllUsers() {
        String sql = "SELECT id, nombres, apellidos, codigoEmpleado, perfil, status, usuario, fecha_creacion, imagen_perfil, paquete FROM kimbo_database.usuarios_kimbo";

        List<GetUsuario> usuarios = jdbcTemplate.query(sql, new RowMapper<GetUsuario>() {
            @Override
            public GetUsuario mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetUsuario usuario = new GetUsuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setCodigoEmpleado(rs.getString("codigoEmpleado"));
                usuario.setPerfil(rs.getString("perfil"));
                usuario.setStatus(rs.getString("status"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setFechaCreacion(rs.getString("fecha_creacion"));  // Ajuste a Timestamp para fecha
                usuario.setImagenPerfil(rs.getString("imagen_perfil"));
                usuario.setPaquete(rs.getInt("paquete"));
                return usuario;
            }
        });

        ResponseService<List<GetUsuario>> responseService = new ResponseService<>();
        responseService.setCodeResponse("00");
        responseService.setMessageResponse("Usuarios obtenidos exitosamente");
        responseService.setData(Arrays.asList(usuarios));

        return responseService;
    }
}
