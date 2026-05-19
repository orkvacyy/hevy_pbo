package com.hevy.hevy.repository;

import com.hevy.hevy.model.WorkoutExercise;
import com.hevy.hevy.model.WorkoutSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkoutExerciseDao implements BaseDao<WorkoutExercise> {

    private final Connection connection;
    private final WorkoutSetDao workoutSetDao;

    public WorkoutExerciseDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.workoutSetDao = new WorkoutSetDao();
    }

    @Override
    public void save(WorkoutExercise workoutExercise) {
        String sql = "INSERT INTO workout_exercises (session_id, exercise_id, exercise_name_snapshot, order_index) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, workoutExercise.getSessionId());
            stmt.setLong(2, workoutExercise.getExerciseId());
            stmt.setString(3, workoutExercise.getExerciseNameSnapshot());
            stmt.setInt(4, workoutExercise.getOrderIndex());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    workoutExercise.setId(generatedKeys.getLong(1));
                }
            }

            saveSets(workoutExercise);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan workout exercise: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<WorkoutExercise> findById(Long id) {
        String sql = "SELECT * FROM workout_exercises WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil workout exercise: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<WorkoutExercise> findAll() {
        String sql = "SELECT * FROM workout_exercises ORDER BY session_id ASC, order_index ASC";
        List<WorkoutExercise> exercises = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                exercises.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil daftar workout exercise: " + e.getMessage(), e);
        }
        return exercises;
    }

    public List<WorkoutExercise> findBySessionId(Long sessionId) {
        String sql = "SELECT * FROM workout_exercises WHERE session_id = ? ORDER BY order_index ASC";
        List<WorkoutExercise> exercises = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, sessionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    exercises.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil exercise dalam sesi: " + e.getMessage(), e);
        }
        return exercises;
    }

    @Override
    public void update(WorkoutExercise workoutExercise) {
        String sql = "UPDATE workout_exercises SET exercise_name_snapshot = ?, order_index = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, workoutExercise.getExerciseNameSnapshot());
            stmt.setInt(2, workoutExercise.getOrderIndex());
            stmt.setLong(3, workoutExercise.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal update workout exercise: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM workout_exercises WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus workout exercise: " + e.getMessage(), e);
        }
    }

    private void saveSets(WorkoutExercise workoutExercise) {
        for (WorkoutSet set : workoutExercise.getSets()) {
            set.setWorkoutExerciseId(workoutExercise.getId());
            workoutSetDao.save(set);
        }
    }

    private WorkoutExercise mapRow(ResultSet rs) throws SQLException {
        WorkoutExercise workoutExercise = new WorkoutExercise(
                rs.getLong("session_id"),
                rs.getLong("exercise_id"),
                rs.getString("exercise_name_snapshot"),
                rs.getInt("order_index")
        );
        workoutExercise.setId(rs.getLong("id"));
        workoutExercise.setSets(workoutSetDao.findByWorkoutExerciseId(workoutExercise.getId()));
        return workoutExercise;
    }
}
