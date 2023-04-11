package generator;

import java.util.Random;

public class GenerateCrossingDirection implements GeneratingStrategy {

	@Override
	public String GenerateObject() {
		String[] crossingDirections = { "straight", "left", "right" };
		Random random = RandomNumber.generateRandomSeed();
		int index = random.nextInt(3);
		String crossingDirection = crossingDirections[index];
		return crossingDirection;
	}
}
