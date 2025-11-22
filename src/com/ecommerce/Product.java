package com.ecommerce;

/**Product with validation.
 * @param productID Unique ID, not blank.
 * @param name  Display name, not blank.
 * @param category  Product category.
 * @param price     Price, must be non-negative.
 * @author Malith Dissanayake
 */
public record Product(String productID, String name, String category, double price) {
    /** Validates product data upon creation. */
    public Product {
        if (productID == null || productID.isBlank()) throw new IllegalArgumentException("Product ID cannot be empty.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name cannot be empty.");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        category = (category == null || category.isBlank()) ? "General" : category;
    }
}