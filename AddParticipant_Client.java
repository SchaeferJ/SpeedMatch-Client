package SpeedClient;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class AddParticipant_Client extends JFrame {
	private String event;
	private DBConnect_Client data;
	private String ip;

	public AddParticipant_Client(String evt, DBConnect_Client d) {
		this.event = evt;
		// this.ip = ia;
		this.data = d;
		initGUI(this.data);
	}

	public void initGUI(DBConnect_Client dbase) {

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		this.getContentPane().add(panel);
		this.getContentPane().setBackground(new Color(255, 229, 204));

		// Title
		JLabel title = new JLabel("Add New Participant");
		title.setForeground(new Color(250, 92, 92));
		title.setFont(new Font("Arial", Font.BOLD, 40));

		// Seperator
		JSeparator sep = new JSeparator();
		sep.setPreferredSize(new Dimension(500, 20));

		// Labels
		JLabel fname = new JLabel("First name");
		fname.setForeground(new Color(250, 92, 92));
		fname.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel lname = new JLabel("Last name");
		lname.setForeground(new Color(250, 92, 92));
		lname.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel gender = new JLabel("Gender");
		gender.setForeground(new Color(250, 92, 92));
		gender.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel subject = new JLabel("Your faculty");
		subject.setForeground(new Color(250, 92, 92));
		subject.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel q1 = new JLabel("Who is your favorite dictator?");
		q1.setForeground(new Color(250, 92, 92));
		q1.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel q2 = new JLabel("Which politcal development would you undo if you had the chance to?  ");
		q2.setForeground(new Color(250, 92, 92));
		q2.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel q3 = new JLabel("With which politician would you like to go party?");
		q3.setForeground(new Color(250, 92, 92));
		q3.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel q4 = new JLabel("Which revolution would you like to have been a part of?");
		q4.setForeground(new Color(250, 92, 92));
		q4.setFont(new Font("Arial", Font.PLAIN, 15));

		// Text Fields
		JTextField fnamebox = new JTextField();
		fnamebox.setForeground(new Color(250, 92, 92));
		fnamebox.setFont(new Font("Arial", Font.PLAIN, 15));
		fnamebox.setColumns(15);

		JTextField lnamebox = new JTextField();
		lnamebox.setForeground(new Color(250, 92, 92));
		lnamebox.setFont(new Font("Arial", Font.PLAIN, 15));
		lnamebox.setColumns(15);

		String[] genders = { "male", "female", "other" };
		JComboBox<String> gnd = new JComboBox<String>(genders);
		gnd.setForeground(new Color(250, 92, 92));
		gnd.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(Color.WHITE);
				super.paint(g);
			}
		});

		String[] subjects = { "Informatics/Mathematics", "Social Sciences", "Economics", "Philosophy" };
		JComboBox<String> subj = new JComboBox<String>(subjects);
		subj.setForeground(new Color(250, 92, 92));
		subj.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(Color.WHITE);
				super.paint(g);
			}
		});

		String[] q1o = { "Pinochet", "Mao", "Mussolini", "Stalin" };
		JComboBox<String> q1m = new JComboBox<String>(q1o);
		q1m.setForeground(new Color(250, 92, 92));
		q1m.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(Color.WHITE);
				super.paint(g);
			}
		});

		String[] q2o = { "Hartz Laws", "Brexit", "Chancellorship of Angela Merkel", "Fall of Berlin Wall" };
		JComboBox<String> q2m = new JComboBox<String>(q2o);
		q2m.setForeground(new Color(250, 92, 92));
		q2m.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(Color.WHITE);
				super.paint(g);
			}
		});

		String[] q3o = { "Gregor Gysi", "Christian Lindner", "Cem Oezdemir", "Frauke Petry" };
		JComboBox<String> q3m = new JComboBox<String>(q3o);
		q3m.setForeground(new Color(250, 92, 92));
		q3m.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(Color.WHITE);
				super.paint(g);
			}
		});

		String[] q4o = { "French Revolution (1789)", "Chinese Cultural Revolution (1966)", "Cuban Revolution (1953)",
				"German November Revoltution (1918)" };
		JComboBox<String> q4m = new JComboBox<String>(q4o);
		q4m.setForeground(new Color(250, 92, 92));
		q4m.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(Color.WHITE);
				super.paint(g);
			}
		});

		// Buttons
		JPanel actionPanel = new JPanel();
		actionPanel.setOpaque(false);
		JButton disc = new JButton("Discard");
		actionPanel.add(disc);

		JButton add = new JButton("Add");
		actionPanel.add(add);

		// Action Listener for Discard-Button
		disc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		// Action Listener for Add-Button
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int cp = data.getCurrentPart();
					int mp = data.getMaxPart();
					if (mp > cp) {
						String f = fnamebox.getText();
						String l = lnamebox.getText();
						int gindex = gnd.getSelectedIndex();
						String fa = subj.getItemAt(subj.getSelectedIndex());
						String q1s = q1m.getItemAt(q1m.getSelectedIndex());
						String q2s = q2m.getItemAt(q2m.getSelectedIndex());
						String q3s = q3m.getItemAt(q3m.getSelectedIndex());
						String q4s = q4m.getItemAt(q4m.getSelectedIndex());
						int uid = dbase.insertParticipant(f, l, gindex, fa, q1s, q2s, q3s, q4s);
						JOptionPane.showMessageDialog(AddParticipant_Client.this, "Your ID is: " + uid, "ID",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(AddParticipant_Client.this, "Sorry, no more places left :(",
								"Event full", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception em) {
					JOptionPane.showMessageDialog(AddParticipant_Client.this, em.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
				dispose();
			}
		});

		// Adding
		/*
		 * GridBagConstraints gbc = new GridBagConstraints(); gbc.fill =
		 * GridBagConstraints.HORIZONTAL; // gbc.gridwidth =
		 * GridBagConstraints.RELATIVE;
		 * 
		 * int i = 1;
		 * 
		 * gbc.gridx = 0; gbc.gridy = 0; // gbc.weighty = 1; gbc.ipady = 20; gbc.anchor
		 * = GridBagConstraints.PAGE_START; panel.add(title, gbc);
		 * 
		 * /* gbc.gridx = 0; gbc.gridy = 1; panel.add(sep, gbc);
		 * 
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(fname, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(fnamebox, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(lname, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(lnamebox, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(gender, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(gnd, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(subject, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(subj, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(q1, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(q1m, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(q2, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(q2m, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(q3, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(q3m, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(q4, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(q4m, gbc);
		 * 
		 * i++;
		 * 
		 * // gbc.gridx = 0; // gbc.gridy = i; // panel.add(id, gbc);
		 * 
		 * // gbc.gridx = 2; // gbc.gridy = i; // panel.add(idbox, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; // gbc.weighty = 1; gbc.anchor =
		 * GridBagConstraints.SOUTHEAST; panel.add(actionPanel, gbc);
		 * 
		 * this.pack();
		 * 
		 * this.setVisible(true);
		 */

		panel.add(title, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.PAGE_START,
				GridBagConstraints.CENTER, new Insets(20, 0, 10, 0), 0, 0));

		panel.add(sep, new GridBagConstraints(1, 1, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 0.0,
				0.0, GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

		panel.add(fname, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(fnamebox, new GridBagConstraints(0, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

		panel.add(lname, new GridBagConstraints(0, 4, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(lnamebox, new GridBagConstraints(0, 5, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

		panel.add(gender, new GridBagConstraints(0, 6, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(gnd, new GridBagConstraints(0, 7, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(subject, new GridBagConstraints(0, 8, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(subj, new GridBagConstraints(0, 9, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(q1, new GridBagConstraints(0, 10, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(q1m, new GridBagConstraints(0, 11, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(q2, new GridBagConstraints(0, 12, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(q2m, new GridBagConstraints(0, 13, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(q3, new GridBagConstraints(0, 14, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(q3m, new GridBagConstraints(0, 15, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(q4, new GridBagConstraints(0, 16, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(q4m, new GridBagConstraints(0, 17, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(actionPanel, new GridBagConstraints(0, 18, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(20, 0, 20, 0), 0, 0));

		this.pack();
		this.setVisible(true);

	}

	public void initializeAddPart() {

		// AddParticipant frame = new AddParticipant();
		this.pack();
		this.setVisible(true);
		this.setTitle("SpeedDating - MatchFinder");
		this.setSize(800, 700);
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
