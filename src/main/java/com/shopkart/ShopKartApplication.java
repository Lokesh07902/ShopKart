package com.shopkart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SESSION 30 — Spring Boot Basics: Intro to Spring Boot & REST APIs
 * This is a SEPARATE entry point from Main.java. Main.java is the plain
 * console demo (no framework, runs with just `javac`/`java`) that shows
 * every data structure working in isolation. ShopKartApplication boots the
 * same logic behind a real HTTP REST API using Spring Boot.
 *
 * Run with: mvn spring-boot:run
 * Then try: GET http://localhost:8080/api/products
 */
@SpringBootApplication
public class ShopKartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopKartApplication.class, args);
    }
}
