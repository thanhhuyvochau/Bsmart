package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Cart;
import fpt.project.bsmart.entity.CartItem;

import java.math.BigDecimal;

public class CartUtil {

    public static BigDecimal calculateTotalPrice(Cart cart) {
        return cart.getCartItems().stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
