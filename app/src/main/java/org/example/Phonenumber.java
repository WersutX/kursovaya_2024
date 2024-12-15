package org.example;

import java.io.Serializable;

class PhoneNumber implements Serializable {
    private String number;
    private String type;

    public PhoneNumber(String number, String type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + ": " + number;
    }
}
