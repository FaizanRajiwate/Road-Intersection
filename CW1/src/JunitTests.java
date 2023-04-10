import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class JunitTests {

	@Nested
	class MainClassTest {
		@Test
		@DisplayName("Test for the Main StartTimer Should Return CurrentTime.")
		@Order(1)
		public void StartTimer_ShouldReturnCurrentTime() {
			String strExpectedStartingTime = String.valueOf(System.currentTimeMillis());
			String strActualStartingTime = String.valueOf(Main.startTimer());

			Assertions.assertEquals(strExpectedStartingTime.substring(0, 10), strActualStartingTime.substring(0, 10));
		}

		@Test
		@DisplayName("Test for the Main StartTimer Should not Return Null.")
		@Order(2)
		public void StartTimer_ShouldNotReturnNull() {
			long strActualStartingTime = Main.startTimer();
			Assertions.assertNotNull(strActualStartingTime);
		}

		@Test
		@DisplayName("Test for the Main Time Elapsed Should  Return 0.")
		@Order(3)
		public void TimeElapsed_ShouldReturnZero() {
			long lngActualStartingTime = Main.timeElapsed(System.currentTimeMillis());

			Assertions.assertEquals(0, lngActualStartingTime);

		}

		@Test
		@DisplayName("Test for the Main Time Elapsed Should not Return null.")
		@Order(4)
		public void TimeElapsed_ShouldNotReturnNull() {
			long lngActualStartingTime = Main.timeElapsed(System.currentTimeMillis());

			Assertions.assertNotNull(lngActualStartingTime);

		}

		@RepeatedTest(5)
		@DisplayName("Test for the Main Time Elapsed Should return Actual time elapsed.")
		@Order(5)
		public void TimeElapsed_ShouldReturnActualTimeElapsed() {
			long lngActualStartingTime = Main.timeElapsed(System.currentTimeMillis() - 10000);

			long lngExpectedStartingTime = 10; // 10 Milliseconds
			Assertions.assertEquals(lngExpectedStartingTime, lngActualStartingTime);

		}

		@Test
		@DisplayName("Test for the Main Time Elapsed Should return Actual time elapsed.")
		@Order(6)
		public void PhaseList_ShouldNotBeNull() {
			GUI gui = new GUI();
			LinkedList<Phases> phaseList = gui.readPhasesFile("phases.csv");
			Assertions.assertNotNull(phaseList);
		}

		@Test
		@DisplayName("Test for the Main Time Elapsed Should return Actual Waiting time.")
		@Order(7)
		public void AverageWaiting_ShouldReturnActualWaitingTime() {

			GUI gui = new GUI();
			// Total cars in CSV file is 0
			LinkedList<Phases> phaseList = gui.readPhasesFile("phases.csv");
			float ActualAverage = Main.averageWaiting(phaseList, 1);
			float ExpectedlAverage = Main.totalCarsForTest / 1;
			Assertions.assertEquals(ExpectedlAverage, ActualAverage);

		}

	}

	@Nested
	class VehicleClassClassTest {
		@Test
		@DisplayName("Test for the Total Vehicles Should not return null.")
		@Order(8)
		public void TotalVehicles_ShouldNotReturnNull() {
			GUI gui = new GUI();
			LinkedList<Phases> phaseList = gui.readPhasesFile("phases.csv");
			int intTotalVehicles = Main.totalVehicles(phaseList);

			Assertions.assertNotNull(intTotalVehicles);

		}

		@Test
		@DisplayName("Test for the Total Vehicles Should not return null.")
		@Order(9)
		public void TotalVehicles_ShouldReturnTotalCars() {
			GUI gui = new GUI();
			// Total cars in CSV file is 0
			LinkedList<Phases> phaseList = gui.readPhasesFile("phases.csv");
			int intExpectedTotalCars = 0;
			int intActualTotalCars = Main.totalVehicles(phaseList);

			Assertions.assertEquals(intExpectedTotalCars, intActualTotalCars);

		}

		@Test
		@DisplayName("Test for the Total Vehicles Should return Total Emissions.")
		@Order(10)

		public void ControlIntersections_ShouldReturnTotalEmission() {
			// the test will be enabled when the method fixes the exception being caused
			// after calling
			// Will be disabled for the time being
			GUI gui = new GUI();
			// Total cars in CSV file is 0
			LinkedList<Phases> phaseList = gui.readPhasesFile("phases.csv");

			LinkedList<Float> flTotalEmission = Main.controlIntersections(phaseList, 1000);
			Assertions.assertNotNull(flTotalEmission.getFirst());
			Assertions.assertNotNull(flTotalEmission.getLast());

		}

		@Test
		@DisplayName("Test for the Creating Vehicle Should create Vehicle object.")
		@Order(12)

		public void CreateVehicle_ShouldReturnVehicleObject() {
			GUI gui = new GUI();
			List<String> strList;
			strList = CreateTestDataVehicle();
			LinkedList<Phases> phaseList = null;
			Object car = gui.createVehicle(strList, phaseList);
			Assertions.assertTrue(car instanceof Vehicles);

		}

		@RepeatedTest(5)
		@DisplayName("Test for the Creating Vehicle Should match supplied Vehicle object.")
		@Order(13)

		public void CreateVehicle_ShouldMatchSuppliedVehicleObject() {
			GUI gui = new GUI();
			LinkedList<Phases> phaseList = null;
			List<String> strList;
			strList = CreateTestDataVehicle();
			Vehicles car = gui.createVehicle(strList, phaseList);
			String strActualCrossingDirection = car.getCrossingDirection();
			String strActualCrossingStatus = car.getCrossingStatus();

			String strExpectedCrossingDirection = "left";
			String strExpectedCrossingStatus = "not crossed";

			Assertions.assertEquals(strExpectedCrossingDirection, strActualCrossingDirection);
			Assertions.assertEquals(strExpectedCrossingStatus, strActualCrossingStatus);

		}

		@Test
		@DisplayName("Test for the Creating Vehicle Should not return Null Vehicle object.")
		@Order(14)

		public void CreateVehicle_ShouldNotReturnNullVehicleObject() {
			GUI gui = new GUI();
			List<String> strList;
			LinkedList<Phases> phaseList = null;
			strList = CreateTestDataVehicle();
			Vehicles car = gui.createVehicle(strList, phaseList);

			Assertions.assertNotNull(car);

		}

	}

	@Nested
	class PhaseClassTest {

		@Test
		@DisplayName("Test for the Creating Phase Should create Phase object.")
		@Order(15)

		public void CreatePhase_ShouldReturnPhaseObject() {
			GUI gui = new GUI();
			List<String> strList;
			strList = CreateTestDataPhase();
			Object phase = gui.createPhase(strList);
			Assertions.assertTrue(phase instanceof Phases);

		}

		@Test
		@DisplayName("Test for the Creating Phase Should not return Null Phase object.")
		@Order(16)

		public void CreatePhase_ShouldNotReturnNullPhaseObject() {
			GUI gui = new GUI();
			List<String> strList;
			strList = CreateTestDataPhase();
			Object phase = gui.createPhase(strList);
			Assertions.assertNotNull(phase);

		}

		@RepeatedTest(5)
		@DisplayName("Test for the Creating Vehicle Should match supplied Vehicle object.")
		@Order(17)

		public void CreatePhase_ShouldMatchSuppliedPhaseObject() {
			GUI gui = new GUI();
			List<String> strList;
			strList = CreateTestDataPhase();
			Phases phase = gui.createPhase(strList);
			Assertions.assertNotNull(phase);
			String strActualPhaseName = phase.getPhaseName();
			Float fltActualPhaseTimer = phase.getPhaseTimer();

			String strExpectedPhaseName = "P8";
			Float fltExpectedPhaseTimer = 30.0f;

			Assertions.assertEquals(strExpectedPhaseName, strActualPhaseName);
			Assertions.assertEquals(fltExpectedPhaseTimer, fltActualPhaseTimer);

		}

	}

	@Nested
	class FileReadTest {

		@Test
		@DisplayName("Test for ReadCsv File Should Return Scanner Object.")
		@Order(18)

		public void ReadCsvFileShouldReturnScannerObject() {
			GUI gui = new GUI();
			Object scanner = gui.readCsvFile("vehicles.csv");
			Assertions.assertTrue(scanner instanceof Scanner);

		}

		@Test
		@DisplayName("Test for ReadCsv File Should Return Scanner Object.")
		@Order(19)

		public void ReadCsvFileShouldReturnFilledScannerObject() {
			GUI gui = new GUI();
			Object scanner = gui.readCsvFile("vehicles.csv");
			Assertions.assertEquals("class java.util.Scanner", scanner.getClass().toString());

		}

		@Test
		@DisplayName("Test for ReadCsv File Should throw null pointer exception when file name is null")
		@Order(20)

		public void ReadCsvFileShouldThrowNullPointerException() {
			GUI gui = new GUI();
			Assertions.assertThrows(NullPointerException.class, () -> gui.readCsvFile(null));
		}

		@Test
		@DisplayName("Test for ReadCsv File Should  NOT throw null pointer exception when file name is empty")
		@Order(21)

		public void ReadCsvFileShouldNotThrowNullPointerExceptionWhenFileNameIsEmpty() {
			GUI gui = new GUI();
			Assertions.assertDoesNotThrow(() -> gui.readCsvFile(""), "The Exception is already handled in the code");
		}

		// readCsvFile

		@Test
		@DisplayName("Test for ReadCsv phase File Should Return LinkedListofString Object.")
		@Order(22)

		public void ReadPhasesFileShouldReturnLinkedListofString() {
			LinkedList<Phases> phases = null;
			GUI gui = new GUI();
			phases = gui.readPhasesFile("phases.csv");
			Assertions.assertTrue(phases instanceof LinkedList<Phases>);

		}

		@Test
		@DisplayName("Test for ReadCsv File Should Return Filled LinkedList Object.")
		@Order(23)

		public void ReadPhaseFileShouldReturnFilledLinkedListObject() {
			LinkedList<Phases> phases = null;
			GUI gui = new GUI();
			phases = gui.readPhasesFile("phases.csv");

			Integer intExpectedLinedListSize = 8;
			Integer intActualLinedListSize = phases.size();

			Assertions.assertEquals(intExpectedLinedListSize, intActualLinedListSize);

		}

		@Test
		@DisplayName("Test for ReadCsv Phase File Should throw null pointer exception when file name is null")
		@Order(24)

		public void ReadPhaseFileShouldThrowNullPointerException() {
			GUI gui = new GUI();
			Assertions.assertThrows(NullPointerException.class, () -> gui.readPhasesFile(null));
		}

		@Test
		@DisplayName("Test for ReadCsv Phase File Should  NOT throw null pointer exception when file name is empty")
		@Order(25)

		public void ReadCsvPaseFileShouldThrowNullPointerExceptionWhenFileNameIsEmpty() {

			GUI gui = new GUI();
			Assertions.assertThrows(NullPointerException.class, () -> gui.readPhasesFile(""));
		}

	}

	@Nested
	class FindPhaseTest {

		@Test
		@DisplayName("Test for FindPhase Should Not Throw Exception When Vehicle And List Of Phases Supplied")
		@Order(29)

		public void FindPhaseShouldNotThrowException_WhenVehicleAndListOfPhasesSupplied() {
			LinkedList<Phases> listOfPhases;
			GUI gui = new GUI();
			LinkedList<Phases> phaseList = null;
			listOfPhases = gui.readPhasesFile("phases.csv");

			List<String> strListVehicle;
			strListVehicle = CreateTestDataVehicle();
			Vehicles car = gui.createVehicle(strListVehicle, phaseList);
			car.setCrossingDirection("straight");
			car.setSegment("1");
			gui.findPhase(car, listOfPhases);
			Assertions.assertDoesNotThrow(() -> gui.findPhase(car, listOfPhases));
		}

		@Test
		@DisplayName("Test for Find Phase Should Return True When Vehicle Is Added To List")
		@Order(30)

		public void FindPhaseShouldReturnTrue_WhenVehicleIsAddedToList() {
			LinkedList<Phases> listOfPhases;
			LinkedList<Phases> phaseList = null;
			GUI gui = new GUI();
			listOfPhases = gui.readPhasesFile("phases.csv");

			List<String> strListVehicle;
			strListVehicle = CreateTestDataVehicle();
			Vehicles car = gui.createVehicle(strListVehicle, phaseList);
			car.setCrossingDirection("straight");
			car.setSegment("1");
			gui.findPhase(car, listOfPhases);
			boolean blnExpected = true;
			boolean blnActual = gui.findPhase(car, listOfPhases);
			Assertions.assertEquals(blnExpected, blnActual);
		}

		@Test
		@DisplayName("Test for ReadCsv Phase File Should throw null pointer exception when Vehicle And List Of Phases Is Null")
		@Order(26)

		public void FindPhaseShouldThrowException_WhenVehicleAndListOfPhasesIsNull() {
			GUI gui = new GUI();
			Assertions.assertThrows(NullPointerException.class, () -> gui.findPhase(null, null));
		}

		@Test
		@DisplayName("Test for ReadCsv Phase File Should throw null pointer exception when List Of Phases Is Null")
		@Order(27)

		public void FindPhaseShouldThrowException_WhenListOfPhasesIsNull() {
			GUI gui = new GUI();
			List<String> strList;
			LinkedList<Phases> phaseList = null;
			strList = CreateTestDataVehicle();
			Vehicles car = gui.createVehicle(strList, phaseList);
			Assertions.assertThrows(NullPointerException.class, () -> gui.findPhase(car, null));
		}

		@Test
		@DisplayName("Test for ReadCsv Phase File Should throw null pointer exception when Vehicle Is Null")
		@Order(28)

		public void FindPhaseShouldThrowException_WhenVehicleIsNull() {
			GUI gui = new GUI();

			List<String> strList;
			strList = CreateTestDataPhase();
			LinkedList<Phases> listOfPhases = new LinkedList<Phases>();
			Phases phase = gui.createPhase(strList);
			listOfPhases.add(phase);
			Assertions.assertThrows(NullPointerException.class, () -> gui.findPhase(null, listOfPhases));
		}

	}

	@Nested
	class CheckDuplicateTest {

		@Test
		@DisplayName("Test for check Duplicate Should Return False For Supplied Data")
		@Order(31)

		public void checkDuplicateShouldReturnFalseForSuppliedData() {
			LinkedList<Phases> listOfPhases;
			GUI gui = new GUI();
			listOfPhases = gui.readPhasesFile("phases.csv");

			boolean blnExpected = false;
			boolean blnActual = gui.checkDuplicate("ABC123XZ", listOfPhases);
			Assertions.assertEquals(blnExpected, blnActual);
		}

	}

	public List<String> CreateTestDataVehicle() {
		List<String> listSplitLine = null;
		Scanner csvScanner = readCsvFile("vehicles.csv");
		while (csvScanner.hasNext()) {
			try {

				String line = csvScanner.nextLine();
				String[] splitLine = line.split(",");

				listSplitLine = Arrays.asList(splitLine);

				// Vehicles car = createVehicle(listSplitLine);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return listSplitLine;
	}

	public List<String> CreateTestDataPhase() {

		List<String> listSplitLine = null;
		Scanner csvScanner = readCsvFile("phases.csv");
		while (csvScanner.hasNext()) {

			String line = csvScanner.nextLine();
			String[] splitLine = line.split(",");
			listSplitLine = Arrays.asList(splitLine);
			// populate Phases table
			// Phases phase = createPhase(listSplitLine);
		}
		return listSplitLine;

	}

	public Scanner readCsvFile(String filename) {
		try {
			Scanner csvScanner = new Scanner(new File(filename));
			csvScanner.useDelimiter(",");
			csvScanner.nextLine();
			return csvScanner;
		} catch (FileNotFoundException e) {
			System.out.println("The file you've selected does not exist in the src directory.");
			return null;
		}
	}

}
