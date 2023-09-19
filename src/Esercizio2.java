import java.io.*;
import java.util.Scanner;

public class Esercizio2 {

    public static void main(String[] args) {
        String file = args[0];
        AVLTree tree = new AVLTree();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int iteration = 0;
            while ((line = reader.readLine()) != null) {
                String[] arrSplit = line.split(" ");
                tree.insert(Integer.parseInt(arrSplit[0]), arrSplit[1]);
                iteration++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Visita inorder dell'albero
        //System.out.println("Visita inorder dell'albero:");
        //tree.inorderTraversal();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Stampa tutte le chiavi in ordine crescente");
            System.out.println("2. Stampa tutte le chiavi maggiori di una soglia");
            System.out.println("3. Stampa delle coppie con a ≤ x ≤ b, e lunghezza della stringa ≤ s");
            System.out.println("4. Esci");

            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Chiavi in ordine crescente:");
                    tree.inorderTraversal();
                    break;

                case 2:
                    System.out.print("Inserisci la soglia: ");
                    int threshold = scanner.nextInt();
                    System.out.println("Chiavi maggiori di " + threshold + ":");
                    tree.printKeysGreaterThanThreshold(threshold);
                    break;

                case 3:
                    System.out.print("Inserisci il valore di a: ");
                    int a = scanner.nextInt();
                    System.out.print("Inserisci il valore di b: ");
                    int b = scanner.nextInt();
                    System.out.print("Inserisci il valore di s: ");
                    int s = scanner.nextInt();
                    System.out.println("Coppie con a ≤ chiave ≤ b e lunghezza del valore ≤ s:");
                    tree.printKeysInRangeAndStringLength(a, b, s);
                    break;

                case 4:
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }
}

class Node {
    protected int key;
    protected String value;
    protected int height;
    protected Node left;
    protected Node right;

    public Node(int key, String value) {
        this.key = key;
        this.value = value;
        this.height = 1;
    }
}

class AVLTree {
    private Node root;

    // Restituisce l'altezza di un nodo (o 0 se il nodo è nullo)
    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Restituisce il bilanciamento del nodo (differenza di altezza tra il sottoalbero sinistro e quello destro)
    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    // Esegue una rotazione a sinistra del nodo x
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Esegue una rotazione a destra del nodo y
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Inserisce un nodo con chiave 'key' nell'albero e ritorna il nuovo albero radicato in 'node'
    private Node insert(Node node, int key, String value) {
        if (node == null) {
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = insert(node.left, key, value);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value);
        } else {
            // Duplicati non sono consentiti
            return node;
        }

        // Aggiorna l'altezza di questo nodo
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Ottieni il bilanciamento del nodo
        int balance = getBalance(node);

        // Esegui le rotazioni se necessario per bilanciare l'albero
        if (balance > 1) {
            if (key < node.left.key) {
                return rightRotate(node);
            } else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }

        if (balance < -1) {
            if (key > node.right.key) {
                return leftRotate(node);
            } else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    // Inserisce una chiave nell'albero AVL
    public void insert(int key, String value) {
        root = insert(root, key, value);
    }

    // Esegue una visita inorder dell'albero
    private void inorderTraversal(Node node) {
        if (node != null) {
            inorderTraversal(node.left);
            System.out.println("<" + node.key + ", " + node.value + ">");
            inorderTraversal(node.right);
        }
    }

    // Esegue una visita inorder dell'albero
    public void inorderTraversal() {
        inorderTraversal(root);
    }

    public void printKeysGreaterThanThreshold(Node node, int threshold) {
        if (node == null) {
            return;
        }

        // Effettua una visita inorder inversa, esplorando prima il sottoalbero destro.
        printKeysGreaterThanThreshold(node.right, threshold);

        // Stampa la chiave solo se è maggiore della soglia.
        if (node.key > threshold) {
            System.out.println("<" + node.key + ", " + node.value + ">");
        }

        // Continua l'esplorazione del sottoalbero sinistro.
        printKeysGreaterThanThreshold(node.left, threshold);
    }

    public void printKeysGreaterThanThreshold(int threshold) {
        printKeysGreaterThanThreshold(root, threshold);
    }

    public void printKeysInRangeAndStringLength(Node node, int a, int b, int s) {
        if (node == null) {
            return;
        }

        // Effettua una visita inorder inversa, esplorando prima il sottoalbero destro.
        printKeysInRangeAndStringLength(node.right, a, b, s);

        // Stampa la chiave e il valore solo se soddisfano i criteri specificati.
        if (node.key >= a && node.key <= b && node.value.length() <= s) {
            System.out.println("<" + node.key + ", " + node.value + ">");
        }

        // Continua l'esplorazione del sottoalbero sinistro.
        printKeysInRangeAndStringLength(node.left, a, b, s);
    }

    public void printKeysInRangeAndStringLength(int a, int b, int s) {
        printKeysInRangeAndStringLength(root, a, b, s);
    }

    public Node search(Node node, int key) {
        if (node == null || node.key == key) {
            return node;
        }

        if (key < node.key) {
            return search(node.left, key);
        } else {
            return search(node.right, key);
        }
    }

    public Node search(int key) {
        return search(root, key);
    }
}