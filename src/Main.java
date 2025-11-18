import com.ecommerce.Customer;
import com.ecommerce.Product;
import com.ecommerce.orders.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * A simple Swing GUI for our E-commerce system.
 * It lets you browse products, add/remove from cart, and place an order.
 * @author Malith Dissanayake
 */
public class Main {

    // --- App-wide data and UI components ---
    private static final Customer customer = new Customer("CUST001", "Malith Dissanayake");
    private static final DefaultListModel<Product> productListModel = new DefaultListModel<>();
    private static final DefaultListModel<Product> cartListModel = new DefaultListModel<>();
    private static JList<Product> productJList;
    private static JList<Product> cartJList;
    private static JLabel totalLabel;

    // --- A simple color scheme for a better look ---
    private static final Color BG_COLOR = new Color(240, 245, 250); // Light blue-gray
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(70, 130, 180); // Steel Blue

    /** Main entry point: sets up data and launches the GUI. */
    public static void main(String[] args) {
        addSampleData();
        SwingUtilities.invokeLater(Main::createAndShowGui);
    }

    /** Creates the main window and all its contents. */
    private static void createAndShowGui() {
        // --- Frame Setup ---
        JFrame frame = new JFrame("Simple E-commerce Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(BG_COLOR);

        // --- Left Panel: Product List ---
        productJList = new JList<>(productListModel);
        JButton addToCartBtn = createStyledButton("Add to Cart");
        addToCartBtn.addActionListener(_ -> addSelectedItemToCart());
        JPanel productPanel = createTitledPanel("Available Products", new JScrollPane(productJList), addToCartBtn);
        
        // --- Right Panel: Shopping Cart ---
        cartJList = new JList<>(cartListModel);
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton removeBtn = createStyledButton("Remove Selected");
        removeBtn.addActionListener(_ -> removeItemFromCart());
        JButton clearBtn = createStyledButton("Clear Cart");
        clearBtn.addActionListener(_ -> clearCart());

        JPanel cartButtonsPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        cartButtonsPanel.setOpaque(false);
        cartButtonsPanel.add(removeBtn);
        cartButtonsPanel.add(clearBtn);

        JPanel cartBottomPanel = new JPanel(new BorderLayout(5,5));
        cartBottomPanel.setOpaque(false);
        cartBottomPanel.add(cartButtonsPanel, BorderLayout.NORTH);
        cartBottomPanel.add(totalLabel, BorderLayout.CENTER);
        
        JPanel cartPanel = createTitledPanel("Shopping Cart", new JScrollPane(cartJList), cartBottomPanel);
        cartPanel.setPreferredSize(new Dimension(300, 400)); // Makes cart panel thinner
        
        // --- Bottom Panel: Place Order ---
        JButton placeOrderBtn = createStyledButton("Place Order");
        placeOrderBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        placeOrderBtn.addActionListener(_ -> placeOrderFromCart());
        JPanel orderPanel = new JPanel();
        orderPanel.setBackground(BG_COLOR);
        orderPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        orderPanel.add(placeOrderBtn);
        
        // --- Assemble Frame ---
        frame.add(productPanel, BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);
        frame.add(orderPanel, BorderLayout.SOUTH);

        updateCartDisplay();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** Adds selected product to cart. */
    private static void addSelectedItemToCart() {
        Product selected = productJList.getSelectedValue();
        if (selected != null) {
            customer.addToCart(selected);
            updateCartDisplay();
        }
    }

    /** Removes selected item from cart. */
    private static void removeItemFromCart() {
        Product selected = cartJList.getSelectedValue();
        if (selected != null) {
            customer.removeFromCart(selected);
            updateCartDisplay();
        }
    }

    /** Clears all items from the cart. */
    private static void clearCart() {
        if (!customer.getShoppingCart().isEmpty()) {
            customer.clearCart();
            updateCartDisplay();
        }
    }

    /** Creates an order, shows summary in a popup, and clears cart. */
    private static void placeOrderFromCart() {
        List<Product> cartItems = customer.getShoppingCart();
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your shopping cart is empty.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Order order = new Order(customer, cartItems);
        JOptionPane.showMessageDialog(null, order.generateSummary(), "Order Placed Successfully!", JOptionPane.INFORMATION_MESSAGE);
        customer.clearCart();
        updateCartDisplay();
    }

    /** Refreshes the cart list and total price display. */
    private static void updateCartDisplay() {
        List<Product> cartItems = customer.getShoppingCart();
        cartListModel.clear();
        cartItems.forEach(cartListModel::addElement);
        double total = cartItems.stream().mapToDouble(Product::getPrice).sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
    
    // --- Helper methods for UI consistency ---
    
    /** Helper to create a consistently styled JPanel. */
    private static JPanel createTitledPanel(String title, JComponent main, JComponent bottom) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title), new EmptyBorder(5, 5, 5, 5)));
        panel.setBackground(PANEL_COLOR);
        panel.add(main, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    /** Helper to create a consistently styled JButton. */
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        return button;
    }

    /** Populates product list with sample data. Comment out call in main() to disable. */
    private static void addSampleData() {
        productListModel.addElement(new Product("P001", "Laptop Pro", 1299.99));
        productListModel.addElement(new Product("P002", "Wireless Mouse", 35.50));
        productListModel.addElement(new Product("P003", "Mechanical Keyboard", 115.00));
        productListModel.addElement(new Product("P004", "4K Monitor", 350.00));
        productListModel.addElement(new Product("P005", "HD Webcam", 60.00));
    }
}