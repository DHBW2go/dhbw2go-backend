package de.dhbw2go.backend.services;

import de.dhbw2go.backend.dualis.DualisCookie;
import de.dhbw2go.backend.dualis.DualisRequester;
import de.dhbw2go.backend.dualis.models.exam.DualisExamModel;
import de.dhbw2go.backend.dualis.models.exam.DualisExamsParser;
import de.dhbw2go.backend.dualis.models.overview.DualisOverviewModel;
import de.dhbw2go.backend.dualis.models.overview.DualisOverviewParser;
import de.dhbw2go.backend.dualis.models.semester.DualisSemesterModel;
import de.dhbw2go.backend.dualis.models.semester.DualisSemesterParser;
import de.dhbw2go.backend.dualis.models.semester.DualisSemestersParser;
import de.dhbw2go.backend.exceptions.dualis.DualisRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.List;

@Service
public class DualisService {

    @Autowired
    private DualisRequester dualisRequester;

    public DualisCookie login(final String username, final String password) throws DualisRequestException {
        final Pair<String, HttpCookie> userArgumentsAndHttpCookie = dualisRequester.requestCookie(username, password);
        return new DualisCookie(userArgumentsAndHttpCookie.getFirst(), userArgumentsAndHttpCookie.getSecond().getName(), userArgumentsAndHttpCookie.getSecond().getValue());
    }

    public List<DualisSemesterModel> getSemesters(final DualisCookie dualisCookie) throws DualisRequestException {
        final String rawSemestersDocument = dualisRequester.requestSemesters(dualisCookie);
        final DualisSemestersParser dualisSemestersParser = new DualisSemestersParser(rawSemestersDocument);
        return dualisSemestersParser.getModel();
    }

    public DualisSemesterModel getSemester(final DualisCookie dualisCookie, final String semesterReferenceArguments) throws DualisRequestException {
        final String rawSemesterDocument = dualisRequester.requestSemester(dualisCookie, semesterReferenceArguments);
        final DualisSemesterParser dualisSemesterParser = new DualisSemesterParser(rawSemesterDocument);
        return dualisSemesterParser.getModel();
    }

    public DualisOverviewModel getOverview(final DualisCookie dualisCookie) throws DualisRequestException {
        final String rawOverviewDocument = dualisRequester.requestOverview(dualisCookie);
        final DualisOverviewParser dualisOverviewParser = new DualisOverviewParser(rawOverviewDocument);
        return dualisOverviewParser.getModel();
    }

    public List<DualisExamModel> getExams(final DualisCookie dualisCookie, final String examsReferenceArguments) throws DualisRequestException {
        final String rawExamsDocument = dualisRequester.requestExams(dualisCookie, examsReferenceArguments);
        final DualisExamsParser dualisExamsParser = new DualisExamsParser(rawExamsDocument);
        return dualisExamsParser.getModel();
    }
}
