import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Esercizio4 {

    public static String FILENAME;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java BellmanFordAbilene <input_file>");
            System.exit(1);
        }
        FILENAME = args[0];

        // Carica i nodi e i link dalla rete Abilene
        ArrayList<NodeAbilene> nodes = extractNodes(FILENAME);
        ArrayList<Link> links = extractLinks(FILENAME);

        System.out.println("#####################################################");
        for (NodeAbilene node: nodes) {
            System.out.println(node.getUniqueId() + ":<" + node.getId() + ", (" + node.getLongitude() + ", " + node.getLatitude() + ")" + ">");
        }
        for (Link link: links) {
            System.out.println("<" + link.getId() + ", (" + link.getSource() + ", " + link.getDestination() + ", " +link.getPreInstalledCapacity() + ")" + ">");
        }
        calculateLinkWeights(links, findMaxPreInstalledCapacity(links));

        indexNodesInLinks(nodes, links);

        for (Link link: links) {
            System.out.println("<" + link.getId() + ", (Source: " + link.getSource() + ", Destination: " + link.getDestination() + ", " +link.getWeight() + ")" + ">");
        }
        System.out.println("#####################################################");
        for (int i = 0; i < nodes.size(); i++) {
            calculateBellmanFord(nodes.size(), nodes.get(i).getUniqueId(), links);
        }


    }

    public static ArrayList<NodeAbilene> extractNodes(String fileName) {
        ArrayList<NodeAbilene> nodes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isInNodeSection = false;
            int iteration = 0;

            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("NODES (")) {
                    isInNodeSection = true;
                    continue; // Salta la riga di intestazione
                }

                if (isInNodeSection && line.trim().equals(")")) {
                    isInNodeSection = false;
                    break; // Uscire dalla sezione dei nodi
                }

                if (isInNodeSection) {
                    // Estrai il nome del nodo dalla riga (prima parte prima della parentesi aperta)
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length > 0) {
                        nodes.add(new NodeAbilene(iteration, parts[0],parts[2], parts[3]));
                        iteration++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodes;
    }
    public static ArrayList<Link> extractLinks(String fileName) {
        ArrayList<Link> links = new ArrayList<>();
        Pattern linkPattern = Pattern.compile("(\\w+) \\( (\\w+) (\\w+) \\) (\\d+\\.\\d+) (\\d+\\.\\d+) (\\d+\\.\\d+) (\\d+\\.\\d+) \\( (.+?) \\)");

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isInLinkSection = false;

            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("LINKS (")) {
                    isInLinkSection = true;
                    continue; // Salta la riga di intestazione
                }

                if (isInLinkSection && line.trim().equals(")")) {
                    isInLinkSection = false;
                    break; // Uscire dalla sezione dei links
                }

                if (isInLinkSection) {
                    Matcher matcher = linkPattern.matcher(line.trim());
                    if (matcher.find()) {
                        String linkId = matcher.group(1);
                        String source = matcher.group(2);
                        String target = matcher.group(3);
                        double preInstalledCapacity = Double.parseDouble(matcher.group(4));

                        Link link = new Link(linkId, source, target, preInstalledCapacity);
                        links.add(link);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return links;
    }

    public static double findMaxPreInstalledCapacity(List<Link> links) {
        double maxCapacity = Double.MIN_VALUE;

        for (Link link : links) {
            if (link.getPreInstalledCapacity() > maxCapacity) {
                maxCapacity = link.getPreInstalledCapacity();
            }
        }

        return maxCapacity;
    }

    public static void calculateLinkWeights(List<Link> links, double maxPreInstalledCapacity) {
        for (Link link : links) {
            double weight = maxPreInstalledCapacity / link.getPreInstalledCapacity();
            link.setWeight(weight);
        }
    }

    public static void indexNodesInLinks(List<NodeAbilene> nodes, List<Link> links) {
        Map<String, Integer> nodeIndexMap = new HashMap<>();

        // Crea una mappa che associa il nome del nodo al suo indice nell'ArrayList
        for (int i = 0; i < nodes.size(); i++) {
            NodeAbilene node = nodes.get(i);
            nodeIndexMap.put(node.getId(), node.getUniqueId());
        }

        // Modifica i nodi di partenza e destinazione dei link con indici unici basati sulla posizione nell'ArrayList di nodi
        for (Link link : links) {
            String sourceNodeId = link.getSource();
            String targetNodeId = link.getDestination();

            if (nodeIndexMap.containsKey(sourceNodeId) && nodeIndexMap.containsKey(targetNodeId)) {
                int sourceIndex = nodeIndexMap.get(sourceNodeId);
                int targetIndex = nodeIndexMap.get(targetNodeId);

                link.setSource(Integer.toString(sourceIndex));
                link.setDestination(Integer.toString(targetIndex));
            }
        }
    }

    public static void calculateBellmanFord(int numNodes, int sourceNode, List<Link> edges) {
        double[] distance = new double[numNodes];
        int[] predecessor = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            distance[i] = Double.POSITIVE_INFINITY;
            predecessor[i] = -1;
        }

        distance[sourceNode] = 0;

        for (int i = 0; i < numNodes - 1; i++) {
            for (Link edge : edges) {
                int u = Integer.parseInt(edge.getSource());
                int v = Integer.parseInt(edge.getDestination());
                double weight = edge.getWeight();

                if (distance[u] + weight < distance[v]) {
                    distance[v] = distance[u] + weight;
                    predecessor[v] = u;
                } else if (distance[v] + weight < distance[u]) {
                    distance[u] = distance[v] + weight;
                    predecessor[u] = v;
                }
            }
        }

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
                printPath(sourceNode, i, predecessor);
            }

            System.out.println();
        }
    }

    public static void printPath(int source, int current, int[] predecessor) {
        if (current == source) {
            System.out.print(source);
        } else if (predecessor[current] == -1) {
            System.out.print("No path");
        } else {
            printPath(source, predecessor[current], predecessor);
            System.out.print("->" + current);
        }
    }

}

class NodeAbilene {
    private int uniqueId;
    private String id;
    private String longitude;
    private String latitude;

    public NodeAbilene(int uniqueId, String id, String longitude, String latitude) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public NodeAbilene(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public int getUniqueId() {
        return uniqueId;
    }
}

class Link {
    private String id;
    private String source;
    private String destination;
    private double preInstalledCapacity;
    private  double weight;

    public Link(String id, String source, String destination, double preInstalledCapacity) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.preInstalledCapacity = preInstalledCapacity;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getPreInstalledCapacity() {
        return preInstalledCapacity;
    }

    public double getWeight() {
        return weight;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}