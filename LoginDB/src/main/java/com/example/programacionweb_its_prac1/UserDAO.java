package com.example.programacionweb_its_prac1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, name, email, created_at FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        null, // No devolvemos la contraseña
                        rs.getTimestamp("created_at")
                );
                users.add(user);
            }
        }
        return users;
    }

    public User getUserById(int id) throws SQLException {
        String query = "SELECT id, name, email, created_at FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null, // No devolvemos la contraseña
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public boolean createUser(User user) throws SQLException {
        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, SHA2(?, 256))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());

            return pstmt.executeUpdate() > 0;
        }
    }

    public User authenticate(String email, String password) throws SQLException {
        String query = "SELECT id, name, email, created_at FROM users WHERE email = ? AND password = SHA2(?, 256)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null, // No devolvemos la contraseña
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public boolean validateToken(String token) {
        // Implementación real de validación de token
        // Esto es solo un ejemplo básico
        return token != null && token.startsWith("Bearer ");
    }
}