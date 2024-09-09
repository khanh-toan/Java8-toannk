package org.ttkd6.bai7;

import java.util.Arrays;
import java.util.List;

public class Bai7 {
    public static void main(String[] args) {

        List<Person> persons = Arrays.asList(
                new Person("mkyong", 30),
                new Person("jack", 20),
                new Person("lawrence", 40)
        );

        // FindAny dùng để tìm và trả về bất kỳ phẩn tử nào trong stream, neu khong có trả về optional.empty()
        Person result1 = persons.stream()
                .filter(x -> "jack".equals(x.getName()))
                .findAny()
                .orElse(null);

        System.out.println(result1);

        Person result2 = persons.stream()
                .filter(x -> "ahmook".equals(x.getName()))
                .findAny()
                .orElse(null);

        System.out.println(result2);

        Person result3 = persons.stream()
                .filter((p) -> "jack".equals(p.getName()) && 20 == p.getAge())
                .findAny()
                .orElse(null);

        System.out.println("result 3 :" + result3);

        //or like this
        Person result4 = persons.stream()
                .filter(p -> {
                    if ("jack".equals(p.getName()) && 20 == p.getAge()) {
                        return true;
                    }
                    return false;
                }).findAny()
                .orElse(null);

        System.out.println("result 4 :" + result4);
    }
}
