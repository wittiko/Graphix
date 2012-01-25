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

	
	/**
	 * 2-dimensionales Array, welches immer quadratisch sein wird.
	 */
	private final int[][] values;

	
	/**
	 * Die Standardgröße der Matrix
	 */
	public final static int DEFAULT_SIZE = 7;

	/**
	 *  Parameterloser Konstruktor wo die Standardgröße übernommen wird.
	 */
	public Matrix() 
        {
		this(DEFAULT_SIZE);
	}

	/**
	 * Konstruktor wo Matrixgröße zwischen 1 und 15 übergeben werden kann.
	 * @param size
	 */
	
	public Matrix(int size) 
	{
		if (size < 1 || size > 15) 
                {
			throw new IllegalArgumentException(
					"illegal dimension. expected value: between 0 and 15 (inclusive), actual value: "
							+ size);
                }
		values = new int[size][size];
	}

	
	/**
	 * Kopiert die übergebene Matrix in das Attribut dieses Objektes.
	 * @param original
	 * 
	 */
	public Matrix(Matrix original) 
        {
		// Erstellt zuerst eine neue Matrix der übergebenen Größe.
		this(original.getDimension());
		// Durchlaufe alle Zeilen und Spalten und kopiere sämtliche Werte.
		for (int row = 0; row < original.getDimension(); row++) {
			for (int column = 0; column < original.getDimension(); column++) {
				setValueAt(row, column, original.getValueAt(row, column));
			}
		}
	}

	/**
         * 
         * @return dimension - Die Größe der Matrix
         */
	public int getDimension() 
        {
		return values.length;
	}

	/**
	 * Liefert einen Wert an der übergebenen Position zurück.
	 * @param row
	 * @param column
	 * @return value
	 * 
	 */
	public int getValueAt(int row, int column) 
	{
		if (row < 0 || row >= values.length) 
                {
			throw new IllegalArgumentException(
					"illegal row index. expected value: between 0 and "
							+ values.length + " (inclusive), actual value: "
							+ row);
		}

		if (column < 0 || column >= values[row].length) 
                {
			throw new IllegalArgumentException(
					"illegal column index. expected value: between 0 and "
							+ values[row].length
							+ " (inclusive), actual value: " + column);
		}
		return values[row][column];
	}

	/**
	 * Setzte den übergebenen Wert an der übergebenen Position.
	 * @param row
	 * @param column
	 * @param value
	 * 
	 */
	public void setValueAt(int row, int column, int value) 
        {
		if (row < 0 || row >= values.length) 
                {
			throw new IllegalArgumentException(
					"illegal row index. expected value: between 0 and "
							+ values.length + " (inclusive), actual value: "
							+ row);
		}
		if (column < 0 || column >= values[row].length) 
                {
			throw new IllegalArgumentException(
					"illegal column index. expected value: between 0 and "
							+ values[row].length
							+ " (inclusive), actual value: " + column);
		}
		values[row][column] = value;
	}

	/**
	 * Multipliziert die übergebene Matrix mit der aktuellen und gibt die daraus resultierende Matrix zurück.
	 * @param matrix
	 * @return result
	 * 
	 */
	public Matrix multiply(Matrix otherMatrix) 
        {
		if (otherMatrix.getDimension() != getDimension()) 
                {
			throw new IllegalArgumentException(
					"illegal matrix size, must have same dimensions.");
		}

		// Erstelle eine neue leere Matrix, welche das Ergebnis aufnimmt.
		Matrix resultMatrix = new Matrix(getDimension());
		// Multipliziere die zwei Matrizen.
		for (int row = 0; row < resultMatrix.values.length; row++) 
                {
			for (int column = 0; column < resultMatrix.values[row].length; column++) 
                        {
				for (int otherColumn = 0; otherColumn < resultMatrix.getDimension(); otherColumn++) 
                                {
					resultMatrix.values[row][column] += values[row][otherColumn] * otherMatrix.values[otherColumn][column];
				}
			}
		}
		return resultMatrix;
	}

	/**
	 * Gibt die Potenz einer Matrix mit dem übergebenen Wert zurück.
	 * @param n
	 * @return result
	 */
	public Matrix power(int n) 
        {
		if (n < 1) 
                {
			throw new IllegalArgumentException(
					"illegal power. expected: n >= 1, actual: " + n);
		}
		Matrix resultMatrix = new Matrix(this);
		for (int i = 1; i < n; i++) 
                {
			resultMatrix = resultMatrix.multiply(this);
		}
		return resultMatrix;
	}
        
        /**
         * 
         * @return knotenGrade - Liefert ein Array mit den KnotenGraden zurück
         */
        public int[] getKnotenGrad()
        {
            int[] knotenGrade = new int[getDimension()];
            for(int i = 0; i < getDimension(); i++)
            {
                knotenGrade[i] = getKnotenGrad(i);
            }
            return knotenGrade;
        }
        
        /**
         * 
         * @param knoten
         * @return knotengrad - Liefert den Knotengrad des übergebenen Knotens zurück
         */
        public int getKnotenGrad(int knoten)
        {
            int knotenGrad = 0;
            if(knoten < getDimension())
            {
                for(int i = 0; i < getDimension(); i++)
                {
                    knotenGrad += getValueAt(knoten, i);
                }
            }
            return knotenGrad;
        }

	/**
	 * Berechnet die Wegmatrix und gibt sie zurück.
	 * @return wegmatrix
	 * 
	 */
	public Matrix wegMatrix() {

		Matrix resultMatrix = new Matrix(getDimension());
                for(int row = 0; row < resultMatrix.getDimension(); row++)
                {
                    for(int column = 0; column < resultMatrix.getDimension(); column++)
                    {
                        if(column == row)
                        {
                            resultMatrix.setValueAt(row, column, 1);
                        }
                        
                    }
                }
                
                for(int power = 1; power < resultMatrix.getDimension(); power++)
                {
                    boolean updated = false;
                    boolean completed = true;
                    Matrix potenzmatrix = power(power);
                    // Wenn die Wegmatrix lauter 1er hat setze completed auf false
                    for(int row = 0; row < resultMatrix.getDimension(); row++)
                    {
                        for(int column = 0; column < resultMatrix.getDimension(); column++)
                        {
                            if(resultMatrix.getValueAt(row, column) == 0)
                            {
                                completed = false;
                            }
                        }
                    }
                    for(int row = 0; row < resultMatrix.getDimension(); row++)
                    {
                        for(int column = 0; column < resultMatrix.getDimension(); column++)
                        {
                            if(potenzmatrix.getValueAt(row, column) > 0)
                            {
                                resultMatrix.setValueAt(row, column, 1);
                                // setze updated auf true wenn ein Wert gesetzt wurde
                                updated = true;
                            }
                        }
                    }
                    // Wenn sich die matrix nicht mehr geändert hat breche ab
                    if(!updated)
                    {
                        break;
                    }
                    // Wenn es nur mehr 1er gibt breche ab
                    if(completed)
                    {
                        break;
                    }
                }
                
		
		return resultMatrix;
	}

	
	/**
	 * Berechnet die Distanzmatrix und liefert diese zurück.
	 * @return distanzmatrix
	 */
	public Matrix distanzMatrix() 
        {

		Matrix resultMatrix = new Matrix(getDimension());
                for(int row = 0; row < resultMatrix.getDimension(); row++)
                {
                    for(int column = 0; column < resultMatrix.getDimension(); column++)
                    {
                        if(row == column)
                        {
                            resultMatrix.setValueAt(row, column, 0);
                        }
                        if(row != column)
                        {
                            resultMatrix.setValueAt(row, column, Integer.MIN_VALUE);
                        }
                        
                    }
                }
                
                for(int power = 1; power < resultMatrix.getDimension(); power++)
                {
                    boolean updated = false;
                    boolean completed = true;
                    Matrix potenzmatrix = power(power);
                    // Wenn die Distanzmatrix kein unendlich mehr hat setze completed auf true
                    for(int row = 0; row < resultMatrix.getDimension(); row++)
                    {
                        for(int column = 0; column < resultMatrix.getDimension(); column++)
                        {
                            if(resultMatrix.getValueAt(row, column) == Integer.MIN_VALUE)
                            {
                                completed = false;
                            }
                        }
                    }
                    for(int row = 0; row < resultMatrix.getDimension(); row++)
                    {
                        for(int column = 0; column < resultMatrix.getDimension(); column++)
                        {
                            if(potenzmatrix.getValueAt(row, column) > 0 && resultMatrix.getValueAt(row, column) == Integer.MIN_VALUE)
                            {
                                resultMatrix.setValueAt(row, column, power);
                                // setze updated auf true wenn ein Wert gesetzt wurde
                                updated = true;
                            }
                        }
                    }
                    // Wenn sich die matrix nicht mehr geändert hat breche ab
                    if(!updated)
                    {
                        break;
                    }
                    // Wenn es nur mehr 1er gibt breche ab
                    if(completed)
                    {
                        break;
                    }
                }
                
		
		return resultMatrix;
	}

	// Berechnung der Exzentrizit�ten
	/**
	 * Berechnet die Exzentritäten und gibt diese als Array zurück.
	 * @return result
	 * 
	 */
	public int[] exzentrizitaeten() 
        {

		Matrix distanzMatrix = distanzMatrix();
		int[] exzentrizitaeten = new int[distanzMatrix.getDimension()];
		// Durchlaufe alle Zeilen und Spalten.
		for(int row = 0; row < distanzMatrix.getDimension(); row++)
                {
                    for(int column = 0; column < distanzMatrix.getDimension(); column++)
                    {
                        exzentrizitaeten[row] = Math.max(distanzMatrix.getValueAt(row, column), exzentrizitaeten[row]);
                    }
                }
                for(int i = 0; i < exzentrizitaeten.length; i++)
                {
                    System.out.println(exzentrizitaeten[i]);
                }
		return exzentrizitaeten;
	}

	/**
	 * Berechnet den Radius und gibt diesen zurück.
	 * Der Radius entspricht der kleinsten Exzentrität.
	 * @return radius
	 */
	public int radius() {
		// Hole die Exzentrizit�ten.
		int[] exzentrizitaeten = exzentrizitaeten();
		// Es wird angenommen, dass der 0. Wert der kleinste ist; das wird nur
		// gemacht, damit der Radius schon beim ersten Vergleich einen Werte aus
		// der Menge m�glicher Ergebnisse enth�lt.
		int radius = exzentrizitaeten[0];
                for(int i = 0; i < exzentrizitaeten.length; i++)
                {
                    radius = Math.min(exzentrizitaeten[i], radius);
                }
		
		return radius;
	}

	/**
	 * Berechnet den Durchmesser und gibt diesen zurück.
	 * Der Durchmesser entspricht der größten Exzentrität.
	 * @return durchmesser
	 */
	public int durchmesser() {
		// Berechnet sich wie der Radius, nur wird hier nach dem Maximum
		// gesucht.
		int[] exzentrizitaeten = exzentrizitaeten();
		int durchmesser = exzentrizitaeten[0];
                for(int i = 0; i < exzentrizitaeten.length; i++)
                {
                    durchmesser = Math.max(exzentrizitaeten[i], durchmesser);
                }

		
		return durchmesser;
	}

	/**
	 * Berechnet das Zentrum und gibt dieses zurück.
	 * Das Zentrum entspricht der Menge der Knoten die die Exzentrität des Radius haben.
	 */
	public ArrayList<Integer> zentrum() {
		// Berechne die Exzentrizit�ten und den Radius.
		int[] exzentrizitaeten = exzentrizitaeten();
		int radius = radius();
		// Erstelle eine Liste (dynamisch) f�r die Knoten.
		ArrayList<Integer> zentren = new ArrayList<Integer>();
                for(int i = 0; i < exzentrizitaeten.length; i++)
                {
                    if(exzentrizitaeten[i] == radius)
                    {
                        zentren.add(i + 1);
                    }
                }
		
		return zentren;
	}

	/**
         * Überprüft ob der Graph zusammenhängend ist
         * @return true wenn sich in der Webmatrix nur 1er befinden, false wenn es einen anderen Wert gibt
         */
	public boolean zusammenhaengend() 
        {
            Matrix weg = wegMatrix();
            for(int row = 0; row < weg.getDimension(); row++)
            {
                for(int column = 0; column < weg.getDimension(); column++)
                {
                    if(weg.getValueAt(row, column) != 1)
                    {
                        return false;
                    }
                }
            }
		
            return true;
	}

	/**
         * Berechnet auf Basis der Wegmatrix die Komponenten
         * @return komponenten - Liefert die Komponenten zurück
         */
	public ArrayList<ArrayList<Integer>> komponenten() 
        {
		return komponenten(wegMatrix());
	}

	/**
         * 
         * @param wegMatrix
         * @return komponenten - Liefert die Komponenten zurück
         */
	private ArrayList<ArrayList<Integer>> komponenten(Matrix wegMatrix) 
        {
		// Erstelle eine Liste, welche mehrere Komponenten aufnehmen kann.
		ArrayList<ArrayList<Integer>> komponenten = new ArrayList<ArrayList<Integer>>();
                for(int row = 0; row < wegMatrix.getDimension(); row++)
                {
                    ArrayList<Integer> neueKomponente = new ArrayList<Integer>();
                    for(int column = 0; column < wegMatrix.getDimension(); column++)
                    {
                        if(wegMatrix.getValueAt(row, column) == 1)
                        {
                            neueKomponente.add(column + 1);
                        }
                    }
                    if(!neueKomponente.isEmpty())
                    {
                        boolean ablegen = true;
                        Iterator<ArrayList<Integer>> it = komponenten.iterator();
                        while(it.hasNext())
                        {
                            ArrayList<Integer> komponente = it.next();
                            if (komponente.containsAll(neueKomponente)) 
                            {
				ablegen = false;
				break;
                            }
                            if (neueKomponente.containsAll(komponente)) 
                            {
				it.remove();
                            }
                            
                        }
                        if (ablegen) 
                        {
                            komponenten.add(neueKomponente);
			}
                    }
                }

		
		return komponenten;
	}

	/**
         * Berechnet die Artikulationen auf Basis der Komponenten
         * @return artikulationen - Liefert die Artikulationen als ArrayList zurück
         */
	public ArrayList<Integer> artikulationen() {
		int komponenten = komponenten().size();
		
		ArrayList<Integer> artikulationen = new ArrayList<Integer>();
                for(int aktknoten = 0; aktknoten < getDimension(); aktknoten++)
                {
                    Matrix work = new Matrix(this);
                    for(int i = 0; i < getDimension(); i++)
                    {
                        work.setValueAt(aktknoten, i, 0);
                        work.setValueAt(i, aktknoten, 0);
                        
                    }
                    ArrayList<ArrayList<Integer>> aktuelleKomponenten = komponenten(work.wegMatrix());
                    if (aktuelleKomponenten.size() - 1 > komponenten) 
                    {
                        artikulationen.add(aktknoten + 1);
                    }
                }
		
		return artikulationen;
	}

	/**
         * Berechnet die Brücken auf Basis der Komponenten
         * @return bruecken - Liefert die Brücken als ArrayList zurück.
         */
	public ArrayList<ArrayList<Integer>> bruecken() 
        {
		ArrayList<ArrayList<Integer>> bruecken = new ArrayList<ArrayList<Integer>>();
                Matrix work = new Matrix(this);
		int komponenten = work.komponenten().size();
                for(int row = 0; row < work.getDimension(); row++)
                {
                    for(int column = 0; column < work.getDimension(); column++)
                    {
                        if(work.getValueAt(row, column) == 1)
                        {
                            work.setValueAt(row, column, 0);
                            work.setValueAt(column, row, 0);
                        
                            if(komponenten(work.wegMatrix()).size() > komponenten)
                            {
                                ArrayList<Integer> bruecke = new ArrayList<Integer>();
                                bruecke.add(Math.min(column + 1, row + 1));
                                bruecke.add(Math.max(column + 1, row + 1));
                                if(!bruecken.contains(bruecke))
                                {
                                    bruecken.add(bruecke);
                                }
                            }
                            work.values[row][column] = 1;
                            work.values[column][row] = 1;
                        }
                    }
                }
		
		return bruecken;
	}
        
      
       
        
        /**
         * Berechnet die Kantenanzahl auf Basis der Adjazenzmatrix
         * @return anzahl - Die Kantenanzahl
         */
	public int getKantenAnzahl() {
		int anzahl = 0;

		for (int row = 0; row < getDimension(); row++) 
                {
			for (int column = 0; column < getDimension(); column++) 
                        {
				if (values[row][column] == 1)
                                {
                                    anzahl++;
                                }
					
			}
		}
		return anzahl / 2;
	}

	/**
         * Berechnet die Blöcke auf Basis der Komponenten, Artikulationen und Brücken.
         * @return bloecke - Gibt die Bloecke als ArrayList zurück.
         */
        public ArrayList<ArrayList<Integer>> bloecke()
        {
            
            ArrayList<ArrayList<Integer>> bloecke = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> artikulationen = artikulationen();
            ArrayList<ArrayList<Integer>> bruecken = bruecken();
            // jede Brücke ist ein Block also der Liste hinzufügen
            Iterator<ArrayList<Integer>> itb = bruecken.iterator();
            while(itb.hasNext())
            {
                bloecke.add(itb.next());
            }
            // wenn es keine artikulationen gibt und der graph ist der gesamte graph ein block
            // OK!
            if(artikulationen.isEmpty() && zusammenhaengend())
            {
                ArrayList<Integer> artikulationsfrei = new ArrayList<Integer>();
                for(int i = 0; i < getDimension(); i++)
                {
                    artikulationsfrei.add(i + 1);
                }
                bloecke.add(artikulationsfrei);
            }
            
            
            // ein isolierter knoten ist auch ein block
            // OK!
            int[] knotengrad = getKnotenGrad();
            
                
            for(int i = 0; i < knotengrad.length; i++)
            {
                   
                if(knotengrad[i] == 0)
                {
                    ArrayList<Integer> isolierterKnoten = new ArrayList<Integer>();
                    isolierterKnoten.add(i + 1);
                    bloecke.add(isolierterKnoten);
                }
                    
            }
            
            // ein block ist der größte zusammenhängende artikulationsfrei teilgraph
            Matrix work = new Matrix(this);
            // berechne die komponenten am anfang
            // nach entfernen der artikulationen bleiben nur mehr teile der blöcke über
            // die teile der blöcke und die dazugehörige artikulation sind ein block
            // 
            ArrayList<ArrayList<Integer>> kompstart = work.komponenten();
            Iterator<Integer> ita = artikulationen.iterator();
            
            
            // entferne alle artikulationen aus der matrix
            System.out.println(work.komponenten().size());
            while(ita.hasNext())
            {
                Integer art = ita.next();
                for(int i = 0; i < work.getDimension(); i++)
                {
                    work.setValueAt(art.intValue()-1, i, 0);
                    work.setValueAt(i, art.intValue()-1, 0);
                }
            }
            
            // berechne die neuen komponenten
            ArrayList<ArrayList<Integer>> kompend = work.komponenten();
            Iterator<ArrayList<Integer>> itke = kompend.iterator();
            Iterator<ArrayList<Integer>> itks = kompstart.iterator();
            // solange es neue komponenten von der endberechnung gibt
            Iterator<Integer> itaneu = artikulationen.iterator();
            
            // entferne alle komponenten die nur einen knoten haben
            // dies sind isolierte knoten
            // wenn es brücken sind wurden sie bereits hinzugefügt
            // wenn es isolierte knoten sind wurden sie bereits hinzugefügt
            while(itke.hasNext())
            {
                if(itke.next().size() == 1)
                {
                    itke.remove();
                }
            }
            
            // solange es komponenten im artikulationsfreien graphen gibt
            Iterator<ArrayList<Integer>> itkegtone = kompend.iterator();
            while(itkegtone.hasNext())
            {
                
                // weise dem block die komponente zu
                ArrayList<Integer> block = itkegtone.next();
                for(int i = 0; i <  block.size(); i++)
                {
                    Integer knoten = block.get(i);
                    // füge die artikulationen die mit einem knoten des blocks verbunden sind hinzu
                    // wenn knoten ist keine artikulation
                    if(!artikulationen.contains(knoten))
                    {
                        
                    
                        for(int j = 0; j < artikulationen.size(); j++)
                        {
                            Integer artikulation = artikulationen.get(j);
                            if(getValueAt(knoten-1, artikulation-1) == 1)
                            {
                                if(!block.contains(artikulation))
                                {
                                    block.add(artikulation);
                                }
                                if(!bloecke.contains(block))
                                {
                                    bloecke.add(block);
                                }
                            
                            }
                        }
                        if(!bloecke.contains(block))
                        {
                            bloecke.add(block);
                        }
                        
                    }
                    
                        
                }
            }
            
            
            
            
            
            
            //System.out.println(work.komponenten().size());
            
                
                
            
            
            return bloecke;
        }

	/**
         * Gibt alle Kanten zurück. Wird für die Graphdarstellung der GUI benötigt.
         * @return selektierteKanten - Gibt alle Kanten als ArrayList zurück
         */
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

	/**
         * Gibt eine Liste aller Knoten zurück. Wird für die GUI benötigt.
         * @return selektierteKnoten - Alle Knoten als ArrayList.
         */
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

	
}