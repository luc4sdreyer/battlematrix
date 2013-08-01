package za.co.entelect.competition.test;

import java.util.Random;

import org.apache.commons.math3.random.MersenneTwister;

import cern.jet.random.engine.MersenneTwister64;

public class Zobrist {

	public Zobrist() {
		
	}
	
	public static void main(String[] args) {
		//BitsStreamGenerator gen = new BitsStreamGenerator(
		MersenneTwister gen1 = new MersenneTwister((new java.util.Date()).getTime());
		MersenneTwister64 gen2 = new MersenneTwister64(new java.util.Date());
		Random gen3 = new Random();
		int[] collisions = new int[3];
		
		for (int i = 0; i < 100000000; i++) {
			long gen1a = gen1.nextLong();
			long gen1b = gen1.nextLong();
			long gen1c = gen1.nextLong();
			long gen2a = gen2.nextLong();
			long gen2b = gen2.nextLong();
			long gen2c = gen2.nextLong();
			long gen3a = gen3.nextLong();
			long gen3b = gen3.nextLong();
			long gen3c = gen3.nextLong();
			
			if (((gen1a ^ gen1b) ^ gen1c) == 0) {
				collisions[0]++;
			}
			if (((gen2a ^ gen2b) ^ gen2c) == 0) {	
				collisions[1]++;
			}
			if (((gen3a ^ gen3b) ^ gen3c) == 0) {
				collisions[2]++;
			}
		}
		
		for (int i = 0; i < collisions.length; i++) {
			System.out.println("collisions[" + i + "]: " + collisions[i]);
		}
	}
}
