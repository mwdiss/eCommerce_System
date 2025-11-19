import com.ecommerce.Customer;
import com.ecommerce.Product;
import com.ecommerce.orders.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A condensed GUI for a simple E-commerce system.
 * @author Malith Dissanayake
 */
public class Main {
    private static final Customer customer = new Customer("Guest");
    private static final CartTableModel cartTableModel = new CartTableModel();
    private static final ProductTableModel productTableModel = new ProductTableModel(createSampleProducts());
    private static JButton cartButton, addSelectedButton, clearCartButton, placeOrderButton;
    private static JLabel totalLabel;

    public static void main(String[] args) { SwingUtilities.invokeLater(Main::createAndShowGui); }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Taiga Store - Come clean us out!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        productTableModel.addTableModelListener(_ -> updateUiStates());

        JTable productTable = createTable(productTableModel, new int[]{0, 4}, new int[]{50, 50});
        new ButtonColumn(productTable, 4, row -> { customer.addToCart(productTableModel.getProductAt(row)); updateUiStates(); });
        JPanel productPanel = new JPanel(new BorderLayout(5, 5));
        productPanel.add(new JScrollPane(productTable));
        productPanel.add(addSelectedButton = createButton("Add Selected", _ -> { productTableModel.getSelectedProducts().forEach(customer::addToCart); updateUiStates(); }), BorderLayout.SOUTH);

        JPanel cartBottomPanel = new JPanel(new BorderLayout(5, 0));
        cartBottomPanel.add(clearCartButton = createButton("🗑 Clear", _ -> { customer.clearCart(); updateUiStates(); }), BorderLayout.WEST);
        cartBottomPanel.add(totalLabel = new JLabel(), BorderLayout.CENTER);
        cartBottomPanel.add(placeOrderButton = createButton("✔ Place Order", _ -> placeOrder()), BorderLayout.EAST);
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));
        JTable cartTable = createTable(cartTableModel, new int[]{1, 3}, new int[]{40, 50});
        new ButtonColumn(cartTable, 3, row -> { customer.removeFromCart(cartTableModel.getProductAt(row)); updateUiStates(); });
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        cartPanel.add(new JScrollPane(cartTable));
        cartPanel.add(cartBottomPanel, BorderLayout.SOUTH);
        cartPanel.setPreferredSize(new Dimension(350, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Welcome to Taiga! Select products to start."), BorderLayout.CENTER);
        topPanel.add(cartButton = createButton("🛒 Cart (0)", _ -> cartPanel.setVisible(!cartPanel.isVisible())), BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(productPanel, BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);
        updateUiStates();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** @param model The table model to use. @param cols Column indices to resize. @param widths The widths for columns. */
    private static JTable createTable(AbstractTableModel model, int[] cols, int[] widths) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        IntStream.range(0, cols.length).forEach(i -> {
            TableColumn col = table.getColumnModel().getColumn(cols[i]);
            col.setPreferredWidth(widths[i]); col.setMaxWidth(widths[i]);
        });
        return table;
    }

    private static void placeOrder() {
        JTextField nameField = new JTextField();
        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void replace(FilterBypass fb, int o, int l, String t, AttributeSet a) throws BadLocationException {
                if ((fb.getDocument().getLength() + t.length() - l) <= 30) super.replace(fb, o, l, t, a);
            }
        });
        do {
            if (JOptionPane.showConfirmDialog(null, new Object[]{"Enter name:", nameField}, "Checkout", JOptionPane.OK_CANCEL_OPTION) != 0) return;
        } while (nameField.getText().trim().isEmpty());

        customer.setCustName(nameField.getText().trim());
        Order order = new Order(customer, customer.getCart());
        System.out.println(getConsoleOrderSummary(order));

        JTextArea receiptArea = new JTextArea(order.getReceipt(), 20, 41);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        JDialog receiptDialog = new JDialog((Frame) null, "Order Placed!", true);
        receiptDialog.add(new JScrollPane(receiptArea));
        receiptDialog.add(createButton("OK", _ -> receiptDialog.dispose()), BorderLayout.SOUTH);
        receiptDialog.pack();
        receiptDialog.setLocationRelativeTo(null);
        customer.clearCart();
        updateUiStates();
        receiptDialog.setVisible(true);
    }
    
    private static void updateUiStates() {
        cartTableModel.updateCartData(customer.getCart());
        double total = customer.getCart().entrySet().stream().mapToDouble(e -> e.getKey().price() * e.getValue()).sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
        cartButton.setText(String.format("🛒 Cart (%d)", customer.getCart().values().stream().mapToInt(Integer::intValue).sum()));
        boolean cartEmpty = customer.getCart().isEmpty();
        clearCartButton.setEnabled(!cartEmpty);
        placeOrderButton.setEnabled(!cartEmpty);
        addSelectedButton.setEnabled(productTableModel.hasSelection());
    }
    
    /** @param text Button text. @param listener Action to perform on click. */
    private static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        Color bg = new Color(70, 130, 180);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        if (listener != null) button.addActionListener(listener);
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { button.setBackground(bg.darker()); }
            public void mouseReleased(MouseEvent e) { button.setBackground(bg); }
        });
        return button;
    }
    
    private static String getConsoleOrderSummary(Order order) {
        String items = order.getProducts().entrySet().stream().map(e -> String.format("  - %s (x%d)", e.getKey().prodName(), e.getValue())).collect(Collectors.joining("\n"));
        return "\n--- ORDER [%s] ---\nCUST: %s\nTOTAL: $%.2f\nITEMS:\n%s\n--- END ---".formatted(order.getOrderID(), order.getCustomer().getCustName(), order.getTotal(), items);
    }
    
    private static List<Product> createSampleProducts() {
        return List.of(
            new Product("P001", "Laptop Pro", "Electronics", 1299.99), new Product("P002", "Wireless Mouse", "Electronics", 35.50),
            new Product("P003", "Java Mug", "Kitchen", 15.00),       new Product("P004", "4K Monitor", "Electronics", 350.00),
            new Product("P005", "Ergo Chair", "Office", 275.00),      new Product("P006", "Desk Lamp", "Office", 45.00),
            new Product("P007", "Blender", "Kitchen", 89.99),         new Product("P008", "Headphones", "Electronics", 150.00),
            new Product("P009", "Standing Desk", "Office", 450.00),   new Product("P010", "Book: Clean Code", "Books", 42.50)
        );
    }
}

class ProductTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final List<Product> products; private final List<Boolean> checked;
    private final String[] columns = {"Select", "Name", "Category", "Price", "Add"};
    public ProductTableModel(List<Product> prods) { this.products=prods; this.checked=new ArrayList<>(prods.stream().map(_->false).toList()); }
    public int getRowCount() { return products.size(); }
    public int getColumnCount() { return columns.length; }
    public String getColumnName(int col) { return columns[col]; }
    public Class<?> getColumnClass(int col) { return col==0?Boolean.class:col==4?JButton.class:String.class; }
    public boolean isCellEditable(int r, int c) { return c == 0 || c == 4; }
    public Product getProductAt(int r) { return products.get(r); }
    public boolean hasSelection() { return checked.contains(true); }
    public List<Product> getSelectedProducts() { return IntStream.range(0,products.size()).filter(checked::get).mapToObj(products::get).collect(Collectors.toList()); }
    public Object getValueAt(int r, int c) {
        Product p = products.get(r);
        return switch(c) { case 0->checked.get(r); case 1->p.prodName(); case 2->p.category(); case 3->String.format("$%.2f",p.price()); case 4->"➕"; default->null; };
    }
    public void setValueAt(Object val, int r, int c) { if (c == 0) { checked.set(r, (Boolean) val); fireTableCellUpdated(r,c); } }
}

class CartTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<Map.Entry<Product, Integer>> items = new ArrayList<>();
    private final String[] columns = {"Item", "Qty", "Total", "Remove"};
    public int getRowCount() { return items.size(); }
    public int getColumnCount() { return columns.length; }
    public String getColumnName(int col) { return columns[col]; }
    public Class<?> getColumnClass(int col) { return col==3 ? JButton.class : String.class; }
    public boolean isCellEditable(int r, int c) { return c == 3; }
    public Product getProductAt(int r) { return items.get(r).getKey(); }
    public Object getValueAt(int r, int c) {
        Product p = items.get(r).getKey(); int qty = items.get(r).getValue();
        return switch(c) { case 0->p.prodName(); case 1->qty; case 2->String.format("$%.2f",p.price()*qty); case 3->"❌"; default->null; };
    }
    public void updateCartData(Map<Product, Integer> cart) { items = new ArrayList<>(cart.entrySet()); fireTableDataChanged(); }
}

class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
	private static final long serialVersionUID = 1L;
	private final JButton button = new JButton(); private final transient RowAction rowAction; private int row;
    @FunctionalInterface interface RowAction { void perform(int row); }
    public ButtonColumn(JTable table, int col, RowAction action) {
        this.rowAction = action; button.addActionListener(this);
        TableColumn column = table.getColumnModel().getColumn(col); column.setCellRenderer(this); column.setCellEditor(this);
    }
    public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){button.setText(""+v);return button;}
    public Component getTableCellEditorComponent(JTable t,Object v,boolean s,int r,int c){button.setText(""+v);this.row=r;return button;}
    public Object getCellEditorValue() { return ""; }
    public void actionPerformed(java.awt.event.ActionEvent e) { fireEditingStopped(); rowAction.perform(row); }
}