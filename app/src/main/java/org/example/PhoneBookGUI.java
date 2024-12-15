package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;


public class PhoneBookGUI extends JFrame {
    private PhoneBook phoneBook;
    private JList<Subscriber> subscriberList;
    private DefaultListModel<Subscriber> listModel;
    private JTextField searchField;
    private JTextArea phoneNumbersArea;

    public PhoneBookGUI() {
        phoneBook = new PhoneBook();
        listModel = new DefaultListModel<>();
        subscriberList = new JList<>(listModel);
        searchField = new JTextField();
        phoneNumbersArea = new JTextArea(10, 30);
        phoneNumbersArea.setEditable(false);

        setTitle("Телефонный справочник");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Поиск:"));
        searchField.setPreferredSize(new Dimension(300, 30));  // Увеличиваем размер поля ввода
        searchPanel.setPreferredSize(new Dimension(400, 50));  // Устанавливаем предпочтительный размер панели
        searchPanel.add(searchField);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Subscriber list
        JScrollPane scrollPane = new JScrollPane(subscriberList);
        panel.add(scrollPane, BorderLayout.WEST);

        // Phone numbers area
        JPanel phonePanel = new JPanel(new BorderLayout());
        phonePanel.add(new JLabel("Телефонные номера:"), BorderLayout.NORTH);
        JScrollPane phoneScrollPane = new JScrollPane(phoneNumbersArea);
        phonePanel.add(phoneScrollPane, BorderLayout.CENTER);
        panel.add(phonePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Добавить абонента");
        JButton loadButton = new JButton("Загрузить");
        JButton saveButton = new JButton("Сохранить");
        buttonPanel.add(addButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Add button listener
        addButton.addActionListener(e -> addSubscriber());

        // Load and Save buttons
        loadButton.addActionListener(e -> loadPhoneBook());
        saveButton.addActionListener(e -> savePhoneBook());

        // Search functionality
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterSubscribers();
            }
        });

        // Subscriber list selection listener to show phone numbers
        subscriberList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showPhoneNumbers(subscriberList.getSelectedValue());
            }
        });
    }

    private void addSubscriber() {
        String name;
        do {
            name = JOptionPane.showInputDialog(this, "Введите имя абонента:");
            if (name == null) {
                // нажатие на cancel
                return;
            } else if (name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Имя абонента не может быть пустым", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (name.trim().isEmpty());

        Subscriber subscriber = new Subscriber(name);

        String number = null;
        String type = null;

        // Проверка для номера телефона
        boolean validNumber = false;
        do {
            number = JOptionPane.showInputDialog(this, "Введите номер телефона:");
            if (number == null) {
                // нажатие на cancel
                return;
            } else if (number.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Номер телефона не может быть пустым.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!number.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Номер телефона может содержать только цифры", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                validNumber = true; // номер валиден
            }
        } while (!validNumber); // повторяем, пока номер не станет валидным

        // Проверка для типа телефона
        boolean validType = false;
        do {
            type = JOptionPane.showInputDialog(this, "Введите тип телефона:");
            if (type == null) {
                // нажатие на cancel
                return;
            } else if (type.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Тип телефона не может быть пустым", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!type.matches("[а-яА-Я]+")) {
                JOptionPane.showMessageDialog(this, "Тип телефона может содержать только буквы", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                validType = true; // тип валиден
            }
        } while (!validType); // повторяем, пока тип не станет валидным

        // Добавляем только если поля удовлетворяют требования
        if (validNumber && validType) {
            subscriber.addPhoneNumber(new PhoneNumber(number, type));
            phoneBook.addSubscriber(subscriber);
            updateSubscriberList();
        }
    }




    private void filterSubscribers() {
        String searchText = searchField.getText().toLowerCase();
        List<Subscriber> filteredList = phoneBook.getSubscribers().stream()
                .filter(subscriber -> subscriber.getName().toLowerCase().contains(searchText) ||
                        subscriber.getPhoneNumbers().stream().anyMatch(phone -> phone.getNumber().contains(searchText)))
                .collect(Collectors.toList());
        listModel.clear();
        filteredList.forEach(listModel::addElement);
    }

    private void updateSubscriberList() {
        phoneBook.getSubscribers().sort(Subscriber::compareTo); // Сортировка абонентов по имени
        listModel.clear();
        phoneBook.getSubscribers().forEach(listModel::addElement);
    }

    private void showPhoneNumbers(Subscriber subscriber) {
        if (subscriber != null) {
            StringBuilder phoneNumbersText = new StringBuilder();
            for (PhoneNumber phoneNumber : subscriber.getPhoneNumbers()) {
                phoneNumbersText.append(phoneNumber.toString()).append("\n");
            }
            phoneNumbersArea.setText(phoneNumbersText.toString());
        } else {
            phoneNumbersArea.setText("");
        }
    }

    private void loadPhoneBook() {
        try {
            phoneBook.loadFromFile("phonebook.dat");
            updateSubscriberList();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки файла", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void savePhoneBook() {
        try {
            phoneBook.saveToFile("phonebook.dat");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка сохранения файла", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PhoneBookGUI frame = new PhoneBookGUI();
            frame.setVisible(true);
            //отображение gui
        });
    }
}