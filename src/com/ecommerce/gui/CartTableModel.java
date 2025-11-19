package com.ecommerce.gui;

import com.ecommerce.Product;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;

/** Data model for shopping cart table */
public class CartTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<Map.Entry<Product, Integer>> items = new ArrayList<>();
    private final String[] columns = {"Item", "Qty", "Total", "Remove"};

    @Override public int getRowCount() { return items.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int col) { return columns[col]; }
    @Override public Class<?> getColumnClass(int col) { return col == 3 ? JButton.class : String.class; }
    @Override public boolean isCellEditable(int r, int c) { return c == 3; }
    public Product getProductAt(int r) { return items.get(r).getKey(); }

    @Override public Object getValueAt(int r, int c) {
        Product p = items.get(r).getKey(); int qty = items.get(r).getValue();
        return switch(c){case 0->p.prodName();case 1->qty;case 2->String.format("$%.2f", p.price()*qty);case 3->"❌";default->null;};
    }
    /** @param cart Updates the table with new cart data. */
    public void updateCartData(Map<Product, Integer> cart) { items = new ArrayList<>(cart.entrySet()); fireTableDataChanged(); }
}