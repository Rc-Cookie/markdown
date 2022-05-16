package com.github.rccookie.markdown;

import java.util.List;

import com.github.rccookie.json.JsonObject;

public class Link implements TextItem<Word> {

    final Word alt = new Word();
    final Word url = new Word();

    public Link() { }

    public Link(String alt, String url) {
        this.alt.value = alt;
        this.url.value = url;
    }

    @Override
    public List<Word> children() {
        return List.of(alt, url);
    }

    @Override
    public String toString() {
        return "[" + alt + "](" + url + ')';
    }

    public Word alt() {
        return alt;
    }

    public Word url() {
        return url;
    }

    @Override
    public Object toJson() {
        return new JsonObject("alt", alt, "url", url);
    }
}
