import java.util.LinkedList;

public class Phases extends Thread {
	private String phaseName;
	private float phaseTimer;
	private LinkedList<Vehicles> cars;
	private LinkedList<Vehicles> crossedCars;
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

	public synchronized void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public synchronized float getPhaseTimer() {
		return phaseTimer;
	}

	public synchronized void setPhaseTimer(float phaseTimer) {
		this.phaseTimer = phaseTimer;
	}

	public synchronized void setLinkedList() {
		this.cars = new LinkedList<Vehicles>();

	}

	public synchronized LinkedList<Vehicles> getLinkedList() {
		return cars;
	}

	public synchronized void setCrossedLinkedList() {
		this.crossedCars = new LinkedList<Vehicles>();

	}

	public synchronized LinkedList<Vehicles> getCrossedLinkedList() {
		return crossedCars;
	}

}
