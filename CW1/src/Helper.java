import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Helper {

	public boolean checkNull(String text) {
		if (text.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkNumberValid(String text) {
		try {
			Float.parseFloat(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean checkDuplicate(String plateNumber, LinkedList<Phases> phaseList) {
		for (Phases phase : phaseList) {
			LinkedList<Vehicles> cars = phase.getLinkedList();
			for (Vehicles car : cars) {
				String carPlateNumber = car.getPlateNumber();
				if (plateNumber.equals(carPlateNumber)) {
					return true;
				}
			}
		}
		return false;
	}

	public synchronized void evaluateVehicleFile(List<String> vehicleLine, LinkedList<Phases> phaseList)
			throws InaccurateDataException, DuplicateIDException, NumberFormatException {
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
		} else if (checkNull(crossingTime)) {
			throw new InaccurateDataException("The Crossing Time cannot be empty");
		} else if (checkNull(vehicleType)) {
			throw new InaccurateDataException("The Vehicle Type cannot be empty");
		} else if (!((vehicleType.equals("car")) || (vehicleType.equals("bus") || (vehicleType.equals("truck"))))) {
			throw new InaccurateDataException("The Vehicle Type cannot be empty");
		} else if (checkNull(crossingStatus)) {
			throw new InaccurateDataException("The Crossing Status cannot be empty ");
		} else if (checkNull(emissionRate)) {
			throw new InaccurateDataException("The Vehicle Emissions cannot be empty");
		} else if (checkNull(vehicleLength)) {
			throw new InaccurateDataException("The Vehicle Length cannot be empty");
		} else if (!checkNumberValid(crossingTime)) {
			throw new NumberFormatException("The Crossing Time entry is not a number");
		} else if (!checkNumberValid(emissionRate)) {
			throw new NumberFormatException("The Vehicle Emissions is not a float.");
		} else if (!checkNumberValid(segment)) {
			throw new NumberFormatException("The Segment is not a number");
		} else if (!((Float.parseFloat(crossingTime) < 10) && (Float.parseFloat(crossingTime) > 0))) {
			throw new NumberFormatException("Your Crossing Time Entry is not between 0 and 10s");
		} else if (!((Float.parseFloat(emissionRate) < 50) && (Float.parseFloat(emissionRate) > 0))) {
			throw new NumberFormatException("Your vehicle emission Entry is not between 0 and 50 " + emissionRate);
		} else if (!((Integer.parseInt(segment) < 5) && (Integer.parseInt(segment) > 0))) {
			throw new NumberFormatException("Your Segment Entry is not between 1 and 4");
		} else if (!((Float.parseFloat(vehicleLength) < 8) && (Float.parseFloat(vehicleLength)) > 0)) {
			throw new NumberFormatException("Your Vehicle Length is not between 0 and 8m");
		} else if (!((crossingStatus.equals("not crossed")) || (crossingStatus.equals("waiting")))) {
			throw new InaccurateDataException("Your Crossing status should either be 'crossed' or 'not crossed'");
		} else if (!((direction.equals("straight")) || (direction.equals("left")) || (direction.equals("right")))) {
			throw new InaccurateDataException("Your direction should either be straight, left or right");
		} else if (checkDuplicate(plateNumber, phaseList)) {
			throw new DuplicateIDException(plateNumber + ": This vehicle has a duplicate car plate number.");
		}
	}

	public synchronized void checkCarSegment(Vehicles car, GUIModel model) {
		synchronized (this) {
			String carSegment = car.getSegment();
			if (carSegment.equals("1")) {
				model.addToS1counter(1);
				model.addToS1WaitingLength(car.getVehicleLength());
				model.addToS1WaitingTime(car.getCrossingTime());
			}
			if (carSegment.equals("2")) {
				model.addToS2counter(1);
				model.addToS2WaitingLength(car.getVehicleLength());
				model.addToS2WaitingTime(car.getCrossingTime());
			}
			if (carSegment.equals("3")) {
				model.addToS3counter(1);
				model.addToS3WaitingLength(car.getVehicleLength());
				model.addToS3WaitingTime(car.getCrossingTime());
			}
			if (carSegment.equals("4")) {
				model.addToS4counter(1);
				model.addToS4WaitingLength(car.getVehicleLength());
				model.addToS4WaitingTime(car.getCrossingTime());
			}
		}

	}

	public synchronized void updateSegmentTable(String segment, GUIModel model) {
		if (segment.equals("1")) {
			String carsAtSegment = Integer.toString(model.getS1counter());
			String waitingTime = Float.toString(model.getS1WaitingTime());
			String waitingLength = Float.toString(model.getS1WaitingLength());
			String avgCrossSegment = Float.toString(model.getS1CrossTime() / 2f);
			model.updateTableModel(model.getStatsModel(), 0, 1, carsAtSegment);
			model.updateTableModel(model.getStatsModel(), 0, 2, waitingTime);
			model.updateTableModel(model.getStatsModel(), 0, 3, waitingLength);
			model.updateTableModel(model.getStatsModel(), 0, 4, avgCrossSegment);
		}
		if (segment.equals("2")) {
			String carsAtSegment = Integer.toString(model.getS2counter());
			String waitingTime = Float.toString(model.getS2WaitingTime());
			String waitingLength = Float.toString(model.getS2WaitingLength());
			String avgCrossSegment = Float.toString(model.getS2CrossTime() / 2f);
			model.updateTableModel(model.getStatsModel(), 1, 1, carsAtSegment);
			model.updateTableModel(model.getStatsModel(), 1, 2, waitingTime);
			model.updateTableModel(model.getStatsModel(), 1, 3, waitingLength);
			model.updateTableModel(model.getStatsModel(), 1, 4, avgCrossSegment);
		}
		if (segment.equals("3")) {
			String carsAtSegment = Integer.toString(model.getS3counter());
			String waitingTime = Float.toString(model.getS3WaitingTime());
			String waitingLength = Float.toString(model.getS3WaitingLength());
			String avgCrossSegment = Float.toString(model.getS3CrossTime() / 2f);
			model.updateTableModel(model.getStatsModel(), 2, 1, carsAtSegment);
			model.updateTableModel(model.getStatsModel(), 2, 2, waitingTime);
			model.updateTableModel(model.getStatsModel(), 2, 3, waitingLength);
			model.updateTableModel(model.getStatsModel(), 2, 4, avgCrossSegment);
		}
		if (segment.equals("4")) {
			String carsAtSegment = Integer.toString(model.getS4counter());
			String waitingTime = Float.toString(model.getS4WaitingTime());
			String waitingLength = Float.toString(model.getS4WaitingLength());
			String avgCrossSegment = Float.toString(model.getS4CrossTime() / 2f);
			model.updateTableModel(model.getStatsModel(), 3, 1, carsAtSegment);
			model.updateTableModel(model.getStatsModel(), 3, 2, waitingTime);
			model.updateTableModel(model.getStatsModel(), 3, 3, waitingLength);
			model.updateTableModel(model.getStatsModel(), 3, 4, avgCrossSegment);
		}
	}

	public synchronized void updateSegmentTable(GUIModel model) {
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
			model.updateModel(model.getStatsModel(), rowData.toArray());
		}
	}

	public synchronized Scanner readCsvFile(String filename) {
		InputStream inputFile = Helper.class.getResourceAsStream(filename);
		Scanner csvScanner = new Scanner(inputFile);
		csvScanner.useDelimiter(",");
		csvScanner.nextLine();
		return csvScanner;
	}

	public synchronized boolean findPhase(Vehicles car, LinkedList<Phases> listOfPhases) {
		String direction = car.getCrossingDirection();
		String segment = car.getSegment();
		if (((direction.equals("straight")) || (direction.equals("right"))) && (segment.equals("1"))) {
			listOfPhases.get(1).getLinkedList().add(car);
			return true;
		} else if (((direction.equals("straight")) || (direction.equals("right"))) && (segment.equals("2"))) {
			listOfPhases.get(3).getLinkedList().add(car);
			return true;
		} else if (((direction.equals("straight")) || (direction.equals("right"))) && (segment.equals("3"))) {
			listOfPhases.get(5).getLinkedList().add(car);
			return true;
		} else if (((direction.equals("straight")) || (direction.equals("right"))) && (segment.equals("4"))) {
			listOfPhases.get(7).getLinkedList().add(car);
			return true;
		} else if ((direction.equals("left") && (segment.equals("1")))) {
			listOfPhases.get(4).getLinkedList().add(car);
			return true;
		} else if ((direction.equals("left") && (segment.equals("2")))) {
			listOfPhases.get(6).getLinkedList().add(car);
			return true;
		} else if ((direction.equals("left") && (segment.equals("3")))) {
			listOfPhases.get(0).getLinkedList().add(car);
			return true;
		} else if ((direction.equals("left") && (segment.equals("4")))) {
			listOfPhases.get(2).getLinkedList().add(car);
			return true;
		} else {
			return false;
		}
	}

	public synchronized Vehicles createVehicle(List<String> csvFileLine, LinkedList<Phases> phaseList) {
		// Extract variables from csv File
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

	public synchronized Phases createPhase(List<String> csvFileLine) {
		// Extract variables from csv File
		String phaseName = csvFileLine.get(0);
		float phaseTimer = Float.parseFloat(csvFileLine.get(1));
		Phases phase = new Phases();
		TrafficController controller = new TrafficController();
		phase.setPhaseName(phaseName);
		phase.setPhaseTimer(phaseTimer);
		phase.setLinkedList();
		phase.setCrossedLinkedList();
		phase.setTrafficController(controller);
		return phase;
	}

	public synchronized LinkedList<Phases> readPhasesFile(String filename) {
		if (filename == null || filename == "") {
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
				// populate Phases table
				Phases phase = createPhase(listSplitLine);
				phaseList.add(phase);

			}
			return phaseList;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return null;

		}
	}

	public synchronized ArrayList<String> randomlyGenerateVehicles() {
		ArrayList<String> vehicleDetails = new ArrayList<String>();

		String plateNumber = generatePlateNumber();
		String vehicleType = generateVehicleType();
		String crossingTime = generateCrossingTime();
		String direction = generateCrossingDirection();
		String crossingStatus = generateCrossingStatus();
		String emissionRate = generateEmissions();
		String vehicleLength = generateVehicleLength();
		String segment = generateVehicleSegment();

		vehicleDetails.add(plateNumber);
		vehicleDetails.add(vehicleType);
		vehicleDetails.add(crossingTime);
		vehicleDetails.add(direction);
		vehicleDetails.add(crossingStatus);
		vehicleDetails.add(emissionRate);
		vehicleDetails.add(vehicleLength);
		vehicleDetails.add(segment);

		return vehicleDetails;
	}

	private synchronized String generatePlateNumber() {
		Random random = new Random();
		int aMax = 90;
		int aMin = 65;

		String plateOpening = "";
		String plateNumerals = "";
		String plateEnding = "";
		for (int i = 0; i < 3; i++) {
			int genLetter = random.nextInt((aMax - aMin) + 1) + aMin;
			plateOpening += (char) genLetter;
		}
		for (int i = 0; i < 3; i++) {
			int genNumber = random.nextInt(10);
			plateNumerals += String.valueOf(genNumber);
		}
		for (int i = 0; i < 3; i++) {
			int genLetter = random.nextInt((aMax - aMin) + 1) + aMin;
			plateEnding += (char) genLetter;
		}
		String plateNumber = plateOpening + plateNumerals + plateEnding;
		return plateNumber;
	}

	private Random generateRandomSeed() {
		Random random = new Random();
//		random.setSeed(seed);
		return random;
	}

	private String generateVehicleType() {
		String[] vehicleTypes = { "bus", "car", "truck" };
		Random random = generateRandomSeed();
		int index = random.nextInt(3);
		String vehicleType = vehicleTypes[index];
		return vehicleType;
	}

	private String generateCrossingDirection() {
		String[] crossingDirections = { "straight", "left", "right" };
		Random random = generateRandomSeed();
		int index = random.nextInt(3);
		String crossingDirection = crossingDirections[index];
		return crossingDirection;
	}

	private String generateCrossingTime() {

		Random random = generateRandomSeed();
		int aMax = 5;
		int aMin = 1;
		float crossingTime = aMin + random.nextFloat() * (aMax - aMin);
		return "" + crossingTime;
	}

	private String generateCrossingStatus() {
		String crossingStatus = "not crossed";
		return crossingStatus;
	}

	private String generateEmissions() {
		Random random = generateRandomSeed();
		int aMax = 50;
		int aMin = 1;
		float emissions = aMin + random.nextFloat() * (aMax - aMin);
		return "" + emissions;
	}

	private String generateVehicleLength() {
		Random random = generateRandomSeed();
		int aMax = 8;
		int aMin = 1;
		float vehicleLength = aMin + random.nextFloat() * (aMax - aMin);
		return "" + vehicleLength;
	}

	private String generateVehicleSegment() {
		String[] vehicleSegments = { "1", "2", "3", "4" };
		Random random = generateRandomSeed();
		int index = random.nextInt(4);
		String vehicleType = vehicleSegments[index];
		return vehicleType;
	}

}
