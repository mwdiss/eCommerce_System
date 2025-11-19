package com.ecommerce;

/**An immutable product with its details.
 * @param productID Unique product identifier.
 * @param prodName  Display name of the product.
 * @param category  Product category.
 * @param price     Product price.
 * @author Malith Dissanayake */
public record Product(String productID, String prodName, String category, double price) {}