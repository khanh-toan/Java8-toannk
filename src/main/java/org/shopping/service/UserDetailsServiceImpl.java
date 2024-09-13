package org.shopping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.shopping.entity.Account;
import org.shopping.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        System.out.println(account);
        if (account == null){
            throw new UsernameNotFoundException(username);
        }
        String role = account.getRole();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        grantedAuthorities.add(grantedAuthority);

        boolean enabled = account.getIsDeleted();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return (UserDetails) new User(account.getUsername(), account.getAddress(), enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, grantedAuthorities);
    }
}
