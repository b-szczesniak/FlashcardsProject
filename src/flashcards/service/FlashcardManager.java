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
    private int addCount = 0, searchCount = 0, removeCount = 0, updateCount = 0;

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
     * Pozwala na aktualizacje wyrazu i tłumaczenia fiszki
     *
     * @param cards lista znalezionych fiszek
     * @param newTerm nowy tekst którym zastępujemy poprzedni
     * @param newTranslation nowe tłumaczenie którym zastępujemy poprzednie
     * @return boolean zwraca true jeśli operacja się udała i false jeśli nie
     */
    public boolean updateCard(List<Flashcard> cards, String newTerm, String newTranslation) {
        if (cards.isEmpty()) {
            return false;
        }

        Flashcard card = cards.getFirst();
        if (newTerm != null && !newTerm.isEmpty()) {
            card.setTerm(newTerm);
        }
        if (newTranslation != null && !newTranslation.isEmpty()) {
            card.setTranslation(newTranslation);
        }
        updateCount++;
        return true;
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
     * @return liczba operacji addCard()
     */
    public int getAddCount()    { return addCount;}

    /**
     * Zwraca liczbę operacji wyszukiwania.
     *
     * @return liczba operacji searchCard()
     */
    public int getSearchCount() { return searchCount;}

    /**
     * Zwraca liczbę operacji usuwania.
     *
     * @return liczba operacji removeCard()
     */
    public int getRemoveCount() { return removeCount;}

    /**
     * Zwraca liczbę operacji aktualizowania.
     *
     * @return liczba operacji updateCard()
     */
    public int getUpdateCount() { return updateCount;}
}
