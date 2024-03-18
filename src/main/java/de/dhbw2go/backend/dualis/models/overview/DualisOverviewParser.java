package de.dhbw2go.backend.dualis.models.overview;

import de.dhbw2go.backend.dualis.models.DualisParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DualisOverviewParser extends DualisParser<DualisOverviewModel> {

    public DualisOverviewParser(Document document) {
        super(document);
    }

    @Override
    protected DualisOverviewModel parse() {
        final DualisOverviewModel dualisOverviewModel = new DualisOverviewModel();
        final Elements studentResults = this.document.select(".nb.list.students_results");
        if (!studentResults.isEmpty()) {
            final Elements coursesAndCreditsTableRows = studentResults.get(0).select("tr:not(.subhead,.tbsubhead)");
            System.out.println("## [DEBUG - Course and Credits Table Row] START");
            for (final Element coursesAndCreditsTableRow : coursesAndCreditsTableRows) {
                this.parseModulesAndCredits(dualisOverviewModel, coursesAndCreditsTableRow);
            }
            System.out.println("## [DEBUG - Course and Credits Table Row] END");
            final Elements gpaTableRows = studentResults.get(1).select("tr");
            System.out.println("## [DEBUG - GPA Table Row] START");
            for (final Element gpaTableRow : gpaTableRows) {
                this.parseGPA(dualisOverviewModel, gpaTableRow);
            }
            System.out.println("## [DEBUG - GPA Table Row] END");
        }
        return dualisOverviewModel;
    }

    private void parseModulesAndCredits(final DualisOverviewModel overviewModel, final Element coursesAndCreditsTableRow) {
        final Elements coursesAndCreditsTableRowData = coursesAndCreditsTableRow.select("td");
        if (coursesAndCreditsTableRowData.size() == 6) {
            final String moduleID = coursesAndCreditsTableRowData.get(0).text();
            final String moduleName;
            if (coursesAndCreditsTableRowData.get(1).childrenSize() > 0) {
                moduleName = coursesAndCreditsTableRowData.get(1).child(0).text();
            } else {
                moduleName = coursesAndCreditsTableRowData.get(1).text();
            }
            String credits = coursesAndCreditsTableRowData.get(3).text();
            String grade = coursesAndCreditsTableRowData.get(4).text();
            String passed = coursesAndCreditsTableRowData.get(5).child(0).attr("title");
            overviewModel.getCourses().add(new DualisOverviewCourseModel(moduleID, moduleName, credits, grade, passed.equalsIgnoreCase("Bestanden")));
            System.out.println("### [DEBUG - Course Table Row Data] Added course: " + moduleID + ", " + moduleName + ", " + credits + ", " + grade + ", " + passed);
        } else if (coursesAndCreditsTableRowData.size() < 6 && !coursesAndCreditsTableRowData.get(0).select(".level00").isEmpty()) {
            if (coursesAndCreditsTableRowData.size() == 5) {
                final String earnedCredits = coursesAndCreditsTableRowData.get(2).text().trim();
                overviewModel.setEarnedCredits(earnedCredits);
                System.out.println("### [DEBUG - Credit Table Row Data] Earned credits: " + earnedCredits);
            } else if (coursesAndCreditsTableRowData.size() == 1) {
                final String neededCredits = coursesAndCreditsTableRowData.get(0).text().replace("Erforderliche Credits für Abschluss: ", "").trim();
                overviewModel.setNeededCredits(neededCredits);
                System.out.println("### [DEBUG - Credit Table Row Data] Needed credits: " + neededCredits);
            }
        }
    }

    private void parseGPA(final DualisOverviewModel overviewModel, final Element gpaTableRows) {
        final Elements gpaTableRowColumn = gpaTableRows.select("th");
        final String columnName = gpaTableRowColumn.get(0).text();
        final String columnValue = gpaTableRowColumn.get(1).text();
        if (columnName.equalsIgnoreCase("Gesamt-GPA")) {
            overviewModel.setTotalGPA(columnValue);
            System.out.println("### [DEBUG - GPA Table Row Column] Total-GPA: " + columnValue);
        } else if (columnName.equalsIgnoreCase("Hauptfach-GPA")) {
            overviewModel.setMajorCourseGPA(columnValue);
            System.out.println("### [DEBUG - GPA Table Row Column] Major Course-GPA: " + columnValue);
        }
    }
}
