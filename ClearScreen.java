// Program to demonstrate very basic terminal screen manipulation.
// I found a basic program on the Internet, and made a few small changes.
// E.E. Buckalew

import java.io.*;

public class ClearScreen {

public static final char ESC = 27;

public static void clear() {
    Console c = System.console();
    if (c == null) {
        System.err.println("no console");
        System.exit(1);
    }
    c.writer().print(ESC + "[2J");
    c.flush();
}

}