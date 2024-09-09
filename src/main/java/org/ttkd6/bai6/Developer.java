package org.ttkd6.bai6;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Developer {
    private String name;
    private BigDecimal salary;
    private int age;
}
