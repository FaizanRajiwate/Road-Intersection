import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class JunctionController extends Thread{
	private Phases phase;
	private LinkedList<Vehicles> queuedVehicles;
	private float phaseDuration;
	private float waitTime = 0;
	private float totalEmissions = 0;	
	private LinkedList<Vehicles> crossedVehicles;
	private TrafficController controller;
	private boolean crossingStructureStatus;
	private GUIModel model;
	private DefaultTableModel vModel;
	private volatile LinkedList<String> createdVehicles;
	private ReportFile file = ReportFile.getInstance();
	
	
	public JunctionController(Phases phase, Helper helper,  GUIModel model) {
		this.phase = phase;
		this.queuedVehicles = phase.getLinkedList();
		this.phaseDuration = phase.getPhaseTimer();
		this.crossedVehicles = phase.getCrossedLinkedList();
		this.controller = phase.getTrafficController();
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
	
	private synchronized void checkPhase() throws InterruptedException {
		file.writeToFile("Entered " + phase.getPhaseName() + ". Advanced Traffic Light to " + controller.getTrafficState().name());
		if (controller.isGreen()) {
			while (phaseDuration > 0) {
				try {
//					System.out.println("This is working");
					Vehicles currCar = queuedVehicles.get(0); //get the first car in queue
					float currCarTime = currCar.getCrossingTime(); // check the crossingTime of the car
					if (phase.getWaitingLength() == 0f) {
						//check if the phase has a waiting length of 0 if so then this is the first car in the queue otherwise set the vehicle length accordingly
						currCar.setQueuedDistance(0);
					}else {
						currCar.setQueuedDistance(phase.getWaitingLength());
					}
					System.out.println("Checking for index of vehicle");
					createdVehicles = model.getVehicleList();
					int index = createdVehicles.indexOf(currCar.getPlateNumber()); 
					while (index == -1) {
						wait(10000); //wait some time for other threads to adapt the situation then try again;
						createdVehicles = model.getVehicleList();
					}
					if (phaseDuration >= currCarTime) {
						float carEmissions = currCar.calculateEmissions(waitTime);
						waitTime += currCar.getCrossingTime();
						phase.updateWaitingLength(currCar.getVehicleLength());
						this.totalEmissions += carEmissions;
						vehicleCrossing(currCar, index);
						completeCrossing();
						//calculate emissions
						phaseDuration -= currCarTime;
						if (phaseDuration < 5f && controller.isGreen()) {
							advanceTrafficState();
							System.out.println(controller.getTrafficState());
						}
					}else {
						file.writeToFile(currCar.getPlateNumber() + " cannot cross due to inadequate time");
						break;
					}
				}catch (IndexOutOfBoundsException e){
					file.writeToFile(phase.getName() + " was empty. Switching to Next Phase");
					if (controller.isGreen()) {
						advanceTrafficState();
						System.out.println(controller.getTrafficState());
					}
					break;
				}			
			}
			if (controller.isGreen()) {
				advanceTrafficState();
				advanceTrafficState();
				System.out.println(controller.getTrafficState());
			}
			else if (controller.isAmber()) {
				advanceTrafficState();
				System.out.println(controller.getTrafficState());
			}
	
			
			
			
		}
		
		
	}
	
	private synchronized void advanceTrafficState() {
		new Thread(() -> {
			controller.advanceState();
		}).start();
	}
	
	
	private synchronized void vehicleCrossing(Vehicles vehicle, int index) throws InterruptedException {
		
        while (crossingStructureStatus) {
            wait();
        }
        crossingStructureStatus = true;        
		vehicle.start();
		vehicle.join();
		queuedVehicles.remove(vehicle);
		crossedVehicles.add(vehicle);
		file.writeToFile(vehicle.getPlateNumber() + " has crossed the intersection");
		waitTime += vehicle.getCrossingTime();
		phase.updateWaitingLength(vehicle.getVehicleLength());
//		this.updateMovingSegmentTable(vehicle);
		this.model.updateTableModel(vModel, index, 4, vehicle.getCrossingStatus());
		System.out.println(vehicle.getPlateNumber() + " has crossed");
		Thread.sleep((long) (vehicle.getCrossingTime() * 1000));
	}
	
	private synchronized void completeCrossing() {
        crossingStructureStatus = false;
        notifyAll();
	}
	
//	private synchronized void updateMovingSegmentTable(Vehicles vehicle) {
//		String segment = vehicle.getSegment();
//		if (segment.equals("1")) {
//			model.addToS1counter(-1);
//			model.addToS1WaitingLength(-1 * vehicle.getVehicleLength());
//			model.addToS1WaitingTime(-1 * vehicle.getCrossingTime());
//			model.updateTableModel(model.getStatsModel(),0,1, Integer.toString(model.getS1counter()));
//			model.updateTableModel(model.getStatsModel(),0,2, Float.toString(model.getS1WaitingTime()));
//			model.updateTableModel(model.getStatsModel(),0,3, Float.toString(model.getS1WaitingLength()));
//		}
//		if (segment.equals("2")) {
//			model.addToS2counter(-1);
//			model.addToS2WaitingLength(-1* vehicle.getVehicleLength());
//			model.addToS2WaitingTime(-1 * vehicle.getCrossingTime());
//			model.updateTableModel(model.getStatsModel(),1,1, Integer.toString(model.getS2counter()));
//			model.updateTableModel(model.getStatsModel(),1,2, Float.toString(model.getS2WaitingTime()));
//			model.updateTableModel(model.getStatsModel(),1,3, Float.toString(model.getS2WaitingLength()));
//		}
//		if (segment.equals("3")) {
//			model.addToS3counter(-1);
//			model.addToS3WaitingLength(-1 * vehicle.getVehicleLength());
//			model.addToS3WaitingTime(-1 * vehicle.getCrossingTime());
//			model.updateTableModel(model.getStatsModel(),2,1, Integer.toString(model.getS3counter()));
//			model.updateTableModel(model.getStatsModel(),2,2, Float.toString(model.getS3WaitingTime()));
//			model.updateTableModel(model.getStatsModel(),2,3, Float.toString(model.getS3WaitingLength()));
//		}
//		if (segment.equals("4")) {
//			model.addToS4counter(-1);
//			model.addToS4WaitingLength(-1 * vehicle.getVehicleLength());
//			model.addToS4WaitingTime(-1 *vehicle.getCrossingTime());
//			model.updateTableModel(model.getStatsModel(),3,1, Integer.toString(model.getS4counter()));
//			model.updateTableModel(model.getStatsModel(),3,2, Float.toString(model.getS4WaitingTime()));
//			model.updateTableModel(model.getStatsModel(),3,3, Float.toString(model.getS4WaitingLength()));
//		}
//	}
	
	
}
