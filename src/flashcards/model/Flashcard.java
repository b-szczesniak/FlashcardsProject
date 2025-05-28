package flashcards.model;

public class Flashcard implements Comparable<Flashcard> {
    private String term;
    private String translation;

    /**
      @param term słowo w oryginale
     * @param translation tłumaczenie
     */
    public Flashcard(String term, String translation) {
        this.term = term;
        this.translation = translation;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public int compareTo(Flashcard o) {
        return this.term.compareToIgnoreCase(o.term);
    }

    @Override
    public String toString() {
        return term + " - " + translation;
    }
}
