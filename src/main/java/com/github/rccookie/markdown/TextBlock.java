package com.github.rccookie.markdown;

public class TextBlock extends ListElement<TextItem<?>> implements DocumentItem<TextItem<?>>, ListItem<TextItem<?>> {

    public TextBlock(TextItem<?>... children) {
        super(children);
    }

    @Override
    public String toString() {
        return Markdown.childrenToString(this, "");
    }
}
