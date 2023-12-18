package dev.phoenixofforce.adventofcode.meta;

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

}
