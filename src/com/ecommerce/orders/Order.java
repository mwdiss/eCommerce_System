package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a customer's order, containing order details like ID, products, and total cost.
 * The order is generated from the customer's shopping cart.
 *
 * @author Malith Dissanayake
 */
public class Order {
    private final String orderID;
    private final Customer customer;
    private final List<Product> products;
    private final double orderTotal;

    /**
     * Constructs a new Order from the customer and their list of products.
     * A unique order ID is automatically generated.
     *
     * @param customer The customer placing the order.
     * @param products The list of products included in the order.
     */
    public Order(Customer customer, List<Product> products) {
        this.orderID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.customer = customer;
        this.products = products;
        this.orderTotal = products.stream().mapToDouble(Product::getPrice).sum();
    }

    /**
     * Generates a concise summary of the order details.
     *
     * @return A formatted string containing the order ID, customer name, products, and total.
     */
    public String generateSummary() {
        String productDetails = products.stream()
                .map(p -> "- " + p.toString())
                .collect(Collectors.joining("\n"));

        return String.format("Order ID: %s\nCustomer: %s\n\nProducts:\n%s\n\nTotal: $%.2f",
                orderID, customer.getCustomerID(), productDetails, orderTotal);
    }
}