package com.github.rccookie.markdown;

import com.github.rccookie.json.JsonArray;

public class OrderedSublist extends OrderedList implements ListItem<ListItem<?>> {

    private final TextBlock text = new TextBlock();

    public OrderedSublist(ListItem<?>... children) {
        super(children);
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
