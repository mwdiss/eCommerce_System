package com.ecommerce;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer with an ID, name, and a personal shopping cart.
 * This class manages products added to the cart.
 *
 * @author Malith Dissanayake
 */
public class Customer {
    private final String customerID;
    private final List<Product> shoppingCart;

    /**
     * Constructs a new Customer with a specified ID and name.
     * Initializes an empty shopping cart for the customer.
     *
     * @param customerID The unique identifier for the customer.
     * @param name The name of the customer.
     */
    public Customer(String customerID, String name) {
        this.customerID = customerID;
        this.shoppingCart = new ArrayList<>();
    }
    
    /**
     * Gets the customer's unique ID.
     *
     * @return The customer ID string.
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * Adds a specified product to the customer's shopping cart.
     *
     * @param product The Product to be added to the cart.
     */
    public void addToCart(Product product) {
        shoppingCart.add(product);
    }

    /**
     * Retrieves the contents of the shopping cart.
     * Returns a copy to protect the internal list from external modifications.
     *
     * @return A new List containing the products in the cart.
     */
    public List<Product> getShoppingCart() {
        return new ArrayList<>(shoppingCart);
    }

    /**
     * Removes all items from the shopping cart, typically after an order is placed.
     */
    public void clearCart() {
        shoppingCart.clear();
    }
}