import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



public class GUIView extends JFrame implements Observer {

	/**
	 * 
	 */
	public GUIModel guiModel;
	private static final long serialVersionUID = 1L;
	private Font font = new Font("Courier", Font.BOLD, 20);
	private Font formFont = new Font("Courier", Font.BOLD, 15);
	private JPanel basePanel;
	
	// table headings
	private JLabel vehicleLabel;
	private JLabel phaseLabel;
	private JLabel segmentLabel;
	private JPanel labelsPanel;
	
	// tables
	private JTable statsTable;
	private JTable vehicleTable;
	private JTable phaseTable;
	private JScrollPane vehiclePane;
	private JScrollPane phasePane;
	private JScrollPane statsPane;
	private JPanel tablesPanel;

	// panel to hold form area
	private JPanel formPanel;
	private JPanel formLabelPanel;
	private JPanel formFieldPanel;
	private JLabel pNumberLabel;
	private JLabel vTypeLabel;
	private JLabel cTimeLabel;
	private JLabel cDirectionLabel;
	private JLabel cStatusLabel;
	private JLabel vEmissionsLabel;
	private JLabel vLengthLabel;
	private JLabel vSegmentLabel;
	private JTextField emissionField;
	private JTextField pNField;
	private JComboBox<String> vTField;
	private JTextField cTField;
	private JComboBox<String> cDField;
	private JTextField cSField;
	private JTextField vEField;
	private JTextField vLField;
	private JComboBox<String> sField;
	// Button Panel
	private JButton addVehicleButton;
	private JPanel addVehicleButtonPanel;
	private String[] crossingDirection = { "straight", "left", "right" };
	private String[] vehicleType = { "bus", "car", "truck" };
	private String[] segment = { "1", "2", "3", "4" };
	// Empty JLabel to fill space
	private JLabel emptyLabel;
	private JLabel emptyLabel2;
	//Total Emission Display
	private JPanel emissionPanel;
	private JLabel emissionLabel;;
	private JButton startButton;
	
	public GUIView(GUIModel model) {
		this.setTitle("Road Intersection");
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		basePanel = createPanel(7, 3, 20, 0);
		labelsPanel = createTableLabelsPanel();
		basePanel.add(labelsPanel);
		tablesPanel = createPanel(1, 3, 20, 0);
		//creating scroll panes for tables
		vehiclePane = createTableScrollPanes();
		phasePane = createTableScrollPanes();
		statsPane = createTableScrollPanes();
		//creating empty table objects
		vehicleTable = new JTable();
		statsTable = new JTable();
		phaseTable = new JTable();
		//adding pane objects to the housing table panel
		tablesPanel.add(vehiclePane);
		tablesPanel.add(phasePane);
		tablesPanel.add(statsPane);
		//add table panel to the base panel object
		basePanel.add(tablesPanel);
		//empty object to add space between tables and the form elements
		emptyLabel = addLabels("", font);
		basePanel.add(emptyLabel);
		//create panel that houses the form elements
		formPanel = createFormPanel();
		basePanel.add(formPanel);
		//Second empty object to add space between form element and the next field
		emptyLabel2 = addLabels("", font);
		basePanel.add(emptyLabel2);
		//Emission Panel
		emissionPanel = addEmissionsPanel();
		basePanel.add(emissionPanel);
		startButton = createButton("Start Simulation");
		basePanel.add(startButton);
		this.add(basePanel);
		this.guiModel = model;
		guiModel.registerObserver(this);
		System.out.println("View successfully registered to the model");
		
	}
	

	private JPanel createPanel(int rows, int columns, int horizontal, int vertical) {
		JPanel _panel = new JPanel();
		_panel.setLayout(new GridLayout(rows, columns, horizontal, vertical));
		return _panel;
	}

	private JComboBox<String> addComboBox(String[] choices) {
		JComboBox<String> option = new JComboBox<String>(choices);
		option.setBackground(Color.white);
		return option;
	}

	private JLabel addLabels(String labelname, Font font) {
		JLabel label = new JLabel(labelname, SwingConstants.CENTER);
		label.setFont(font);
		return label;
	}

	private JPanel createTableLabelsPanel() {
		vehicleLabel = addLabels("Vehicles", font);
		phaseLabel = addLabels("Phases", font);
		segmentLabel = addLabels("Segments", font);
		JPanel _panel = createPanel(1, 3, 20, 0);
		_panel.add(vehicleLabel);
		_panel.add(phaseLabel);
		_panel.add(segmentLabel);
		return _panel;
	}

	public JScrollPane createTableScrollPanes() {
		return new JScrollPane();
	}

	private JPanel createFormPanel() {
		JPanel _panel = createPanel(4, 1, 20, 10);
		formLabelPanel = createFormLabelPanel();
		_panel.add(formLabelPanel);
		formFieldPanel = createFormFields();
		_panel.add(formFieldPanel);
		addVehicleButtonPanel = createFormButtonPanel();
		_panel.add(addVehicleButtonPanel);
		return _panel;
	}

