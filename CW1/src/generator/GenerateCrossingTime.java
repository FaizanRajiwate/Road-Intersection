package generator;

import java.util.Random;

public class GenerateCrossingTime implements GeneratingStrategy {

	@Override
	public String GenerateObject() {
		Random random = RandomNumber.generateRandomSeed();
		int aMax = 5;
		int aMin = 1;
		float crossingTime = aMin + random.nextFloat() * (aMax - aMin);
		return "" + crossingTime;
	}

}
