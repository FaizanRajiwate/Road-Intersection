import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.table.DefaultTableModel;

public class VehicleRNG extends Thread{
		
		private Helper helper;
		private LinkedList<Phases> phaseList;
		private Random random;
		
		private GUIModel model;
		private DefaultTableModel vModel;

		private int s1counter;
		private float s1WaitingTime;
		private float s1CrossTime;
		private float s1WaitingLength;
		private int s2counter;
		private float s2WaitingTime;
		private float s2CrossTime;
		private float s2WaitingLength;
		private int s3counter;
		private float s3WaitingTime;
		private float s3CrossTime;
		private float s3WaitingLength;
		private int s4counter;
		private float s4WaitingTime;
		private float s4CrossTime;
		private float s4WaitingLength;
		private float totalEmissions; 
		public VehicleRNG(Helper helper, LinkedList<Phases> phaseList, GUIModel model){
			this.helper = helper;
			this.phaseList = phaseList;
			this.random = new Random();
			this.model = model;
			this.vModel = model.getVehicleModel();
			
			s1counter = model.getS1counter();
			s1WaitingTime = model.getS1WaitingTime();
			s1CrossTime = model.getS1CrossTime();
			s1WaitingLength = model.getS1WaitingLength();
			s2counter = model.getS2counter();
			s2WaitingTime = model.getS2WaitingTime();
			s2CrossTime = model.getS2CrossTime();
			s2WaitingLength = model.getS2WaitingLength();
			s3counter = model.getS3counter();
			s3WaitingTime = model.getS3WaitingTime();
			s3CrossTime = model.getS3CrossTime();
			s3WaitingLength = model.getS3WaitingLength();
			s4counter = model.getS4counter();
			s4WaitingTime = model.getS4WaitingTime();
			s4CrossTime = model.getS4CrossTime();
			s4WaitingLength = model.getS4WaitingLength();
			totalEmissions = model.getTotalEmissions(); 
			
		}
		
		private void rngVehicleCreator(Helper helper) throws PhaseException, NumberFormatException, InaccurateDataException, DuplicateIDException {
			ArrayList<String> vehicleDetails = new ArrayList<String>();
			vehicleDetails = helper.randomlyGenerateVehicles();
			helper.evaluateVehicleFile(vehicleDetails, phaseList);
			Vehicles car = helper.createVehicle(vehicleDetails, phaseList);
			boolean sortedPhase = helper.findPhase(car, phaseList);
			if (sortedPhase) {
				System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
				model.addNewVehicle(car.getPlateNumber());
			}else {
				throw new PhaseException(car.getPlateNumber() + " could not be sorted, check the segment and direction for format errors. " + car.getSegment() + ", " + car.getCrossingDirection());
			}
			helper.checkCarSegment(car, model);
			helper.updateSegmentTable(car.getSegment(), model);
			model.updateModel(vModel, vehicleDetails.toArray());
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					rngVehicleCreator(helper);
					try {
		                Thread.sleep(random.nextInt(3000) + 1000); // Generate a new vehicle every 1-6 seconds
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
				} catch (NumberFormatException | PhaseException | InaccurateDataException | DuplicateIDException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
		
		
			
		
}
