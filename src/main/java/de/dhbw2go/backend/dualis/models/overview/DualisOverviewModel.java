package de.dhbw2go.backend.dualis.models.overview;

import de.dhbw2go.backend.dualis.models.DualisModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DualisOverviewModel implements DualisModel {

    private final List<DualisOverviewCourseModel> courses = new ArrayList<>();
    private String earnedCredits = "N/A";
    private String neededCredits = "N/A";
    private String totalGPA = "N/A";
    private String majorCourseGPA = "N/A";
}
