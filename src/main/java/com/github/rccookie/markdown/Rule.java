package com.github.rccookie.markdown;

import java.util.List;

import com.github.rccookie.json.JsonObject;

public class Rule implements DocumentItem<Word> {

    @Override
    public List<Word> children() {
        Word word = new Word();
        word.value = "---";
        return List.of(word);
    }

    @Override
    public String toString() {
        return "---";
    }

    @Override
    public Object toJson() {
        return new JsonObject();
    }
}
