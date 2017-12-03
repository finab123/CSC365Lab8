/*CSC 365 -- Lab 8 */
import java.io.*;
import java.util.*;
import java.sql.*;

public class OwnerMenu {
   private Connection conn;

   public OwnerMenu(Connection conn) {
      this.conn = conn;
   }
   
   public void startMenu() {
      ClearScreen.clear();
      System.out.println(" --- OWNER SUBSYSTEM --- ");
      boolean exit = false;
      printMenuOptions();
      Scanner inputScanner = new Scanner(System.in);
      while(!exit) {
         String option = inputScanner.next();
         switch(option) {
            case "1":
               occupancyOverview();
               printMenuOptions();
               break;
            case "2":
	       viewRevenue();
               printMenuOptions();
               break;
            case "3":
               reviewRooms();
              printMenuOptions();
               break;
            case "4":
               reviewReservations();
               printMenuOptions();
               break;
            case "5":
               reviewReservationInfo();
               printMenuOptions();
               break;
            case "6":
               exit = true;
               break;
            default:
               System.out.println("Please enter a valid input");
               exit = false;
               break;
         }
      }
   }
   private void printMenuOptions() {
      System.out.println("Select a menu option: ");
      System.out.println();
      System.out.println("Menu:");
      System.out.println("[1] Occupancy overview");
      System.out.println("[2] View revenue");
      System.out.println("[3] Review rooms");
      System.out.println("[4] Review reservations");
      System.out.println("[5] Review detailed reservation information");
      System.out.println("[6] Return to main menu");
   }

   private void occupancyOverview() {
      try {
      Statement statement = conn.createStatement();
      ClearScreen.clear();
      ResultSet r;
      System.out.println("---Occupancy Overview---");
      System.out.println("Enter date or time period. ");
      System.out.println("Format dates with Month/Day using numbers.\n Separate time period with slash.");
      Scanner s = new Scanner(System.in);
      String firstDateString = s.next();
      String[] firstDate = firstDateString.split("/");
      System.out.println("Enter the second date or NO if there is no second date");
      String secondDateString = s.next();
      printRooms();
      System.out.println("Enter room code to view reservation");
      String roomCode = s.next();
      if(!secondDateString.toUpperCase().equals("NO")) {
         String[] secondDate = secondDateString.split("/");
         r = statement.executeQuery("Select * from reservations r where Room = '"+ roomCode + 
            "' and ('"+firstDate[1] + "-" + firstDate[0] + "-10' between check_out and check_in OR '" +
             secondDate[1] + "-" + secondDate[0] + "-10' between check_out and check_in)"); 
         while(r.next()) {
            System.out.println("Code: " + r.getString(1));
            System.out.println("Room: " + r.getString(2));
            System.out.println("Check In and Check Out: " + r.getString(3) + "  " + r.getString(4));
            System.out.println("Rate: " + r.getString(5));
            System.out.println("Guest Name: " + r.getString(7) + "  " + r.getString(8));
         }
       }
       // Only process the first date. 
      else {
         r = statement.executeQuery("Select * from reservations r where Room = '"+ roomCode +
            "' and '"+firstDate[1] + "-" + firstDate[0] + "-10' between check_out and check_in");
         while(r.next()) {
            System.out.println("Code: " + r.getString(1));
            System.out.println("Room: " + r.getString(2));
            System.out.println("Check In and Check Out: " + r.getString(3) + "  " + r.getString(4));
            System.out.println("Rate: " + r.getString(5));
            System.out.println("Guest Name: " + r.getString(7) + "  " + r.getString(8));
         }
      }
      }
      catch(SQLException e) {
         System.out.println(e);
      }
   }

   private void printRooms() {
      try {
         Statement statement = conn.createStatement(); 
         ResultSet r = statement.executeQuery("SELECT id, name from rooms");
         while(r.next()) {
            System.out.println("Code: " + r.getString(1) + "  Name: " + r.getString(2));
         }
      }
      catch(SQLException e) {
         System.out.println(e);
      }
   }

