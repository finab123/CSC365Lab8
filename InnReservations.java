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
      }
      
      try {
         Scanner serverScanner = new Scanner(new File("ServerSettings.txt"));
         String url = serverScanner.nextLine();
         String userName = serverScanner.nextLine();
         String password = serverScanner.nextLine();

         conn = DriverManager.getConnection(url + userName + "user=" + userName + 
            "&password=" + password);       
      }
      catch(FileNotFoundException e) {
         System.out.println("File not found.. " + e);
      }
      catch(Exception e) {
         System.out.println("Could not open connection");
         System.exit(-1);
      }
      System.out.println("\nConnected to mySQL!\n");
   }
}
