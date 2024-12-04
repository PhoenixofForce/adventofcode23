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

    private void setup(List<String> linesInput) {
        String currentParagraph = "";

        for(String line: linesInput) {
            if(line.isEmpty()) {
                paragraphs.add(currentParagraph.trim());
                currentParagraph = "";
            }

            file += line;
            lines.add(line);
            if(!line.isEmpty()) currentParagraph += line + " ";
        }

        if(!currentParagraph.isEmpty()) paragraphs.add(currentParagraph);
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

}
