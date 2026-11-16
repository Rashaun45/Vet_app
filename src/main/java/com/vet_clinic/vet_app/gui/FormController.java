package com.vet_clinic.vet_app.gui;

import com.vet_clinic.vet_app.util.ErrorHandler;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Brings a form that was drawn in the NetBeans Design tab to life.
 * The form supplies its components (table, text boxes, buttons); this class does the work:
 * fills the JTable through a TableModel, and runs Add (insert), Update, Delete and Refresh (select).
 */
public class FormController<T> {

    private static final Logger LOGGER = LogManager.getLogger(FormController.class);

    @FunctionalInterface public interface SqlSupplier<R> { R get() throws SQLException; }
    @FunctionalInterface public interface SqlFunction<A, R> { R apply(A a) throws SQLException; }
    @FunctionalInterface public interface SqlBiFunction<A, B, R> { R apply(A a, B b) throws SQLException; }

    private record Field<T>(String label, JComponent component, boolean required, boolean updatable,
                            Function<T, String> getter) { }

    private final JInternalFrame frame;
    private final Color accent;
    private final JTable table;
    private final JTextField search;
    private final JLabel status;
    private final JButton newBtn, addBtn, updateBtn, deleteBtn, refreshBtn;
    private final List<Field<T>> fields = new ArrayList<>();

    private RecordTableModel<T> model;
    private TableRowSorter<RecordTableModel<T>> sorter;
    private SqlSupplier<List<T>> loader;
    private Function<Map<String, String>, T> builder;
    private SqlFunction<T, Integer> inserter;
    private SqlFunction<T, Boolean> deleter;
    private SqlBiFunction<T, String, Boolean> updater;

    public FormController(JInternalFrame frame, Color accent, JTable table, JTextField search, JLabel status,
                          JButton newBtn, JButton addBtn, JButton updateBtn, JButton deleteBtn, JButton refreshBtn) {
        this.frame = frame;
        this.accent = accent;
        this.table = table;
        this.search = search;
        this.status = status;
        this.newBtn = newBtn;
        this.addBtn = addBtn;
        this.updateBtn = updateBtn;
        this.deleteBtn = deleteBtn;
        this.refreshBtn = refreshBtn;
    }

    // =====================================================================
    //  Telling the controller about the form's fields
    // =====================================================================
    /** A normal text box. */
    public void text(String label, JTextField box, String tip, boolean required, Function<T, String> getter) {
        addField(label, box, tip, required, false, null, getter);
    }

    /** The text box the Update button is allowed to change. */
    public void updatableText(String label, JTextField box, String tip, boolean required, Function<T, String> getter) {
        addField(label, box, tip, required, true, null, getter);
    }

    /** A drop-down list the user can also type into. */
    public void choice(String label, JComboBox<?> box, String tip, boolean required, String[] options,
                       Function<T, String> getter) {
        addField(label, box, tip, required, false, options, getter);
    }

    public void updatableChoice(String label, JComboBox<?> box, String tip, boolean required, String[] options,
                                Function<T, String> getter) {
        addField(label, box, tip, required, true, options, getter);
    }

    @SuppressWarnings("unchecked")
    private void addField(String label, JComponent comp, String tip, boolean required, boolean updatable,
                          String[] options, Function<T, String> getter) {
        comp.setToolTipText(tip);
        if (comp instanceof JComboBox<?> box) {
            String[] withBlank = new String[options.length + 1];
            withBlank[0] = "";
            System.arraycopy(options, 0, withBlank, 1, options.length);
            ((JComboBox<Object>) box).setModel((ComboBoxModel<Object>) (ComboBoxModel<?>) new DefaultComboBoxModel<>(withBlank));
            box.setEditable(true);
            box.setSelectedItem("");
        }
        fields.add(new Field<>(label, comp, required, updatable, getter));
    }

