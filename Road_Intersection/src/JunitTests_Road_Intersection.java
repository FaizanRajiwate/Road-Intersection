
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;

@TestMethodOrder(OrderAnnotation.class)
public class JunitTests_Road_Intersection {
	@Nested
	class clsVehiclesTest{
		@Test
		@DisplayName("Test for the null file Exception for Vehicles table.")
		@Order(1)
		public void NullFile_ShouldThrowNullPointerException_VehiclesTable()
		{	
			String strVehicleFile = null;
			Assertions.assertThrows(NullPointerException.class, ()-> Vehicles.VehiclesTable(strVehicleFile));
		}
		
		@Test
		@DisplayName("Test for not throwing null file Exception for Vehicles table.")
		@EnabledIf("VehicleFileExists")
		@Order(2)
		public void NullFile_ShouldNotThrowNullPointerException_VehiclesTable()
		{	
			String strVehicleFile = "src/Vehicles.csv";
			
			Assertions.assertDoesNotThrow(()->Vehicles.VehiclesTable(strVehicleFile),"Exception Handled");
		}
		
		
		@Test
		@DisplayName("Test for the File Not Found for Vehicles table. Stop processing and return")
		@Order(3)
		public void FileUnavailable_ShouldReturnFalseAndStop_VehiclesTable()
		{	
			String strVehicleFile = "";
			
			assertTrue(!Vehicles.VehiclesTable(strVehicleFile));
		}
		
		@Test
		@DisplayName("Test for the File Found for Vehicles table. Continue processing and return")
		@EnabledIf("VehicleFileExists")
		@Order(4)
		public void FileAvailable_ShouldContinueProcessing_VehiclesTable()
		{	
			String strVehicleFile = "src/Vehicles.csv";
			
			assertTrue(Vehicles.VehiclesTable(strVehicleFile));
		}
		
		@Test
		@DisplayName("Test for not throwing File Not Found Exception for Vehicles table,Because it is handled in the method itself.")
		@Order(5)
		public void FileAvailable_ShouldNotThrowFileNotFoundException_VehiclesTable()

		{	
			String strVehicleFile = "";
			
			Assertions.assertDoesNotThrow(()->Vehicles.VehiclesTable(strVehicleFile),"Exception Handled");
		}

		boolean VehicleFileExists() {
			File fileVehicles = new File("src/Vehicles.csv");
			return fileVehicles.exists();
			}
		
	}
	  
	@Nested
	class clsPhaseTablesTest{
		@Test
		@DisplayName("Test for the null file Exception for Phases table.")
		@Order(6)
		public void NullFile_ShouldThrowNullPointerException_PhasesTable()
		{	
			String strPhasesFile = null;
			
			Assertions.assertThrows(NullPointerException.class, ()->Vehicles.PhasesTable(strPhasesFile));
		}
		
		@Test
		@DisplayName("Test for not throwing null file Exception for Phases table.")
		@EnabledIf("PhaseTableFileExists")
		@Order(7)
		public void NullFile_ShouldNotThrowNullPointerException_PhasesTable()
		{	
			String strPhasesFile = "src/Intersection.csv";
			
			Assertions.assertDoesNotThrow(()->Vehicles.PhasesTable(strPhasesFile),"Exception Handled");
		}

		@Test
		@DisplayName("Test for the File Not Found for Phases table. Stop processing and return")
		@Order(8)
		public void FileUnavailable_ShouldReturnFalseAndStop_PhasesTable()
		{	
			String strPhasesFile = "";
			
			assertTrue(!Vehicles.PhasesTable(strPhasesFile));
		}

		@Test
		@DisplayName("Test for the File Found for Phases table. Continue processing and return")
		@EnabledIf("PhaseTableFileExists")
		@Order(9)
		public void FileAvailable_ShouldContinueProcessing_PhasesTable()
		{	
			String strPhasesFile = "src/Intersection.csv";
			
			assertTrue(Vehicles.PhasesTable(strPhasesFile));
		}
		
		
		@Test
		@DisplayName("Test for not throwing File Not Found Exception for Phases table,Because it is handled in the method itself.")
		@Order(10)
		public void FileAvailable_ShouldNotThrowFileNotFoundException_PhasesTable()

