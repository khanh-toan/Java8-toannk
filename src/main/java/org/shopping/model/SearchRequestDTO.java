package org.shopping.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class SearchRequestDTO {
    private Integer pageIndex;
    private Integer pageSize;
    private String orderBy;

    public void validateInput(){
        if(pageIndex == null || pageIndex < 1)
            pageIndex = DefaultValuePage.PAGE_INDEX;

        if(pageSize == null || pageSize <= 0)
            pageSize = DefaultValuePage.PAGE_SIZE;

        orderBy = validateOrder(orderBy);
    }

    public static class DefaultValuePage {
        public static final Integer PAGE_INDEX = 1;
        public static final Integer PAGE_SIZE = 5;
        public static final String SORT_BY = "id";
    }

    public static String validateOrder(String orderBy) {
        if (orderBy != null && orderBy.equalsIgnoreCase("asc"))
            return "ASC";
        return "DESC";
    }

}
