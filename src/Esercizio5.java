import java.util.*;

public class Esercizio5 {

    public static String FILENAME;
    public static double[][] distances;
    public static int[][] predecessors;
    public static double[][] weights;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java BellmanFordAbilene <input_file>");
            System.exit(1);
        }
        FILENAME = args[0];

        // Carica i nodi e i link dalla rete Abilene
        ArrayList<NodeAbilene> nodes = Esercizio4.extractNodes(FILENAME);
        ArrayList<Link> links = Esercizio4.extractLinks(FILENAME);

        System.out.println("#####################################################");
        for (NodeAbilene node: nodes) {
            System.out.println(node.getUniqueId() + ":<" + node.getId() + ", (" + node.getLongitude() + ", " + node.getLatitude() + ")" + ">");
        }
//        for (Link link: links) {
//            System.out.println("<" + link.getId() + ", (" + link.getSource() + ", " + link.getDestination() + ", " +link.getPreInstalledCapacity() + ")" + ">");
//        }
        Esercizio4.calculateLinkWeights(links, Esercizio4.findMaxPreInstalledCapacity(links));

        Esercizio4.indexNodesInLinks(nodes, links);

        weights = new double[nodes.size()][nodes.size()];
        for (Link link: links) {
            System.out.println("<" + link.getId() + ", (Source: " + link.getSource() + ", Destination: " + link.getDestination() + ", " +link.getWeight() + ")" + ">");
            weights[Integer.parseInt(link.getSource())][Integer.parseInt(link.getDestination())] = link.getWeight();
        }
        System.out.println("#####################################################");

        //dijkstra(nodes, links);
        printMatrixWithIndicesAndNull(distances);
        printMatrixWithIndicesAndNull(predecessors, false);

//        for (int i = 0; i < nodes.size(); i++) {
//            for (int j = 0; j < nodes.size(); j++) {
//                System.out.println("d[" + i + "][" + j + "] = " + distances[i][j]);
//            }
//        }
    }

        public static void dijkstra() {
        
        }
/*
        // Stampare l'output formattato
        System.out.println();
        System.out.println("s d dist path");
        System.out.println("---- ---- ------------ -------------------");

        for (int i = 0; i < numNodes; i++) {
            System.out.print(sourceNode + " " + i + " ");
            System.out.printf("%.4f", distance[i]);
            System.out.print(" ");

            if (distance[i] == 0) {
                System.out.print(i);
            } else {
                Esercizio4.printPath(sourceNode, i, predecessor);
            }

            System.out.println();
        }*/

    public static void printMatrixWithIndicesAndNull(double[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        System.out.print("      "); // Spazio per l'etichetta della colonna
        for (int j = 0; j < numCols; j++) {
            System.out.printf("%5d\t", j); // Etichette delle colonne
        }
        System.out.println(); // Vai a capo dopo le etichette delle colonne

        for (int i = 0; i < numRows; i++) {
            System.out.printf("%5d| ", i); // Etichetta della riga
            for (int j = 0; j < numCols; j++) {
                double value = matrix[i][j];
                if (value == 0.0) {
                    System.out.printf("null\t"); // Mostra "null" se il valore è 0
                } else if (value == Float.MAX_VALUE){
                    System.out.printf("inf\t");
                } else {
                    System.out.printf("%.4f\t", value);
                }
            }
            System.out.println(); // Vai a capo alla fine di ogni riga
        }
        System.out.println();
    }

    public static void printMatrixWithIndicesAndNull(int[][] matrix, boolean nullString) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        System.out.print("      "); // Spazio per l'etichetta della colonna
        for (int j = 0; j < numCols; j++) {
            System.out.printf("%5d\t", j); // Etichette delle colonne
        }
        System.out.println(); // Vai a capo dopo le etichette delle colonne

        for (int i = 0; i < numRows; i++) {
            System.out.printf("%5d| ", i); // Etichetta della riga
            for (int j = 0; j < numCols; j++) {
                int value = matrix[i][j]; // Modificato il tipo di dato da double a int
                if (nullString) {
                    if (value == 0) {
                        System.out.printf("null\t"); // Mostra "null" se il valore è 0
                    } else {
                        System.out.printf("%5d\t", value); // Modificato il formato a %5d per numeri interi
                    }
                } else {
                    System.out.printf("%5d\t", value); // Modificato il formato a %5d per numeri interi
                }

            }
            System.out.println(); // Vai a capo alla fine di ogni riga
        }
        System.out.println();
    }
}