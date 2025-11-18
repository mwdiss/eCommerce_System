package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * An order, created from the customer's cart.
 * @author Malith Dissanayake
 */
public class Order {
    private final String orderID;
    private final Customer customer;
    private final Map<Product, Integer> products;
    private final double total;

    public Order(Customer customer, Map<Product, Integer> products) {
        this.orderID = UUID.randomUUID().toString().substring(0, 8);
        this.customer = customer;
        this.products = products;
        this.total = products.entrySet().stream().mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
    }

    /** Generates a multi-line order summary. */
    public String getSummary() {
        String details = products.entrySet().stream()
            .map(e -> String.format("- %s (Qty: %d)", e.getKey().getName(), e.getValue()))
            .collect(Collectors.joining("\n"));
        return String.format("Order ID: %s\nCustomer: %s\n\nItems:\n%s\n\nTotal: $%.2f", orderID, customer.getName(), details, total);
    }
}