import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GUIController  {
	// list of observers
	private List<Observer> registeredObservers = new LinkedList<Observer>();
	private ReportFile file;
	private GUIModel model;
	private GUIView view;
	private Helper helper;
	private float totalEmissions; 
	String[] phaseSegment = {"3","1","4","2","1","3","2","4"};
	private LinkedList<Phases> phaseList;

	
	//GUI Elements 
	private JPanel tablesPanel;
	//Vehicle Elements
	private JScrollPane vehiclePane;
	private DefaultTableModel vehicleModel;
	private JTable vehicleTable;
	
	//Phase Elements
	private JScrollPane phasePane;
	private DefaultTableModel phaseModel;
	private JTable phaseTable;
	
	//Segment Statistics Elements
	private JScrollPane statsPane;
	private DefaultTableModel statsModel;
	private JTable statsTable;
	
	//Form Fields
	private JTextField emissionField;
	private JTextField pNField;
	private JComboBox<String> vTField;
	private JTextField cTField;
	private JComboBox<String> cDField;
	private JTextField cSField;
	private JTextField vEField;
	private JTextField vLField;
	private JComboBox<String> sField;
	
	
	public GUIController(GUIModel _model, GUIView _view, Helper _helper) {
		this.model = _model;
		this.view = _view;
		this.helper = _helper;
		this.phaseList = helper.readPhasesFile("phases.csv");
		 //Segment Table Variables
		totalEmissions = model.getTotalEmissions(); 
		this.file = ReportFile.getInstance();
		//GUI Elements;
		tablesPanel = view.getTablesPanel();
		if(tablesPanel != null) {Main.blnDoWork = true;}else{Main.blnDoWork = false;};
		tablesPanel.add(addVehiclePane(phaseList, helper, model, view));
		Main.blnDoWork = false;
		tablesPanel.add(addPhasesPane(phaseList, helper, model, view));
		tablesPanel.add(addStatsPane(phaseList, helper, model, view));
		
		//Getting Form Fields
		emissionField = view.getEmissionField();
		pNField = view.getpNField();
		vTField = view.getvTField();
		cTField = view.getcTField();
		cDField = view.getcDField();
		cSField = view.getcSField();
		vEField = view.getvEField();
		vLField = view.getvLField();
		sField = view.getsField();
		
		view.setEmissionField(Float.toString(totalEmissions));
		view.addVehicleButtonListener(new AddVehicleListener());
		view.startButtonListener(new StartButtonListener(model));
		
			
	}
	
	class AddVehicleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			new Thread() { 
				public void run() {
				// time-consuming code to run here
					addVehicles();
					// Update the user interface components here
					SwingUtilities.invokeLater(new Runnable() 
					{ 
						public void run() {
					        
					                                          }
					});
					
				}
				}.start();
			// TODO Auto-generated method stub
			        }		 
	}
	
	class StartButtonListener implements ActionListener {
			
		private GUIModel model;
				
		public StartButtonListener(GUIModel model) {
			this.model = model;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			new Thread() { 
				public void run() {
				// time-consuming code to run here
					executeSimulation();
					// Update the user interface components here
					SwingUtilities.invokeLater(new Runnable() 
					{ 
						public void run() {}
					});
				}
				}.start();		
				model.notifyObservers();
			}
	}		 
	
	
	public void executeSimulation() {
		VehicleRNG rngVehicle = new VehicleRNG(helper, phaseList, model);
		rngVehicle.start();
		while (true) {
			for (Phases phase: phaseList) {
				JunctionController controller = new JunctionController(phase, helper, model);
				controller.start();
				break;
				
			}
			break;
		}
	}
	
	public void addVehicles()
	{
				try {
	            	String pNEntry = pNField.getText();
	            	String vTEntry = vTField.getSelectedItem().toString();
	            	String cTEntry = cTField.getText();
	            	String cDEntry = cDField.getSelectedItem().toString();
	            	String cSEntry = cSField.getText();
	            	String vEEntry = vEField.getText();
	            	String vLEntry = vLField.getText();
	            	String sEntry = sField.getSelectedItem().toString();
	        		String[] newVehicle = {pNEntry, vTEntry, cTEntry, cDEntry, cSEntry, vEEntry, vLEntry, sEntry};
	            	List<String> newVehicleLine = Arrays.asList(newVehicle);
	            	helper.evaluateVehicleFile(newVehicleLine, phaseList);
	    			Vehicles car = helper.createVehicle(newVehicleLine, phaseList);
	    			if (car == null) {
	    				throw new InaccurateDataException("The row with " + newVehicleLine.get(0) + "could not be created");
	    			}           			

	    			totalEmissions += car.getVehicleEmission();
	    			emissionField.setText(Float.toString(totalEmissions));
	    			vehicleModel.addRow(newVehicle);            			           			
	    			pNField.setText("");
	            	cTField.setText("");
	            	vEField.setText("");
	            	vLField.setText("");
	            	boolean sortedPhase = helper.findPhase(car, phaseList);
					if (sortedPhase) {
						//System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
					}else {
						throw new PhaseException(car.getPlateNumber() + " could not be sorted, check the segment and direction for format errors. " + car.getSegment() + ", " + car.getCrossingDirection());
					}
					helper.checkCarSegment(car, model);
					String segment = car.getSegment();
					
					helper.updateSegmentTable(segment, model);
					file.writeToFile(car.getPlateNumber() + "has been accepted and added to the Queue");
	        	}catch(InaccurateDataException ex) {
	        		JFrame alert = new JFrame();
					JOptionPane.showMessageDialog(alert, ex);
	        	}catch(NumberFormatException ex) {
	        		JFrame alert = new JFrame();
					JOptionPane.showMessageDialog(alert, ex);
	        	}catch(PhaseException ex) { 
	        		JFrame alert = new JFrame();
					JOptionPane.showMessageDialog(alert, ex);
	        	}catch(DuplicateIDException ex) {
	        		JFrame alert = new JFrame();
					JOptionPane.showMessageDialog(alert, ex);
	        	}
			this.model.notifyObservers();
	}

	private JScrollPane addVehiclePane(LinkedList<Phases> phaseList, Helper helper, GUIModel model, GUIView view) {
		
		while(!Main.blnDoWork) { 
			try { wait(); }
			catch (InterruptedException e) {}
			}

		vehiclePane = view.getVehiclePane();
		vehicleModel = model.getVehicleModel();
		vehicleTable = view.getvehicleTable();
		synchronized (this) {
			
			try { 
				Scanner csvScanner = helper.readCsvFile("vehicles.csv");
				if (csvScanner == null) {
					throw new FileNotFoundException("The File you entered cannot be found");
				}else {
				
					while (csvScanner.hasNext()) {
						try {
						
							String line = csvScanner.nextLine();
							String[] splitLine = line.split(",");
							List<String> listSplitLine = Arrays.asList(splitLine);
							helper.evaluateVehicleFile(listSplitLine, phaseList);
							//populate VehicleJTable
							Vehicles car = helper.createVehicle(listSplitLine, phaseList);
							
							if (car == null) {
								throw new InaccurateDataException("The row with " + listSplitLine.get(0) + "could not be created");
							}
							file.writeToFile(car.getPlateNumber() + " has been read and added to the Queue");
							vehicleModel.addRow(splitLine);	
							totalEmissions += car.getVehicleEmission();
							boolean sortedPhase = helper.findPhase(car, phaseList);
							if (sortedPhase) {
								model.addNewVehicle(car.getPlateNumber());
								//System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
							}else {
								throw new PhaseException(car.getPlateNumber() + " could not be sorted, check the segment and direction for format errors. " + car.getSegment() + ", " + car.getCrossingDirection());
							}
						}catch (PhaseException e) {
							JFrame alert = new JFrame();
							JOptionPane.showMessageDialog(alert, e);
							continue;
						}catch (InaccurateDataException e) {
							e.printStackTrace();
							JFrame alert = new JFrame();
							JOptionPane.showMessageDialog(alert, e);
							continue;
						}catch(DuplicateIDException e){
							JFrame alert = new JFrame();
							JOptionPane.showMessageDialog(alert, e);
							continue;
						}catch(NumberFormatException e) {
							JFrame alert = new JFrame();
							JOptionPane.showMessageDialog(alert, e);
							continue;
						}
						
					}
				}		     
			}
			 catch (FileNotFoundException e) {
				System.out.println(e);
			}
			
			vehicleTable.setAutoCreateRowSorter(true);
			vehicleTable.setModel(vehicleModel);
			vehiclePane.getViewport().add(view.getvehicleTable()); 
			return vehiclePane;
		}
		
	}
	
	private JScrollPane addPhasesPane(LinkedList<Phases> phaseList, Helper helper, GUIModel model, GUIView view) {
		phasePane = view.getPhasePane();
		phaseModel = model.getPhaseModel();
		phaseTable = view.getphaseTable();
		synchronized (this) 
		{
			for (Phases phase: phaseList) {
				Vector<String> rowData = new Vector<String>();
				rowData.add(phase.getPhaseName());
				rowData.add(Float.toString(phase.getPhaseTimer()));
				phaseModel.addRow(rowData);
			}
			
			phaseTable.setModel(phaseModel);
			phasePane.getViewport().add(phaseTable);
			file.writeToFile("Lanes have been read and created");
			return phasePane;
		}
				
	}
		
	private JScrollPane addStatsPane(LinkedList<Phases> phaseList, Helper helper, GUIModel model, GUIView view) {
		statsPane = view.getStatsPane();
		statsModel = model.getStatsModel();
		statsTable = view.getstatsTable();
		synchronized (this) 
		{	
			for (int i = 0; i < 8; i++) {
				Phases phase = phaseList.get(i);
				String segment = phaseSegment[i];
				if (segment.equals("1")) {
					model.addToS1CrossTime(phase.getPhaseTimer());
				}
				if (segment.equals("2")) {
					model.addToS2CrossTime(phase.getPhaseTimer());
				}
				if (segment.equals("3")) {
					model.addToS3CrossTime(phase.getPhaseTimer());
				}
				if (segment.equals("4")) {
					model.addToS4CrossTime(phase.getPhaseTimer());
				}

				LinkedList<Vehicles> carsQueue = phase.getLinkedList();
				for (Vehicles car : carsQueue) {
					helper.checkCarSegment(car,model);
				}

			}
			for (int i = 1; i < 5; i ++) {
				ArrayList<String> rowData = new ArrayList<String>();
				String segment = Integer.toString(i);
				rowData.add(segment);
				if (i == 1) {
					String carsAtSegment = Integer.toString(model.getS1counter());
					String waitingTime = Float.toString(model.getS1WaitingTime());
					String waitingLength = Float.toString(model.getS1WaitingLength());
					String avgCrossSegment = Float.toString(model.getS1CrossTime() / 2f);
					rowData.add(carsAtSegment);
					rowData.add(waitingTime);
					rowData.add(waitingLength);
					rowData.add(avgCrossSegment);
				}
				if (i == 2) {
					String carsAtSegment = Integer.toString(model.getS2counter());
					String waitingTime = Float.toString(model.getS2WaitingTime());
					String waitingLength = Float.toString(model.getS2WaitingLength());
					String avgCrossSegment = Float.toString(model.getS2CrossTime() / 2f);
					rowData.add(carsAtSegment);
					rowData.add(waitingTime);
					rowData.add(waitingLength);
					rowData.add(avgCrossSegment);
				}
				if (i == 3) {
					String carsAtSegment = Integer.toString(model.getS3counter());
					String waitingTime = Float.toString(model.getS3WaitingTime());
					String waitingLength = Float.toString(model.getS3WaitingLength());
					String avgCrossSegment = Float.toString(model.getS3CrossTime() / 2f);
					rowData.add(carsAtSegment);
					rowData.add(waitingTime);
					rowData.add(waitingLength);
					rowData.add(avgCrossSegment);
				}
				if (i == 4) {
					String carsAtSegment = Integer.toString(model.getS4counter());
					String waitingTime = Float.toString(model.getS4WaitingTime());
					String waitingLength = Float.toString(model.getS4WaitingLength());
					String avgCrossSegment = Float.toString(model.getS4CrossTime() / 2f);
					rowData.add(carsAtSegment);
					rowData.add(waitingTime);
					rowData.add(waitingLength);
					rowData.add(avgCrossSegment);
				}
				model.updateModel(statsModel,rowData.toArray());
						
			}
			statsTable.setModel(statsModel);
			statsPane.getViewport().add(statsTable);
			file.writeToFile("Initial Segments calculated Statistics.");
			return statsPane;		
		}
		
	}
	



	

}