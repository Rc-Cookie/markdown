package com.github.rccookie.markdown;

import java.util.List;

import com.github.rccookie.json.JsonObject;

public class CodeBlock implements DocumentItem<Word>, ListItem<Word> {

    final Word language = new Word();
    final Word code = new Word();

    public CodeBlock() { }

    public CodeBlock(String language, String code) {
        this.language.value = language;
        this.code.value = code;
    }

    @Override
    public List<Word> children() {
        return List.of(language, code);
    }

    @Override
    public String toString() {
        return "```" + language + '\n' + code + "\n```";
    }

    public Word language() {
        return language;
    }

    public Word code() {
        return code;
    }

    @Override
    public Object toJson() {
        return new JsonObject("language", language, "code", code);
    }
}
