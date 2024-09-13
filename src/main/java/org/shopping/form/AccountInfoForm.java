package org.shopping.form;

import lombok.*;
import org.shopping.entity.Account;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

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
        this.id = account.getId();
        this.userName = account.getUsername();
        this.userRole = account.getRole();
        this.address = account.getAddress();
        this.description = account.getDescription();
        this.image = account.getImage();
    }

    public Account getAccount() throws IOException {
        Account account = new Account();
        account.setId(this.id);
        account.setRole(this.userRole);
        account.setUsername(this.userName);
        account.setDescription(this.description);
        account.setAddress(this.address);
        account.setImage(this.image);
        account.setImage(this.fileData.getBytes());
        account.setUpdatedAt(new Date());
        return account;
    }
}
