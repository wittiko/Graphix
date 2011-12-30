package at.htlv.messner.graphix.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
/**
 * 
 * 
 * @author Gregor Messner
 *
 */
public class Matrix implements Serializable, MatrixInt {
	private static final long serialVersionUID = -7528821221945142099L;

	// 2-dimensionales Array, welches immer quadratisch sein wird.
	private final int[][] values;

	// Die Standardgröße der Matrix
	public final static int DEFAULT_SIZE = 7;

	/**
	 *  Parameterloser Konstruktor wo die Standardgröße übernommen wird.
	 */
	public Matrix() {
		this(DEFAULT_SIZE);
	}

	/**
	 * Konstruktor wo Matrixgröße zwischen 1 und 15 übergeben werden kann.
	 * @param Größe der Matrix
	 */
	
	public Matrix(int size) 
	{
		if (size < 1 || size > 15) {
			throw new IllegalArgumentException(
					"illegal dimension. expected value: between 0 and 15 (inclusive), actual value: "
							+ size);
		}
		// Erstellt ein neues, leeres Aray, wo alle Werte anfangs 0 sind.
		values = new int[size][size];
	}

	// Copy-Konstruktor, kopiert die Daten der übergebenen Matrix in eine Neue.
	public Matrix(Matrix original) {
		// Erstellt zuerst eine neue Matrix der übergebenen Größe.
		this(original.getDimension());
		// Durchlaufe alle Zeilen und Spalten und kopiere sämtliche Werte.
		for (int row = 0; row < original.getDimension(); row++) {
			for (int column = 0; column < original.getDimension(); column++) {
				setValueAt(row, column, original.getValueAt(row, column));
			}
		}
	}

	// Dimension entspricht der Länge des 2-dimensionalen Arrays.
	public int getDimension() {
		return values.length;
	}

	// Frage den Werte für eine Zeile und Spalte ab.
	public int getValueAt(int row, int column) {
		if (row < 0 || row >= values.length) {
			throw new IllegalArgumentException(
					"illegal row index. expected value: between 0 and "
							+ values.length + " (inclusive), actual value: "
							+ row);
		}

		if (column < 0 || column >= values[row].length) {
			throw new IllegalArgumentException(
					"illegal column index. expected value: between 0 and "
							+ values[row].length
							+ " (inclusive), actual value: " + column);
		}
		return values[row][column];
	}

	// Setze den Wert für eine bestimmte Zeile und Spalte.
	public void setValueAt(int row, int column, int value) {
		if (row < 0 || row >= values.length) {
			throw new IllegalArgumentException(
					"illegal row index. expected value: between 0 and "
							+ values.length + " (inclusive), actual value: "
							+ row);
		}
		if (column < 0 || column >= values[row].length) {
			throw new IllegalArgumentException(
					"illegal column index. expected value: between 0 and "
							+ values[row].length
							+ " (inclusive), actual value: " + column);
		}
		values[row][column] = value;
	}

	// Multipliziert die aktuelle Matrix mit der übergebenen und gibt eine neue
	// Matrix als Ergebnis zurück.
	public Matrix multiply(Matrix otherMatrix) {
		if (otherMatrix.getDimension() != getDimension()) {
			throw new IllegalArgumentException(
					"illegal matrix size, must have same dimensions.");
		}

		// Erstelle eine neue leere Matrix, welche das Ergebnis aufnimmt.
		Matrix resultMatrix = new Matrix(getDimension());
		// Multipliziere die zwei Matrizen.
		for (int row = 0; row < resultMatrix.values.length; row++) {
			for (int column = 0; column < resultMatrix.values[row].length; column++) {
				for (int otherColumn = 0; otherColumn < resultMatrix
						.getDimension(); otherColumn++) {
					resultMatrix.values[row][column] += values[row][otherColumn]
							* otherMatrix.values[otherColumn][column];
				}
			}
		}
		// Gebe die Matrix zurück, welche das Ergebnis enthält.
		return resultMatrix;
	}

