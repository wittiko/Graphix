package at.htlv.messner.graphix.model;

import java.util.ArrayList;
/**
 * 
 * @author Gregor Messner
 * 
 */
public interface MatrixInt 
{
	public int getDimension();
	public int getValueAt(int row, int column);
	public void setValueAt(int row, int column, int value);
	public Matrix multiply(Matrix otherMatrix);
	public Matrix power(int n);
	public Matrix wegMatrix();
	public Matrix distanzMatrix();
	public int[] exzentrizitaeten();
	public int radius();
	public int durchmesser();
	public ArrayList<Integer> zentrum();
	public boolean zusammenhaengend();
	public ArrayList<ArrayList<Integer>> komponenten();
	public ArrayList<Integer> artikulationen();
	public ArrayList<ArrayList<Integer>> bruecken();
	public int getKantenAnzahl();
	public ArrayList<ArrayList<Integer>> bloecke();
	public ArrayList<ArrayList<Integer>> selektierteKanten();
	public ArrayList<Integer> selektierteKnoten();
	
	

}
