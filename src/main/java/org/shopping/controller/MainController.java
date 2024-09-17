package org.shopping.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import org.shopping.entity.Account;
import org.shopping.entity.Product;
import org.shopping.model.CartInfo;
import org.shopping.model.ProductInfo;
import org.shopping.service.AccountService;
import org.shopping.service.ProductService;
import org.shopping.utils.CartsUtils;
import org.shopping.utils.ConvertUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ProductService productService;
    /*private final AccountService accountService;*/

    @GetMapping("/productList")
    public String listUserHandler(Model model,
                                  @RequestParam(value = "name", required = false,defaultValue = "") String likeName,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                                  @RequestParam(value = "size", required = false, defaultValue = "8") int size) {
        // Đảm bảo currentPage không nhỏ hơn 1
        if (currentPage < 1) {
            currentPage = 1;
        }

        Page<Product> productPage = productService.getProducts(likeName, currentPage, size);

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

    @GetMapping(value = { "/shoppingCart" })
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = CartsUtils.getCartInSession(request);
        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    @PostMapping(value = { "/shoppingCart" })
    public String shoppingCartHandler(HttpServletRequest request, Model model, @ModelAttribute("cartForm") CartInfo cartInfo) {
        CartInfo myCart = CartsUtils.getCartInSession(request);
        cartInfo.updateQuantity(myCart);
        model.addAttribute("cartForm", myCart);
        return "redirect:/shoppingCart";
    }

}
