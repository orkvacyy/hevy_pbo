package com.hevy.hevy.repository;

import com.hevy.hevy.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements BaseDao<User> {

    private final Connection connection;

    public UserDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, email, password, role, is_active) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setBoolean(5, user.isActive());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil user: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil user berdasarkan email: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil user berdasarkan username: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil daftar user: " + e.getMessage(), e);
        }
        return users;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, role = ?, is_active = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setBoolean(5, user.isActive());
            stmt.setLong(6, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal update user: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE users SET is_active = FALSE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menonaktifkan user: " + e.getMessage(), e);
        }
    }

    public void resetPassword(Long id, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password baru tidak boleh kosong");
        }

        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal reset password user: " + e.getMessage(), e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password")
        );
        user.setId(rs.getLong("id"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        return user;
    }
}
