package com.github.rccookie.markdown;

public class Document extends ListElement<DocumentItem<?>> {

    public Document(DocumentItem<?>... children) {
        super(children);
    }

    @Override
    public String toString() {
        return Markdown.childrenToString(this, "\n\n");
    }
}
