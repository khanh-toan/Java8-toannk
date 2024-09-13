package org.shopping.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.shopping.entity.Account;
import org.shopping.form.AccountInfoForm;
import org.shopping.service.AccountService;
import org.springframework.boot.Banner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.Optional;

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

        System.out.println("Account!!!!!!!!!!: " + account);

        accountInfoForm = new AccountInfoForm(account);

        System.out.println("AccountInfo: " + accountInfoForm);
        model.addAttribute("accountInfoForm", accountInfoForm);
        return "accountInfo";
    }

    @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.POST)
    public String userInfo(Model model,
                           @ModelAttribute("accountInform") @Validated AccountInfoForm accountInfoForm, BindingResult result,
                           final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            return "accountInfo";
        }
        System.out.println("Form Account: " + accountInfoForm);
        try {
            accountService.updateProfile(accountInfoForm.getAccount());
        }catch (IOException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("error", message);
            return "accountInfo";
        }
        return "redirect:accountInfo";
    }

    @RequestMapping(value = {"/productImage"}, method = RequestMethod.GET)
    public void accountImage(HttpServletRequest request, HttpServletResponse response, Model model,
                               @RequestParam("id") Integer id) throws IOException{
        Account fromAccountDB = null;
        if(id != null) {
            Optional<Account> findAccount = accountService.selectById(id);
            if (findAccount.isPresent()) {
                fromAccountDB = findAccount.get();
            }
        }
        if(fromAccountDB != null && fromAccountDB.getImage() != null){
            String contentType = accountService.getImageContentType(fromAccountDB.getImage());
            if (contentType != null) {
                response.setContentType(contentType);
                response.getOutputStream().write(fromAccountDB.getImage());
                response.getOutputStream().close();
            } else {
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported image type");
            }
        }else{
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product image not found");
        }
    }

}
