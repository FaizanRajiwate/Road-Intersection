
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class JunctionController extends Thread {
	
	//This class controls the intersection

	private float waitTime = 0;
	private LinkedList<Phases> phaseList;
	private boolean crossingStructureStatus;
	private GUIModel model;
	private DefaultTableModel vModel;
	private volatile LinkedList<String> createdVehicles;
	private ReportFile file = ReportFile.getInstance();

	public JunctionController(LinkedList<Phases> phaseList, Helper helper, GUIModel model) {
		this.phaseList = phaseList;
		this.crossingStructureStatus = false;
		this.model = model;
		vModel = model.getVehicleModel();
		this.createdVehicles = model.getVehicleList();
	}

	@Override
	public void run() {
		try {
			checkPhase();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkPhase() throws InterruptedException {
		//Executes the Simulation. 
		//iterates through all phases, advancing the traffic light and verifying that it is green.
		//If green enters the phase, takes the first car, compares it's crossing time to the phase duration
		//if the phase duration is less than the car's crossing time, 
		//the crossing structure flag is occupied, the vehicle is started
		//the vehicle is removed from the list of vehicles waiting to cross and added to the crossed vehicles.
		//the next car is checked.
		//For optimization purposes, if the phase is empty the phase is switched to the next, 
		//the traffic light is advanced to red. 
		while (true) {
			for (Phases phase : phaseList) {
				TrafficController controller = phase.getTrafficController();//get the traffic controller object of the phases
				LinkedList<Vehicles> queuedVehicles = phase.getLinkedList();//get the list of vehicles waiting to cross
				LinkedList<Vehicles> crossedVehicles = phase.getCrossedLinkedList();//get the list of crossed vehicles
				float phaseDuration = phase.getPhaseTimer(); //get the duration the phase runs for
				Thread initialState = advanceTrafficState(controller); //create a thread object to advance the traffic light.
				initialState.start(); 
				initialState.join(); //join the thread, so the junction controller thread waits for the light to be advanced
				file.writeToFile("Entered " + phase.getPhaseName() + ". Advanced Traffic Light to "
						+ controller.getTrafficState().name()); //log state change
				if (controller.isGreen()) {
					while (phaseDuration > 0) {
						try {
							Vehicles currCar = queuedVehicles.get(0); // get the first car in queue
							float currCarTime = currCar.getCrossingTime(); // check the crossingTime of the car
							if (phase.getWaitingLength() == 0f) {
								// check if the phase has a waiting length of 0 if so then this is the first car
								// in the queue otherwise set the vehicle length accordingly
								currCar.setQueuedDistance(0);
								waitTime += 0; 
							} else {
								currCar.setQueuedDistance(phase.getWaitingLength());
								waitTime += currCar.getCrossingTime();
							}

							createdVehicles = model.getVehicleList(); //get the updated vehicle list to check for new vehicles.
							int index = createdVehicles.indexOf(currCar.getPlateNumber()); //check for index of car, as this will be it's position on the table
							if (phaseDuration >= currCarTime) {
								float carEmissions = currCar.calculateEmissions(waitTime); //calculate car's emissions
								model.addRunningEmissions(carEmissions); //accumulate the emissions
								vehicleCrossing(phase, currCar, index, queuedVehicles, crossedVehicles);
								completeCrossing();
								// calculate emissions
								phaseDuration -= currCarTime;
								if (phaseDuration < 5f && controller.isGreen()) {
									Thread ctrl = advanceTrafficState(controller);
									ctrl.start();
									ctrl.join();
									file.writeToFile(phase.getPhaseName() + " had less than 5 seconds; advanced to "
											+ controller.getTrafficState().name());
								}
							} else {
								file.writeToFile(currCar.getPlateNumber() + " cannot cross due to inadequate time ");
								if (controller.isGreen()) {
									Thread ctrl = advanceTrafficState(controller);
									ctrl.start();
									ctrl.join();
								}
								file.writeToFile(phase.getPhaseName() + " was advanced to"
										+ controller.getTrafficState().name());
								break;
							}
						} catch (IndexOutOfBoundsException e) {
							file.writeToFile(phase.getPhaseName() + " was empty. Switching to Next Phase");
							if (controller.isGreen()) {
								Thread ctrl = advanceTrafficState(controller);
								ctrl.start();
								ctrl.join();
								file.writeToFile(phase.getPhaseName() + " was advanced to "
										+ controller.getTrafficState().name());
							}
							break;
						}
					}
					if (controller.isGreen()) {
						Thread ctrl = advanceTrafficState(controller);
						ctrl.start();
						ctrl.join();
						file.writeToFile(
								phase.getPhaseName() + " was advanced to " + controller.getTrafficState().name());
						Thread ctrl2 = advanceTrafficState(controller);
						ctrl2.start();
						ctrl2.join();
						file.writeToFile(
								phase.getPhaseName() + " was advanced to " + controller.getTrafficState().name());
					} else if (controller.isAmber()) {
						Thread ctrl = advanceTrafficState(controller);
						ctrl.start();
						ctrl.join();
						file.writeToFile(
								phase.getPhaseName() + " was advanced to " + controller.getTrafficState().name());
					}
				}
			}
		}
	}

	private synchronized Thread advanceTrafficState(TrafficController controller) {
		return new Thread(() -> {
			controller.advanceState(); //change Traffic Light
		});
	}

	private synchronized void vehicleCrossing(Phases phase, Vehicles vehicle, int index,
			LinkedList<Vehicles> queuedVehicles, LinkedList<Vehicles> crossedVehicles) throws InterruptedException {
		//if the crossing structure is occupied wait()
		while (crossingStructureStatus) {
			wait();
		}
		// if not occupied, change occupied status
		crossingStructureStatus = true;
		vehicle.start(); //change vehicles status to crossed
		vehicle.join(); //wait for change to take place
		queuedVehicles.remove(vehicle); //remove vehicle from the list of queued vehicles
		crossedVehicles.add(vehicle); //add to the list of crossed vehicles
		file.writeToFile(vehicle.getPlateNumber() + " has crossed the intersection. " + "Distance: " + phase.getWaitingLength());
		waitTime += vehicle.getCrossingTime(); //update total waiting time
		phase.updateWaitingLength(vehicle.getVehicleLength());
		phase.updateWaitingTime(vehicle.getCrossingTime());
		this.updateMovingSegmentTable(vehicle); //change GUI Segment Table to crossed
		this.model.updateTableModel(vModel, index, 4, vehicle.getCrossingStatus()); //Update the GUI Vehicle Model
		Thread.sleep((long) (vehicle.getCrossingTime() * 1000)); //sleep to simulate vehicle crossing intersection
	}

	private synchronized void completeCrossing() {
		crossingStructureStatus = false; //empty crossing structure
		file.writeToFile("Crossing Structure is Empty");
		notifyAll();
	}

	private synchronized void updateMovingSegmentTable(Vehicles vehicle) {
		//Update segment table, with new statistics
		String segment = vehicle.getSegment();
		if (segment.equals("1")) {
			model.addToS1counter(-1); //reduce cars waiting at the intersection every time one crosses
			model.addToS1WaitingLength(-1 * vehicle.getVehicleLength()); //subtract the car's length from waiting length for the segment
			model.addToS1WaitingTime(-1 * vehicle.getCrossingTime()); //subtract the car's waiting time from time at segment.
			model.updateTableModel(model.getStatsModel(), 0, 1, Integer.toString(model.getS1counter())); //updates segment table at specified row and column
			model.updateTableModel(model.getStatsModel(), 0, 2, Float.toString(model.getS1WaitingTime()));
			model.updateTableModel(model.getStatsModel(), 0, 3, Float.toString(model.getS1WaitingLength()));
		}
		if (segment.equals("2")) {
			model.addToS2counter(-1);
			model.addToS2WaitingLength(-1 * vehicle.getVehicleLength());
			model.addToS2WaitingTime(-1 * vehicle.getCrossingTime());
			model.updateTableModel(model.getStatsModel(), 1, 1, Integer.toString(model.getS2counter()));
			model.updateTableModel(model.getStatsModel(), 1, 2, Float.toString(model.getS2WaitingTime()));
			model.updateTableModel(model.getStatsModel(), 1, 3, Float.toString(model.getS2WaitingLength()));
		}
		if (segment.equals("3")) {
			model.addToS3counter(-1);
			model.addToS3WaitingLength(-1 * vehicle.getVehicleLength());
			model.addToS3WaitingTime(-1 * vehicle.getCrossingTime());
			model.updateTableModel(model.getStatsModel(), 2, 1, Integer.toString(model.getS3counter()));
			model.updateTableModel(model.getStatsModel(), 2, 2, Float.toString(model.getS3WaitingTime()));
			model.updateTableModel(model.getStatsModel(), 2, 3, Float.toString(model.getS3WaitingLength()));
		}
		if (segment.equals("4")) {
			model.addToS4counter(-1);
			model.addToS4WaitingLength(-1 * vehicle.getVehicleLength());
			model.addToS4WaitingTime(-1 * vehicle.getCrossingTime());
			model.updateTableModel(model.getStatsModel(), 3, 1, Integer.toString(model.getS4counter()));
			model.updateTableModel(model.getStatsModel(), 3, 2, Float.toString(model.getS4WaitingTime()));
			model.updateTableModel(model.getStatsModel(), 3, 3, Float.toString(model.getS4WaitingLength()));
		}
	}

}
