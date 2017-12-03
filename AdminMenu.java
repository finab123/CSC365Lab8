/* CSC 365 -- Lab 8
 * Fina Beauchamp (smbeauch) and Brandon Newby
 */
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.Date;

import javax.print.DocFlavor.STRING;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AdminMenu {

    private static final String ROOM_TABLE = "testRoom";
    private static final String RES_TABLE = "testRes"; 
    private Connection conn = null;
    private Scanner inputScanner = null;

	public AdminMenu(Connection conn) {

        inputScanner = new Scanner(System.in);
		this.conn = conn;
	}

	public static void getDBStatus(Connection conn) {
        
        int resCount = getReservationCount(conn);
        int roomCount = getRoomCount(conn);
        
        System.out.print("Reservations Status: ");
        if (resCount >= 600) {

            System.out.print("Full");
        } else if (resCount == 0) {
            System.out.print("Empty");
        } else {
            System.out.print("No Database");
        }
        System.out.println(" - Count: " + (resCount == -1 ? 0: resCount));


        System.out.print("Rooms Status: ");
        if (roomCount >= 10) {

            System.out.print("Full");
        } else if (roomCount == 0) {
            System.out.print("Empty");
        } else {
            System.out.print("No Database");
        }
        System.out.println(" - Count: " + (roomCount == -1 ? 0: roomCount));

	}

    private void switchSubSystem() {
        System.out.println("Switching SubSystem...");
        ClearScreen.clear();
    }

	private static void printMenuOptions() {

        
        System.out.println("\nSelect an action by number: ");
        System.out.println("[0] Show Menu Options");  
        System.out.println("[1] Database Status");
        System.out.println("[2] Display Table");
        System.out.println("[3] Clear Database");
        System.out.println("[4] Load/Reload DB");
        System.out.println("[5] Remove Databases");
        System.out.println("[6] Switch Subsystem");
        System.out.println(); 
    }


	public int startMenu() {
 
		String option;
        boolean isValid = true;
        ClearScreen.clear();
        getDBStatus(conn);
    	printMenuOptions();

    	while(isValid) {
      	  option = inputScanner.next();
          switch(option) {
            case "0":
                printMenuOptions();
                break;
            case "1":
                break;
            case "2":
                displayTable();
                break;
            case "3":
                clearDB();
                break;
            case "4":
                reloadDB();
                break;
            case "5":
				removeDB();
                break;
            case "6":
				switchSubSystem();
                isValid = false;
                break;
            default:
               System.out.println("Command Invalid");
               isValid = true;
               break;
          }
          getDBStatus(conn);
          System.out.println("Please Enter Another Command:");
        }
        return 0;
    }

    private void displayRooms() {

        try {
            
            String count = "SELECT * FROM " + ROOM_TABLE;
            
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(count);
            
            while (rs.next()) {
                
                System.out.print(rs.getString(1) + ", ");
                System.out.print(rs.getString(2) + ", ");
                System.out.print(rs.getInt(3) + ", ");
                System.out.print(rs.getString(4) + ", ");
                System.out.print(rs.getInt(5) + ", ");
                System.out.print(rs.getInt(6) + ", ");
                System.out.println(rs.getString(7));

            }

        } catch (Exception e) {

        }


    }

    private void displayReservations() {

        try {

            String count = "SELECT * FROM " + RES_TABLE;
            
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(count);
            
            while (rs.next()) {
                
                System.out.print(rs.getInt(1) + ", ");
                System.out.print(rs.getString(2) + ", ");
                System.out.print(rs.getDate(3) + ", ");
                System.out.print(rs.getDate(4) + ", ");
                System.out.print(rs.getBigDecimal(5) + ", ");
                System.out.print(rs.getString(6) + ", ");
                System.out.print(rs.getString(7) + ", ");
                System.out.print(rs.getInt(8) + ", ");
                System.out.println(rs.getInt(9));

            }

        } catch (Exception e) {
        }
    }
    private void displayTable() {

        System.out.println("\nSelect a table to display or anything else to exit:");
        System.out.println("[0] Display Rooms");
        System.out.println("[1] Display Reservations");

        switch(inputScanner.next()) {
            case "0":
                displayRooms();
                break;
            case "1":
                displayReservations();
                break;
            default:

        }

    }

    private void clearDB() {

        try {
            
            String setup = "DELETE FROM " + ROOM_TABLE;
            
            Statement s1 = conn.createStatement();
            s1.executeUpdate(setup);

            setup = "DELETE FROM " + RES_TABLE;
            s1.execute(setup);
            
        }
        catch (Exception e) {
 
            System.out.println("ee96: " + e);
        }



    }

    private void createTableRooms() {
        try {
            
            String setup = "CREATE TABLE " + ROOM_TABLE + "("

                         + "id CHAR(3) PRIMARY KEY,"
                         + "name CHAR(40),"
                         + "num_beds TINYINT,"
                         + "bed_type CHAR(6),"
                         + "max_occ TINYINT,"
                         + "price INT,"
                         + "decor CHAR(20))";
            
            Statement s1 = conn.createStatement();
            s1.executeUpdate(setup);
            
        }
        catch (Exception e) {
 
        }
    }

    private void createTableReservations() {

        try {
            
            String setup = "CREATE TABLE " + RES_TABLE + "("

                         + "code INT PRIMARY KEY,"
                         + "room CHAR(3) REFERENCES " + ROOM_TABLE + "(id),"
                         + "check_in DATE,"
                         + "check_out DATE,"
                         + "rate DECIMAL(5,2),"
                         + "lName CHAR(30),"
                         + "fName CHAR(20),"
                         + "adults TINYINT,"
                         + "kids TINYINT,"

                         + "UNIQUE (room, check_in, check_out))";
            
            Statement s1 = conn.createStatement();
            s1.executeUpdate(setup);
        }
        catch (Exception e) {
 
        }
    }

    // removes quotes from strings
    private String trim(String in) {
        return in.substring(1, in.length() - 1); 
    }

    private void populateRooms() {

        try {
            Scanner roomScanner = new Scanner(new File("SQLFiles/Rooms.csv"));
            //remove the first line which holds the column names;
            String line = roomScanner.nextLine();

            String psText = "INSERT INTO " + ROOM_TABLE + " VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(psText);

            while (roomScanner.hasNextLine()) {

                line = roomScanner.nextLine();
                String[] args = line.split(",");

                ps.setString(1, trim(args[0]));
                ps.setString(2, trim(args[1]));
                ps.setInt(3, Integer.parseInt(args[2]));
                ps.setString(4, trim(args[3]));
                ps.setInt(5, Integer.parseInt(args[4]));
                ps.setInt(6, Integer.parseInt(args[5]));
                ps.setString(7, trim(args[6]));

                ps.executeUpdate();

            }
       }
       catch(Exception e) {
          //System.out.println("Exception " + e);
          
       }
        
    }

    private void populateReservations() {
       
        try {
            Scanner resScanner = new Scanner(new File("SQLFiles/Reservations.csv"));
            //remove the first line which holds the column names;
            String line = resScanner.nextLine();

            String psText = "INSERT INTO " + RES_TABLE + " VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(psText);

            while (resScanner.hasNextLine()) {

                line = resScanner.nextLine();
                String[] args = line.split(",");

                DateFormat format = new SimpleDateFormat("d-MMM-yy", Locale.ROOT);

                ps.setInt(1, Integer.parseInt(args[0]));
                ps.setString(2, trim(args[1]));
                ps.setDate(3, new java.sql.Date(format.parse(trim(args[2])).getTime()));
                ps.setDate(4, new java.sql.Date(format.parse(trim(args[3])).getTime()));
                ps.setBigDecimal(5, new BigDecimal(args[4]));
                ps.setString(6, trim(args[5]));
                ps.setString(7, trim(args[6]));
                ps.setInt(8, Integer.parseInt(args[7]));
                ps.setInt(9, Integer.parseInt(args[8]));

                ps.executeUpdate();

            }
       }
       catch(Exception e) {
          //System.out.println("Exception " + e);
         
       }
        
    }

    private void reloadDB() {
        
        clearDB();
        createTableRooms();
        createTableReservations();
        populateRooms();
        populateReservations();
        System.out.println("DataBase Reloaded");
    }
    

    private void removeDB() {

        try {
            
            String drop = "DROP TABLE " + RES_TABLE + ", " + ROOM_TABLE;
            
            Statement s1 = conn.createStatement();
            s1.executeUpdate(drop);

        } catch (Exception e) {
            System.out.println("Could not drop tables" + e);
        }
        System.out.println("DataBase dropped");

    }

    private static int getReservationCount(Connection conn) {

        int numRes = 0;

        try {

            String count = "SELECT COUNT(*) FROM " + RES_TABLE;
            
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(count);
            
            if (rs.next()) {
                numRes = rs.getInt(1);
                //System.out.println(numRes);
            }

        } catch (Exception e) {
            numRes = -1;
        }
        return numRes;
    }

    private static int getRoomCount(Connection conn) {
       
        int numRooms = 0;
        try {

            String count = "SELECT COUNT(*) FROM " + ROOM_TABLE;
            
            Statement s1 = conn.createStatement();
            ResultSet rs = s1.executeQuery(count);

            if (rs.next()) {
                numRooms = rs.getInt(1);
                //System.out.println(numRooms);
            }

        } catch (Exception e) {
            numRooms = -1;
        }
        return numRooms;
    }
}