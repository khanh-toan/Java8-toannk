package org.shopping.service;

import lombok.RequiredArgsConstructor;
import org.shopping.entity.Account;
import org.shopping.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account findByUserName(String username) {
        Account account = accountRepository.findByUsername(username);
        return account;
    }
}
