package com.ecommerce;

/**
 * Represents a product with an ID, name, and price in the e-commerce system.
 * This class provides a simple structure for product data.
 *
 * @author Malith Dissanayake
 */
public class Product {
    private final String productID;
    private final String name;
    private final double price;

    /**
     * Constructs a new Product with specified details.
     *
     * @param productID The unique identifier for the product.
     * @param name The name of the product.
     * @param price The price of the product.
     */
    public Product(String productID, String name, double price) {
        this.productID = productID;
        this.name = name;
        this.price = price;
    }

    /**
     * Gets the product's unique ID.
     *
     * @return The product ID string.
     */
    public String getProductID() {
        return productID;
    }

    /**
     * Gets the product's name.
     *
     * @return The product name string.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the product's price.
     *
     * @return The product price as a double.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns a string representation of the product for display purposes.
     *
     * @return A formatted string with the product's name and price.
     */
    @Override
    public String toString() {
        return String.format("%s - $%.2f", name, price);
    }
}