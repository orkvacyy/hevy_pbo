package com.hevy.hevy.service;

import com.hevy.hevy.model.BaseExercise;
import com.hevy.hevy.model.User;
import com.hevy.hevy.model.WorkoutExercise;
import com.hevy.hevy.model.WorkoutSession;
import com.hevy.hevy.model.WorkoutSet;
import com.hevy.hevy.repository.ExerciseDao;
import com.hevy.hevy.repository.UserDao;
import com.hevy.hevy.repository.WorkoutExerciseDao;
import com.hevy.hevy.repository.WorkoutSessionDao;
import com.hevy.hevy.repository.WorkoutSetDao;
import java.util.List;
import java.util.Optional;

public class WorkoutService {

    private final ExerciseDao exerciseDao;
    private final UserDao userDao;
    private final WorkoutSessionDao sessionDao;
    private final WorkoutExerciseDao workoutExerciseDao;
    private final WorkoutSetDao workoutSetDao;

    public WorkoutService() {
        this.exerciseDao = new ExerciseDao();
        this.userDao = new UserDao();
        this.sessionDao = new WorkoutSessionDao();
        this.workoutExerciseDao = new WorkoutExerciseDao();
        this.workoutSetDao = new WorkoutSetDao();
    }

    public WorkoutSession startSession(Long userId) {
        validateActiveUser(userId);
        Optional<WorkoutSession> activeSession = sessionDao.findActiveByUserId(userId);
        if (activeSession.isPresent()) {
            return activeSession.get();
        }

        WorkoutSession session = new WorkoutSession(userId);
        session.start();
        sessionDao.save(session);
        return session;
    }

    public Optional<WorkoutSession> findActiveSession(Long userId) {
        validateActiveUser(userId);
        return sessionDao.findActiveByUserId(userId);
    }

    public List<WorkoutSession> findSessionsByUser(Long userId) {
        validateActiveUser(userId);
        return sessionDao.findByUserId(userId);
    }

    public WorkoutExercise addExercise(Long userId, Long sessionId, Long exerciseId) {
        WorkoutSession session = findOwnedSession(userId, sessionId);
        if (!session.isActive()) {
            throw new IllegalStateException("Sesi sudah selesai");
        }

        BaseExercise exercise = exerciseDao.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise tidak ditemukan"));
        ensureExerciseAccessible(userId, exercise);

        int orderIndex = session.getExercises().size() + 1;
        WorkoutExercise workoutExercise = new WorkoutExercise(
                session.getId(),
                exercise.getId(),
                exercise.getName(),
                orderIndex
        );
        workoutExerciseDao.save(workoutExercise);
        return workoutExercise;
    }

    public WorkoutSession finishSession(Long userId, Long sessionId, String notes) {
        WorkoutSession session = findOwnedSession(userId, sessionId);
        session.finish(notes);
        sessionDao.update(session);
        return sessionDao.findById(sessionId).orElse(session);
    }

    public void cancelSession(Long userId, Long sessionId) {
        findOwnedSession(userId, sessionId);
        sessionDao.delete(sessionId);
    }

    public WorkoutSet addSet(Long userId, Long workoutExerciseId, double weightKg, int reps) {
        validateActiveUser(userId);

        // memastikan workout exercise ini milik user yang benar
        WorkoutExercise we = workoutExerciseDao.findById(workoutExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Workout exercise tidak ditemukan"));
        findOwnedSession(userId, we.getSessionId());

        // set number = jumlah set yang sudah ada + 1
        List<WorkoutSet> existing = workoutSetDao.findByWorkoutExerciseId(workoutExerciseId);
        int setNumber = existing.size() + 1;

        WorkoutSet set = new WorkoutSet(workoutExerciseId, setNumber, weightKg, reps);
        workoutSetDao.save(set);
        return set;
    }

    public void deleteSet(Long userId, Long setId) {
        validateActiveUser(userId);
        WorkoutSet set = workoutSetDao.findById(setId)
                .orElseThrow(() -> new IllegalArgumentException("Set tidak ditemukan"));

        WorkoutExercise we = workoutExerciseDao.findById(set.getWorkoutExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Workout exercise tidak ditemukan"));
        findOwnedSession(userId, we.getSessionId());

        workoutSetDao.delete(setId);
    }

    private WorkoutSession findOwnedSession(Long userId, Long sessionId) {
        validateActiveUser(userId);
        WorkoutSession session = sessionDao.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesi workout tidak ditemukan"));
        if (!session.getUserId().equals(userId)) {
            throw new SecurityException("User tidak boleh mengakses sesi milik user lain");
        }
        return session;
    }

    private void ensureExerciseAccessible(Long userId, BaseExercise exercise) {
        if (exercise.isGlobal()) {
            return;
        }
        if (!exercise.getOwnerId().equals(userId)) {
            throw new SecurityException("User tidak boleh memilih exercise milik user lain");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId wajib diisi");
        }
    }

    private void validateActiveUser(Long userId) {
        validateUserId(userId);
        User user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
        if (!user.isActive()) {
            throw new IllegalArgumentException("User sedang tidak aktif");
        }
    }
}
