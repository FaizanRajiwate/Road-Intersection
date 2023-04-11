import java.util.LinkedList;

public class Phases extends Thread {
	private String phaseName;
	private float phaseTimer;
	private LinkedList<Vehicles> vehicleQueue;
	private LinkedList<Vehicles> crossedvehicleQueue;
	private TrafficController trafficController;
	private float waitingTime = 0f;
	private float waitingVehicleLength = 0f;

	public void run() { // code to be run as a thread
		System.out.println("Started....Phases");
	}

	public synchronized void setTrafficController(TrafficController controller) {
		this.trafficController = controller;
	}

	public void updateWaitingLength(float vehicleLength) {
		waitingVehicleLength += vehicleLength;
	}

	public float getWaitingLength() {
		return waitingVehicleLength;
	}

	public float getWaitingTime() {
		return this.waitingTime;
	}

	public void updateWaitingTime(float time) {
		waitingTime += time;
	}

	public TrafficController getTrafficController() {

		return this.trafficController;
	}

	public synchronized String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public float getPhaseTimer() {
		return phaseTimer;
	}

	public void setPhaseTimer(float phaseTimer) {
		this.phaseTimer = phaseTimer;
	}

	public void setLinkedList() {
		this.vehicleQueue = new LinkedList<Vehicles>();

	}

	public synchronized LinkedList<Vehicles> getLinkedList() {
		return vehicleQueue;
	}

	public synchronized void setCrossedLinkedList() {
		this.crossedvehicleQueue = new LinkedList<Vehicles>();

	}

	public synchronized LinkedList<Vehicles> getCrossedLinkedList() {
		return crossedvehicleQueue;
	}

	public synchronized void addToQueue(Vehicles vehicle) {
		this.vehicleQueue.add(vehicle);
	}

	public synchronized void addToCrossed(Vehicles vehicle) {
		this.crossedvehicleQueue.add(vehicle);
	}

	public synchronized void removeFromQueue(Vehicles vehicle) {
		this.vehicleQueue.remove(vehicle);
	}
}
