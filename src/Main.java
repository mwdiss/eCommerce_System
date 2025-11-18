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

/**
 * the condensed GUI for the Taiga E-commerce system.
 * @author Malith Dissanayake
 */
public class Main {
    private static final Customer customer = new Customer("Guest");
    private static final CartTableModel cartTableModel = new CartTableModel();
    private static final ProductTableModel productTableModel = new ProductTableModel(createSampleProducts());
    private static JButton cartButton, addSelectedButton, clearCartButton, placeOrderButton;
    private static JLabel totalLabel;

    public static void main(String[] args) { SwingUtilities.invokeLater(Main::createAndShowGui); }

    /** creates and displays the main app window. */
    private static void createAndShowGui() {
        JFrame frame = new JFrame("Taiga Store - Come clean us out!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.setLayout(new BorderLayout(10, 10));
        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        productTableModel.addTableModelListener(_ -> updateUiStates());

        JTable productTable = createTable(productTableModel, new int[]{0,4}, new int[]{50,50});
        new ButtonColumn(productTable, 4, row -> { customer.addToCart(productTableModel.getProductAt(row)); updateUiStates(); });

        cartButton = createButton("🛒 Cart (0)", null);
        addSelectedButton = createButton("Add Selected", _ -> { productTableModel.getSelectedProducts().forEach(customer::addToCart); updateUiStates(); });
        clearCartButton = createButton("🗑 Clear", _ -> { customer.clearCart(); updateUiStates(); });
        placeOrderButton = createButton("✔ Place Order", _ -> placeOrder());
        totalLabel = new JLabel(); totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Welcome to Taiga!"), BorderLayout.CENTER); topPanel.add(cartButton, BorderLayout.EAST);

        JPanel productPanel = new JPanel(new BorderLayout(5, 5));
        productPanel.add(new JScrollPane(productTable)); productPanel.add(addSelectedButton, BorderLayout.SOUTH);

        JTable cartTable = createTable(cartTableModel, new int[]{1,3}, new int[]{40,50});
        new ButtonColumn(cartTable, 3, row -> { customer.removeFromCart(cartTableModel.getProductAt(row)); updateUiStates(); });

        JPanel cartBottomPanel = new JPanel(new BorderLayout(5, 0));
        cartBottomPanel.add(clearCartButton, BorderLayout.WEST); cartBottomPanel.add(totalLabel, BorderLayout.CENTER); cartBottomPanel.add(placeOrderButton, BorderLayout.EAST);

        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        cartPanel.add(new JScrollPane(cartTable)); cartPanel.add(cartBottomPanel, BorderLayout.SOUTH);
        cartPanel.setPreferredSize(new Dimension(350, 0));

        cartButton.addActionListener(_ -> cartPanel.setVisible(!cartPanel.isVisible()));

        frame.add(topPanel, BorderLayout.NORTH); frame.add(productPanel, BorderLayout.CENTER); frame.add(cartPanel, BorderLayout.EAST);
        updateUiStates(); //sets initial button states
        frame.setSize(800, 600); frame.setLocationRelativeTo(null); frame.setVisible(true);
    }

    /** factory for creating tables with sorting and fixed column sizes. */
    private static JTable createTable(AbstractTableModel model, int[] cols, int[] widths) {
        JTable table = new JTable(model);
        table.setRowHeight(30); table.setAutoCreateRowSorter(true);
        for(int i = 0; i < cols.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(cols[i]);
            col.setPreferredWidth(widths[i]); col.setMaxWidth(widths[i]);
        }
        return table;
    }

    /** handles order placement and shows receipt. */
    private static void placeOrder() {
        JTextField nameField = new JTextField();
        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void replace(FilterBypass fb, int o, int l, String t, AttributeSet a) throws BadLocationException {
                if(fb.getDocument().getLength()+t.length()-l<=30) super.replace(fb,o,l,t,a);
            }
        });

        while(true) { //loop until valid name is entered or user cancels
            if(JOptionPane.showConfirmDialog(null,new Object[]{"Enter name:",nameField},"Checkout",JOptionPane.OK_CANCEL_OPTION)!=0) return;
            if(!nameField.getText().trim().isEmpty()) break;
        }

        customer.setCustName(nameField.getText().trim());
        Order order = new Order(customer, customer.getCart());
        System.out.println(getConsoleOrderSummary(order));

