package dev.phoenixofforce.adventofcode.common;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PuzzleInput {

    private String file = "";
    private final List<String> lines = new ArrayList<>();
    private final List<String> paragraphs = new ArrayList<>();

    public PuzzleInput(int day, int year) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FileUtils.getFile(day, year).get()));

            String line = reader.readLine();
            String currentParagraph = "";

            while(line != null) {
                if(line.isEmpty()) {
                    paragraphs.add(currentParagraph.trim());
                    currentParagraph = "";
                }

                file += line;
                lines.add(line);
                if(!line.isEmpty()) currentParagraph += line + " ";

                line = reader.readLine();
            }

            if(!currentParagraph.isEmpty()) paragraphs.add(currentParagraph);

            reader.close();
        } catch (FileNotFoundException ignored) { } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
