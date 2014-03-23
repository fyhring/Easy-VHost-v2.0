
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Elev
 * Date: 12-10-13
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */
public class ReadnWrite {

    private String fileName;
    private String encoding = "utf8";

	public ReadnWrite(String fileName)
    {
        this.fileName = fileName;
    }

    public final boolean find(String search) throws IOException
    {
        Scanner scanner     = new Scanner( new FileInputStream(fileName), encoding );
        boolean found       = false;

        try
        {
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();

                if (line.equals(search)) found = true;
            }
        }
        finally {
            scanner.close();
        }
        return found;
    }

    public String read(boolean parse, boolean localsOnly) throws IOException
    {
        log("Reading..");

        StringBuilder text  = new StringBuilder();
        String NL           = System.getProperty("line.separator");
        Scanner scanner     = new Scanner( new FileInputStream(fileName), encoding );

        try
        {
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();
                if (parse)
                {
                    if (localsOnly)
                    {
                        if (line.startsWith("127.0.0.1"))
                        {
                            text.append( parse(line) ).append( NL );
                        }
                    }
                    else
                    {
                        text.append( parse(line) ).append( NL );
                    }

                } else {
                    if (localsOnly)
                    {
                        if (line.startsWith("127.0.0.1")) { text.append( line ).append( NL ); }
                    }
                    else
                    {
                        text.append( line ).append( NL );
                    }
                }
            }
        }
        finally {
            scanner.close();
        }
        return ""+ text;
    }
    
    public String readVhostPath() throws IOException
    {
    	Scanner scanner 	= new Scanner( new FileInputStream(fileName), encoding );
    	
    	try
    	{
    		while (scanner.hasNextLine())
    		{
    			String line = scanner.nextLine().trim();
    			if (line.startsWith("DocumentRoot"))
    			{
    				return line.replace("DocumentRoot", "");
    			}
    		}
    	}
    	finally {
    		scanner.close();
    	}
    	return "";
    }

    public void write(String text) throws IOException
    {
        Writer out = new OutputStreamWriter( new FileOutputStream(fileName), encoding);
        try {
            out.write(text);
        }
        finally { out.close(); }
    }

    public void writeto(String text) throws IOException
    {
        String writeText = read(false, false) + text;
        write(writeText);
    }
    
    public ArrayList<String> dirListing()
    {
    	
    	File f = new File(fileName);
    	ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
    	
    	return names;
    	
    }


    private String parse(String line)
    {
        line = line.replaceAll("(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9])[.]){3}(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9]))", "");
        line = line.replaceAll("\\s", "");
        return line;
    }

    private static void log(String msg)
    {
        System.out.println(String.valueOf(msg));
    }


}
