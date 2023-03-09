package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class DictionaryGUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final Map<String, String> dictionary;
    private final JTextArea textArea;

    public DictionaryGUI() {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        setTitle("Słownik");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dictionary = loadDictionary();

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));

        JButton addButton = new JButton("Dodaj słowo");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);

        JButton showButton = new JButton("Pokaż słownik");
        showButton.addActionListener(this);
        buttonPanel.add(showButton);

        JButton sortButton = new JButton("Sortuj po polsku");
        sortButton.addActionListener(this);
        buttonPanel.add(sortButton);

        JButton exitButton = new JButton("Wyjdź");
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);

        JButton deleteButton = new JButton("Usuń");
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);

        JButton deleteByNumberButton = new JButton("Usuń po numerze");
        deleteByNumberButton.addActionListener(this);
        buttonPanel.add(deleteByNumberButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private Map<String, String> loadDictionary() {
        Map<String, String> dictionary = new TreeMap<>();
        try {
            File file = new File("dictionary.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String[] word = scanner.nextLine().split(":");
                    dictionary.put(word[0], word[1]);
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    private void saveDictionary() {
        try {
            FileWriter fileWriter = new FileWriter("dictionary.txt");
            for (String key : dictionary.keySet()) {
                fileWriter.write(key + ":" + dictionary.get(key) + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addWord() {
        String word = JOptionPane.showInputDialog(this, "Podaj słowo w języku angielskim:");
        String translation = JOptionPane.showInputDialog(this, "Podaj tłumaczenie słowa:");
        if (word != null && translation != null) {
            dictionary.put(word, translation);
            saveDictionary();
            showDictionary();
        }
    }

    private void removeWord() {
        String word = JOptionPane.showInputDialog(this, "Podaj słowo, które chcesz usunąć:");
        if (word != null) {
            String removedTranslation = dictionary.remove(word);
            if (removedTranslation != null) {
                JOptionPane.showMessageDialog(this, "Usunięto słowo: " + word);
                saveDictionary();
            } else {
                JOptionPane.showMessageDialog(this, "Słowo nie istnieje w słowniku: " + word);
            }
        }
    }
    private void removeWordByNumber() {
        String numberString = JOptionPane.showInputDialog(this, "Podaj numer słowa, które chcesz usunąć:");
        if (numberString != null) {
            try {
                int number = Integer.parseInt(numberString);
                if (number < 1 || number > dictionary.size()) {
                    JOptionPane.showMessageDialog(this, "Nieprawidłowy numer słowa.");
                } else {
                    String word = null;
                    int count = 1;
                    for (String key : dictionary.keySet()) {
                        if (count == number) {
                            word = key;
                            break;
                        }
                        count++;
                    }
                    if (word != null) {
                        String removedTranslation = dictionary.remove(word);
                        if (removedTranslation != null) {
                            JOptionPane.showMessageDialog(this, "Usunięto słowo: " + word);
                            saveDictionary();
                        } else {
                            JOptionPane.showMessageDialog(this, "Słowo nie istnieje w słowniku: " + word);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowy numer słowa.");
            }
        }
    }



    private void showDictionary() {
        if (dictionary.isEmpty()) {
            textArea.setText("Słownik jest pusty.");
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (String key : dictionary.keySet()) {
                sb.append(count++).append(". ").append(key).append(" : ").append(dictionary.get(key)).append("\n");
            }
            textArea.setText(sb.toString());
        }
    }
    private void sortDictionary() {
        if (dictionary.isEmpty()) {
            textArea.setText("Słownik jest pusty.");
        } else {
            List<Map.Entry<String, String>> sortedList = new ArrayList<>(dictionary.entrySet());
            sortedList.sort(Map.Entry.comparingByValue());
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (Map.Entry<String, String> entry : sortedList) {
                sb.append(i++).append(". ").append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
            }
            textArea.setText(sb.toString());
        }
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Dodaj słowo":
                addWord();
                break;
            case "Pokaż słownik":
                showDictionary();
                break;
            case "Sortuj po polsku":
                sortDictionary();
                break;
            case "Wyjdź":
                saveDictionary();
                dispose();
                break;
            case "Usuń":
                removeWord();
                break;
            case "Usuń po numerze":
                removeWordByNumber();
                break;
            default:
                break;

        }
    }

    public static void main(String[] args) {
        DictionaryGUI gui = new DictionaryGUI();
        gui.setVisible(true);
        gui.showDictionary();
    }
}
