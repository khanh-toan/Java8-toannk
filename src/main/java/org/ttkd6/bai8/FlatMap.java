package org.ttkd6.bai8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMap {
    public static void main(String[] args) {
        List<List<String>> listOfLists = Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("d", "e"),
                Arrays.asList("f", "g", "h")
        );

        // Sử dụng map() để tạo một Stream của các List
        // Nếu sử dụng map trong này thì kết quả sẽ là
        // [java.util.stream.ReferencePipeline$Head@4eec7777, java.util.stream.ReferencePipeline$Head@3b07d329, java.util.stream.ReferencePipeline$Head@41629346]
        // đây là danh sách stream của các stream chứ không thể lấy ra 1 list chuỗi
        /*List<Stream<String>> listStream = listOfLists.stream()
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());*/

        // Sử dụng flatMap
        List<String> flatList = listOfLists.stream()
                .flatMap(list -> list.stream())
                .toList();

        System.out.println(flatList);
    }
}