   private void viewRevenue() {
      try {
         System.out.println("Enter number for reservations(1), days occupied(2), or revenue(3)");
         Scanner s = new Scanner(System.in);
         String option = s.next();
         System.out.println("|| Room Code | Jan | Feb | March | April | May | June | July | August | Sep. | Nov. | Dec. ||");
         String psText = "select count(*) from reservations where check_out between ? and ? and room = ?";
         PreparedStatement rCountsStatement = conn.prepareStatement(psText);
         String psText2 = "select sum(datediff(check_out, check_in)) from reservations where check_out between ? and ? and room = ?";
         PreparedStatement daysStatement = conn.prepareStatement(psText2);
         String psText3 = "select sum(datediff(check_out, check_in) * rate) from reservations where check_out between ? and ? and room = ?";
         PreparedStatement revenueStatement = conn.prepareStatement(psText3); 

         Statement statement = conn.createStatement();
         ResultSet roomSet = statement.executeQuery("select id from rooms");
	 ArrayList<String> rooms = new ArrayList<>();
         while(roomSet.next()) {
            rooms.add(roomSet.getString(1)); 
         }
         if(option.trim().equals("1")) {
             for(String room : rooms) {
                System.out.println("| " +room+ " | ");
                for(int i = 1; i < 13; i++) {
                   rCountsStatement.setString(1, "01-"+i+"-10");
                   rCountsStatement.setString(2, "31-"+i+"-10");
                   rCountsStatement.setString(3, room);
                   ResultSet r = rCountsStatement.executeQuery();
                   r.next();
                   System.out.print("| " +r.getString(1) + " | ");
                } 
                System.out.println();
             }
         }
         else if(option.trim().equals("3")) {
            for(String room : rooms) {
                System.out.println("| " +room+ " | ");
                for(int i = 1; i < 13; i++) {
                   revenueStatement.setString(1, "01-"+i+"-10");
                   revenueStatement.setString(2, "31-"+i+"-10");
                   revenueStatement.setString(3, room);
                   ResultSet r = rCountsStatement.executeQuery();
                   r.next();
                   System.out.print("| " +r.getString(1) + " | ");
                }
                System.out.println();
             }
         }
         else if(option.trim().equals("2")) {
            for(String room : rooms) {
                System.out.println("| " +room+ " | ");
                for(int i = 1; i < 13; i++) {
                   daysStatement.setString(1, "01-"+i+"-10");
                   daysStatement.setString(2, "31-"+i+"-10");
                   daysStatement.setString(3, room);
                   ResultSet r = rCountsStatement.executeQuery();
                   r.next();
                   System.out.print("| " +r.getString(1) + " | ");
                }
                System.out.println();
             }
         }
      }
      catch(SQLException e) {
         System.out.println(e);
      }
   }

   private void reviewRooms() {
      try {
      System.out.println("Available Rooms...");
      Statement statement = conn.createStatement();
      ResultSet roomSet = statement.executeQuery("select id from rooms");
      ArrayList<String> rooms = new ArrayList<>();
      while(roomSet.next()) {
          String room = roomSet.getString(1);
          rooms.add(room);
          System.out.println(room);
      }
      System.out.println("Select a room to view");
      Scanner s = new Scanner(System.in);
      String roomCode = s.next();
     
      ResultSet r = statement.executeQuery("select * from rooms where id = '"+roomCode+"'");
      while(r.next()) {
         System.out.println("Room Code: " + r.getString(1) + " Name: " + r.getString(2) + " Num beds: " +
           r.getString(3) + " Bed Type: " + r.getString(4) + " Price: " + r.getString(6));
      } 
      }
      catch(SQLException e) {
         System.out.println(e);
      }
   }

   private void reviewReservations() {
      reviewReservations();
   }

   private void reviewReservationInfo() {

   }
}
