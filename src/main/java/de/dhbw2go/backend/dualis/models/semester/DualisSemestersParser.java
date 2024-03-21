package de.dhbw2go.backend.dualis.models.semester;

import de.dhbw2go.backend.dualis.models.DualisParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DualisSemestersParser extends DualisParser<List<DualisSemesterModel>> {

    public DualisSemestersParser(final String rawDocument) {
        super(rawDocument);
    }

    @Override
    protected List<DualisSemesterModel> parse() {
        final List<DualisSemesterModel> dualisSemestersModels = new ArrayList<>();
        final Elements semesters = this.document.select("select#semester").select("option");
        for (final Element semester : semesters) {
            final String name = semester.text();
            final String referenceArguments = "-N" + semester.val();
            final DualisSemesterModel dualisSemesterModel = new DualisSemesterModel(name, referenceArguments);
            System.out.println("[DEBUG] Found semester: " + dualisSemesterModel);
            dualisSemestersModels.add(dualisSemesterModel);
        }
        return dualisSemestersModels;
    }
}
