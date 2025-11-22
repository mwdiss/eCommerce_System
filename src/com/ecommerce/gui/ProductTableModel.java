package com.ecommerce.gui;

import com.ecommerce.Product;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.JButton;

/** Data model for main product table */
public class ProductTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final List<Product> products;
    private final List<Boolean> checked;
    private final String[] columns = {"Select", "Name", "Category", "Price", "Add"};

    public ProductTableModel(List<Product> prods) {
        this.products = prods; this.checked = new ArrayList<>(prods.stream().map(_ -> false).toList());
    }
    @Override public int getRowCount() { return products.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int col) { return columns[col]; }
    @Override public Class<?> getColumnClass(int col) { return col==0?Boolean.class:col==4?JButton.class:String.class;}
    @Override public boolean isCellEditable(int r, int c) { return c == 0 || c == 4; }
    public Product getProductAt(int r) { return products.get(r); }
    public boolean hasSelection() { return checked.contains(true); }
    public List<Product> getSelectedProducts() { return IntStream.range(0,products.size()).filter(checked::get).mapToObj(products::get).collect(Collectors.toList()); }
    @Override public Object getValueAt(int r, int c) {
        Product p = products.get(r);
        return switch(c){case 0->checked.get(r);case 1->p.name();case 2->p.category();case 3->String.format("$%.2f",p.price());case 4->"➕";default->null;};
    }
    @Override public void setValueAt(Object val, int r, int c) { if(c==0){checked.set(r,(Boolean)val);fireTableCellUpdated(r,c);} }
}