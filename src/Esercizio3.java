import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Esercizio3 {
    public static int numeroPromossi = 0;
    public static int numeroDipendenti = 0;
    public static double[] punteggi;
    public static double threshold = 0;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Utilizzo: java PremioPromozioneDipendenti <file_di_input>");
            return;
        }
        punteggi = new double[0];
        String inputFile = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("# numero di dipendenti")) {
                    String tmp = reader.readLine();
                    numeroDipendenti = Integer.parseInt(tmp);
                }
                if (line.contains("# vettore")) {
                    int iteration = 0;
                    while (!line.contains("# soglia k")) {
                        line = reader.readLine();
                        if (line.matches("[0-9]*\\.?[0-9]+")) {
                            punteggi = Arrays.copyOf(punteggi, iteration + 1);
                            punteggi[iteration] = Double.parseDouble(line);
                            iteration++;
                        } else {
                            threshold = Double.parseDouble(reader.readLine());
                            break;
                        }
                    }
                }
            }
            reader.close();
            Arrays.sort(punteggi);
            numeroPromossi = contaDipendentiPromossi(punteggi, threshold);

             System.out.println("Numero totale di dipendenti premiati: " + numeroPromossi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int contaDipendentiPromossi(double[] indices, double k) {
        return contaDipendentiPromossiDivideEtImpera(indices, k, 0, indices.length - 1);
    }

    public static int contaDipendentiPromossiDivideEtImpera(double[] indices, double k, int start, int end) {
        if (start > end) {
            return 0;
        }

        int middle = (start + end) / 2;
        double midValue = indices[middle];

        if (midValue > k) {
            // Il dipendente al centro supera la soglia k, quindi conta se stesso e
            // le porzioni a sinistra e destra.
            return 1 + contaDipendentiPromossiDivideEtImpera(indices, k, start, middle - 1)
                    + contaDipendentiPromossiDivideEtImpera(indices, k, middle + 1, end);
        } else {
            // Il dipendente al centro non supera la soglia k, quindi cerca solo a destra.
            return contaDipendentiPromossiDivideEtImpera(indices, k, middle + 1, end);
        }
    }

}
