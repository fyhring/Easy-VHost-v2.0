
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class PathFinder extends JPanel implements ActionListener {
	
	
	private static final long serialVersionUID = -1151007103342283249L;
	
	public JFileChooser chooser;
	public String choosertitle = "V¾lg mappe";
	private String result;
   
	
	public String getFile() {
		return result;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	    chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(choosertitle);
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    	result = chooser.getSelectedFile().toString();
	    } else {
	    	System.out.println("No Selection ");
	    }
		
	}

}
