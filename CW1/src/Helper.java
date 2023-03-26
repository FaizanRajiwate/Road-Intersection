import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Helper {
	
	
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
			throw new DuplicateIDException(plateNumber + ": This vehicle has a duplicate car plate number.");
		}
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
	}public boolean findPhase(Vehicles car, LinkedList<Phases> listOfPhases) {
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
	
	
	

}
