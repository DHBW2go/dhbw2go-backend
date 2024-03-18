package de.dhbw2go.backend.dualis;

public enum DualisURL {

    OVERVIEW("STUDENT_RESULT"),
    STUDIES("COURSERESULTS"),
    EXAMS("RESULTDETAILS");

    private static final String BASE_URL = "https://dualis.dhbw.de/scripts/mgrqispi.dll?APPNAME=CampusNet&PRGNAME=";
    private final String url;

    DualisURL(String url) {
        this.url = BASE_URL + url;
    }

    public String getURL() {
        return url;
    }
}
