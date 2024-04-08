package de.dhbw2go.backend.dualis.models.overview;

import de.dhbw2go.backend.dualis.models.DualisParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DualisOverviewParser extends DualisParser<DualisOverviewModel> {

    public DualisOverviewParser(final String rawDocument) {
        super(rawDocument);
    }

    @Override
    protected DualisOverviewModel parse() {
        final DualisOverviewModel dualisOverviewModel = new DualisOverviewModel();
        final Elements studentResults = this.document.select(".nb.list.students_results");
        if (!studentResults.isEmpty()) {
            final Elements coursesAndCreditsTableRows = studentResults.get(0).select("tr:not(.subhead,.tbsubhead)");
            for (final Element coursesAndCreditsTableRow : coursesAndCreditsTableRows) {
                this.parseModulesAndCredits(dualisOverviewModel, coursesAndCreditsTableRow);
            }
            final Elements gpaTableRows = studentResults.get(1).select("tr");
            for (final Element gpaTableRow : gpaTableRows) {
                this.parseGPA(dualisOverviewModel, gpaTableRow);
            }
        }
        return dualisOverviewModel;
    }

    private void parseModulesAndCredits(final DualisOverviewModel overviewModel, final Element coursesAndCreditsTableRow) {
        final Elements coursesAndCreditsTableRowData = coursesAndCreditsTableRow.select("td");
        if (coursesAndCreditsTableRowData.size() == 6) {
            final String moduleID = coursesAndCreditsTableRowData.get(0).text();
            final String moduleName;
            final String examsReferenceArguments;
            if (coursesAndCreditsTableRowData.get(1).childrenSize() > 0) {
                moduleName = coursesAndCreditsTableRowData.get(1).child(0).text();
                examsReferenceArguments = "-N" + coursesAndCreditsTableRowData.get(1).child(0).attr("href").split("-N")[3];
            } else {
                moduleName = coursesAndCreditsTableRowData.get(1).text();
                examsReferenceArguments = null;
            }
            String credits = coursesAndCreditsTableRowData.get(3).text();
            String grade = coursesAndCreditsTableRowData.get(4).text();
            String passed = coursesAndCreditsTableRowData.get(5).child(0).attr("title");
            final DualisOverviewCourseModel dualisOverviewCourseModel = new DualisOverviewCourseModel(moduleID, moduleName, credits, grade, passed.equalsIgnoreCase("Bestanden"));
            if (examsReferenceArguments != null) {
                dualisOverviewCourseModel.setExamsReferenceArguments(examsReferenceArguments);
            }
            System.out.println("[DEBUG] Added course: " + dualisOverviewCourseModel);
            overviewModel.getCourses().add(dualisOverviewCourseModel);
        } else if (coursesAndCreditsTableRowData.size() < 6 && !coursesAndCreditsTableRowData.get(0).select(".level00").isEmpty()) {
            if (coursesAndCreditsTableRowData.size() == 5) {
                final String earnedCredits = coursesAndCreditsTableRowData.get(2).text().trim();
                System.out.println("[DEBUG] Set earned credits: " + earnedCredits);
                overviewModel.setEarnedCredits(earnedCredits);
            } else if (coursesAndCreditsTableRowData.size() == 1) {
                final String neededCredits = coursesAndCreditsTableRowData.get(0).text().replace("Erforderliche Credits für Abschluss: ", "").trim();
                System.out.println("[DEBUG] Set needed credits: " + neededCredits);
                overviewModel.setNeededCredits(neededCredits);
            }
        }
    }

    private void parseGPA(final DualisOverviewModel overviewModel, final Element gpaTableRows) {
        final Elements gpaTableRowColumn = gpaTableRows.select("th");
        final String columnName = gpaTableRowColumn.get(0).text();
        final String columnValue = gpaTableRowColumn.get(1).text();
        if (columnName.equalsIgnoreCase("Gesamt-GPA")) {
            System.out.println("[DEBUG] Set total gpa: " + columnValue);
            overviewModel.setTotalGPA(columnValue);
        } else if (columnName.equalsIgnoreCase("Hauptfach-GPA")) {
            System.out.println("[DEBUG] Set major course gpa: " + columnValue);
            overviewModel.setMajorCourseGPA(columnValue);
        }
    }
}
