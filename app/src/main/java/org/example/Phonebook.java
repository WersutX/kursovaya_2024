package org.example;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

class PhoneBook {
    private List<Subscriber> subscribers;

    public PhoneBook() {
        this.subscribers = new ArrayList<>();
    }

    public void addSubscriber(Subscriber subscriber) {
        // Проверяем, существует ли абонент с таким же именем
        for (Subscriber existingSubscriber : subscribers) {
            if (existingSubscriber.getName().equals(subscriber.getName())) {
                // Если абонент найден, просто добавляем номер к его записи
                existingSubscriber.addPhoneNumber(subscriber.getPhoneNumbers().get(0)); // Добавляем только первый номер из нового абонента
                return;
            }
        }
        // Если абонент не найден, добавляем нового
        subscribers.add(subscriber);
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(subscribers);
        }
    }

    public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            subscribers = (List<Subscriber>) in.readObject();
        }
    }
}