package dev.phoenixofforce.adventofcode.y2021.day23;

import dev.phoenixofforce.adventofcode.solver.ArrayUtils;
import dev.phoenixofforce.adventofcode.solver.Direction;
import lombok.Getter;

import java.util.*;

public class State {

	static Map<Character, Integer> costs = Map.of('A', 1, 'B', 10, 'C', 100, 'D', 1000);
	static Map<Character, Integer> roomX = Map.of('A', 3, 'B', 5, 'C', 7, 'D', 9);

	record Position(int x, int y) { }

	private final Set<Position> characterPositions;
	private final char[][] map;
	@Getter
	private final int score;

	public State(char[][] map, int score) {
		this.map = map;
		this.score = score;

		characterPositions = new HashSet<>();
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++) {
				if(isFrog(map[y][x])) {
					characterPositions.add(new Position(x, y));
				}
			}
		}
	}

	private boolean isFrog(char c) {
		return !List.of('.', '#', ' ').contains(c);
	}

	public List<State> getStates() {
		List<State> out = new LinkedList<>();

		for(Position p: characterPositions) {
			char c = map[p.y][p.x];

			if (isFrog(c)) {
				if(isHome(p, c)) continue;

				Set<Position> moves = getAllMoves(p);
				if(p.y != 1) {
					for(Position m: moves) {
						if(isHall(m)) {
							char[][] copy = ArrayUtils.copy(map);
							copy[p.y][p.x] = '.';
							copy[m.y][m.x] = c;
							int moveCosts = Math.abs(m.x - p.x) + Math.abs(m.y - p.y);
							moveCosts *= costs.get(c);

							out.add(new State(copy, moveCosts + score));
						}
					}
				} else {
					for(Position m: moves) {
						if(isHome(m, c)) {
							int dy = 1;
							while(map[m.y + dy][m.x] == '.' && isHome(new Position(m.x, m.y + dy), c)) {
								dy++;
							}dy--;

							Position m2 = new Position(m.x, m.y + dy);

							char[][] copy = ArrayUtils.copy(map);
							copy[p.y][p.x] = '.';
							copy[m2.y][m2.x] = c;
							int moveCosts = Math.abs(m2.x - p.x) + Math.abs(m2.y - p.y);
							moveCosts *= costs.get(c);

							List<State> realOut = new ArrayList<>();
							realOut.add(new State(copy, moveCosts + score));
							return realOut;
						}
					}
				}
			}
		}

		return out;
	}

	boolean isHome(Position m, char c) {
		if (!(m.x == roomX.get(c) && m.y > 1)) return false;
		for(int i=-3; i<=3; i++) {
			if(!(m.y+i  > 1 && m.y + i < map.length - 1)) continue;

			char o = map[m.y+i][m.x];
			if (isFrog(o)) {
				if (o != c) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean isHall(Position p) {
		if(p.y != 1) return false;
		if(map[p.y][p.x] != '.') return false;
		if(map[p.y + 1][p.x] != '#') return false;

		return true;
	}

	private Set<Position> getAllMoves(Position start) {
		Set<Position> out = new HashSet<>();
		List<Position> open = new ArrayList<>(getNeighbors(start));

		while(!open.isEmpty()) {
			Position p = open.remove(0);
			if(!out.contains(p)) {
				out.add(p);
				open.addAll(getNeighbors(p));
			}
		}

		return out;
	}

	private List<Position> getNeighbors(Position position) {
		List<Position> out = new ArrayList<>(4);

		List<Position> positions = Direction.getNeighbors4(Position::new, position.x, position.y);
		for(Position position1 : positions) {
			if(map[position1.y][position1.x] == '.') out.add(position1);
		}

		return out;
	}

	public boolean isEnd() {
		boolean allAs = map[2][3] == 'A' && map[3][3] == 'A';
		boolean allBs = map[2][5] == 'B' && map[3][5] == 'B';
		boolean allCs = map[2][7] == 'C' && map[3][7] == 'C';
		boolean allDs = map[2][9] == 'D' && map[3][9] == 'D';

		if(map.length > 5) {
			allAs &= map[4][3] == 'A' && map[5][3] == 'A';
			allBs &= map[4][5] == 'B' && map[5][5] == 'B';
			allCs &= map[4][7] == 'C' && map[5][7] == 'C';
			allDs &= map[4][9] == 'D' && map[5][9] == 'D';
		}

		return allAs && allBs && allCs && allDs;
	}

	public int getHeuristic() {
		int out = 0;

		for(Position p: characterPositions) {
			char c = map[p.y][p.x];

			int moves = 0;
			moves += Math.abs(p.y - 1) * 2;
			moves += Math.abs(p.x - roomX.get(c));

			out += moves * costs.get(c);
		}

		return out;
	}

	@Override
	public String toString() {
		return ArrayUtils.toString(map);
	}

	@Override
	public boolean equals(Object o2) {
		if(o2 instanceof State s) {
			return s.toString().equals(this.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(toString());
	}
}