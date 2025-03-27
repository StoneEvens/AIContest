import java.sql.*;

public class sql_Build_new
{
	
public void newt( String args[] )
  {
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      
      // String nextTable = "T1";  // 預設第一個表名
      
 
      c = DriverManager.getConnection("jdbc:sqlite:T1.db");
      System.out.println("Opened database successfully");
      stmt = c.createStatement();
      
      
      
      /* 用不到
      
      ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name LIKE 'T%' ORDER BY CAST(SUBSTR(name, 2) AS INTEGER) DESC LIMIT 1");
      int latestNumber = 0;
      if (rs.next()) {
          String latestTable = rs.getString("name");
          try {
          latestNumber = Integer.parseInt(latestTable.substring(1));
          } catch ( Exception e ) {latestNumber = 0;}

      }
      nextTable = "T"+(latestNumber + 1) ;
      
      
      
      System.out.println(nextTable);
       	*/ 


      String sql = String.format("CREATE TABLE %s  " +
                   "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                   " NAME           TEXT    NOT NULL, " + 
                   "COST            INT     NOT NULL, " + 
                   "YEAR        INT,"+
                   "MONTH 		INT," + 
                   "DATE		INT)","T1"); 
      
      
      
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