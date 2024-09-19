package org.shopping.form;

import lombok.Data;
import org.shopping.entity.Product;
import org.shopping.utils.ValidateUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Data
public class ProductForm {
    private int id;
    private String name;
    private String code;
    private double price;
    private String description;
    private boolean isDelete;
    private boolean newProduct = false;
    private byte[] image;
    // Upload file.
    private MultipartFile fileData;

    public ProductForm() {
        this.newProduct= true;
    }

    public ProductForm(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.code = product.getCode();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.isDelete = product.getIsDeleted();
    }

    public Product getProduct( ) throws IOException {
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setCode(this.code);
        product.setPrice(this.price);
        product.setDescription(this.description);
        product.setIsDeleted(this.isDelete);
        product.setCreatedAt(new Date());
        if(this.fileData.isEmpty()) {
            product.setImage(this.image);
        }else {
            product.setImage(this.getFileData().getBytes());
        }
        return product;
    }

    public void validateInput(){
        ValidateUtils.checkNullOrEmpty(name, "Name");
        ValidateUtils.checkDoubleInRange(price, "price", 1d, Double.MAX_VALUE);
    }
}
