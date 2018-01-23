package SpeedClient;
import java.io.*;

import javax.swing.JOptionPane;

/**
 *
 * @author Jochen
 * 
 */


public class Processing_Client {

	// Detects which operating system SpeedMatch is running on

	public static String detectOS() {
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win")) {
			return "Windows";
		}
		else if(os.contains("nix")||os.contains("nux")||os.contains("mac")) {
			return "Unix";
		}
		else {
			return "other";
		}
	}

	// Returns the working directory of the program in system-specific formatting

	public static String getHomeDir() {
		char sep = '/';
		if(detectOS().equals("Windows")) {
			sep = '\\';
		}
		return System.getProperty("user.home")+ sep+"SpeedMatch"+sep;
	}

	// Get system-specific Line separator

	public static String getLineSep() {
		String newline = "\n";
		if(detectOS().equals("Windows")) {
			newline = "\r\n";
		}
		return newline;
	}

}
