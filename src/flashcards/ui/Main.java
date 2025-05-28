package flashcards.ui;

import flashcards.model.Flashcard;
import flashcards.service.FlashcardManager;
import flashcards.util.CSVUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Flashcard> demo = new ArrayList<>();
        demo.add(new Flashcard("apple", "jabłko"));
        demo.add(new Flashcard("Cat", "kot"));
        demo.add(new Flashcard("banana", "banan"));
        demo.forEach(System.out::println);
        Collections.sort(demo);
        System.out.println("Sorted");
        demo.forEach(System.out::println);

        CSVUtil.writeCSV("demo.csv", demo);
        List<Flashcard> list = CSVUtil.readCSV("demo.csv");
        list.forEach(System.out::println);

        FlashcardManager manager = new FlashcardManager();
        manager.addCard(new Flashcard("dog", "pies"));
        manager.addCard(new Flashcard("cat", "kot"));
        System.out.println(manager.getCardsSorted());
    }
}