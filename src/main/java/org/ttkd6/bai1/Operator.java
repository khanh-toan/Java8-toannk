package org.ttkd6.bai1;
@FunctionalInterface
public interface Operator<T> {
    T process(T a, T b);
}
