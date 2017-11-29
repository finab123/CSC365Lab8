/* CSC 365 -- Lab 8
 * Fina Beauchamp (smbeauch) and Brandon Newby
 */
import java.io.*;
import java.util.*;
import java.sql.*;

public class InnReservations {
   private static Connection conn = null;
   public static void main(String[] args) {
       //Load the mysql JDBC driver
      try {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         System.out.println("Driver class found and loaded.");
      }
      catch (Exception e) {
         System.out.println("Driver not found... " + e);
      };
      
      try {
         Scanner serverScanner = new Scanner(new File("ServerSettings.txt"));
         String url = serverScanner.nextLine();
         String userName = serverScanner.nextLine();
         String password = serverScanner.nextLine();
	 String connectionString = url + userName + "?user=" + userName +
            "&password=" + password;
	 System.out.println(connectionString);

         conn = DriverManager.getConnection(connectionString);       
      }
      catch(FileNotFoundException e) {
         System.out.println("File not found.. " + e);
      }
      catch(Exception e) {
         System.out.println("Could not open connection");
         System.exit(-1);
      }
      System.out.println("\nConnected to mySQL!\n");
      Scanner inputScanner = new Scanner(System.in);
      printMenuOptions(); 

      String option;
      boolean isValid = false;
      while(!isValid) {
          option = inputScanner.next();
          switch(option) {
            case "1":
               System.out.println("ADmin stuff..");
               isValid = true;
               break;
            case "2":
               System.out.println("Owner...");
               isValid = true;
               break;
            case "3":
                System.out.println("Guest...");
                isValid = true;
                break;
            case "4":
                System.out.println("Exiting...");
                System.exit(0);
            default:
               System.out.println("Enter a valid option...");
               isValid = false;
               break;
         }
      }
      
   }

   private static void printMenuOptions() {
      System.out.println("Select a sub-system by number: ");
      System.out.println("[1] Admin");
      System.out.println("[2] Owner");
      System.out.println("[3] Guest");
      System.out.println("[4] Quit");
   }
}
