package com.ecommerce.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.Component;
import java.awt.event.ActionListener;

/** reusable button component for cells in a JTable. */
public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
	private static final long serialVersionUID = 1L;
	private final JButton button = new JButton(); private final transient RowAction rowAction; private int row;
    @FunctionalInterface public interface RowAction { void perform(int row); }

    /** @param table The table to add this button to. @param col The column index. @param action The action to perform on click. */
    public ButtonColumn(JTable table, int col, RowAction action) {
        rowAction = action; button.addActionListener(this);
        TableColumn column = table.getColumnModel().getColumn(col);
        column.setCellRenderer(this); column.setCellEditor(this);
    }
    @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { button.setText(""+v); return button; }
    @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { button.setText(""+v); row = r; return button; }
    @Override public Object getCellEditorValue() { return ""; }
    @Override public void actionPerformed(java.awt.event.ActionEvent e) { fireEditingStopped(); rowAction.perform(row); }
}