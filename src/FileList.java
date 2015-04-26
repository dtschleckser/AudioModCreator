import java.awt.FlowLayout;
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
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class FileList {
	public static ArrayList<String> retrieveSoundData()
	{
		JFrame notificationPane = new JFrame();
		notificationPane.setLayout(new FlowLayout());
		notificationPane.add(new JLabel("Updating sound files - Please wait."));
		notificationPane.setVisible(true);
		notificationPane.setSize(200, 70);
		ArrayList<String> response = new ArrayList<String>();
		URL oUrl = null;
		try 
		{
			//first time running, so let's force an update
			if(Configuration.getValue("lastUpdate") == null)
				Configuration.setValue("lastUpdate", Long.toString(0L));
			//get the sound files from server
			oUrl = new URL("http://oxivod.me/get-sound-files.php?prevUpdate="+Configuration.getValue("lastUpdate"));
			HttpURLConnection servCon = (HttpURLConnection) oUrl.openConnection();
			servCon.setConnectTimeout(10000);
			InputStream is = servCon.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = rd.readLine()) != null) 
				response.add(line);
			rd.close();
			//We're up to date - set the most recent update to current time
			//and get the dota-sound-list and return it.
			if(response.get(0).equals("OK"))
			{
				response = new ArrayList<String>();
				Configuration.setValue("lastUpdate", Long.toString(System.currentTimeMillis()/1000L));
				Scanner s = new Scanner(new File("dota-sound-list.txt"));
				while(s.hasNextLine())
					response.add(s.nextLine());
			}
			//Store the new file if it's not updated
			else
			{
				try 
				{
					FileOutputStream fos = new FileOutputStream(new File("dota-sound-list.txt"));
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
					for(String s : response)
						bw.write(s);
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (java.net.SocketTimeoutException e) {
			JOptionPane.showMessageDialog(null, "Couldn't connect to server.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		notificationPane.setVisible(false);
		return response;
	}
}
