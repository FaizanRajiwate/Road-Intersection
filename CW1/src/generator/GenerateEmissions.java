package generator;

import java.util.Random;

public class GenerateEmissions implements GeneratingStrategy {

	@Override
	public String GenerateObject() {
		Random random = RandomNumber.generateRandomSeed();
		int aMax = 50;
		int aMin = 1;
		float emissions = aMin + random.nextFloat() * (aMax - aMin);
		return "" + emissions;
	}

}
