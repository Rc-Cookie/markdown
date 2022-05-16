package com.github.rccookie.markdown;

public class Text extends ListElement<Word> implements TextItem<Word> {

    public Text() { }

    public Text(Word... children) {
        super(children);
    }

    public Text(String... words) {
        for(String word : words)
            add(new Word(word));
    }

    @Override
    public String toString() {
        return Markdown.childrenToString(this, " ");
    }
}
