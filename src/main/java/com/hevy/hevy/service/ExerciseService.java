package com.hevy.hevy.service;

import com.hevy.hevy.model.BaseExercise;
import com.hevy.hevy.model.CardioExercise;
import com.hevy.hevy.model.StrengthExercise;
import com.hevy.hevy.repository.ExerciseDao;

import java.util.List;
import java.util.Optional;

public class ExerciseService {

    private final ExerciseDao exerciseDao;

    public ExerciseService() {
        this.exerciseDao = new ExerciseDao();
    }

    public List<BaseExercise> findLibrary(Long userId) {
        if (userId == null) {
            return exerciseDao.findAll();
        }
        return exerciseDao.findAllByUser(userId);
    }

    public Optional<BaseExercise> findAccessibleById(Long exerciseId, Long userId) {
        Optional<BaseExercise> exercise = exerciseDao.findById(exerciseId);
        if (exercise.isEmpty()) {
            return Optional.empty();
        }

        BaseExercise found = exercise.get();
        if (found.isGlobal() || found.getOwnerId().equals(userId)) {
            return exercise;
        }
        throw new SecurityException("Exercise ini tidak bisa diakses user tersebut");
    }

    public BaseExercise create(String name, String category, String muscleGroup, String equipment, Long ownerId) {
        BaseExercise exercise = buildExercise(null, name, category, muscleGroup, equipment, ownerId);
        exerciseDao.save(exercise);
        return exercise;
    }

    public BaseExercise update(Long exerciseId, Long userId, String name, String muscleGroup, String equipment) {
        BaseExercise exercise = findAccessibleById(exerciseId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise tidak ditemukan"));
        if (exercise.isGlobal() && userId != null) {
            throw new SecurityException("User biasa tidak boleh mengubah exercise global");
        }

        exercise.setName(name);
        exercise.setMuscleGroup(muscleGroup);
        exercise.setEquipment(equipment);
        exerciseDao.update(exercise);
        return exercise;
    }

    public void delete(Long exerciseId, Long userId) {
        BaseExercise exercise = findAccessibleById(exerciseId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise tidak ditemukan"));
        if (exercise.isGlobal() && userId != null) {
            throw new SecurityException("User biasa tidak boleh menghapus exercise global");
        }
        exerciseDao.delete(exerciseId);
    }

    public double calculateTotalVolume(List<BaseExercise> exercises, double weight, int reps, int sets) {
        return exercises.stream()
                .mapToDouble(exercise -> exercise.calculateVolume(weight, reps, sets))
                .sum();
    }

    private BaseExercise buildExercise(Long id, String name, String category, String muscleGroup, String equipment, Long ownerId) {
        if ("cardio".equalsIgnoreCase(category)) {
            return new CardioExercise(id, name, muscleGroup, equipment, ownerId, 0);
        }
        return new StrengthExercise(id, name, muscleGroup, equipment, ownerId);
    }
}