	// Gibt die n-te Potenz der aktuellen Matrix zur�ck.
	public Matrix power(int n) {
		if (n < 1) {
			throw new IllegalArgumentException(
					"illegal power. expected: n >= 1, actual: " + n);
		}
		// Erstelle eine Kopie der aktuellen Matrix (entspricht 1.Potenz)
		Matrix resultMatrix = new Matrix(this);
		// F�r jeden weiteren Potenzschritt jenseits 1 ist eine Multiplikation
		// mit der Original-Matrix notwendig.
		for (int i = 1; i < n; i++) {
			resultMatrix = resultMatrix.multiply(this);
		}
		// Gebe die potenzierte Matrix zur�ck.
		return resultMatrix;
	}

	// Berechnung der Wegmatrix
	public Matrix wegMatrix() {

		Matrix resultMatrix = new Matrix(getDimension());
		// Diagonale wird auf 1 gesetzt.
		for (int i = 0; i < resultMatrix.getDimension(); i++) {
			resultMatrix.values[i][i] = 1;
		}
		// Durchlaufe alle Potenzen von 1 bis vor die Dimension.
		for (int power = 1; power < getDimension(); power++) {
			// Berechne die i-te Potenz der aktuellen Matrix.
			Matrix powerMatrix = power(power);

			boolean matrixUpdated = false;
			// Durchlaufe alle Zeilen und Spalten.
			for (int row = 0; row < powerMatrix.getDimension(); row++) {
				for (int column = 0; column < powerMatrix.getDimension(); column++) {
					// Enth�lt die Potenzmatrix f�r die aktuelle Zeile und
					// Spalte einen Wert > 0, dann setze diese Stelle auf 1.
					if (powerMatrix.values[row][column] > 0) {
						resultMatrix.values[row][column] = 1;
						// Merke, dass die Matrix aktualisiert worden ist.
						matrixUpdated = true;
					}
				}
			}
			// Ist die Matrix in der aktuellen Wiederholung nicht aktualisiert
			// worden, dann breche ab.
			if (!matrixUpdated) {
				break;
			}
		}
		// Gebe die Wegmatrix zur�ck.
		return resultMatrix;
	}

	// Berechnung der Distanzmatrix
	public Matrix distanzMatrix() {

		Matrix resultMatrix = new Matrix(getDimension());
		// Setze alle Werte in der Matrix auf "unendlich", nur die Diagonale
		// wird auf 0 gesetzt.
		for (int row = 0; row < resultMatrix.getDimension(); row++) {
			for (int column = 0; column < resultMatrix.getDimension(); column++) {
				if (column == row) {
					resultMatrix.values[row][column] = 0;
				} else {
					// Zur Darstellung des Zeichens "unendlich" wird
					// Integer.MIN_VALUE verwendet, welches im Panel als
					// "unendlich" interpretiert wird.
					resultMatrix.values[row][column] = Integer.MIN_VALUE;
				}
			}
		}
		// Durchlaufe alle Potenzen ab 1 bis vor die Dimension.
		for (int power = 1; power < getDimension(); power++) {
			// Berechne die aktuelle Potenz der aktuellen Matrix.
			Matrix powerMatrix = power(power);

			boolean matrixUpdated = false;
			// Durchlaufe alle Zeilen und Spalten.
			for (int row = 0; row < powerMatrix.getDimension(); row++) {
				for (int column = 0; column < powerMatrix.getDimension(); column++) {

					// Ist der Wert in der Potenzmatrix > 0 und in der
					// Ergebenismatrix "unendlich", dann soll die Potenzgr��e
					// als Wert �bernommen werden.
					if (powerMatrix.values[row][column] > 0
							&& resultMatrix.values[row][column] == Integer.MIN_VALUE) {
						resultMatrix.values[row][column] = power;
						// Merke, dass sich die Matrix ge�nert hat.
						matrixUpdated = true;
					}
				}
			}
			// Hat sich die Matrix nicht ge�ndert, dann breche ab.
			if (!matrixUpdated) {
				break;
			}
		}
		// Gebe die Distanzmatrix zur�ck.
		return resultMatrix;
	}

