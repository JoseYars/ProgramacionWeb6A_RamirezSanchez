package com.example.programacionweb_its_prac1;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserServlet", value = {"/user-servlet", "/user-servlet/*"})
public class UserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (!userDAO.validateToken(token)) {
            sendErrorResponse(response, 422, "Token inválido o faltante");
            return;
        }

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Obtener todos los usuarios
                List<User> users = userDAO.getAllUsers();
                sendJsonResponse(response, HttpServletResponse.SC_OK, users);
            } else {
                // Obtener usuario por ID
                String[] splits = pathInfo.split("/");
                if (splits.length != 2) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Formato de URL inválido");
                    return;
                }

                int userId = Integer.parseInt(splits[1]);
                User user = userDAO.getUserById(userId);

                if (user == null) {
                    sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                    return;
                }

                sendJsonResponse(response, HttpServletResponse.SC_OK, user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en la base de datos");
        } catch (NumberFormatException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "ID debe ser numérico");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (!userDAO.validateToken(token)) {
            sendErrorResponse(response, 422, "Token inválido o faltante");
            return;
        }

        try {
            User newUser = gson.fromJson(request.getReader(), User.class);

            if (newUser.getName() == null || newUser.getEmail() == null || newUser.getPassword() == null) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Faltan campos requeridos");
                return;
            }

            boolean created = userDAO.createUser(newUser);

            if (created) {
                sendJsonResponse(response, HttpServletResponse.SC_CREATED,
                        new JsonResponse("success", "Usuario creado exitosamente"));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al crear usuario");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // Violación de constraint (email duplicado)
                sendErrorResponse(response, HttpServletResponse.SC_CONFLICT, "El email ya está registrado");
            } else {
                e.printStackTrace();
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en la base de datos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, int statusCode, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(gson.toJson(data));
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}