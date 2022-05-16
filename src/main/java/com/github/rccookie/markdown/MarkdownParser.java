package com.github.rccookie.markdown;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.github.rccookie.util.Console;

final class MarkdownParser {

    private MarkdownParser() {
        throw new UnsupportedOperationException();
    }

    private static final Pattern RULE = Pattern.compile("^---+\\s*$");
    private static final Pattern HEADING = Pattern.compile("^#{1,6} ");
    private static final Pattern QUOTE = Pattern.compile("^>[^\n]*(\\n>[^\n]*)*$");
    private static final Pattern CODE_BLOCK = Pattern.compile("^```[a-zA-Z]*\n(.|\n)*```$");
    private static final Pattern UNORDERED_LIST = Pattern.compile("^[-+*]");
    private static final Pattern ORDERED_LIST = Pattern.compile("^\\d+\\.");
    private static final Pattern UNORDERED_SUBLIST = Pattern.compile("\n {2}[-+*]");
    private static final Pattern ORDERED_SUBLIST = Pattern.compile("\n {2}\\d+\\.");
    private static final Pattern LINK = Pattern.compile("^\\[(\\\\]|[^]\n])+]\\([a-zA-Z0-9_.:/]+\\)");
    private static final Pattern IMAGE = Pattern.compile("^!\\[(\\\\]|[^]\\n])+]\\([a-zA-Z0-9_.:/]+\\)");
    private static final Pattern BOLD = Pattern.compile("^\\*\\*(\\\\\\*|[^*])*[^\\\\]\\*\\*");
    private static final Pattern ITALIC = Pattern.compile("^\\*(\\\\\\*|[^*])*[^\\\\*]\\*");
    private static final Pattern STRIKETHROUGH = Pattern.compile("^~(\\\\~|[^~])*[^\\\\~]~");
    private static final Pattern INLINE_CODE = Pattern.compile("^`((\\\\`)|[^`])*`");
    private static final Pattern SOFT_BREAK = Pattern.compile("^  +\n");

    private static final Set<Character> CRITICAL_CHARS = Set.of('[', '!', '*', '_', '~', '`', ' ');

    public static Document parse(String source) {

        Document document = new Document();

        // Remove leading and trailing lines and spaces
        for(String block : source.strip().replace("\r", "")
                // Remove irrelevant trailing whitespaces from lines
                .replaceAll(" *\\n( *\\n)+", "\n\n").replaceAll("([^ ]) \n", "$1\n")
                // Split into blocks
                .split("\\n\\n")) {
            Console.split("Parsing block:");
            Console.log(block);
            if(RULE.matcher(block).find())
                document.add(new Rule());
            else if(HEADING.matcher(block).find())
                document.add(parseHeading(block));
            else if(QUOTE.matcher(block).find())
                document.add(parseQuote(block));
            else if(CODE_BLOCK.matcher(block).find())
                document.add(parseCodeBlock(block));
            else if(UNORDERED_LIST.matcher(block).find())
                document.add(parseUnorderedList(block));
            else if(ORDERED_LIST.matcher(block).find())
                document.add(parseOrderedList(block));
            else
                document.add(parseTextBlock(block, true));
        }

        return document;
    }



    private static Heading parseHeading(String block) {
        int count = 0;
        while(block.charAt(count) == '#') count++;

        Heading heading = new Heading(count);
        for(TextItem<?> item : parseTextBlock(block.substring(count + 1), false))
            heading.add(item);
        return heading;
    }

    private static Blockquote parseQuote(String block) {
        Blockquote blockquote = new Blockquote();
        for(DocumentItem<?> item : parse(block.lines().map(s -> s.replaceFirst("> ?", "")).collect(Collectors.joining("\n"))))
            blockquote.add(item);
        return blockquote;
    }

