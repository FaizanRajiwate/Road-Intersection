import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
import javax.swing.table.DefaultTableModel;

public class GUIController {
	private GUIModel model;
	private GUIView view;
	private Helper helper;
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
		
		//GUI Elements;
		tablesPanel = view.getTablesPanel();
		tablesPanel.add(addVehiclePane(phaseList, helper, model, view));
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
			
	}
	class AddVehicleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
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
//                    	cSField.setText(""); //field is now uneditable so no need to clear it
            	vEField.setText("");
            	vLField.setText("");
            	boolean sortedPhase = helper.findPhase(car, phaseList);
				if (sortedPhase) {
					System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
				}else {
					throw new PhaseException(car.getPlateNumber() + " could not be sorted, check the segment and direction for format errors. " + car.getSegment() + ", " + car.getCrossingDirection());
				}
				checkCarSegment(car);
				String segment = car.getSegment();

				if (segment.equals("1")) {
					String carsAtSegment = Integer.toString(s1counter);
					String waitingTime = Float.toString(s1WaitingTime);
					String waitingLength = Float.toString(s1WaitingLength);
					String avgCrossSegment = Float.toString(s1CrossTime / 2);
					statsModel.setValueAt(carsAtSegment, 0, 1);
					statsModel.setValueAt(waitingTime, 0, 2);
					statsModel.setValueAt(waitingLength, 0, 3);
					statsModel.setValueAt(avgCrossSegment, 0, 4);
				}
				if (segment.equals("2")) {
					String carsAtSegment = Integer.toString(s2counter);
					String waitingTime = Float.toString(s2WaitingTime);
					String waitingLength = Float.toString(s2WaitingLength);
					String avgCrossSegment = Float.toString(s2CrossTime / 2);
					statsModel.setValueAt(carsAtSegment, 1, 1);
					statsModel.setValueAt(waitingTime, 1, 2);
					statsModel.setValueAt(waitingLength, 1, 3);
					statsModel.setValueAt(avgCrossSegment, 1, 4);
				}
				if (segment.equals("3")) {
					String carsAtSegment = Integer.toString(s3counter);
					String waitingTime = Float.toString(s3WaitingTime);
					String waitingLength = Float.toString(s3WaitingLength);
					String avgCrossSegment = Float.toString(s3CrossTime / 2);
					statsModel.setValueAt(carsAtSegment, 2, 1);
					statsModel.setValueAt(waitingTime, 2, 2);
					statsModel.setValueAt(waitingLength, 2, 3);
					statsModel.setValueAt(avgCrossSegment, 2, 4);
				}
				if (segment.equals("4")) {
					String carsAtSegment = Integer.toString(s4counter);
					String waitingTime = Float.toString(s4WaitingTime);
					String waitingLength = Float.toString(s4WaitingLength);
					String avgCrossSegment = Float.toString(s4CrossTime / 2);
					statsModel.setValueAt(carsAtSegment, 3, 1);
					statsModel.setValueAt(waitingTime, 3, 2);
					statsModel.setValueAt(waitingLength, 3, 3);
					statsModel.setValueAt(avgCrossSegment, 3, 4);
				}	
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
        }		 
	}
	
	
	private JScrollPane addVehiclePane(LinkedList<Phases> phaseList, Helper helper, GUIModel model, GUIView view) {
		vehiclePane = view.getVehiclePane();
		vehicleModel = model.getVehicleModel();
		vehicleTable = view.getvehicleTable();
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
						vehicleModel.addRow(splitLine);	
						totalEmissions += car.getVehicleEmission();
						boolean sortedPhase = helper.findPhase(car, phaseList);
						if (sortedPhase) {
							System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
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
	
	private JScrollPane addPhasesPane(LinkedList<Phases> phaseList, Helper helper, GUIModel model, GUIView view) {
		phasePane = view.getPhasePane();
		phaseModel = model.getPhaseModel();
		phaseTable = view.getphaseTable();
		for (Phases phase: phaseList) {
			Vector<String> rowData = new Vector<String>();
			rowData.add(phase.getPhaseName());
			rowData.add(Float.toString(phase.getPhaseTimer()));
			phaseModel.addRow(rowData);
		}
		phaseTable.setModel(phaseModel);
		phasePane.getViewport().add(phaseTable);
		return phasePane;		
	}
	
	private void checkCarSegment(Vehicles car) {
		String carSegment = car.getSegment();
		if (carSegment.equals("1")) {
			s1counter ++;	
			s1WaitingTime += car.getCrossingTime();
			s1WaitingLength += car.getVehicleLength();
		}
		if (carSegment.equals("2")) {
			s2counter ++;
			s2WaitingTime += car.getCrossingTime();
			s2WaitingLength += car.getVehicleLength();
		}
		if (carSegment.equals("3")) {
			s3counter ++;
			s3WaitingTime += car.getCrossingTime();
			s3WaitingLength += car.getVehicleLength();
		}
		if (carSegment.equals("4")) {
			s4counter ++;
			s4WaitingTime += car.getCrossingTime();
			s4WaitingLength += car.getVehicleLength();
		}
	}
	
	private JScrollPane addStatsPane(LinkedList<Phases> phaseList, Helper helper, GUIModel model, GUIView view) {
		statsPane = view.getStatsPane();
		statsModel = model.getStatsModel();
		statsTable = view.getstatsTable();
		for (int i = 0; i < 8; i++) {
			Phases phase = phaseList.get(i);
			String segment = phaseSegment[i];
			if (segment.equals("1")) {
				s1CrossTime += phase.getPhaseTimer();
			}
			if (segment.equals("2")) {
				s2CrossTime += phase.getPhaseTimer();
			}
			if (segment.equals("3")) {
				s3CrossTime += phase.getPhaseTimer();
			}
			if (segment.equals("4")) {
				s4CrossTime += phase.getPhaseTimer();
			}

			LinkedList<Vehicles> carsQueue = phase.getLinkedList();
			for (Vehicles car : carsQueue) {
				checkCarSegment(car);
			}

		}
		for (int i = 1; i < 5; i ++) {
			Vector<String> rowData = new Vector<String>();
			String segment = Integer.toString(i);
			rowData.add(segment);
			if (i == 1) {
				String carsAtSegment = Integer.toString(s1counter);
				String waitingTime = Float.toString(s1WaitingTime);
				String waitingLength = Float.toString(s1WaitingLength);
				String avgCrossSegment = Float.toString(s1CrossTime / 2);
				rowData.add(carsAtSegment);
				rowData.add(waitingTime);
				rowData.add(waitingLength);
				rowData.add(avgCrossSegment);
			}
			if (i == 2) {
				String carsAtSegment = Integer.toString(s2counter);
				String waitingTime = Float.toString(s2WaitingTime);
				String waitingLength = Float.toString(s2WaitingLength);
				String avgCrossSegment = Float.toString(s2CrossTime / 2);
				rowData.add(carsAtSegment);
				rowData.add(waitingTime);
				rowData.add(waitingLength);
				rowData.add(avgCrossSegment);
			}
			if (i == 3) {
				String carsAtSegment = Integer.toString(s3counter);
				String waitingTime = Float.toString(s3WaitingTime);
				String waitingLength = Float.toString(s3WaitingLength);
				String avgCrossSegment = Float.toString(s3CrossTime / 2);
				rowData.add(carsAtSegment);
				rowData.add(waitingTime);
				rowData.add(waitingLength);
				rowData.add(avgCrossSegment);
			}
			if (i == 4) {
				String carsAtSegment = Integer.toString(s4counter);
				String waitingTime = Float.toString(s4WaitingTime);
				String waitingLength = Float.toString(s4WaitingLength);
				String avgCrossSegment = Float.toString(s4CrossTime / 2);
				rowData.add(carsAtSegment);
				rowData.add(waitingTime);
				rowData.add(waitingLength);
				rowData.add(avgCrossSegment);
			}	
			statsModel.addRow(rowData);			
		}
		statsTable.setModel(statsModel);
		statsPane.getViewport().add(statsTable);
		return statsPane;		
	}
	
}
