package junit_Tests_Road_Intersection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class JunitTests_Road_Intersection {
	
	@Test  
	@DisplayName("Test for the null file or file not found for Vehicles table.")
	public void NullFile_ShouldThrowFileNotFound_VehiclesTable()
	{	
		
		Vehicles vehicle = new Vehicles();
		//add code here to test Null file Exception for reading Vehicles.csv file
	}
	
	@Test  
	@DisplayName("Test for checking if vehicles.csv is updated.")
	public void WriteFile_ShouldAddVehicleDetails_VehiclesTable()
	{	
		
		Vehicles vehicle = new Vehicles();
		//add code here to test for writing Vehicles.csv file
	}
	
	@Test  
	@DisplayName("Test for the null file or file not found for Phases table.")
	public void NullFile_ShouldThrowFileNotFound_PhasesTable()
	{	
		
		Vehicles vehicle = new Vehicles();
		//add code here to test Null file Exception for reading Intersection.csv file
	}

	@Test  
	@DisplayName("Test for the null file or file not found for Statistics table.")
	public void NullFile_ShouldThrowFileNotFound_StatisticsTable()
	{	
		
		Vehicles vehicle = new Vehicles();
		//add code here to test Null file Exception for reading Statistics.csv file
	}
}
