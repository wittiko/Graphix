package at.htlv.messner.graphix.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import at.htlv.messner.graphix.model.Matrix;

public class MonitoringPanel extends JPanel {

	private static final long serialVersionUID = 8320890127120058410L;
	private static JTextPane textPane;

	public MonitoringPanel() {
		initPanels();
	}

	public void initPanels() {
		setLayout(new BorderLayout());
		textPane = new JTextPane();
		textPane.setBackground(new Color(250, 250, 250));
		textPane.setContentType("text/html");
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void clear() {
		textPane.setText("");
	}

	public void updateWith(Matrix matrix) {

		String text = "<font size =3 face=Fixedsys>";

		text += "<b>Beteiligte Knoten:</b><br>" + "Anzahl: "
				+ matrix.getDimension() + "<br>";
		String knotenText = "{ }";
		text += "Knoten:";
		ArrayList<Integer> knoten = matrix.selektierteKnoten();
		if (matrix.selektierteKnoten().size() > 0) {
			knotenText = "{ " + knoten.get(0).toString();

			for (int i = 1; i < knoten.size(); i++) {
				knotenText += ", " + knoten.get(i);
			}
			knotenText += " }";
		}
		text += knotenText + "<br>";

		text += "<p>" + "<b>Ausgew�hlte Kanten:</b><br>" + "Anzahl: "
				+ matrix.getKantenAnzahl() + "<br>";
		String kantenText = "{ }";
		text += "Kanten:";

		ArrayList<ArrayList<Integer>> kanten = matrix.selektierteKanten();
		if (matrix.selektierteKanten().size() > 0) {
			kantenText = "{ " + kanten.get(0).toString();

			for (int i = 1; i < kanten.size(); i++) {
				kantenText += ", " + kanten.get(i);
			}
			kantenText += " }";
		}
		text += kantenText + "<br>";

		int wert = 0;
		for (int potenz = 1; potenz <= matrix.getDimension(); potenz++) {
			text += "<p><b>Adjazenzmatrix" + "<sup>" + potenz
					+ "</sup> (x)</b><br>";
			Matrix power = matrix.power(potenz);

			int max = 0;
			for (int row = 0; row < power.getDimension(); row++) {
				for (int column = 0; column < power.getDimension(); column++) {
					max = Math.max(max, power.getValueAt(row, column));

					// if (matrix.getValueAt(row, column) > max) {
					// max = matrix.getValueAt(row, column);
					// }
				}
			}

			int anzahlZiffern = Integer.toString(max).length();

			for (int row = 0; row < matrix.getDimension(); row++) {
				text += "<pre>" + "{ ";
				for (int column = 0; column < matrix.getDimension(); column++) {
					wert = power.getValueAt(row, column);

					text += String.format("<b>%" + anzahlZiffern + "d</b> ",
							wert);
				}
				text += "}" + "<br>" + "</pre>";
			}
		}
		textPane.setText(text);
		textPane.setCaretPosition(0);
	}
}
