package org.ttkd6.bai6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LambdaExpression {
    private static List<Developer> getDevelopers() {

        List<Developer> result = new ArrayList<>();

        result.add(new Developer("mkyong", new BigDecimal("70000"), 33));
        result.add(new Developer("alvin", new BigDecimal("80000"), 20));
        result.add(new Developer("jason", new BigDecimal("100000"), 10));
        result.add(new Developer("iris", new BigDecimal("170000"), 55));

        return result;
    }

    public static void main(String[] args) {
        List<Developer> listDevs = getDevelopers();

        System.out.println("Before Sort");
        for (Developer developer : listDevs) {
            System.out.println(developer);
        }

        //sort by age
        listDevs.sort((Developer d1, Developer d2)->d1.getAge()-d2  .getAge());

        //lambda
        //listDevs.sort((o1, o2)->o1.getSalary().compareTo(o2.getSalary()));

        //Reversed sort
        /*Comparator<Developer> salaryComparator = (o1, o2)->o1.getSalary().compareTo(o2.getSalary());
        listDevs.sort(salaryComparator.reversed());*/

        System.out.println("After Sort");
        listDevs.forEach(d -> System.out.println(d));
    }
}
