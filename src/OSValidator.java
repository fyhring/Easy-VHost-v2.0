
/**
 * Created with IntelliJ IDEA.
 * User: Elev
 * Date: 12-10-13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static void get()
    {
        System.out.println(OS);
    }

    public boolean windows()
    {
        return (OS.contains("win"));
    }

    public boolean mac()
    {
        return (OS.contains("mac"));
    }
    public boolean unix()
    {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public boolean solaris()
    {
        return (OS.contains("sunos"));
    }
}
