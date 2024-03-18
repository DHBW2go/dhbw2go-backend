package de.dhbw2go.backend.dualis.models;

import org.jsoup.nodes.Document;

public abstract class DualisParser<T extends DualisModel> {

    protected final Document document;
    protected final T dualisModel;

    public DualisParser(final Document document) {
        this.document = document;
        this.dualisModel = parse();
    }

    protected abstract T parse();
}
