package de.dhbw2go.backend.dualis.models.semester;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DualisSemesterCourseModel {

    @NotEmpty
    private final String moduleId;

    @NotEmpty
    private final String moduleName;

    @Nullable
    private final String credits;

    @Nullable
    private final String grade;

    @Nullable
    private String examsReferenceArguments;
}
