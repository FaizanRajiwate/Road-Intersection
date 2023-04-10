import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportFile {
	private static ReportFile instance;
	private static String filename = "report.txt";
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	// This class implements singleton pattern for single instance of this class
	// across whole code
	// private constructor, access only within class
	private ReportFile() {
	}

	// public getInstance(), accessible everywhere
	public static synchronized ReportFile getInstance() {
		if (instance == null) { // If there is no instance
			synchronized (ReportFile.class) { // Put a synchronisation lock
				if (instance == null) // check again for instance
					instance = new ReportFile();
			}
		}
		return instance;
	}

	public synchronized void writeToFile(String message) {
		LocalDateTime now = LocalDateTime.now();
		String formattedDate = now.format(formatter);
		String logMessage = "[" + formattedDate + "] " + message + "\n";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
			writer.write(logMessage);
		} catch (IOException e) {
			System.err.println("Error writing to log file: " + e.getMessage());
		}

	}
}