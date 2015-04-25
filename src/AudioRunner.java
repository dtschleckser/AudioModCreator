import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class AudioRunner {
	public static void main(String[]args)
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AudioGui ag = new AudioGui();
		ag.setSize(1200,800);
		ag.setVisible(true);
	}
}
