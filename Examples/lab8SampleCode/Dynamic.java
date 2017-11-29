// Sample code illustrating a justifiable use of Statement object
// for dynamic querying.  Exception handling can be improved.
// This sample displays the first column from one of your tables.
// Dr. M. Liu, with revisions by E.E.Buckalew

import java.io.*;
import java.util.*;
import java.sql.*;

class Dynamic{
   public static void main (String args []){
      Connection conn = null; // for our connection to mySQL
      String userID = null;   // our login userID for mySQL
      char[] pword = null;    // our password for mySQL
      Statement  stmt = null; // a JDBC statement to represent our query
      String query = null;    // we'll build our query in this string
      ResultSet rset = null;  // will be our query result
      int i = 0;              // for numbering our query results; start at 0
      Scanner sc = null;      // for getting our tablename from the keyboard

      // Load the JDBC driver
      try {
         Class.forName ("com.mysql.jdbc.Driver").newInstance();
         System.out.println ("Driver class found and loaded.");

	 // get the userID and password
	 Console console = System.console();
	 userID = console.readLine("Enter your mySQL userID: ");
	 pword = console.readPassword("Enter your mySQL password: ");

  	 // Now make the mySQL connection (userID is both database and user)
  	 // this way you can run it on your own database
	 System.out.println ("Connecting...");
	 conn =  DriverManager.getConnection(
	   "jdbc:mysql://cslvm74.csc.calpoly.edu/" + userID + "?"
	   + "user=" + userID + "&password=" + new String(pword));
	 System.out.println ("connected.");

         // Use keyboard input
         sc = new Scanner(System.in);
         System.out.println("\nEnter table name to display from database "
	    + userID + ": " );
         String tableName = sc.next().trim();

	 // here we build the query in query and then execute it
         // note: A table name cannot be a value-parameter!
         stmt = conn.createStatement();
         query = "select * from " + tableName;
         System.out.println("Query String Is:\n" + query);
	 rset = stmt.executeQuery(query);

	 // and now we process the result. ok, this barely qualifies as
	 // processing, but if you use your imagination...
         System.out.println("Values of first attribute in table "
	    + tableName + ":");
         while (rset.next ()) {  // rset is a ResultSet object
            System.out.println(i + "\t" + rset.getString(1));
            i++;
         } //end while

      } //end try

      catch (Exception ex){
  	     ex.printStackTrace( );
      }
      finally {
      	 try {
             stmt.close( );
             rset.close( );
             conn.close( );
             sc.close( );
         }
         catch (Exception ex) {
   	 }
      } // end finally

   } //end main

} //end class
