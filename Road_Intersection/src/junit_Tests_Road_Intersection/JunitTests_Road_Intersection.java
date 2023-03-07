package junit_Tests_Road_Intersection;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;


public class JunitTests_Road_Intersection {
	
	  @Rule
	  public ExpectedException exception = ExpectedException.none(); // has to be public
	
	@Test
	@DisplayName("Test for the null file Exception for Vehicles table.")
	public void NullFile_ShouldThrowNullPointerException_VehiclesTable()
	{	
		String strVehicleFile = null;
		Assertions.assertThrows(NullPointerException.class, ()-> Vehicle.VehiclesTableTest(strVehicleFile));
	}
	
	@Test
	@DisplayName("Test for not throwing null file Exception for Vehicles table.")
	public void NullFile_ShouldNotThrowNullPointerException_VehiclesTable()
	{	
		String strVehicleFile = "src/Vehicles.csv";
		
		Assertions.assertDoesNotThrow(()->Vehicle.VehiclesTableTest(strVehicleFile),"Exception Handled");
	}
	
	
	@Test
	@DisplayName("Test for the File Not Found for Vehicles table. Stop processing and return")
	public void FileUnavailable_ShouldReturnFalseAndStop_VehiclesTable()
	{	
		String strVehicleFile = "";
		
		assertTrue(!Vehicle.VehiclesTableTest(strVehicleFile));
	}
	
	@Test
	@DisplayName("Test for the File Found for Vehicles table. Continue processing and return")
	public void FileAvailable_ShouldContinueProcessing_VehiclesTable()
	{	
		String strVehicleFile = "src/Vehicles.csv";
		
		assertTrue(Vehicle.VehiclesTableTest(strVehicleFile));
	}
	
	@Test
	@DisplayName("Test for not throwing File Not Found Exception for Vehicles table,Because it is handled in the method itself.")
	public void FileAvailable_ShouldNotThrowFileNotFoundException_VehiclesTable()

	{	
		String strVehicleFile = "";
		
		Assertions.assertDoesNotThrow(()->Vehicle.VehiclesTableTest(strVehicleFile),"Exception Handled");
	}

	
	///////////// Phases table tests
	@Test
	@DisplayName("Test for the null file Exception for Phases table.")
	public void NullFile_ShouldThrowNullPointerException_PhasesTable()
	{	
		String strPhasesFile = null;
		
		Assertions.assertThrows(NullPointerException.class, ()->Vehicle.PhasesTableTest(strPhasesFile));
	}
	
	@Test
	@DisplayName("Test for not throwing null file Exception for Phases table.")
	public void NullFile_ShouldNotThrowNullPointerException_PhasesTable()
	{	
		String strPhasesFile = "src/Intersection.csv";
		
		Assertions.assertDoesNotThrow(()->Vehicle.PhasesTableTest(strPhasesFile),"Exception Handled");
	}

	@Test
	@DisplayName("Test for the File Not Found for Phases table. Stop processing and return")
	public void FileUnavailable_ShouldReturnFalseAndStop_PhasesTable()
	{	
		String strPhasesFile = "";
		
		assertTrue(!Vehicle.PhasesTableTest(strPhasesFile));
	}

	@Test
	@DisplayName("Test for the File Found for Phases table. Continue processing and return")
	public void FileAvailable_ShouldContinueProcessing_PhasesTable()
	{	
		String strPhasesFile = "src/Intersection.csv";
		
		assertTrue(Vehicle.PhasesTableTest(strPhasesFile));
	}
	
	
	@Test
	@DisplayName("Test for not throwing File Not Found Exception for Phases table,Because it is handled in the method itself.")
	public void FileAvailable_ShouldNotThrowFileNotFoundException_PhasesTable()

	{	
		String strPhasesFile = "";
		
		Assertions.assertDoesNotThrow(()->Vehicle.PhasesTableTest(strPhasesFile),"Exception Handled");
	}
	///////////// Phases table tests Ends
	
///////////// Statistics table tests
	@Test
	@DisplayName("Test for the null value of Statistics table."	)
	public void LoadData_StatisticsTable_NullValues()
	{	
		String[][] statisticsTableUnderTest = null;
		String[][] statisticsTableActual = Vehicle.StatisticsTableTest();
		Assertions.assertNotEquals(statisticsTableUnderTest, statisticsTableActual);
		
	}
	
	@Test
	@DisplayName("Test for the data of Statistics table."	)
	public void LoadData_StatisticsTable()
	{	
		String statisticsTableUnderTest[][] = {{"S1", "600s", "2000m", "20s"},
		        {"S2", "60s", "300m", "10s"},
		        {"S3", "300s", "1500m", "15s"},
		        {"S4", "40s", "100m", "10s"},};
		        
				String  statisticsTableActual[][] = Vehicle.StatisticsTableTest();
				Assertions.assertArrayEquals(statisticsTableUnderTest, statisticsTableActual);
		
	}
	
	
///////////// Statistics table tests Ends
	
	////////Add Data in the vehicle File
	@Test
	@DisplayName("Test for the null file Exception for Vehicles table.")
	public void NullFile_ShouldThrowNullPointerException_AddVehiclesTable()
	{	
		String strVehicleFile = null;
		Assertions.assertThrows(NullPointerException.class, ()-> Vehicle.AddDataIntoVehicleTable(strVehicleFile));
	}
	
	@Test
	@DisplayName("Test for not throwing null file Exception for Vehicles table.")
	public void NullFile_ShouldNotThrowNullPointerException_AddVehiclesTable()
	{	
		String strVehicleFile = "src/Vehicles.csv";
		Vehicle.setGUIAddVehicles();
		Vehicle.addActionListenersAndSetFrame();
        		
		Assertions.assertDoesNotThrow(()->Vehicle.AddDataIntoVehicleTable(strVehicleFile),"Exception Handled");
	}
	
	@Test  
	@DisplayName("Test for checking if vehicles.csv is updated.")
	public void WriteFile_ShouldAddVehicleDetails_VehiclesTable()
	{	try
		{
		String strVehicleFile = "src/Vehicles.csv";
		BufferedInputStream fis1 = new BufferedInputStream(new FileInputStream(strVehicleFile));
		
		Vehicle.setGUIAddVehicles();
		Vehicle.addActionListenersAndSetFrame();
        
		Vehicle.AddDataIntoVehicleTable(strVehicleFile);
		BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(strVehicleFile));
		long lngComparator = CompareBothFiles(fis1,fis2);
		Assertions.assertEquals(-1, lngComparator);
		}
	catch(FileNotFoundException ex){
		ex.printStackTrace();
	     }
		
	}
	
	private long CompareBothFiles(BufferedInputStream fis1, BufferedInputStream fis2)
	{
		try {
			
			int ch = 0;
	        long pos = 1;
	        while ((ch = fis1.read()) != -1) {
	            if (ch != fis2.read()) {
	                return pos;
	            }
	            pos++;
	        }
	        if (fis2.read() == -1) {
	            return -1;
	        }
	        else 
	        {
	            return pos;
	        }
			
		}
		catch(IOException ex){
			ex.printStackTrace();
		     }
		return 0;
				
	}
	
	
}
