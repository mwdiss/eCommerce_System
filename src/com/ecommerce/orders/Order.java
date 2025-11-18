package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a completed order. It captures a snapshot of the customer's cart.
 * @author Malith Dissanayake
 */
public class Order {
    private final String orderID;
    private final Customer customer;
    private final List<Product> products;
    private final double orderTotal;

    /**
     * Creates an order from a customer and a list of products.
     * @param customer The customer placing the order.
     * @param products The products they are buying.
     */
    public Order(Customer customer, List<Product> products) {
        this.orderID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.customer = customer;
        this.products = products;
        this.orderTotal = products.stream().mapToDouble(Product::getPrice).sum();
    }

    /**
     * Creates a neat, multi-line summary of the order.
     * @return A formatted string with all order details.
     */
    public String generateSummary() {
        String productDetails = products.stream().map(p -> "- " + p.toString()).collect(Collectors.joining("\n"));
        return String.format("Order ID: %s\nCustomer: %s\n\nProducts:\n%s\n\nTotal: $%.2f",
                orderID, customer.getName(), productDetails, orderTotal);
    }
}