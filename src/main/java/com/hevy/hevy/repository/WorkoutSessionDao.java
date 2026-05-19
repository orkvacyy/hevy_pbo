package com.hevy.hevy.repository;

import com.hevy.hevy.model.WorkoutExercise;
import com.hevy.hevy.model.WorkoutSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkoutSessionDao implements BaseDao<WorkoutSession> {

    private final Connection connection;
    private final WorkoutExerciseDao workoutExerciseDao;

    public WorkoutSessionDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.workoutExerciseDao = new WorkoutExerciseDao();
    }

    @Override
    public void save(WorkoutSession session) {
        String sql = "INSERT INTO workout_sessions (user_id, notes, started_at, finished_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, session.getUserId());
            stmt.setString(2, session.getNotes());
            setTimestamp(stmt, 3, session.getStartedAt() == null ? LocalDateTime.now() : session.getStartedAt());
            setTimestamp(stmt, 4, session.getFinishedAt());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    session.setId(generatedKeys.getLong(1));
                }
            }

            saveExercises(session);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan workout session: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<WorkoutSession> findById(Long id) {
        String sql = "SELECT * FROM workout_sessions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil workout session: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<WorkoutSession> findAll() {
        String sql = "SELECT * FROM workout_sessions ORDER BY started_at DESC";
        List<WorkoutSession> sessions = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sessions.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil daftar workout session: " + e.getMessage(), e);
        }
        return sessions;
    }

    public List<WorkoutSession> findByUserId(Long userId) {
        String sql = "SELECT * FROM workout_sessions WHERE user_id = ? ORDER BY started_at DESC";
        List<WorkoutSession> sessions = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil sesi user: " + e.getMessage(), e);
        }
        return sessions;
    }

    public Optional<WorkoutSession> findActiveByUserId(Long userId) {
        String sql = "SELECT * FROM workout_sessions WHERE user_id = ? AND finished_at IS NULL ORDER BY started_at DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil sesi aktif user: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void update(WorkoutSession session) {
        String sql = "UPDATE workout_sessions SET notes = ?, started_at = ?, finished_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, session.getNotes());
            setTimestamp(stmt, 2, session.getStartedAt());
            setTimestamp(stmt, 3, session.getFinishedAt());
            stmt.setLong(4, session.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal update workout session: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM workout_sessions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus workout session: " + e.getMessage(), e);
        }
    }

    private void saveExercises(WorkoutSession session) {
        for (WorkoutExercise workoutExercise : session.getExercises()) {
            workoutExercise.setSessionId(session.getId());
            workoutExerciseDao.save(workoutExercise);
        }
    }

    private WorkoutSession mapRow(ResultSet rs) throws SQLException {
        WorkoutSession session = new WorkoutSession(rs.getLong("user_id"));
        session.setId(rs.getLong("id"));
        session.setNotes(rs.getString("notes"));

        Timestamp startedAt = rs.getTimestamp("started_at");
        if (startedAt != null) {
            session.setStartedAt(startedAt.toLocalDateTime());
        }

        Timestamp finishedAt = rs.getTimestamp("finished_at");
        if (finishedAt != null) {
            session.setFinishedAt(finishedAt.toLocalDateTime());
        }

        session.setExercises(workoutExerciseDao.findBySessionId(session.getId()));
        return session;
    }

    private void setTimestamp(PreparedStatement stmt, int index, LocalDateTime value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.TIMESTAMP);
            return;
        }
        stmt.setTimestamp(index, Timestamp.valueOf(value));
    }
}
