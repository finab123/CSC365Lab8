// Sample JDBC code illustrating simple SQL injection but ostensibly
// for the purpose of selecting a single tuple using a PreparedStatement
// object with a parameter. 
// Will create and drop a table called employees in your database.
// If you already have such a table, this program will fail, most ungracefully.
// E.E. Buckalew (ok, so yeah, I borrowed sections from Dr. M. Liu)
import java.io.*;
import java.util.*;
import java.sql.*;

class simpleSQLi {
   public static void main (String args []) {
      Console console;
      Connection conn = null;
      ResultSet rset = null;
      PreparedStatement pstmt = null;
      String query = null; 
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
	 System.out.print("Connecting...");
	 conn =  DriverManager.getConnection(
	    "jdbc:mysql://cslvm74.csc.calpoly.edu/" + userID + "?"
	    + "user=" + userID + "&password=" + new String(pword));
	 System.out.println ("connected.");

	 // now we're going to create a table and populate it
	 try {
	    // put our CREATE TABLE statement into a String
	    String table = "CREATE TABLE employees ("
	       + "eno INTEGER, "
	       + "ssn INTEGER, "
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
	    String psText = "INSERT INTO employees "
	       + "VALUES(?,?,?,?,STR_TO_DATE(?,'%Y-%m-%d'))";
	    PreparedStatement ps = conn.prepareStatement(psText);

	    // instantiate the parameters for the first tuple and execute
	    ps.setInt(1, 1000);       // eno
	    ps.setInt(2, 123456789);  // ssn
	    ps.setString(3,"Jones");  // ename
	    ps.setInt(4,67226);       // zip
	    ps.setString(5,"1995-12-12"); // hireDate
	    ps.executeUpdate();

	    // instantiate the parameters for the second tuple and execute
	    ps.setInt(1, 1001);
	    ps.setInt(2,234567890);
	    ps.setString(3,"Smith");
	    ps.setInt(4,60606);
	    ps.setString(5,"1992-01-01");
	    ps.executeUpdate();

	    // instantiate the parameters for the third tuple and execute
	    ps.setInt(1, 1002);
	    ps.setInt(2,345678901);
	    ps.setString(3,"Braun");
	    ps.setInt(4,50302);
	    ps.setString(5,"1994-09-01");
	    ps.executeUpdate();

	    // close the PreparedStatement object
	    ps.close();

	 }
	 catch (Exception ee) {
	    System.out.println("ee112: " + ee);
	 }

	 // we will now get an eno with which to search the employees table
	 Scanner sc = new Scanner(System.in);
	 System.out.println("\nEnter <ENO> to list (choices: 1000,1001,1002):");
         String eno = sc.nextLine( );    

	 // we will now select one of the three employees by eno and display
	 query = "select * from employees where ENO = " + eno;
	 pstmt = conn.prepareStatement(query);
	 rset = pstmt.executeQuery();   // NO PARAMETER NEEDED
	 System.out.println("The employee is:");
	 System.out.println("count\tENO    \tSSN\t\tENAME\tZIP\thireDate");
	 boolean f = rset.next(); 
	 while (f) {
	    System.out.print(i + "\t");
	    System.out.print (rset.getString ("eno") + "\t");
	    System.out.print (rset.getString ("ssn") + "\t");
	    System.out.print (rset.getString ("ename") + "\t");
	    System.out.print (rset.getString ("zip") + "\t");
	    System.out.print (rset.getString ("hireDate") + "\t");
	    System.out.println( );
	    i++;
	    f=rset.next();
	 }
	 System.out.println(
	    "\nThis is where the simple SQL injection comes in.\n"
	    + "You will again be given a choice of ENO values,\n"
	    + "but instead, you will deviate from the choices and enter:\n"
	    + "\t1007 OR 1=1 -- \n"
	    + "\t7 OR hireDate LIKE '%' --\n"
	    + "\t1 OR SSN LIKE '%' --\n");

	 // we will now get an eno with which to search the employees table
	 System.out.println("\nEnter <ENO> to list (choices: 1000,1001,1002):");
         eno = sc.nextLine( );    

	 // we will now select one of the three employees by eno and display
	 query = "select * from employees where ENO = " + eno;
	 pstmt = conn.prepareStatement(query);
	 rset = pstmt.executeQuery();   // NO PARAMETER NEEDED
	 System.out.println("The employee is:");
	 System.out.println("count\tENO    \tSSN\t\tENAME\tZIP\thireDate");
	 i=0;
	 f = rset.next(); 
	 while (f) {
	    System.out.print(i + "\t");
	    System.out.print (rset.getString ("eno") + "\t");
	    System.out.print (rset.getString ("ssn") + "\t");
	    System.out.print (rset.getString ("ename") + "\t");
	    System.out.print (rset.getString ("zip") + "\t");
	    System.out.print (rset.getString ("hireDate") + "\t");
	    System.out.println( );
	    i++;
	    f=rset.next();
	 }

	 // Now why don't we just go ahead and drop the table?
	 String table = "DROP TABLE employees"; 
	 Statement s1 = conn.createStatement();
	 s1.executeUpdate(table);
      
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
