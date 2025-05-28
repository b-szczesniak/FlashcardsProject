package flashcards.ui;

import flashcards.model.Flashcard;
import flashcards.service.FlashcardManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {
    private FlashcardManager mgr = new FlashcardManager();
    private long startTime;
    private DefaultTableModel tableModel;
    private JTable table;

    public MainFrame() {
        super("Fiszki do angielskiego");
        startTime = System.currentTimeMillis();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initToolBar();
        initTable();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
    }

    private void initToolBar() {
        JToolBar toolBar = new JToolBar();
        JButton btnAdd = new JButton("Dodaj");
        JButton btnRemove = new JButton("Usuń");
        JButton btnSearch = new JButton("Wyszukaj");
        JButton btnShow = new JButton("Pokaż wszystkie");
        JButton btnExit = new JButton("Zapisz i zakończ");

        btnAdd.addActionListener(e -> addCard());
        btnRemove.addActionListener(e -> removeCard());
        btnSearch.addActionListener(e -> searchCard());
        btnShow.addActionListener(e -> updateTable(mgr.getCardsSorted()));
        btnExit.addActionListener(e -> onExit());

        toolBar.add(btnAdd);
        toolBar.add(btnRemove);
        toolBar.add(btnSearch);
        toolBar.add(btnShow);
        toolBar.addSeparator();
        toolBar.add(btnExit);

        add(toolBar, BorderLayout.NORTH);
    }

    private void initTable() {
        String[] columns = {"Słowo", "Tłumaczenie"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        // Na start załaduj istniejące fiszki, jeśli są
        try {
            mgr.loadFromCSV("cards.csv");
            updateTable(mgr.getCardsSorted());
        } catch (IOException ignored) {
        }
    }

    private void updateTable(List<Flashcard> list) {
        tableModel.setRowCount(0);
        for (Flashcard f : list) {
            tableModel.addRow(new Object[]{f.getTerm(), f.getTranslation()});
        }
    }

    private void addCard() {
        try {
            String term = JOptionPane.showInputDialog(this, "Wprowadź słowo (ang.):");
            if (term == null || term.trim().isEmpty()) return;
            String translation = JOptionPane.showInputDialog(this, "Wprowadź tłumaczenie:");
            if (translation == null || translation.trim().isEmpty()) return;
            mgr.addCard(new Flashcard(term.trim(), translation.trim()));
            JOptionPane.showMessageDialog(this, "Dodano fiszkę!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeCard() {
        String term = JOptionPane.showInputDialog(this, "Wprowadź słowo fiszki do usunięcia:");
        if (term == null || term.trim().isEmpty()) return;
        List<Flashcard> results = mgr.searchCard(term.trim());
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nie znaleziono fiszki.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Flashcard f = results.getFirst();
            int confirm = JOptionPane.showConfirmDialog(this, "Usunąć: " + f + "?", "Potwierdź", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mgr.removeCard(f);
                JOptionPane.showMessageDialog(this, "Usunięto fiszkę.");
            }
        }
    }

    private void searchCard() {
        String term = JOptionPane.showInputDialog(this, "Wyszukaj fiszkę (słowo lub tłumaczenie):");
        if (term == null || term.trim().isEmpty()) return;
        List<Flashcard> results = mgr.searchCard(term.trim());
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Brak wyników.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder();
            results.forEach(f -> sb.append(f).append("\n"));
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(300, 200));
            JOptionPane.showMessageDialog(this, scroll, "Wyniki wyszukiwania", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showAll() {
        List<Flashcard> list = mgr.getCardsSorted();
        String[] columns = {"Słowo", "Tłumaczenie"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Flashcard f : list) {
            model.addRow(new Object[]{f.getTerm(), f.getTranslation()});
        }
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scroll, "Wszystkie fiszki", JOptionPane.PLAIN_MESSAGE);
    }

    private void onExit() {
        try {
            mgr.saveToCSV("cards.csv");
            long duration = System.currentTimeMillis() - startTime;
            JOptionPane.showMessageDialog(this,
                    "Zapisano dane.\nCzas działania: " + duration/1000 + "s");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd zapisu: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
