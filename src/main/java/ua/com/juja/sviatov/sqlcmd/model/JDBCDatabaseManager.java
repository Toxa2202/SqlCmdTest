package ua.com.juja.sviatov.sqlcmd.model;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by anton.sviatov on 23.05.2019.
 */
public class JDBCDatabaseManager implements DatabaseManager {
        // Initialize connection once
    private Connection connection;

    @Override
    public DataSet[] getTableData(String tableName) {
        try {
            int size = getSize(tableName);

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            // Array to store all saved data from the tables
            DataSet[] result = new DataSet[size];
            int index = 0;

            while (rs.next()) {
                // Save every line in array with freeIndex iteration
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                // Save info from all not-empty tables with PUT method
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        // Get number of all written data elements
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName);
        rsCount.next();
        // Save than number to integer variable
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    // Get List of existing tables
    @Override
    public String[] getTableNames() {
            // try to catch SQL errors
        try {
            Statement stmt = connection.createStatement();
                // Select all tables
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM " +
                    "information_schema.tables WHERE table_schema = 'public' AND " +
                    "table_type = 'BASE TABLE'");
                // Create empty 100  String array
            String[] tables = new String[100];
            int index = 0;
                // Put info inside the array, call every time ResultSet
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
                // Save only not empty fields in the array
            tables = Arrays.copyOf(tables, index, String[].class);
            stmt.close();
            rs.close();
                // Return list of tables upstairs
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
                // If Error, return empty array
            return new String[0];
        }
    }

        // Connection to Database
    @Override
    public void connect(String database, String userName, String password) {
            // Try to catch ClassNotFoundException errors
        try {
                // Connect to jdbc driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
                // If Error - get message
            throw new RuntimeException("Please add jdbc jar to project", e);
        }
            // Try to catch SQLException errors
        try {
                // Make connection to current DB with login / password
            connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5434/" + database,
                        userName, password);
        } catch (SQLException e) {
            // close connection
            connection = null;
            // If errors - get message
            throw new RuntimeException(
                    String.format("Can't get connection for model:%s user:%s",
                            database, userName), e);
        }
    }

    @Override
    public void clear(String tableName) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM public." + tableName);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        try {
            Statement stmt = connection.createStatement();

            String tableNames = getNamesFormatted(input, "%s,");
            String values = getValuesFormatted(input, "'%s',");

            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ")" +
                    "VALUES (" + values + ")");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getValuesFormatted(DataSet input, String format) {
        String values = "";
        for (Object value: input.getValues()) {
            values += String.format(format, value);
        }
        // delete last symbol by substring
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        try {
            String tableNames = getNamesFormatted(newValue, "%s = ?,");

            PreparedStatement ps = connection.prepareStatement("UPDATE public." +
                    tableName + " SET " + tableNames + " WHERE id = ?");
            int index = 1;
            for (Object value: newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setInt(index, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getTableColumns(String tableName) {
        // try to catch SQL errors
        try {
            Statement stmt = connection.createStatement();
            // Select all tables
            ResultSet rs = stmt.executeQuery("SELECT * FROM " +
                    "information_schema.columns WHERE table_schema = 'public' AND " +
                    "table_name = '" + tableName + "'");
            // Create empty 100  String array
            String[] tables = new String[100];
            int index = 0;
            // Put info inside the array, call every time ResultSet
            while (rs.next()) {
                tables[index++] = rs.getString("column_name");
            }
            // Save only not empty fields in the array
            tables = Arrays.copyOf(tables, index, String[].class);
            stmt.close();
            rs.close();
            // Return list of tables upstairs
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            // If Error, return empty array
            return new String[0];
        }
    }

    private String getNamesFormatted(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }
}