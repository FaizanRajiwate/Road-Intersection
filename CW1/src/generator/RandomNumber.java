package generator;

import java.util.Random;

public class RandomNumber {

	public static Random generateRandomSeed() {
		Random random = new Random();
//		random.setSeed(seed);
		return random;
	}

}
