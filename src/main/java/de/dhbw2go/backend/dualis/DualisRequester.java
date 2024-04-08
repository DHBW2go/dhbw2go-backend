package de.dhbw2go.backend.dualis;

import de.dhbw2go.backend.exceptions.dualis.DualisRequestException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DualisRequester {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);
    private final HttpClient httpClient;

    public DualisRequester() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .executor(EXECUTOR_SERVICE)
                .build();
    }

    public String requestSemesters(final DualisCookie dualisCookie) {
        return requestSemester(dualisCookie, null);
    }

    public String requestSemester(final DualisCookie dualisCookie, final String semesterReferenceArguments) throws DualisRequestException {
        return requestURL(DualisURL.SEMESTER, dualisCookie.getUserArguments(), semesterReferenceArguments, dualisCookie.buildHttpCookie());
    }

    public String requestOverview(final DualisCookie dualisCookie) throws DualisRequestException {
        return requestURL(DualisURL.OVERVIEW, dualisCookie.getUserArguments(), null, dualisCookie.buildHttpCookie());
    }

    public String requestExams(final DualisCookie dualisCookie, final String examsReferenceArguments) throws DualisRequestException {
        return requestURL(DualisURL.EXAM, dualisCookie.getUserArguments(), examsReferenceArguments, dualisCookie.buildHttpCookie());
    }

    public Pair<String, HttpCookie> requestCookie(final String username, final String password) throws DualisRequestException {
        final Map<String, String> formData = generateFormData(username, password);
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://dualis.dhbw.de/scripts/mgrqispi.dll"))
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .POST(buildFormDataFromMap(formData))
                .build();
        try {
            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            final String userArguments = this.getArguments(httpResponse.headers());
            final HttpCookie httpCookie = this.getCookie(httpResponse.headers());
            if (userArguments != null && httpCookie != null) {
                return Pair.of(userArguments, httpCookie);
            }
        } catch (final IOException | InterruptedException exception) {
            throw new DualisRequestException(httpRequest.uri().toString());
        }
        return null;
    }

    private String requestURL(final DualisURL dualisURL, final String userArguments, final String extraArguments, final HttpCookie httpCookie) throws DualisRequestException {
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(dualisURL.getURL(userArguments, extraArguments)))
                .setHeader("Cookie", httpCookie.getName() + "=" + httpCookie.getValue())
                .GET()
                .build();
        try {
            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (final IOException | InterruptedException exception) {
            throw new DualisRequestException(httpRequest.uri().toString());
        }
    }

    private String getArguments(final HttpHeaders httpHeaders) {
        final Map<String, List<String>> headers = httpHeaders.map();
        if (headers.containsKey("REFRESH")) {
            final List<String> refreshes = headers.get("REFRESH");
            for (final String refresh : refreshes) {
                final String arguments = refresh.split("&")[2];
                return arguments.replaceAll(",", "").replace("ARGUMENTS=", "").replace("-N000019", "").replace("-N000000000000000", "");
            }
        }
        return null;
    }

    private HttpCookie getCookie(final HttpHeaders httpHeaders) {
        final Map<String, List<String>> headers = httpHeaders.map();
        if (headers.containsKey("Set-Cookie")) {
            final List<String> cookies = headers.get("Set-Cookie");
            for (final String cookie : cookies) {
                if (cookie.startsWith("cnsc")) {
                    return new HttpCookie("cnsc", cookie.replace("cnsc =", "").split(";")[0]);
                }
            }
        }
        return null;
    }

    private Map<String, String> generateFormData(final String username, final String password) {
        Map<String, String> formData = new LinkedHashMap<>();
        formData.put("usrname", URLEncoder.encode(username, StandardCharsets.UTF_8));
        formData.put("pass", URLEncoder.encode(password, StandardCharsets.UTF_8));
        formData.put("clino", "000000000000001");
        formData.put("menuno", "000324");
        formData.put("menu_type", "classic");
        formData.put("browser", "");
        formData.put("platform", "");
        formData.put("APPNAME", "CampusNet");
        formData.put("PRGNAME", "LOGINCHECK");
        formData.put("ARGUMENTS", "clino%2Cusrname%2Cpass%2Cmenuno%2Cmenu_type%2Cbrowser%2Cplatform");
        return formData;
    }

    private HttpRequest.BodyPublisher buildFormDataFromMap(final Map<String, String> formData) {
        final StringBuilder stringBuilder = new StringBuilder();
        formData.forEach((key, value) -> {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append("&");
            }
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(value);
        });
        return HttpRequest.BodyPublishers.ofString(stringBuilder.toString());
    }
}
