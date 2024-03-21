package de.dhbw2go.backend.dualis.models.exam;

import de.dhbw2go.backend.dualis.models.DualisParser;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class DualisExamsParser extends DualisParser<List<DualisExamModel>> {

    public DualisExamsParser(final String rawDocument) {
        super(rawDocument);
    }

    @Override
    protected List<DualisExamModel> parse() {
        final List<DualisExamModel> dualisExamModels = new ArrayList<>();

        final Element table = this.document.select("table").get(0);
        for (int i = 0; i < table.select("tr").size(); i++) {
            try {
                final String topic = table.select("tr").get(i).select("td").get(1).text();
                final String grade = table.select("tr").get(i).select("td").get(3).text();
                if (!topic.isEmpty() && table.select("tr").get(i).select("td").get(1).hasClass("tbdata")) {
                    final DualisExamModel dualisExamModel = new DualisExamModel(topic, grade);
                    System.out.println("[DEBUG] Added exam: " + dualisExamModel);
                    dualisExamModels.add(dualisExamModel);
                }
            } catch (final IndexOutOfBoundsException exception) {
            }
        }
        return dualisExamModels;
    }
}
