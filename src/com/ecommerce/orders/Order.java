package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * an order with receipt generation.
 * @author Malith Dissanayake
 */
public class Order {
    private final String orderID = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
    private final Customer customer;
    private final Map<Product, Integer> products;
    private double total = 0.0;

    public Order(Customer cust, Map<Product, Integer> prods) { this.customer = cust; this.products = prods; }
    public String getOrderID() { return orderID; }
    public Customer getCustomer() { return customer; }
    public Map<Product, Integer> getProducts() { return products; }
    public double getTotal() { 
        if(total == 0.0) total = products.entrySet().stream().mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
        return total;
    }

    /** generates a professional, itemized receipt string. */
    public String getReceipt() {
        //matches the image: Item, Price, Qty, Total
        String header = String.format("%-20s %7s %3s %7s\n", "Item", "Price", "Qty", "Total");
        String items = products.entrySet().stream()
            .map(e -> String.format("%-20s %7.2f %3d %7.2f", e.getKey().getProdName(), e.getKey().getPrice(), e.getValue(), e.getKey().getPrice() * e.getValue()))
            .collect(Collectors.joining("\n"));

        return String.format(
            "--- Taiga ---\n\n" +
            "RECEIPT FOR: %s\n" +
            "ORDER ID: %s\n" +
            "----------------------------------------\n" +
            header +
            "----------------------------------------\n" +
            items + "\n" +
            "----------------------------------------\n" +
            "%33s%7.2f\n" + //formatted to align correctly
            "----------------------------------------\n\n" +
            "Thank you for your purchase!\n",
            customer.getCustName(), orderID, "TOTAL:", getTotal()
        );
    }
}