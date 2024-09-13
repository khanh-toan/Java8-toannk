package org.shopping.form;

import lombok.Data;
import org.shopping.model.CustomerInfo;

@Data
public class CustomerForm {
    private String name;
    private String address;
    private String email;
    private String phone;

    private boolean valid;

    public CustomerForm(CustomerInfo customerInfo) {
        if (customerInfo != null) {
            this.name = customerInfo.getName();
            this.address = customerInfo.getAddress();
            this.email = customerInfo.getEmail();
            this.phone = customerInfo.getPhone();
        }
    }


}
