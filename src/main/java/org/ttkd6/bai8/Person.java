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

public class FlatMapExample2 {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
                new Person("John", Arrays.asList("1234", "5678")),
                new Person("Jane", Arrays.asList("8765", "4321")),
                new Person("Jake", Arrays.asList("6789"))
        );

        // Sử dụng flatMap() để "trải phẳng" tất cả số điện thoại thành một danh sách duy nhất
        List<String> allPhoneNumbers = people.stream()
                .flatMap(person -> person.getPhoneNumbers().stream())
                .toList();

        System.out.println(allPhoneNumbers);
    }
}