import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DFS {

    public static void main(String[] args) {
        int numVertices = 12;
        Graph graph = new Graph(numVertices);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 4, 1);
        graph.addEdge(1, 5, 4);
        graph.addEdge(1, 11, 1);
        graph.addEdge(2, 5, 1);
        graph.addEdge(2, 8, 1);
        graph.addEdge(3, 6, 1);
        graph.addEdge(3, 9, 1);
        graph.addEdge(3, 10, 1);
        graph.addEdge(4, 6, 1);
        graph.addEdge(4, 7, 1);
        graph.addEdge(5, 6, 1);
        graph.addEdge(7, 9, 1);
        graph.addEdge(8, 11, 1);
        graph.addEdge(9, 10, 1);

        for (int source = 0; source < numVertices; source++) {
            System.out.println("\ns d dist path");
            System.out.println("---- ---- ------------ -------------------");
            for (int destination = 0; destination < numVertices; destination++) {
                if (source != destination) {
                    List<Integer> path = graph.calculateShortestPath(source, destination);
                    if (path.size() > 0) {
                        double distance = calculatePathDistance(graph, path);
                        printPath(source, destination, distance, path);
                    }
                }
            }
        }
    }

    public static double calculatePathDistance(Graph graph, List<Integer> path) {
        double distance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            int u = path.get(i);
            int v = path.get(i + 1);
            distance += graph.adjacencyMatrix[u][v];
        }
        return distance;
    }

    public static void printPath(int source, int destination, double distance, List<Integer> path) {
        System.out.print(source + " " + destination + " " + String.format("%.4f", distance) + " ");

        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print("->");
            }
        }
        System.out.println();
    }
}

class Graph {
    private int numVertices;
    public int[][] adjacencyMatrix;
    private double[][] memo;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        adjacencyMatrix = new int[numVertices][numVertices];
        memo = new double[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            Arrays.fill(memo[i], -1.0);
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjacencyMatrix[source][destination] = weight;
        adjacencyMatrix[destination][source] = weight; // Considera il grafo non orientato
    }

    public double dijkstraWithMemoization(int source, int destination) {
        if (source == destination) {
            return 0.0;
        }

        if (memo[source][destination] != -1.0) {
            return memo[source][destination];
        }

        double[] distance = new double[numVertices];
        boolean[] visited = new boolean[numVertices];

        Arrays.fill(distance, Double.MAX_VALUE);
        distance[source] = 0;

        for (int i = 0; i < numVertices - 1; i++) {
            int u = getMinDistanceVertex(distance, visited);
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && adjacencyMatrix[u][v] != 0) {
                    double newDistance = distance[u] + adjacencyMatrix[u][v];
                    if (newDistance < distance[v]) {
                        distance[v] = newDistance;
                    }
                }
            }
        }

        memo[source][destination] = distance[destination];
        memo[destination][source] = distance[destination];

        return memo[source][destination];
    }

    public List<Integer> calculateShortestPath(int source, int destination) {
        List<Integer> path = new ArrayList<>();
        path.add(destination);

        int currentVertex = destination;
        double[] distance = new double[numVertices];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[source] = 0;

        while (currentVertex != source) {
            int prevVertex = -1;
            double shortestDistance = Double.MAX_VALUE;

            for (int neighbor = 0; neighbor < numVertices; neighbor++) {
                if (adjacencyMatrix[currentVertex][neighbor] != 0) {
                    double tentativeDistance = distance[neighbor] + adjacencyMatrix[currentVertex][neighbor];
                    if (tentativeDistance < shortestDistance) {
                        shortestDistance = tentativeDistance;
                        prevVertex = neighbor;
                    }
                }
            }

            if (prevVertex == -1) {
                break;
            }

            path.add(prevVertex);
            currentVertex = prevVertex;
        }

        Collections.reverse(path);
        return path;
    }

    private int getMinDistanceVertex(double[] distance, boolean[] visited) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < numVertices; v++) {
            if (!visited[v] && distance[v] < minDistance) {
                minDistance = distance[v];
                minIndex = v;
            }
        }

        return minIndex;
    }
}