    private static CodeBlock parseCodeBlock(String block) {
        int newline = block.indexOf('\n');
        String code = block.substring(newline+1, block.length() - 3);
        if(code.endsWith("\n"))
            code = code.substring(0, code.length() - 1);
        return new CodeBlock(block.substring(3, newline), code);
    }

    private static UnorderedList parseUnorderedList(String block) {
        UnorderedList list = new UnorderedList();
        for(String item : block.replaceFirst("^[-+*] ?", "").split("\n[-+*]"))
            list.add(parseListItem(item));
        return list;
    }

    private static OrderedList parseOrderedList(String block) {
        OrderedList list = new OrderedList();
        for(String item : block.replaceFirst("^\\d+\\. ?", "").split("\n\\d\\."))
            list.add(parseListItem(item));
        return list;
    }

    private static UnorderedSublist parseUnorderedSublist(String block, Matcher listRegion) {
        UnorderedSublist list = new UnorderedSublist(parseTextBlock(block.substring(0, listRegion.start()), true));
        for(String item : block.substring(listRegion.start()+1).replaceFirst("^ {2}[-+*] ?", "").split("\n {2}[-+*]"))
            list.add(parseListItem(item));
        return list;
    }

    private static OrderedSublist parseOrderedSublist(String block, Matcher listRegion) {
        OrderedSublist list = new OrderedSublist(parseTextBlock(block.substring(0, listRegion.start()), true));
        for(String item : block.substring(listRegion.start()+1).replaceFirst("^ {2}\\d+\\. ?", "").split("\n {2}\\d+\\."))
            list.add(parseListItem(item));
        return list;
    }

    private static ListItem<?> parseListItem(String block) {
        Matcher m;
        if(QUOTE.matcher(block).find())
            return parseQuote(block);
        else if(CODE_BLOCK.matcher(block).find())
            return parseCodeBlock(block);
        else if((m = UNORDERED_SUBLIST.matcher(block)).find())
            return parseUnorderedSublist(block, m);
        else if((m = ORDERED_SUBLIST.matcher(block)).find())
            return parseOrderedSublist(block, m);
        else
            return parseTextBlock(block, true);
    }

    private static TextBlock parseTextBlock(String block, boolean softBreaks) {
        TextBlock textBlock = new TextBlock();

        StringBuilder str = new StringBuilder(block);
        boolean wasSpace = true, wasSpecial = false;
        while(str.length() != 0) {
            char c = str.charAt(0);

            if(CRITICAL_CHARS.contains(c)) {
                boolean matched = true;
                Matcher m;
                if((m = BOLD.matcher(str)).find())
                    textBlock.add(parseBold(str, m));
                else if((m = ITALIC.matcher(str)).find())
                    textBlock.add(parseItalic(str, m));
                else if((m = STRIKETHROUGH.matcher(str)).find())
                    textBlock.add(parseStrikethrough(str, m));
                else if((m = IMAGE.matcher(str)).find())
                    textBlock.add(parseImage(str, m));
                else if((m = LINK.matcher(str)).find())
                    textBlock.add(parseLink(str, m));
                else if((m = INLINE_CODE.matcher(str)).find())
                    textBlock.add(parseInlineCode(str, m));
                else if(softBreaks && (m = SOFT_BREAK.matcher(str)).find()) {
                    textBlock.add(parseSoftBreak());
                    wasSpace = false;
                } else {
                    matched = false;
                    wasSpace = c == ' ';
                    str.deleteCharAt(0);
                }
                if(matched) {
                    str.delete(0, m.end());
                    if(wasSpace)
                        textBlock.add(textBlock.childCount() - 1, new Space());
                }
                if(!matched) {
                    if(wasSpecial && wasSpace)
                        textBlock.add(new Space());
                    textBlock.add(parseText(str, softBreaks));
                }
                wasSpecial = matched;
            }
            else textBlock.add(parseText(str, softBreaks));
        }

        return textBlock;
    }

