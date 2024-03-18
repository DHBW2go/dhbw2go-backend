package de.dhbw2go.backend;

import de.dhbw2go.backend.dualis.models.overview.DualisOverviewParser;
import org.jsoup.Jsoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"de.dhbw2go.backend"})
@EnableJpaRepositories(basePackages = "de.dhbw2go.backend.repositories")
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("# [DEBUG - DUALIS] START");
        try {
            final DualisOverviewParser dualisOverviewParser = new DualisOverviewParser(Jsoup.parse(new File("src/main/resources/developement/dualis-overview.html"), "UTF-8"));
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("# [DEBUG - DUALIS] END");
    }
}
