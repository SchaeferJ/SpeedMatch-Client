package SpeedClient;
import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sql.*;
import javax.swing.JOptionPane;


public class DBConnect_Client {

	private Connection con;
	private String event;
	private String username = "speedmatch";
	private String password = "loveisintheair!";

	public DBConnect_Client(String eventname) {
		con = null;
		this.event = DBTools_Client.sanitize(eventname);
	}

	public DBConnect_Client() {
		con = null;
	}

	public void setEventname(String eventname) {
		this.event = DBTools_Client.sanitize(eventname);
	}

	// Allows to specify user-defined login credentials.

	public void setLogin(String usr, String pwd) {
		this.username = usr;
		this.password = pwd;
	}

	// Searches for database server and establishes DB-connection.

	public void initConnection() {
		this.initConnection(DBTools_Client.getDevices());
	}

	// Establishes DB-connection using predefined IP

	public void initConnection(InetAddress ia) {
		try {
			String ip = ia.getHostAddress();
			String url = "jdbc:mysql://"+ip+":3306/speedmatch";
			Class.forName ("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection (url,username,password);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Unable to connect to database: "+Processing_Client.getLineSep()+e.getMessage()+ Processing_Client.getLineSep() + Processing_Client.getLineSep()+"Check if username and password are correct, if you have selected the right server and"+Processing_Client.getLineSep()+"if you have granted remote access permissions to the user in the RDBMS", "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Adds a participant to the Database

	public int insertParticipant(String fname, String lname, int gender, String fa, String dik, String ereig, String feier, String rev) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO "+event+" (fname, lname, gender, fa, dik, ereig, feier, rev) VALUES (?,?,?,?,?,?,?,?);");
			ps.setString(1, fname);
			ps.setString(2, lname);
			ps.setInt(3, gender);
			ps.setString(4, fa);
			ps.setString(5, dik);
			ps.setString(6, ereig);
			ps.setString(7, feier);
			ps.setString(8, rev);
			ps.executeUpdate();
			ps.close();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID();");
			int id = -99;
			while(rs.next()) {
				id = rs.getInt(1);
			}
			s.close();
			return id;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while updating Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
			return -99;
		}
	}

	// Returns an array containing the names of all tables in the Database,
	// i.e. the names of all events

	public String[] getEventsFromDB() {
		try {
			ArrayList<String> elist = new ArrayList<String>();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("show tables;");
			while(rs.next()) {
				String tmp = rs.getString(1);
				if(!tmp.startsWith("zzz")) {
					elist.add(tmp);
				}
			}
			String[] earr = new String[elist.size()];
			earr = elist.toArray(earr);
			return earr;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while reading from Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}


	//Checks whether there is already a table with the same name
	public boolean eventExists() {
		String[] ea = this.getEventsFromDB();
		for(int i=0; i<ea.length; i++) {
			if(ea[i].equals(this.event)) {
				return true;
			}
		}
		return false;
	}

	// Get Maximum Participants allowed in event

	public int getMaxPart() {
		try {
			String sql = "SELECT maxpart FROM zzzmemory WHERE name='"+this.event+"';";
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			int maxpart = 0;
			while(rs.next()) {
				maxpart = rs.getInt("maxpart");
			}
			return maxpart;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while reading from Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
	}

	public int getCurrentPart() {
		try {
			String sql = "SELECT MAX(id) FROM "+this.event+";";
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			int cur = 0;
			while(rs.next()) {
				cur = rs.getInt(1);
			}
			return cur;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while reading from Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
	}


	// Closes Database Connection

	public void close() {
		try {
			con.close();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while closing Database Connection: "+e.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}


}
