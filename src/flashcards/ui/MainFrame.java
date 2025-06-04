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

/**
 * Główne okno aplikacji z GUI do obsługi fiszek.
 */
public class MainFrame extends JFrame {
    private FlashcardManager mgr = new FlashcardManager();
    private long startTime;
    private DefaultTableModel tableModel;
    private JTable table;

    /**
     * Konstruktor okna inicjalizuje komponenty GUI i ładuje dane.
     */
    public MainFrame() {
        super("Fiszki do angielskiego");
        startTime = System.currentTimeMillis();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(900, 600);
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

    /**
     * Tworzy pasek narzędzi z przyciskami i przypisuje akcje.
     */
    private void initToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setPreferredSize(new Dimension(0, 60));
        JButton btnAdd = new JButton("Dodaj");
        JButton btnRemove = new JButton("Usuń");
        JButton btnSearch = new JButton("Wyszukaj");
        JButton btnUpdate = new JButton("Zaktualizuj");
        JButton btnShow = new JButton("Pokaż wszystkie");
        JButton btnExit = new JButton("Zapisz i zakończ");

        Font btnFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        Dimension btnSize = new Dimension(150, 45);
        for (JButton btn : new JButton[]{btnAdd, btnRemove, btnSearch, btnUpdate, btnShow, btnExit}) {
            btn.setFont(btnFont);
            btn.setPreferredSize(btnSize);
        }

        btnAdd.addActionListener(e -> addCard());
        btnRemove.addActionListener(e -> removeCard());
        btnSearch.addActionListener(e -> searchCard());
        btnUpdate.addActionListener(e -> updateCard());
        btnShow.addActionListener(e -> updateTable(mgr.getCardsSorted()));
        btnExit.addActionListener(e -> onExit());

        toolBar.add(btnAdd);
        toolBar.add(btnRemove);
        toolBar.add(btnSearch);
        toolBar.add(btnUpdate);
        toolBar.add(btnShow);
        toolBar.addSeparator(new Dimension(20, 0));
        toolBar.add(btnExit);

        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Inicjalizuje tabelę i ładuje dane z CSV.
     */
    private void initTable() {
        String[] columns = {"Słowo", "Tłumaczenie"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setPreferredSize(new Dimension(table.getWidth(), 32));
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        try {
            mgr.loadFromCSV("cards.csv");
            updateTable(mgr.getCardsSorted());
        } catch (IOException ignored) {
        }
    }

    /**
     * Odświeża zawartość tabeli listą fiszek.
     *
     * @param list lista fiszek do wyświetlenia
     */
    private void updateTable(List<Flashcard> list) {
        tableModel.setRowCount(0);
        for (Flashcard f : list) {
            tableModel.addRow(new Object[]{f.getTerm(), f.getTranslation()});
        }
    }

    /**
     * Obsługa dodania nowej fiszki poprzez okno dialogowe.
     */
    private void addCard() {
        try {
            String term = JOptionPane.showInputDialog(this, "Wprowadź słowo (ang.):");
            boolean ScontainsDigit = term.chars().anyMatch(Character::isDigit);
            if (term == null || term.trim().isEmpty() || ScontainsDigit) return;
            String translation = JOptionPane.showInputDialog(this, "Wprowadź tłumaczenie:");
            boolean TcontainsDigit = translation.chars().anyMatch(Character::isDigit);
            if (translation == null || translation.trim().isEmpty() || TcontainsDigit) return;
            mgr.addCard(new Flashcard(term.trim(), translation.trim()));
            updateTable(mgr.getCardsSorted());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obsługa aktualizacji fiszki poprzez okna dialogowe.
     */
    private void updateCard()
    {
        try {
            String searchTerm = JOptionPane.showInputDialog(this, "Wyszukaj słowo, które chcesz zaktualizować (ang.) :");
            if (searchTerm == null || searchTerm.trim().isEmpty()) return;
            List<Flashcard> results = mgr.searchCard(searchTerm.trim());
            if (!results.isEmpty()) {
                String newTerm = JOptionPane.showInputDialog(this, "Wprowadź słowo (ang.):");
                boolean NScontainsDigit = newTerm.chars().anyMatch(Character::isDigit);
                if (newTerm == null || newTerm.trim().isEmpty() || NScontainsDigit) return;
                String newTranslation = JOptionPane.showInputDialog(this, "Wprowadź tłumaczenie:");
                boolean NTcontainsDigit = newTranslation.chars().anyMatch(Character::isDigit);
                if (newTranslation == null || newTranslation.trim().isEmpty() || NTcontainsDigit) return;
                mgr.updateCard(results, newTerm.trim(), newTranslation.trim());
                updateTable(mgr.getCardsSorted());
            }
            else {
                JOptionPane.showMessageDialog(this, "Nie znaleziono takiego słowa.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obsługa usuwania poprzez okno dialogowe.
     */
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
                updateTable(mgr.getCardsSorted());
            }
        }
    }

    /**
     * Wyszukuje fiszkę i wyświetla wyniki w tabeli.
     */
    private void searchCard() {
        String term = JOptionPane.showInputDialog(this, "Wyszukaj fiszkę (słowo lub tłumaczenie):");
        if (term == null || term.trim().isEmpty()) return;
        List<Flashcard> results = mgr.searchCard(term.trim());
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Brak wyników.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            updateTable(results);
        }
    }

    /**
     * Zapisuje plik CSV, wyświetla statystyki i zamyka aplikację.
     */
    private void onExit() {
        try {
            mgr.saveToCSV("cards.csv");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd zapisu: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        long duration = System.currentTimeMillis() - startTime;
        String[] statsCols = {"Statystyka", "Wartość"};
        DefaultTableModel statsModel = new DefaultTableModel(statsCols, 0);
        statsModel.addRow(new Object[]{"Czas działania (s)", duration / 1000});
        statsModel.addRow(new Object[]{"Dodano fiszek", mgr.getAddCount()});
        statsModel.addRow(new Object[]{"Usunięto fiszek", mgr.getRemoveCount()});
        statsModel.addRow(new Object[]{"Wyszukiwań", mgr.getSearchCount()});
        statsModel.addRow(new Object[]{"Zaktualizowano fiszek", mgr.getUpdateCount()});
        JTable statsTable = new JTable(statsModel);
        statsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statsTable.setRowHeight(24);
        statsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        JScrollPane statsScroll = new JScrollPane(statsTable);
        statsScroll.setPreferredSize(new Dimension(400, 150));
        JOptionPane.showMessageDialog(this, statsScroll, "Statystyki działania", JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }
}
