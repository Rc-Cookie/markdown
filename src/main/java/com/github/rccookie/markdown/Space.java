package com.github.rccookie.markdown;

public class Space extends Word implements TextItem<Element<?>> {

    public Space() {
        super(" ");
    }

    @Override
    public String toString() {
        return " ";
    }

    @Override
    public void setString(String value) {
        throw new UnsupportedOperationException();
    }
}
