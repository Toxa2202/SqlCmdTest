package ua.com.juja.sviatov.sqlcmd.view;

import java.util.Scanner;

/**
 * Created by anton.sviatov on 26.09.2019.
 */
public class Console implements View {
    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
