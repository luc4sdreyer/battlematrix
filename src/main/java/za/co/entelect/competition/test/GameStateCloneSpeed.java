package za.co.entelect.competition.test;

import java.util.ArrayList;

import za.co.entelect.competition.GameState;
import za.co.entelect.competition.MapGenerator;

public class GameStateCloneSpeed {
	public static void main(String[] args) {
		GameState gameState = MapGenerator.generateRandom(100, 120, 20, 20, 1, 0.3, true, true, 2, 2, 47515685604563L);
		
		System.out.println(gameState);
		System.out.println();
		
		ArrayList<Integer> clonesPerTest = new ArrayList<Integer>(); 
		long time = 0;
		
		double millisecondsPerTest = 1000;
		int counter = 0;
		int numWarmupTests = 3;
		for (int i = 0; i < numWarmupTests; i++) {
			counter = 0;
			time = System.currentTimeMillis();
			
			while (System.currentTimeMillis() - millisecondsPerTest < time) {
				gameState.clone();
				counter++;
			}
			clonesPerTest.add(counter);
		}
		
		int numTests = 10;
		for (int i = 0; i < numTests; i++) {
			counter = 0;
			time = System.currentTimeMillis();
			
			while (System.currentTimeMillis() - millisecondsPerTest < time) {
				gameState.clone();
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
