package com.github.rccookie.markdown;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public final class Markdown {

    private Markdown() {
        throw new UnsupportedOperationException();
    }

    static String childrenToString(Element<?> element, String delimiter) {
        return element.stream().map(Element::toString).collect(Collectors.joining(delimiter));
    }

    public static String toString(Document document) {
        return document.toString();
    }

    public static Document parse(String markdown) {
        return MarkdownParser.parse(markdown);
    }

    public static Document load(File file) {
        try {
            return parse(Files.readString(file.toPath()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document load(String file) {
        return load(new File(file));
    }

    public static void write(Document document, File file) {
        try {
            Files.writeString(file.toPath(), document + "\n");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(Document document, String file) {
        write(document, new File(file));
    }
}
