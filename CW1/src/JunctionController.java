import java.util.LinkedList;

import javax.swing.table.DefaultTableModel;

public class JunctionController extends Thread{
	private Phases phase;
	private LinkedList<Vehicles> queuedVehicles;
	private float phaseDuration;
	private float waitTime = 0;
	private float totalEmissions = 0;	
	LinkedList<Vehicles> crossedVehicles;
	TrafficController controller;
	private Helper helper;
	private LinkedList<Phases> phaseList;
	private boolean crossingStructureStatus;
	private boolean crossing;
	private GUIModel model;
	private DefaultTableModel vModel;
	private LinkedList<String> createdVehicles;
	
	
	public JunctionController(Phases phase, Helper helper, LinkedList<Phases> phaseList, GUIModel model, LinkedList<String> createdVehicles) {
		this.phase = phase;
		this.queuedVehicles = phase.getLinkedList();
		this.phaseDuration = phase.getPhaseTimer();
		this.crossedVehicles = phase.getCrossedLinkedList();
		this.controller = phase.getTrafficController();
		this.helper = helper;
		this.phaseList = phaseList;
		this.crossingStructureStatus = false;
		this.crossing = false;
		this.model = model;
		vModel = model.getVehicleModel();
		this.createdVehicles = createdVehicles;
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
			while (phaseDuration > 0) {
				try {
//					System.out.println("This is working");
					
					Vehicles currCar = queuedVehicles.get(0); //get the first car in queue
					float currCarTime = currCar.getCrossingTime(); // check the crossingTime of the car
					if (phase.getWaitingLength() == 0f) {
						//check if the phase has a waiting length of 0 if so then this is the first car in the queue otherwise set the vehicle length accordingly
						currCar.setQueuedDistance(0);
					}
					currCar.setQueuedDistance(phase.getWaitingLength());
					int index = createdVehicles.indexOf(currCar.getPlateNumber()); 
					
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
						if (phaseDuration < 5f) {
							controller.advanceState();
						}
					}else {
						System.out.println(currCar.getPlateNumber() + " cannot cross due to inadequate time");
						break;
					}
				}catch (IndexOutOfBoundsException e){
					System.out.println("Vehicle does not exist in Created Vehiclesa");
					break;
				}
				
			
			}
			controller.advanceState();
		}
		
		
	}
	
	
	private void vehicleCrossing(Vehicles vehicle, int index) throws InterruptedException {
		synchronized (this) {
            while (crossingStructureStatus) {
                wait();
            }
            crossingStructureStatus = true;
            crossing = true;
        }
		vehicle.start();
		queuedVehicles.remove(vehicle);
		crossedVehicles.add(vehicle);
		waitTime += vehicle.getCrossingTime();
		phase.updateWaitingLength(vehicle.getVehicleLength());
		this.model.updateVehicleWaiting(vModel, index, 4, vehicle.getCrossingStatus());
	}
	
	private void completeCrossing() {
		synchronized (this) {
            crossing = false;
            crossingStructureStatus = false;
            notifyAll();
        }
	}
	
	
	
}
