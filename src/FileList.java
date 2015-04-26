import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class FileList {
	public static ArrayList<String> retrieveSoundData(boolean displayWindow)
	{
		System.out.println("Current time: " + Long.toString(System.currentTimeMillis()/1000L));
		JFrame notificationPane = new JFrame();
		if(displayWindow)
		{
			notificationPane.setLayout(new FlowLayout());
			notificationPane.add(new JLabel("Updating sound files - Please wait."));
			notificationPane.setVisible(true);
			notificationPane.setSize(200, 70);
		}
		// First time running, so let's force an update
		if(Configuration.getValue("lastUpdate") == null)
			Configuration.setValue("lastUpdate", Long.toString(0L));
		
		/* Get the response from the server, can either be
		   the updated list of sounds if you're out of 
		   date or "OK" if you're up to date */ 
		ArrayList<String> response = loadFromServer();
		
		//Load from local file, up to date.
		if(response.get(0).equals("Ok"))
			response = loadFromFile();
		
		//Not up to date - let's store the response to a local file
		else
			storeFile(response);
		notificationPane.setVisible(false);
		
		return response;
	}
	private static ArrayList<String> loadFromServer()
	{
		ArrayList<String> response = new ArrayList<String>();
		try
		{
			//get the sound files from server
			URL oUrl = new URL("http://oxivod.me/get-sound-files.php?prevUpdate="+Configuration.getValue("lastUpdate"));
			HttpURLConnection servCon = (HttpURLConnection) oUrl.openConnection();
			servCon.setConnectTimeout(10000);
			InputStream is = servCon.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			while((line = rd.readLine()) != null) 
				response.add(line);
			rd.close();
		}catch(IOException ex)
		{
			
		}
		return response;
	}
	private static ArrayList<String> loadFromFile() {
		ArrayList<String> response = new ArrayList<String>();
		Configuration.setValue("lastUpdate", Long.toString(System.currentTimeMillis()/1000L));
		if(new File("dota-sound-list.txt").exists())
		{
			Scanner s;
			try {
				s = new Scanner(new File("dota-sound-list.txt"));
				while(s.hasNextLine())
					response.add(s.nextLine());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//If the file doesn't exist but the response is OK,
			//it won't have any data - force an update and re-run.
		}
		else
		{
			Configuration.setValue("lastUpdate", Long.toString(0L));
			FileList.retrieveSoundData(false);
		}
		return response;
	}
	private static void storeFile(ArrayList<String> response)
	{

		try 
		{
			FileOutputStream fos = new FileOutputStream(new File("dota-sound-list.txt"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(String s : response)
			{
				bw.write(s);
				bw.write("\r");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(Configuration.getValue("lastUpdate").equals(Long.toString(0L)))
			Configuration.setValue("lastUpdate", Long.toString(System.currentTimeMillis()/1000L));
	}
}
