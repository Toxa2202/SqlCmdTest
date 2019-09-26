package ua.com.juja.sviatov.sqlcmd.model;

import java.util.Arrays;

/**
 * Created by anton.sviatov on 26.09.2019.
 */
public class InMemoryDatabaseManager implements DatabaseManager {
    public static final String TABLE_NAME = "user"; // todo implement multitables
    private DataSet[] data = new DataSet[1000];
    private int freeIndex = 0;

    @Override
    public DataSet[] getTableData(String tableName) {
        validateTable(tableName);
        return Arrays.copyOf(data, freeIndex);
    }

    private void validateTable(String tableName) {
        if ("[user]".equals(tableName)) {
            throw new UnsupportedOperationException(
                    "Only for user table, but you try to work with " + tableName);
        }
    }

    @Override
    public String[] getTableNames() {
        return new String[]{TABLE_NAME};
    }

    @Override
    public void connect(String database, String userName, String password) {
        // do nothing
    }

    @Override
    public void clear(String tableName) {
        validateTable(tableName);
        data = new DataSet[1000];
        freeIndex = 0;
    }

    @Override
    public void create(String tableName, DataSet input) {
        validateTable(tableName);
        data[freeIndex] = input;
        freeIndex++;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        validateTable(tableName);
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].get("id") == (Object) id) {
                data[index].updateFrom(newValue);
            }
        }
    }
}