    // =====================================================================
    //  Start: connects the table, the buttons and the database
    // =====================================================================
    public void start(String[] columns, BiFunction<T, Integer, Object> cell,
                      SqlSupplier<List<T>> loader, Function<Map<String, String>, T> builder,
                      SqlFunction<T, Integer> inserter, SqlFunction<T, Boolean> deleter,
                      SqlBiFunction<T, String, Boolean> updater) {
        this.loader = loader;
        this.builder = builder;
        this.inserter = inserter;
        this.deleter = deleter;
        this.updater = updater;

        // the JTable gets its data from a TableModel
        model = new RecordTableModel<>(columns, cell);
        table.setModel(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table.setFillsViewportHeight(true);
        table.setToolTipText("Click a row to edit it. Click a column heading to sort.");

        frame.setClosable(true);
        frame.setIconifiable(true);
        frame.setMaximizable(true);
        frame.setResizable(true);
        frame.setFrameIcon(Badge.icon(frame.getTitle().charAt(0), accent, 16));

        search.setToolTipText("Type to filter the table (Ctrl+F)");
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilter(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilter(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });

        setupButton(newBtn, KeyEvent.VK_N, "Clear the form to enter a new record (Ctrl+N)", e -> newRecord());
        setupButton(addBtn, KeyEvent.VK_A, "Insert the record shown in the form into the database", e -> onAdd());
        setupButton(updateBtn, KeyEvent.VK_U, "Save the change to the highlighted field", e -> onUpdate());
        setupButton(deleteBtn, KeyEvent.VK_D, "Delete the selected record", e -> onDelete());
        setupButton(refreshBtn, KeyEvent.VK_E, "Reload the table from the database (F5)", e -> reload());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onSelectionChanged();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && selectedRow() != null) {
                    updatableField().component().requestFocusInWindow();
                }
            }
        });

        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "new", this::newRecord);
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh", this::reload);
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "search",
                () -> search.requestFocusInWindow());

        setEditing(false);
        reload();
    }

    private void setupButton(JButton b, int mnemonic, String tip, java.awt.event.ActionListener action) {
        b.setMnemonic(mnemonic);
        b.setToolTipText(tip);
        b.addActionListener(action);
    }

    private void bindKey(KeyStroke key, String name, Runnable r) {
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key, name);
        frame.getRootPane().getActionMap().put(name, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { r.run(); }
        });
    }

    // =====================================================================
    //  Behaviour
    // =====================================================================
    private void applyFilter() {
        String text = search.getText().trim();
        sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        setStatus(table.getRowCount() + " of " + model.getRowCount() + " records shown");
    }

    private T selectedRow() {
        int viewRow = table.getSelectedRow();
        return viewRow < 0 ? null : model.getRow(table.convertRowIndexToModel(viewRow));
    }

    private void onSelectionChanged() {
        T row = selectedRow();
        if (row == null) {
            setEditing(false);
            return;
        }
        for (Field<T> f : fields) {
            setValue(f.component(), f.getter().apply(row));
        }
        setEditing(true);
        setStatus("Record selected - only " + updatableField().label() + " can be changed. Edit it and press Update.");
    }

    /** editing = a row is selected, so only the updatable field is enabled. Otherwise: add mode. */
    private void setEditing(boolean editing) {
        for (Field<T> f : fields) {
            f.component().setEnabled(!editing || f.updatable());
        }
        addBtn.setEnabled(!editing);
        updateBtn.setEnabled(editing);
        deleteBtn.setEnabled(editing);
    }

    private void newRecord() {
        table.clearSelection();
        for (Field<T> f : fields) {
            setValue(f.component(), "");
        }
        setEditing(false);
        fields.get(0).component().requestFocusInWindow();
        setStatus("Fill in the form and press Add. Fields marked * are required.");
    }

    private Field<T> updatableField() {
        for (Field<T> f : fields) {
            if (f.updatable()) {
                return f;
            }
        }
        return fields.get(0);
    }

    private Map<String, String> readValues() {
        Map<String, String> values = new LinkedHashMap<>();
        for (Field<T> f : fields) {
            values.put(f.label(), getValue(f.component()));
        }
        return values;
    }

    private static String getValue(JComponent c) {
        if (c instanceof JComboBox<?> box) {
            Object item = box.getEditor().getItem();
            return item == null ? "" : item.toString().trim();
        }
        return ((JTextField) c).getText().trim();
    }

    private static void setValue(JComponent c, String value) {
        String v = value == null ? "" : value;
        if (c instanceof JComboBox<?> box) {
            box.setSelectedItem(v);
        } else {
            ((JTextField) c).setText(v);
        }
    }

    private void onAdd() {
        Map<String, String> values = readValues();
        for (Field<T> f : fields) {
            if (f.required() && values.get(f.label()).isEmpty()) {
                warn(f.label() + " is required.");
                return;
            }
        }
        T row;
        try {
            row = builder.apply(values);
        } catch (IllegalArgumentException e) {
            warn(e.getMessage());
            return;
        }
        try {
            int newId = inserter.apply(row);
            LOGGER.info("{}: added a record (id {})", frame.getTitle(), newId);
            newRecord();
            reload();
            setStatus("Record added (id " + newId + ")");
        } catch (SQLException e) {
            dbError("Adding a record to " + frame.getTitle(), e);
        } catch (RuntimeException e) {
            unexpected("Adding a record to " + frame.getTitle(), e);
        }
    }

    private void onUpdate() {
        T row = selectedRow();
        if (row == null) {
            return;
        }
        Field<T> field = updatableField();
        String value = getValue(field.component());
        if (field.required() && value.isEmpty()) {
            warn(field.label() + " is required.");
            return;
        }
        try {
            if (updater.apply(row, value)) {
                LOGGER.info("{}: updated {}", frame.getTitle(), field.label());
                reload();
                setStatus(field.label() + " updated");
            } else {
                warn("Nothing was changed - the record may no longer exist. Press Refresh.");
            }
        } catch (IllegalArgumentException e) {
            warn(e.getMessage());
        } catch (SQLException e) {
            dbError("Updating a record in " + frame.getTitle(), e);
        } catch (RuntimeException e) {
            unexpected("Updating a record in " + frame.getTitle(), e);
        }
    }

    private void onDelete() {
        T row = selectedRow();
        if (row == null) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(frame, "Delete the selected record permanently?",
                "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (deleter.apply(row)) {
                LOGGER.info("{}: deleted a record", frame.getTitle());
                reload();
                setStatus("Record deleted");
            } else {
                warn("Nothing was deleted - the record may no longer exist. Press Refresh.");
            }
        } catch (SQLException e) {
            dbError("Deleting a record from " + frame.getTitle(), e);
        } catch (RuntimeException e) {
            unexpected("Deleting a record from " + frame.getTitle(), e);
        }
    }

    /** Loads the table in the background so the window never freezes. */
    private void reload() {