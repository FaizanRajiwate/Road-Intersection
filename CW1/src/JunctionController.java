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
	private Helper helper;
	private boolean crossingStructureStatus;
	private GUIModel model;
	private DefaultTableModel vModel;
	private volatile LinkedList<String> createdVehicles;
	
	
	public JunctionController(Phases phase, Helper helper,  GUIModel model) {
		this.phase = phase;
		this.queuedVehicles = phase.getLinkedList();
		this.phaseDuration = phase.getPhaseTimer();
		this.crossedVehicles = phase.getCrossedLinkedList();
		this.controller = phase.getTrafficController();
		this.helper = helper;
		
		this.crossingStructureStatus = false;
		this.model = model;
		vModel = model.getVehicleModel();
		this.createdVehicles = model.getVehicleList();
//		this.checkPhase(queuedVehicles, crossedVehicles, phaseDuration);
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
		this.controller.advanceState();
		if (controller.isGreen()) {
			System.out.println(controller.getTafficState());
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
						createdVehicles = model.getVehicleList();
						index = createdVehicles.indexOf(currCar.getPlateNumber());
						System.out.println(index);
					}
					
					if (phaseDuration >= currCarTime) {
//						crossedVehicles.add(currCar);					
//						
//						
//						queuedVehicles.remove(currCar);
//						//crossing = false
//						//crossingStructureOccupied = false
//						currCar.start();
//						System.out.println(currCar.getPlateNumber() + "has crossed from " + phase.getPhaseName());
////						phaseWaitTime += currCar.getCrossingTime();
//						float carEmissions = currCar.calculateEmissions(waitTime);
//						waitTime += currCar.getCrossingTime();
//						phase.updateWaitingLength(currCar.getVehicleLength());
//						this.totalEmissions += carEmissions;
						vehicleCrossing(currCar, index);
						completeCrossing();
						//calculate emissions
						phaseDuration -= currCarTime;
						if (phaseDuration < 5f && controller.isGreen()) {
							controller.advanceState();
						}
					}else {
						System.out.println(currCar.getPlateNumber() + " cannot cross due to inadequate time");
						break;
					}
				}catch (IndexOutOfBoundsException e){
					System.out.println("Vehicle did not exist in the list at time of search");
					controller.advanceState();
					System.out.println(controller.getTafficState());
					break;
				}			
			}
			controller.advanceState();
			System.out.println(controller.getTafficState());
			
			
		}
		
		
	}
	
	
	private synchronized void vehicleCrossing(Vehicles vehicle, int index) throws InterruptedException {
		
        while (crossingStructureStatus) {
            wait();
        }
        crossingStructureStatus = true;        
		System.out.println("I'm working");
		vehicle.start();
		vehicle.join();
		queuedVehicles.remove(vehicle);
		crossedVehicles.add(vehicle);
		waitTime += vehicle.getCrossingTime();
		phase.updateWaitingLength(vehicle.getVehicleLength());
		this.model.updateTableModel(vModel, index, 4, vehicle.getCrossingStatus());
		System.out.println(vehicle.getPlateNumber() + " has crossed");
		Thread.sleep((long) (vehicle.getCrossingTime() * 1000));
		
	}
	
	private synchronized void completeCrossing() {
        crossingStructureStatus = false;
        notifyAll();
	}
	
	
	
}
