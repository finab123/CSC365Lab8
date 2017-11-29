/*
 * This sample can be used to check the JDBC driver version.
 * It also illustrates password masking (available with Java 6.0.)
 * This version masks the password.
 * Dr. M. Liu   with revisions by E.E. Buckalew
 */
import java.sql.*;
import java.io.Console;

class Version2 {
   public static void main (String args []) {
      String userID = null;
      char[ ] pword = null;   	

      try {
	 // Load the mysql JDBC driver
	 Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	 System.out.println ("Driver class found and loaded.");   
      }
      catch (Exception ex) {
         System.out.println("Driver not found");
         System.out.println(ex);
      };



	 // get the userID and password
	 Console console = System.console();
	 userID = console.readLine("Enter your mySQL userID: ");
	 pword = console.readPassword("Enter your mySQL password: ");

	 // now connect to mySQL
	 System.out.println ("Connecting...");
	 Connection conn =  DriverManager.getConnection(
	   "jdbc:mysql://cslvm74.csc.calpoly.edu/students?"
	   + "user=" + userID + "&password=" + new String(pword));
	 System.out.println ("connected.");

	 // Create mySQL DatabaseMetaData object
	 DatabaseMetaData meta = conn.getMetaData( );
	 System.out.println ("JDBC driver version is " +
	    meta.getDriverVersion());

	 // Close the connection
	 conn.close();
      } //end try
      catch (Exception ex) {
	 ex.printStackTrace( ); // for debugging
      }
   } //end main
} //end class Version2
