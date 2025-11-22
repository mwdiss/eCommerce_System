package com.ecommerce.orders;

import com.ecommerce.Product;
import java.util.Map;
import java.util.UUID;

/**
* represents a placed order, a reciept.
* @author Malith Dissanayake
*/
public class Order {
    private final String orderID;
    private final String customerID;
    private final String customerName;
    private final Map<Product, Integer> products;
    private final double orderTotal;

    /** constructs an order, calculating total upon creation. */
    public Order(String customerID, String customerName, Map<Product, Integer> products) {
        this.orderID = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
        this.customerID = customerID;
        this.customerName = customerName;
        this.products = products;
        //calc total once
        this.orderTotal = products.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }

    /** generates a formatted string summary of the order details. */
    public String generateOrderSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("-- Order Summary --\n");
        summary.append(String.format("Order ID: %s\nCustomer: %s (ID: %s)\n", orderID, customerName, customerID));
        summary.append("---------------------\nItems:\n");
        products.forEach((product, quantity) -> summary.append(String.format("  - %s (x%d)\n", product.getName(), quantity)));
        summary.append("---------------------\n");
        summary.append(String.format("Total: $%.2f\n", orderTotal));
        summary.append("---------------------\n");
        return summary.toString();
    }
}
