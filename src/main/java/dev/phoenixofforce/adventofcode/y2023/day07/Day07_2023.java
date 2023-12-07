package dev.phoenixofforce.adventofcode.y2023.day07;

import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day07_2023 implements Puzzle {

    private static boolean isPart2 = false;

    @Data
    static class Hand implements Comparable<Hand> {

        private String original;

        private List<Integer> cardsInHand;
        private long bid;
        private int handValue = 0;

        public Hand(String in) {
            original = in;
            cardsInHand = in.split(" ")[0].chars()
                .mapToObj(e -> (char) e)
                .mapToInt(e -> switch(e) {
                    case '2' -> 2;
                    case '3' -> 3;
                    case '4' -> 4;
                    case '5' -> 5;
                    case '6' -> 6;
                    case '7' -> 7;
                    case '8' -> 8;
                    case '9' -> 9;
                    case 'T' -> 10;
                    case 'J' -> isPart2? 1: 11;
                    case 'Q' -> 12;
                    case 'K' -> 13;
                    case 'A' -> 14;
                    default -> 0;
                })
                .boxed()
                .toList();
            bid = Long.parseLong(in.split(" ")[1]);
            handValue = calcHandValue();
        }

        private int calcHandValue() {
            List<Long> cardCounts = new ArrayList<>();
            int highestIndex = 0;
            for(int i = 0; i < 15; i++) {
                int finalI = i;
                long amount = cardsInHand.stream().filter(e -> e == finalI).count();
                cardCounts.add(amount);
                if(amount > cardCounts.get(highestIndex) && i != 1) highestIndex = i; //1 = J
            }
            if(isPart2) {
                cardCounts.set(highestIndex, cardCounts.get(highestIndex) + cardCounts.get(1));
                cardCounts.set(1, 0L); //remove jacks
            }

            //five kind
            if(cardCounts.stream().anyMatch(e -> e == 5)) return 6;
            //four kind
            else if(cardCounts.stream().anyMatch(e -> e == 4)) return 5;
            //full house
            else if(cardCounts.stream().anyMatch(e -> e == 3) && cardCounts.stream().anyMatch(e -> e == 2)) return 4;
            //three of a kind
            else if(cardCounts.stream().anyMatch(e -> e == 3) && cardCounts.stream().noneMatch(e -> e == 2)) return 3;
            //two pair
            else if(cardCounts.stream().filter(e -> e == 2).count() == 2) return 2;
            //one pair
            else if(cardCounts.stream().filter(e -> e == 2).count() == 1 && cardCounts.stream().noneMatch(e -> e == 3)) return 1;

            //high card
            return 0;
        }

        @Override
        public int compareTo(Hand o) {
            if(handValue != o.handValue) return Integer.compare(handValue, o.handValue);
            for(int i = 0; i < cardsInHand.size(); i++) {
                if(!cardsInHand.get(i).equals(o.cardsInHand.get(i)))
                    return Integer.compare(cardsInHand.get(i), o.cardsInHand.get(i));
            }
            return 0;
        }
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Hand> sorted = input.getLines().stream()
            .map(Hand::new)
            .sorted()
            .toList();

        BigInteger out = BigInteger.ZERO;
        for(int i = 0; i < sorted.size(); i++) {
            System.out.println(i + ": " + sorted.get(i).getHandValue() + " " + sorted.get(i).getOriginal() + " " + sorted.get(i).getCardsInHand());
            out = out.add( BigInteger.valueOf(i+1).multiply(BigInteger.valueOf(sorted.get(i).getBid())) );
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        isPart2 = true;
        return solvePart1(input);
    }



}
