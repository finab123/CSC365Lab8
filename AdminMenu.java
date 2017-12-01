/* CSC 365 -- Lab 8
 * Fina Beauchamp (smbeauch) and Brandon Newby
 */
import java.io.*;
import java.util.*;
import java.sql.*;

public class AdminMenu {

	private static Connection conn = null;

	public AdminMenu(Connection conn) {

		this.conn = conn;
	}

	public static void getDBStatus(Connection conn) {

		System.out.println("Getting DB Status");
	}

    private void displayTable() {
    	System.out.println("Displaying Table");
    }

    private void clearDB() {
    	System.out.println("Clearing DB...");
    }

    private void reloadDB() {
    	System.out.println("Reloading DB...");
    }

    private void removeDB() {
    	System.out.println("Removing DB...");
    }

    private void switchSubSystem() {
    	System.out.println("Switching SubSystem");
    }

	private static void printMenuOptions() {
  	  System.out.println("Select an action by number: ");
      System.out.println("[1] Database Status");
      System.out.println("[2] Display Table");
      System.out.println("[3] Clear Database");
      System.out.println("[4] Load/Reload DB");
      System.out.println("[5] Remove Databases");
      System.out.println("[6] Switch Subsystem");
    }


	public int startMenu() {
 
		Scanner inputScanner = new Scanner(System.in);
		String option;
    	boolean isValid = false;
    	while(!isValid) {
      	  option = inputScanner.next();
          switch(option) {
            case "1":
                System.out.println("Database Status...");
                getDBStatus(conn);
                isValid = true;
                break;
            case "2":
                System.out.println("Display Table...");
                displayTable();
                isValid = true;
                break;
            case "3":
                System.out.println("Clear Database...");
                clearDB();
                isValid = true;
                break;
            case "4":
                System.out.println("Load/Reload DB...");
                reloadDB();
                break;
            case "5":
				System.out.println("Remove Databases...");
				removeDB();
                isValid = true;
                break;
            case "6":
				System.out.println("Switch Subsystem...");
				switchSubSystem();
                isValid = true;
                break;
            default:
               System.out.println("Enter a valid option...");
               isValid = false;
               break;
          }
        }
        return 0;
	}

}