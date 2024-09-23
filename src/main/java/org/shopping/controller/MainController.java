package org.shopping.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import org.shopping.entity.Account;
import org.shopping.entity.Order;
import org.shopping.entity.Product;
import org.shopping.entity.Review;
import org.shopping.form.CustomerForm;
import org.shopping.form.ProductForm;
import org.shopping.form.ReviewDTO;
import org.shopping.model.CartInfo;
import org.shopping.model.CustomerInfo;
import org.shopping.model.OrderInfo;
import org.shopping.model.ProductInfo;
import org.shopping.service.AccountService;
import org.shopping.service.OrderService;
import org.shopping.service.ProductService;
import org.shopping.service.ReviewService;
import org.shopping.utils.CartsUtils;
import org.shopping.utils.ConvertUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ProductService productService;
    private final OrderService orderService;
    private final AccountService accountService;
    private final ReviewService reviewService;

    @GetMapping("/productList")
    public String listUserHandler(Model model,
                                  @RequestParam(value = "name", required = false,defaultValue = "") String likeName,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                                  @RequestParam(value = "size", required = false, defaultValue = "8") int size) {
        // Đảm bảo currentPage không nhỏ hơn 1
        if (currentPage < 1) {
            currentPage = 1;
        }

        Page<Product> productPage = productService.getProducts(likeName, true, currentPage, size);

        List<Product> list = new ArrayList<>();
        list = ConvertUtils.convertList(productPage.getContent(), Product.class);

        model.addAttribute("paginationPages", list);
        model.addAttribute("searchName", likeName);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", productPage.getTotalPages()); // Thêm tổng số trang
        model.addAttribute("totalItems", productPage.getTotalElements()); // Thêm tổng số sản phẩm
        return "productList";
    }

    @GetMapping(value = {"/productImage"})
    public void accountImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("id") Integer id) throws IOException{
        Product fromProductDB = null;
        if(id != null) {
            Optional<Product> findAccount = productService.selectById(id);
            if (findAccount.isPresent()) {
                fromProductDB = findAccount.get();
            }
        }
        if(fromProductDB != null && fromProductDB.getImage() != null){
            String contentType = productService.getImageContentType(fromProductDB.getImage());
            if (contentType != null) {
                response.setContentType(contentType);
                response.getOutputStream().write(fromProductDB.getImage());
                response.getOutputStream().close();
            } else {
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported image type");
            }
        }else{
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product image not found");
        }
    }

    @GetMapping({ "/buyProduct" })
    public String listProductHandler(HttpServletRequest request, Model model,
                                     @RequestParam(value = "id", defaultValue = "") Integer id){
        Product product = null;
        if(id != null){
            Optional<Product> productExist = productService.selectById(id);
            product = productExist.get();
        }
        if(product != null){
            CartInfo cartInfo = CartsUtils.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.addProduct(productInfo, 1);
        }
        return "redirect:/shoppingCart";
    }

    @GetMapping(value = { "/shoppingCart"})
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = CartsUtils.getCartInSession(request);
        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    @PostMapping(value = { "/shoppingCart"})
    public String shoppingCartHandler(HttpServletRequest request, Model model, @ModelAttribute("cartForm") CartInfo cartInfo) {
        CartInfo myCart = CartsUtils.getCartInSession(request);
        try {
            myCart.updateQuantity(cartInfo);
            model.addAttribute("cartForm", myCart);
            return "redirect:/shoppingCart";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cartForm", myCart);
            return "shoppingCart";
        }
    }

    @GetMapping(value = {"/shoppingCartCustomer"})
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model){
        CartInfo cartInfo = CartsUtils.getCartInSession(request);
        if (cartInfo.isEmpty()){
            return "redirect:/shoppingCart";
        }
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        CustomerForm customerForm = new CustomerForm(customerInfo);
        model.addAttribute("customerForm", customerForm);
        return "shoppingCartCustomer";
    }

    @PostMapping(value = "/shoppingCartCustomer")
    public String shoppingCartCustomer(HttpServletRequest request,
                                       Model model,
                                       @ModelAttribute("customerForm") @Validated CustomerForm customerForm,
                                       BindingResult result,
                                       final RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            customerForm.setValid(false);
            // Forward tới trang nhập lại.
            return "shoppingCartCustomer";
        }
        customerForm.setValid(true);
        CartInfo cartInfo = CartsUtils.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);
        return "redirect:/shoppingCartConfirmation";
    }

    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model){
        CartInfo cartInfo = CartsUtils.getCartInSession(request);
        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";
        }
        model.addAttribute("myCart", cartInfo);
        return "shoppingCartConfirmation";
    }

    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = CartsUtils.getCartInSession(request);
        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";
        }
        try {
            orderService.addOrder(cartInfo);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("myCart", cartInfo);
            return "shoppingCartConfirmation";
        }
        // Xóa giỏ hàng khỏi session.
        CartsUtils.removeCartInSession(request);
        // Lưu thông tin đơn hàng cuối đã xác nhận mua.
        CartsUtils.storeLastOrderedCartInSession(request, cartInfo);
        return "redirect:/shoppingCartFinalize";
    }

    @RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {
        CartInfo lastOrderedCart = CartsUtils.getLastOrderedCartInSession(request);
        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }

    @GetMapping(value = "/admin/order")
    public String orderView(Model model, @RequestParam("orderId") Integer orderId){
        OrderInfo orderInfo = orderService.findOrderDetailById(orderId);
        model.addAttribute("orderInfo", orderInfo);
        return "order";
    }

    @GetMapping("/productDetail")
    public String productDetail(@RequestParam("id") Integer productId, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountService.findByUserName(userDetails.getUsername());
        ProductForm productForm = productService.getProductDetails(productId, account.getId());
        model.addAttribute("productForm", productForm);
        model.addAttribute("userId", account.getId());
        return "productDetail";  // Tên trang HTML cho chi tiết sản phẩm
    }

    @PostMapping("/addReview")
    public String addReview(@RequestParam("productId") Integer productId,
                            @RequestParam("userId") Integer userId,
                            @RequestParam("rating") Integer rating,
                            @RequestParam("comment") String comment,
                            RedirectAttributes redirectAttributes) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setProductId(productId);
        reviewDTO.setUserId(userId);
        reviewDTO.setRating(rating);
        reviewDTO.setComment(comment);
        reviewDTO.setCreatedAt(new Date());
        reviewService.saveReview(reviewDTO);
        // Thêm thông báo thành công
        redirectAttributes.addFlashAttribute("successMessage", "Your review has been submitted successfully!");
        // Redirect về trang chi tiết sản phẩm
        return "redirect:/productDetail?id=" + productId;
    }

}
