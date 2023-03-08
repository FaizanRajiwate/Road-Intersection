import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ReportFile {	
	
	public FileWriter writeToFile(String filename) {
		try {
			File file = new File(filename);
			if (file.createNewFile()){
				System.out.println("New File " + filename + " successfully created.");
			}else {
				System.out.println("This file already exists");
			}
			FileWriter writer = new FileWriter(file);
			return writer;
		}catch (IOException e) {
			System.out.println("There has been an error with the file");
			return null;
		}
	
	}
}
