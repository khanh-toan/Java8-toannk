package org.shopping.form;

import lombok.*;
import org.shopping.entity.Account;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoForm {
    private int id;
    private String userName;
    private String address;
    private String userRole;
    private String description;
    private boolean newAccount = false;
    private byte[] image;
    // Upload file.
    private MultipartFile fileData;

    public AccountInfoForm(Account account) {
        this.id = Integer.parseInt(account.getId());
        this.userName = account.getUsername();
        this.userRole = account.getRole();
        this.address = account.getAddress();
        this.description = account.getDescription();
        this.image = account.getImage();
    }
}
