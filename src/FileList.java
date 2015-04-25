import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FileList {
	public static void main(String[]args)
	{
		String s = retrieveSoundData();
	}
	//This method is a ~2.2MB download 
	//so only use when it's necessary (update detected, etc) 
	public static String retrieveSoundData()
	{
		String response = "";
		URL oUrl = null;
		try {
			oUrl = new URL("http://oxivod.me/get-sound-files.php");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection servCon = (HttpURLConnection) oUrl.openConnection();
			InputStream is = servCon.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = rd.readLine()) != null) {
				System.out.println(response);
				response += line;
				response += '\r';
			}
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return response;
	}
}
