package com.ecommerce;

import java.util.LinkedHashMap;
import java.util.Map;

/**Customer and cart
 * @author Malith Dissanayake */
public class Customer {
    private String custName;
    private final Map<Product, Integer> cart = new LinkedHashMap<>();

    /** @param name Initial name for the customer. */
    public Customer(String name) { this.custName = name; }
    
    public String getCustName() { return custName; }
    public void setCustName(String name) { this.custName = name; }
    public Map<Product, Integer> getCart() { return new LinkedHashMap<>(cart); }
    public void addToCart(Product p) { cart.merge(p, 1, Integer::sum); }
    public void removeFromCart(Product p) { cart.remove(p); }
    public void clearCart() { cart.clear(); }
}