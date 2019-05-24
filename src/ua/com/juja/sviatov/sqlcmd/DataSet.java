package ua.com.juja.sviatov.sqlcmd;

/**
 * Created by anton.sviatov on 24.05.2019.
 */
public class DataSet {

    // Inner Class
    static class Data {
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public Data[] data = new Data[100]; // TODO remove magic number
    public int index = 0;

    public void put(String name, Object value) {
        data[index++] = new Data(name, value);
    }

    public Object[] getValues() {
        Object[] result = new Object[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public String[] getNames() {
        String[] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }
}
