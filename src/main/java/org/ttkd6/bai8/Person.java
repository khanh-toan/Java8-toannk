package org.ttkd6.bai8;

import java.util.Arrays;
import java.util.List;

public class Person {
    private String name;
    private List<String> phoneNumbers;

    public Person(String name, List<String> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }
}

