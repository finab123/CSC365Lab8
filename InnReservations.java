/* CSC 365 -- Lab 8
 * Fina Beauchamp (smbeauch) and Brandon Newby
 */
import java.io.*;
import java.util.*;
import java.sql.*;

public class InnReservations {
   private static Connection conn = null;
   public static void main(String[] args) {
       String url = "";
       String userName = "";
       String password = "";

       //Load the mysql JDBC driver
       try {
           Scanner serverScanner = new Scanner(new File("ServerSettings.txt"));
           url = serverScanner.nextLine();
           userName = serverScanner.nextLine();
           password = serverScanner.nextLine();
      }
      catch(FileNotFoundException e) {
         System.out.println("File not found.. " + e);
         System.exit(-1);
      }
      establishConnection(url, userName, password);
   
      checkTable(userName, "reservations");
      checkTable(userName, "rooms");

      Scanner inputScanner = new Scanner(System.in);
      printMenuOptions(); 

      String option;
      boolean isValid = true;
      while(isValid) {
          option = inputScanner.next();
          switch(option) {
            case "0":
                printMenuOptions();
                break;
            case "1":
               System.out.println("ADmin stuff..");
               AdminMenu menu = new AdminMenu(conn);
               menu.startMenu();
               ClearScreen.clear();
               printMenuOptions();
               break;
            case "2":
               System.out.println("Owner...");
               ClearScreen.clear();
               printMenuOptions();
               break;
            case "3":
                System.out.println("Guest...");
                ClearScreen.clear();
                printMenuOptions();
                break;
            case "4":
                System.out.println("Exiting...");
                System.exit(0);
            default:
               System.out.println("Enter a valid option...");
               break;
         }
      }
      
   }

   private static void establishConnection(String url, String userName, String password) {
      try {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         System.out.println("Driver class found and loaded.");
      }
      catch (Exception e) {
         System.out.println("Driver not found... " + e);
      };

      try {
         String connectionString = url + userName + "?user=" + userName +
            "&password=" + password;
         System.out.println(connectionString);

         conn = DriverManager.getConnection(connectionString);
      }
      catch(Exception e) {
         System.out.println("Could not open connection");
         System.exit(-1);
      }
      System.out.println("\nConnected to mySQL!\n");
   }

   private static void checkTable(String userName, String tableName) {
      try {
         //Check if the tables exist..
         Statement s = conn.createStatement();
         ResultSet r = s.executeQuery("SELECT table_name FROM information_schema.tables"
	   + " WHERE table_schema = '" + userName+"' AND table_name = '" +tableName+"'");
         r.last();
         //table does not exist, create it. 
         if(r.getRow() == 0) {
            String table = "CREATE TABLE " + tableName;
            if(tableName.equals("reservations")) {
              table += "(code char(30) PRIMARY KEY, id CHAR(3) REFERENCES ROOMS(id), checkIn DATE, checkOut DATE, " +
                    "rate DECIMAL(6, 2), lastName char(30), firstName char(30), adults TINYINT, kids TINYINT)";
            }
            else if(tableName.equals("rooms")) {
               table += "(id CHAR(5) PRIMARY KEY, name CHAR(30) UNIQUE, beds TINYINT, bedType char(20), "
                   + "maxOccupancy INTEGER, basePrice DECIMAL(6, 2), decor char(30))";
            }
            s.executeUpdate(table);
         }
      }
      catch(Exception e) {
         System.out.println(e);
      }
   }
   private static void printMenuOptions() {
      System.out.println("Select a sub-system by number: ");
      System.out.println("[0] Show Menu Options");
      System.out.println("[1] Admin");
      System.out.println("[2] Owner");
      System.out.println("[3] Guest");
      System.out.println("[4] Quit");
      System.out.println();
   }
}
