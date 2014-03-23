
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created with IntelliJ IDEA.
 * User: Elev
 * Date: 12-10-13
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
public class ApacheController {

    protected static OSValidator is = new OSValidator();

    private static String hostsFile, httpdFile, sitesLine;
    public String ApacheDir;

    public ApacheController()
    {
        if (is.windows())   hostsFile = "C:\\Windows\\System32\\drivers\\etc\\hosts";
        if (is.mac())       hostsFile = "/etc/hosts";
        if (is.unix())      hostsFile = "/etc/hosts";

        if (is.windows())   httpdFile = "C:\\wamp\\bin\\apache\\apache2.2.22\\conf\\httpd.conf";
        if (is.unix())      httpdFile = "/etc/apache2/apache2.conf";
        if (is.mac())       httpdFile = "/etc/apache2/httpd.conf";

        if (is.windows())   sitesLine = "Include \"sites-enabled/*\"";
        if (is.unix())      sitesLine = "Include /etc/apache2/sites-enabled/";
        if (is.mac())       sitesLine = "Include /etc/apache2/sites-enabled/";
        
        if (is.windows())   ApacheDir = "C:\\wamp\\bin\\apache\\apache2.2.22\\sites-enabled";
        if (is.unix())      ApacheDir = "/etc/apache2/sites-enabled";
        if (is.mac())       ApacheDir = "/etc/apache2/sites-enabled";

    }

    public static void runCommand(String command)
    {
        Process p;

        System.out.println(command);

        try
        {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();

            BufferedReader reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );

            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while(line != null)
            {
                line = reader.readLine();
                sb.append( line );
            }

            String output = sb.toString();

            System.out.println( output );
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public final void restart()
    {
    	System.out.println("Restarting...");
    	
        if (is.windows())
        {
            runCommand("NET STOP wampapache");
            runCommand("NET START wampapache");
        }
        if (is.unix() && is.mac())
        {
        	//runCommand("apachectl -k graceful");
        	String commands[] = {"su", "-c", "apachectl -k graceful"};
        	try {
    			Process p = Runtime.getRuntime().exec(commands);
    			p.waitFor();
    		} catch (IOException e) {
    			e.printStackTrace();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
        	
        }
    }

    public final void checkHosts()
    {
        if (is.windows())
        {
            ReadnWrite hosts = new ReadnWrite(hostsFile);
            try
            {
                System.out.println( hosts.read(true, true) );
                //hosts.find()
            }
            catch (IOException e) { e.printStackTrace(); }
        }

    }
    
    public final void addToHosts(String domain) throws IOException
    {
    	ReadnWrite hosts = new ReadnWrite(hostsFile);
    	hosts.writeto("127.0.0.1\t"+ domain);
    }

    public final boolean checkFolders()
    {
        DirController directory = new DirController();
        return (directory.apacheExists());
    }

    public final void createFolders()
    {
        DirController directory = new DirController();
        directory.createDir( directory.ApacheDir );
    }

    public final boolean checkHttpd()
    {
        ReadnWrite httpd = new ReadnWrite(httpdFile);
        boolean result   = false;
        try
        {
            result = httpd.find(sitesLine);
        }
        catch (IOException e) { e.printStackTrace(); }

        return result;
    }

    public final void fixHttpd()
    {
        String NL           = System.getProperty("line.separator");
        ReadnWrite httpd    = new ReadnWrite(httpdFile);

        try
        {
            httpd.writeto(NL + NL +"# Include all Virtual Hosts"+ NL + sitesLine);
        }
        catch (IOException e) { e.printStackTrace(); }
        finally { restart(); }
    }
    
    public final void createVhostFile(String path)
    {
    	DirController directory = new DirController();
    	if (!directory.fileExists(path))
    	{
    		directory.createFile(path);
    	}
    }
    
    public final void createVhostContent(String domain, String path, String filePath) throws IOException
    {
    	ReadnWrite file = new ReadnWrite(filePath);
    	
    	String NL = System.getProperty("line.separator");
    	
    	String content = "<VirtualHost *:80>" + NL + "\tServerName "+ domain + NL + "\tDocumentRoot "+ path + NL +"</VirtualHost>";
    	try {
			file.write(content);
		} catch (IOException e) { e.printStackTrace(); }
    	
    	addToHosts(domain);
    }
    
    public final ArrayList<String> getVhosts() throws IOException
    {
    	ReadnWrite reader = new ReadnWrite(ApacheDir);
    	
    	ArrayList<String> files = reader.dirListing();
    	ArrayList<String> data = new ArrayList<String>();
    	
    	for (String file : files) {
    		if (!".DS_Store".equals(file))
    		{
	    		String path = ApacheDir +"/"+ file;
				ReadnWrite fileReader = new ReadnWrite(path);
	    		ArrayList<String> vhost = new ArrayList<String>();
	    		vhost.add(file);
	    		vhost.add(fileReader.readVhostPath());
				data.addAll( vhost );
				System.out.println(file +" -> "+ fileReader.readVhostPath());
    		}
		}
    	
    	return data; 
    }
}
