import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class VillageEdge implements Comparable<VillageEdge> {
  Character from;
  Character to;
  int distance;
  int value;

  public VillageEdge(Character v1, Character v2, int weight, int value) {
    this.from = v1;
    this.to = v2;
    this.distance = weight;
    this.value = value;
  }

  public float getValuePerCost() {
    return (float) value / getPriceToPave();
  }

  public int getPriceToPave() {
    return (int) distance * VillagePaving.RP_PER_METER;
  }

  @Override
  public int compareTo(VillageEdge other) {
    return Integer.compare(this.distance, other.distance);
  }

  @Override
  public String toString() {
    return "Edge < " +
        "from = " + from +
        ", to = " + to +
        ", distance = " + distance +
        ", value = " + value +
        ", cost = " + this.getPriceToPave() +
        ", value/cost = " + this.getValuePerCost() +
        " >";

  }
}

class Graph {
  int villagesCount;
  int roadsCount;
  VillageEdge[] villages;

  public Graph(int v, int e) {
    this.villagesCount = v;
    this.roadsCount = e;
    this.villages = new VillageEdge[e];
  }

  public void sortEdgesByValueCost() {
    Arrays.sort(this.villages, (a, b) -> {
      float r1 = a.getValuePerCost();
      float r2 = b.getValuePerCost();
      if (r1 < r2)
        return 1;
      else if (r1 > r2)
        return -1;
      else
        return 0;
    });
  }
}

class VillagePavingOptimizer {
  private int budget;
  private int initialBudget;
  private Graph graph;
  private List<VillageEdge> pavedRoads;

  public VillagePavingOptimizer(Graph graph, int budget) {
    this.graph = graph;
    this.budget = budget;
    this.initialBudget = budget;
    this.pavedRoads = new ArrayList<>();
  }

  public void optimize() {
    System.out.println("----- Optimization Result -----");
    System.out.println("Initial Budget: Rp " + String.format("%,d", this.budget));

    pavedRoads.clear();
    graph.sortEdgesByValueCost();

    for (VillageEdge edge : graph.villages) {
      int cost = edge.getPriceToPave();
      if (cost <= budget) {
        budget -= cost;
        pavedRoads.add(edge);
      }
    }

    System.out.println("Paved Roads:");
    for (VillageEdge paved : pavedRoads) {
      System.out.println(paved);
    }

    System.out.println("-----------------------");
    System.out.println("Remaining Budget: Rp " + String.format("%,d", budget));
    System.out.println("Total Value Gained: " +
        pavedRoads.stream().mapToInt(e -> e.value).sum());
  }

  public List<VillageEdge> getPavedRoads() {
    return pavedRoads;
  }

  public int getRemainingBudget() {
    return budget;
  }

  public int getInitialBudget() {
    return initialBudget;
  }
}

public class VillagePaving {
  public static final int RP_PER_METER = 2000;

  public static void main(String[] args) {
    int V = 18;
    int E = 23;

    int budget = 40000;
    Graph area = new Graph(V, E);

    area.villages[0] = new VillageEdge('P', 'A', 5, 80);
    area.villages[1] = new VillageEdge('P', 'B', 22, 65);
    area.villages[2] = new VillageEdge('P', 'C', 13, 60);
    area.villages[3] = new VillageEdge('P', 'D', 4, 55);
    area.villages[4] = new VillageEdge('P', 'E', 15, 57);
    area.villages[5] = new VillageEdge('P', 'I', 16, 75);
    area.villages[6] = new VillageEdge('P', 'J', 1, 10);
    area.villages[7] = new VillageEdge('P', 'H', 4, 100);
    area.villages[8] = new VillageEdge('I', 'H', 20, 100);
    area.villages[9] = new VillageEdge('J', 'I', 30, 70);
    area.villages[10] = new VillageEdge('J', 'H', 19, 95);
    area.villages[11] = new VillageEdge('J', 'U', 5, 71);
    area.villages[12] = new VillageEdge('H', 'K', 3, 100);
    area.villages[13] = new VillageEdge('H', 'U', 20, 89);
    area.villages[14] = new VillageEdge('E', 'K', 16, 60);
    area.villages[15] = new VillageEdge('E', 'F', 21, 60);
    area.villages[16] = new VillageEdge('D', 'F', 7, 65);
    area.villages[17] = new VillageEdge('F', 'G', 8, 42);
    area.villages[18] = new VillageEdge('U', 'L', 10, 56);
    area.villages[19] = new VillageEdge('U', 'M', 17, 90);
    area.villages[20] = new VillageEdge('M', 'N', 13, 37);
    area.villages[21] = new VillageEdge('M', 'O', 11, 24);
    area.villages[22] = new VillageEdge('M', 'Q', 10, 43);

    VillagePavingOptimizer optimizer = new VillagePavingOptimizer(area, budget);

    // Run optimization
    optimizer.optimize();

    // Show visualization with GraphStream
    System.out.println("\n=== Opening Graph Visualization ===");
    System.out.println("Close the visualization window to exit the program.");

    GraphStreamVisualizer.showSolutionComparison(area, optimizer.getPavedRoads());
  }
}