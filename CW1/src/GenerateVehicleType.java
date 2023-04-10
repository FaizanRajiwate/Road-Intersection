import java.util.Random;

public class GenerateVehicleType implements GeneratingStrategy {

	@Override
	public String GenerateObject() {
		// TODO Auto-generated method stub
		String[] vehicleTypes = {"bus", "car", "truck"};
		Random random = RandomNumber.generateRandomSeed();
		int index = random.nextInt(3);
		String vehicleType = vehicleTypes[index];
		return vehicleType;
	}

}
