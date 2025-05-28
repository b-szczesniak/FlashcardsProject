package flashcards.service;

import flashcards.model.Flashcard;
import flashcards.util.CSVUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardManager {
    private List<Flashcard> cards = new ArrayList<>();
    private int addCount = 0, searchCount = 0, removeCount = 0;

    public FlashcardManager() {}

    public void loadFromCSV(String path) throws IOException {
        cards = CSVUtil.readCSV(path);
    }

    public void saveToCSV(String path) throws IOException {
        CSVUtil.writeCSV(path, cards);
    }

    public void addCard(Flashcard card) {
        cards.add(card);
        addCount++;
    }

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

    public void removeCard(Flashcard card) {
        boolean removed = cards.remove(card);
        if (removed) removeCount++;
    }

    public List<Flashcard> getCardsSorted() {
        Collections.sort(cards);
        return List.copyOf(cards);
    }

    public int getAddCount()    { return addCount;}
    public int getSearchCount() { return searchCount;}
    public int getRemoveCount() { return removeCount;}
}
