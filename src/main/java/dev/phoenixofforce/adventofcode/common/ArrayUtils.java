package dev.phoenixofforce.adventofcode.common;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

	public interface Cloner<T> {
		T clone(T t);
	}

	public interface CountIf<T> {
		boolean countIf(T t);
	}

	/* 
		Generic
		Char
		Boolean
		Long
		Double
		Float
		Int
	 */

	//>--| STRING LIST TO ARRAY
	public static char[][] strings2CharArray(List<String> lines) {
		char[][] out = new char[lines.size()][lines.get(0).length()];
		for(int i = 0; i < lines.size(); i++) {
			for(int j = 0; j < lines.get(0).length(); j++) {
				out[i][j] = lines.get(i).charAt(j);
			}
		}

		return out;
	}

	public static boolean[][] strings2BooleanArray(List<String> lines, char trueChar) {
		boolean[][] out = new boolean[lines.size()][lines.get(0).length()];
		for(int i = 0; i < lines.size(); i++) {
			for(int j = 0; j < lines.get(0).length(); j++) {
				out[i][j] = lines.get(i).charAt(j) == trueChar;
			}
		}

		return out;
	}

	public static int[][] strings2IntArray(List<String> lines) {
		int[][] out = new int[lines.size()][lines.get(0).length()];
		for(int i = 0; i < lines.size(); i++) {
			for(int j = 0; j < lines.get(0).length(); j++) {
				out[i][j] = Integer.parseInt(lines.get(i).charAt(j)+"");
			}
		}

		return out;
	}

	//>--| TO STRING

	public static <T> String toString(T[][] map) {
		StringBuilder out = new StringBuilder();
		for(T[] c: map) {
			out.append(Arrays.toString(c)).append("\r\n");
		}
		return out.toString();
	}

	public static String toString(boolean[][] map) {
		StringBuilder out = new StringBuilder();
		for (boolean[] a : map) {
			for (boolean b : a) {
				out.append(b ? '#' : '.');
			}
			out.append("\r\n");
		}
		return out.toString();
	}

	public static String toString(char[][] map) {
		StringBuilder out = new StringBuilder();
		for(char[] c: map) {
			out.append(new String(c)).append("\r\n");
		}
		return out.toString();
	}

	public static String toString(int[][] map) {
		StringBuilder out = new StringBuilder();
		for(int[] c: map) {
			for (int i : c) {
				out.append(i);
			}
			out.append("\r\n");
		}
		return out.toString();
	}

	//>--| COPY

	public static <T> T[][] copy(T[][] map, Cloner<T> cloner) {
		T[][] out = (T[][]) new Object[map.length][map[0].length];
		for(int x = 0; x < map.length; x++) {
			for(int y  = 0; y < map[0].length; y++) {
				out[x][y] = cloner.clone(map[x][y]);
			}
		}

		return out;
	}

	public static char[][] copy(char[][] map) {
		char[][] out = new char[map.length][map[0].length];
		for(int x = 0; x < map.length; x++) {
			System.arraycopy(map[x], 0, out[x], 0, map[0].length);
		}

		return out;
	}

	public static boolean[][] copy(boolean[][] map) {
		boolean[][] out = new boolean[map.length][map[0].length];
		for(int x = 0; x < map.length; x++) {
			System.arraycopy(map[x], 0, out[x], 0, map[0].length);
		}

		return out;
	}

	public static long[][] copy(long[][] map) {
		long[][] out = new long[map.length][map[0].length];
		for(int x = 0; x < map.length; x++) {
			System.arraycopy(map[x], 0, out[x], 0, map[0].length);
		}

		return out;
	}

	public static double[][] copy(double[][] map) {
		double[][] out = new double[map.length][map[0].length];
		for(int x = 0; x < map.length; x++) {
			System.arraycopy(map[x], 0, out[x], 0, map[0].length);
		}

		return out;
	}

	public static float[][] copy(float[][] map) {
		float[][] out = new float[map.length][map[0].length];
		for(int x = 0; x < map.length; x++) {
			System.arraycopy(map[x], 0, out[x], 0, map[0].length);
		}

		return out;
	}

	public static int[][] copy(int[][] map) {
		int[][] out = new int[map.length][map[0].length];
		for(int x = 0; x < map.length; x++) {
			System.arraycopy(map[x], 0, out[x], 0, map[0].length);
		}

		return out;
	}

	//>--| COUNT IF

	public static <T> int countIf(T[][] in, CountIf<T> countif) {
		int out = 0;
		for (T[] ts : in) {
			for (int x = 0; x < in[0].length; x++) {
				if (countif.countIf(ts[x])) out++;
			}
		}

		return out;
	}

	public static int countIf(boolean[][] in) {
		int out = 0;
		for (boolean[] a : in) {
			for (int x = 0; x < in[0].length; x++) {
				if (a[x]) out++;
			}
		}

		return out;
	}

	public static int countIf(boolean[][] in, CountIf<Boolean> countif) {
		int out = 0;
		for (boolean[] a : in) {
			for (int x = 0; x < in[0].length; x++) {
				if (countif.countIf(a[x])) out++;
			}
		}

		return out;
	}

	public static int countIf(char[][] in, CountIf<Character> countif) {
		int out = 0;
		for (char[] a : in) {
			for (int x = 0; x < in[0].length; x++) {
				if (countif.countIf(a[x])) out++;
			}
		}

		return out;
	}

	public static int countIf(int[][] in, CountIf<Integer> countif) {
		int out = 0;
		for (int[] a : in) {
			for (int x = 0; x < in[0].length; x++) {
				if (countif.countIf(a[x])) out++;
			}
		}

		return out;
	}
}