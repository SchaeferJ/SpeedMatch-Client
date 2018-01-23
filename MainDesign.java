package SpeedClient;

import javax.swing.*;
import javax.swing.SwingWorker.StateValue;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.net.InetAddress;

public class MainDesign extends JFrame implements PropertyChangeListener {

	private String[] eventarr = new String[0];
	private int lselect = -1; // Selected Item Index
	private DBConnect_Client data;
	private int connected = 0;
	private String ip;
	private Task task;
	private int index = -99;
	private InetAddress ca;
	private ProgressMonitor pm;
	private DefaultListModel<String> dlm = new DefaultListModel<String>();

	public MainDesign() {
		data = new DBConnect_Client();
		initGUI();
	}

	class Task extends SwingWorker<Void, Void> {
		public Void doInBackground() {
			try {
				setProgress(0);
				ip = DBTools_Client.getLocalIP();
				InetAddress[] ia = new InetAddress[255];
				int counter = 0;
				double pr = 0;
				InetAddress local = InetAddress.getByName(ip);
				byte[] local_ip = local.getAddress();
				for (int i = 1; i <= 254; i++) {
					local_ip[3] = (byte) i;
					InetAddress address = InetAddress.getByAddress(local_ip);
					// setNote("Pinging " + address.getHostAddress());
					if (address.isReachable(20)) {
						ia[counter] = address;
						counter++;
					} else if (!address.getHostAddress().equals(address.getHostName())) {
						ia[counter] = address;
						counter++;
					}
					pr += 0.365; // CAUTION: Magic number
					setProgress((int) pr);
				}
				Object[] obj = new Object[counter];
				setProgress(99);
				for (int j = 0; j < counter; j++) {
					obj[j] = j + ": " + ia[j].getHostAddress() + " - " + ia[j].getHostName();
				}
				String select = (String) JOptionPane.showInputDialog(null,
						"SpeedMatch detected " + counter
								+ " devices on your network. Please select the one running your Database",
						"Select device", JOptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
				index = Character.getNumericValue(select.charAt(0));
				if (index >= 0) {
					ca = ia[index];
				}
				setProgress(100);
				return null;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "An error occured while searching for server: " + e.getMessage(),
						"NETWORK ERROR", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}

	}

	class conncect implements Runnable {
		public void run() {
			while (!(task.getState().equals(StateValue.DONE))) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// Ignore
				}
			}
			if (index >= 0) {
				data.initConnection(ca);
				setArr(data);
				if (eventarr != null) {
					dlm.removeAllElements();
					for (int i = 0; i < eventarr.length; i++) {
						dlm.addElement(eventarr[i]);
					}
					connected = 1;
				}
			}
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			pm.setProgress(Math.min(progress, 100));
			String message = String.format("Completed %d%%.\n", progress);
			pm.setNote(message);
		}

	}

	public void setArr(DBConnect_Client d) {
		try {
			this.eventarr = d.getEventsFromDB();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error while retrieving events: " + e.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
			dlm.addElement("---PLEASE CONNECT TO A DATABASE SERVER---");
		}
	}

