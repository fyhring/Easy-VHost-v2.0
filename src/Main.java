
public class Main {
	
	private static ApacheController apache;
	
	public static void main(String[] args) {
		
		apache = new ApacheController();
		

        /**
         * Before we launch the UI and read from apache, we need to make sure we have
         * all the settings correctly. Like check that the "sites-enabled" folder exists
         * and that apache actually does include the files from that folder.
         */
        if (!apache.checkFolders()) apache.createFolders();
        if (!apache.checkHttpd())   apache.fixHttpd();
        
        new Main();
		
	}
	
	public Main() {
		
		new Application();
		
	}


}