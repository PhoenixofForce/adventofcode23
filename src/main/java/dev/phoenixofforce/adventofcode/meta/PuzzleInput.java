package dev.phoenixofforce.adventofcode.meta;

import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class PuzzleInput {

    private String file = "";
    private final List<String> lines = new ArrayList<>();
    private final List<String> paragraphs = new ArrayList<>();
    private final List<List<String>> paragraphsAsList = new ArrayList<>();

    public PuzzleInput(int day, int year) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FileUtils.getFile(day, year).get()));

            String line = reader.readLine();
            List<String> linesInput = new ArrayList<>();

            while(line != null) {
                linesInput.add(line);
                line = reader.readLine();
            }

            setup(linesInput);
            reader.close();
        } catch (FileNotFoundException ignored) { } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PuzzleInput(List<String> lines) {
        setup(lines);
    }

    private PuzzleInput(String file, List<String> lines, List<String> paragraphs) {
        this.file = file;
        this.lines.addAll(lines);
        this.paragraphs.addAll(paragraphs);
    }

    private void setup(List<String> linesInput) {
        String currentParagraph = "";
        List<String> currentParagraphList = new ArrayList<>();

        for(String line: linesInput) {
            if(line.isEmpty()) {
                paragraphs.add(currentParagraph.trim());
                currentParagraph = "";

                paragraphsAsList.add(currentParagraphList);
                currentParagraphList = new ArrayList<>();
            }

            file += line;
            lines.add(line);
            if(!line.isEmpty()) {
                currentParagraph += line + " ";
                currentParagraphList.add(line);
            }
        }

        if(!currentParagraph.isEmpty()) paragraphs.add(currentParagraph);
        if(!currentParagraphList.isEmpty()) paragraphsAsList.add(currentParagraphList);
    }

    public PuzzleInput mapLines(Function<String, String> mapper) {
        return new PuzzleInput(lines.stream()
            .map(mapper)
            .toList()
        );
    }

    public char getChar(long x, long y) {
        return lines.get((int) y).charAt((int) x);
    }

    public char getChar(Position pos) {
        return getChar(pos.getX(), pos.getY());
    }

    public void setChar(Position pos, char c) {
        StringBuilder newLine = new StringBuilder(lines.get((int) pos.getY()));
        newLine.setCharAt((int) pos.getX(), c);

        this.lines.set((int) pos.getY(), newLine.toString());
    }

    public char getCharOrElseDot(Position pos) {
        return getCharOrElse(pos, '.');
    }

    public char getCharOrElse(Position pos, char defaultChar) {
        if(!inbounds(pos)) return defaultChar;
        return getChar(pos.getX(), pos.getY());
    }

    public char getCharTranscending(long x, long y) {
        Position mapped = mapInbounds(new Position(x, y));
        return getChar(mapped.getX(), mapped.getY());
    }

    public Position mapInbounds(Position in) {
        long x = in.getX();
        long y = in.getY();
        while(x < 0) x += width();
        while(y < 0) y += height();
        x %= width();
        y %= height();
        return new Position(x, y);
    }

    public boolean inbounds(long x, long y) {
        return 0 <= x && x < lines.get(0).length() &&
            0 <= y && y < lines.size();
    }

    public boolean inbounds(Position pos) {
        return inbounds(pos.getX(), pos.getY());
    }

    public long width() {
        return lines.get(0).length();
    }

    public long height() {
        return lines.size();
    }

    public char[][] toArray() {
        char[][] map = new char[(int) height()][(int) width()];
        for(int x = 0; x < width(); x++) {
            for(int y = 0; y < height(); y++) {
                map[y][x] = getChar(x, y);
            }
        }
        return map;
    }

    @Override
    public PuzzleInput clone() {
        return new PuzzleInput(file, lines, paragraphs);
    }
}
