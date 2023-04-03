import java.io.IOException;
import java.io.FileWriter;
import java.util.LinkedList;
import java.lang.Float;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;


public class Main{	
	static float totalCarsForTest = 0;
	
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
	
	public static float averageWaiting(LinkedList<Phases> phaseList, float waitTime) {
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
		totalCarsForTest = totalCars;
		return totalCars / waitTime;
	}
	
	public static int totalVehicles(LinkedList<Phases> phaseList) {
		//Find the number of vehicles that crossed successfully
		int totalCars = 0;
		for (Phases phase: phaseList) {
			totalCars += phase.getCrossedLinkedList().size();
		}
		return totalCars;
	}
	
	public static LinkedList<Float> exitedVehiclesPerPhase(LinkedList<Phases> phaseList) {
		//Find the number of vehicles which left each phase.
		//checks the list of phase objects which are included in order. 
		LinkedList<Float> exitedVehicleNumber = new LinkedList<Float>();
		for (Phases phase : phaseList) {
			LinkedList<Vehicles> exitedVehicles = phase.getCrossedLinkedList();
			float numberOfExitedVehicles = exitedVehicles.size();
			exitedVehicleNumber.add(numberOfExitedVehicles);
		}
		return exitedVehicleNumber;
	}
	
	public static LinkedList<Float> controlIntersections(LinkedList<Phases> phaseList, long executionTime) {
		//execution Time is the amount of time the program was kept running for before being exited
		//phaseList is the list of phases available at the intersection
		//The program iterates through each phase in the order of phase 1, phase 1, phase 3, phase 4 etc.... 
		//and checks the vehicles in for if they can move through in the respective allocated phase time, 
		//at the point where a vehicle cannot cross successfully, the active phase becomes the nest available phase.
		//This is done until the execution time becomes 0.
		float waitTime = 0f;
		float waitTotal = 0f;
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
//				float phaseWaitTime = 0f;
				while (phaseTimer > 0) {
					try {
						Vehicles currCar = queuedVehicles.get(0);
						float currCarTime = currCar.getCrossingTime();
						waitTime += currCarTime;
						waitTotal += waitTime;
						if (phaseTimer >= currCarTime) {
							crossedVehicles.add(currCar);
							queuedVehicles.remove(currCar);
//							phaseWaitTime += currCar.getCrossingTime();
							float carEmissions = currCar.calculateEmissions(waitTime);
							totalEmissions += carEmissions;
							//calculate emissions
							phaseTimer -= currCarTime;
						}else {
							System.out.println(currCar.getPlateNumber() + " cannot cross due to inadequate time");
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
		LinkedList<Float> waitTimeEmissionList = new LinkedList<Float>();
		waitTimeEmissionList.add(totalEmissions);
		waitTimeEmissionList.add(waitTotal);
		return waitTimeEmissionList;
	}
	
	public static void main(String Args[]) {
		GUIController controller = new GUIController(new GUIModel(), new GUIView(), new Helper() );
		GUIModel guimodelThread = new GUIModel(); 
		guimodelThread.start();
		Vehicles vehiclesThread = new Vehicles(); 
		vehiclesThread.start();
		Phases phasesThread = new Phases(); 
		phasesThread.start();
		
	}
}

