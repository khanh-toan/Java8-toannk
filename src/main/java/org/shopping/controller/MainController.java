package org.shopping.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.shopping.dao.*;
import org.shopping.entity.Product;
import org.shopping.form.*;
import org.shopping.model.*;
import org.shopping.pagination.PaginationResult;
import org.shopping.utils.Utils;
import org.shopping.validator.CustomerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@Transactional
public class MainController {
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CustomerFormValidator customerFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);
        // Trường hợp update SL trên giỏ hàng.
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class) {
        }
        // Trường hợp save thông tin khách hàng.
        // (@ModelAttribute @Validated CustomerInfo customerForm)
        else if (target.getClass() == CustomerForm.class) {
            dataBinder.setValidator(customerFormValidator);
        }
    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    // Danh sách sản phẩm.
    @RequestMapping({"/productList"})
    public String listProductHandler(Model model,
                                     @RequestParam(value = "name", defaultValue = "") String likeName,
                                     @RequestParam(value = "page", defaultValue = "1") int page) {
        // Hiện thì tối đá 5 sản phẩm trên 1 trang
        final int maxResult = 5;
        // Hiện thị số trang trên thanh điều hướng
        final int maxNavigationPage = 10;
        PaginationResult<ProductInfo> result = productDAO.queryProducts(page,
                maxResult, maxNavigationPage, likeName);
        model.addAttribute("paginationProducts", result);
        return "productList";
    }

    @RequestMapping({"/buyProduct"})
    public String listProductHandler(HttpServletRequest request, Model model,
                                     @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {
            CartInfo cartInfo = Utils.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.addProduct(productInfo, 1);
        }
        return "redirect:/shoppingCart";
    }

    @RequestMapping({"/shoppingCartRemoveProduct"})
    public String removeProductHandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {
            CartInfo cartInfo = Utils.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.removeProduct(productInfo);

        }
        return "redirect:/shoppingCart";
    }

    // POST: Cập nhập số lượng cho các sản phẩm đã mua.
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                                        Model model, //
                                        @ModelAttribute("cartForm") CartInfo cartForm) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);
        return "redirect:/shoppingCart";
    }

    // GET: Hiển thị giỏ hàng.
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = Utils.getCartInSession(request);
        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    // GET: Nhập thông tin khách hàng.
    @RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        }
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        CustomerForm customerForm = new CustomerForm(customerInfo);
        model.addAttribute("customerForm", customerForm);
        return "shoppingCartCustomer";
    }

    // POST: Save thông tin khách hàng.
    @RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(HttpServletRequest request,
                                           Model model,
                                           @ModelAttribute("customerForm") @Validated CustomerForm customerForm,
                                           BindingResult result,
                                           final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            customerForm.setValid(false);
// Forward tới trang nhập lại.
            return "shoppingCartCustomer";
        }
        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);
        return "redirect:/shoppingCartConfirmation";
    }

    // GET: Xem lại thông tin để xác nhận.
    @RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo == null || cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";
        }
        model.addAttribute("myCart", cartInfo);
        return "shoppingCartConfirmation";
    }

    // POST: Gửi đơn hàng (Save).
    @RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.POST)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";

        }
        try {
            orderDAO.saveOrder(cartInfo);
        } catch (Exception e) {
            return "shoppingCartConfirmation";
        }
        // Xóa giỏ hàng khỏi session.
        Utils.removeCartInSession(request);
        // Lưu thông tin đơn hàng cuối đã xác nhận mua.
        Utils.storeLastOrderedCartInSession(request, cartInfo);
        return "redirect:/shoppingCartFinalize";
    }

    @RequestMapping(value = {"/shoppingCartFinalize"}, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {
        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }

    @RequestMapping(value = {"/productImage"}, method = RequestMethod.GET)
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException {
        Product product = null;
        if (code != null) {
            product = this.productDAO.findProduct(code);
        }
        if (product != null && product.getImage() != null) {
            String contentType = getImageContentType(product.getImage());
            if(contentType != null){
                response.setContentType(contentType); // Đặt loại MIME chính xác cho ảnh
                response.getOutputStream().write(product.getImage());
                response.getOutputStream().close();
            }else {
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported image type");
            }
        }else{
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product image not found");
        }
    }

    // Phương thức để xác định loại ảnh từ dữ liệu ảnh
    private String getImageContentType(byte[] imageData) {
        // Sử dụng một cách kiểm tra đơn giản dựa trên byte array
        if (imageData.length > 4) {
            // Kiểm tra các magic numbers cơ bản của file ảnh
            if ((imageData[0] & 0xFF) == 0xFF && (imageData[1] & 0xFF) == 0xD8) {
                return "image/jpeg"; // JPEG
            } else if ((imageData[0] & 0xFF) == 0x89 && new String(imageData, 1, 3).equals("PNG")) {
                return "image/png"; // PNG
            } else if (new String(imageData, 0, 3).equals("GIF")) {
                return "image/gif"; // GIF
            }
        }
        return null;
    }
}
