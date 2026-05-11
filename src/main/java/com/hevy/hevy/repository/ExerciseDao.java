package com.hevy.hevy.repository;

import com.hevy.hevy.model.BaseExercise;
import com.hevy.hevy.model.StrengthExercise;
import com.hevy.hevy.model.CardioExercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExerciseDao implements BaseDao<BaseExercise> {

    private final Connection connection;

    public ExerciseDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(BaseExercise exercise) {
        String sql = "INSERT INTO exercises (name, category, muscle_group, equipment, owner_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, exercise.getName());
            stmt.setString(2, exercise.getCategory());
            stmt.setString(3, exercise.getMuscleGroup());
            stmt.setString(4, exercise.getEquipment());
            if (exercise.getOwnerId() == null) {
                stmt.setNull(5, Types.BIGINT);
            } else {
                stmt.setLong(5, exercise.getOwnerId());
            }
            stmt.executeUpdate();

            // ambil id yang digenerate DB, set balik ke object
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                exercise.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("gagal menyimpan exercise: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<BaseExercise> findById(Long id) {
        String sql = "SELECT * FROM exercises WHERE id = ? AND is_deleted = FALSE";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("gagla mengambil exercise: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // read all (global + local user)
    @Override
    public List<BaseExercise> findAll() {
        String sql = "SELECT * FROM exercises WHERE is_deleted = FALSE ORDER BY owner_id IS NULL DESC, name ASC";
        List<BaseExercise> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil daftar exercise: " + e.getMessage(), e);
        }
        return list;
    }

    // ── read punya user tertentu + global
    public List<BaseExercise> findAllByUser(Long userId) {
        String sql = "SELECT * FROM exercises WHERE is_deleted = FALSE AND (owner_id IS NULL OR owner_id = ?) ORDER BY owner_id IS NULL DESC, name ASC";
        List<BaseExercise> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("gagal mengambil exercise user: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(BaseExercise exercise) {
        String sql = "UPDATE exercises SET name = ?, muscle_group = ?, equipment = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, exercise.getName());
            stmt.setString(2, exercise.getMuscleGroup());
            stmt.setString(3, exercise.getEquipment());
            stmt.setLong(4, exercise.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("gagal update exercise: " + e.getMessage(), e);
        }
    }

    // soft del
    @Override
    public void delete(Long id) {
        String sql = "UPDATE exercises SET is_deleted = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("gagal menghapus exercise: " + e.getMessage(), e);
        }
    }


    private BaseExercise mapRow(ResultSet rs) throws SQLException {
        Long id            = rs.getLong("id");
        String name        = rs.getString("name");
        String category    = rs.getString("category");
        String muscleGroup = rs.getString("muscle_group");
        String equipment   = rs.getString("equipment");
        Long ownerId       = rs.getObject("owner_id") != null ? rs.getLong("owner_id") : null;

        BaseExercise exercise;
        if ("strength".equals(category)) {
            exercise = new StrengthExercise(id, name, muscleGroup, equipment, ownerId);
        } else {
            exercise = new CardioExercise(id, name, muscleGroup, equipment, ownerId, 0);
        }
        exercise.setDeleted(rs.getBoolean("is_deleted"));
        return exercise;
    }
}