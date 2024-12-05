package dev.phoenixofforce.adventofcode.y2024.day05;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day05_2024 implements Puzzle {

    private int input_index = 0;
    private Map<String, List<String>> requirements;

    @Override
    public Object solvePart1(PuzzleInput input) {
        requirements = mapInput(input);

        long out = 0;
        for(int i = input_index; i < input.getLines().size(); i++) {
            out += handleValidLine(requirements, List.of(input.getLines().get(i).split(",")));
        }
        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long out = 0;
        for(int i = input_index; i < input.getLines().size(); i++) {
            out += handleInvalidLines(requirements, List.of(input.getLines().get(i).split(",")));
        }
        return out;
    }

    private Map<String, List<String>> mapInput(PuzzleInput input) {
        input_index = 0;
        Map<String, List<String>> requirements = new HashMap<>();

        for(; input_index < input.getLines().size(); input_index++) {
            String line = input.getLines().get(input_index);
            if(line.isBlank()) {
                input_index++;
                break;
            }

            String[] parts = line.split("\\|");
            String needsToBePrintedFirst = parts[0];
            String needsToBePrintedAfter = parts[1];

            if(!requirements.containsKey(needsToBePrintedAfter)) {
                requirements.put(needsToBePrintedAfter, new ArrayList<>());
            }
            requirements.get(needsToBePrintedAfter).add(needsToBePrintedFirst);
        }
        return requirements;
    }

    private long handleValidLine(Map<String, List<String>> requirements, List<String> pagesToPrint) {
        for(int i = 0; i < pagesToPrint.size(); i++) {
            if(isCurrentIndexIncorrectlySorted(requirements, pagesToPrint, i)) {
                return 0;
            }
        }

        return Long.parseLong(pagesToPrint.get((int) Math.floor(pagesToPrint.size() / 2.0)));
    }

    private long handleInvalidLines(Map<String, List<String>> requirements, List<String> pagesToPrint) {
        for(int i = 0; i < pagesToPrint.size(); i++) {
            if(isCurrentIndexIncorrectlySorted(requirements, pagesToPrint, i)) {
                return handleValidLine(requirements, sort(requirements, pagesToPrint));
            }
        }

        return 0;
    }

    private boolean isCurrentIndexIncorrectlySorted(Map<String, List<String>> requirements, List<String> pagesToPrint, int i) {
        String page = pagesToPrint.get(i);
        List<String> currentRequirements = requirements.getOrDefault(page, List.of());

        for(String req: currentRequirements) {
            if(pagesToPrint.contains(req) && !pagesToPrint.subList(0, i).contains(req)) {
                return true;
            }
        }
        return false;
    }

    private List<String> sort(Map<String, List<String>> requirements, List<String> pagesToPrint) {
        return pagesToPrint.stream()
            .sorted((firstElement, secondElement) -> {
                //If the second element is a requirement for the first one, the first element is "bigger" so it stays on the right
                // Otherwise we can swap them without consequences
                List<String> currentRequirements = requirements.getOrDefault(firstElement, List.of());
                if(currentRequirements.contains(secondElement)) return 1;
                return -1;
            })
            .toList();
    }
}
