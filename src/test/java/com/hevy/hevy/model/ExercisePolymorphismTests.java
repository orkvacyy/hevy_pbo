package com.hevy.hevy.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExercisePolymorphismTests {

    @Test
    void calculateVolumeUsesOverriddenMethodsThroughBaseExerciseList() {
        List<BaseExercise> exercises = List.of(
                new StrengthExercise(1L, "Bench Press", "chest", "barbell", null),
                new CardioExercise(2L, "Treadmill", "other", "machine", null, 30)
        );

        List<Double> volumes = exercises.stream()
                .map(exercise -> exercise.calculateVolume(20, 10, 3))
                .toList();

        assertThat(volumes).containsExactly(600.0, 90.0);
    }
}
