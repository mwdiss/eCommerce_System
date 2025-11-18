package com.ecommerce;

import java.util.Objects;

/**
 * Represents a product with its details.
 * @author Malith Dissanayake
 */
public class Product {
    private final String productID;
    private final String name;
    private final String category;
    private final double price;

    public Product(String productID, String name, String category, double price) {
        this.productID = productID; this.name = name; this.category = category; this.price = price;
    }

    public String getProductID() { return productID; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return productID.equals(((Product) o).productID);
    }
    @Override public int hashCode() { return Objects.hash(productID); }
}