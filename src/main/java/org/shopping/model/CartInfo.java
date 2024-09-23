package org.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shopping.common.ConflictException;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartInfo {
    private int orderNum;
    private CustomerInfo customerInfo;
    private final List<CartLineInfo> cartLines = new ArrayList<>();

    private CartLineInfo findLineByProductId(int id){
        return this.cartLines.stream()
                .filter(line -> line.getProductInfo().getId() == id)
                .findFirst()
                .orElse(null);
    }
    public boolean isValidCustomer() {
        return this.customerInfo != null && this.customerInfo.isValid();
    }

     public void addProduct(ProductInfo productInfo, int quantity){
        CartLineInfo lineInfo = this.findLineByProductId(productInfo.getId());
        if(lineInfo == null){
            lineInfo= new CartLineInfo();
            lineInfo.setQuantity(0);
            lineInfo.setProductInfo(productInfo);
            this.cartLines.add(lineInfo);
        }
        int newQuantity = lineInfo.getQuantity() + quantity;
        if(newQuantity < 0){
            this.cartLines.remove(lineInfo);
        }else {
            lineInfo.setQuantity(newQuantity);
        }
     }

     public void updateProduct(int id, int quantity){
         CartLineInfo lineInfo = this.findLineByProductId(id);
         if(lineInfo != null){
             if(quantity <= 0){
                 this.cartLines.remove(lineInfo);
             }else{
                 lineInfo.setQuantity(quantity);
             }
         }else {

         }
     }

     public void removeProduct(ProductInfo productInfo){
         CartLineInfo lineInfo = this.findLineByProductId(productInfo.getId());
         if (lineInfo != null){
             this.cartLines.remove(lineInfo);
         }
     }

     public boolean isEmpty(){
        return this.cartLines.isEmpty();
     }

     public int getQuantityTotal(){
        return this.cartLines.stream()
                .mapToInt(CartLineInfo::getQuantity)
                .sum();
     }

     public void updateQuantity(CartInfo cartInfo){
         if (cartInfo != null) {
             List<CartLineInfo> lines = cartInfo.getCartLines();
             for (CartLineInfo line : lines) {
                 this.updateProduct(line.getProductInfo().getId(), line.getQuantity());
             }
         }
     }

    public Double getAmountTotal() {
        return this.cartLines.stream()
                .mapToDouble(CartLineInfo::getAmount)
                .sum();
    }
}
