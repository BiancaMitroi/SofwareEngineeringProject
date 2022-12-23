package com.example.app.controller;

import com.example.app.models.Order;
import com.example.app.models.Product;
import com.example.app.models.ShoppingCart;
import com.example.app.models.User;
import com.example.app.models.dto.OrderRequest;
import com.example.app.repo.OrderRepo;
import com.example.app.repo.ProductRepo;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {

    private ShoppingCart shoppingCart = ShoppingCart.getInstance();
    private final OrderRepo orderRepo;

    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    @Autowired
    public OrderController(OrderRepo orderRepo, UserRepo userRepo, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @GetMapping(value = "/")
    public String getPage() {
        return "Welcome!";
    }

    @GetMapping(value = "/all")
    public List<Order> getOrders() {
        return orderRepo.findAll();
    }

    @PostMapping(value = "/save")
    public void saveOrders(@RequestBody OrderRequest orderRequest) {
        Order order = new Order();
        Optional<User> user = userRepo.findByUsername(orderRequest.getUsername());
        if (user.isEmpty()) {
            return;
        }
        if (shoppingCart.getCart().isEmpty() || !shoppingCart.getCart().containsKey(user.get().getId()) || shoppingCart.getCart().get(user.get().getId()) == null) {
            return;
        }
        HashMap<Long, Integer> cart = shoppingCart.getCart().get(user.get().getId());
        if (cart == null) {
            return;
        }
        List<Product> products = new ArrayList<>();
        Float totalPrice = Float.valueOf(0);
        for (Long productId : cart.keySet()) {
            Optional<Product> productOptional = productRepo.findById(productId);
            if(productOptional.isPresent()) {
                products.add(productOptional.get());
                totalPrice += productOptional.get().getPrice();
            }
        }
        order.setUser(user.get());
        order.setName(orderRequest.getName());
        order.setSurname(orderRequest.getSurname());
        order.setAddress(orderRequest.getAddress());
        order.setPostalCode(orderRequest.getPostalCode());
        order.setProducts(products);
        order.setTotalPrice(totalPrice);

        orderRepo.save(order);

        shoppingCart.emptyCart(user.get());
    }

    @PutMapping(value = "/update/{id}")
    public void updateOrders(@PathVariable long id, @RequestBody Order order) {
        Order updateOrder = orderRepo.findById(id).get();
        updateOrder.setTotalPrice(order.getTotalPrice());
        updateOrder.setUser(order.getUser());
        orderRepo.save(updateOrder);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteOrders(@PathVariable long id) {
        Order deleteOrder = orderRepo.findById(id).get();
        orderRepo.delete(deleteOrder);
    }
}
