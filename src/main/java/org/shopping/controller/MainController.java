package org.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.shopping.model.ProductInfo;
import org.shopping.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ProductService productService;

    @GetMapping("/productList")
    public String listProductHandler(Model model,
                                     @RequestParam(value = "name", required = false, defaultValue = "") String likeName,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                                     @RequestParam(value = "size", required = false, defaultValue = "8") int size){
        ProductInfo productInfo = new ProductInfo();

        return "";
    }
}
