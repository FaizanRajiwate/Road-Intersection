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
	JPanel tablesPanel;
	JScrollPane vehiclePane;
	JScrollPane phasePane;
	JScrollPane statsPane;
	JTable phaseTable;
	JTable segmentStatsTable;
	JTable statsTable;
	DefaultTableModel vehicleModel;
	DefaultTableModel phaseModel;
	DefaultTableModel statsModel;
	JPanel addVehicle;
	String[] crossingDirection = {"straight", "left", "right"};
	String[] vehicleType = {"bus", "car", "truck"};
	String[] segment = {"1", "2", "3", "4"};
	String[] phaseSegment = {"3","1","4","2","1","3","2","4"};
	JLabel pNumberLabel;
	JLabel vTypeLabel;
	JLabel cTimeLabel;
	JLabel cDirectionLabel;
	JLabel cStatusLabel;
	JLabel vEmissionsLabel;
	JLabel vLengthLabel;
	JLabel vSegmentLabel;
	JTextField emissionField;
	JTextField pNField;
	JComboBox<String> vTField;
	JTextField cTField;
	JComboBox<String> cDField;
	JTextField cSField;
	JTextField vEField;
	JTextField vLField;
	JComboBox<String> sField;
	int s1counter = 0;
	float s1WaitingTime = 0;
	float s1CrossTime = 0;
	float s1WaitingLength = 0;
	int s2counter = 0;
	float s2WaitingTime = 0;
	float s2CrossTime = 0;
	float s2WaitingLength = 0;
	int s3counter = 0;
	float s3WaitingTime = 0;
	float s3CrossTime = 0;
	float s3WaitingLength = 0;
	int s4counter = 0;
	float s4WaitingTime = 0;
	float s4CrossTime = 0;
	float s4WaitingLength = 0;
	float totalEmissions = 0;
	
	public GUI(){
		//creates an empty jframe object with a title and makes it visible
		setTitle("Road Intersection");
		setVisible(true);
	}
	
	public Vehicles createVehicle(List<String> csvFileLine, LinkedList<Phases> phaseList) {
		//Extract variables from csv File
		String plateNumber = csvFileLine.get(0);
		String vehicleType = csvFileLine.get(1);
		vehicleType = vehicleType.toLowerCase();
		String crossingTime = csvFileLine.get(2);
		String direction = csvFileLine.get(3);
		String crossingStatus = csvFileLine.get(4);
		vehicleType = crossingStatus.toLowerCase();
		String emissionRate = csvFileLine.get(5);
		String vehicleLength = csvFileLine.get(6);
		String segment = csvFileLine.get(7);
		
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
		if(filename == null || filename == "" )
		{
			throw new NullPointerException();
		}
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
	
	public JLabel addLabels(String labelname) {
		JLabel label = new JLabel(labelname, SwingConstants.CENTER);
		return label;
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
						List<String> listSplitLine = Arrays.asList(splitLine);
						evaluateVehicleFile(listSplitLine, phaseList);
						//populate VehicleJTable
						Vehicles car = createVehicle(listSplitLine, phaseList);
						if (car == null) {
							throw new InaccurateDataException("The row with " + listSplitLine.get(0) + "could not be created");
						}
						vehicleModel.addRow(splitLine);	
						totalEmissions += car.getVehicleEmission();
						boolean sortedPhase = findPhase(car, phaseList);
						if (sortedPhase) {
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
		vehicleTable = new JTable(vehicleModel);
		vehicleTable.setAutoCreateRowSorter(true);
		vehiclePane = new JScrollPane(vehicleTable);
		return vehiclePane;
	}
	
	
	
	public JPanel tablesDisplayPanel(LinkedList<Phases> phaseList) {
		JScrollPane vehiclePane = addVehicleTable(phaseList);
		tablesPanel = new JPanel();
		tablesPanel.setLayout(new GridLayout(1, 3, 20, 0));
		tablesPanel.add(vehiclePane);
		//Panel for Phase and phase Table
		phasePane = addPhasesPane(phaseList);
		statsPane = addStatsDisplayPane(phaseList);
		tablesPanel.add(phasePane);
		tablesPanel.add(statsPane);
		return tablesPanel;
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
	
	public void evaluateVehicleFile(List<String> vehicleLine, LinkedList<Phases> phaseList) throws InaccurateDataException, DuplicateIDException, NumberFormatException
	{
		String plateNumber = vehicleLine.get(0);
		String vehicleType = vehicleLine.get(1);
		vehicleType = vehicleType.toLowerCase().trim();
		String crossingTime = vehicleLine.get(2);
		String direction = vehicleLine.get(3);
		String crossingStatus = vehicleLine.get(4);
		crossingStatus = crossingStatus.toLowerCase().trim();
		String emissionRate = vehicleLine.get(5);
		String vehicleLength = vehicleLine.get(6);
		String segment = vehicleLine.get(7);
		
		
		if (checkNull(plateNumber)) {
			throw new InaccurateDataException("The Plate Number cannot be empty");
		}else if (checkNull(crossingTime)){
			throw new InaccurateDataException("The Crossing Time cannot be empty");
		}else if (checkNull(vehicleType)){
			throw new InaccurateDataException("The Vehicle Type cannot be empty");
		}else if (!((vehicleType.equals("car")) || (vehicleType.equals("bus") || (vehicleType.equals("truck"))))){
			throw new InaccurateDataException("The Vehicle Type cannot be empty");	
		}else if(checkNull(crossingStatus)) {
			throw new InaccurateDataException("The Crossing Status cannot be empty ");
		}else if (checkNull(emissionRate)) {
			throw new InaccurateDataException("The Vehicle Emissions cannot be empty");
		}else if (checkNull(vehicleLength)) {
			throw new InaccurateDataException("The Vehicle Length cannot be empty");
		}else if(!checkNumberValid(crossingTime)) {
			throw new NumberFormatException("The Crossing Time entry is not a number");
		}else if(!checkNumberValid(emissionRate)) {
			throw new NumberFormatException("The Vehicle Emissions is not a float.");
		}else if (!checkNumberValid(segment)) {
			throw new NumberFormatException("The Segment is not a number");
		}else if (!((Float.parseFloat(crossingTime) < 10) && (Float.parseFloat(crossingTime) > 0))) {
			throw new NumberFormatException("Your Crossing Time Entry is not between 0 and 10s");
		}else if (!((Float.parseFloat(emissionRate) < 50) && (Float.parseFloat(emissionRate) > 0))) {
			throw new NumberFormatException("Your vehicle emission Entry is not between 0 and 50 " + emissionRate);
		}else if (!((Integer.parseInt(segment) < 5) && (Integer.parseInt(segment) > 0))) {
			throw new NumberFormatException("Your Segment Entry is not between 1 and 4");
		}else if (!((Float.parseFloat(vehicleLength) < 8) && (Float.parseFloat(vehicleLength)) > 0)) {
    			throw new NumberFormatException("Your Vehicle Length is not between 0 and 8m");
		}else if(!((crossingStatus.equals("not crossed")) || (crossingStatus.equals("waiting")))){
			throw new InaccurateDataException("Your Crossing status should either be 'crossed' or 'not crossed'");
		}else if(!((direction.equals("straight")) || (direction.equals("left")) || (direction.equals("right")))){
			throw new InaccurateDataException("Your direction should either be straight, left or right");
		}else if (checkDuplicate(plateNumber, phaseList)) {
			//throw new DuplicateIDException(plateNumber + ": This vehicle has a duplicate car plate number.");
		}
	}
	
	public JComboBox<String> addComboBox(String[] choices) {
		JComboBox<String> option = new JComboBox<String>(choices);
		option.setBackground(Color.white);
		return option;	
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
		cSField.setBackground(Color.white);
		cSField.setFont(formFont);
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
		buttonPanel.setLayout(new GridLayout(1,3));
//		JLabel _label1 = new JLabel("");
//		JLabel _label2 = new JLabel("");
		JButton vehicleAdd = new JButton("Add Vehicle");
		vehicleAdd.setLayout(new FlowLayout(FlowLayout.CENTER));
		vehicleAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {            	
            	            	
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
            	evaluateVehicleFile(newVehicleLine, phaseList);
    			Vehicles car = createVehicle(newVehicleLine, phaseList);
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
            	boolean sortedPhase = findPhase(car, phaseList);
				if (sortedPhase) {
					//System.out.println(car.getPlateNumber() + " has been added to the appropriate phase");
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
        });
		JLabel emptyLabel = new JLabel();
		buttonPanel.add(vehicleAdd);	
		addVehicle.add(formLabelPanel);
		addVehicle.add(fieldPanel);
		addVehicle.add(emptyLabel);
		addVehicle.add(buttonPanel);
				
		return addVehicle;
	}
	
	public void checkCarSegment(Vehicles car) {
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
	
	public JScrollPane addStatsDisplayPane(LinkedList<Phases> phaseList) {
		String[] columnNames = {"Segment",	"No. of Vehicles Waiting", "Waiting Time", 
				"Waiting Length", "Avg. Cross Time"};
		statsModel = new DefaultTableModel();
		statsModel.setColumnIdentifiers(columnNames);
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
		JTable statsTable = new JTable(statsModel);
		statsPane = new JScrollPane(statsTable);
		return statsPane;		
	}
	
	public JPanel addEmissionsPanel() {
		 Font formFont = new Font("Courier", Font.BOLD, 15);
		 JPanel emissionPanel = new JPanel();
		 emissionPanel.setLayout(new FlowLayout());
		 JLabel emissionLabel = addLabels("Total CO2 Emissions");
		 emissionLabel.setFont(formFont);
		 emissionField = new JTextField(Float.toString(totalEmissions), 6);
		 emissionField.setBackground(Color.white);
		 emissionField.setFont(formFont);
		 emissionField.setEditable(false);
		 emissionPanel.add(emissionLabel);
		 emissionPanel.add(emissionField);		 
		 return emissionPanel;
	}
}


