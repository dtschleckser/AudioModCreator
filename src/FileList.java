import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;


public class FileList {
	public static void updateData()
	{
		Configuration.loadConfig();
		String result = retrieveSoundData();
		if(!(result.equals(""))) //If blank text is gotten from the server it means you're up to date
			Configuration.setValue("lastUpdate", Long.toString(System.currentTimeMillis()/1000L));
		else
			try 
			{
				FileOutputStream fos = new FileOutputStream(new File("dota-sound-list.txt"));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				bw.write(result);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	//This method is a ~2.2MB download 
	//so only use when it's necessary (update detected, etc) 
	public static String retrieveSoundData()
	{
		String response = "";
		URL oUrl = null;
		try 
		{
			if(Configuration.getValue("lastUpdate") == null)
				Configuration.setValue("lastUpdate", Long.toString(System.currentTimeMillis()/1000L));
			oUrl = new URL("http://oxivod.me/get-sound-files.php?prevUpdate="+Configuration.getValue("lastUpdate"));
			HttpURLConnection servCon = (HttpURLConnection) oUrl.openConnection();
			servCon.setConnectTimeout(10000);
			InputStream is = servCon.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = rd.readLine()) != null) 
			{
				response += line;
				response += '\r';
			}
			rd.close();
		} catch (java.net.SocketTimeoutException e) {
			JOptionPane.showMessageDialog(null, "Couldn't connect to server.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(response);
		return response;
	}
}
