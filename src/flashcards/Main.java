package flashcards;

import flashcards.ui.MainFrame;

import javax.swing.*;
import java.io.IOException;

/**
 * Punkt startowy aplikacji.
 */
public class Main {
    /**
     * Uruchamia główne okno aplikacji.
     *
     * @param args argumenty linii komend (nieużywane)
     */
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}