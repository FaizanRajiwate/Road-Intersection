import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class VehicleRNG extends Thread {
		
		private Helper helper;
		private LinkedList<Phases> phaseList;
		private Random random;

		public VehicleRNG(Helper helper, LinkedList<Phases> phaseList){
			this.helper = helper;
			this.phaseList = phaseList;
			this.random = new Random();
		}
		
		private void rngVehicleCreator(Helper helper) throws PhaseException {
			ArrayList<String> vehicleDetails = new ArrayList<String>();
			vehicleDetails = helper.randomlyGenerateVehicles();
			Vehicles car = helper.createVehicle(vehicleDetails, phaseList);
			boolean sortedPhase = helper.findPhase(car, phaseList);
			if (sortedPhase) {
				System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
			}else {
				throw new PhaseException(car.getPlateNumber() + " could not be sorted, check the segment and direction for format errors. " + car.getSegment() + ", " + car.getCrossingDirection());
			}
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					rngVehicleCreator(helper);
					
					Thread.sleep(random.nextInt(10000) + 2000);
				}catch(PhaseException e) {
					System.out.println(e);
				}catch(InterruptedException a) {
					a.printStackTrace();
				}
			}
		}
		
		
}
