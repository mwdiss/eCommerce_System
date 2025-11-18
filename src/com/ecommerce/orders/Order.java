package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;
import java.util.Map;
import java.util.UUID;

/**
 * an order with detailed receipt generation.
 * @author Malith Dissanayake
 */
public class Order {
    private final String orderID = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
    private final Customer customer;
    private final Map<Product, Integer> products;

    public Order(Customer cust, Map<Product, Integer> prods) { this.customer = cust; this.products = prods; }

    /** generates a professional, itemized receipt string. */
    public String getReceipt() {
        StringBuilder sb = new StringBuilder();
        double subTotal = 0.0, totalDiscount = 0.0;
        sb.append("--- TAIGA ---\n").append("RECEIPT FOR: ").append(customer.getCustName()).append("\nORDER ID: ").append(orderID).append("\n--------------------------------------------------\n").append(String.format("%-25s %s %8s %8s\n", "Item", "Qty", "Price", "Total")).append("--------------------------------------------------\n");
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product p = entry.getKey(); int qty = entry.getValue(); double itemTotal = p.getDiscountedPrice() * qty;
            subTotal += p.getPrice() * qty; totalDiscount += (p.getPrice() - p.getDiscountedPrice()) * qty;
            sb.append(String.format("%-25s %d %8.2f %8.2f\n", p.getProdName(), qty, p.getDiscountedPrice(), itemTotal));
            if (p.getDiscount() > 0) sb.append(String.format("  (Discount: %.0f%%)\n", p.getDiscount() * 100));
        }
        sb.append("--------------------------------------------------\n").append(String.format("%38s %8.2f\n", "Subtotal:", subTotal));
        if (totalDiscount > 0) sb.append(String.format("%38s %8.2f\n", "You Saved:", -totalDiscount));
        sb.append(String.format("%38s %8.2f\n", "GRAND TOTAL:", subTotal - totalDiscount)).append("--------------------------------------------------\n\n").append("Thank you for shopping at Taiga!\n");
        return sb.toString();
    }
}