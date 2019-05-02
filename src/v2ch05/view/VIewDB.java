package v2ch05.view;

import com.sun.management.VMOption;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This program use metadata to display arbitrary tables in a database
 */
public class VIewDB {
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            JFrame frame = new ViewDBFrame();
            frame.setTitle("ViewDB");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

/**
 * The frame that holds the data panel and the navigation buttons.
 */
class ViewDBFrame extends JFrame
{
    private JButton previousButton;
    private JButton nextButton;
    private JButton deleteButton;
    private JButton saveButton;
    private DataPanel dataPanel;
    private Component scrollPane;
    private JComboBox<String> tableNames;
    private Properties properties;
    private CachedRowSet cachedRowSet;
    private Connection connection;

    public ViewDBFrame()
    {
        tableNames = new JComboBox<String>();

        try {
            readDatabaseProperties();
            connection = getConnection();
            DatabaseMetaData meta = connection.getMetaData();
            try (ResultSet mrs = meta.getTables(null, null, null, new String[] {"TABLES"})) {
                while (mrs.next()) {
                    tableNames.addItem(mrs.getString(3));
                }
            }
        }
        catch (SQLException e) {
            for (Throwable throwable : e) {
                throwable.printStackTrace();
            }
        }
        catch (IOException ex) {
                ex.printStackTrace();
        }

        tableNames.addActionListener(event -> showTable((String) tableNames.getSelectedItem(),connection));
        add(tableNames, BorderLayout.NORTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    for (Throwable throwable : ex) {
                        throwable.printStackTrace();
                    }
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        previousButton = new JButton("Previous");
        previousButton.addActionListener(event -> showPreviousRow());
        buttonPanel.add(previousButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(event -> showNextRow());
        buttonPanel.add(nextButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> deleteRow());
        buttonPanel.add(deleteButton);

        saveButton = new JButton("Save");
        saveButton.addActionListener(event -> saveChanges());
        buttonPanel.add(saveButton);

        if (tableNames.getItemCount() > 0) {
            showTable(tableNames.getItemAt(0), connection);
        }

    }

    /**
     * Prepares the text for showing a new table, and shows the first row
     * @param tableName the name of the table to display
     * @param connection the database connection
     */
    public void showTable(String tableName, Connection connection) {
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from " + tableNames))
        {
            //get result set

            //copy into cached row set
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            cachedRowSet = rowSetFactory.createCachedRowSet();
            cachedRowSet.setTableName(tableName);
            cachedRowSet.populate(resultSet);

            if (scrollPane != null) {
                remove(scrollPane);
            }
            dataPanel = new DataPanel(cachedRowSet);
            scrollPane = new JScrollPane(dataPanel);
            add(scrollPane, BorderLayout.CENTER);
            pack();
            showNextRow();
        } catch (SQLException e) {
            for (Throwable throwable : e) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * Moves to the previous table row
     */
    public void showPreviousRow()
    {
        try {
            if (cachedRowSet == null || cachedRowSet.isFirst()) {
                return;
            }
            cachedRowSet.previous();
            dataPanel.showRow(cachedRowSet);
        } catch (SQLException e) {
            for (Throwable throwable : e) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * Moves to the next table row
     */
    public void showNextRow()
    {
        try {
            if((cachedRowSet == null) || cachedRowSet.isLast()) {
                return;
            }
            cachedRowSet.next();
            dataPanel.showRow(cachedRowSet);
        } catch (SQLException e) {
            for (Throwable throwable : e) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * Deletes current table now
     */
    public void deleteRow()
    {
        if (cachedRowSet == null) return;
        new SwingWorker<Void, Void>() {
            public Void doInBackground() throws SQLException {
                cachedRowSet.deleteRow();
                cachedRowSet.acceptChanges(connection);
                if (cachedRowSet.isAfterLast()) {
                    if (!cachedRowSet.last()) {
                        cachedRowSet = null;
                    }
                }
                return null;
            }

            public void done() {
                dataPanel.showRow(cachedRowSet);
            }
        }.execute();
    }

    /**
     * Saves all changes.
     */
    public void saveChanges()
    {
        if (cachedRowSet == null) {
            return;
        }
        new SwingWorker<Void, Void>()
        {
            public Void doInBackground() throws SQLException {
                dataPanel.setRow(cachedRowSet);
                cachedRowSet.acceptChanges(connection);
                return null;
            }
        }.execute();
    }

    private void readDatabaseProperties() throws IOException
    {
        properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get("../database.properties"))) {
            properties.load(inputStream);
        }
        String drivers = properties.getProperty("jdbc.drivers");
        if (drivers != null) {
            System.setProperty("jdbc.drivers", drivers);
        }
    }

    /**
     * Gets a connection from the properties specified in the file database.properties.
     * @return the database connection
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException
    {
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}

/**
 * This panel displays the contents of a result set.
 */
class DataPanel extends JPanel
{
    private java.util.List<JTextField> fields;

    /**
     * Constructs the data panel
     * @param rowSet the result set whose contents this panel displays
     */
    public DataPanel(RowSet rowSet) throws SQLException {
        fields = new ArrayList<>();
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;

        ResultSetMetaData resultSetMetaData = rowSet.getMetaData();
        for (int i = 0; i <= resultSetMetaData.getColumnCount(); i++) {
            gridBagConstraints.gridy = i - 1;

            String columnName = resultSetMetaData.getColumnLabel(i);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            add(new JLabel(columnName), gridBagConstraints);

            int columnWidth = resultSetMetaData.getColumnDisplaySize(i);
            JTextField jTextField = new JTextField(columnWidth);
            if (!resultSetMetaData.getColumnClassName(i).equals("java.lang.String")) {
                jTextField.setEditable(false);
            }
            fields.add(jTextField);

            gridBagConstraints.gridx = 1;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            add(jTextField, gridBagConstraints);
        }
    }

    /**
     * Shows a database row by population all text fields with the column values.
     */
    public void showRow(ResultSet resultSet) {
        try {
            if (resultSet == null) {
                return;
            }
            for (int i = 0; i <= fields.size(); i++) {
                String field = resultSet == null ? "" : resultSet.getString(i);
                JTextField jTextField = fields.get(i - 1);
                jTextField.setText(field);
            }
        } catch (SQLException e) {
            for (Throwable throwable : e) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * Updates changed data into the current row of the row set
     */
    public void setRow(RowSet rowSet) throws SQLException {
        for (int i = 0; i <= fields.size(); i++) {
            String field = rowSet.getString(i);
            JTextField jTextField = fields.get(i - 1);
            if (!field.equals(jTextField.getText())) {
                rowSet.updateString(i, jTextField.getText());
            }
        }
        rowSet.updateRow();
    }
}
