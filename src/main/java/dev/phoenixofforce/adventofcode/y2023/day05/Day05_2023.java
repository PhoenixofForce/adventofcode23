package dev.phoenixofforce.adventofcode.y2023.day05;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class Day05_2023 implements Puzzle {

    @Data
    static class ConverterRange {

        private final List<Long> map;
        public ConverterRange(String mapParagraph) {
            map = Arrays.stream(mapParagraph.split(":")[1].trim().split(" "))
                .map(Long::parseLong)
                .toList();
        }

        private long map(long source) {
            for(int i = 0; i < map.size(); i += 3) {
                long destinationStart = map.get(i);
                long sourceStart = map.get(i + 1);
                long range = map.get(i + 2);

                long sourceEnd = sourceStart + range;

                if(sourceStart <= source && source < sourceEnd) {
                    long difference = source - sourceStart;
                    return destinationStart + difference;
                }
            }

            return source;
        }

    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Long> current = Arrays.stream(input.getParagraphs().get(0)
            .split(":")[1].trim().split(" "))
            .map(Long::parseLong)
            .toList();

        List<ConverterRange> ranges = new ArrayList<>();
        for(int i = 1; i < input.getParagraphs().size(); i++) ranges.add(new ConverterRange(input.getParagraphs().get(i)));
        return getLowestLocation(ranges, current);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Long> current = Arrays.stream(input.getParagraphs().get(0)
                .split(":")[1].trim().split(" "))
            .map(Long::parseLong)
            .toList();

        @Data @RequiredArgsConstructor
        class Range {
            private final long from;
            private final long to;

            public boolean contains(Long r) {
                return from <= r && r < to;
            }
        }

        List<ConverterRange> ranges = new ArrayList<>();
        for(int i = 1; i < input.getParagraphs().size(); i++) ranges.add(new ConverterRange(input.getParagraphs().get(i)));

        //cutoff duplicates
        List<Range> toVisit = new ArrayList<>();
        for(int i = 0; i < current.size(); i += 2) {
            AtomicLong start = new AtomicLong(current.get(i));
            long range = current.get(i + 1);
            AtomicLong end = new AtomicLong(start.get() + range);

            if(toVisit.stream().anyMatch(r -> r.contains(start.get()) && r.contains(end.get()))) continue;
            toVisit.stream()
                .filter(r -> r.contains(start.get()))
                .max(Comparator.comparing(Range::getFrom))
                .ifPresent(r -> start.set(r.getFrom()));

            toVisit.stream()
                .filter(r -> r.contains(end.get()))
                .min(Comparator.comparing(Range::getTo))
                .ifPresent(r -> end.set(r.getFrom()));

            toVisit.add(new Range(start.get(), end.get()));
        }

        AtomicReference<Long> lowest = new AtomicReference<>();
        lowest.set(null);

        int i = 0;
        for(Range currentRange: toVisit) {
            long start = currentRange.from;
            long range = currentRange.to - currentRange.from;

            for(long j = 0; j < range; j++) {
                if(j % 100000L == 0) System.out.println(i + "/" + toVisit.size() + "\t" + j + "/" + range + " = " + lowest.get());

                long currentSeed = start + j;
                long location = getLowestLocation(ranges, List.of(currentSeed));
                if(lowest.get() == null || location < lowest.get() && location != 0) lowest.set(location);
                j += getSmallestIndexSkip(ranges, currentSeed);
            }

            i++;
        }

        return lowest;
    }

    private long getLowestLocation(List<ConverterRange> converterRanges, Collection<Long> current) {
        current = current.stream()
            .map(l -> {
                AtomicLong out = new AtomicLong(l);
                converterRanges.forEach(r -> out.set(r.map(out.get())));
                return out.get();
            })
            .toList();

        return current.stream().sorted().findFirst().orElse(Long.MAX_VALUE);
    }

    private long getSmallestIndexSkip(List<ConverterRange> converterRanges, long value) {
        long smallestJump = Long.MAX_VALUE;

        for(ConverterRange converterRange: converterRanges) {
            long nearestStart = Long.MAX_VALUE;

            for(int i = 0; i < converterRange.map.size(); i += 3) {
                long destinationStart = converterRange.map.get(i);
                long sourceStart = converterRange.map.get(i + 1);
                long range =  converterRange.map.get(i + 2);

                long sourceEnd = sourceStart + range;

                if(sourceStart <= value && value < sourceEnd) { //jump to end of current mapping
                    long difference = value - sourceStart;
                    nearestStart = sourceEnd - value - 1;
                    value = destinationStart + difference;
                    break;
                }

                else if(value < sourceStart) {  //jump to start of next mapping
                    nearestStart = sourceStart - value - 1;
                }
            }

            smallestJump = Math.min(nearestStart, smallestJump);
        }

        return smallestJump;
    }
}