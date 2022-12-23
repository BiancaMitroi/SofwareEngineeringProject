package com.example.app.repo;


import com.example.app.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepo extends JpaRepository<Products, Long>{

}
