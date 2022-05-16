package com.github.rccookie.markdown;

public class UnorderedList extends ListElement<ListItem<?>> implements DocumentItem<ListItem<?>> {

    public UnorderedList(ListItem<?>... children) {
        super(children);
    }

    @Override
    public String toString() {
        if(children.isEmpty()) return "";
        return "- " + Markdown.childrenToString(this, "\n- ");
    }
}