	// Berechnung der Exzentrizit�ten
	public int[] exzentrizitaeten() {

		Matrix distanzMatrix = distanzMatrix();
		// Erstelle ein Array, das genau so gro� ist , wie die Distanzmatrix
		// hoch ist, wo an jeder Stelle das Maximum gespeichert wird.
		int[] exzentrizitaeten = new int[distanzMatrix.getDimension()];
		// Durchlaufe alle Zeilen und Spalten.
		for (int row = 0; row < distanzMatrix.getDimension(); row++) {
			for (int column = 0; column < distanzMatrix.getDimension(); column++) {
				// Bestimme, welcher Wert gr��er it: Der bisher f�r diese Zeile
				// gespeicherte, oder der in der Distanzmatrix f�r diese Zeile
				// und Spalte stehende.
				exzentrizitaeten[row] = Math.max(exzentrizitaeten[row],
						distanzMatrix.values[row][column]);
			}
		}
		// Gebe die Exzentrizit�ten zur�ck.
		return exzentrizitaeten;
	}

	// Berechnung des Radius
	public int radius() {
		// Hole die Exzentrizit�ten.
		int[] exzentrizitaeten = exzentrizitaeten();
		// Es wird angenommen, dass der 0. Wert der kleinste ist; das wird nur
		// gemacht, damit der Radius schon beim ersten Vergleich einen Werte aus
		// der Menge m�glicher Ergebnisse enth�lt.
		int radius = exzentrizitaeten[0];
		// Durchlaufe alle restlichen Elemente ab der Stelle 1 und speichere
		// immer das kleinere Element im Radius ab.
		for (int row = 1; row < exzentrizitaeten.length; row++) {
			radius = Math.min(radius, exzentrizitaeten[row]);
		}
		// Gebe den Radius zur�ck.
		return radius;
	}

	// Berechnung des Durchmessers
	public int durchmesser() {
		// Berechnet sich wie der Radius, nur wird hier nach dem Maximum
		// gesucht.
		int[] exzentrizitaeten = exzentrizitaeten();
		int durchmesser = exzentrizitaeten[0];

		for (int i = 1; i < exzentrizitaeten.length; i++) {
			durchmesser = Math.max(durchmesser, exzentrizitaeten[i]);
		}
		return durchmesser;
	}

	// Berechnung des Zentrums
	public ArrayList<Integer> zentrum() {
		// Berechne die Exzentrizit�ten und den Radius.
		int[] exzentrizitaeten = exzentrizitaeten();
		int radius = radius();
		// Erstelle eine Liste (dynamisch) f�r die Knoten.
		ArrayList<Integer> zentren = new ArrayList<Integer>();

		for (int row = 0; row < exzentrizitaeten.length; row++) {
			if (exzentrizitaeten[row] == radius) {
				// Da row dem Index des Knotens entspricht, muss f�r den Knoten
				// row + 1 abgelegt werden.
				zentren.add(row + 1);
			}
		}
		// Gebe die Zentren zur�ck.
		return zentren;
	}

	// Pr�fung, ob Graph zusammenh�ngend ist (Alle Elemente in der Wegmatrix 1).
	public boolean zusammenhaengend() {
		Matrix wegMatrix = wegMatrix();
		// Durchlaufe alle Zeilen und Spalten.
		for (int row = 0; row < wegMatrix.getDimension(); row++) {
			for (int column = 0; column < wegMatrix.getDimension(); column++) {
				if (wegMatrix.values[row][column] != 1) {
					// Wird eine Zeile gefunden, f�r die der Wert nicht 1 ist,
					// dann gibt es keinen Weg und der Graph ist nicht
					// zusammenh�ngend.
					return false;
				}
			}
		}
		// Sind alle Elemente 1, dann wurde die if-Anweisung nie betreten, und
		// am Ende wird festgestellt, dass der Graph zusammenh�ngend ist.
		return true;
	}

	// Berechnet die Komponenten auf Basis der Wegmatrix des aktuellen Knotens.
	public ArrayList<ArrayList<Integer>> komponenten() {
		return komponenten(wegMatrix());
	}

