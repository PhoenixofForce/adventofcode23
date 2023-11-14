package dev.phoenixofforce.adventofcode.y2021.day04;

import java.util.Arrays;
import java.util.List;

public class BingoBoard {
	
	public static final int BINGO_SIZE = 5;
	
	private final int[][] numbers;
	private final boolean[][] wasNumberChosen;
	
	private boolean won = false;
	
	public BingoBoard(String line) {
		numbers = new int[BINGO_SIZE][BINGO_SIZE];
		wasNumberChosen = new boolean[BINGO_SIZE][BINGO_SIZE];
		
		line = line.replaceAll(" +", " ");
		List<Integer> boardNumbers = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
		
		for(int i = 0; i < boardNumbers.size(); i++) {
			numbers[i/BINGO_SIZE][i % BINGO_SIZE] = boardNumbers.get(i);
		}
	}
	
	public boolean drawNumber(int num) {
		if(won) return false;
		
		for(int x = 0; x < numbers.length; x++) {
			for(int y = 0; y < numbers.length; y++) {
				if(numbers[x][y] == num) wasNumberChosen[x][y] = true;
			}
		}
		
		return checkWin();
	}

	public boolean checkWin() {
		boolean out = false;
		
		for(int x = 0; x < numbers.length; x++) {
			boolean allFive1 = true;
			boolean allFive2 = true;
						
			for(int y = 0; y < numbers.length; y++) {
				if(!wasNumberChosen[x][y]) allFive1 = false;
				if(!wasNumberChosen[y][x]) allFive2 = false;
			}
			if(allFive1 || allFive2) out = true;
		}
		
		if(out) won = true;
		return out;
	}
	
	public int getUnmarkedSum( ) {
		int out = 0;
		
		for(int x = 0; x < numbers.length; x++) {
			for(int y = 0; y < numbers.length; y++) {
				if(!wasNumberChosen[x][y]) out += numbers[x][y];
			}
		}
		
		return out;
	}
}