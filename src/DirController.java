
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Elev
 * Date: 13-10-13
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class DirController {

    public String ApacheDir;

    public DirController()
    {
        OSValidator is = new OSValidator();

        if (is.windows())   ApacheDir = "C:\\wamp\\bin\\apache\\apache2.2.22\\sites-enabled";
        if (is.mac())       ApacheDir = "/etc/apache2/sites-enabled";
        if (is.unix())      ApacheDir = "/etc/apache2/sites-enabled";
    }

    public final boolean apacheExists()
    {
        return (dirExists( ApacheDir ));
    }

    public final boolean createDir(String path)
    {
        File dir = new File(path);
        return dir.mkdir();
    }

    public final boolean dirExists(String path)
    {
        File file = new File(path);
        return (file.exists() && file.isDirectory());
    }
    
    public final boolean fileExists(String path)
    {
    	File file = new File(path);
    	return (file.exists());
    }
    
    public final void createFile(String path)
    {
    	File file = new File(path);
    	try {
			file.createNewFile();
		} catch (IOException e) { e.printStackTrace(); }
    }

}
