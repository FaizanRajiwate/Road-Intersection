
public class Vehicles {
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
	
	public String getPlateNumber() { 
		return plateNumber; 
	}
	 
	public void setPlateNumber (String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	public String getVehicleType() { 
		return vehicleType; 
	}
	
	public void setVehicleType (String vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	public float getCrossingTime() {
		return crossingTime;
	}
	
	public void setCrossingTime(float crossingTime) {
		this.crossingTime = crossingTime;
	}
	
	public String getCrossingDirection() { 
		return crossingDirection; 
	}
	
	public void setCrossingDirection (String crossingDirection) {
		this.crossingDirection = crossingDirection;
	}
	
	public String getCrossingStatus(){
		return crossingStatus;
	}
	
	public void setCrossingStatus(String crossingStatus) {
		this.crossingStatus = crossingStatus;
	}
	
	public float getVehicleLength() {
		return vehicleLength; 
	}
	
	public void setVehicleLength(float vehicleLength) {
		this.vehicleLength = vehicleLength;
	}
	
	public float getVehicleEmission() {
		return vehicleEmission;
	}
	
	public void setVehicleEmission(float vehicleEmission) {
		this.vehicleEmission = vehicleEmission;
	}
	
	public void setSegment(String segmentNumber) {
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
	
	public float calculateEmissions(float waitingTime) {
		return this.getVehicleEmission() * (waitingTime/60);
	}
	

}

