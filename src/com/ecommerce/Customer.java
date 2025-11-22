package com.ecommerce;
import com.ecommerce.orders.Order;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**Customer and cart
 * @author Malith Dissanayake */
public class Customer {
	private final String customerID;
    private String custName;
    private final Map<Product, Integer> cart = new LinkedHashMap<>();

    /** @param customerID Unique ID @param name Initial name for the customer. */
    public Customer(String name) { this.custName = name; this.customerID = UUID.randomUUID().toString().toUpperCase().substring(0, 8); }
    
    public String getCustomerID() { return customerID; }
    public String getCustName() { return custName; }
    public void setCustName(String name) { this.custName = name; }
    public Map<Product, Integer> getCart() { return new LinkedHashMap<>(cart); }
    public void addToCart(Product p) { cart.merge(p, 1, Integer::sum); }
    public void removeFromCart(Product p) { cart.remove(p); }
    public void clearCart() { cart.clear(); }
    
    /** total cost in cart. */
    public double calculateTotal() { return cart.entrySet().stream() .mapToDouble(e -> e.getKey().price() * e.getValue()) .sum();}
    /** Order from the cart */
    public Order placeOrder() { Order order = new Order(this, getCart()); clearCart(); return order; }
}