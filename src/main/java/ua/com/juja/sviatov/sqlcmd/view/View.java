package ua.com.juja.sviatov.sqlcmd.view;

/**
 * Created by anton.sviatov on 26.09.2019.
 */
public interface View {

    void write(String message);
    String read();

}
