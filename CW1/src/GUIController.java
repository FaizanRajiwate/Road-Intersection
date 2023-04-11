
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
//import java.awt.event.WindowAdapter;
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

import exceptions.DuplicateIDException;
import exceptions.InaccurateDataException;
import exceptions.PhaseException;

public class GUIController {
	// list of observers
	private List<Observer> registeredObservers = new LinkedList<Observer>();
	private ReportFile file;
	private GUIModel model;
	private GUIView view;
	private Helper helper;
	String[] phaseSegment = { "3", "1", "4", "2", "1", "3", "2", "4" };
	private LinkedList<Phases> phaseList;

	// GUI Elements
	private JPanel tablesPanel;
	// Vehicle Elements
	private JScrollPane vehiclePane;
	private DefaultTableModel vehicleModel;
	private JTable vehicleTable;

	// Phase Elements
	private JScrollPane phasePane;
	private DefaultTableModel phaseModel;
	private JTable phaseTable;

	// Segment Statistics Elements
	private JScrollPane statsPane;
	private DefaultTableModel statsModel;
	private JTable statsTable;

	// Form Fields
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
		this.phaseList = helper.readPhasesFile("/phases.csv");
		// Segment Table Variables
		this.file = ReportFile.getInstance();
		// GUI Elements;
		tablesPanel = view.getTablesPanel();
		if (tablesPanel != null) {
			Main.blnDoWork = true;
		} else {
			Main.blnDoWork = false;
		}
		;
		tablesPanel.add(addVehiclePane(phaseList, helper, model, view));
		Main.blnDoWork = false;
		tablesPanel.add(addPhasesPane(phaseList, helper, model, view));
		tablesPanel.add(addStatsPane(phaseList, helper, model, view));

		// Getting Form Fields
		pNField = view.getpNField();
		vTField = view.getvTField();
		cTField = view.getcTField();
		cDField = view.getcDField();
		cSField = view.getcSField();
		vEField = view.getvEField();
		vLField = view.getvLField();
		sField = view.getsField();

