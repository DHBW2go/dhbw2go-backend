package de.dhbw2go.backend.dualis.models.overview;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DualisOverviewCourseModel {

    @NotEmpty
    private final String moduleId;

    @NotEmpty
    private final String moduleName;

    @Nullable
    private final String credits;

    @Nullable
    private final String grade;

    @NotNull
    private final boolean passed;

    @Nullable
    private String examsReferenceArguments;
}
