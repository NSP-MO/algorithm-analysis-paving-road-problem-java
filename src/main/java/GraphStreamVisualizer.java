import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

class GraphStreamVisualizer {
  private static final String STYLESHEET = "node {" +
      "  size: 30px;" +
      "  fill-color: #4A90E2;" +
      "  text-size: 20px;" +
      "  text-style: bold;" +
      "  text-color: white;" +
      "  text-alignment: center;" +
      "  stroke-mode: plain;" +
      "  stroke-color: #2C3E50;" +
      "  stroke-width: 2px;" +
      "}" +
      "node.paved {" +
      "  fill-color: #2ECC71;" +
      "  size: 35px;" +
      "}" +
      "edge {" +
      "  fill-color: #95A5A6;" +
      "  size: 2px;" +
      "  text-size: 14px;" +
      "  text-background-mode: rounded-box;" +
      "  text-background-color: #ECF0F1;" +
      "  text-color: #2C3E50;" +
      "  text-padding: 5px;" +
      "}" +
      "edge.paved {" +
      "  fill-color: #27AE60;" +
      "  size: 4px;" +
      "  text-background-color: #D5F4E6;" +
      "}" +
      "edge.notpaved {" +
      "  fill-color: #E74C3C;" +
      "  size: 2px;" +
      "}";

  public static void showInitialGraph(Graph graph) {
    System.setProperty("org.graphstream.ui", "swing");

    org.graphstream.graph.Graph gsGraph = new SingleGraph("Village Roads - Initial Graph");
    gsGraph.setAttribute("ui.stylesheet", STYLESHEET);
    gsGraph.setAttribute("ui.quality");
    gsGraph.setAttribute("ui.antialias");

    // Add all edges from the graph
    for (VillageEdge edge : graph.villages) {
      // Add nodes if they don't exist
      if (gsGraph.getNode(edge.from.toString()) == null) {
        Node node = gsGraph.addNode(edge.from.toString());
        node.setAttribute("ui.label", edge.from.toString());
      }
      if (gsGraph.getNode(edge.to.toString()) == null) {
        Node node = gsGraph.addNode(edge.to.toString());
        node.setAttribute("ui.label", edge.to.toString());
      }

      // Add edge
      String edgeId = edge.from.toString() + "-" + edge.to.toString();
      Edge e = gsGraph.addEdge(edgeId, edge.from.toString(), edge.to.toString(), false);
      String label = String.format("d:%d v:%d c:%d", edge.distance, edge.value, edge.getPriceToPave());
      e.setAttribute("ui.label", label);
    }

    // Display the graph
    Viewer viewer = gsGraph.display();
    viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
  }

  public static void showSolutionComparison(Graph graph, List<VillageEdge> pavedRoads) {
    System.setProperty("org.graphstream.ui", "swing");

    // Create a frame to hold both graphs side by side
    JFrame frame = new JFrame("Village Paving Solution");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(1, 2, 10, 0));
    frame.setSize(1600, 800);

    // Create initial graph
    org.graphstream.graph.Graph initialGraph = new SingleGraph("Initial Graph");
    initialGraph.setAttribute("ui.stylesheet", STYLESHEET);
    initialGraph.setAttribute("ui.quality");
    initialGraph.setAttribute("ui.antialias");

    for (VillageEdge edge : graph.villages) {
      if (initialGraph.getNode(edge.from.toString()) == null) {
        Node node = initialGraph.addNode(edge.from.toString());
        node.setAttribute("ui.label", edge.from.toString());
      }
      if (initialGraph.getNode(edge.to.toString()) == null) {
        Node node = initialGraph.addNode(edge.to.toString());
        node.setAttribute("ui.label", edge.to.toString());
      }

      String edgeId = edge.from.toString() + "-" + edge.to.toString();
      Edge e = initialGraph.addEdge(edgeId, edge.from.toString(), edge.to.toString(), false);
      String label = String.format("d:%d v:%d", edge.distance, edge.value);
      e.setAttribute("ui.label", label);

      // Mark if paved or not
      boolean isPaved = pavedRoads.stream()
          .anyMatch(pe -> (pe.from.equals(edge.from) && pe.to.equals(edge.to)) ||
              (pe.from.equals(edge.to) && pe.to.equals(edge.from)));
      if (isPaved) {
        e.setAttribute("ui.class", "paved");
      } else {
        e.setAttribute("ui.class", "notpaved");
      }
    }

    // Mark paved nodes
    for (VillageEdge edge : pavedRoads) {
      Node n1 = initialGraph.getNode(edge.from.toString());
      Node n2 = initialGraph.getNode(edge.to.toString());
      if (n1 != null)
        n1.setAttribute("ui.class", "paved");
      if (n2 != null)
        n2.setAttribute("ui.class", "paved");
    }

    // Create solution graph (only paved roads)
    org.graphstream.graph.Graph solutionGraph = new SingleGraph("Solution - Paved Roads");
    solutionGraph.setAttribute("ui.stylesheet", STYLESHEET);
    solutionGraph.setAttribute("ui.quality");
    solutionGraph.setAttribute("ui.antialias");

    for (VillageEdge edge : pavedRoads) {
      if (solutionGraph.getNode(edge.from.toString()) == null) {
        Node node = solutionGraph.addNode(edge.from.toString());
        node.setAttribute("ui.label", edge.from.toString());
        node.setAttribute("ui.class", "paved");
      }
      if (solutionGraph.getNode(edge.to.toString()) == null) {
        Node node = solutionGraph.addNode(edge.to.toString());
        node.setAttribute("ui.label", edge.to.toString());
        node.setAttribute("ui.class", "paved");
      }

      String edgeId = edge.from.toString() + "-" + edge.to.toString();
      Edge e = solutionGraph.addEdge(edgeId, edge.from.toString(), edge.to.toString(), false);
      String label = String.format("d:%d v:%d", edge.distance, edge.value);
      e.setAttribute("ui.label", label);
      e.setAttribute("ui.class", "paved");
    }

    // Create viewers
    Viewer viewer1 = initialGraph.display(false);
    viewer1.enableAutoLayout();
    ViewPanel view1 = (ViewPanel) viewer1.getDefaultView();

    Viewer viewer2 = solutionGraph.display(false);
    viewer2.enableAutoLayout();
    ViewPanel view2 = (ViewPanel) viewer2.getDefaultView();

    // Create panels with titles
    JPanel panel1 = new JPanel(new BorderLayout());
    JLabel label1 = new JLabel("Initial Graph - All Roads", SwingConstants.CENTER);
    label1.setFont(new Font("Arial", Font.BOLD, 16));
    label1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panel1.add(label1, BorderLayout.NORTH);
    panel1.add(view1, BorderLayout.CENTER);

    JPanel panel2 = new JPanel(new BorderLayout());
    JLabel label2 = new JLabel("Solution - Paved Roads Only", SwingConstants.CENTER);
    label2.setFont(new Font("Arial", Font.BOLD, 16));
    label2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panel2.add(label2, BorderLayout.NORTH);
    panel2.add(view2, BorderLayout.CENTER);

    frame.add(panel1);
    frame.add(panel2);
    frame.setVisible(true);

    // Disable auto layour after 3 seconds
    new Thread(() -> {
      try {
        Thread.sleep(4000);
        viewer1.disableAutoLayout();
        viewer2.disableAutoLayout();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }
}