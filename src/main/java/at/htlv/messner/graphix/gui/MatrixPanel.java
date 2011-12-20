package at.htlv.messner.graphix.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import at.htlv.messner.graphix.model.Matrix;

public class MatrixPanel extends JPanel {
	private static final long serialVersionUID = -6858830340288094707L;

	private JButton[][] buttons = new JButton[15][15];
	private int[][] value = new int[15][15];

	private Color selectedColor = Color.WHITE;
	private Color unselectedColor = Color.WHITE;
	private Color notSelectableColor = Color.DARK_GRAY;
	private Color disabledColor = new Color(240, 240, 240);
	private boolean enableDiagonal;
	private TitledBorder border;

	private Matrix currentMatrix;

	public MatrixPanel(String title, Matrix matrix, boolean editable,
			boolean enableDiagonal) {
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException(
					"expected: title.length > 0, actual: " + title);
		}

		if (matrix == null) {
			throw new IllegalArgumentException(
					"expected: matrix != null, actual: " + matrix);
		}
		border = new TitledBorder(title);
		setBorder(border);
		setLayout(new GridLayout(16, 16));

		this.enableDiagonal = enableDiagonal;

		add(new JLabel());
		for (int column = 0; column < buttons[0].length; column++) {
			add(new JLabel(String.format("%02d", column + 1)));
		}

		for (int row = 0; row < buttons.length; row++) {
			add(new JLabel(String.format("%02d", row + 1)));
			for (int column = 0; column < buttons[row].length; column++) {
				add(buttons[row][column] = new JButton());
				buttons[row][column].setBorder(new EtchedBorder());

				final int r = row;
				final int c = column;

				if (editable) {
					buttons[row][column]
							.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									value[r][c] = (value[r][c] + 1) % 2;
									value[c][r] = (value[c][r] + 1) % 2;
									buttons[r][c]
											.setBackground(value[r][c] == 0 ? unselectedColor
													: selectedColor);
									buttons[r][c].setText("<html>"
											+ value[r][c] + "</html>");
									buttons[c][r]
											.setBackground(value[r][c] == 0 ? unselectedColor
													: selectedColor);
									buttons[c][r].setText("<html>"
											+ value[r][c] + "</html>");
								}
							});
				}

			}
		}

		currentMatrix = matrix;
		update(true);
	}

	public void updateSize(int dimension) {
		updateWith(new Matrix(dimension), false);
	}

	private void update(boolean updateSelection) {
		if (updateSelection) {
			value = new int[15][15];
		}

		for (int row = 0; row < buttons.length; row++) {
			for (int column = 0; column < buttons[row].length; column++) {

				if (row == column && !enableDiagonal) {
					setEnabled(false);
				}

				if (row < currentMatrix.getDimension()
						&& column < currentMatrix.getDimension()) {
					if (updateSelection) {
						value[row][column] = currentMatrix.getValueAt(row,
								column);
					}

					if (row == column && !enableDiagonal) {
						buttons[row][column].setBackground(notSelectableColor);
						buttons[row][column]
								.setText("<html><font color = white>0</font/></html>");
					} else {
						buttons[row][column].setEnabled(true);
						buttons[row][column]
								.setBackground(value[row][column] == 0 ? unselectedColor
										: selectedColor);
						if (value[row][column] == Integer.MIN_VALUE) {
							buttons[row][column]
									.setText("<html>&infin;</html>");
							buttons[row][column].setBackground(unselectedColor);
						} else if (value[row][column] > 99) {
							buttons[row][column].setText("<html>##</html>");
							buttons[row][column].setBackground(selectedColor);

						} else {
							buttons[row][column].setText("<html>"
									+ value[row][column] + "</html>");
						}
					}
				} else {
					buttons[row][column].setEnabled(false);
					buttons[row][column].setBackground(disabledColor);
					buttons[row][column].setText("<html></html>");
				}

				boolean enable = row != column
						&& column < currentMatrix.getDimension()
						&& row < currentMatrix.getDimension();

				buttons[row][column].setEnabled(enable);
			}
		}
	}

	public void updateWith(Matrix matrix, boolean updateSelection) {
		if (matrix == null) {
			throw new IllegalArgumentException(
					"expected: matrix != null, actual: " + matrix);
		}

		currentMatrix = matrix;
		update(updateSelection);

	}

	@Override
	public Dimension getSize() {
		return new Dimension(305, 305);
	}

	@Override
	public Dimension getMaximumSize() {
		return getSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return getSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}

	public Matrix getCurrentMatrix() {
		int dimension = 0;
		for (int i = 0; i < buttons.length
				&& buttons[i][i].getBackground() == notSelectableColor; dimension = ++i) {
		}

		Matrix currentMatrix = new Matrix(dimension);
		for (int row = 0; row < dimension; row++) {
			for (int column = 0; column < dimension; column++) {
				currentMatrix.setValueAt(row, column, value[row][column]);
			}
		}

		return currentMatrix;
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
		update(false);
	}

	public Color getUnselectedColor() {
		return unselectedColor;
	}

	public void setUnselectedColor(Color unselectedColor) {
		this.unselectedColor = unselectedColor;
		update(false);
	}

	public Color getNotSelectableColor() {
		return notSelectableColor;
	}

	public void setNotSelectableColor(Color notSelectableColor) {
		this.notSelectableColor = notSelectableColor;
		update(false);
	}

	public Color getDisabledColor() {
		return disabledColor;
	}

	public void setDisabledColor(Color disabledColor) {
		this.disabledColor = disabledColor;
		update(false);
	}

	public void setTitle(String title) {
		border.setTitle(title);
		repaint();
	}
}
