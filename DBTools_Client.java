package SpeedClient;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

//This class provides tools for Database connectivity


public class DBTools_Client{
    
	
	// Takes an IP-Address and scans the range x.x.x.x/24 for devices.
	// It is assumed that a device exists, if it can be pinged or if the
	// hostname is not equivalent to the IP-Address, i.e. there is an entry
	// in a DNS lookup table for the respective IP. After completion of search
	// the user is asked to select the database server from the list of devices
	// on the network. IP Address of Server is returned.
	
	// This code assumes IPv4 and subnetmask 255.255.255
	
	// INCREDIBLY slow on Windows. Probably has something to do
	// with rights management
	
	public static InetAddress getDevices(String ip) {
		JOptionPane.showMessageDialog(null, "SpeedMatch is now searching for devices on the network. This may take up to 2 Minutes. Please stand by.", "Searching...", JOptionPane.INFORMATION_MESSAGE);;
		try {
			ProgressMonitor pm = new ProgressMonitor(null, "Scanning...","Searching for Devices on Network", 0,260);
			InetAddress[] ia = new InetAddress[255];
			int counter = 0;
			InetAddress local = InetAddress.getByName(ip);
			byte[] local_ip = local.getAddress();
			for(int i=1; i<=254;i++) {
				local_ip[3] = (byte)i;
				InetAddress address = InetAddress.getByAddress(local_ip);
				pm.setNote("Pinging " + address.getHostAddress());
				if (address.isReachable(20))
				{
					ia[counter] = address;
					counter++;
				}
				else if (!address.getHostAddress().equals(address.getHostName()))
				{
					ia[counter] = address;
					counter++;
				}
				pm.setProgress(i);
			}
		Object[] obj = new Object[counter];
		int prg = 255;
		pm.setProgress(prg);
		for(int j=0; j<counter; j++) {
			obj[j] = j + ": " + ia[j].getHostAddress() + " - " + ia[j].getHostName();
			prg++;
			pm.setProgress(prg);
		}
		pm.setProgress(260);
		String select = (String) JOptionPane.showInputDialog(null, "SpeedMatch detected " + counter + " devices on your network. Please select the one running your Database", "Select device", JOptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
		int index = Character.getNumericValue(select.charAt(0));
		return ia[index];
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "An error occured while searching for server: "+e.getMessage(), "NETWORK ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	public static InetAddress getDevices() {
		return getDevices(getLocalIP());
	}
	
	// Gets a list of all installed network interfaces and retrieves their respective
	// local IPs. Interfaces connected to a local network should have an IP starting
	// with 192.168. - This IP is returned.
	
	public static String getLocalIP(){
		try {
		Enumeration e = NetworkInterface.getNetworkInterfaces();
		while(e.hasMoreElements())
		{
			NetworkInterface n = (NetworkInterface) e.nextElement();
			Enumeration ee = n.getInetAddresses();
			while (ee.hasMoreElements())
			{
				InetAddress i = (InetAddress) ee.nextElement();
				String ip = i.getHostAddress();
				if(ip.startsWith("192.168.")) {
					return ip;
				}
			}
		}
		JOptionPane.showMessageDialog(null, "An unknown error occured while retrieving local IP", "NETWORK ERROR", JOptionPane.ERROR_MESSAGE);
		return null;
		}catch(SocketException e) {
			JOptionPane.showMessageDialog(null, "ERROR: could not retrieve local IP: "+e.getMessage(), "NETWORK ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	// Removes characters that could be used for SQL injections
	// Only used when Prepared Statements are not possible (e.g. create table)
	public static String sanitize(String s) {
		String san = s.replaceAll("[^\\w]", "");
		if(s.matches(".*[^\\w]+.*")) {
			JOptionPane.showMessageDialog(null, "Your event name contains potentially malicious characters."+Processing_Client.getLineSep()+"It has been changed from '" + s +"' to '"+san+"'", "Security alert", JOptionPane.INFORMATION_MESSAGE);
		}
		return san;
	}
}