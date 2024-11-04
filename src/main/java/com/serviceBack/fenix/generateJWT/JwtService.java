package com.serviceBack.fenix.generateJWT;


import com.serviceBack.fenix.models.Usuarios;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Usuarios usuario, String perfil, String status) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);  // Una hora de expiración

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        return Jwts.builder()
                .setSubject(usuario.getUsuario())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("perfil", perfil)
                .claim("status", status)
                .claim("chanel", usuario.getChanel())
                .signWith(key)
                .compact();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            return !claims.getExpiration().before(new Date()); // Verifica que el token no haya expirado
        } catch (SignatureException | ExpiredJwtException e) {
            // Token inválido o expirado
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