		{	
			String strPhasesFile = "";
			
			Assertions.assertDoesNotThrow(()->Vehicles.PhasesTable(strPhasesFile),"Exception Handled");
		}
		
		boolean PhaseTableFileExists() {
			File fileVehicles = new File("src/Intersection.csv");
			return fileVehicles.exists();
			}
		
	}
	
	@Nested
	class clsStatisticsTableTest{
		@Test
		@DisplayName("Test for the null value of Statistics table."	)
		@Order(11)
		public void LoadData_StatisticsTable_NullValues()
		{	
			String[][] statisticsTableUnderTest = null;
			String[][] statisticsTableActual = Vehicles.StatisticsTable();
			Assertions.assertNotEquals(statisticsTableUnderTest, statisticsTableActual);
			
		}
		
		@Test
		@DisplayName("Test for the data of Statistics table."	)
		@Order(12)
		public void LoadData_StatisticsTable()
		{	
			String statisticsTableUnderTest[][] = {{"S1", "600s", "2000m", "20s"},
			        {"S2", "60s", "300m", "10s"},
			        {"S3", "300s", "1500m", "15s"},
			        {"S4", "40s", "100m", "10s"},};
			        
					String  statisticsTableActual[][] = Vehicles.StatisticsTable();
					Assertions.assertArrayEquals(statisticsTableUnderTest, statisticsTableActual);
			
		}
		
	}
	
	@Nested
	class clsAddVehiclesTableTests{
		@Test
		@DisplayName("Test for the null file Exception for Vehicles table.")
		@Order(15)
		public void NullFile_ShouldThrowNullPointerException_AddVehiclesTable()
		{	
			String strVehicleFile = null;
			Assertions.assertThrows(NullPointerException.class, ()-> Vehicles.AddDataIntoVehicleTable(strVehicleFile));
		}
		
		@Test
		@DisplayName("Test for not throwing null file Exception for Vehicles table.")
		@EnabledIf("VehicleFileExists")
		@Order(13)
		
		public void NullFile_ShouldNotThrowNullPointerException_AddVehiclesTable()
		{	
			String strVehicleFile = "src/Vehicles.csv";
			Vehicles.setGUIAddVehicles();
			Vehicles.addActionListenersAndSetFrame();
	        		
			Assertions.assertDoesNotThrow(()->Vehicles.AddDataIntoVehicleTable(strVehicleFile),"Exception Handled");
		}
		
		@Test  
		@DisplayName("Test for checking if vehicles.csv is updated.")
		@EnabledIf("VehicleFileExists")
		@Order(14)
		@Disabled
		public void WriteFile_ShouldAddVehicleDetails_VehiclesTable() throws IOException
		{	try
			{
			String strVehicleFile = "src/Vehicles.csv";
			List<String> listOfStrings
	        = new ArrayList<String>();
			BufferedReader bf = new BufferedReader(
			        new FileReader(strVehicleFile));
			String line = bf.readLine();
			while (line != null) {
		        listOfStrings.add(line);
		        line = bf.readLine();
		    }
			bf.close();
			String[] array
	        = listOfStrings.toArray(new String[0]);
			System.out.println("Length of the array is" + array.length);
			System.out.println(array);
			
			
			Vehicles.setGUIAddVehicles();
			Vehicles.addActionListenersAndSetFrame();
	        
			Vehicles.AddDataIntoVehicleTable(strVehicleFile);
			
		   //	Assertions.assertEquals(1, lngComparator);
			}
		catch(FileNotFoundException ex){
			ex.printStackTrace();
		     }
			
		}
		boolean VehicleFileExists() {
			File fileVehicles = new File("src/Vehicles.csv");
			return fileVehicles.exists();
			}

	}

	
	
}
