package com.github.rccookie.markdown;

import com.github.rccookie.json.JsonArray;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.util.Utils;

public class Heading extends ListElement<TextItem<?>> implements DocumentItem<TextItem<?>> {

    int n;

    public Heading(int n, TextItem<?>... children) {
        super(children);
        setSize(n);
    }

    @Override
    public String toString() {
        return Utils.repeat("#", n) + ' ' + Markdown.childrenToString(this, "");
    }

    public int getSize() {
        return n;
    }

    public void setSize(int n) {
        this.n = Arguments.checkRange(n, 1, 7);
    }

    @Override
    public Object toJson() {
        JsonArray json = (JsonArray) super.toJson();
        json.add(0, n);
        return json;
    }
}
