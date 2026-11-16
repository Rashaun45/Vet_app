package com.vet_clinic.vet_app.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import javax.swing.table.AbstractTableModel;

/** The TableModel behind every JTable in the application. One generic class serves all tables. */
public class RecordTableModel<T> extends AbstractTableModel {

    private final String[] columns;
    private final BiFunction<T, Integer, Object> cellReader;
    private List<T> rows = new ArrayList<>();

    public RecordTableModel(String[] columns, BiFunction<T, Integer, Object> cellReader) {
        this.columns = columns;
        this.cellReader = cellReader;
    }

    public void setRows(List<T> newRows) {
        this.rows = new ArrayList<>(newRows);
        fireTableDataChanged();
    }

    public T getRow(int modelRow) {
        return rows.get(modelRow);
    }

    @Override public int getRowCount() { return rows.size(); }

    @Override public int getColumnCount() { return columns.length; }

    @Override public String getColumnName(int column) { return columns[column]; }

    @Override public Object getValueAt(int row, int column) { return cellReader.apply(rows.get(row), column); }

    /** Lets numeric columns sort as numbers instead of text. */
    @Override
    public Class<?> getColumnClass(int column) {
        if (rows.isEmpty()) {
            return Object.class;
        }
        Object sample = getValueAt(0, column);
        return sample == null ? Object.class : sample.getClass();
    }
}