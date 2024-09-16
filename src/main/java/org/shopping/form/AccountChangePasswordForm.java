package org.shopping.form;

import lombok.Data;
import org.shopping.entity.Account;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@Data
public class AccountChangePasswordForm {
    private int id;
    private String encrytedPassword;
    private String verifyPassword;
    private boolean newPassword;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public AccountChangePasswordForm(Account account){
        this.id = account.getId();
        this.encrytedPassword =account.getPassword();
    }

    public Account getAccount() throws IOException{
        Account account = new Account();
        account.setId(this.id);
        if (this.encrytedPassword != null) {
            account.setPassword(bCryptPasswordEncoder.encode(this.encrytedPassword));
        }
        return account;
    }

}
