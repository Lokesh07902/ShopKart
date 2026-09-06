package com.shopkart.web;

import com.shopkart.model.Product;
import com.shopkart.repository.CustomerRepository;
import com.shopkart.repository.ProductRepository;
import com.shopkart.util.CatalogLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * SESSION 30 — Wires up the SAME ProductRepository (Session 8-11) used by
 * the console app as Spring-managed @Bean singletons, so the REST
 * controllers below reuse all the DSA work instead of duplicating it.
 */
@Configuration
public class AppConfig {

    @Bean
    public ProductRepository productRepository() {
        ProductRepository repo = new ProductRepository();
        List<Product> seed = CatalogLoader.loadFromCsv("data/products.csv");
        seed.forEach(repo::add);
        return repo;
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new CustomerRepository();
    }
}