	// L�sst den Benutzer die Wegmatrix �bergeben, f�r welche die Komponenten
	// berechnet werden sollen. Eine Komponenten ist ein Teilgraph.
	private ArrayList<ArrayList<Integer>> komponenten(Matrix wegMatrix) {
		// Erstelle eine Liste, welche mehrere Komponenten aufnehmen kann.
		ArrayList<ArrayList<Integer>> komponenten = new ArrayList<ArrayList<Integer>>();

		for (int row = 0; row < wegMatrix.getDimension(); row++) {
			// Erstelle eine neue leere Liste f�r die aktuelle Komponente
			ArrayList<Integer> neueKomponente = new ArrayList<Integer>();
			// F�ge alle Knoten ein, die von der aktuellen Zeile aus erreichbar
			// sind. Zusammen bilden sie eine Komponenten.
			for (int column = 0; column < wegMatrix.getDimension(); column++) {
				if (wegMatrix.values[row][column] == 1) {
					neueKomponente.add(column + 1);
				}
			}
			// Ist die Komponente nicht leer, dann kann sie potentiell in die
			// List der Komponenten eingef�gt werden.
			if (!neueKomponente.isEmpty()) {
				// Merke, ob sie tats�chlich abgelegt werden soll.
				boolean ablegen = true;
				// Durchlaufe all bereits abgelegten Komponenten mit einem
				// Iterator.
				for (Iterator<ArrayList<Integer>> kompIt = komponenten
						.iterator(); kompIt.hasNext();) {
					// Lese die aktuelle existierende Komponente aus.
					ArrayList<Integer> komponente = kompIt.next();
					// �berpr�fe, ob diese aktuelle Komponente die neue
					// Komponente bereits vollst�ndig enth�lt.
					if (komponente.containsAll(neueKomponente)) {
						ablegen = false;
						break;
					}
					// Enth�lt die neue Komponente jedoch die existierende, dann
					// ist die existierende eine Teilmenge der neuen. Die
					// existierende wird daher aus der Liste entfernt.
					if (neueKomponente.containsAll(komponente)) {
						kompIt.remove();
					}
				}
				// Soll die neue Komponente abgelegt werden, dann wird das
				// gemacht.
				if (ablegen) {
					komponenten.add(neueKomponente);
				}
			}
		}
		return komponenten;
	}

	// Berechnung der Artikulationen (Cutpoints).
	public ArrayList<Integer> artikulationen() {
		// Merke die urspr�ngliche Anzahl an Komponenten.
		int komponenten = komponenten().size();
		// Erstelle eine leere Liste, in der die Knoten aufgenommen werden,
		// welche Artikulationen sind.
		ArrayList<Integer> artikulationen = new ArrayList<Integer>();
		// Durchlaufe alle Knoten.
		for (int entfernterKnoten = 0; entfernterKnoten < getDimension(); entfernterKnoten++) {
			// Erstelle eine Kopie der Adjazenzmatrix
			Matrix copy = new Matrix(this);
			// Entferne s�mtliche Verbindungen f�r den aktuellen Knoten (Zeile
			// und Spalte wird auf 0 gesetzt.)
			for (int i = 0; i < getDimension(); i++) {
				copy.setValueAt(entfernterKnoten, i, 0);
				copy.setValueAt(i, entfernterKnoten, 0);
			}

			// Berechne f�r die ge�nderte Adjazenzmatrix die Wegmatrix und f�r
			// diese die Komponenten.
			ArrayList<ArrayList<Integer>> aktuelleKomponenten = komponenten(copy
					.wegMatrix());
			// Der aktuelle Knoten wird immer bei den Komponenten enthalten
			// sein, denn nur seine Verbindungen wurden gel�scht, er ist aber
			// noch immer Teil des Graphens. Deshalb muss er der Knoten, der nun
			// garantiert als "Insel" auftaucht, aus der Liste von Komponenten
			// entfernt werden. Dazu wird eine Liste angelegt, die nur diesen
			// Knoten enth�lt, anschlie�end wird sie aus der Liste von
			// Komponenten entfernt.
			ArrayList<Integer> listeMitKnoten = new ArrayList<Integer>();
			listeMitKnoten.add(entfernterKnoten + 1);
			aktuelleKomponenten.remove(listeMitKnoten);
			// Anschlie�end wird �berpr�ft, ob die neu entstandene Liste mehr
			// Komponenten enth�lt, als die urspr�ngliche. Wenn ja, dann wurde
			// eine neue Artikulation gefunden, und der Knoten dazu wird
			// gespeichert.
			if (aktuelleKomponenten.size() > komponenten) {
				artikulationen.add(entfernterKnoten + 1);
			}
		}
		// Gebe die Artikulationen zur�ck.
		return artikulationen;
	}