    private static Bold parseBold(StringBuilder str, Matcher region) {
        Bold bold = new Bold();
        for(TextItem<?> item : parseTextBlock(str.substring(2, region.end()-2), true))
            bold.add(item);
        return bold;
    }

    private static Italic parseItalic(StringBuilder str, Matcher region) {
        Italic italic = new Italic();
        for(TextItem<?> item : parseTextBlock(str.substring(1, region.end()-1), true))
            italic.add(item);
        return italic;
    }

    private static Strikethrough parseStrikethrough(StringBuilder str, Matcher region) {
        Strikethrough strikethrough = new Strikethrough();
        for(TextItem<?> item : parseTextBlock(str.substring(1, region.end()-1), true))
            strikethrough.add(item);
        return strikethrough;
    }

    private static InlineCode parseInlineCode(StringBuilder str, Matcher region) {
        return new InlineCode(unescape(str.substring(1, region.end()-1)));
    }

    private static Image parseImage(StringBuilder str, Matcher region) {
        String area = str.substring(2, region.end()-1);
        int center = area.lastIndexOf("](");
        return new Image(unescape(area.substring(0, center)), area.substring(center+2));
    }

    private static Link parseLink(StringBuilder str, Matcher region) {
        String area = str.substring(1, region.end()-1);
        int center = area.lastIndexOf("](");
        return new Link(unescape(area.substring(0, center)), area.substring(center+2));
    }

    private static SoftBreak parseSoftBreak() {
        return new SoftBreak();
    }

    @SuppressWarnings("DuplicatedCode")
    private static Text parseText(StringBuilder str, boolean softBreaks) {
        Text text = new Text();

        while(true) {

            text.add(parseWord(str));
            if(str.length() == 0) break;

            char c = str.charAt(0);
            if(CRITICAL_CHARS.contains(c) &&
                    BOLD.matcher(str).find() ||
                    ITALIC.matcher(str).find() ||
                    STRIKETHROUGH.matcher(str).find() ||
                    IMAGE.matcher(str).find() ||
                    LINK.matcher(str).find() ||
                    INLINE_CODE.matcher(str).find() ||
                    softBreaks && SOFT_BREAK.matcher(str).find()
            ) break;

            for(; Character.isWhitespace(c); c = str.charAt(0)) {
                str.deleteCharAt(0);
                if(str.length() == 0) return text;
            }

            if(CRITICAL_CHARS.contains(c) &&
                    BOLD.matcher(str).find() ||
                    ITALIC.matcher(str).find() ||
                    STRIKETHROUGH.matcher(str).find() ||
                    IMAGE.matcher(str).find() ||
                    LINK.matcher(str).find() ||
                    INLINE_CODE.matcher(str).find() ||
                    softBreaks && SOFT_BREAK.matcher(str).find()
            ) break;
        }

        return text;
    }

    private static Word parseWord(StringBuilder text) {
        StringBuilder word = new StringBuilder();
        while(text.length() != 0) {
            char c = text.charAt(0);
            if(c == '\\') {
                text.deleteCharAt(0);
                if(text.length() == 0) break;
                word.append(text.charAt(0));
            }
            else if(Character.isWhitespace(c)) break;
            else if(CRITICAL_CHARS.contains(c) &&
                    BOLD.matcher(text).find() ||
                    ITALIC.matcher(text).find() ||
                    STRIKETHROUGH.matcher(text).find() ||
                    IMAGE.matcher(text).find() ||
                    LINK.matcher(text).find() ||
                    INLINE_CODE.matcher(text).find()
            ) break;
            else word.append(c);
            text.deleteCharAt(0);
        }
        return new Word(word.toString());
    }

    private static String unescape(String str) {
        return str.replace("\\\\", "\u0000")
                .replace("\\", "")
                .replace('\u0000', '\\');
    }


    public static void main(String[] args) throws Exception {
        String md = Files.readString(Path.of("test.md"));
        Document document = parse(md);
        Console.log(document.toJson());
        Console.log(document);
    }
}