		view.addVehicleButtonListener(new AddVehicleListener());
		view.startButtonListener(new StartButtonListener());
		view.mainWindowListener(new MainWindowListener());

	}
	class MainWindowListener extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			//Window listener to generate simulation crossing report 
			int totalCrossedVehicles = model.getVehicleList().size();
			float averageRunningEmissions = model.getRunningEmissions() / model.getVehicleList().size();
						
			file.writeToFile("Exiting Simulation.... ");
			file.writeToFile("Average Emission per Vehicle: " + averageRunningEmissions);
			file.writeToFile("===========================================================");
			file.writeToFile("Number of Crossed Vehicles");
			float waitTime = 0;
			for (Phases phase: phaseList) {
				int numberOfCrossedVehicles = phase.getCrossedLinkedList().size();
				file.writeToFile(phase.getPhaseName() + ", Crossed Vehicles: " + 
						numberOfCrossedVehicles + ", Average Waiting Time: " + (float) phase.getWaitingTime()/numberOfCrossedVehicles);
				waitTime += phase.getWaitingTime();
			}
			file.writeToFile("===========================================================");
			file.writeToFile("Average Waiting Time: " + (float) waitTime/ totalCrossedVehicles);
			
			JFrame alert = new JFrame();
			JOptionPane.showMessageDialog(alert, "Exiting Application");
			
			System.exit(0);
		}
		
	}

	class AddVehicleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//action listener creates a new thread to add vehicles manually
			new Thread() {
				public void run() {
					// time-consuming code to run here
					addVehicles();
					// Update the user interface components here
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

						}
					});

				}
			}.start();
			model.notifyObservers();
			// TODO Auto-generated method stub
		}
	}

	class StartButtonListener implements ActionListener {

		private GUIModel model;


	@Override
		public void actionPerformed(ActionEvent e) {
		//executes simulation, randomly generates vehicle. Each in a thread. 
			new Thread() {
				public void run() {
					// time-consuming code to run here
					executeSimulation();
					// Update the user interface components here
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
						}
					});
				}
			}.start();
			view.disableStartSimulation();
			
		}
	}

	public void executeSimulation() {
		//randomly generates vehicle in its own thread.
		VehicleRNG rngVehicle = new VehicleRNG(helper, phaseList, model);
		rngVehicle.start();
		//executes the intersection simulation to cross vehicles, in its own thread
		JunctionController controller = new JunctionController(phaseList, helper, model);
		controller.start();
		while (true) {
			view.setEmissionField(Float.toString(model.getTotalEmissions()));
		}
	}

	public void addVehicles() {
		//functionality to add vehicles
		try {
			String pNEntry = pNField.getText();
			String vTEntry = vTField.getSelectedItem().toString();
			String cTEntry = cTField.getText();
			String cDEntry = cDField.getSelectedItem().toString();
			String cSEntry = cSField.getText();
			String vEEntry = vEField.getText();
			String vLEntry = vLField.getText();
			String sEntry = sField.getSelectedItem().toString();
			String[] newVehicle = { pNEntry, vTEntry, cTEntry, cDEntry, cSEntry, vEEntry, vLEntry, sEntry };
			//takes the content of the form and makes a string array out of it
			List<String> newVehicleLine = Arrays.asList(newVehicle);
			//creates an object to list from the string array to be evaluated
			helper.evaluateVehicleFile(newVehicleLine, phaseList);
			Vehicles car = helper.createVehicle(newVehicleLine, phaseList); //creates vehicle
			if (car == null) {
				throw new InaccurateDataException("The row with " + newVehicleLine.get(0) + "could not be created");
			}

			model.addToTotalEmissions(car.getVehicleEmission()); //add to total emission rating
			view.setEmissionField(Float.toString(model.getTotalEmissions())); //updates the text field with this
			vehicleModel.addRow(newVehicle); //adds newly created vehicle to list of created vehicles
			pNField.setText("");
			cTField.setText("");
			vEField.setText("");
			vLField.setText("");
			//empties text fields
			boolean sortedPhase = helper.findPhase(car, phaseList); //finds the appropriate phase and adds vehicle to that queue
			if (sortedPhase) {
				// System.out.println(car.getPlateNumber() + " has been added to the appropriate
				// phase");
			} else {
				throw new PhaseException(car.getPlateNumber()
						+ " could not be sorted, check the segment and direction for format errors. " + car.getSegment()
						+ ", " + car.getCrossingDirection());
			}
			helper.checkCarSegment(car, model); //checks the segment the vehicle belongs to and updates variables
			String segment = car.getSegment(); //gets segment to update the model.

			helper.updateSegmentTable(segment, model);
			file.writeToFile(car.getPlateNumber() + "has been accepted and added to the Queue");
		} catch (exceptions.InaccurateDataException ex) {
			JFrame alert = new JFrame();
			JOptionPane.showMessageDialog(alert, ex);
		} catch (NumberFormatException ex) {
			JFrame alert = new JFrame();
			JOptionPane.showMessageDialog(alert, ex);
		} catch (exceptions.PhaseException ex) {
			JFrame alert = new JFrame();
			JOptionPane.showMessageDialog(alert, ex);
		} catch (exceptions.DuplicateIDException ex) {
			JFrame alert = new JFrame();
			JOptionPane.showMessageDialog(alert, ex);
		}
		// this.model.notifyObservers();
	}

	private JScrollPane addVehiclePane(LinkedList<Phases> phaseList, Helper helper, GUIModel model, GUIView view) {
		//fills empty vehicle table object with appropriate vehicle model.
		while (!Main.blnDoWork) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		
		vehiclePane = view.getVehiclePane();
		vehicleModel = model.getVehicleModel();
		vehicleTable = view.getvehicleTable();
		synchronized (this) {

			try {
				Scanner csvScanner = helper.readCsvFile("vehicles.csv"); 
				if (csvScanner == null) {
					throw new FileNotFoundException("The File you entered cannot be found");
				} else {

					while (csvScanner.hasNext()) {
						try {

							String line = csvScanner.nextLine();
							String[] splitLine = line.split(",");
							List<String> listSplitLine = Arrays.asList(splitLine);
							helper.evaluateVehicleFile(listSplitLine, phaseList); //evaluates line from csv file for validity
							// populate VehicleJTable
							Vehicles car = helper.createVehicle(listSplitLine, phaseList);

							if (car == null) {
								throw new InaccurateDataException(
										"The row with " + listSplitLine.get(0) + "could not be created");
							}
							file.writeToFile(car.getPlateNumber() + " has been read and added to the Queue");
							vehicleModel.addRow(splitLine);
							model.addToTotalEmissions(car.getVehicleEmission());
							boolean sortedPhase = helper.findPhase(car, phaseList);
							if (sortedPhase) {
								model.addNewVehicle(car.getPlateNumber());
								// System.out.println(car.getPlateNumber() + " has been added to the appropriate
								// phase");
							} else {
								throw new PhaseException(car.getPlateNumber()
										+ " could not be sorted, check the segment and direction for format errors. "
										+ car.getSegment() + ", " + car.getCrossingDirection());
							}
						} catch (PhaseException e) {
							JFrame alert = new JFrame();
							JOptionPane.showMessageDialog(alert, e);
							continue;
						} catch (InaccurateDataException e) {
							e.printStackTrace();
							JFrame alert = new JFrame();
							JOptionPane.showMessageDialog(alert, e);
							continue;
						} catch (DuplicateIDException e) {
							JFrame alert = new JFrame();
							JOptionPane.showMessageDialog(alert, e);
							continue;
						} catch (NumberFormatException e) {
							JFrame alert = new JFrame();
							JOptionPane.showMessageDialog(alert, e);
							continue;
						}

					}
				}
			} catch (FileNotFoundException e) {
				System.out.println(e);
			}

			vehicleTable.setAutoCreateRowSorter(true); //for sorting by column
			vehicleTable.setModel(vehicleModel);
			vehiclePane.getViewport().add(view.getvehicleTable()); //adds table to viewport for the empty pane
			view.setEmissionField(Float.toString(model.getTotalEmissions())); //updates emission field with total emissions.
			return vehiclePane;
		}

	}

	private JScrollPane addPhasesPane(LinkedList<Phases> phaseList, Helper helper, GUIModel model, GUIView view) {
		phasePane = view.getPhasePane();
		phaseModel = model.getPhaseModel();
		phaseTable = view.getphaseTable();
		synchronized (this) {
			for (Phases phase : phaseList) {
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
		synchronized (this) {
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
					helper.checkCarSegment(car, model);
				}

			}
			for (int i = 1; i < 5; i++) {
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
				model.updateModel(statsModel, rowData.toArray());

			}
			statsTable.setModel(statsModel);
			statsPane.getViewport().add(statsTable);
			file.writeToFile("Initial Segments calculated Statistics.");
			return statsPane;
		}

	}

}