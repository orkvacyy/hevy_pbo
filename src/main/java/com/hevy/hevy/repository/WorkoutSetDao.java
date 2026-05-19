package com.hevy.hevy.repository;

import com.hevy.hevy.model.WorkoutSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkoutSetDao implements BaseDao<WorkoutSet> {

    private final Connection connection;

    public WorkoutSetDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(WorkoutSet set) {
        String sql = "INSERT INTO sets (workout_exercise_id, set_number, weight_kg, reps) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, set.getWorkoutExerciseId());
            stmt.setInt(2, set.getSetNumber());
            stmt.setDouble(3, set.getWeightKg());
            stmt.setInt(4, set.getReps());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    set.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan set: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<WorkoutSet> findById(Long id) {
        String sql = "SELECT * FROM sets WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil set: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<WorkoutSet> findAll() {
        String sql = "SELECT * FROM sets ORDER BY workout_exercise_id ASC, set_number ASC";
        List<WorkoutSet> sets = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sets.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil daftar set: " + e.getMessage(), e);
        }
        return sets;
    }

    public List<WorkoutSet> findByWorkoutExerciseId(Long workoutExerciseId) {
        String sql = "SELECT * FROM sets WHERE workout_exercise_id = ? ORDER BY set_number ASC";
        List<WorkoutSet> sets = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, workoutExerciseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil set workout exercise: " + e.getMessage(), e);
        }
        return sets;
    }

    @Override
    public void update(WorkoutSet set) {
        String sql = "UPDATE sets SET set_number = ?, weight_kg = ?, reps = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, set.getSetNumber());
            stmt.setDouble(2, set.getWeightKg());
            stmt.setInt(3, set.getReps());
            stmt.setLong(4, set.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal update set: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM sets WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus set: " + e.getMessage(), e);
        }
    }

    private WorkoutSet mapRow(ResultSet rs) throws SQLException {
        WorkoutSet set = new WorkoutSet(
                rs.getLong("workout_exercise_id"),
                rs.getInt("set_number"),
                rs.getDouble("weight_kg"),
                rs.getInt("reps")
        );
        set.setId(rs.getLong("id"));
        return set;
    }
}
