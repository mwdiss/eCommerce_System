package com.ecommerce;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a customer with a cart that tracks item quantities.
 * @author Malith Dissanayake
 */
public class Customer {
    private String name;
    private final Map<Product, Integer> cart = new LinkedHashMap<>();

    public Customer(String name) { this.name = name; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Map<Product, Integer> getCart() { return cart; }
    public void addToCart(Product p) { cart.put(p, cart.getOrDefault(p, 0) + 1); }
    public void removeFromCart(Product p) { cart.remove(p); }
    public void clearCart() { cart.clear(); }
}