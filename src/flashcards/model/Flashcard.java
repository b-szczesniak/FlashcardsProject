package flashcards.model;

/**
 * Reprezentuje pojedynczą fiszkę (flashcard) z terminem i tłumaczeniem.
 */
public class Flashcard implements Comparable<Flashcard> {
    private String term;
    private String translation;

    /**
     * Tworzy nową fiszkę z terminem i tłumaczeniem
     *
      @param term słowo w oryginale
     * @param translation tłumaczenie
     */
    public Flashcard(String term, String translation) {
        this.term = term;
        this.translation = translation;
    }

    /**
     * Zwraca termin fiszki.
     *
     * @return termin (angielskie słowo)
     */
    public String getTerm() {
        return term;
    }

    /**
     * Ustawia termin fiszki.
     *
     * @param term nowe słowo (angielskie)
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * Zwraca tłumaczenie fiszki.
     *
     * @return tłumaczenie (polskie słowo)
     */
    public String getTranslation() {
        return translation;
    }

    /**
     * Ustawia tłumaczenie fiszki.
     *
     * @param translation nowe tłumaczenie (polskie słowo)
     */
    public void setTranslation(String translation) {
        this.translation = translation;
    }

    /**
     * Porównuje dwie fiszki leksykograficznie po terminie, ignorując wielkość liter.
     *
     * @param other druga fiszka do porównania
     * @return wartość ujemna, zero lub dodatnia zgodnie z kontraktem Comparable
     */
    @Override
    public int compareTo(Flashcard other) {
        return this.term.compareToIgnoreCase(other.term);
    }

    /**
     * Zwraca reprezentację tekstową fiszki w formacie "term - translation".
     *
     * @return tekstowa reprezentacja fiszki
     */
    @Override
    public String toString() {
        return term + " - " + translation;
    }
}
