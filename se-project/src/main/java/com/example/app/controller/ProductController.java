package com.example.app.controller;

import com.example.app.models.Product;
import com.example.app.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    private final ProductRepo productRepo;

    @Autowired
    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @RequestMapping(value = "/" )
    public String getPage() {
        return "Welcome";
    }

    @GetMapping(value = "/all")
    public List<Product> getProducts(){
        return productRepo.findAll();
    }

    @PostMapping(value = "/save")
    public String saveProducts(@RequestBody Product product){

        productRepo.save(product);
        return "Saved..";
    }

    @PutMapping(value = "update/{id}")
    public String updateProduct (@PathVariable long id, @RequestBody Product product){
        Product updatedProduct = productRepo.findById(id).get();
        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setStock(product.getStock());
        updatedProduct.setDescription(product.getDescription());
        productRepo.save(updatedProduct);
        return "Updated..";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable long id){
        Product deleteProduct = productRepo.findById(id).get();
        productRepo.delete(deleteProduct);
        return "Delete user with the id: " +id;

    }
}
