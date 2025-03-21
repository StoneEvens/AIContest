import java.sql.*;


public class SEARCH {
	  public static void main( String args[] )
	  {
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test.db");
	      c.setAutoCommit(false);
	      
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
	      
	      
	      while ( rs.next() ) {
	    	  int id = rs.getInt("ID");
	         String  name = rs.getString("NAME");
	         int age  = rs.getInt("COST");
	         String  address = rs.getString("TIME");
	         System.out.println( "ID = " + id );
	         System.out.println( "NAME = " + name );
	         System.out.println( "COST = " + age );
	         System.out.println( "TIME = " + address );
	         System.out.println();
	      }
	      
	      
	      
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Operation done successfully");
	      
	   }
}
