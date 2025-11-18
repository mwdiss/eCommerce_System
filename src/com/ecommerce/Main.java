package com.ecommerce;
import com.ecommerce.orders.Order;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A simple Swing GUI to demonstrate the functionality of the e-commerce system.
 * Allows browsing products, adding them to a cart, and placing an order.
 *
 * @author Malith Dissanayake
 */
public class Main {

    private static final Customer customer = new Customer("CUST001", "Malith Dissanayake");
    private static final DefaultListModel<Product> productListModel = new DefaultListModel<>();
    private static final JTextArea cartArea = new JTextArea(15, 35);
    private static JList<Product> productJList;

    /**
     * Main method to set up data and launch the GUI.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        addSampleData(); // Load sample products before starting
        SwingUtilities.invokeLater(Main::createAndShowGui);
    }

    /**
     * Sets up and displays the main application window and its components.
     */
    private static void createAndShowGui() {
        JFrame frame = new JFrame("Simple E-commerce System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Panel for listing available products
        productJList = new JList<>(productListModel);
        productJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JButton addToCartButton = new JButton("Add Selected Item to Cart");
        addToCartButton.addActionListener(_ -> addSelectedItemToCart());
        JPanel productPanel = new JPanel(new BorderLayout(5, 5));
        productPanel.setBorder(BorderFactory.createTitledBorder("Available Products"));
        productPanel.add(new JScrollPane(productJList), BorderLayout.CENTER);
        productPanel.add(addToCartButton, BorderLayout.SOUTH);

        // Panel for showing the shopping cart and placing an order
        cartArea.setEditable(false);
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(_ -> placeOrderFromCart());
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart & Order Details"));
        cartPanel.add(new JScrollPane(cartArea), BorderLayout.CENTER);
        cartPanel.add(placeOrderButton, BorderLayout.SOUTH);

        frame.add(productPanel, BorderLayout.WEST);
        frame.add(cartPanel, BorderLayout.CENTER);

        updateCartDisplay();
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    }

    /**
     * Adds the product selected in the JList to the customer's shopping cart.
     */
    private static void addSelectedItemToCart() {
        Product selected = productJList.getSelectedValue();
        if (selected != null) {
            customer.addToCart(selected);
            updateCartDisplay();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a product first.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Creates an order from cart items, displays a summary, and clears the cart.
     */
    private static void placeOrderFromCart() {
        List<Product> cartItems = customer.getShoppingCart();
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your shopping cart is empty.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Order order = new Order(customer, cartItems);
        cartArea.setText("--- Order Placed! ---\n\n" + order.generateSummary());
        customer.clearCart();
    }

    /**
     * Updates the text area to show current items in the cart and the total cost.
     */
    private static void updateCartDisplay() {
        List<Product> cartItems = customer.getShoppingCart();
        double total = cartItems.stream().mapToDouble(Product::getPrice).sum();

        StringBuilder cartText = new StringBuilder("Current Cart Items:\n\n");
        cartItems.forEach(item -> cartText.append("- ").append(item.getName()).append("\n"));
        cartText.append(String.format("\n--------------------------\nTotal: $%.2f", total));
        cartArea.setText(cartText.toString());
    }

    /**
     * Populates the application with sample product data for demonstration purposes.
     * This method can be run without in the future by commenting out the call.
     */
    private static void addSampleData() {
        productListModel.addElement(new Product("P001", "Laptop Pro", 1299.99));
        productListModel.addElement(new Product("P002", "Wireless Mouse", 35.50));
        productListModel.addElement(new Product("P003", "Mechanical Keyboard", 115.00));
        productListModel.addElement(new Product("P004", "4K Monitor", 350.00));
        productListModel.addElement(new Product("P005", "HD Webcam", 60.00));
    }
}