	public void initGUI() {

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		this.getContentPane().add(panel);
		this.getContentPane().setBackground(new Color(255, 229, 204));
	    setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Title and Subtitle
		JLabel title = new JLabel("Welcome to the SpeedMatch Client!");
		title.setForeground(new Color(250, 92, 92));
		title.setFont(new Font("Arial", Font.BOLD, 40));

		JLabel subtitle = new JLabel("To begin, please select an existing event");
		subtitle.setForeground(new Color(250, 92, 92));
		subtitle.setFont(new Font("Arial", Font.BOLD, 25));

		// Seperator
		JSeparator sep = new JSeparator();
		sep.setPreferredSize(new Dimension(500, 20));

		// Label
		JLabel label = new JLabel("Events available on server");
		label.setForeground(new Color(250, 92, 92));
		label.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 15));

		// Buttons
		JPanel pagePanel = new JPanel();
		pagePanel.setOpaque(false);
		JButton next = new JButton("Next >");
		pagePanel.add(next);
		next.setEnabled(false);
		JButton exit = new JButton("Exit");
		pagePanel.add(exit);

		// Buttons to create or select events
		JPanel eventPanel = new JPanel();
		eventPanel.setOpaque(false);
		JButton connect = new JButton("Connect to Server");
		eventPanel.add(connect);

		// List of Events
		JList<String> list = new JList<String>(dlm);
		dlm.addElement("---PLEASE CONNECT TO A DATABASE SERVER---");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Action Listener for List
		ListSelectionModel lsm = list.getSelectionModel();
		lsm.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					lselect = list.getSelectedIndex();
					if (connected == 1) {
						next.setEnabled(true);
					}
				}
			}
		});

		// Action Listener for Server Connection
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ip = JOptionPane.showInputDialog(null,
							"Enter IP or Hostname of Server" + Processing_Client.getLineSep()
									+ "Leave empty to automatically search for servers on local network",
							"Connect to Database", JOptionPane.PLAIN_MESSAGE);
					if (ip == null) {
						// User pressed "Cancel" -> Do nothing
					} else if (ip.equals("")) {
						// dbase.initConnection();
						JOptionPane.showMessageDialog(null,
								"SpeedMatch is now searching for devices on the network. This may take up to 2 Minutes. Please stand by.",
								"Searching...", JOptionPane.INFORMATION_MESSAGE);
						pm = new ProgressMonitor(MainDesign.this, "Searching for Servers", "Searching...", 0, 100);
						pm.setProgress(0);
						task = new Task();
						task.addPropertyChangeListener(MainDesign.this);
						task.execute();
						Thread t = new Thread(new conncect());
						t.start();
					} else {
						ca = InetAddress.getByName(ip);
						data.initConnection(ca);
						setArr(data);
						if (eventarr != null) {
							dlm.removeAllElements();
							for (int i = 0; i < eventarr.length; i++) {
								dlm.addElement(eventarr[i]);
							}
							connected = 1;
						}
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Unable to connect to server. " + ex.getMessage());
				}

			}
		});

		// Action Listener for Exit
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (connected == 1) {
					data.close();
				}
				dispose();
			}
		});

		// Action Listener for Next
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String en = eventarr[lselect];
					data.setEventname(en);
					AddParticipant_Client addp = new AddParticipant_Client(en, data);
					addp.setVisible(true);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(MainDesign.this, ex.getMessage(), "ERROR while opening file",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// GridBagConstraints
		// Adding
		/*
		 * GridBagConstraints gbc = new GridBagConstraints(); gbc.fill =
		 * GridBagConstraints.HORIZONTAL; gbc.gridwidth = GridBagConstraints.RELATIVE;
		 * 
		 * gbc.gridx = 0; gbc.gridy = 0; gbc.ipady = 20; gbc.anchor =
		 * GridBagConstraints.PAGE_START; panel.add(title, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 1; panel.add(subtitle, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 2; panel.add(sep, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 3; panel.add(label, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 4; panel.add(new JScrollPane(list), gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(10,0,0,0);
		 * panel.add(eventPanel, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 5; panel.add(pagePanel, gbc);
		 * 
		 * this.pack();
		 * 
		 * this.setVisible(true);
		 */

		panel.add(title, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.PAGE_START,
				GridBagConstraints.VERTICAL, new Insets(20, 0, 20, 0), 0, 0));

		panel.add(subtitle, new GridBagConstraints(1, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(sep, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(0, 0, 30, 0), 0, 0));

		panel.add(label, new GridBagConstraints(0, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(30, 0, 10, 0), 0, 0));

		JScrollPane sp = new JScrollPane(list);
		sp.setMinimumSize(new Dimension(300, 200));
		panel.add(sp, new GridBagConstraints(0, 4, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(10, 0, 30, 0), 0, 0));

		panel.add(eventPanel, new GridBagConstraints(0, 5, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(30, 0, 30, 0), 0, 0));

		panel.add(pagePanel, new GridBagConstraints(0, 6, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(50, 0, 20, 0), 0, 0));

		this.pack();
		this.setVisible(true);
	}

}
