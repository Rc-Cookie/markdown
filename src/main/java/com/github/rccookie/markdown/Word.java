package com.github.rccookie.markdown;

import java.util.List;

public class Word implements Element<Element<?>> {

    String value;

    public Word() { }

    public Word(String value) {
        this.value = value;
    }

    @Override
    public List<Element<?>> children() {
        return List.of();
    }

    @Override
    public String toString() {
        return value;
    }

    public void setString(String value) {
        this.value = value;
    }

    @Override
    public Object toJson() {
        return value;
    }
}