	// Berechnung der Br�cken
	public ArrayList<ArrayList<Integer>> bruecken() {
		// Merke die Br�cken als Paare von Knotennummern.
		ArrayList<ArrayList<Integer>> bruecken = new ArrayList<ArrayList<Integer>>();
		// Erstelle eine Kopie der urspr�nglichen Matrix und merke, wieviel
		// Komponenten sie hat.
		Matrix copy = new Matrix(this);
		int komponenten = copy.komponenten().size();
		// Jede Zeile und Spalte wird durchlaufen, damit jede Kante einzeln
		// entfernt weden kann.
		for (int row = 0; row < copy.getDimension(); row++) {
			for (int column = 0; column < copy.getDimension(); column++) {
				if (copy.values[row][column] == 1) {
					// Ist die aktuelle Kante gesetzt, dann setze sie in beide
					// Richtungen auf 0.
					copy.values[row][column] = 0;
					copy.values[column][row] = 0;
					// Berechne f�r diese ge�nderte Matrix die Wegmatrix, und
					// f�r diese die Komponenten. �bersteigen sie die
					// urspr�ngliche Anzahl an Komponenten, dann ist die
					// verarbeitete Kante eine Br�cke, die gemerkt werden muss.
					if (komponenten(copy.wegMatrix()).size() > komponenten) {
						// Erstelle eine leere Liste.
						ArrayList<Integer> bruecke = new ArrayList<Integer>();
						// Schreibe die zwei Knoten in die Liste, welche die
						// Kante bilden. Schreibe die Knoten in der Reihenfolge,
						// dass zuerst der kleinere Knoten, dann der gr��ere
						// Knoten geschrieben wird. Die Reihenfolge wird
						// eingehalten, damit zwei Listen [2, 3] und [3, 2] als
						// gleich erkannt werden, indem sie immer als [2, 3]
						// geschrieben werden.
						bruecke.add(Math.min(column + 1, row + 1));
						bruecke.add(Math.max(column + 1, row + 1));
						// Wenn die Br�cken die neue Br�cke noch nicht
						// enthalten, dann wird die neue Br�cke in die Liste mit
						// aufgenommen.
						if (!bruecken.contains(bruecke)) {
							bruecken.add(bruecke);
						}
					}
					// Setze die Kante wieder zur�ck, damit die Matrix f�r die
					// n�chste Berechnung korrigiert wird.
					copy.values[row][column] = 1;
					copy.values[column][row] = 1;
				}
			}
		}
		// Gebe die Br�cken zur�ck.
		return bruecken;
	}

	// Berechnung der Kantenanzahl f�r Baumberechnung.
	public int getKantenAnzahl() {
		int anzahl = 0;

		for (int row = 0; row < getDimension(); row++) {
			for (int column = 0; column < getDimension(); column++) {
				if (values[row][column] == 1)
					anzahl++;
			}
		}
		System.out.println("Kantenanzahl " + anzahl / 2);
		return anzahl / 2;
	}

	// Pr�fung, ob Graph ein Baum ist.
	public boolean istBaum() {
		return getKantenAnzahl() - getDimension() + komponenten().size() == 0
				&& komponenten().size() == 1;
	}

	// Pr�fung, ob Graph ein Wald ist.
	public boolean istWald() {
		return getKantenAnzahl() - getDimension() + komponenten().size() == 0
				&& komponenten().size() > 1;
	}

	// Berechnung der Anzahl der Bl�cke.
	public int anzahlBloecke() {
		int aenderungen = komponenten().size();

		for (int artikulation : artikulationen()) {
			Matrix copy = new Matrix(wegMatrix());

			for (int i = 0; i < getDimension(); i++) {
				copy.setValueAt(artikulation - 1, i, 0);
				copy.setValueAt(i, artikulation - 1, 0);
			}
			aenderungen += copy.komponenten().size() - komponenten().size();
		}
		return aenderungen;
	}

