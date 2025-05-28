package flashcards.util;

import flashcards.model.Flashcard;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVUtil {
    /**
     * Wczytuje fiszki z pliku CSV.
     * @param path ścieżka do pliku
     * @return lista fiszek
     * @throws IOException gdy pliku nie ma lub błąd I/O
     */
    public static List<Flashcard> readCSV(String path) throws IOException {
        List<Flashcard> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(path))){
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String[] split = sc.nextLine().split(",", 2);
                list.add(new Flashcard(split[0].trim(), split[1].trim()));
            }
        }
        return list;
    }

    /**
     * Zapisuje fiszki do pliku CSV
     * @param path ścieżka do pliku
     * @param data lista fiszek
     * @throws IOException gdy błąd I/O
     */
    public static void writeCSV(String path, List<Flashcard> data) throws IOException {
        try (PrintWriter pw = new PrintWriter(new File(path))){
            pw.println("term, translation");
            data.forEach(f -> pw.println(f.getTerm() + ", " + f.getTranslation()));
        }
    }
}
