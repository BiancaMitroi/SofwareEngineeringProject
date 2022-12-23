package com.example.app.models;

import com.example.app.models.dto.CartProductDto;
import com.example.app.repo.ProductRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ShoppingCart {
    private Float totalPrice;

    private HashMap<Long, HashMap<Long, Integer>> cart;

    public static ShoppingCart shoppingCart = null;

    private ShoppingCart() {
        this.totalPrice = Float.valueOf(0L);
        this.cart = new HashMap<>();
    }

    public static ShoppingCart getInstance(){
        if(shoppingCart == null){
            shoppingCart = new ShoppingCart();
        }
        return shoppingCart;
    }

    public void addProductToCart(User user, Product product) {
        if (!cart.containsKey(user.getId())) {
            HashMap<Long, Integer> products = new HashMap<>();
            cart.put(user.getId(), products);
        }
        if (cart.get(user.getId()).containsKey(product.getId())) {
            cart.get(user.getId()).replace(product.getId(), cart.get(user.getId()).get(product.getId()) + 1);
        } else {
            cart.get(user.getId()).put(product.getId(), 1);
        }
        totalPrice += product.getPrice();
    }

    public void removeProductFromCart(User user, Product product) {
        if (cart.containsKey(user.getId())) {
            if (cart.get(user.getId()).containsKey(product.getId())) {
                if (cart.get(user.getId()).get(product.getId()) > 1) {
                    cart.get(user.getId()).replace(product.getId(), cart.get(user.getId()).get(product.getId()) - 1);
                } else {
                    cart.get(user.getId()).remove(product.getId());
                }
                totalPrice -= product.getPrice();
            }
            if (cart.get(user.getId()).size() == 0) {
                cart.remove(user.getId());
            }
        }
    }

    public void emptyCart(User user) {
        if (cart.containsKey(user.getId())) {
            cart.get(user.getId()).clear();
            cart.remove(user.getId());
        }
    }

    public HashMap<Long, HashMap<Long, Integer>> getCart() {
        return cart;
    }
}
