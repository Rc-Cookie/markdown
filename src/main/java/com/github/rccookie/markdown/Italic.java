package com.github.rccookie.markdown;

public class Italic extends ListElement<TextItem<?>> implements TextItem<TextItem<?>> {

    public Italic(TextItem<?>... children) {
        super(children);
    }

    @Override
    public String toString() {
        return "*" + Markdown.childrenToString(this, "") + '*';
    }
}
