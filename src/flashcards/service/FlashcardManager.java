package flashcards.service;

import flashcards.model.Flashcard;
import flashcards.util.CSVUtil;

import java.io.IOException;
import java.util.*;

/**
 * Zarządza kolekcją fiszek: CRUD, sortowanie i statystyki operacji.
 */
public class FlashcardManager {
    private List<Flashcard> cards = new ArrayList<>();
    private int addCount = 0, searchCount = 0, removeCount = 0;

    /**
     * Wczytuje fiszki z pliku CSV, zamieniając obecną listę.
     *
     * @param path ścieżka do pliku CSV
     * @throws IOException w przypadku błędu I/O
     */
    public void loadFromCSV(String path) throws IOException {
        cards = CSVUtil.readCSV(path);
    }

    /**
     * Zapisuje aktualną listę fiszek do pliku CSV.
     *
     * @param path ścieżka wyjściowa do pliku CSV
     * @throws IOException w przypadku błędu I/O
     */
    public void saveToCSV(String path) throws IOException {
        CSVUtil.writeCSV(path, cards);
    }

    /**
     * Dodaje nową fiszkę do listy i inkrementuje licznik.
     *
     * @param card fiszka do dodania
     */
    public void addCard(Flashcard card) {
        cards.add(card);
        addCount++;
    }

    /**
     * Wyszukuje fiszki po terminie lub tłumaczeniu (dokładne porównanie ignorujące wielkość liter).
     *
     * @param term wyszukiwany tekst
     * @return lista pasujących fiszek
     */
    public List<Flashcard> searchCard(String term) {
        searchCount++;
        List<Flashcard> result = new ArrayList<>();
        for (Flashcard card : cards) {
            if (card.getTerm().equalsIgnoreCase(term)
                || card.getTranslation().equalsIgnoreCase(term)) {
                result.add(card);
            }
        }
        return result;
    }

    /**
     * Usuwa podaną fiszkę z listy i inkrementuje licznik.
     *
     * @param card fiszka do usunięcia
     * @return true jeśli usunięto, false jeśli nie znaleziono
     */
    public void removeCard(Flashcard card) {
        boolean removed = cards.remove(card);
        if (removed) removeCount++;
    }

    /**
     * Zwraca posortowaną listę fiszek (według term) bez możliwości modyfikacji.
     *
     * @return niemodyfikowalna lista fiszek posortowana
     */
    public List<Flashcard> getCardsSorted() {
        Collections.sort(cards);
        return List.copyOf(cards);
    }

    /**
     * Zwraca liczbę dodanych fiszek.
     *
     * @return liczba operacji add()
     */
    public int getAddCount()    { return addCount;}

    /**
     * Zwraca liczbę operacji wyszukiwania.
     *
     * @return liczba operacji search()
     */
    public int getSearchCount() { return searchCount;}

    /**
     * Zwraca liczbę operacji usuwania.
     *
     * @return liczba operacji remove()
     */
    public int getRemoveCount() { return removeCount;}
}
