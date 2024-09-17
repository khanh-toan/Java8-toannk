package org.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.shopping.entity.Product;
import org.shopping.model.ProductInfo;
import org.shopping.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ProductService productService;

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

        model.addAttribute("paginationPages", productPage);
        model.addAttribute("searchName", likeName);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", productPage.getTotalPages()); // Thêm tổng số trang
        model.addAttribute("totalItems", productPage.getTotalElements()); // Thêm tổng số sản phẩm
        return "productList";
    }

}