	private JPanel createFormLabelPanel() {
		JPanel _panel = createPanel(1, 1, 20, 0); // create panel to hold the labels

		// create the form field labels
		pNumberLabel = addLabels("Plate Number", formFont);
		vTypeLabel = addLabels("Vehicle Type", formFont);
		cTimeLabel = addLabels("Crossing Time", formFont);
		cDirectionLabel = addLabels("Crossing Direction", formFont);
		cStatusLabel = addLabels("Crossing Status", formFont);
		vEmissionsLabel = addLabels("Vehicle Emissions", formFont);
		vLengthLabel = addLabels("Vehicle Length (m)", formFont);
		vSegmentLabel = addLabels("Vehicle Segment", formFont);
		// add to panel
		_panel.add(pNumberLabel);
		_panel.add(vTypeLabel);
		_panel.add(cTimeLabel);
		_panel.add(cDirectionLabel);
		_panel.add(cStatusLabel);
		_panel.add(vEmissionsLabel);
		_panel.add(vLengthLabel);
		_panel.add(vSegmentLabel);

		return _panel;
	}

	private JPanel createFormButtonPanel() {
		JPanel _panel = createPanel(1, 3, 0, 20);
		addVehicleButton = createButton("Add Vehicle");
		JLabel emptyLabel = new JLabel("");
		JLabel emptyLabel2 = new JLabel("");
		_panel.add(emptyLabel);
		_panel.add(addVehicleButton);
		_panel.add(emptyLabel2);
		return _panel;
	}

	private JButton createButton(String buttonName) {
		JButton _button = new JButton(buttonName);
		_button.setLayout(new FlowLayout(FlowLayout.CENTER));
		return _button;
	}

	private JPanel createFormFields() {
		// text fields
		pNField = createTextField();
		vTField = addComboBox(vehicleType);
		cTField = createTextField();
		cDField = addComboBox(crossingDirection);
		cSField = new JTextField();
		cSField.setText("not crossed");
		cSField.setBackground(Color.white);
		cSField.setFont(formFont);
		cSField.setEditable(false);
		vEField = createTextField();
		vLField = createTextField();
		sField = addComboBox(segment);

		JPanel _panel = createPanel(1, 1, 20, 0);
		_panel.add(pNField);
		_panel.add(vTField);
		_panel.add(cTField);
		_panel.add(cDField);
		_panel.add(cSField);
		_panel.add(vEField);
		_panel.add(vLField);
		_panel.add(sField);

		return _panel;
	}
	
	private JTextField createTextField() {
		JTextField _textField = new JTextField();
		_textField.setBackground(Color.white);
		_textField.setFont(formFont);
		return _textField;
	}
	
	public JPanel addEmissionsPanel() {
		JPanel _panel = new JPanel();
		_panel.setLayout(new FlowLayout());;
		emissionLabel = addLabels("Total CO2 Emissions", formFont);
		emissionField = new JTextField("", 6);
		emissionField.setBackground(Color.white);
		emissionField.setFont(formFont);
		emissionField.setEditable(false);
		emissionField.setEditable(false);
		_panel.add(emissionLabel);
		_panel.add(emissionField);
		JLabel _label = new JLabel("");
		_panel.add(_label);
		return _panel;
	}


	public void addVehicleButtonListener(ActionListener listener) {
		addVehicleButton.addActionListener(listener);
	}
	
	public void startButtonListener(ActionListener listener) {
		startButton.addActionListener(listener);
	}
	
	public JScrollPane getStatsPane() {
		return statsPane;
	}
	
	public JComboBox<String> getsField() {
		return sField;
	}

	public JTable getstatsTable() {
		
		return statsTable;
	}
	
	public JTable getvehicleTable() {
		
		return vehicleTable;
	}
	
	public JTable getphaseTable() {
		
		return phaseTable;
	}

	public JScrollPane getVehiclePane() {
		return vehiclePane;
	}
	
	public JScrollPane getPhasePane() {
		return phasePane;
	}

	public JPanel getTablesPanel() {
		return tablesPanel;
	}

	public JTextField getEmissionField() {
		return emissionField;
	}
	
	public void setEmissionField(String labelName) {
		this.emissionField.setText(labelName);
	}

	public JComboBox<String> getvTField() {
		return vTField;
	}

	public JTextField getcTField() {
		return cTField;
	}

	public JTextField getcSField() {
		return cSField;
	}

	public JTextField getvEField() {
		return vEField;
	}

	public JTextField getvLField() {
		return vLField;
	}
	
	public JTextField getpNField() {
		return pNField;
	}
	
	public JComboBox<String> getcDField() {
		return cDField;
	}

	@Override
	public void update() {
		System.out.println("Success-----The View has been Updated");
		JOptionPane.showMessageDialog(null, // display count
				"The View has been Updated");
		
	}

}
