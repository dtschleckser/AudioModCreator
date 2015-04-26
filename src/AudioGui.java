import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.TableModel;


public class AudioGui extends JFrame {
	private SoundTable st;
	private TableModel stm;
	public AudioGui()
	{
		/*
		 * Create the panel for tables
		 * (custom and default sounds)
		 * and add the proper elements
		 */
		JPanel tablePanel = new JPanel();
		BoxLayout tableBox = new BoxLayout(tablePanel, BoxLayout.X_AXIS);
		tablePanel.setLayout(tableBox);
		st = new SoundTable();
		//		st = new SoundTable("as");
		stm = st.getTable().getModel();
		CustomTable ct = new CustomTable(stm.getRowCount(), stm.getColumnCount()+1);
		ct.setPreferredSize(new Dimension(700, (int) ct.getSize().getHeight()));
		JButton jb = new JButton("->");
		/*
		 * Transfer the selected values from 
		 * the SoundTable to the CustomTable.
		 */
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = st.getTable().getSelectedRow();
				int rowCount = st.getTable().getSelectedRowCount();
				for(int i = row; i < (row + rowCount); i++)
				{
					ct.getTable().getModel().setValueAt(stm.getValueAt(i, 0), getClosestEmptyPair(ct), 0);
					ct.getTable().getModel().setValueAt(stm.getValueAt(i, 1), getClosestEmptyPair(ct), 1);
				}
			}
		});

		/*
		 * Create the panel for searching
		 * and add its elements
		 */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel searchPanel = new JPanel();
		BoxLayout searchBox = new BoxLayout(searchPanel, BoxLayout.X_AXIS);
		searchPanel.setLayout(searchBox);
		JTextField jtf = new JTextField();
		jtf.setSize(new Dimension(100, 40));
		jtf.setMaximumSize(new Dimension(100, 40));
		JButton sb = new JButton("Search");
		sb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				st.getJSPFromFilter(jtf.getText());
				stm = st.getTable().getModel();

			}

		});

		JPanel compilePanel = new JPanel(new FlowLayout());
		compilePanel.setLayout(new BoxLayout(compilePanel, BoxLayout.Y_AXIS));
		JButton compileButton = new JButton("Compile");
		JLabel compZipLabel = new JLabel("Create a ZIP file? ");
		JCheckBox compZipOut = new JCheckBox();
		JTextField compName = new JTextField();
		JLabel customDotaPath = new JLabel("If you have a custom path for your DOTA installation, put it here: ");
		JTextField customPath = new JTextField();

		compileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < ct.getTable().getModel().getRowCount(); i++)
				{

					Object filePath = ct.getTable().getModel().getValueAt(i, 0);
					Object fileName = ct.getTable().getModel().getValueAt(i, 1);
					Object newFile = ct.getTable().getModel().getValueAt(i, 2);
					if(filePath != null && fileName != null && newFile != null)
					{
						try
						{
							File modsFile = new File("\\mods\\");
							if(!(modsFile.exists()))
								modsFile.mkdir();
							String from = ((String)newFile);//.replace("\\", "/");
							String to = (System.getProperty("user.dir")+"\\mods"+compName.getText()+"\\"+filePath+""+fileName);//.replace("\\", "/");
							String toPath = System.getProperty("user.dir")+"\\mods"+compName.getText()+"\\"+filePath;
							//new File(System.getProperty("user.dir")+"\\mods"+compName.getText()+"\\"+filePath).mkdir();
							System.out.println(toPath);
							File toFile = new File(toPath);
							toFile.mkdirs();
							
							System.out.println(from);
							System.out.println(to);
							copyFile(new File(from),
									new File(to));
						} 
						catch(IOException ex)
						{
							ex.printStackTrace();
						}
					}
				}
			}
		});

		compilePanel.add(compZipLabel);
		compilePanel.add(compZipOut);
		compilePanel.add(compileButton);
		compilePanel.add(compName);
		searchPanel.add(jtf);
		searchPanel.add(sb);
		tablePanel.add(st);
		tablePanel.add(jb);
		tablePanel.add(ct);

		/*
		 * Position the search panel at the top of the page
		 * and the tables at the bottom.
		 */
		JPanel totalContainer = new JPanel();
		totalContainer.setLayout(new BorderLayout());
		totalContainer.add(searchPanel, BorderLayout.PAGE_START);
		totalContainer.add(tablePanel, BorderLayout.CENTER);
		totalContainer.add(compilePanel, BorderLayout.EAST);
		this.add(totalContainer);
	}
	public static void copyFile(File sourceFile, File destFile) throws IOException 
	{
		if(!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally {
			if(source != null) {
				source.close();
			}
			if(destination != null) {
				destination.close();
			}
		}
	}
	public int getClosestEmptyPair(CustomTable jt)
	{
		TableModel jtm = jt.getTable().getModel();
		for(int i = 0; i < jtm.getRowCount(); i++)
			if(jtm.getValueAt(i, 0) == null || jtm.getValueAt(i, 1) == null)
				return i;
		return -1;
	}
}