	// Liste aller ausgew�hlten Kanten erstellen.
	public ArrayList<ArrayList<Integer>> selektierteKanten() {
		ArrayList<ArrayList<Integer>> selektierteKanten = new ArrayList<ArrayList<Integer>>();
		// Durchlaufe alle Zeilen und Spalten.
		for (int row = 0; row < getDimension(); row++) {
			for (int column = 0; column < getDimension(); column++) {
				// Verarbeite die aktuelle Kante nur, wenn es sich nicht um die
				// Diagonale handelt und der Wert > 0 ist (also eine Kante
				// eingetragen ist f�r diese Verbindung).
				if (values[row][column] > 0) {
					// Erstelle eine neue Liste, in welche die zwei
					// Knoten-Nummern eingetragen werden sollen.
					ArrayList<Integer> neueKante = new ArrayList<Integer>();
					// F�ge zuerst den Knoten mit der kleineren Nummer ein, dann
					// jenen mit der gr��eren.
					neueKante.add(Math.min(row + 1, column + 1));
					neueKante.add(Math.max(column + 1, row + 1));

					if (!selektierteKanten.contains(neueKante)) {
						selektierteKanten.add(neueKante);
					}
				}
			}
		}
		return selektierteKanten;
	}

	// Liste aller ausgew�hlter Knoten erstellen.
	public ArrayList<Integer> selektierteKnoten() {
		ArrayList<Integer> selektierteKnoten = new ArrayList<Integer>();
		// Durchlaufe alle Zeilen und Spalten.
		for (int row = 0; row < getDimension(); row++) {
			for (int column = 0; column < getDimension(); column++) {
				// Ignoriere die Diagonale. Es werden nur Knoten gewertet, die
				// abseits der Diagonale eingetragen worden sind, deren Wert
				// also > 0 ist.
				if (values[row][column] > 0) {
					// Wurden Zeile oder Spalte noch nicht in die Liste
					// eingetragen, dann f�ge sie ein.
					if (!selektierteKnoten.contains(row + 1)) {
						selektierteKnoten.add(row + 1);
					}
					if (!selektierteKnoten.contains(column + 1)) {
						selektierteKnoten.add(column + 1);
					}
				}
			}
		}
		// Sortiere die Liste von Knoten.
		Collections.sort(selektierteKnoten);
		return selektierteKnoten;
	}

	// Pr�fe, ob Graph eine offene eulersche Linie enth�lt.
	public boolean hasOffeneEulerscheLinie() {
		if (komponenten().size() > 1) {
			return false;
		}
		int ungeradeGrade = 0;
		for (int row = 0; row < getDimension(); row++) {
			int kantenGrad = 0;
			for (int column = 0; column < getDimension(); column++) {
				if (values[row][column] == 1) {
					kantenGrad++;
				}
			}
			if (kantenGrad % 2 != 0) {
				ungeradeGrade++;
			}
		}
		return ungeradeGrade == 2;
	}

	// Pr�fe, ob Graph eine geschlossene eulersche Linie enth�lt.
	public boolean hasGeschlosseneEulerscheLinie() {
		if (komponenten().size() > 1) {
			return false;
		}
		int ungeradeGrade = 0;
		for (int row = 0; row < getDimension(); row++) {
			int kantenGrad = 0;
			for (int column = 0; column < getDimension(); column++) {
				if (values[row][column] == 1) {
					kantenGrad++;
				}
			}
			if (kantenGrad % 2 != 0) {
				ungeradeGrade++;
			}
		}
		return ungeradeGrade == 0;
	}

