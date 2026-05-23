package com.hevy.hevy.service;

import com.hevy.hevy.dto.request.CreateExerciseRequest;
import com.hevy.hevy.model.BaseExercise;
import com.hevy.hevy.model.User;
import com.hevy.hevy.repository.ExerciseDao;
import com.hevy.hevy.repository.UserDao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class AdminService {

    private final UserDao userDao;
    private final ExerciseDao exerciseDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminService() {
        this.userDao         = new UserDao();
        this.exerciseDao     = new ExerciseDao();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    //user management

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User deactivateUser(Long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
        user.setActive(false);
        userDao.update(user);
        return user;
    }

    public void hardDeleteUser(Long userId) {
        userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
        userDao.hardDelete(userId);
    }

    public User activateUser(Long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
        user.setActive(true);
        userDao.update(user);
        return user;
    }

    public void resetPassword(Long userId, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password baru tidak boleh kosong");
        }
        userDao.resetPassword(userId, passwordEncoder.encode(newPassword));
    }

    // global exercise

    public BaseExercise createGlobalExercise(CreateExerciseRequest req) {
        ExerciseService exerciseService = new ExerciseService();
        return exerciseService.create(
                req.getName(),
                req.getCategory(),
                req.getMuscleGroup(),
                req.getEquipment(),
                null // owner null = global
        );
    }

    public BaseExercise updateGlobalExercise(Long exerciseId, CreateExerciseRequest req) {
        ExerciseService exerciseService = new ExerciseService();
        return exerciseService.update(exerciseId, null, req.getName(), req.getMuscleGroup(), req.getEquipment());
    }

    public void deleteGlobalExercise(Long exerciseId) {
        exerciseDao.delete(exerciseId);
    }
}