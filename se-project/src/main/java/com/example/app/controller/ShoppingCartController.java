package com.example.app.controller;

import com.example.app.models.Product;
import com.example.app.models.ShoppingCart;
import com.example.app.models.User;
import com.example.app.models.dto.CartProductDto;
import com.example.app.models.dto.ProductToCartDTO;
import com.example.app.repo.ProductRepo;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class ShoppingCartController {
    private final ShoppingCart cart = ShoppingCart.getInstance();

    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    @Autowired
    public ShoppingCartController(UserRepo userRepo, ProductRepo productRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @GetMapping(value = "/")
    public String getPage() {
        return "Welcome!";
    }

    @PostMapping("/add")
    public void addProduct(@RequestBody ProductToCartDTO productToCartDTO) {
        Optional<User> user = userRepo.findByUsername(productToCartDTO.getUsername());
        Optional<Product> product = productRepo.findById(productToCartDTO.getProductId());
        if (user.isEmpty() || product.isEmpty()) {
            return;
        }
        User resultUser = user.get();
        Product resultProduct = product.get();
        cart.addProductToCart(resultUser, resultProduct);
    }

    @PostMapping("/remove")
    public void removeProduct(@RequestBody ProductToCartDTO productToCartDTO) {
        Optional<User> user = userRepo.findByUsername(productToCartDTO.getUsername());
        Optional<Product> product = productRepo.findById(productToCartDTO.getProductId());
        if (user.isEmpty() || product.isEmpty()) {
            return;
        }
        User resultUser = user.get();
        Product resultProduct = product.get();
        cart.removeProductFromCart(resultUser, resultProduct);
    }

    @GetMapping("/{username}")
    public List<CartProductDto> getCartForUser(@PathVariable String username) {
        Optional<User> userId = userRepo.findByUsername(username);
        if (userId.isEmpty()) {
            return new ArrayList<>();
        }
        HashMap<Long, Integer> cart = this.cart.getCart().get(userId.get().getId());
        if (cart == null) {
            return new ArrayList<>();
        }
        List<CartProductDto> result = new ArrayList<>();
        for (Long productId : cart.keySet()) {
            Optional<Product> productOptional = productRepo.findById(productId);
            productOptional.ifPresent(product -> {
                CartProductDto cartProductDto = new CartProductDto();
                cartProductDto.setId(product.getId());
                cartProductDto.setDescription(product.getDescription());
                cartProductDto.setImage(product.getImage());
                cartProductDto.setName(product.getName());
                cartProductDto.setPrice(product.getPrice());
                cartProductDto.setStock(product.getStock());
                cartProductDto.setQuantity(cart.get(productId));
                result.add(cartProductDto);
            });
        }
        return result;
    }
}
