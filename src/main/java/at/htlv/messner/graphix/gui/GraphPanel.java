package at.htlv.messner.graphix.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.collections15.Transformer;

import at.htlv.messner.graphix.model.Matrix;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class GraphPanel extends JPanel {

	private static final long serialVersionUID = -2707712944901661771L;

	static Graph<Integer, String> graph;
	static VisualizationViewer<Integer, String> vv;
	static CircleLayout<Integer, String> layout;

	public GraphPanel(String title) {
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException(
					"expected: title.length > 0, actual: " + title);
		}
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(title));

	}

	public void updateWith(Matrix matrix) {
		removeAll();

		graph = new UndirectedSparseMultigraph<Integer, String>();

		for (int i = 1; i <= matrix.getDimension(); i++) {
			graph.addVertex(i);
		}

		int i = 0;
		for (ArrayList<Integer> selektierteKanten : matrix.selektierteKanten()) {
			graph.addEdge("Edge" + i, selektierteKanten);
			i++;
		}

		layout = new CircleLayout<Integer, String>(graph);
		layout.setSize(new Dimension(300, 260));
		vv = new VisualizationViewer<Integer, String>(layout);

		Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
			public Paint transform(Integer i) {
				return Color.yellow;
			}
		};

		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<Integer>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		vv.getRenderContext().setEdgeShapeTransformer(	new
				EdgeShape.Line<Integer, String>());

		add(vv, BorderLayout.CENTER);
	}
}