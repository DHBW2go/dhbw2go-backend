package de.dhbw2go.backend.dualis.models.overview;

import de.dhbw2go.backend.dualis.models.DualisModel;
import lombok.Data;

@Data
public class DualisOverviewCourseModel implements DualisModel {

    private final String moduleID;
    private final String moduleName;
    private final String credits;
    private final String grade;
    private final boolean status;

}
