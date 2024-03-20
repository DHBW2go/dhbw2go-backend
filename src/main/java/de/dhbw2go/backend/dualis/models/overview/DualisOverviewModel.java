package de.dhbw2go.backend.dualis.models.overview;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DualisOverviewModel {

    private final List<DualisOverviewCourseModel> courses = new ArrayList<>();
    @NotEmpty
    private String earnedCredits = "N/A";
    @NotEmpty
    private String neededCredits = "N/A";
    @NotEmpty
    private String totalGPA = "N/A";
    @NotEmpty
    private String majorCourseGPA = "N/A";
}
