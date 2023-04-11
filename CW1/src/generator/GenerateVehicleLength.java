package generator;

import java.util.Random;

public class GenerateVehicleLength implements GeneratingStrategy{

	@Override
	public String GenerateObject() {
		Random random = RandomNumber.generateRandomSeed();
		int aMax = 8;
		int aMin = 1;
		float vehicleLength = aMin + random.nextFloat() * (aMax - aMin);
		return "" + vehicleLength;
	}

}
