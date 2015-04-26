import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class Configuration {
	public static Properties p;
	public static void loadConfig()
	{
		p = new Properties();
		File f = new File("config.cfg");
		if(!f.exists())
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileInputStream fis = new FileInputStream(f);
			p.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getValue(String key)
	{
		return p.getProperty(key);
	}
	
	public static void setValue(String key, String value)
	{
		p.setProperty(key, value);
		OutputStream out;
		try {
			out = new FileOutputStream(new File("config.cfg"));
			p.store(out, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
