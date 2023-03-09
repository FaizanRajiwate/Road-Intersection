import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;
import java.lang.Float;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;


public class Main{		
	
	public static long startTimer() {
		long startingTime = System.currentTimeMillis();
		return startingTime;
	}
	
	public static long timeElapsed(long startingTime) {
		//This function takes the starting time of the program, and calculates the elapsed time since.
		//The input is the starting time, it returns the elapsed time which is also an integer (long)
		long timeElapsed;
		long currentTime = System.currentTimeMillis();
	    timeElapsed = currentTime - startingTime;
		long elapsedTimeInSeconds = timeElapsed / 1000;
		return elapsedTimeInSeconds;
	}
	
	public static float averageWaiting(LinkedList<Phases> phaseList, float executionTime) {
		//This function approximates the waiting time, by determining 
		//how many cars crossed in the amount of time the program was run for
		//It receives as input a linkedlist comprising of various phases known as phaselist and 
		//how long the program was executed for before being shut down. 
		//it returns a flost which determines how long each car that passed waited for.
		float totalCars = 0;
		for (Phases phase : phaseList) {
			LinkedList<Vehicles> exitedCars = phase.getCrossedLinkedList();
			totalCars += exitedCars.size();
			System.out.println(totalCars);
		}
		System.out.println(totalCars); 
		return totalCars / executionTime;
	}
	
	public static int totalVehicles(LinkedList<Phases> phaseList) {
		int totalCars = 0;
		for (Phases phase: phaseList) {
			totalCars += phase.getCrossedLinkedList().size();
		}
		return totalCars;
	}
	public static float controlIntersections(LinkedList<Phases> phaseList, long executionTime) {
		//execution Time is the amount of time the program was kept running for before being exited
		//phaseList is the list of phases available at the intersection
		float unusedTime = 0f;
		float waitTime = 0f;
		float totalEmissions = 0f;
		while (executionTime > 0) {
			for (Phases phase: phaseList) {
				System.out.println("Currently Working on " + phase.getPhaseName());
				float phaseTimer = phase.getPhaseTimer();
				LinkedList<Vehicles> queuedVehicles = phase.getLinkedList();
				LinkedList<Vehicles> crossedVehicles = phase.getCrossedLinkedList();
				if (executionTime <= 0) {
					break;
				}
				else if (executionTime < phaseTimer) {
					phaseTimer = executionTime;	
				}
				executionTime -= phaseTimer;
				float phaseWaitTime = 0f;
				while (phaseTimer > 0) {
					try {
						Vehicles currCar = queuedVehicles.get(0);
						float currCarTime = currCar.getCrossingTime();
						waitTime += currCarTime;
						if (phaseTimer >= currCarTime) {
							crossedVehicles.add(currCar);
							queuedVehicles.remove(currCar);
							phaseWaitTime += currCar.getCrossingTime();
							float carEmissions = currCar.calculateEmissions(waitTime);
							totalEmissions += carEmissions;
							//calculate emissions
							phaseTimer -= currCarTime;
						}else {
							System.out.println(currCar.getPlateNumber() + " cannot cross due to inadequate time");
							unusedTime += phaseTimer;
							break;
						}
					}catch (IndexOutOfBoundsException e){
						System.out.println(phase.getPhaseName() + " is currently empty");
						System.out.println("Remaining Execution Time: " + executionTime);
						break;
					}
				
				}
			}
		}
		return totalEmissions;
	}
	
	public static void main(String Args[]) {
		long startingTime = startTimer();
		GUI mainWindow = new GUI();
		LinkedList<Phases> phaseList = mainWindow.readPhasesFile("phases.csv");
		mainWindow.setLayout(new GridLayout(7,3,20,0));
		Font font = new Font("Courier", Font.BOLD, 20);
		JLabel vehicleLabel = mainWindow.addLabels("Vehicles");
		JLabel phaseLabel = mainWindow.addLabels("Phases");
		JLabel segmentLabel = mainWindow.addLabels("Segments");
		vehicleLabel.setFont(font);
		phaseLabel.setFont(font);
		segmentLabel.setFont(font);
		JPanel labelPane = new JPanel();
		labelPane.setLayout(new FlowLayout(FlowLayout.CENTER,650,20));
		labelPane.add(vehicleLabel);
		labelPane.add(phaseLabel);
//		labelPane.add(segmentLabel);
		mainWindow.add(labelPane);

		//Pane for Tables
		JPanel tableDisplayPanel = mainWindow.tablesDisplayPanel(phaseList);
		mainWindow.add(tableDisplayPanel);
		JLabel emptyLabel = new JLabel();
		mainWindow.add(emptyLabel);
		System.out.println("Table Created");
		//Create Add Vehicle Form
		JPanel addVehicle = mainWindow.addTableDisplayPanel(phaseList);
		mainWindow.add(addVehicle);
		
		

		JLabel emptyLabel2 = new JLabel();
		mainWindow.add(emptyLabel2);
		JLabel emptyLabel3 = new JLabel();
		mainWindow.add(emptyLabel3);
		mainWindow.pack();
//		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				long elapsedTime = timeElapsed(startingTime);
				float totalEmissions = controlIntersections(phaseList, elapsedTime);
				float waitingTimeAverage = averageWaiting(phaseList, elapsedTime);
				int totalCarsCrossed = totalVehicles(phaseList);
				System.out.println("Done.\nWaiting Average: " + waitingTimeAverage);
				ReportFile file = new ReportFile();
				try {
					FileWriter writingFile = file.writeToFile("report.txt");
					if (writingFile == null) {
						System.out.println("This file cannot be written to!");
					}else {
						writingFile.write("The Average Waiting Time per car is: " + waitingTimeAverage + "\n");
						writingFile.write("The total Emissions are: " + totalEmissions + "\n");
						writingFile.write("The total crossed vehicles are: " + totalCarsCrossed + "\n");
						writingFile.close();
					}
				}catch (IOException a) {
					System.out.println("The File could not be written");
				}
				
				JFrame alert = new JFrame();
				JOptionPane.showMessageDialog(alert, "Total Time Elapsed: " + elapsedTime);
				
				System.exit(0);
			}
			
		});
		
		
		
		

		
		 
		

//		mainWindow.add(buttonPanel);



		

//		//Add to Main Window
//		mainWindow.add(addVehicle);
//		
//		mainWindow.add(phasePanel);
//		
		
		
		
		
	
//		
//
//			
//		}
		//figure out details of report
		

		
	}
}

