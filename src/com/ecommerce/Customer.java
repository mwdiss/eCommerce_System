package com.ecommerce;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * a customer and their shopping cart.
 * @author Malith Dissanayake
 */
public class Customer {
    private String custName;
    private final Map<Product, Integer> cart = new LinkedHashMap<>();

    /**
     * creates a new customer.
     * @param name the initial name for the customer.
     */
    public Customer(String name) { this.custName = name; }
    public String getCustName() { return custName; }
    public void setCustName(String name) { this.custName = name; }
    public Map<Product, Integer> getCart() { return cart; }
    public void addToCart(Product p) { cart.put(p, cart.getOrDefault(p, 0) + 1); }
    public void removeFromCart(Product p) { cart.remove(p); }
    public void clearCart() { cart.clear(); }
}