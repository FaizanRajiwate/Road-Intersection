import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class GUIModel extends Thread implements Subject {

	private List<Observer> registeredObservers = new LinkedList<Observer>();
	private String[] vehicleColNames = { "plate number", "type", "crossing time (s)", "direction", "crossing status",
			"emission rate (g/min)", "length (m)", "segment" };

	private String[] phaseColNames = { "Phase name", "Phase Timer" };

	private String[] segmentStatColNames = { "Segment", "No. of Vehicles Waiting", "Waiting Time", "Waiting Length",
			"Avg. Cross Time" };

	private DefaultTableModel vehicleModel;
	private DefaultTableModel phaseModel;
	private DefaultTableModel statsModel;
	private volatile LinkedList<String> createdVehicles;

	private int s1counter = 0;
	private float s1WaitingTime = 0;
	private float s1CrossTime = 0;
	private float s1WaitingLength = 0;
	private int s2counter = 0;
	private float s2WaitingTime = 0;
	private float s2CrossTime = 0;
	private float s2WaitingLength = 0;
	private int s3counter = 0;
	private float s3WaitingTime = 0;
	private float s3CrossTime = 0;
	private float s3WaitingLength = 0;
	private int s4counter = 0;
	private float s4WaitingTime = 0;
	private float s4CrossTime = 0;
	private float s4WaitingLength = 0;
	private float totalEmissions = 0;
	private float runningEmissions = 0;

	public GUIModel() {
		vehicleModel = createTableModel(vehicleColNames);
		phaseModel = createTableModel(phaseColNames);
		statsModel = createTableModel(segmentStatColNames);
		createdVehicles = new LinkedList<String>();
	}

	public void run() { // code to be run as a thread
		System.out.println("Started....");

	}

	public synchronized void addNewVehicle(String plateNumber) {
		this.createdVehicles.add(plateNumber);
	}

	public synchronized LinkedList<String> getVehicleList() {
		return createdVehicles;
	}

	public synchronized DefaultTableModel createTableModel(String[] columns) {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		return model;
	}

	public synchronized void updateModel(DefaultTableModel model, Object[] objects) {
		model.addRow(objects);
	}

	public DefaultTableModel getVehicleModel() {
		return vehicleModel;
	}

	public DefaultTableModel getPhaseModel() {
		return phaseModel;
	}

	public DefaultTableModel getStatsModel() {
		return statsModel;
	}

	@Override
	public void registerObserver(Observer observer) {

		registeredObservers.add(observer);
		System.out.println("View successfully registered to the model");
		System.out.println("Observer Count is" + registeredObservers.size());
	}

	@Override
	public void removeObserver(Observer observer) {
		registeredObservers.remove(observer);

	}

	@Override
	public void notifyObservers() {

		System.out.println("All observers are notified");
		System.out.println(registeredObservers.size());
		for (Observer obs : registeredObservers)
			obs.update();
	}

	public void updateTableModel(DefaultTableModel model, int row, int column, String crossingStatus) {
		model.setValueAt(crossingStatus, row, column);
	}

	public int getS1counter() {
		return s1counter;
	}

	public void addToS1counter(int s1counter) {
		this.s1counter += s1counter;
	}

	public float getS1WaitingTime() {
		return s1WaitingTime;
	}

	public void addToS1WaitingTime(float s1WaitingTime) {
		this.s1WaitingTime += s1WaitingTime;
	}

	public float getS1CrossTime() {
		return s1CrossTime;
	}

	public void addToS1CrossTime(float s1CrossTime) {
		this.s1CrossTime += s1CrossTime;
	}

	public float getS1WaitingLength() {
		return s1WaitingLength;
	}

	public void addToS1WaitingLength(float s1WaitingLength) {
		this.s1WaitingLength += s1WaitingLength;
	}

	public int getS2counter() {
		return s2counter;
	}

	public void addToS2counter(int s2counter) {
		this.s2counter += s2counter;
	}

	public float getS2WaitingTime() {
		return s2WaitingTime;
	}

	public void addToS2WaitingTime(float s2WaitingTime) {
		this.s2WaitingTime = s2WaitingTime;
	}

	public float getS2CrossTime() {
		return s2CrossTime;
	}

	public void addToS2CrossTime(float s2CrossTime) {
		this.s2CrossTime += s2CrossTime;
	}

	public float getS2WaitingLength() {
		return s2WaitingLength;
	}

	public void addToS2WaitingLength(float s2WaitingLength) {
		this.s2WaitingLength += s2WaitingLength;
	}

	public int getS3counter() {
		return s3counter;
	}

	public void addToS3counter(int s3counter) {
		this.s3counter += s3counter;
	}

	public float getS3WaitingTime() {
		return s3WaitingTime;
	}

	public void addToS3WaitingTime(float s3WaitingTime) {
		this.s3WaitingTime += s3WaitingTime;
	}

	public float getS3CrossTime() {
		return s3CrossTime;
	}

	public void addToS3CrossTime(float s3CrossTime) {
		this.s3CrossTime += s3CrossTime;
	}

	public float getS3WaitingLength() {
		return s3WaitingLength;
	}

	public void addToS3WaitingLength(float s3WaitingLength) {
		this.s3WaitingLength += s3WaitingLength;
	}

	public int getS4counter() {
		return s4counter;
	}

	public void addToS4counter(int s4counter) {
		this.s4counter += s4counter;
	}

	public float getS4WaitingTime() {
		return s4WaitingTime;
	}

	public void addToS4WaitingTime(float s4WaitingTime) {
		this.s4WaitingTime += s4WaitingTime;
	}

	public float getS4CrossTime() {
		return s4CrossTime;
	}

	public void addToS4CrossTime(float s4CrossTime) {
		this.s4CrossTime += s4CrossTime;
	}

	public float getS4WaitingLength() {
		return s4WaitingLength;
	}

	public void addToS4WaitingLength(float s4WaitingLength) {
		this.s4WaitingLength += s4WaitingLength;
	}

	public synchronized float getTotalEmissions() {
		return totalEmissions;
	}

	public synchronized void addToTotalEmissions(float totalEmissions) {
		this.totalEmissions += totalEmissions;
	}
	
	public synchronized float getRunningEmissions() {
		return runningEmissions;
	}

	public synchronized void addRunningEmissions(float totalEmissions) {
		this.runningEmissions += totalEmissions;
	}
	

}
