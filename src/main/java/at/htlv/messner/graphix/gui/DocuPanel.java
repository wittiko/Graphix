package at.htlv.messner.graphix.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class DocuPanel extends JPanel {

	private static final long serialVersionUID = 8320890127120058410L;
	private static JTextPane textPane;

	public DocuPanel() {
		initPanels();
	}

	public void initPanels() {
		setLayout(new BorderLayout());
		textPane = new JTextPane();
		textPane.setBackground(new Color(250, 250, 250));
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane, BorderLayout.CENTER);

		try {
			textPane.read(getClass().getResourceAsStream("/scripts/Dokumentation.txt"),
					"");
		} catch (IOException e) {
			e.printStackTrace();
			textPane.setText("Error reading file.");
		}
		textPane.setCaretPosition(0);
	}
}
