package at.htlv.messner.graphix;

import java.io.IOException;

import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new HelloWorld();
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new at.htlv.messner.graphix.gui.MainFrame().setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
    }
}
