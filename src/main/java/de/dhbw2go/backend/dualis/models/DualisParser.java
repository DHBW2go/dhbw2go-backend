package de.dhbw2go.backend.dualis.models;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class DualisParser<T> {

    protected final Document document;

    @Getter
    protected final T model;

    public DualisParser(final String rawDocument) {
        this.document = Jsoup.parse(rawDocument);
        this.model = parse();
    }

    protected abstract T parse();

}
