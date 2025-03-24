import java.sql.*;

public class sql_Build_new
{
	
  public static void main( String args[] )
  {
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:test.db");  //--名稱-------
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql = "CREATE TABLE COMPANY " +
                   "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                   " NAME           TEXT    NOT NULL, " + 
                   "COST            INT     NOT NULL, " + 
                   "YEAR        INT,"+
                   "MONTH 		INT," + 
                   "DATE		INT,"+
                   "HOUR		INT,"+
                   "MIN         INT,"+
                   "SEC         INT)"; 
      stmt.executeUpdate(sql);
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Table created successfully");
  }
  
}