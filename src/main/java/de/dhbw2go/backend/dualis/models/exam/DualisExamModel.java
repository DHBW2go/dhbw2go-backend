package de.dhbw2go.backend.dualis.models.exam;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DualisExamModel {

    @NotEmpty
    private final String topic;

    @Nullable
    private final String grade;
}
