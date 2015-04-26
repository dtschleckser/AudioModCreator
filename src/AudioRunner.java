import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class AudioRunner {
	public static void main(String[]args)
	{
		Configuration.loadConfig();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		//Update the local files for sound lists
		//FileList.updateData();
		AudioGui ag = new AudioGui();
		ag.setTitle("DOTA 2 Audio Mod Creator");
		ag.setSize(1200,800);
		ag.setVisible(true);
	}
}
