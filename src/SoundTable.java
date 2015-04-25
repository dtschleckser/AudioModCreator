import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class SoundTable extends JPanel {
	private JTable table;
	private JScrollPane jsp;
	private String[] columnNames = new String[2];
	public SoundTable()
	{
		super(new GridLayout(1, 0));
		
		columnNames[0] = "File Path";
		columnNames[1] = "File Name";

		//fill the tables with everything
		getJSPFromFilter("");
		
	}
	public void getJSPFromFilter(String search)
	{
		System.out.println(isJSPAdded());
		if(isJSPAdded())
			remove(jsp);
		Object[][] data = getDataMatches(search);
		table = new JTable(data, columnNames);
		if(jsp == null)
			jsp = new JScrollPane(table);
		jsp.setViewportView(table);
		add(jsp);
	}
	public boolean isJSPAdded()
	{
		for(Component c : getComponents())
			if(c.equals(jsp))
				return true;
		return false;
	}
	public Object[][] getDataMatches(String filter)
	{
		ArrayList<String> strData = new ArrayList<String>();
		String[] fileList = {"dota-sound-list.txt","vo-sound-list.txt"};
		int results = 0;
		for(String file : fileList)
		{
			try {
				FileInputStream input = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(input);
				Scanner sc = new Scanner(bis);
				while(sc.hasNext())
				{
					String s = sc.nextLine();
					if(s.contains(filter))
					{
						results++;
						strData.add(s);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println(results + " results found.");
		Object[][] data = new Object[strData.size()][columnNames.length];

		for(int i = 0; i < strData.size(); i++)
		{
			//split data into filenames & filepaths
			String rawFile = strData.get(i);
			String fileName = rawFile.substring(rawFile.lastIndexOf('\\')+1);
			String filePath = rawFile.substring(0, rawFile.lastIndexOf('\\')+1);
			data[i][1] = fileName;
			data[i][0] = filePath;
		}
		return data;
	}
	public JTable getTable()
	{
		return table;
	}
	public void setTable(JTable t)
	{
		table = t;
	}
}
