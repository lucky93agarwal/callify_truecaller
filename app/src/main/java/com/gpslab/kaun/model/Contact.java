package com.gpslab.kaun.model;

import android.net.Uri;

import java.util.ArrayList;

public class Contact {
    public String id;
    public String name;
    public Uri imageuri;
    public ArrayList<ContactEmail> emails;
    public ArrayList<ContactPhone> numbers;

    public Contact(String id, String name, Uri imageuri) {
        this.id = id;
        this.name = name;
        this.imageuri = imageuri;
        this.emails = new ArrayList<ContactEmail>();
        this.numbers = new ArrayList<ContactPhone>();
    }

    @Override
    public String toString() {
        String result = name;
        if (numbers.size() > 0) {
            ContactPhone number = numbers.get(0);
            result += " (" + number.number + " - " + number.type + ")";
        }
        if (emails.size() > 0) {
            ContactEmail email = emails.get(0);
            result += " [" + email.address + " - " + email.type + "]";
        }
        return result;
    }

    public void addEmail(String address, String type,String id) {
        emails.add(new ContactEmail(address, type, id));
    }

    public void addNumber(String number, String type) {
        numbers.add(new ContactPhone(number, type));
    }
}
