package com.shopkart.repository;

import com.shopkart.model.Customer;

/** Thin specialization of Repository<T> for Customer — see Session 8 notes in Repository.java. */
public class CustomerRepository extends Repository<Customer> {
    public CustomerRepository() {
        super(Customer::getId);
    }
}
