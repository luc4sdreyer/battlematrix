package za.co.entelect.competition;

import java.util.Random;

public class Test {
	public static void main(String[] args) {
		//runMemTest();
		runGeometryTest();
	}

	public static void runGeometryTest() {
		for (int direction = 0; direction < 4; direction++) {
			System.out.println("new x:"+(int)Math.sin(Math.PI/2*direction)+" new y: "+(int)Math.cos(Math.PI/2*direction));
		}		
	}

	/**
	 * Test the amount of memory used by a boolean array
	 */
	public static void runMemTest() {
		//int numArrays = 10;
		for (int i = 1; i < 10; i++) {
			allocBooleanArray((int) Math.pow(2, i));
		}
	}

	public static void allocBooleanArray(int n) {
		Random rand = new Random();
		final int numArrays = n;
		final int M = Integer.MAX_VALUE/1024;

		boolean[][] b = new boolean[numArrays][M];

		for (int i = 0; i < numArrays; i++) {
			for (int j = 0; j < M; j++) {
				b[i][j] = rand.nextBoolean();
			}
		}
		System.out.println("Max memory after allocating " +n*M/8+ "B: " + Runtime.getRuntime().maxMemory()); 

	}
}
