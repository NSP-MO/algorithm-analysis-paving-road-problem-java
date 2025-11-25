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
  private Graph graph;

  public VillagePavingOptimizer(Graph graph, int budget) {
    this.graph = graph;
    this.budget = budget;
  }

  public void optimize() {
    List<VillageEdge> pavedRoads = new ArrayList<VillageEdge>();
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
    System.out.println("Remaining Budget: " + budget);
    System.out.println("Total Value Gained: " +
        pavedRoads.stream().mapToInt(e -> e.value).sum());
  }
}

public class VillagePaving {
  public static final int RP_PER_METER = 5000;

  public static void main(String[] args) {
    int V = 9;
    int E = 15;

    int budget = 40000;
    Graph area = new Graph(V, E);

    area.villages[0] = new VillageEdge('H', 'I', 1, 0);
    area.villages[1] = new VillageEdge('D', 'E', 20, 12);
    area.villages[2] = new VillageEdge('A', 'B', 2, 23);
    area.villages[3] = new VillageEdge('E', 'I', 3, 2);
    area.villages[4] = new VillageEdge('E', 'F', 3, 5);
    area.villages[5] = new VillageEdge('A', 'C', 4, 7);
    area.villages[6] = new VillageEdge('B', 'C', 4, 3);
    area.villages[7] = new VillageEdge('B', 'F', 4, 5);
    area.villages[8] = new VillageEdge('C', 'D', 5, 6);
    area.villages[9] = new VillageEdge('F', 'H', 5, 12);
    area.villages[10] = new VillageEdge('B', 'E', 6, 23);
    area.villages[11] = new VillageEdge('E', 'H', 6, 2);
    area.villages[12] = new VillageEdge('A', 'G', 7, 1);
    area.villages[13] = new VillageEdge('F', 'G', 8, 2);
    area.villages[14] = new VillageEdge('G', 'H', 9, 5);

    VillagePavingOptimizer optimizer = new VillagePavingOptimizer(area, budget);

    optimizer.optimize();
  }
}