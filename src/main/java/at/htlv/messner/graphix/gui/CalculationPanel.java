package at.htlv.messner.graphix.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import at.htlv.messner.graphix.model.Matrix;

public class CalculationPanel extends JPanel {

	private static final long serialVersionUID = 1790315753452164325L;
	private static JTextPane textPane;

	public CalculationPanel() {
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
		System.out.println(matrix);

		String text = "<font size =3 face=Fixedsys>";
                //text += "Noch keine Ausgabe ;)";
                
		if (matrix.zusammenhaengend()) {
			text += "<b>EXZENTRIZITÄTEN:</b>" + "<br>";
			int[] exzentrizitaeten = matrix.exzentrizitaeten();
//			text += "Anzahl: " + exzentrizitaeten.length + "<br>";
			for (int i = 0; i < exzentrizitaeten.length; i++) {
				text += "Knoten " + (i + 1) + ": " + exzentrizitaeten[i]
						+ "<br>";
			}
			   text += "<p><b>RADIUS:</b>" + "<br>" + "Kleinste Exzentrizität: "
					+ matrix.radius() + "<br>" + "<p>" + "<b>DURCHMESSER:</b>"
					+ "<br>" + "Größte Exzentrizität: " + matrix.durchmesser()
					+ "<br>" + "<p>" + "<b>ZENTRUM:</b>" + "<br>" + "Knoten: "
					+ matrix.zentrum() + "<br>";
		} else {
			text += "<b>EXZENTRIZITÄTEN:</b>"
					+ "<br> Graph ist nicht zusammenhängend!<br>";
		}
                
		text += "<p>" + "<b>KOMPONENTEN:</b>" + "<br>";
		ArrayList<ArrayList<Integer>> komponenten = matrix.komponenten();
		text += "Anzahl: " + komponenten.size() + "<br>";
		for (int i = 0; i < komponenten.size(); i++) {
			text += "Komponente " + (i + 1) + ": " + komponenten.get(i)
					+ "<br>";
		}
                
		text += "<p>" + "<b>ARTIKULATIONEN:</b>" + "<br>";
		ArrayList<Integer> artikulationen = matrix.artikulationen();
		text += "Anzahl: " + artikulationen.size() + "<br>Knoten: "
				+ artikulationen;

                
		text += "<p>" + "<b>BRÜCKEN:</b><br>" + "Anzahl: "
				+ matrix.bruecken().size() + "<br>";
		String brueckenText = "{ }";
		text += "Kanten:";

		ArrayList<ArrayList<Integer>> bruecken = matrix.bruecken();
		if (matrix.bruecken().size() > 0) {
			brueckenText = "{ " + bruecken.get(0).toString();

			for (int i = 1; i < bruecken.size(); i++) {
				brueckenText += ", " + bruecken.get(i);
			}
			brueckenText += " }";
		}
		text += brueckenText + "<br>";
                /*
		text += "<p>" + "<b>BL�CKE:</b><br>";
		text += "Anzahl: " + matrix.anzahlBloecke() + "<br>";

		text += "<p>" + "<b>BAUM:</b><br>";
		if (matrix.istBaum() == true) {
			text += "Der Graph ist ein Baum!";
		} else if (matrix.istWald() == true) {
			text += "Der Graph ist ein Wald!";
		} else {
			text += "Mindestens eine Komponente ist kein Baum!";
		}*/

		text += "<p>" + "<b>EULER'SCHE LINIE:</b><br>";
                if(matrix.hasEuler())
                {
                    text += "Eulersche Linie vorhanden";
                }
                else
                {
                    text += "Eulersche Linie nicht vorhanden";
                }
		/*if (matrix.hasOffeneEulerscheLinie()) {
			text += "Offene Linie vorhanden!";
			ArrayList<ArrayList<Integer>> eulerWeg = matrix.eulerWeg();
			String eulerWegText = "" + eulerWeg.get(0).toString();
			for (int i = 1; i < eulerWeg.size(); i++) {
				eulerWegText += ", " + eulerWeg.get(i);
			}
			eulerWegText += "";
			text += "<br>" + eulerWegText + "<br>";
		} else if (matrix.hasGeschlosseneEulerscheLinie()) {
			text += "Geschlossene Linie vorhanden!";
			ArrayList<ArrayList<Integer>> eulerWeg = matrix.eulerWeg();
			String eulerWegText = "" + eulerWeg.get(0).toString();
			for (int i = 1; i < eulerWeg.size(); i++) {
				eulerWegText += ", " + eulerWeg.get(i);
			}
			eulerWegText += "";
			text += "<br>" + eulerWegText + "<br>";
		} else {
			text += "Nicht vorhanden!";
		}
		text += "<p>";*/
		textPane.setText(text);
		textPane.setCaretPosition(0);

	}
}
