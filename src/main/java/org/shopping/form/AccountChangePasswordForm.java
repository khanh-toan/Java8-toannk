package org.shopping.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shopping.entity.Account;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountChangePasswordForm {
    private int id;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    /*private boolean newPassword;*/

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public AccountChangePasswordForm(Account account){
        this.id = account.getId();
        this.currentPassword =account.getPassword();
    }

    public Account getAccount() throws IOException{
        Account account = new Account();
        account.setId(this.id);
        if (this.currentPassword != null) {
            account.setPassword(bCryptPasswordEncoder.encode(this.currentPassword));
        }
        return account;
    }

}
