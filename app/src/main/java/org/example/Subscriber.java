package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Subscriber implements Serializable, Comparable<Subscriber> {
    private String name;
    private List<PhoneNumber> phoneNumbers;

    public Subscriber(String name) {
        this.name = name;
        this.phoneNumbers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    @Override
    public int compareTo(Subscriber other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + " (" + phoneNumbers.size() + " phone(s))";
    }
}