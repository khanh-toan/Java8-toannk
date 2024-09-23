package org.shopping.form;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer id;
    private Integer productId;
    private Integer userId;
    private Integer rating;
    private String comment;
    private Boolean isDeleted;
    private String userName;
    private Date createdAt;

    public ReviewDTO(Integer rating, String comment, Date createdAt, String userName) {
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userName = userName;
    }

}
