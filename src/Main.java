import com.ecommerce.Customer;
import com.ecommerce.Product;
import com.ecommerce.orders.Order;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A feature-rich, animated Swing GUI for the E-commerce system.
 * @author Malith Dissanayake
 */
public class Main {
    private static Customer customer = new Customer("Guest");
    private static ProductTableModel prodTblModel;
    private static CartTableModel cartTblModel;
    private static JPanel cartPanel;
    private static JLabel totalLbl;
    private static JButton cartBtn;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGui);
    }

    /** Creates and displays the main application window. */
    private static void createAndShowGui() {
        JFrame frame = new JFrame("E-commerce Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // --- Models & Tables ---
        prodTblModel = new ProductTableModel(createSampleProducts());
        JTable prodTbl = new JTable(prodTblModel);
        prodTbl.setRowHeight(30);
        new ButtonColumn(prodTbl, 4, row -> {
            customer.addToCart(prodTblModel.getProductAt(row));
            updateCart();
            flashComponent(cartBtn, Color.ORANGE, 2);
        });

        cartTblModel = new CartTableModel();
        JTable cartTbl = new JTable(cartTblModel);
        cartTbl.setRowHeight(30);
        new ButtonColumn(cartTbl, 3, row -> {
            customer.removeFromCart(cartTblModel.getProductAt(row));
            updateCart();
        });

        // --- UI Panels ---
        cartBtn = createButton("🛒 Cart", _ -> cartPanel.setVisible(!cartPanel.isVisible()));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Welcome!", JLabel.LEFT), BorderLayout.CENTER);
        topPanel.add(cartBtn, BorderLayout.EAST);

        JButton addSelBtn = createButton("Add Selected", _ -> {
            prodTblModel.getSelectedProducts().forEach(customer::addToCart);
            updateCart();
            flashComponent(cartBtn, Color.ORANGE, 2);
        });
        JPanel prodPanel = new JPanel(new BorderLayout(5, 5));
        prodPanel.add(new JScrollPane(prodTbl), BorderLayout.CENTER);
        prodPanel.add(addSelBtn, BorderLayout.SOUTH);

        totalLbl = new JLabel("Total: $0.00");
        totalLbl.setFont(totalLbl.getFont().deriveFont(Font.BOLD, 16f));
        JButton orderBtn = createButton("Place Order", _ -> placeOrder());
        JPanel cartBottomPanel = new JPanel(new BorderLayout());
        cartBottomPanel.add(totalLbl, BorderLayout.CENTER);
        cartBottomPanel.add(orderBtn, BorderLayout.EAST);

        cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        cartPanel.add(new JScrollPane(cartTbl), BorderLayout.CENTER);
        cartPanel.add(cartBottomPanel, BorderLayout.SOUTH);
        cartPanel.setPreferredSize(new Dimension(400, 0));
        cartPanel.setVisible(false);

        // --- Frame Assembly ---
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(prodPanel, BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** Handles the entire order placement process. */
    private static void placeOrder() {
        if (customer.getCart().isEmpty()) return;
        String name = JOptionPane.showInputDialog(null, "Enter your name for the order:", "Checkout", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) return;

        customer.setName(name);
        Order order = new Order(customer, customer.getCart());
        JOptionPane.showMessageDialog(null, order.getSummary(), "Order Placed!", JOptionPane.INFORMATION_MESSAGE);
        customer.clearCart();
        updateCart();
    }

    /** Central method to refresh cart view, counter, and total. */
    private static void updateCart() {
        Map<Product, Integer> cartData = customer.getCart();
        cartTblModel.updateCartData(cartData);
        double total = cartData.entrySet().stream().mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
        totalLbl.setText(String.format("Total: $%.2f", total));
        int count = cartData.values().stream().mapToInt(Integer::intValue).sum();
        cartBtn.setText(String.format("🛒 Cart (%d)", count));
    }

    /** UI factory for creating styled, animated buttons. */
    private static JButton createButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        Color defaultBg = new Color(70, 130, 180); // Steel Blue
        btn.setBackground(defaultBg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(listener);
        btn.addMouseListener(new MouseAdapter() { // Simple press animation
            public void mousePressed(MouseEvent e) { btn.setBackground(defaultBg.darker()); }
            public void mouseReleased(MouseEvent e) { btn.setBackground(defaultBg); }
        });
        return btn;
    }

    /** Briefly flashes a component's background color as a visual cue. */
    private static void flashComponent(JComponent comp, Color flashColor, int flashes) {
        Color original = comp.getBackground();
        Timer timer = new Timer(150, null);
        timer.addActionListener(new ActionListener() {
            private int count = 0;
            public void actionPerformed(ActionEvent e) {
                if (count >= flashes * 2) {
                    comp.setBackground(original);
                    timer.stop();
                } else {
                    comp.setBackground(count % 2 == 0 ? flashColor : original);
                    count++;
                }
            }
        });
        timer.start();
    }

    private static List<Product> createSampleProducts() {
        return List.of(new Product("P001","Laptop Pro","Electronics",1299.99), new Product("P002","Wireless Mouse","Electronics",35.50), new Product("P003","Java Mug","Kitchen",15.00), new Product("P004","4K Monitor","Electronics",350.00), new Product("P005","Ergo Chair","Office",275.00));
    }
}

/** Manages product table data. */
class ProductTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private final List<Product> prods;
    private final List<Boolean> checked;
    private final String[] cols = {"Select", "Name", "Category", "Price", "Add"};

    ProductTableModel(List<Product> prods) {
        this.prods = prods;
        checked = new ArrayList<>(prods.size());
        prods.forEach(_ -> checked.add(false));
    }

    public int getRowCount() { return prods.size(); }
    public int getColumnCount() { return cols.length; }
    public String getColumnName(int c) { return cols[c]; }
    public Class<?> getColumnClass(int c) { return c == 0 ? Boolean.class : c == 4 ? JButton.class : Object.class; }
    public boolean isCellEditable(int r, int c) { return c == 0 || c == 4; }
    public Product getProductAt(int r) { return prods.get(r); }
    public List<Product> getSelectedProducts() {
        List<Product> selected = new ArrayList<>();
        for(int i = 0; i < prods.size(); i++) if(checked.get(i)) selected.add(prods.get(i));
        return selected;
    }
    public Object getValueAt(int r, int c) {
        Product p = prods.get(r);
        return switch (c) {
            case 0 -> checked.get(r);
            case 1 -> p.getName();
            case 2 -> p.getCategory();
            case 3 -> String.format("$%.2f", p.getPrice());
            case 4 -> "+";
            default -> null;
        };
    }
    public void setValueAt(Object val, int r, int c) {
        if(c == 0) checked.set(r, (Boolean) val);
    }
}

/** Manages shopping cart table data. */
class CartTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<Map.Entry<Product, Integer>> items = new ArrayList<>();
    private final String[] cols = {"Item", "Price", "Qty", "Remove"};

    public int getRowCount() { return items.size(); }
    public int getColumnCount() { return cols.length; }
    public String getColumnName(int c) { return cols[c]; }
    public Class<?> getColumnClass(int c) { return c == 3 ? JButton.class : Object.class; }
    public boolean isCellEditable(int r, int c) { return c == 3; }
    public Product getProductAt(int r) { return items.get(r).getKey(); }

    public Object getValueAt(int r, int c) {
        Map.Entry<Product, Integer> item = items.get(r);
        return switch (c) {
            case 0 -> item.getKey().getName();
            case 1 -> String.format("$%.2f", item.getKey().getPrice());
            case 2 -> item.getValue();
            case 3 -> "x";
            default -> null;
        };
    }
    public void updateCartData(Map<Product, Integer> cart) {
        items = new ArrayList<>(cart.entrySet());
        fireTableDataChanged();
    }
}

/** Reusable component for adding a clickable button to a JTable column. */
class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, ActionListener, javax.swing.table.TableCellEditor {
    private static final long serialVersionUID = 1L;
    private final JButton renderBtn, editBtn;
    private final RowAction action;
    private int row;

    interface RowAction { void onAction(int row); }

    public ButtonColumn(JTable table, int col, RowAction action) {
        this.action = action;
        renderBtn = new JButton(); editBtn = new JButton();
        editBtn.addActionListener(this);
        table.getColumnModel().getColumn(col).setCellRenderer(this);
        table.getColumnModel().getColumn(col).setCellEditor(this);
    }

    public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
        renderBtn.setText(val != null ? val.toString() : "");
        return renderBtn;
    }
    public Component getTableCellEditorComponent(JTable t, Object val, boolean sel, int r, int c) {
        editBtn.setText(val != null ? val.toString() : "");
        row = r;
        return editBtn;
    }
    public Object getCellEditorValue() { return editBtn.getText(); }
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        action.onAction(row);
    }
}