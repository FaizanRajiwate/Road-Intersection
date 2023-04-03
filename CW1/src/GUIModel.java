import javax.swing.table.DefaultTableModel;

public class GUIModel extends Thread {

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

private String[] vehicleColNames = {
		"plate number",	
		"type",	
		"crossing time (s)",
		"direction","crossing status",	
		"emission rate (g/min)", 
		"length (m)", 
		"segment"};


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
	
	

}
