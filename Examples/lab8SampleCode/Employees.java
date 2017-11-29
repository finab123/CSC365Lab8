//  Sample JDBC code illustrating making a table with three tuples
//  for use in the Select and Select2 JDBC sample programs.
//  We use a PreparedStatement object.
//  E.E. Buckalew (ok, so yeah, I borrowed sections from Dr. M. Liu)
import java.io.*;
import java.util.*;
import java.sql.*;

class Employees {	
   public static void main (String args []) {
      Console console;
      Connection conn = null;
      ResultSet rset = null;
      PreparedStatement pstmt = null;
      String query = null, line = null; 	
      String userID = null;
      char[ ] pword = null;   	
      int i = 0;

      try {
	 // Load the mysql JDBC driver
	 Class.forName("com.mysql.jdbc.Driver").newInstance();
	 System.out.println ("Driver class found and loaded.");

	 // get the userID and password (with masking)
	 console = System.console();
	 userID = console.readLine("Enter your mySQL userID: ");
	 pword = console.readPassword("Enter your mySQL password: ");

	 // now connect to mySQL with user's own database
	 System.out.println ("Connecting...");
	 conn =  DriverManager.getConnection(
	    "jdbc:mysql://cslvm74.csc.calpoly.edu/" + userID + "?"
	    + "user=" + userID + "&password=" + new String(pword));
	 System.out.println ("connected.");

	 // now we're going to create a table and populate it
	 try {
	    // put our CREATE TABLE statement into a String
	    String table = "CREATE TABLE employees ("
	       + "eno INTEGER, "
	       + "ename VARCHAR(25), "
	       + "zip INTEGER, "
	       + "hireDate DATE, "
	       + "PRIMARY KEY (eno))";

	    // here we get our Statement and execute our CREATE TABLE statement
	    Statement s1 = conn.createStatement();
	    s1.executeUpdate(table);

	 }
	 catch (Exception ee) {
	    System.out.println("ee96: " + ee);
	 }

	 // put three tuples into employees with parameterized PreparedStatement
	 // we'll put hireDate in as a String and convert with STR_TO_DATE
	 try {
	    String psText =
	       "INSERT INTO employees VALUES(?,?,?,STR_TO_DATE(?,'%Y-%m-%d'))";
	    PreparedStatement ps = conn.prepareStatement(psText);

	    // instantiate the parameters for the first tuple and execute
	    ps.setInt(1, 1000);
	    ps.setString(2,"Jones");
	    ps.setInt(3,67226);
	    ps.setString(4,"1995-12-12");
	    ps.executeUpdate();

	    // instantiate the parameters for the second tuple and execute
	    ps.setInt(1, 1001);
	    ps.setString(2,"Smith");
	    ps.setInt(3,60606);
	    ps.setString(4,"1992-01-01");
	    ps.executeUpdate();

	    // instantiate the parameters for the third tuple and execute
	    ps.setInt(1, 1002);
	    ps.setString(2,"Braun");
	    ps.setInt(3,50302);
	    ps.setString(4,"1994-09-01");
	    ps.executeUpdate();

	    // close the PreparedStatement object
	    ps.close();

	 }
	 catch (Exception ee) {
	    System.out.println("ee112: " + ee);
	 }

	 // we will now get an eno with which to search the employees table
	 Scanner sc = new Scanner(System.in);
	 System.out.println("Enter <ENO> to list:");
	 String eno = sc.next( );

	 // we will now select one of the three employees by eno and display
	 query = "select * from employees where ENO = " + eno;
	 pstmt = conn.prepareStatement(query);
	 rset = pstmt.executeQuery();   // NO PARAMETER NEEDED
	 System.out.println("The employee is:");	
	 System.out.println("\tENO    \tENAME\tZIP\thireDate");
	 rset.next( );
	 System.out.print("\t");
	 System.out.print (rset.getString ("eno") + "\t");
	 System.out.print (rset.getString ("ename") + "\t");
	 System.out.print (rset.getString ("zip") + "\t");
	 System.out.print (rset.getString ("hireDate") + "\t");
	 System.out.println( );
      } //end try

      catch (Exception ex) {
	 ex.printStackTrace( );
      }
      finally {
	 try {
	    pstmt.close( );
	 }
	 catch (Exception e){}
	 try {
	    rset.close( );
	 }
	 catch (Exception e){}
	 try {
	    conn.close( );
	 }
	 catch (Exception e){}	
      } // end finally   	

   } // end main

} // end class
