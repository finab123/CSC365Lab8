/* CSC 365 -- Lab 8
 * Fina Beauchamp (smbeauch) and Brandon Newby
 */
import java.io.*;
import java.util.*;

import javax.print.DocFlavor.STRING;

import java.sql.*;

public class AdminMenu {

    private Connection conn = null;
    private Scanner inputScanner = null;

	public AdminMenu(Connection conn) {

        inputScanner = new Scanner(System.in);
		this.conn = conn;
	}

	public static void getDBStatus(Connection conn) {

        System.out.println("Getting DB Status...");
        System.out.println("Database Status: functionNotWritten");
        System.out.println("Reservations: " + getReservationCount(conn));
        System.out.println("Rooms: " + getRoomCount(conn));
	}

    private void switchSubSystem() {
        System.out.println("Switching SubSystem...");
        ClearScreen.clear();
    }

	private static void printMenuOptions() {

        
        System.out.println("Select an action by number: ");
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
    	printMenuOptions();

    	while(isValid) {
      	  option = inputScanner.next();
          switch(option) {
            case "0":
                printMenuOptions();
                break;
            case "1":
                getDBStatus(conn);
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
          System.out.println("Please Enter Another Command");
        }
        return 0;
    }

    private void displayTable() {
        System.out.println("Displaying Table...");
        


    }

    private void clearDB() {
        System.out.println("Clearing DB...");


    }

    private void createTableRooms() {
        try {
            
            String setup = "CREATE TABLE rooms("

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
 
            System.out.println("ee96: " + e);
        }
    }

    private void createTableReservations() {

        try {
            
            String setup = "CREATE TABLE reservations("

                         + "code INT PRIMARY KEY,"
                         + "room CHAR(3) REFERENCES rooms(id),"
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
 
            System.out.println("ee96: " + e);
        }
    }

    private void populateRooms() {

        try {
            Scanner roomScanner = new Scanner(new File("SQLFiles/Rooms.csv"));
            //remove the first line which holds the column names;
            String line = roomScanner.nextLine();

            String psText = "INSERT INTO rooms VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(psText);

            while (roomScanner.hasNextLine()) {

                line = roomScanner.nextLine();
                String[] args = line.split(",");

                ps.setString(1, args[0]);
                ps.setString(2, args[1]);
                ps.setInt(3, Integer.parseInt(args[2]));
                ps.setString(4, args[3]);
                ps.setInt(5, Integer.parseInt(args[4]));
                ps.setInt(6, Integer.parseInt(args[5]));
                ps.setString(7, args[6]);

                ps.executeUpdate();

            }
       }
       catch(Exception e) {
          System.out.println("Exception " + e);
          System.exit(-1);
       }
        
    }

    private void populateReservations() {
       
    //     try {
    //         Scanner resScanner = new Scanner(new File("SQLFiles/Reservations.csv"));
    //         //remove the first line which holds the column names;
    //         String line = resScanner.nextLine();

    //         String psText = "INSERT INTO reservations VALUES (?,?,?,?,?,?,?,?,?)";
    //         PreparedStatement ps = conn.prepareStatement(psText);

    //         while (resScanner.hasNextLine()) {

    //             line = resScanner.nextLine();
    //             String[] args = line.split(",");

    //             ps.setInt(1, Integer.parseInt(args[0]));
    //             ps.setString(2, args[1]);
    //             ps.setString(3, args[2]);
    //             ps.setString(4, args[3]);
    //             ps.setInt(5, Integer.parseInt(args[4]));
    //             ps.setInt(6, Integer.parseInt(args[5]));
    //             ps.setString(7, args[6]);

    //             ps.executeUpdate();

    //         }
    //    }
    //    catch(Exception e) {
    //       System.out.println("Exception " + e);
    //       System.exit(-1);
    //    }
        
    }

    private void reloadDB() {
        System.out.println("Reloading DB...");
        createTableRooms();
        createTableReservations();
        populateRooms();
        populateReservations();
    }
    

    private void removeDB() {

        System.out.println("Removing DB...");
        try {
            
            String drop = "DROP TABLE reservations, rooms";
            
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(drop);

        } catch (Exception e) {
            System.out.println("Could not drop tables" + e);
        }

    }

    private static int getReservationCount(Connection conn) {

        int numRes = 0;

        try {

            String count = "SELECT COUNT(*) FROM reservations";
            
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(count);
            
            if (rs.next()) {
                numRes = rs.getInt(1);
                //System.out.println(numRes);
            }

        } catch (Exception e) {
            System.out.println("Could not getReservationCount" + e);
        }
        return numRes;
    }

    private static int getRoomCount(Connection conn) {
       
        int numRooms = 0;
        try {

            String count = "SELECT COUNT(*) FROM rooms";
            
            Statement s1 = conn.createStatement();
            ResultSet rs = s1.executeQuery(count);

            if (rs.next()) {
                numRooms = rs.getInt(1);
                //System.out.println(numRooms);
            }

        } catch (Exception e) {
            System.out.println("Could not getRoomCount" + e);
        }
        return numRooms;
    }
}