package dev.phoenixofforce.adventofcode.y2024.day22;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day22_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getLines()
            .stream()
            .mapToLong(Long::parseLong)
            .map(this::generate2000thsSecretNumber)
            .sum();
    }

    @Data @AllArgsConstructor
    private static class PriceHistory {
        private long change0, change1, change2, change3;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<List<Long>> prices = input.getLines()
            .stream()
            .map(Long::parseLong)
            .map(this::generateSecretNumbers)
            .map(secretNumbers -> secretNumbers.stream().map(number -> number % 10).toList())
            .toList();

        List<Map<PriceHistory, Long>> priceEvolution = new ArrayList<>();
        for(List<Long> price: prices) {
            Map<PriceHistory, Long> evolution = getPriceHistoryMap(price);
            priceEvolution.add(evolution);
        }

        Set<PriceHistory> allPriceHistories = priceEvolution.stream()
            .flatMap(e -> e.keySet().stream())
            .collect(Collectors.toSet());

        return allPriceHistories.stream()
            .mapToLong(history -> priceEvolution.stream()
                .mapToLong(historyMap -> historyMap.getOrDefault(history, 0L))
                .sum()
            )
            .max().orElse(0);
    }

    private static Map<PriceHistory, Long> getPriceHistoryMap(List<Long> price) {
        Map<PriceHistory, Long> evolution = new HashMap<>();
        for(int i = 4; i < price.size(); i++) {
            long priceDiff0 = price.get(i - 3) - price.get(i - 4);
            long priceDiff1 = price.get(i - 2) - price.get(i - 3);
            long priceDiff2 = price.get(i - 1) - price.get(i - 2);
            long priceDiff3 = price.get(i) - price.get(i - 1);
            long actualPrice = price.get(i);

            PriceHistory current = new PriceHistory(priceDiff0, priceDiff1, priceDiff2, priceDiff3);
            if(!evolution.containsKey(current)) evolution.put(current, actualPrice);
        }
        return evolution;
    }

    private List<Long> generateSecretNumbers(long secretNumber) {
        List<Long> secretNumbers = new ArrayList<>();
        secretNumbers.add(secretNumber);
        for(int i = 0; i < 2000; i++) {
            secretNumber = getNextSecretNumber(secretNumber);
            secretNumbers.add(secretNumber);
        }
        return secretNumbers;
    }

    private long generate2000thsSecretNumber(long secretNumber) {
        for(int i = 0; i < 2000; i++) {
            secretNumber = getNextSecretNumber(secretNumber);
        }
        return secretNumber;
    }

    private static long getNextSecretNumber(long secretNumber) {
        secretNumber = (secretNumber ^ (secretNumber * 64)) % 16777216;
        secretNumber = (secretNumber ^ (secretNumber / 32)) % 16777216;
        secretNumber = (secretNumber ^ (secretNumber * 2048)) % 16777216;
        return secretNumber;
    }

}
