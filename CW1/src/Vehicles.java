
public class Vehicles extends Thread {
	private String plateNumber;
	private String vehicleType;
	private float crossingTime;
	private String crossingDirection;
	private String crossingStatus;
	private float vehicleLength;
	private float vehicleEmission;
	private String segment;
	private float queuedDistance;
//	private float travelledDistance;
	
	public void run()
	{ // code to be run as a thread
		System.out.println("Started....Vehicle");
				
	}
	
	public String getPlateNumber() { 
		return plateNumber; 
	}
	 
	public synchronized void setPlateNumber (String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	public String getVehicleType() { 
		return vehicleType; 
	}
	
	public synchronized void setVehicleType (String vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	public float getCrossingTime() {
		return crossingTime;
	}
	
	public synchronized void setCrossingTime(float crossingTime) {
		this.crossingTime = crossingTime;
	}
	
	public String getCrossingDirection() { 
		return crossingDirection; 
	}
	
	public synchronized void setCrossingDirection (String crossingDirection) {
		this.crossingDirection = crossingDirection;
	}
	
	public String getCrossingStatus(){
		return crossingStatus;
	}
	
	public synchronized void setCrossingStatus(String crossingStatus) {
		this.crossingStatus = crossingStatus;
	}
	
	public float getVehicleLength() {
		return vehicleLength; 
	}
	
	public synchronized void setVehicleLength(float vehicleLength) {
		this.vehicleLength = vehicleLength;
	}
	
	public float getVehicleEmission() {
		return vehicleEmission;
	}
	
	public synchronized void setVehicleEmission(float vehicleEmission) {
		this.vehicleEmission = vehicleEmission;
	}
	
	public synchronized void setSegment(String segmentNumber) {
		this.segment = segmentNumber;
	}
	
	public void setQueuedDistance(float queuedDistance) {
		this.queuedDistance = queuedDistance;
	}
	
	public float getQueuedDistance() {
		return this.queuedDistance;
	}
		
	public String getSegment() {
		return segment;
	}
	
	public synchronized float calculateEmissions(float waitingTime) {
		/*
		while(true) { // when no number available
			try { wait(); } // consumer enters Waiting state
			catch (InterruptedException e) {}
			// it will stay here until notified
			}
			System.out.println("Got: " );
			empty = true; // consumer has consumed the number
			notifyAll(); // so wake up the producer
		*/
		return this.getVehicleEmission() * (waitingTime/60);
	}
	

}

