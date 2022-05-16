package com.github.rccookie.markdown;

import com.github.rccookie.json.JsonArray;

public class UnorderedSublist extends UnorderedList implements ListItem<ListItem<?>> {

    private final TextBlock text = new TextBlock();

    public UnorderedSublist() {
    }

    public UnorderedSublist(TextBlock text, ListItem<?>... children) {
        super(children);
        text.forEach(this.text::add);
    }

    public TextBlock text() {
        return text;
    }

    @Override
    public String toString() {
        return text + "\n  " + super.toString().replace("\n", "\n  ");
    }

    @Override
    public Object toJson() {
        JsonArray json = (JsonArray) super.toJson();
        json.add(0, text);
        return json;
    }
}
