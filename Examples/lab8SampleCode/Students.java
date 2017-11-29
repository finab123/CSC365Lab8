// Sample JDBC code illustrating the execution and processing of a 
// query that does not modify  the database. 
// Dr. M. Liu, with modifications by E.E.Buckalew
import java.io.*;
import java.util.*;
import java.sql.*;

class Students {	
   public static void main (String args []){
      Connection conn = null;
      PreparedStatement  stmt = null;
      ResultSet rset = null; 
      String id = null, pword = null;

      // determine if the proper number of arguments were supplied
      if (args.length != 2){
         System.out.println("USAGE: two arguments required: "
	    + " <ID> <PASSWORD>");
         System.exit(-1);
      }   	  

      try {
	 // Prep the id and password for loading the mysql JDBC driver
         id = args[0].trim();
         pword = args[1].trim();

         // Load the mysql JDBC driver
         Class.forName ("com.mysql.jdbc.Driver").newInstance(); 
         System.out.println ("Driver class found and loaded.");   

	 // Now make the connection
         System.out.println ("Connecting...");
         conn = DriverManager.getConnection(
           "jdbc:mysql://cslvm74.csc.calpoly.edu/students?"
           + "user=" + id + "&password=" + pword);
         System.out.println ("connected.");      
	 
	 // Create a Statement- a PreparedStatement object is more appropriate
         //   than a Statement object
         String query =          	
	    "select * " +
	    "from   list S, teachers T " +
	    "where  S.classroom = T.classroom"; 
         System.out.println("QUERY STRING is:\n" + query);
         stmt = conn.prepareStatement(query);
         	
         // Exceute the query and obtain the result set 
         rset = stmt.executeQuery();
         	
         // Iterate through the result set and print the attribute values
	 // which we will get by attribute name rather than position
         int i = 1; 
         System.out.println("\tFirstName    \tLastName\tgrade"
	    + "\tclassroom\tFirst    \tLast\tclassroom");
         while (rset.next ()){
            System.out.print(i + "\t");
            System.out.print (rset.getString ("FirstName") + "\t");
            System.out.print (rset.getString ("LastName") + "\t");
            System.out.print (rset.getInt ("grade") + "\t");
            System.out.print (rset.getInt ("S.classroom") + "\t");
            System.out.print (rset.getString ("First") + "\t");
            System.out.print (rset.getString ("Last") + "\t");
            System.out.print (rset.getInt ("T.classroom") + "\t");
            System.out.println( );
            i++;
         } //end while
         rset.close( );
      } //end try
      catch (Exception ex){
  	     ex.printStackTrace( );
      }
      finally {
      	 try {
             stmt.close( );
             conn.close( ); 
         }
         catch (Exception ex) {
   	        ex.printStackTrace( );    
   	 }    	
      }    	
  
   } //end main
   
} //end class
