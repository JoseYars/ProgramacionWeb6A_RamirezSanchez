package com.example.programacionweb_its_prac1;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.example.programacionweb_its_prac1.AutenticacionServlet.generalKey;

@WebServlet("/user-servlet/*")
public class UserServlet extends HttpServlet {
    private final JsonResponse jResp = new JsonResponse();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String authTokenHeader = req.getHeader("Authorization");
        validateAuthToken(req, resp, authTokenHeader.split(" ")[1]);
    }

    private void validateAuthToken(HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(generalKey())
                .build();
        try {
            // Parsear el token y verificar
            Jws<Claims> claims = jwtParser.parseSignedClaims(token);

            // Encontrar el usuario
            String username = claims.getPayload().getSubject();
            User user = null;
            for (User u : AutenticacionServlet.users.values()) {
                if (u.getUsername().equals(username)) {
                    user = u;
                    break;
                }
            }

            if (user != null) {
                // Crear un mapa con los datos del usuario (excluyendo la contraseña)
                Map<String, Object> userData = new HashMap<>();
                userData.put("fullName", user.getFullName());
                userData.put("email", user.getEmail());
                userData.put("username", user.getUsername());

                jResp.success(req, resp, "Datos de usuario", userData);
            } else {
                jResp.failed(req, resp, "Usuario no encontrado", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ExpiredJwtException e) {
            jResp.failed(req, resp, "Token expirado", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            jResp.failed(req, resp, "Token inválido: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}