import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class CustomTable extends JPanel {
	public JTable table;
	public CustomTable(int rows, int cols)
	{
		super(new GridLayout(1,0));

		String[] columnNames = {"File Path", "File Name", "New File"};
		Object[][] data = new Object[rows][cols];

		table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		
		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//Add the scroll pane to this panel.
		this.add(scrollPane);

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col == 2) {

					JFileChooser jfc = new JFileChooser();
					int returnVal = jfc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File f = jfc.getSelectedFile();
						getTable().getModel().setValueAt(f.getAbsolutePath(), row, 2);
					}
				}
			}
		});
	}
	public JTable getTable()
	{
		return table;
	}
}
