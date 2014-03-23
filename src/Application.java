
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class Application extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2394886366847241664L;
	
	private String path;
	private String url;
	
	private JTextField newVhostDomain;
	private PathFinder pathFinder;
	private JButton newVhostBtn;
	private JButton pathFinderBtn;
	private JLabel pathFinderLabel;
	
	public Application() {
		
		path = null;
		url  = null;
		
		setLayout(null);
		
		
		drawLayout();
		
		// Set 'pathFinderBtn' action
		pathFinder = new PathFinder();
		pathFinderBtn.addActionListener(pathFinder);
		
		// Set 'newVhostBtn' action
		/* addVhost = new VHost();
		 * newVhostBtn.addActionListener(addVhost);
		 */
		
		newVhostBtn.addActionListener((ActionListener) this);  

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Easy VHost v2.0");
		setSize(750, 500);
		setLocationRelativeTo(null);
		
		setVisible(true);
		setResizable(false);
		
		
		
		// Check if the inputs are valid.
		
		boolean keepLooping = true;
		
		
		while( keepLooping ) {
			
			path = pathFinder.getFile();
			url  = newVhostDomain.getText();
			
			if( path != null && url != null ) {
				newVhostBtn.setEnabled(true);
				pathFinderLabel.setText(path);
			}
		}
		
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		ApacheController apache = new ApacheController();
		
		String vhost_path = apache.ApacheDir + System.getProperty("file.separator") + url;
		apache.createVhostFile(vhost_path);
		try {
			apache.createVhostContent(url, path, vhost_path);
		} catch (IOException e1) { e1.printStackTrace(); }
		
		try {
		    Thread.sleep(1000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		apache.restart();
	}
	
	
	
	private void drawLayout() {
		
		
		/**
		 * Frame for new VHost
		 */
		JLabel newVhostLabel = new JLabel();
		newVhostLabel.setText("Add new Virtual Host");
		newVhostLabel.setBounds(50, 15, 650, 50);
		add(newVhostLabel);
		
		JLabel newVhostFrame = new JLabel();
		newVhostFrame.setBorder(BorderFactory.createEtchedBorder());
		newVhostFrame.setBounds(50, 50, 650, 75);
		add(newVhostFrame);
		
		// Virtual Domain name text field
		newVhostDomain = new JTextField();
		newVhostDomain.setText("site.dev");
		newVhostDomain.setBounds(60, 60, 150, 25);
		newVhostDomain.setFocusable(true);
		add(newVhostDomain);
		
		// Button to open the path finder
		pathFinderBtn = new JButton();
		pathFinderBtn.setText("Path");
		pathFinderBtn.setBounds(220, 60, 75, 25);
		add(pathFinderBtn);
		
		// Path label
		pathFinderLabel = new JLabel();
		pathFinderLabel.setText("Please select a folder..");
		pathFinderLabel.setBounds(65, 95, 650, 25); // 305, 60, 380, 25
		pathFinderLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		add(pathFinderLabel);
		
		// Button to add the virtualhost
		newVhostBtn = new JButton();
		newVhostBtn.setText("Add VHost");
		newVhostBtn.setBounds(305, 60, 100, 25); // 56, 95, 100, 25
		newVhostBtn.setEnabled(false);
		add(newVhostBtn);
		
		
		/**
		 * Frame for list of Vhosts
		 */
		// Label with text
		JLabel listVhostsLabel = new JLabel();
		listVhostsLabel.setText("List of Virtual Hosts");
		listVhostsLabel.setBounds(50, 135, 650, 50);
		add(listVhostsLabel);
		
		// Label with border.
		JLabel listVhostsFrame = new JLabel();
		listVhostsFrame.setBorder(BorderFactory.createEtchedBorder());
		listVhostsFrame.setBounds(50, 170, 650, 210);
		add(listVhostsFrame);
		
		// Make table
		JTable table = makeListOfVhosts();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setMaxWidth(150);
		table.getColumnModel().getColumn(1).setMaxWidth(498);
		table.setBounds(51, 171, 648, 208);
		add(table);
		
		// Delete vhost btn
		JButton dltVhostBtn = new JButton();
		dltVhostBtn.setText("Delete Selected Vhost");
		dltVhostBtn.setBounds(44, 385, 180, 25);
		dltVhostBtn.setEnabled(false);
		add(dltVhostBtn);
		
	}
	
	
	public JTable makeListOfVhosts() {
		
		//DefaultListModel listModel = new DefaultListModel();
		ApacheReader reader = new ApacheReader();
		
		String[][] hosts = reader.getVhosts();
		
		for(int i=0; i < hosts.length; i++) {
			//listModel.addElement(hosts[i][0]);
		}
		
		/*listModel.addElement("Jane Doe");
		listModel.addElement("John Smith");
		listModel.addElement("Kathy Green");

		return listModel;*/
		
		ApacheController apache = new ApacheController();
		ArrayList<String> host = null;
		try {
			 host = apache.getVhosts();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] columns = {
				"Domain", "Path"
		};

		  DefaultTableModel model = new DefaultTableModel(columns, 0);

		  for (Object item : host) {
		     Object[] row = new Object[3];
		     //... fill in row with info from item

		     model.addRow(row);
		  }
		
		//return new JTable(hosts, columns);
		  return new JTable(model);
		
	}
	
	
	/* Auto starter */
	public static void main(String args[]) {
		
		new Application();
		
	}
	

}
