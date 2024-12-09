package dev.phoenixofforce.adventofcode.y2024.day09;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

@Component
public class Day09_2024 implements Puzzle {

    private static final int FREE_SPACE_ID = -1;

    @Data
    @AllArgsConstructor
    private static class Block {
        private int id;
        private int size;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Integer> blocks = new ArrayList<>();
        for(int i = 0; i < input.getFile().length(); i += 2) {
            int blockedSize = Integer.parseInt(input.getFile().charAt(i) + "");
            int freeSize = (i+1) < input.getFile().length() ? Integer.parseInt(input.getFile().charAt(i + 1) + ""): 0;

            for(int j = 0; j < blockedSize; j++) blocks.add(i / 2);
            for(int j = 0; j < freeSize; j++) blocks.add(FREE_SPACE_ID);
        }
        trim(blocks);

        while(blocks.contains(FREE_SPACE_ID)) {
            int last = blocks.getLast();
            int indexOfFreeSpace = blocks.indexOf(FREE_SPACE_ID);
            blocks.set(indexOfFreeSpace, last);
            blocks.removeLast();
            trim(blocks);
        }

        long checksum = 0;
        for(long i = 0; i < blocks.size(); i++) {
            checksum += i * blocks.get((int) i);
        }

        return checksum;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Block> blocks = new ArrayList<>();
        int lastId = 0;
        for(int i = 0; i < input.getFile().length(); i += 2) {
            int blockedSize = Integer.parseInt(input.getFile().charAt(i) + "");
            int freeSize = (i+1) < input.getFile().length() ? Integer.parseInt(input.getFile().charAt(i + 1) + ""): 0;

            blocks.add(new Block(i/2, blockedSize));
            blocks.add(new Block(FREE_SPACE_ID, freeSize));
            lastId = i/2;
        }

        for(int i = lastId; i >= 1; i--) {
            int finalI = i;
            OptionalInt indexOfCurrentBlock = findIndex(blocks, block -> block.id == finalI);
            if(indexOfCurrentBlock.isEmpty()) continue;

            Block currentBlock = blocks.get(indexOfCurrentBlock.getAsInt());
            OptionalInt indexOfFreeSpace = findIndex(blocks, block -> block.id == FREE_SPACE_ID && block.size >= currentBlock.size);
            if(indexOfFreeSpace.isEmpty()) continue;

            if(indexOfCurrentBlock.getAsInt() < indexOfFreeSpace.getAsInt()) continue;

            Block freeBlock = blocks.get(indexOfFreeSpace.getAsInt());
            blocks.set(indexOfCurrentBlock.getAsInt(), new Block(FREE_SPACE_ID, currentBlock.size));

            freeBlock.size = freeBlock.size - currentBlock.size;
            blocks.add(indexOfFreeSpace.getAsInt(), currentBlock);

            optimizeSpaces(blocks);
        }

        long checksum = 0;
        int i = 0;
        for(Block b: blocks) {
            for(int j = 0; j < b.size; j++) {
                if(b.id >= 0) checksum += (long) i * b.id;
                i++;
            }
        }
        return checksum;
    }

    private void trim(List<Integer> list) {
        while(list.getLast() == FREE_SPACE_ID) {
            list.removeLast();
        }
    }

    private OptionalInt findIndex(List<Block> block, Function<Block, Boolean> filter) {
        return IntStream.range(0, block.size())
            .filter(e -> filter.apply(block.get(e)))
            .findFirst();
    }

    private void optimizeSpaces(List<Block> blocks) {
        //combine free blocks
        for(int i = blocks.size() - 2; i >= 0; i--) {
            if(blocks.get(i).id != FREE_SPACE_ID || blocks.get(i + 1).id != FREE_SPACE_ID) continue;
            blocks.get(i).size += blocks.get(i + 1).size;
            blocks.remove(i + 1);
        }

        //remove empty free blocks
        for(int i = blocks.size() - 1; i >= 0; i--) {
            if(blocks.get(i).id == FREE_SPACE_ID && blocks.get(i).size <= 0) {
                blocks.remove(i);
            }
        }
    }
}
