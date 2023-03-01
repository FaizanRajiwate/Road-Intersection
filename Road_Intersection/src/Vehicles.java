
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.*;

public class Vehicles {
    // frame

    static JFrame f;
    static JFrame addVehiclesFrame;
    // Table
    static JTable vehiclesTable;
    static JTable phasesTable;
    //static JTable statisticsTable;
    //Table Model
    static DefaultTableModel vehiclesModel;

    public static void main(String[] args) {
        // Frame initialization
        f = new JFrame();
        // Frame Title
        f.setTitle("CourseWork");

        f.setVisible(true);
        //Labels
        JLabel vehiclesLabel = new JLabel("Vehicles");
        JLabel phasesLabel = new JLabel("Phases");
        // JLabel statsLabel = new JLabel("Statistics");

        Font font = new Font("Courier", Font.BOLD, 20);
        vehiclesLabel.setFont(font);
        phasesLabel.setFont(font);
        // statsLabel.setFont(font);
        //Buttons
        JButton Add = new JButton("Add Vehicles");
        Add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addVehiclesFrame = new JFrame();
                addVehiclesFrame.setTitle("Add Vehicles");
                addVehiclesFrame.setSize(650, 650);
                addVehiclesFrame.setVisible(true);
                addVehiclesTable();
            }
        });
        JButton Exit = new JButton("Exit");
        Exit.addActionListener((event) -> System.exit(0));

        //methods to get data from CSV files
        VehiclesTable();
        PhasesTable();
        // StatisticsTable();
        //GUI Layout
        f.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(-120, -250, 130, 300);

        // column 0
        c.gridx = 0;

        // row 0
        c.gridy = 0;

        f.add(vehiclesLabel, c);
        // column 1
        c.gridx = 1;

        f.add(phasesLabel, c);
        // column 2
//        c.gridx = 2;
//
//        f.add(statsLabel, c);
        // column 0
        c.gridx = 0;

        // row 2
        c.gridy = 1;

        // increases components width by 20 pixels
        c.ipadx = 20;

        // increases components height by 20 pixels
        c.ipady = 10;
        f.add(new JScrollPane(vehiclesTable), c);

        // column 1
        c.gridx = 1;
        f.add(new JScrollPane(phasesTable), c);

//        c.gridx = 2;
//
//        // row 3
//        c.gridy = 1;
//
//        f.add(new JScrollPane(statisticsTable), c);
        // column 0
        c.gridx = 0;

        // row 3
        c.gridy = 2;

        f.add(Add, c);
        // column 1
        c.gridx = 2;

        // row 3
        c.gridy = 2;

        f.add(Exit, c);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void VehiclesTable() {
        String thisLine = "";
        // Column Names
        String[] columnNames = {"Vehicles", "Type", "Crossing Time", "Direction", "Length", "Emission", "Status",
            "Segment"};
        // Initializing the JTable
        vehiclesModel = new DefaultTableModel();
        vehiclesModel.setColumnIdentifiers(columnNames);
        vehiclesTable = new JTable(vehiclesModel);
        int i = 0;// line count of csv
        String[][] data = new String[0][];// csv data line count=0 initially
        try {
            //parsing a CSV file into BufferedReader class constructor  
            BufferedReader br = new BufferedReader(new FileReader("src/Vehicles.csv"));
            while ((thisLine = br.readLine()) != null) // returns a Boolean value
            {
                i++;
                String[][] newdata = new String[i][2];// create new array for data

                String lineContent[] = thisLine.split(",");// get contents of line as an array
                newdata[i - 1] = lineContent;// add new line to the array

                System.arraycopy(data, 0, newdata, 0, i - 1);// copy previously read values to new array
                data = newdata;// set new array as csv data
                vehiclesModel.addRow(data[i - 1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Print Vehicles.csv
        for (String[] strings : data) {
            for (String string : strings) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }

    public static void PhasesTable() {
        String thisLine = "";
        int i = 0;// line count of csv
        String[][] data = new String[0][];// csv data line count=0 initially
        try {
            //parsing a CSV file into BufferedReader class constructor  
            BufferedReader br = new BufferedReader(new FileReader("src/Intersection.csv"));
            while ((thisLine = br.readLine()) != null) // returns a Boolean value
            {
                i++;
                String[][] newdata = new String[i][2];// create new array for data

                String lineContent[] = thisLine.split(",");// get contents of line as an array
                newdata[i - 1] = lineContent;// add new line to the array

                System.arraycopy(data, 0, newdata, 0, i - 1);// copy previously read values to new array
                data = newdata;// set new array as csv data
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Print phases.csv
        for (String[] strings : data) {
            for (String string : strings) {
                System.out.print(string + " ");
            }
            System.out.println();
        }

        // Column Names
        String[] columnNames = {"Phases", "Duration"};

        // Initializing the JTable
        phasesTable = new JTable(data, columnNames);
        phasesTable.getColumnModel().getColumn(0).setMaxWidth(80);
        phasesTable.getColumnModel().getColumn(1).setMaxWidth(80);

    }

//    public static void StatisticsTable() {
//        String data[][] = {{"S1", "600s", "2000m", "20s"},
//        {"S2", "60s", "300m", "10s"},
//        {"S3", "300s", "1500m", "15s"},
//        {"S4", "40s", "100m", "10s"},};
//        String[] columnNames = {"Segment", "Waiting Time", "Waiting Length", "Cross Time"};
//        statisticsTable = new JTable();
//        statisticsTable = new JTable(data, columnNames);
//    }
    public static void addVehiclesTable() {
        String[] colHeadings = {"Vehicle", "Type", "Crossing Time", "Direction", "Length", "Emission", "Status", "Segment"};
        int numRows = 1;
        DefaultTableModel model = new DefaultTableModel(numRows, colHeadings.length);
        model.setColumnIdentifiers(colHeadings);
        JTable table = new JTable(model);
        JButton Add = new JButton("Add Vehicles");
        Add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //write to file
                try {
                    FileWriter writer = new FileWriter("src/Vehicles.csv", true);
                    for (int i = 0; i < table.getRowCount(); i++) {
                        for (int j = 0; j < table.getColumnCount(); j++) {
                            if (table.isEditing()) {
                                table.getCellEditor().stopCellEditing();
                            }
                            //write
                            if (j <= 6) {
                                writer.append(table.getValueAt(i, j).toString() + ",");
                            }
                            if (j == 7) {
                                writer.append(table.getValueAt(i, j).toString());
                            }
                        }
                        writer.append("\n");//record per line 
                    }
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                Object[] rowData = new Object[table.getColumnCount()];
                for (int i = 0; i < table.getColumnCount(); i++) {
                    rowData[i] = table.getValueAt(0, i);
                }
                vehiclesModel.addRow(rowData);
            }
        });
        JButton Exit = new JButton("Exit");
        Exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addVehiclesFrame.dispose();
            }
        });
        addVehiclesFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //column 0
        c.gridx = 0;

        // row 0
        c.gridy = 0;
        // c.gridwidth = 2;
        addVehiclesFrame.add(new JScrollPane(table), c);
        //column 0
        c.gridx = 0;

        // row 1
        c.gridy = 1;
        c.gridwidth = 0;
        addVehiclesFrame.add(Add, c);
        //column 0
        c.gridx = 1;

        // row 2
        c.gridy = 1;
        c.gridwidth = 0;
        addVehiclesFrame.add(Exit, c);
    }
}
