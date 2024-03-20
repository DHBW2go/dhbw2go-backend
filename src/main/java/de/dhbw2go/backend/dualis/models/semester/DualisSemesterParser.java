package de.dhbw2go.backend.dualis.models.semester;

import de.dhbw2go.backend.dualis.models.DualisParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DualisSemesterParser extends DualisParser<DualisSemesterModel> {

    private final static Pattern EXAMS_ARGUMENTS_PATTERN = Pattern.compile("dl_popUp\\(\"(.+?)\",\"Resultdetails\"", Pattern.DOTALL);

    public DualisSemesterParser(final String rawDocument) {
        super(rawDocument);
    }

    @Override
    protected DualisSemesterModel parse() {
        final Elements semesters = this.document.select("select#semester").select("option").select("[selected=selected]");
        if (!semesters.isEmpty()) {
            final String name = semesters.get(0).text();
            final String referenceArguments = "-N" + semesters.get(0).val();
            final DualisSemesterModel dualisSemesterModel = new DualisSemesterModel(name, referenceArguments);
            System.out.println("[DEBUG] Loaded semester: " + dualisSemesterModel);
            final Elements rows = this.getRows(this.document);
            if (rows != null) {
                for (int i = 0; i < rows.size(); i++) {
                    final Elements tableData = this.getTableRowData(rows, i);
                    if (tableData != null) {
                        final String moduleId = tableData.get(0).text();
                        final String moduleName = tableData.get(1).text();
                        final String credits = tableData.get(2).text();
                        final String grade = tableData.get(3).text();
                        final DualisSemesterCourseModel dualisSemesterCourseModel = new DualisSemesterCourseModel(moduleId, moduleName, credits, grade);
                        final String examsReferenceArguments = this.findExamsArguments(tableData);
                        if (examsReferenceArguments != null) {
                            dualisSemesterCourseModel.setExamsReferenceArguments(examsReferenceArguments);
                        }
                        System.out.println("[DEBUG] Added semester course: " + dualisSemesterCourseModel);
                        dualisSemesterModel.getCourses().add(dualisSemesterCourseModel);
                    }
                }
            }
            return dualisSemesterModel;
        }
        return null;
    }

    private Elements getRows(final Document semesterCoursesDocument) {
        final Elements table = semesterCoursesDocument.select("table.nb.list");
        if (!table.isEmpty()) {
            final Elements tableBody = table.get(0).select("tbody tr");
            if (!tableBody.isEmpty()) {
                return tableBody;
            }
        }
        return null;
    }

    private Elements getTableRowData(final Elements rows, final int i) {
        if (i < rows.size()) {
            final Elements tableData = rows.get(i).select("td");
            if (!tableData.isEmpty()) {
                return tableData;
            }
        }
        return null;
    }

    private String findExamsArguments(final Elements tableData) {
        final Element script = tableData.get(5).selectFirst("script");
        if (script != null) {
            final Matcher matcher = DualisSemesterParser.EXAMS_ARGUMENTS_PATTERN.matcher(script.html());
            if (matcher.find()) {
                return "-N" + matcher.group(1).split("-N")[3];
            }
        }
        return null;
    }
}
