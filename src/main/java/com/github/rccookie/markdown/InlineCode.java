package com.github.rccookie.markdown;

import java.util.List;

import com.github.rccookie.json.JsonObject;

public class InlineCode implements TextItem<Word> {

    final Word code = new Word();

    public InlineCode() { }

    public InlineCode(String code) {
        this.code.value = code;
    }

    @Override
    public List<Word> children() {
        return List.of(code);
    }

    @Override
    public String toString() {
        return "`" + code + '`';
    }

    public Word code() {
        return code;
    }

    @Override
    public Object toJson() {
        return new JsonObject("code", code);
    }
}
