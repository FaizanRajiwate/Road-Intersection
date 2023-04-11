import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.table.DefaultTableModel;
import exceptions.DuplicateIDException;
import exceptions.InaccurateDataException;
import exceptions.PhaseException;

public class VehicleRNG extends Thread {
	//Thread to randomly generate vehicles

	private Helper helper;
	private LinkedList<Phases> phaseList; //list of phases vehicles should go into. 
	private Random random; //random object to generate random numbers
	private GUIModel model; //receive GUI model to get DefaultTableModels
	private DefaultTableModel vModel;
	private ReportFile file = ReportFile.getInstance(); //singleton log class

	public VehicleRNG(Helper helper, LinkedList<Phases> phaseList, GUIModel model) {
		this.helper = helper;
		this.phaseList = phaseList;
		this.random = new Random();
		this.model = model;
		this.vModel = model.getVehicleModel();
	}

	private synchronized void rngVehicleCreator(Helper helper)
			throws PhaseException, NumberFormatException, InaccurateDataException, DuplicateIDException {
		//Function to create a new vehicle
		ArrayList<String> vehicleDetails = new ArrayList<String>();
		vehicleDetails = helper.randomlyGenerateVehicles();
		helper.evaluateVehicleFile(vehicleDetails, phaseList); //evaluates the vehicle details to confirm that the vehicle meets the rules
		Vehicles car = helper.createVehicle(vehicleDetails, phaseList); //creates the vehicle
		boolean sortedPhase = helper.findPhase(car, phaseList); //finds the phase of the vehicle and allocates it to that phase
		if (sortedPhase) {
			System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
			model.addNewVehicle(car.getPlateNumber()); //adds vehicle to list of created vehicles
		} else {
			throw new PhaseException(
					car.getPlateNumber() + " could not be sorted, check the segment and direction for format errors. "
							+ car.getSegment() + ", " + car.getCrossingDirection());
		}
		helper.checkCarSegment(car, model); //checks segment to increment necessary segment stats
		helper.updateSegmentTable(car.getSegment(), model); //update segment table with appropriate 
		file.writeToFile(car.getPlateNumber() + " has been created"); 
		model.updateModel(vModel, vehicleDetails.toArray()); //updates vehicle table
		model.addToTotalEmissions(car.getVehicleEmission()); //adds to the total emission rating
	}

	@Override
	public void run() {
		while (true) {
			try {
				rngVehicleCreator(helper);
				Thread.sleep(random.nextInt(5000) + 1000); // Generate a new vehicle every 1-6 seconds
			
			} catch (NumberFormatException | PhaseException | InaccurateDataException | DuplicateIDException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
