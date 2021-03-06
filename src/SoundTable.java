import java.awt.Component;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SoundTable extends JPanel {
	private JTable table;
	private JScrollPane jsp;
	private String[] columnNames = new String[2];
	private ArrayList<String> strData;
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
		if(strData == null)
			strData = FileList.retrieveSoundData(true);
		
		ArrayList<String> filteredList = new ArrayList<String>();
		for(int i = 0; i < strData.size(); i++)
		{
			String str = strData.get(i);
			if(strData.get(i).contains(filter))
				filteredList.add(str);
		}
		Object[][] data = new Object[filteredList.size()][columnNames.length];
		for(int i = 0; i < filteredList.size(); i++)
		{
			//split data into filenames & filepaths
			String rawFile = filteredList.get(i);
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
