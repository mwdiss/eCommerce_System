package com.ecommerce;

import java.util.Objects;

/**
 * a product with its details.
 * @author Malith Dissanayake
 */
public class Product {
    private final String productID, prodName, category;
    private final double price;

    /**
     * creates a new product.
     * @param id the unique product code.
     * @param name the product's display name.
     * @param cat the product category.
     * @param price the product's price.
     */
    public Product(String id, String name, String cat, double price) {
        this.productID = id; this.prodName = name; this.category = cat; this.price = price;
    }

    public String getProductID() { return productID; }
    public String getProdName() { return prodName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }

    @Override public boolean equals(Object o) { return o instanceof Product p && productID.equals(p.productID); }
    @Override public int hashCode() { return Objects.hash(productID); }
}