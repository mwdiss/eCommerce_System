package com.ecommerce;

/**
* product model for the store.
* includes validation for data integrity.
* @author Malith Dissanayake
*/
public class Product {
    private final String productID;
    private String name;
    private double price;

    /**
    * constructs a new product, validates inputs.
    * @param productID unique id, not null/blank.
    * @param name display name, not null/blank.
    * @param price cost of the product, must be non-negative.
    */
    public Product(String productID, String name, double price) {
        //validate inputs
        if (productID == null || productID.isBlank()) throw new IllegalArgumentException("product id cannot be empty.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("product name cannot be empty.");
        if (price < 0) throw new IllegalArgumentException("price cannot be negative.");

        this.productID = productID;
        this.name = name;
        this.price = price;
    }

    //getters
    public String getProductID() { return productID; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    //setters with validation
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("product name cannot be empty.");
        this.name = name;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("price cannot be negative.");
        this.price = price;
    }

    /** provides simple string representation of the product. */
    @Override
    public String toString() {
        return String.format("%s (id: %s, price: $%.2f)", this.name, this.productID, this.price);
    }
}