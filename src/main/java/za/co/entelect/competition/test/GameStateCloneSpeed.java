package za.co.entelect.competition.test;

import java.util.ArrayList;

import za.co.entelect.competition.GameState;
import za.co.entelect.competition.MapGenerator;

public class GameStateCloneSpeed {
	public static void main(String[] args) {
		GameState gameState = MapGenerator.generateRandom(100, 120, 20, 20, 1, 0.3, true, true, 2, 2, 47515685604563L);
		
		System.out.println(gameState);
		System.out.println();
		
		boolean timeLimited = true;
		
		if (timeLimited) {
			ArrayList<Long> timePerTest = new ArrayList<Long>(); 
			
			final int testSize = 1000000;
			long time = 0;
			
			int numWarmupTests = 3;
			for (int i = 0; i < numWarmupTests; i++) {
				time = System.nanoTime();
				for (int j = 0; j < testSize; j++) {
					doTest1(gameState);
				}				
				timePerTest.add(System.nanoTime() - time);
			}
			
			int numTests = 10;
			for (int i = 0; i < numTests; i++) {
				time = System.nanoTime();
				for (int j = 0; j < testSize; j++) {
					doTest1(gameState);
				}				
				timePerTest.add(System.nanoTime() - time);
			}
	
			long min = Long.MAX_VALUE;
			int minIdx = 0;
			for (int i = 0; i < timePerTest.size(); i++) {
				if (timePerTest.get(i) < min) {
					min = timePerTest.get(i);
					minIdx = i;
				}
			}
			
			System.out.println("Testing the minimum time (ns) to perform " + testSize + " operations.");
			System.out.println("==== Results ===");
			for (int i = 0; i < timePerTest.size(); i++) {
				if (i == 0) {
					System.out.println("==== Warmup ====");			
				} else if (i == numWarmupTests) {
					System.out.println("== Real tests ==");			
				}
				String extra = new String();
				if (i == minIdx) {
					extra = " <-- best result!";
				}
				System.out.println("Time per test: " + timePerTest.get(i) + extra);		
			}
		} else {
			ArrayList<Integer> clonesPerTest = new ArrayList<Integer>(); 
			long time = 0;
			
			double millisecondsPerTest = 100;
			int counter = 0;
			int numWarmupTests = 3;
			for (int i = 0; i < numWarmupTests; i++) {
				counter = 0;
				time = System.currentTimeMillis();
				
				while (System.currentTimeMillis() - millisecondsPerTest < time) {
					doTest1(gameState);
					counter++;
				}
				clonesPerTest.add(counter);
			}
			
			int numTests = 10;
			for (int i = 0; i < numTests; i++) {
				counter = 0;
				time = System.currentTimeMillis();
				
				while (System.currentTimeMillis() - millisecondsPerTest < time) {
					doTest1(gameState);
					counter++;
				}
				clonesPerTest.add(counter);
			}			
	
			int max = 0;
			int maxIdx = 0;
			for (int i = 0; i < clonesPerTest.size(); i++) {
				if (clonesPerTest.get(i) > max) {
					max = clonesPerTest.get(i);
					maxIdx = i;
				}
			}
			
			System.out.println("Testing the maximum number of operations that can happen in " + String.format("%.0f", millisecondsPerTest) + "ms");
			System.out.println("==== Results ===");
			for (int i = 0; i < clonesPerTest.size(); i++) {
				if (i == 0) {
					System.out.println("==== Warmup ====");			
				} else if (i == numWarmupTests) {
					System.out.println("== Real tests ==");			
				}
				String extra = new String();
				if (i == maxIdx) {
					extra = " <-- best result!";
				}
				System.out.println("Clones done: " + clonesPerTest.get(i) + extra);		
			}
		}		
	}

	public static void doTest1(GameState gameState) {
		gameState.clone();
		
//		int[][] map = gameState.getMap(); 
//		int[][] newMap = new int[map.length][map[0].length];
//		for (int y = 0; y < newMap.length; y++) {
//			for (int x = 0; x < newMap[0].length; x++) {
//				newMap[y][x] = map[y][x];
//			}
//		}
		
//		int[][] map = gameState.getMap(); 
//		final int mLength = map.length;
//		int[][] newMap = new int[mLength][];
//		for (int y = 0; y < mLength; y++) {
//			newMap[y] = map[y].clone();
//		}
	}	
}
