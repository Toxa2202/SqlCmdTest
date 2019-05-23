package ua.com.juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.anton.sviatov.DatabaseManager;

import java.sql.Connection;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by anton.sviatov on 23.05.2019.
 */
public class DatabaseManagerTest {
    private DatabaseManager manager;

    @Before
    public void setup() {
        manager = new DatabaseManager();
        manager.connect("sqlcmd", "postgres", "postgres");
    }

    @Test
    public void testGetAllTableNames() {
        String[] tableNames = manager.getTableNames();
        assertEquals("[user, test]", Arrays.toString(tableNames));
    }
}
