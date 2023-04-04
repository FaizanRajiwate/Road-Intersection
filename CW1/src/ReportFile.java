import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportFile {
	private static ReportFile instance;
	private String filename;
    //This class implements singleton pattern for single instance of this class across whole code
	// private constructor, access only within class
	private ReportFile(String filename) {
		this.filename = filename;
	}

	// public getInstance(), accessible everywhere
	public static synchronized ReportFile getInstance(String filename) {
		if (instance == null) {
			instance = new ReportFile(filename);

		}
		return instance;
	}

	public FileWriter writeToFile() {
		try {
			File file = new File(filename);
			if (file.createNewFile()) {
				System.out.println("New File " + filename + " successfully created.");
			} else {
				System.out.println("This file already exists");
			}
			FileWriter writer = new FileWriter(file);
			return writer;
		} catch (IOException e) {
			System.out.println("There has been an error with the file");
			return null;
		}

	}
}
