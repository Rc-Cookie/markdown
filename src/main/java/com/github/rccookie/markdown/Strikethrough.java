package com.github.rccookie.markdown;

public class Strikethrough extends ListElement<TextItem<?>> implements TextItem<TextItem<?>> {

    public Strikethrough(TextItem<?>... children) {
        super(children);
    }

    @Override
    public String toString() {
        return "~" + Markdown.childrenToString(this, "") + '~';
    }
}
