import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class Esercizio3 {
    public static int numeroPromossi = 0;
    public static ArrayList<Double> punteggio;
    public static double threshold = 0;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Utilizzo: java PremioPromozioneDipendenti <file_di_input>");
            return;
        }
        punteggio = new ArrayList<>();
        String inputFile = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("# numero di dipendenti")) {
                    String tmp = reader.readLine();
                    numeroPromossi = Integer.parseInt(tmp);
                }
                if (line.contains("# vettore")) {
                    while (!line.contains("# soglia k")) {
                        line = reader.readLine();
                        if (line.matches("[0-9]*\\.?[0-9]+")) {
                            punteggio.add(Double.parseDouble(line));
                        } else {
                            threshold = Double.parseDouble(reader.readLine());
                            break;
                        }
                    }
                }
            }
            reader.close();
            System.out.println(punteggio.size());

            numeroPromossi = contaDipendentiPromossi(punteggio, threshold);

             System.out.println("Numero totale di dipendenti promossi: " + numeroPromossi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int contaDipendentiPromossi(ArrayList<Double> indices, double k) {
        return contaDipendentiPromossiDivideEtImpera(indices, k, 0, indices.size() - 1);
    }

    public static int contaDipendentiPromossiDivideEtImpera(ArrayList<Double> indices, double k, int start, int end) {
        if (start > end) {
            return 0;
        }

        int middle = (start + end) / 2;
        double midValue = indices.get(middle);

        if (midValue > k) {
            return 1 + contaDipendentiPromossiDivideEtImpera(indices, k, start, middle - 1)
                    + contaDipendentiPromossiDivideEtImpera(indices, k, middle + 1, end);
        } else {
            return contaDipendentiPromossiDivideEtImpera(indices, k, middle + 1, end);
        }
    }
}
