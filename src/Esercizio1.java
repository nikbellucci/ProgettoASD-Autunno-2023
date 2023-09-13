import java.io.*;
import java.util.*;

public class Esercizio1 {

    public static int SEED_PRNG = 998755;
    public static int SIZE_MAP = 10000;
    public static int MAX_NUMBER_OF_KEY = 1000000000;
    public static int MAX_NUMBER_OF_VALUE = 700;
    public static void main(String[] args) {

        MapImplementation<Integer, Integer> testMap = new MapImplementation<>(SIZE_MAP);
        Random randKey = new Random(SEED_PRNG);
        Random randValue = new Random(SEED_PRNG);
        Random testRandKey = new Random(7105419);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("orionActiveSite.txt"));
            BufferedWriter writerInserimento = new BufferedWriter(new FileWriter("accessiInserimento.txt"));
            int key;
            int value;
            int iteration = 0;
            int numeroAccessiTotaliInserire = 0;
            while(testMap.size() < SIZE_MAP) {
                key = randKey.nextInt((MAX_NUMBER_OF_KEY - 1 ) + 1) + 1;
                value = randValue.nextInt((MAX_NUMBER_OF_VALUE - 1) + 1) + 1;
                testMap.inserire(key, value);
                numeroAccessiTotaliInserire += testMap.getAccessCounterInserire();
                writerInserimento.write("Chiave " + key + ". Numero di accessi: " + testMap.getAccessCounterInserire() + "\n");
                writer.write("<" + Integer.toString(key) + "," + Integer.toString(value) + ">");
                writer.newLine();
                iteration++;
            }
            writer.close();
            writerInserimento.write("Numero Accessi Medi in inserimento: " + calculateNMA(numeroAccessiTotaliInserire, 10000));
            writerInserimento.close();

            int[] testKey = new int[300];
            for (int i = 0; i < 300; i++) {
                testKey[i] = testRandKey.nextInt((MAX_NUMBER_OF_KEY - 1 ) + 1) + 1;
            }

            int numeroAccessiTotaliVerifica = 0;
            BufferedWriter writerVerifica = new BufferedWriter(new FileWriter("accessiVerifica.txt"));
            for (int i = 0; i < 300; i++) {
                testMap.verifica(testKey[i]);
                numeroAccessiTotaliVerifica += testMap.getAccessCounterVerifica();
                writerVerifica.write("Sito con chiave " + testKey[i] + ": Numero di accessi = " + testMap.getAccessCounterVerifica()+ "\n");
            }
            writerVerifica.write("Numero Accessi Medi in verifica: " + calculateNMA(numeroAccessiTotaliVerifica, 300));
            writerVerifica.close();

//            BufferedReader reader = new BufferedReader(new FileReader("accessiVerifica.txt"));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//            reader.close();

            System.out.println("Numero Accessi Medi in inserimento: " + calculateNMA(numeroAccessiTotaliInserire, 10000));
            System.out.println("Numero Accessi Medi in verifica: " + calculateNMA(numeroAccessiTotaliVerifica, 300));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static double calculateNMA(int totalAccesses, int numberOfOperations) {
        return (double) totalAccesses / numberOfOperations;
    }
}

class MapEntry<K, V> {
    private final K key;
    private V value;


    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}

class MapImplementation<K, V> {
    private LinkedList<MapEntry>[] entries;
    public int SIZE;
    private final static double MAXIMUM_LOAD = 0.90;
    private int elementCounter;
    private int accessCounterVerifica;

    private int accessCounterInserire;

    public MapImplementation (int size){
        this.SIZE = size;
        this.elementCounter = 0;
        this.entries = new LinkedList[SIZE];
        this.accessCounterVerifica = 0;
        this.accessCounterInserire = 0;
    }

    private int computeIndex(int key) {
        return key % Esercizio1.SIZE_MAP;
    }

    public void inserire(K key, V value) {
        int index = computeIndex((int) key);
        this.accessCounterInserire = 0;

        // Controlla se la lista è già presente nel vettore
        if (this.entries[index] == null) {
            this.entries[index] = new LinkedList<>();
            this.accessCounterInserire++;
        }

        // Controlla se la chiave è già presente nella lista
        boolean exists = false;
        for (MapEntry<K, V> entry : this.entries[index]) {
            this.accessCounterInserire++;
            if (entry.getKey().equals(key)) {
                exists = true;
                break;
            }
        }

        // Se la chiave non è presente nella lista, la inserisce
        if (!exists) {
            this.entries[index].add(new MapEntry<>(key, value));
            this.elementCounter++;
            this.accessCounterInserire++;
        }

        // Aggiorna il numero di accessi alla funzione
        //System.out.println("Sito con chiave " + key + " è stato inserito. Numero di accessi: " + accessCounterInserire);

    }

    public boolean verifica(int key) {
        int index = computeIndex(key);
        this.accessCounterVerifica = 0;
        if (this.entries[index] == null) {
            this.accessCounterVerifica++;
        } else {
            Iterator<MapEntry> iterator = this.entries[index].iterator();
            while (iterator.hasNext()) {
                MapEntry<K, V> entry = iterator.next();
                if (entry.getKey().equals(key)) {
                    accessCounterVerifica++;
                    //System.out.println("Sito con chiave " + key + " presente: Numero di accessi = " + this.accessCounterVerifica);
                    //System.out.println(index);
                    return true;
                } else {
                    this.accessCounterVerifica++;
                }
            }
            //System.out.println("Il sito con chiave " + key + " non è presente. Numero di accessi = " + this.accessCounterVerifica);
        }
        return false;
    }

    public int size() {
        return this.elementCounter;
    }

    public int getAccessCounterVerifica() {
        return accessCounterVerifica;
    }

    public int getAccessCounterInserire() {
        return accessCounterInserire;
    }

    public void stampa(int key) { //Aggiustarlo per le linkedlist
        int index = computeIndex((int)key);
        boolean exists = true;
        if (this.entries[index] != null) {
            for (MapEntry entry: this.entries[index]) {
                if (entry.getKey().equals(key)){
                    System.out.println("<" + entry.getKey() + "," + entry.getValue() + ">");
                    return;
                } else {
                    exists = false;
                }
            }
        }

        if (!exists) {
            System.out.println("<Chiave non trovata>");
        }
    }

    public void stampaTutto() {
        int iteration = 0;
        while(iteration < this.SIZE) {
            int iterationList = 0;
            if (this.entries[iteration] != null) {
                for (MapEntry entry: this.entries[iteration]) {
                    System.out.println("indice: " + iteration + "<" + entry.getKey() + "," + entry.getValue() + ">");
                }
            } else {
                System.out.println("Entry non esistente");
            }
            iteration++;
        }
    }
}