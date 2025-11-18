package com.ecommerce;

import java.util.Objects;

/**
 * a product with its details and discount.
 * @author Malith Dissanayake
 */
public class Product {
    private final String productID, prodName, category;
    private final double price, discount;

    /**
     * creates a new product.
     * @param id the unique product code.
     * @param name the product's display name.
     * @param cat the product category.
     * @param price the original price.
     * @param discount the discount percentage (e.g., 0.1 for 10%).
     */
    public Product(String id, String name, String cat, double price, double discount) {
        this.productID = id; this.prodName = name; this.category = cat; this.price = price; this.discount = discount;
    }

    public String getProductID() { return productID; }
    public String getProdName() { return prodName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getDiscount() { return discount; }
    public double getDiscountedPrice() { return price * (1 - discount); }

    @Override public boolean equals(Object o) { return o instanceof Product p && productID.equals(p.productID); }
    @Override public int hashCode() { return Objects.hash(productID); }
}