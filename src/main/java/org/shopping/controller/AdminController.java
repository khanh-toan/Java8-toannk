package org.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.shopping.entity.Account;
import org.shopping.form.AccountInfoForm;
import org.shopping.service.AccountService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;
    @RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
    public String login(Model model) {
        System.out.println("test login!!!!!!!!!");
        return "login";
    }

    @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
    public String userInfo(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccountInfoForm accountInfoForm = null;
        Account account = accountService.findByUserName(userDetails.getUsername());
        accountInfoForm = new AccountInfoForm(account);
        model.addAttribute("accountInfoForm", accountInfoForm);
        return "accountInfo";
    }
}
