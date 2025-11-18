import com.ecommerce.Customer;
import com.ecommerce.Product;
import com.ecommerce.orders.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * the ultra-condensed GUI for the Taiga E-commerce system.
 * @author Malith Dissanayake
 */
public class Main {
    private static Customer customer = new Customer("Guest");
    private static ProductTableModel productTableModel = new ProductTableModel(createSampleProducts());
    private static CartTableModel cartTableModel = new CartTableModel();
    private static JPanel cartPanel;
    private static JLabel totalLabel;
    private static JButton cartButton;

    public static void main(String[] args) { SwingUtilities.invokeLater(Main::createAndShowGui); }

    /** creates and displays the main app window. */
    private static void createAndShowGui() {
        JFrame frame = new JFrame("Taiga - Your Online Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        JTable productTable = createTable(productTableModel, new int[]{0,3,5}, new int[]{50,70,50});
        productTable.getColumnModel().getColumn(4).setCellRenderer(new PriceCellRenderer()); //price column
        new ButtonColumn(productTable, 5, row -> { //add button logic
            customer.addToCart(productTableModel.getProductAt(row)); updateCart(true);
        });

        cartButton = createButton("🛒 Cart (0)", _ -> cartPanel.setVisible(!cartPanel.isVisible()));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Welcome to Taiga!"), BorderLayout.CENTER);
        topPanel.add(cartButton, BorderLayout.EAST);
        
        JPanel productPanel = new JPanel(new BorderLayout(5, 5));
        productPanel.add(new JScrollPane(productTable));
        productPanel.add(createButton("Add Selected", _ -> {
            productTableModel.getSelectedProducts().forEach(customer::addToCart); updateCart(true);
        }), BorderLayout.SOUTH);

        JTable cartTable = createTable(cartTableModel, new int[]{4}, new int[]{60});
        cartTable.getColumnModel().getColumn(1).setCellRenderer(new PriceCellRenderer()); //unit price column
        new ButtonColumn(cartTable, 4, row -> { //remove button logic
            customer.removeFromCart(cartTableModel.getProductAt(row)); updateCart(false);
        });

        cartPanel = createCartPanel(cartTable);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(productPanel, BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);
        frame.setSize(1024, 600); frame.setLocationRelativeTo(null); frame.setVisible(true);
    }

    /** builds the right-side cart panel. */
    private static JPanel createCartPanel(JTable cartTable) {
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        bottomPanel.add(createButton("🗑️", _ -> { customer.clearCart(); updateCart(false); }), BorderLayout.WEST);
        bottomPanel.add(totalLabel, BorderLayout.CENTER);
        bottomPanel.add(createButton("Place Order", _ -> placeOrder()), BorderLayout.EAST);
        
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        panel.add(new JScrollPane(cartTable));
        panel.add(bottomPanel, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(500, 0));
        panel.setVisible(false);
        return panel;
    }
    
    /** a factory for creating all tables with fixed column sizes. */
    private static JTable createTable(AbstractTableModel model, int[] cols, int[] widths) {
        JTable table = new JTable(model);
        table.setRowHeight(40);
        for(int i = 0; i < cols.length; i++) { //set fixed widths for specific columns
            TableColumn col = table.getColumnModel().getColumn(cols[i]);
            col.setPreferredWidth(widths[i]); col.setMaxWidth(widths[i]);
        }
        return table;
    }

    /** handles order placement and shows a receipt. */
    private static void placeOrder() {
        if (customer.getCart().isEmpty()) return;
        String custName = JOptionPane.showInputDialog(null, "Enter name:", "Checkout", JOptionPane.PLAIN_MESSAGE);
        if (custName == null || custName.trim().isEmpty()) return;
        
        customer.setCustName(custName);
        Order order = new Order(customer, customer.getCart());
        JTextArea receiptArea = new JTextArea(order.getReceipt(), 15, 50);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); receiptArea.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(receiptArea), "Order Placed!", JOptionPane.INFORMATION_MESSAGE);
        customer.clearCart(); updateCart(false);
    }
    
    /** refreshes all cart-related ui and optionally flashes the cart button. */
    private static void updateCart(boolean animate) {
        Map<Product, Integer> cartData = customer.getCart();
        cartTableModel.updateCartData(cartData);
        double total = cartData.entrySet().stream().mapToDouble(e -> e.getKey().getDiscountedPrice() * e.getValue()).sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
        cartButton.setText(String.format("🛒 Cart (%d)", cartData.values().stream().mapToInt(i -> i).sum()));
        if(animate) flashComponent(cartButton, new Color(34, 139, 34), 2);
    }
    
    /** a factory for creating all styled buttons. */
    private static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        Color bg = new Color(70, 130, 180);
        button.setBackground(bg); button.setForeground(Color.WHITE); button.setFocusPainted(false);
        button.addActionListener(listener);
        button.addMouseListener(new MouseAdapter() { //press animation
            public void mousePressed(MouseEvent e) { button.setBackground(bg.darker()); }
            public void mouseReleased(MouseEvent e) { button.setBackground(bg); }
        });
        return button;
    }

    /** briefly flashes a component's background for visual feedback. */
    private static void flashComponent(JComponent comp, Color flashColor, int flashes) {
        Color original = comp.getBackground();
        Timer timer = new Timer(150, null);
        timer.addActionListener(new ActionListener() {
            private int count = 0;
            public void actionPerformed(ActionEvent e) {
                if (++count > flashes*2) { comp.setBackground(original); timer.stop(); } 
                else { comp.setBackground(count%2!=0 ? flashColor : original); }
            }
        });
        timer.start();
    }
    
    /** our easily editable list of sample products. */
    private static List<Product> createSampleProducts() {
        return List.of( new Product("P001","Laptop Pro","Electronics",1299.99,0.10), new Product("P002","Wireless Mouse","Electronics",35.50,0.0), new Product("P003","Java Mug","Kitchen",15.00,0.20), new Product("P004","4K Monitor","Electronics",350.00,0.15), new Product("P005","Ergo Chair","Office",275.00,0.0), new Product("P006","Desk Lamp","Office",45.00,0.0), new Product("P007","Blender","Kitchen",89.99,0.05), new Product("P008","Headphones","Electronics",150.00,0.0), new Product("P009","Standing Desk","Office",450.00,0.10), new Product("P010","Book: Clean Code","Books",42.50,0.0));
    }
}

