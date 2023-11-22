package dev.phoenixofforce.adventofcode.common;

import java.util.ArrayList;
import java.util.List;

public class DirectionUtils {

	public interface CoordinateParser<T> {
		T parse(int x, int y);
	}

	public enum Direction4 {
		EAST(new int[]{1, 0}), SOUTH(new int[]{0, 1}), WEST(new int[]{-1, 0}), NORTH(new int[]{0, -1});

		private final int[] dir;
		Direction4(int[] dir) {
			this.dir = dir;
		}

		public int[] scaleArray(int length) {
			return new int[] { dir[0] * length, dir[1] * length };
		} 
		
		public int[] toArray() {
			return dir;
		}

		public Direction4 prev() {
			Direction4[] vals = values();
			int pos = this.ordinal() - 1;
			if(pos > 0) pos += vals.length;
			return vals[pos];
		}

		public Direction4 next() {
			Direction4[] vals = values();
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}
	
	public static Direction4 getDirection4(char in) {
		return getDirection4(in + "");
	}

	public static Direction4 getDirection4(String in) {
		return switch (in) {
			case ">", "R", "E" -> Direction4.EAST;
			case "v", "D", "S" -> Direction4.SOUTH;
			case "<", "L", "W" -> Direction4.WEST;
			case "^", "U", "N" -> Direction4.NORTH;
			default -> null;
		};
	}
	
	public static boolean isDirection(char in) {
		return isDirection(in + "");
	}
	
	public static boolean isDirection(String in) {
		return switch (in) {
			case ">", "R", "E", "v", "D", "S", "<", "L", "W", "^", "U", "N" -> true;
			default -> false;
		};
	}
	
	public static boolean isCompassDirection(char in) {
		return isCompassDirection(in + "");
	}
	
	public static boolean isCompassDirection(String in) {
		return switch (in) {
			case "E", "S", "W", "N" -> true;
			default -> false;
		};
	}
	
	public static boolean isArrowDirection(char in) {
		return isArrowDirection(in + "");
	}
	
	public static boolean isArrowDirection(String in) {
		return switch (in) {
			case ">", "v", "<", "^" -> true;
			default -> false;
		};
	}

	public static boolean isRelativeDirection(char in) {
		return isRelativeDirection(in + "");
	}
	
	public static boolean isRelativeDirection(String in) {
		return switch (in) {
			case "R", "D", "L", "U" -> true;
			default -> false;
		};
	}
	
	public static List<int[]> getNeighbors4(int x, int y) {
		return getNeighbors4((a, b) -> new int[]{a, b}, x, y);
	}

	public static <T> List<T> getNeighbors4(CoordinateParser<T> parser, int x, int y) {
		List<T> out = new ArrayList<>();
		for(Direction4 d: Direction4.values()) {
			out.add(parser.parse(x + d.dir[0], y + d.dir[1]));
		}
		return out;
	}

	public static List<int[]> getNeighbors4(int x, int y, int minX, int maxX, int minY, int maxY, boolean turnAround) {
		return getNeighbors4((a, b) -> new int[]{a, b}, x, y, minX, maxX, minY, maxY, turnAround);
	}

	public static <T> List<T> getNeighbors4(CoordinateParser<T> parser, int x, int y, int minX, int maxX, int minY, int maxY, boolean turnAround) {
		int xWidth = Math.abs(maxX - minX);
		int yWidth = Math.abs(maxY - minY);

		List<T> out = new ArrayList<>();
		for(Direction4 d: Direction4.values()) {
			int nx  = x + d.dir[0];
			int ny = y + d.dir[1];

			if(minX <= nx && nx < maxX && minY <= ny && ny < maxY) {
				out.add(parser.parse(nx, ny));
			} else if(turnAround) {
				while(nx < minX) nx += xWidth;
				while(nx >= maxX) nx -= xWidth;

				while(ny < minY) ny += yWidth;
				while(ny >= maxY) ny -= yWidth;

				out.add(parser.parse(nx, ny));
			}
		}
		return out;
	}
}
