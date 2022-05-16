package com.github.rccookie.markdown;

public class Bold extends ListElement<TextItem<?>> implements TextItem<TextItem<?>> {

    public Bold(TextItem<?>... children) {
        super(children);
    }

    @Override
    public String toString() {
        return "**" + Markdown.childrenToString(this, "") + "**";
    }
}
