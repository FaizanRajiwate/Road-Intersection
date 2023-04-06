import java.util.LinkedList;

public class JunctionController extends Thread{
	private Phases phase;
	private LinkedList<Vehicles> queuedVehicles;
	private float phaseDuration;
	private float waitTime = 0;
	private float totalEmissions = 0;	
	LinkedList<Vehicles> crossedVehicles;
	TrafficController controller;
	
	
	public JunctionController(Phases phase) {
		this.phase = phase;
		this.queuedVehicles = phase.getLinkedList();
		this.phaseDuration = phase.getPhaseTimer();
		this.crossedVehicles = phase.getCrossedLinkedList();
		this.controller = phase.getTrafficController();
//		this.checkPhase(queuedVehicles, crossedVehicles, phaseDuration);
	}
	
	@Override
	public void run() {
		
	}

	
	private synchronized void checkPhase() {
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
					
					if (phaseDuration >= currCarTime) {
						//currCar.start()						
						crossedVehicles.add(currCar);
						System.out.println(currCar.getPlateNumber() + "has crossed from " + phase.getPhaseName());
						queuedVehicles.remove(currCar);
						//crossing = false
						//crossingStructureOccupied = false
//						phaseWaitTime += currCar.getCrossingTime();
						float carEmissions = currCar.calculateEmissions(waitTime);
						waitTime += currCar.getCrossingTime();
						phase.updateWaitingLength(currCar.getVehicleLength());
						this.totalEmissions += carEmissions;
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
					System.out.println(phase.getPhaseName() + " is currently empty");
					break;
				}
				
			
			}
			controller.advanceState();
		}
		
		
	}
	
	
	private void vehicleCrossing(Vehicles vehicle) {
		
	}
	
	private void completeCrossing() {
		
	}
	
	
	
}
