package com.github.rccookie.markdown;

public class Blockquote extends ListElement<DocumentItem<?>> implements DocumentItem<DocumentItem<?>>, ListItem<DocumentItem<?>> {

    public Blockquote(DocumentItem<?>... children) {
        super(children);
    }

    @Override
    public String toString() {
        return "> " + Markdown.childrenToString(this, "\n\n")
                .replace("\n", "\n> ")
                .replace("> \n", ">\n");
    }
}
