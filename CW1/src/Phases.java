import java.util.LinkedList;

public class Phases  extends Thread {
	private String phaseName;
	private float phaseTimer;
	private LinkedList<Vehicles> cars;
	private LinkedList<Vehicles> crossedCars;
	private TrafficController trafficController;
	
	public void run()
	{ // code to be run as a thread
		System.out.println("Started....Phases");	
	}
	
	public synchronized void setTrafficController(TrafficController controller) {
		this.trafficController = controller;
	}
	
	public TrafficController getTrafficController() {

		return this.trafficController;
	}
	
	
	public synchronized String getPhaseName() { 
		return phaseName; 
	}
	 
	public synchronized void setPhaseName (String phaseName) {
		this.phaseName = phaseName;
	}
	
	public synchronized float getPhaseTimer() { 
		return phaseTimer; 
	}
	 
	public synchronized void setPhaseTimer (float phaseTimer) {
		this.phaseTimer = phaseTimer;
	}
	
	public synchronized void setLinkedList() {
		this.cars = new LinkedList<Vehicles>();
		
	}
	
	public synchronized LinkedList<Vehicles> getLinkedList(){
		return cars;
	}
	
	public synchronized void setCrossedLinkedList() {
		this.crossedCars = new LinkedList<Vehicles>();
		
	}
	
	public synchronized LinkedList<Vehicles> getCrossedLinkedList(){
		return crossedCars;
	}
	
	
}