        JTextArea receiptArea = new JTextArea(order.getReceipt(), 20, 41);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); receiptArea.setEditable(false);
        receiptArea.setBorder(new EmptyBorder(15, 5, 5, 5));
        JDialog receiptDialog = new JDialog((Frame)null, "Order Placed!", true);
        ((JPanel)receiptDialog.getContentPane()).setBorder(new EmptyBorder(10,10,10,10));
        receiptDialog.add(new JScrollPane(receiptArea));
        receiptDialog.add(createButton("OK", _ -> receiptDialog.dispose()), BorderLayout.SOUTH);
        receiptDialog.pack(); receiptDialog.setResizable(true); receiptDialog.setLocationRelativeTo(null);
        customer.clearCart(); updateUiStates();
        receiptDialog.setVisible(true);
    }

    /** refreshes all cart and product selection related ui states. */
    private static void updateUiStates() {
        cartTableModel.updateCartData(customer.getCart());
        double total = customer.getCart().entrySet().stream().mapToDouble(e->e.getKey().getPrice()*e.getValue()).sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
        cartButton.setText(String.format("🛒 Cart (%d)", customer.getCart().values().stream().mapToInt(i->i).sum()));
        boolean cartEmpty = customer.getCart().isEmpty();
        clearCartButton.setEnabled(!cartEmpty); placeOrderButton.setEnabled(!cartEmpty);
        addSelectedButton.setEnabled(productTableModel.hasSelection());
    }

    /** factory for creating all styled buttons. */
    private static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text); Color bg = new Color(70, 130, 180);
        button.setBackground(bg); button.setForeground(Color.WHITE); button.setFocusPainted(false);
        if (listener != null) button.addActionListener(listener);
        button.addMouseListener(new MouseAdapter() { //press animation
            public void mousePressed(MouseEvent e) { button.setBackground(bg.darker()); }
            public void mouseReleased(MouseEvent e) { button.setBackground(bg); }
        });
        return button;
    }

    private static String getConsoleOrderSummary(Order order) {
        String items = order.getProducts().entrySet().stream().map(e->String.format("  - %s(x%d)",e.getKey().getProdName(),e.getValue())).collect(Collectors.joining("\n"));
        return String.format("\n--- ORDER [%s] ---\nCUST: %s\nTOTAL: $%.2f\nITEMS:\n%s\n--- END ---", order.getOrderID(), order.getCustomer().getCustName(), order.getTotal(), items);
    }

    /** our easily editable list of sample products. */
    private static List<Product> createSampleProducts() {
        return List.of(
            new Product("P001","Laptop Pro","Electronics",1299.99), new Product("P002","Wireless Mouse","Electronics",35.50),
            new Product("P003","Java Mug","Kitchen",15.00), new Product("P004","4K Monitor","Electronics",350.00),
            new Product("P005","Ergo Chair","Office",275.00), new Product("P006","Desk Lamp","Office",45.00),
            new Product("P007","Blender","Kitchen",89.99), new Product("P008","Headphones","Electronics",150.00),
            new Product("P009","Standing Desk","Office",450.00), new Product("P010","Book: Clean Code","Books",42.50)
        );
    }
}

/** the data model for the product table. */
class ProductTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private final List<Product> products; private final List<Boolean> checked;
    private final String[] columns = {"Select", "Name", "Category", "Price", "Add"};

    public ProductTableModel(List<Product> prods) { products=prods; checked=new ArrayList<>(); prods.forEach(_->checked.add(false)); }
    public int getRowCount() { return products.size(); } public int getColumnCount() { return columns.length; }
    public String getColumnName(int col) { return columns[col]; }
    public Class<?> getColumnClass(int col) { return col==0?Boolean.class:col==4?JButton.class:Object.class; }
    public boolean isCellEditable(int r, int c) { return c == 0 || c == 4; }
    public Product getProductAt(int r) { return products.get(r); }
    public boolean hasSelection() { return checked.contains(true); }
    public List<Product> getSelectedProducts() {
        List<Product> selected = new ArrayList<>(); for(int i=0;i<products.size();i++)if(checked.get(i))selected.add(products.get(i)); return selected;
    }
    public Object getValueAt(int r, int c) {
        Product p = products.get(r);
        return switch(c) { case 0->checked.get(r); case 1->p.getProdName(); case 2->p.getCategory(); case 3->String.format("$%.2f",p.getPrice()); case 4->"➕"; default->null; };
    }
    public void setValueAt(Object val, int r, int c) { if(c==0){checked.set(r,(Boolean)val); fireTableCellUpdated(r,c); } }
}

/** the data model for the shopping cart. */
class CartTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<Map.Entry<Product, Integer>> items = new ArrayList<>();
    private final String[] columns = {"Item", "Qty", "Total", "Remove"};

    public int getRowCount() { return items.size(); } public int getColumnCount() { return columns.length; }
    public String getColumnName(int col) { return columns[col]; }
    public Class<?> getColumnClass(int col) { return col==3?JButton.class:Object.class; }
    public boolean isCellEditable(int r, int c) { return c == 3; }
    public Product getProductAt(int r) { return items.get(r).getKey(); }
    public Object getValueAt(int r, int c) {
        Product p = items.get(r).getKey(); int qty = items.get(r).getValue();
        return switch(c) { case 0->p.getProdName(); case 1->qty; case 2->String.format("$%.2f", p.getPrice()*qty); case 3->"❌"; default->null; };
    }
    public void updateCartData(Map<Product, Integer> cart) { items = new ArrayList<>(cart.entrySet()); fireTableDataChanged(); }
}

/** a reusable button component for tables. */
class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private static final long serialVersionUID = 1L;
    private final JButton renderButton = new JButton(), editButton = new JButton();
    private final RowAction rowAction; private int row;
    interface RowAction { void perform(int row); }

    public ButtonColumn(JTable table, int col, RowAction action) {
        rowAction = action; editButton.addActionListener(_ -> { fireEditingStopped(); rowAction.perform(row); });
        TableColumn column = table.getColumnModel().getColumn(col); column.setCellRenderer(this); column.setCellEditor(this);
    }
    public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r, int c){renderButton.setText(""+v); return renderButton;}
    public Component getTableCellEditorComponent(JTable t,Object v,boolean s,int r,int c){editButton.setText(""+v); row=r; return editButton;}
    public Object getCellEditorValue() { return ""; }
}