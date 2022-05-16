package com.github.rccookie.markdown;

public class OrderedList extends ListElement<ListItem<?>> implements DocumentItem<ListItem<?>> {

    public OrderedList(ListItem<?>... children) {
        super(children);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int i=0;
        for(ListItem<?> item : this)
            str.append(++i).append(". ").append(item).append('\n');
        if(i != 0) str.deleteCharAt(str.length() - 1);
        return str.toString();
    }
}