	// Berechnung des Eulerweges
	public ArrayList<ArrayList<Integer>> eulerWeg() {

		// Pr�fe auf geschlossenen oder offenen Eulerweg
		if (!hasGeschlosseneEulerscheLinie() && !hasOffeneEulerscheLinie()) {
			throw new IllegalArgumentException("Kein Eulerweg vorhanden!");
		}

		// Erstelle f�r jede Kante ihr Gegenst�ck in einer neuen Liste. Kante
		// 1-2 wird auch als 2-1 erstellt, damit alle Richtungen m�glich sind.
		ArrayList<ArrayList<Integer>> ungerichteteKanten = selektierteKanten();
		ArrayList<ArrayList<Integer>> umgedrehteKanten = new ArrayList<ArrayList<Integer>>();
		for (ArrayList<Integer> kante : ungerichteteKanten) {
			ArrayList<Integer> umgedrehteKante = new ArrayList<Integer>();

			umgedrehteKante.add(kante.get(1));
			umgedrehteKante.add(kante.get(0));
			umgedrehteKanten.add(umgedrehteKante);
		}

		// Erstelle eine Liste wo sowohl alle urspr�nglichen Kanten, als auch
		// die jeweils umgedrehten Kanten hinzugef�gt werden.
		ArrayList<ArrayList<Integer>> gerichteteBeideRichtungen = new ArrayList<ArrayList<Integer>>();
		gerichteteBeideRichtungen.addAll(ungerichteteKanten);
		gerichteteBeideRichtungen.addAll(umgedrehteKanten);

		// L�se mit vollst�ndiger Enumeration der Kanten, dabei wird jede Kante
		// mit jeder kombiniert. Der erste Parameter ist die bisherge L�sung (am
		// Anfang leer) und der zweite Parameter ist die Liste der verbleibenden
		// Kanten (am Anfang alle).
		return eulerWegRekursiv(new ArrayList<ArrayList<Integer>>(),
				gerichteteBeideRichtungen);
	}

	// Rekursive Methode zur Berechnung des Eulerweges
	private ArrayList<ArrayList<Integer>> eulerWegRekursiv(
			ArrayList<ArrayList<Integer>> currentSolution,
			ArrayList<ArrayList<Integer>> remainingEdges) {

		// Durchlaufe alle verbleibenden Kanten.
		for (ArrayList<Integer> edge : remainingEdges) {

			// Ist die L�sung noch leer, dann f�ge die Kante hinzu. In jedem
			// anderen Fall muss die Kante eine Verbindung zur letzten Kante der
			// bisherigen L�sung haben.
			if (currentSolution.size() == 0
					|| currentSolution.get(currentSolution.size() - 1).get(1) == edge
							.get(0)) {

				// Die neue L�sung ist eine Kopie der alten L�sung, welche nun
				// auch die neue Kante enth�lt.
				ArrayList<ArrayList<Integer>> newSolution = new ArrayList<ArrayList<Integer>>(
						currentSolution);
				newSolution.add(edge);

				// Wenn die Kante 1-2 genutzt worden ist, dann f�llt auch das
				// Gegenst�ck 2-1 aus der Menge m�glicher Kanten und daher wird
				// die Kante auch entfernt.
				ArrayList<Integer> oppositeEdge = new ArrayList<Integer>();
				oppositeEdge.add(edge.get(1));
				oppositeEdge.add(edge.get(0));

				// Die neue Menge an verbleibenden Kanten, ist die alte Menge an
				// verbleibender Kanten ohne der neu hinzugef�gten Kante.
				ArrayList<ArrayList<Integer>> newRemainingEdges = new ArrayList<ArrayList<Integer>>(
						remainingEdges);

				newRemainingEdges.remove(edge);
				newRemainingEdges.remove(oppositeEdge);

				// Berechne den n�chsten Schritt rekursiv und merke, was zur�ck
				// gegeben wird.
				ArrayList<ArrayList<Integer>> nextSolution = eulerWegRekursiv(
						newSolution, newRemainingEdges);

				// Eine L�sung wurde gefunden, wenn die L�sung so gro� ist, wie
				// die Gr��e der aktuellen L�sung plus die Anzahl der H�lfte der
				// derzeit verbleibenden Kanten.
				if (nextSolution.size() == currentSolution.size()
						+ remainingEdges.size() / 2) {

					// Falls eine L�sung gefunden worden ist, dann gebe sie
					// zur�ck. Wurde keine gefunden, dann r�cke zur n�chsten
					// Kante vor und kombiniere neu.
					return nextSolution;
				}
			}
		}
		// Wurde keine vollst�ndige L�sung gefunden, dann gebe zur�ck, womit die
		// Methode aufgerufen worden ist.
		return currentSolution;
	}
}