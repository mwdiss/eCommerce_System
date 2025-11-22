package com.ecommerce;

import com.ecommerce.orders.Order;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
* represents a customer and manages their shopping cart.
* @author Malith Dissanayake
*/
public class Customer {
    private final String customerID;
    private String name;
    private final Map<Product, Integer> shoppingCart = new LinkedHashMap<>(); //map for quantity

    /** constructs a new customer with validation. */
    public Customer(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("customer name cannot be null or blank.");
        this.customerID = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
        this.name = name;
    }

    //cart management
    public void addToCart(Product product) {
        if (product == null) throw new IllegalArgumentException("cannot add null product to cart.");
        this.shoppingCart.merge(product, 1, Integer::sum); //add or increment qty
    }

    public void removeFromCart(Product product) { this.shoppingCart.remove(product); }

    /** calculates total cost of all items in cart. */
    public double calculateTotal() {
        return shoppingCart.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }

    /**
    * creates an order from cart and clears cart.
    * @throws IllegalStateException if cart is empty.
    */
    public Order placeOrder() {
        if (shoppingCart.isEmpty()) throw new IllegalStateException("cannot place an order with an empty cart.");
        Order order = new Order(this.customerID, this.name, new LinkedHashMap<>(this.shoppingCart)); //snapshot
        this.shoppingCart.clear();
        return order;
    }

    //getters
    public String getCustomerID() { return customerID; }
    public String getName() { return name; }
    public Map<Product, Integer> getShoppingCart() { return this.shoppingCart; }
}