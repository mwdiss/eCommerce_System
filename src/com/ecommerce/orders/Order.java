package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A customer order with details and receipt generation.
 * @author Malith Dissanayake
 */
public class Order {
    private final String orderID = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
    private final Customer customer;
    private final Map<Product, Integer> products;
    private final double total;

    /** @param cust Customer placing the order. @param prods Products in the order. */
    public Order(Customer cust, Map<Product, Integer> prods) {
        this.customer = cust; this.products = prods;
        this.total = prods.entrySet().stream().mapToDouble(e -> e.getKey().price() * e.getValue()).sum();
    }
    
    public String getOrderID() { return orderID; }
    public Customer getCustomer() { return customer; }
    public Map<Product, Integer> getProducts() { return products; }
    public double getTotal() { return total; }

    /** Generates a formatted receipt string for the order. */
    public String getReceipt() {
        String items = products.entrySet().stream()
            .map(e -> String.format("%-20s %7.2f %3d %7.2f", e.getKey().prodName(), e.getKey().price(), e.getValue(), e.getKey().price() * e.getValue()))
            .collect(Collectors.joining("\n"));
        return """
            --- Taiga ---
            RECEIPT FOR: %s
            ORDER ID: %s
            ----------------------------------------
            %-20s %7s %3s %7s
            ----------------------------------------
            %s
            ----------------------------------------
            %33s%7.2f
            ----------------------------------------
            Thank you for your purchase!
            """.formatted(customer.getCustName(), orderID, "Item", "Price", "Qty", "Total", items, "TOTAL:", getTotal());
    }
}