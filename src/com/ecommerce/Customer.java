package com.ecommerce;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer, holding their ID, name, and shopping cart.
 * @author Malith Dissanayake
 */
public class Customer {
    private final String customerID;
    private final String name;
    private final List<Product> shoppingCart;

    /**
     * Creates a new customer with an empty cart.
     * @param customerID The customer's unique ID.
     * @param name The customer's name.
     */
    public Customer(String customerID, String name) {
        this.customerID = customerID;
        this.name = name;
        this.shoppingCart = new ArrayList<>();
    }

    // Getters for customer details.
    public String getCustomerID() { return customerID; }
    public String getName() { return name; }
    
    /** Adds a product to the cart. */
    public void addToCart(Product product) { shoppingCart.add(product); }

    /** Removes a specific product from the cart. */
    public void removeFromCart(Product product) { shoppingCart.remove(product); }
    
    /** Returns a safe copy of the cart's contents. */
    public List<Product> getShoppingCart() { return new ArrayList<>(shoppingCart); }

    /** Empties the cart, usually after an order is placed. */
    public void clearCart() { shoppingCart.clear(); }
}