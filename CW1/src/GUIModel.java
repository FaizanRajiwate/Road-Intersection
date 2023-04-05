import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class GUIModel extends Thread implements Subject{

	// list of observers
	private List<Observer> registeredObservers = new 
	LinkedList<Observer>();
	
	private String[] vehicleColNames = {
		"plate number",	
		"type",	
		"crossing time (s)",
		"direction","crossing status",	
		"emission rate (g/min)", 
		"length (m)", 
		"segment"};
	
	private String[] phaseColNames = {"Phase name",	
	"Phase Timer"};

	private String[] segmentStatColNames = {"Segment",	
	"No. of Vehicles Waiting", 
	"Waiting Time", 
	"Waiting Length", 
	"Avg. Cross Time"};

	private DefaultTableModel vehicleModel;
	private DefaultTableModel phaseModel;
	private DefaultTableModel statsModel;


	public GUIModel() {
		vehicleModel = createTableModel(vehicleColNames);
		phaseModel = createTableModel(phaseColNames);
		statsModel = createTableModel(segmentStatColNames);
		
	}
	

	public void run()
	{ // code to be run as a thread
		System.out.println("Started....");
				
	}

	public synchronized DefaultTableModel createTableModel(String[] columns) {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		
		return model;
		
	}
	
	public synchronized void updateModel(DefaultTableModel  model, String[] rowData) {
		model.addRow(rowData);
		
	}
	
	public DefaultTableModel getVehicleModel() {
		return vehicleModel;
	}
	
	public DefaultTableModel getPhaseModel(){
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
		for(Observer obs : registeredObservers) obs.update();
	}
	

}
