package dev.phoenixofforce.adventofcode.y2023.day19;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.w3c.dom.ranges.Range;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Component
public class Day19_2023 implements Puzzle {

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Part{
        private long x;
        private long m;
        private long a;
        private long s;
    }

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class RangedPart{
        private String nextWorkflow;
        private long xFrom, xTo;
        private long mFrom, mTo;
        private long aFrom, aTo;
        private long sFrom, sTo;

        public RangedPart clone() {
            return new RangedPart(nextWorkflow,
                xFrom, xTo,
                mFrom, mTo,
                aFrom, aTo,
                sFrom, sTo);
        }
    }

    @Getter
    private static class WorkFlow {
        private String name;
        private List<String> rules;
        public WorkFlow(String in) {
            name = in.split("\\{")[0];
            rules = Arrays.stream(in.replaceAll("[{}]", " ")
                .trim()
                .split(" ")[1]
                .split(","))
                .toList();
        }

        public String sendToNext(Part part) {
            for(int i = 0; i < rules.size() - 1; i++) {
                String rule = rules.get(i);
                String condition = rule.split(":")[0];
                String returnValue = rule.split(":")[1];

                long compareTo = Long.parseLong(condition.substring(2));
                long partsNumber = switch (condition.charAt(0)) {
                    case 'x' -> part.x;
                    case 'm' -> part.m;
                    case 'a' -> part.a;
                    case 's' -> part.s;
                    default -> throw new IllegalStateException("Unexpected value: " + condition.charAt(1));
                };

                if (switch (condition.charAt(1)) {
                    case '>' -> partsNumber > compareTo;
                    case '<' -> partsNumber < compareTo;
                    default -> throw new IllegalStateException("Unexpected value: " + condition.charAt(1));
                }) {
                    return returnValue;
                }
            }
            return rules.getLast();
        }

        List<RangedPart> sendToNext(RangedPart rangedPart) {
            List<RangedPart> out = new ArrayList<>();
            RangedPart current = rangedPart.clone();
            for(int i = 0; i < rules.size() - 1; i++) {
                String rule = rules.get(i);
                String condition = rule.split(":")[0];
                String returnValue = rule.split(":")[1];

                RangedPart smaller = current.clone();
                RangedPart bigger = current.clone();

                boolean shouldBeSmaller = condition.charAt(1) == '<';
                int smallerOffset = shouldBeSmaller? -1: 0;
                int biggerOffset = shouldBeSmaller? 0: 1;

                long compareTo = Long.parseLong(condition.substring(2));
                switch (condition.charAt(0)) {
                    case 'x' -> {
                        smaller.setXTo(compareTo + smallerOffset);
                        bigger.setXFrom(compareTo + biggerOffset);
                    }
                    case 'm' -> {
                        smaller.setMTo(compareTo + smallerOffset);
                        bigger.setMFrom(compareTo + biggerOffset);
                    }
                    case 'a' -> {
                        smaller.setATo(compareTo + smallerOffset);
                        bigger.setAFrom(compareTo + biggerOffset);
                    }
                    case 's' -> {
                        smaller.setSTo(compareTo + smallerOffset);
                        bigger.setSFrom(compareTo + biggerOffset);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + condition.charAt(1));
                }

                if(shouldBeSmaller) {
                    smaller.setNextWorkflow(returnValue);
                    out.add(smaller);
                    current = bigger;
                }
                else {
                    bigger.setNextWorkflow(returnValue);
                    out.add(bigger);
                    current = smaller;
                }
            }
            current.setNextWorkflow(rules.getLast());
            out.add(current);

            return out;
        }
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        Map<String, WorkFlow> workflows = new HashMap<>();
        boolean inWorkflowPart = true;

        List<Part> accepted = new ArrayList<>();
        for(int i = 0; i < input.getLines().size(); i++) {
            String line = input.getLines().get(i);
            if(line.isEmpty()) {
                inWorkflowPart = false;
                continue;
            }
            if(inWorkflowPart) {
                WorkFlow workFlow = new WorkFlow(line);
                workflows.put(workFlow.getName(), workFlow);
                continue;
            }

            String[] params = line.substring(1, line.length() - 1).split(",");
            Part part = new Part(
                Integer.parseInt(params[0].substring(2)),
                Integer.parseInt(params[1].substring(2)),
                Integer.parseInt(params[2].substring(2)),
                Integer.parseInt(params[3].substring(2))
            );

            String current = "in";
            while(true) {
                WorkFlow currentWorkflow = workflows.get(current);
                current = currentWorkflow.sendToNext(part);
                if(current.equals("A")) {
                    accepted.add(part);
                    break;
                }
                if(current.equals("R")) {
                    break;
                }
            }
        }

        return accepted.stream()
            .mapToLong(e -> e.x + e.m + e.a + e.s)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Map<String, WorkFlow> workflows = new HashMap<>();
        for(int i = 0; i < input.getLines().size(); i++) {
            String line = input.getLines().get(i);
            if(line.isEmpty()) {
                break;
            }
            WorkFlow workFlow = new WorkFlow(line);
            workflows.put(workFlow.getName(), workFlow);

        }

        List<RangedPart> toCheck = new ArrayList<>();
        toCheck.add(new RangedPart("in", 1, 4000,
            1, 4000, 1, 4000, 1, 4000));

        long out = 0;
        while(!toCheck.isEmpty()) {
            RangedPart current = toCheck.remove(0);

            if(current.getNextWorkflow().equals("R")) {
                continue;
            }

            if(current.getNextWorkflow().equals("A")) {
                out += (LongStream.range(current.xFrom, current.xTo + 1).count() *
                    LongStream.range(current.mFrom, current.mTo + 1).count() *
                    LongStream.range(current.aFrom, current.aTo + 1).count() *
                    LongStream.range(current.sFrom, current.sTo + 1).count());
                continue;
            }

            WorkFlow currentWorkFlow = workflows.get(current.nextWorkflow);
            List<RangedPart> nextStates = currentWorkFlow.sendToNext(current);
            toCheck.addAll(nextStates);
        }

        return out;
    }

}
