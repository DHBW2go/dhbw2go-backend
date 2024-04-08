package de.dhbw2go.backend.dualis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DualisURL {

    SEMESTER("COURSERESULTS&", "-N000310"),
    OVERVIEW("STUDENT_RESULT&", "-N000307"),
    EXAM("RESULTDETAILS&", "-N000307");

    private static final String BASE_URL = "https://dualis.dhbw.de/scripts/mgrqispi.dll?APPNAME=CampusNet&PRGNAME=";
    private final String fragment;
    private final String arguments;

    public String getURL(final String userArguments, final String extraArguments) {
        return BASE_URL + fragment + "ARGUMENTS=" + userArguments + "," + arguments + (extraArguments != null ? "," + extraArguments : "");
    }
}
