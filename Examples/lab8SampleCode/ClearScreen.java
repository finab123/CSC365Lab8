// Program to demonstrate very basic terminal screen manipulation.
// I found a basic program on the Internet, and made a few small changes.
// E.E. Buckalew

import java.io.*;

public class ClearScreen {

public static final char ESC = 27;

public static void main(String[] args)
throws Exception {
    Console c = System.console();
    if (c == null) {
        System.err.println("no console");
        System.exit(1);
    }

    // clear screen for the first time
    c.writer().print(ESC + "[2J");
    c.flush();

//  original code that we don't need, but I've left it for you to see
//  just in case it interests you  -- eeb
//    Thread.sleep(200);
//    for (int i = 0; i < 100; ++i) {
//        // reposition the cursor to 1|1
//        c.writer().print(ESC + "[1;1H");
//        c.flush();
//
//        c.writer().println("hello " + i);
//        c.flush();
//
//        Thread.sleep(200);
//    }

      // This is what I added.
      // First, we position the cursor in the upper left corner.
      c.writer().print(ESC + "[1;1H");
      c.flush();

      // Now we print something inane, just to show we can
      // and to see where we are.
      System.out.println("Now is the time\n"
	 + "for all good men\n"
	 + "to come to the aid\n"
	 + "of the party.\n\n");

      // wait so we can see what was printed, (we're slow)
      Thread.sleep(2000);

      // now we clear the screen again and re-position the cursor top left
      // and since we obviously have a penchant for printing inane things,
      // we do that again. At least it is a different inanity.
      c.writer().print(ESC + "[2J");
      c.writer().print(ESC + "[1;1H");
      c.flush();
      System.out.println("\nThe quick brown fox\n"
	 + "jumped over the\n"
	 + "lazy dog.\n\n");

      // wait for everyone to read what we printed, then fool with
      // positioning the cursor (but without clearing the screen this time)
      Thread.sleep(2000);
   // c.writer().print(ESC + "[2J");
      c.writer().print(ESC + "[15;1H");
      c.flush();
      System.out.println("\nThe quick brown fox\n"
	 + "jumped over the\n"
	 + "lazy dog. Again!\n\n");
   } // end main

}
