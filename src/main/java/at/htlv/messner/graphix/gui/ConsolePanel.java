package at.htlv.messner.graphix.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import at.htlv.messner.graphix.model.Matrix;


public class ConsolePanel extends JPanel 
{

	private static final long serialVersionUID = 8320890127120058410L;
	private static JTextPane textPane;

	public ConsolePanel() 
        {
		initPanels();
	}

	public void initPanels() 
        {
		setLayout(new BorderLayout());
		textPane = new JTextPane();
		textPane.setBackground(new Color(250, 250, 250));
		textPane.setContentType("text/html");
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void clear() 
        {
		textPane.setText("");
	}

	public void updateWith(Matrix matrix) 
        {

		String text = "<font size =3 face=Fixedsys>";
		text += "<p>" + "<b>Matrix:</b><br>";
		text += matrix;

		text += "<p>" + "<b>Selektierte Kanten:</b><br>";
		ArrayList<ArrayList<Integer>> kanten = matrix.selektierteKanten();
		text += "Anzahl: " + kanten + "<br>";

		text += "<p>" + "<b>Selektierte Knoten:</b><br>";
		ArrayList<Integer> knoten = matrix.selektierteKnoten();

		text += "Anzahl: " + "{ " +  knoten + " }" + "<br>";
		

		textPane.setText(text);
		textPane.setCaretPosition(0);
	}
}
