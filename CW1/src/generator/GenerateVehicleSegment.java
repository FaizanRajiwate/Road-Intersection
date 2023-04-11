package generator;

import java.util.Random;

public class GenerateVehicleSegment implements GeneratingStrategy {

	@Override
	public String GenerateObject() {
		
		String[] vehicleSegments = {"1", "2", "3", "4"};
		Random random = RandomNumber.generateRandomSeed();
		int index = random.nextInt(4);
		String vehicleType = vehicleSegments[index];
		return vehicleType;
	}

}
