package org.shopping.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.shopping.model.CartInfo;

public class CartsUtils {
    public static CartInfo getCartInSession(HttpServletRequest request) {
        CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");

        if (cartInfo == null) {
            cartInfo = new CartInfo();
            request.getSession().setAttribute("myCart", cartInfo);
        }
        return cartInfo;
    }

    public static void removeCartInSession(HttpServletRequest request) {
        request.getSession().removeAttribute("myCart");
    }

    public static void storeLastOrderedCartInSession(HttpServletRequest request, CartInfo cartInfo) {
        request.getSession().setAttribute("lastOrderedCart", cartInfo);
        System.out.println(cartInfo);
    }

    public static CartInfo getLastOrderedCartInSession(HttpServletRequest request) {
        return (CartInfo) request.getSession().getAttribute("lastOrderedCart");
    }
}
