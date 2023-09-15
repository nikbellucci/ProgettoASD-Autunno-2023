import java.io.*;

public class Esercizio2 {

    public static void main(String[] args) {
        String file = args[0];
        AVLTree tree = new AVLTree();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tmp = reader.readLine();
            int iteration = 0;
            while (tmp != null) {
                String[] arrSplit = tmp.split(" ");
                tree.insert(Integer.parseInt(arrSplit[0]), arrSplit[1]);
                tmp = reader.readLine();
                iteration++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Visita inorder dell'albero
        System.out.println("Visita inorder dell'albero:");
        tree.inorderTraversal();

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
}