import com.ecommerce.Customer;
import com.ecommerce.Product;
import com.ecommerce.gui.*;
import com.ecommerce.orders.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**Main application window
 * @author Malith Dissanayake */
public class Main {
    private static final Customer customer = new Customer("Guest");
    private static final CartTableModel cartTableModel = new CartTableModel();
    private static final ProductTableModel productTableModel = new ProductTableModel(createSampleProducts());
    private static JButton cartButton, addSelectedButton, clearCartButton, placeOrderButton;
    private static JLabel totalLabel;

    public static void main(String[] args) { SwingUtilities.invokeLater(Main::createAndShowGui); }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Taiga - Clean us out!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        productTableModel.addTableModelListener(_ -> updateUiStates());

        JTable productTable = createTable(productTableModel, new int[]{0,4}, new int[]{50,50});
        new ButtonColumn(productTable, 4, row -> { customer.addToCart(productTableModel.getProductAt(row)); updateUiStates(); });
        JPanel productPanel = new JPanel(new BorderLayout(5, 5));
        productPanel.add(new JScrollPane(productTable));
        productPanel.add(addSelectedButton = createButton("Add Selected", _ -> { productTableModel.getSelectedProducts().forEach(customer::addToCart); updateUiStates(); }), BorderLayout.SOUTH);

        JPanel cartPanel = createCartPanel();
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Welcome to Taiga!"), BorderLayout.CENTER);
        topPanel.add(cartButton = createButton("🛒 Cart (0)", _ -> cartPanel.setVisible(!cartPanel.isVisible())), BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(productPanel, BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);
        updateUiStates();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /** @return cart panel */
    private static JPanel createCartPanel() {
        JTable cartTable = createTable(cartTableModel, new int[]{1,3}, new int[]{40,50});
        new ButtonColumn(cartTable, 3, row -> { customer.removeFromCart(cartTableModel.getProductAt(row)); updateUiStates(); });
        JPanel cartBottom = new JPanel(new BorderLayout(5, 0));
        cartBottom.add(clearCartButton = createButton("🗑 Clear", _ -> { customer.clearCart(); updateUiStates(); }), BorderLayout.WEST);
        cartBottom.add(totalLabel = new JLabel(), BorderLayout.CENTER);
        cartBottom.add(placeOrderButton = createButton("✔ Place Order", _ -> placeOrder()), BorderLayout.EAST);
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));
        
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        cartPanel.add(new JScrollPane(cartTable));
        cartPanel.add(cartBottom, BorderLayout.SOUTH);
        cartPanel.setPreferredSize(new Dimension(350, 0));
        return cartPanel;
    }

    /** @param model The table model. @param cols Column indices to resize. @param widths Column widths. */
    private static JTable createTable(AbstractTableModel model, int[] cols, int[] widths) {
        JTable table = new JTable(model);
        table.setRowHeight(30); table.setAutoCreateRowSorter(true);
        IntStream.range(0, cols.length).forEach(i -> { TableColumn c = table.getColumnModel().getColumn(cols[i]); c.setPreferredWidth(widths[i]); c.setMaxWidth(widths[i]); });
        return table;
    }

    private static void placeOrder() {
        JTextField nameField = new JTextField();
        while (true) { /** loop untill valid user input */
            if (JOptionPane.showConfirmDialog(null, new Object[]{"Enter name (2-30 letters/spaces):", nameField}, "Checkout", JOptionPane.OK_CANCEL_OPTION) != 0) return;
            if (nameField.getText().trim().matches("^[a-zA-Z ]{2,30}$")) { customer.setCustName(nameField.getText().trim()); break; }
        }
        Order order = customer.placeOrder(); //order
        System.out.println(getConsoleOrderSummary(order));

        JTextArea receipt = new JTextArea(order.getReceipt(), 20, 41);
        receipt.setFont(new Font("Monospaced", Font.PLAIN, 12)); receipt.setEditable(false);
        JDialog dialog = new JDialog((Frame)null,"Order Placed!",true);
        dialog.add(new JScrollPane(receipt));
        dialog.add(createButton("OK", _ -> dialog.dispose()), BorderLayout.SOUTH);
        dialog.pack(); dialog.setLocationRelativeTo(null);
        customer.clearCart(); updateUiStates(); dialog.setVisible(true);
    }
    
    private static void updateUiStates() {
        cartTableModel.updateCartData(customer.getCart());
        double total = customer.calculateTotal(); //get total
        totalLabel.setText(String.format("Total: $%.2f", total));
        cartButton.setText(String.format("🛒 Cart (%d)", customer.getCart().values().stream().mapToInt(Integer::intValue).sum()));
        boolean isEmpty = customer.getCart().isEmpty();
        clearCartButton.setEnabled(!isEmpty); placeOrderButton.setEnabled(!isEmpty);
        addSelectedButton.setEnabled(productTableModel.hasSelection());
    }
    
    /** @param text Button text. @param listener Click action. */
    private static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        Color bg = new Color(70, 130, 180);
        button.setBackground(bg); button.setForeground(Color.WHITE); button.setFocusPainted(false);
        if (listener != null) button.addActionListener(listener);
        button.addMouseListener(new MouseAdapter(){public void mousePressed(MouseEvent e){button.setBackground(bg.darker());}public void mouseReleased(MouseEvent e){button.setBackground(bg);}});
        return button;
    }
    
    /** @param order The order to summarize. */
    private static String getConsoleOrderSummary(Order order) {
    	String items = order.getProducts().entrySet().stream() .map(e -> String.format("  - %s(x%d)", e.getKey().name(), e.getValue())) .collect(Collectors.joining("\n"));
        return "\n--- ORDER [%s] ---\nCUST ID: %s \nCUST Name: %s \n\nTOTAL: $%.2f\nITEMS:\n%s\n--- END ---".formatted(order.getOrderID(), order.getCustomer().getCustomerID(), order.getCustomer().getCustName(), order.getTotal(), items);
    }
    
    private static List<Product> createSampleProducts() {
        return List.of(
            new Product("P001","Laptop Pro","Electronics",1299.99), new Product("P002","Wireless Mouse","Electronics",35.50),
            new Product("P003","Java Mug","Kitchen",15.00), new Product("P004","4K Monitor","Electronics",350.00),
            new Product("P005","Ergo Chair","Office",275.00), new Product("P006","Desk Lamp","Office",45.00)
        );
    }
}