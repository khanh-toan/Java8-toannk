package org.shopping.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.shopping.entity.Account;
import org.shopping.entity.Order;
import org.shopping.entity.Product;
import org.shopping.form.AccountChangePasswordForm;
import org.shopping.form.AccountDTO;
import org.shopping.form.AccountInfoForm;
import org.shopping.form.ProductForm;
import org.shopping.model.OrderInfo;
import org.shopping.service.AccountService;
import org.shopping.service.OrderService;
import org.shopping.service.ProductService;
import org.shopping.service.UserDetailsServiceImpl;
import org.shopping.utils.ConvertUtils;
import org.shopping.validator.AccountInfoValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;
    private final ProductService productService;
    private final AccountInfoValidator accountInfoValidator;
    private final UserDetailsServiceImpl userDetailsService;
    private final OrderService orderService;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null){
            return;
        }
        if (target.getClass() == AccountInfoForm.class){
            dataBinder.setValidator(accountInfoValidator);
            System.out.println("AccountInfo Valid");
        }
    }

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

        System.out.println("Account!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " + account.getId());

        accountInfoForm = new AccountInfoForm(account);

        System.out.println("AccountInfo: " + accountInfoForm);

        AccountChangePasswordForm accountChangePasswordForm = new AccountChangePasswordForm(account);
        model.addAttribute("accountChangePasswordForm", accountChangePasswordForm);
        model.addAttribute("accountInfoForm", accountInfoForm);
        return "accountInfo";
    }

    @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.POST)
    public String userInfo(Model model,
                           @ModelAttribute("accountInfoForm") @Validated AccountInfoForm accountInfoForm, BindingResult result,
                           final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            return "accountInfo";
        }
        System.out.println("Form Account: " + accountInfoForm);
        try {
            accountService.updateProfile(accountInfoForm.getAccount());

            //Neu update username thi cap nhat lai SecurityContextHolder
            Account updatedAccount = accountInfoForm.getAccount();
            UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(updatedAccount.getUsername());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedUserDetails,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("error", message);
            model.addAttribute("hasErrors", true);
            return "accountInfo";
        }
        return "redirect:accountInfo";
    }

    @RequestMapping(value = {"/accountImage"}, method = RequestMethod.GET)
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

    @GetMapping(value = { "/admin/changePassword" })
    public String userChangePassword(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccountChangePasswordForm accountChangePassword = null;
        Account account = accountService.findByUserName(userDetails.getUsername());
        accountChangePassword = new AccountChangePasswordForm(account);
        model.addAttribute("accountChangePasswordForm", accountChangePassword);
        return "changePassword";
    }

    @PostMapping(value = { "/admin/changePassword"})
    public String userChangePassword(Model model,
                                 @ModelAttribute("accountChangePasswordForm") @Validated AccountChangePasswordForm accountChangePasswordForm,
                                 BindingResult result, final RedirectAttributes redirectAttributes) {
        if(result.hasErrors()){
            return "changePassword";
        }
        try {
            accountService.updatePassword(accountChangePasswordForm);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (Exception e) {
            System.out.println("ALTER");
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("error", message);
            return "changePassword";
        }
        return "redirect:changePassword";
    }

    @GetMapping(value = "/orderList")
    public String orderList(Model model,
                            @RequestParam(value = "name", required = false,defaultValue = "") String likeName,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                            @RequestParam(value = "size", required = false, defaultValue = "8") int size) {
        // Đảm bảo currentPage không nhỏ hơn 1
        if (currentPage < 1) {
            currentPage = 1;
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountService.findByUserName(userDetails.getUsername());

        Page<Order> orderPage = orderService.getOrders(likeName, currentPage, size, account.getId(), account.getIsDeleted());

        List<Order> list = new ArrayList<>();
        list = ConvertUtils.convertList(orderPage.getContent(), Order.class);

        model.addAttribute("paginationPages", list);
        model.addAttribute("searchName", likeName);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", orderPage.getTotalPages()); // Thêm tổng số trang
        model.addAttribute("totalItems", orderPage.getTotalElements()); // Thêm tổng số sản phẩm
        return "orderList";
    }

    @GetMapping(value = { "/admin/productManagement" })
    public String listUserHandler(Model model,
                                  @RequestParam(value = "name", required = false,defaultValue = "") String likeName,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                                  @RequestParam(value = "size", required = false, defaultValue = "8") int size) {
        Page<Product> productPage = productService.getProducts(likeName, null, currentPage, size);
        model.addAttribute("paginationPages", productPage);
        model.addAttribute("searchName", likeName);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", productPage.getTotalPages()); // Thêm tổng số trang
        model.addAttribute("totalItems", productPage.getTotalElements()); // Thêm tổng số sản phẩm
        return "admin/productManagement";
    }

    @GetMapping(value = "/admin/updateProduct")
    public String updateProduct(Model model, @RequestParam("id") Integer id){
        ProductForm productForm = null;

        if (id != null) {
            Optional<Product> productFromDB = productService.selectById(id);
            if (productFromDB.isPresent()) {
                Product product = productFromDB.get();
                productForm = new ProductForm(product);
            }
        }
        if (productForm == null) {
            productForm = new ProductForm();
            productForm.setNewProduct(true);
        }
        model.addAttribute("productForm", productForm);
        return "admin/product";
    }

    @PostMapping(value = "/admin/updateProduct")
    public String updateProduct(Model model, @ModelAttribute("productForm") @Validated ProductForm productForm,
                                BindingResult result, final RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            return "admin/product";
        }
        try {
            productForm.validateInput();
            Product updateProduct = productForm.getProduct();
            productService.updateProduct(updateProduct);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            return "admin/product";
        }
        return "redirect:productManagement";
    }

    @GetMapping(value = "/admin/createProduct")
    public String createProduct(Model model){
        ProductForm productForm = new ProductForm();
        model.addAttribute("productForm", productForm);
        return "admin/createProduct";
    }

    @PostMapping(value = "/admin/createProduct")
    public String createProduct(Model model, @ModelAttribute("productForm") ProductForm productForm,
                                BindingResult result, final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/createProduct";
        }
        try {
            productForm.validateInput();
            Product createProduct = productForm.getProduct();
            productService.createProduct(createProduct);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            return "admin/createProduct";
        }
        return "redirect:productManagement";
    }

    @GetMapping(value = "/admin/manageAccount")
    public String listAccount(Model model,
                              @RequestParam(value = "name", required = false,defaultValue = "") String likeName,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                              @RequestParam(value = "size", required = false, defaultValue = "8") int size){
        if (currentPage < 1) {
            currentPage = 1;
        }

        Page<Account> accountPage = accountService.getAccounts(likeName, null, currentPage, size);

        List<Account> list = ConvertUtils.convertList(accountPage.getContent(), Account.class);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Account> filterList = list.stream()
                .filter(account -> !account.getUsername().equals(userDetails.getUsername()))
                .toList();

        model.addAttribute("paginationPages", filterList);
        model.addAttribute("searchName", likeName);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", accountPage.getTotalPages()); // Thêm tổng số trang
        model.addAttribute("totalItems", accountPage.getTotalElements()); // Thêm tổng số sản phẩm
        return "admin/accountManagement";
    }

    @PostMapping(value = "/admin/createAccount")
    public ResponseEntity<?> createAccount(Model model, @ModelAttribute AccountDTO accountDTO) {
        // Xử lý dữ liệu accountDTO nhận được từ form
        System.out.println("Username: " + accountDTO.getUsername());
        System.out.println("Password: " + accountDTO.getPassword());
        System.out.println("Status: " + accountDTO.getStatus());

        try {
            accountService.saveAccount(accountDTO);
            return ResponseEntity.ok("Account created successfully!");
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    @PostMapping(value = "/admin/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam("id") Integer id,
                                          @RequestParam("status") Boolean status){
        try {
            accountService.updateAccountStatus(id, status);
            return ResponseEntity.ok("Status updated successfully!");
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }
}
