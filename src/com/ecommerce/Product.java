package com.ecommerce;

/**
 * A simple class for a product. Holds its ID, name, and price.
 * @author Malith Dissanayake
 */
public class Product {
    private final String productID;
    private final String name;
    private final double price;

    /**
     * Creates a new product.
     * @param productID The product's unique ID.
     * @param name The product's name.
     * @param price The product's price.
     */
    public Product(String productID, String name, double price) {
        this.productID = productID;
        this.name = name;
        this.price = price;
    }

    // Standard getter methods.
    public String getProductID() { return productID; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    /**
     * Defines how the product is displayed in UI lists.
     * @return A clean, formatted string like "Laptop Pro - $1299.99".
     */
    @Override
    public String toString() {
        return String.format("%s - $%.2f", name, price);
    }
}