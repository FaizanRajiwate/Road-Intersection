import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;
import java.lang.Float;
import java.lang.Integer;




public class GUI extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8261139661343866575L;
	JLabel vehicleLabel;
	JLabel phaseLabel;
	JLabel segmentStatistics;
	JTable vehicleTable;
	JScrollPane vehiclePane;
	JScrollPane phasePane;
	JTable phaseTable;
	JTable segmentStatsTable;
	DefaultTableModel vehicleModel;
	DefaultTableModel phaseModel;
	JPanel addVehicle;
	String[] crossingDirection = {"straight", "left", "right"};
	String[] vehicleType = {"bus", "car", "truck"};
	String[] segment = {"1", "2", "3", "4"};
	JLabel pNumberLabel;
	JLabel vTypeLabel;
	JLabel cTimeLabel;
	JLabel cDirectionLabel;
	JLabel cStatusLabel;
	JLabel vEmissionsLabel;
	JLabel vLengthLabel;
	JLabel vSegmentLabel;
	JTextField pNField;
	JComboBox<String> vTField;
	JTextField cTField;
	JComboBox<String> cDField;
	JTextField cSField;
	JTextField vEField;
	JTextField vLField;
	JComboBox<String> sField;
	
	public GUI(){
		setTitle("Road Intersection");
		setVisible(true);
	}
	
	public Vehicles createVehicle(List<String> csvFileLine) {
		//Extract variables from csv File
		String plateNumber = csvFileLine.get(0);
		String vehicleType = csvFileLine.get(1);
		String crossingTime = csvFileLine.get(2);
		String direction = csvFileLine.get(3);
		String crossingStatus = csvFileLine.get(4);
		String emissionRate = csvFileLine.get(5);
		String vehicleLength = csvFileLine.get(6);
		String segment = csvFileLine.get(7);
		
		//set instance variable for car object;
		Vehicles car = new Vehicles();
		car.setPlateNumber(plateNumber);
		car.setVehicleType(vehicleType);
		car.setCrossingTime(Float.parseFloat(crossingTime));
		car.setCrossingDirection(direction);
		car.setCrossingStatus(crossingStatus);
		car.setVehicleEmission(Float.parseFloat(emissionRate));
		car.setVehicleLength(Float.parseFloat(vehicleLength));
		car.setSegment(segment);
		
		return car;
	}
	
	public Phases createPhase(List<String> csvFileLine) {
		//Extract variables from csv File
		String phaseName = csvFileLine.get(0);
		float phaseTimer = Float.parseFloat(csvFileLine.get(1));					
		Phases phase = new Phases();
		phase.setPhaseName(phaseName);
		phase.setPhaseTimer(phaseTimer);
		phase.setLinkedList();
		phase.setCrossedLinkedList();
		return phase;	
	}
	
	public Scanner readCsvFile(String filename) {
		try {
			Scanner csvScanner = new Scanner(new File(filename));
			csvScanner.useDelimiter(",");
			csvScanner.nextLine();
			return csvScanner;
		}catch (FileNotFoundException e) {
			System.out.println("The file you've selected does not exist in the src directory.");
			return null;
		}
	}	
	
	public boolean findPhase(Vehicles car, LinkedList<Phases> listOfPhases) {
		String direction = car.getCrossingDirection();
		String segment = car.getSegment();
		if (((direction.equals("straight")) || (direction.equals("right"))) && (segment.equals("1"))) {
				listOfPhases.get(1).getLinkedList().add(car);
				return true;
			}
		else if (((direction.equals("straight")) || (direction.equals("right"))) && (segment.equals("2")))  {
				listOfPhases.get(3).getLinkedList().add(car);
				return true;
			}
		else if (((direction.equals("straight")) || (direction.equals("right"))) && (segment.equals("3")))  {
				listOfPhases.get(5).getLinkedList().add(car);
				return true;
			}
		else if (((direction.equals("straight")) || (direction.equals("right"))) && (segment.equals("4")))  {
				listOfPhases.get(7).getLinkedList().add(car);
				return true;
		}
		else if ((direction.equals("left")  && (segment.equals("1")))) {
				listOfPhases.get(4).getLinkedList().add(car);
				return true;
			}
		else if ((direction.equals("left") && (segment.equals("2")))) {
				listOfPhases.get(6).getLinkedList().add(car);
				return true;
			}
		else if ((direction.equals("left")  && (segment.equals("3")))) {
				listOfPhases.get(0).getLinkedList().add(car);
				return true;
			}
		else if ((direction.equals("left")  && (segment.equals("4")))) {
				listOfPhases.get(2).getLinkedList().add(car);
				return true;
			}
		else {
			return false;
		}
		
	}
	
	public LinkedList<Phases> readPhasesFile(String filename) {
		try {
			LinkedList<Phases> phaseList = new LinkedList<Phases>();
			Scanner csvScanner = readCsvFile("phases.csv");
			if (csvScanner == null) {
				System.out.println();
				throw new FileNotFoundException("The File you entered cannot be read, check the file");
			}
			while (csvScanner.hasNext()) {
				
				String line = csvScanner.nextLine();
				String[] splitLine = line.split(",");
				List<String> listSplitLine = Arrays.asList(splitLine);
				//populate Phases table
				Phases phase = createPhase(listSplitLine);
				phaseList.add(phase);

			}
			return phaseList;
	            
		}
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			 System.out.println(e);
			 return null;		
		
		 }
	}
	
	public JScrollPane addPhasesPane(LinkedList<Phases> phaseList) {
		String[] columnNames = {"Phase name",	"Phase Timer"};
		phaseModel = new DefaultTableModel();
		phaseModel.setColumnIdentifiers(columnNames);
		for (Phases phase: phaseList) {
			Vector<String> rowData = new Vector<String>();
			rowData.add(phase.getPhaseName());
			rowData.add(Float.toString(phase.getPhaseTimer()));
			phaseModel.addRow(rowData);
		}
		JTable phasesTable = new JTable(phaseModel);
		phasePane = new JScrollPane(phasesTable);
		return phasePane;
		
		
	}

	public JLabel addLabels(String labelname) {
		JLabel label = new JLabel(labelname, SwingConstants.CENTER);
		return label;
	}
	
	public JScrollPane addVehicleTable(LinkedList<Phases> phaseList) {
		String[] columnNames = {"plate number",	"type",	"crossing time (s)", "direction","crossing status",	"emission rate (g/min)", "length (m)", "segment"};
		vehicleModel = new DefaultTableModel();
		vehicleModel.setColumnIdentifiers(columnNames);
		
		try { 
			Scanner csvScanner = readCsvFile("vehicles.csv");
			if (csvScanner == null) {
				throw new FileNotFoundException("The File you entered cannot be found");
			}else {
			
				while (csvScanner.hasNext()) {
					try {
					
						String line = csvScanner.nextLine();
						String[] splitLine = line.split(",");
						Object[] rowData = new Object[splitLine.length];
						List<String> listSplitLine = Arrays.asList(splitLine);
						rowData = splitLine;
						vehicleModel.addRow(rowData);
						//populate VehicleJTable
					
						Vehicles car = createVehicle(listSplitLine);
					
						boolean sortedPhase = findPhase(car, phaseList);
						if (sortedPhase) {
							System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
						}else {
							throw new PhaseException(car.getPlateNumber() + " could not be sorted, check the segment and direction for format errors. " + car.getSegment() + ", " + car.getCrossingDirection());
						}
					}catch (PhaseException e) {
						 System.out.println(e); 
					 }				
				}
			}		     
		}
		 catch (FileNotFoundException e) {
			System.out.println(e);
		}
		vehicleTable = new JTable(vehicleModel);
		vehicleTable.setAutoCreateRowSorter(true);
		vehiclePane = new JScrollPane(vehicleTable);
		return vehiclePane;
	}
	
	public JComboBox<String> addComboBox(String[] choices) {
		JComboBox<String> option = new JComboBox<String>(choices);
		return option;	
	}
	
	public JPanel tablesDisplayPanel(LinkedList<Phases> phaseList) {
		JScrollPane vehiclePane = addVehicleTable(phaseList);
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1,2, 20, 0));
		tablePanel.add(vehiclePane);
		//Panel for Phase and phase Table
		JScrollPane phasePane = addPhasesPane(phaseList);
		tablePanel.add(phasePane);
		return tablePanel;
	}
	
	public boolean checkNull(String text) {
		if(text.trim().length() ==0) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean checkNumberValid(String text) {
		try {
			Float.parseFloat(text);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	public boolean checkDuplicate(String plateNumber, LinkedList<Phases> phaseList) {
		for (Phases phase: phaseList) {
			LinkedList<Vehicles> cars = phase.getLinkedList();
			for (Vehicles car: cars) {
				String carPlateNumber = car.getPlateNumber();
				if (plateNumber.equals(carPlateNumber)) {
					return true;
				}
			}
		}
		return false;
	}
	public JPanel addTableDisplayPanel(LinkedList<Phases> phaseList) {
		Font formFont = new Font("Courier", Font.BOLD, 15);
		JPanel addVehicle = new JPanel();
		addVehicle.setLayout(new GridLayout(4,1, 20, 0));
		
		JPanel formLabelPanel = new JPanel();
		formLabelPanel.setLayout(new GridLayout(1, 1, 20, 50));

		pNumberLabel = addLabels("PlateNumber");
		vTypeLabel = addLabels("Vehicle Type");
		cTimeLabel = addLabels("Crossing Time");
		cDirectionLabel = addLabels("Crossing Direction");
		cStatusLabel = addLabels("Crossing Status");
		vEmissionsLabel = addLabels("Vehicle Emissions");
		vLengthLabel = addLabels("Vehicle Length");
		vSegmentLabel = addLabels("Vehicle Segment");
		
		pNumberLabel.setFont(formFont);
		vTypeLabel.setFont(formFont);
		cTimeLabel.setFont(formFont);
		cDirectionLabel.setFont(formFont);
		cStatusLabel.setFont(formFont);
		vEmissionsLabel.setFont(formFont);
		vLengthLabel.setFont(formFont);
		vSegmentLabel.setFont(formFont);
		
		formLabelPanel.add(pNumberLabel);
		formLabelPanel.add(vTypeLabel);
		formLabelPanel.add(cTimeLabel);
		formLabelPanel.add(cDirectionLabel);
		formLabelPanel.add(cStatusLabel);
		formLabelPanel.add(vEmissionsLabel);
		formLabelPanel.add(vLengthLabel);
		formLabelPanel.add(vSegmentLabel);

		//text fields
		pNField = new JTextField();
		vTField = addComboBox(this.vehicleType);
		cTField = new JTextField();
		cDField = addComboBox(this.crossingDirection);
		cSField = new JTextField();
		cSField.setText("not crossed");
		cSField.setEditable(false);
		vEField = new JTextField();
		vLField = new JTextField();
		sField = addComboBox(this.segment);
		
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new GridLayout(1,1, 20, 0));
		fieldPanel.add(pNField);
		fieldPanel.add(vTField);
		fieldPanel.add(cTField);
		fieldPanel.add(cDField);
		fieldPanel.add(cSField);
		fieldPanel.add(vEField);
		fieldPanel.add(vLField);
		fieldPanel.add(sField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,1));
		JButton vehicleAdd = new JButton("Add Vehicle");
		vehicleAdd.setLayout(new FlowLayout(FlowLayout.CENTER));
		vehicleAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //write to file
            	String pNEntry = pNField.getText();
            	String vTEntry = vTField.getSelectedItem().toString();
            	String cTEntry = cTField.getText();
            	String cDEntry = cDField.getSelectedItem().toString();
            	String cSEntry = cSField.getText();
            	String vEEntry = vEField.getText();
            	String vLEntry = vLField.getText();
            	String sEntry = sField.getSelectedItem().toString();
            	
            	try {
            		if (checkNull(pNEntry)) {
            			throw new InaccurateDataException("The Plate Number cannot be empty");
            		}else if (checkNull(cTEntry)){
            			throw new InaccurateDataException("The Crossing Time cannot be empty");
            		}else if(checkNull(cSEntry)) {
            			throw new InaccurateDataException("The Crossing Status cannot be empty ");
            		}else if (checkNull(vEEntry)) {
            			throw new InaccurateDataException("The Vehicle Emissions cannot be empty");
            		}else if (checkNull(vLEntry)) {
            			throw new InaccurateDataException("The Vehicle Length cannot be empty");
            		}else if(!checkNumberValid(cTEntry)) {
            			throw new NumberFormatException("The Crossing Time entry is not a number");
            		}else if(!checkNumberValid(vEEntry)) {
            			throw new NumberFormatException("The Vehicle Emissions is not a float.");
            		}else if (!checkNumberValid(sEntry)) {
            			throw new NumberFormatException("The Segment is not a number");
            		}else if (!((Float.parseFloat(cTEntry) < 10) && (Float.parseFloat(cTEntry) > 0))) {
            			throw new NumberFormatException("Your Crossing Time Entry is not between 0 and 10s");
            		}else if (!((Float.parseFloat(vEEntry) < 50) && (Float.parseFloat(vEEntry) > 0))) {
            			throw new NumberFormatException("Your vehicle emission Entry is not between 0 and 50 " + vEEntry);
            		}else if (!((Integer.parseInt(sEntry) < 5) && (Integer.parseInt(sEntry) > 0))) {
            			throw new NumberFormatException("Your Segment Entry is not between 1 and 4");
            		}else if(!((cSEntry.equals("not crossed")) || (cSEntry.equals("crossed")))) {
            			throw new InaccurateDataException("Your Crossing status should either be 'crossed' or 'not crossed'");
            		}else if (checkDuplicate(pNEntry, phaseList)) {
            			throw new DuplicateIDException(pNEntry + ": This vehicle has a duplicate car plate number.");
            		}else {
//            			Vector<String> tableEntry = new Vector<String>();
            			String[] tableEntry = {pNEntry, vTEntry, cTEntry, cDEntry, cSEntry, vEEntry, vLEntry, sEntry};
            			List<String> newLine = Arrays.asList(tableEntry);
            			Vehicles car = createVehicle(newLine);
            			
//            			tableEntry.add(pNEntry);
//            			tableEntry.add(vTEntry);
//            			tableEntry.add(cTEntry);
//            			tableEntry.add(cDEntry);
//            			tableEntry.add(cSEntry);
//            			tableEntry.add(vEEntry);
//            			tableEntry.add(vLEntry);
//            			tableEntry.add(sEntry);
            			
            			vehicleModel.addRow(tableEntry);
            			
            			
            			
            			pNField.setText("");
                    	cTField.setText("");
//                    	cSField.setText(""); //field is now uneditable so no need to clear it
                    	vEField.setText("");
                    	vLField.setText("");
                    	boolean sortedPhase = findPhase(car, phaseList);
						if (sortedPhase) {
							System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
						}else {
							throw new PhaseException(car.getPlateNumber() + " could not be sorted, check the segment and direction for format errors. " + car.getSegment() + ", " + car.getCrossingDirection());
						}

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
        });
		JLabel emptyLabel = new JLabel();
		buttonPanel.add(vehicleAdd);	
		addVehicle.add(formLabelPanel);
		addVehicle.add(fieldPanel);
		addVehicle.add(emptyLabel);
		addVehicle.add(buttonPanel);
				
		return addVehicle;
	}
	
	
}