/** custom cell renderer for showing discounted prices beautifully. */
class PriceCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    public PriceCellRenderer() { setHorizontalAlignment(CENTER); }
    public Component getTableCellRendererComponent(JTable tbl,Object val,boolean sel,boolean foc,int r,int c) {
        super.getTableCellRendererComponent(tbl, val, sel, foc, r, c);
        if (val instanceof Product p) setText(p.getDiscount()>0 ? String.format("<html><center><s>$%.2f</s><br><b color='#2E8B57'>$%.2f</b></center></html>",p.getPrice(),p.getDiscountedPrice()):String.format("$%.2f",p.getPrice()));
        return this;
    }
}

/** the data model for the main product table. */
class ProductTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private final List<Product> products; private final List<Boolean> checked;
    private final String[] columns = {"Select", "Name", "Category", "Discount", "Price", "Add"};

    public ProductTableModel(List<Product> prods) { this.products = prods; this.checked = new ArrayList<>(); prods.forEach(_->checked.add(false)); }
    public int getRowCount() { return products.size(); }
    public int getColumnCount() { return columns.length; }
    public String getColumnName(int col) { return columns[col]; }
    public Class<?> getColumnClass(int col) { return col==0 ? Boolean.class : col==4 ? Product.class : col==5 ? JButton.class : Object.class; }
    public boolean isCellEditable(int r, int c) { return c == 0 || c == 5; }
    public Product getProductAt(int r) { return products.get(r); }
    public List<Product> getSelectedProducts() {
        List<Product> selected = new ArrayList<>();
        for(int i=0; i<products.size(); i++) if(checked.get(i)) selected.add(products.get(i));
        return selected;
    }
    public Object getValueAt(int r, int c) {
        Product p = products.get(r);
        return switch (c) { case 0->checked.get(r); case 1->p.getProdName(); case 2->p.getCategory(); case 3->p.getDiscount()>0?String.format("%.0f%%",p.getDiscount()*100):"-"; case 4->p; case 5->"+"; default->null; };
    }
    public void setValueAt(Object val, int r, int c) { if(c == 0) checked.set(r, (Boolean) val); }
}

/** the data model for the shopping cart. */
class CartTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<Map.Entry<Product, Integer>> items = new ArrayList<>();
    private final String[] columns = {"Item", "Unit Price", "Qty", "Total", "Remove"};
    
    public int getRowCount() { return items.size(); }
    public int getColumnCount() { return columns.length; }
    public String getColumnName(int col) { return columns[col]; }
    public Class<?> getColumnClass(int col) { return col==1?Product.class : col==4?JButton.class:Object.class; }
    public boolean isCellEditable(int r, int c) { return c == 4; }
    public Product getProductAt(int r) { return items.get(r).getKey(); }
    public Object getValueAt(int r, int c) {
        Product p = items.get(r).getKey(); int qty = items.get(r).getValue();
        return switch (c) { case 0->p.getProdName(); case 1->p; case 2->qty; case 3->String.format("$%.2f", p.getDiscountedPrice()*qty); case 4->"x"; default->null; };
    }
    public void updateCartData(Map<Product, Integer> cart) { items = new ArrayList<>(cart.entrySet()); fireTableDataChanged(); }
}

/** a highly condensed, reusable button component for tables. */
class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private static final long serialVersionUID = 1L;
    private final JButton renderButton = new JButton(), editButton = new JButton();
    private final RowAction rowAction; private int row;
    interface RowAction { void perform(int row); }
    
    public ButtonColumn(JTable table, int col, RowAction action) {
        this.rowAction = action; editButton.addActionListener(e -> { fireEditingStopped(); rowAction.perform(row); });
        TableColumn column = table.getColumnModel().getColumn(col);
        column.setCellRenderer(this); column.setCellEditor(this);
    }
    public Component getTableCellRendererComponent(JTable tbl,Object v,boolean s,boolean f,int r,int c) { renderButton.setText(""+v); return renderButton; }
    public Component getTableCellEditorComponent(JTable tbl,Object v,boolean s,int r,int c) { editButton.setText(""+v); this.row = r; return editButton; }
    public Object getCellEditorValue() { return ""; }
}