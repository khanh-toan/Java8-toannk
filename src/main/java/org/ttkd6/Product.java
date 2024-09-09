    package org.ttkd6;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;

    import java.util.Date;

    @Builder
    @Data
    @AllArgsConstructor
    public class Product {
        private Integer id;
        private Integer categoryId;
        private String name;
        private Date saleDate;
        private Integer quality;
        private Boolean isDelete;
    }

