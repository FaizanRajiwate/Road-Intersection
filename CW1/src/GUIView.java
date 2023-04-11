
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

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
	// Total Emission Display
	private JPanel emissionPanel;
	private JLabel emissionLabel;;
	//
	private JButton startButton;

	public GUIView(GUIModel model) {
		// creates a panel unto the base JFrame, with grid layout adds panels for
		// specific sections
		this.setTitle("Road Intersection");
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		basePanel = createPanel(7, 3, 20, 0);
		labelsPanel = createTableLabelsPanel();
		basePanel.add(labelsPanel);
		tablesPanel = createPanel(1, 3, 20, 0);
		// creating scroll panes for tables
		vehiclePane = createTableScrollPanes();
		phasePane = createTableScrollPanes();
		statsPane = createTableScrollPanes();
		// creating empty table objects
		vehicleTable = new JTable();
		statsTable = new JTable();
		phaseTable = new JTable();
		// adding pane objects to the housing table panel
		tablesPanel.add(vehiclePane);
		tablesPanel.add(phasePane);
		tablesPanel.add(statsPane);
		// add table panel to the base panel object
		basePanel.add(tablesPanel);
		// empty object to add space between tables and the form elements
		emptyLabel = addLabels("", font);
		basePanel.add(emptyLabel);
		// create panel that houses the form elements
		formPanel = createFormPanel();
		basePanel.add(formPanel);
		// Second empty object to add space between form element and the next field
		emptyLabel2 = addLabels("", font);
		basePanel.add(emptyLabel2);
		// Emission Panel
		emissionPanel = addEmissionsPanel();
		basePanel.add(emissionPanel);
		startButton = new JButton("Start Simulation");
		basePanel.add(startButton);
		this.add(basePanel);
		this.guiModel = model;
		guiModel.registerObserver(this);
		System.out.println("View successfully registered to the model");

	}

	private JPanel createPanel(int rows, int columns, int horizontal, int vertical) {
		// create a panel using grid layout with the number of rows, columns, horizontal
		// and vertical space
		JPanel _panel = new JPanel();
		_panel.setLayout(new GridLayout(rows, columns, horizontal, vertical));
		return _panel;
	}

	private JComboBox<String> addComboBox(String[] choices) {
		// creates a combo box with the string array provided
		JComboBox<String> option = new JComboBox<String>(choices);
		option.setBackground(Color.white);
		return option;
	}

	private JLabel addLabels(String labelname, Font font) {
		// creates a label using the font created, and centers it.
		JLabel label = new JLabel(labelname, SwingConstants.CENTER);
		label.setFont(font);
		return label;
	}

	private JPanel createTableLabelsPanel() {
		// creates a panel that just contains the table names.
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
		// creates a new scroll pane
		return new JScrollPane();
	}

	private JPanel createFormPanel() {
		// creates a panel that holds the form for adding new vehicles manually
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
		// panel containing labels for the form fields. will be inserted into the over
		// arching form panel
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
		// panel to hold add vehicle button
		JPanel _panel = createPanel(1, 3, 0, 20);
		addVehicleButton = createAddVehicleButton("Add Vehicle");
		JLabel emptyLabel = new JLabel("");
		JLabel emptyLabel2 = new JLabel("");
		_panel.add(emptyLabel);
		_panel.add(addVehicleButton);
		_panel.add(emptyLabel2);
		return _panel;
	}

	private JButton createAddVehicleButton(String buttonName) {
		// creates add vehicle button
		JButton _button = new JButton(buttonName);
		_button.setLayout(new FlowLayout(FlowLayout.CENTER));
		return _button;
	}

	private JPanel createFormFields() {
		// text fields
		// creates form fields to add to that panel;
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
		// creates textfield objects
		JTextField _textField = new JTextField();
		_textField.setBackground(Color.white);
		_textField.setFont(formFont);
		return _textField;
	}

	public JPanel addEmissionsPanel() {
		// panel to hold emissions text field.
		JPanel _panel = new JPanel();
		_panel.setLayout(new FlowLayout());
		;
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
		// action listener for manually adding vehicles. implemented in controller
		addVehicleButton.addActionListener(listener);
	}

	public void startButtonListener(ActionListener listener) {
		// action listener to start simulation.
		startButton.addActionListener(listener);
	}

	public void mainWindowListener(WindowAdapter window) {
		// action listener to generate some stats on crossed vehicles.
		this.addWindowListener(window);
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

	public void disableStartSimulation() {
		// disable start simulation button
		startButton.setEnabled(false);
	}

}