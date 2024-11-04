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
        String status;

        try {
            // Verificar el estado del usuario
            status = jdbcTemplate.queryForObject(checkStatusSql, new Object[]{usuarios.getUsuario()}, String.class);

            if (!"A".equalsIgnoreCase(status)) {
                // Usuario inactivo
                responseService.setCodeResponse("403");
                responseService.setMessageResponse("Usuario inactivo");
                responseService.setData(null);
                return responseService;
            }

        } catch (EmptyResultDataAccessException e) {
            // Usuario no encontrado
            responseService.setCodeResponse("404");
            responseService.setMessageResponse("Usuario no existe");
            responseService.setData(null);
            return responseService;
        }

        // Usuario activo, proceder con el proceso de inicio de sesión
        try {
            List<GetSession> sessions = jdbcTemplate.query(stored.STORED_PROCEDURE_CALL_GET_LOGING_USER, new Object[]{usuarios.getUsuario()}, new RowMapper<GetSession>() {
                @Override
                public GetSession mapRow(ResultSet rs, int rowNum) throws SQLException {
                    GetSession session = new GetSession();
                    session.setUser(rs.getString("usuario"));

                    // Verificar la contraseña
                    if (usuarios.getUsuario().toUpperCase().equals(rs.getString("usuario"))
                            && BCrypt.checkpw(usuarios.getSecrete_pass(), rs.getString("secrete_pass"))) {

                        // Configuración de la sesión
                        session.setPerfil(rs.getString("perfil"));
                        session.setStatus(rs.getString("status"));

                        // Generación del JWT
                        String idUser = rs.getString(1);
                        String jwt = jwtService.generateToken(usuarios, session.getPerfil(), session.getStatus());
                        System.out.println(jwt);

                        // Generación del ID de sesión
                        String strSessionId = strSessiones.generateSessionId();
                        System.out.println("Token de sesión: " + strSessionId);

                        Object[] params = new Object[]{
                            Integer.parseInt(idUser),
                            usuarios.getUsuario(),
                            "A",
                            strSessionId,
                            usuarios.getChanel(),
                            jwt
                        };

                        // Ejecutar el procedimiento de log
                        int result = jdbcTemplate.update(stored.STORED_PROCEDURE_CALL_INSERT_USER_LOG, params);
                        if (result > 0) {
                            session.setJwt(jwt);
                            session.setStrSessionId(strSessionId);
                        }

                        // Configurar la respuesta con el JWT
                        responseService.setCodeResponse("200");
                        responseService.setMessageResponse("Sesión iniciada correctamente");
                        responseService.setData(Arrays.asList(session));
                    } else {
                        throw new RuntimeException("Contraseña incorrecta");
                    }

                    return session;
                }
            });

        } catch (Exception e) {
            // Error en el proceso de autenticación
            responseService.setCodeResponse("500");
            responseService.setMessageResponse("Error interno: " + e.getMessage());
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
        responseService.setCodeResponse("200");
        responseService.setMessageResponse("Usuarios obtenidos exitosamente");
        responseService.setData(Arrays.asList(usuarios));

        return responseService;
    }
}
