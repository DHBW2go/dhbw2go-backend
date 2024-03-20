package de.dhbw2go.backend.dualis.models.semester;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DualisSemesterModel {

    @NotEmpty
    private final String name;

    @NotEmpty
    private final String referenceArguments;

    private final List<DualisSemesterCourseModel> courses = new ArrayList<>();
}
