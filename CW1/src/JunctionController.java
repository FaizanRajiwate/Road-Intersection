import java.util.LinkedList;

public class JunctionController extends Thread{
	private Phases phase;
	private LinkedList<Vehicles> queuedVehicles;
	private float phaseDuration;
	private float waitTime = 0;
	private float totalEmissions = 0;	
	LinkedList<Vehicles> crossedVehicles;
	
	public JunctionController(Phases phase) {
		this.phase = phase;
		this.queuedVehicles = phase.getLinkedList();
		this.phaseDuration = phase.getPhaseTimer();
		this.crossedVehicles = phase.getCrossedLinkedList();
//		this.checkPhase(queuedVehicles, crossedVehicles, phaseDuration);
	}
	
	@Override
	public void run() {
		
	}

	
	private synchronized void checkPhase(LinkedList<Vehicles> queuedVehicles, LinkedList<Vehicles> crossedVehicles, float phaseDuration) {
		while (phaseDuration > 0) {
			try {
				System.out.println("This is working");
				Vehicles currCar = queuedVehicles.get(0);
				float currCarTime = currCar.getCrossingTime();
				this.waitTime += currCarTime;
				if (phaseDuration >= currCarTime) {
					crossedVehicles.add(currCar);
					System.out.println(currCar.getPlateNumber() + "has crossed from " + phase.getPhaseName());
					queuedVehicles.remove(currCar);
//					phaseWaitTime += currCar.getCrossingTime();
					float carEmissions = currCar.calculateEmissions(waitTime);
					this.totalEmissions += carEmissions;
					//calculate emissions
					phaseDuration -= currCarTime;
				}else {
					System.out.println(currCar.getPlateNumber() + " cannot cross due to inadequate time");
					break;
				}
			}catch (IndexOutOfBoundsException e){
				System.out.println(phase.getPhaseName() + " is currently empty");
				break;
			}
		
		}
		System.out.println("Phase Duration is not > 0");
	}
	
	
	
	
}
