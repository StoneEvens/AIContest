import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;





public class CRUD {
	
	

	public void put(String name, int cost) 
	  {
		
		
		
		  Connection db = null;
		  Statement stmt = null;

	    try {
	    	
	    	
	    	//db設定

	    	  Class.forName("org.sqlite.JDBC");
	          db = DriverManager.getConnection("jdbc:sqlite:test.db");
	          db.setAutoCommit(false);
	          
	          stmt = db.createStatement();
	    
	          
	          
	          
	          //時間格式
	          
	            TimeZone tz = TimeZone.getTimeZone("Asia/Taipei"); 
	            Calendar formattedTime = Calendar.getInstance(tz);


	            
	          //存入db
	          String sql =  String.format("INSERT INTO COMPANY (NAME, COST, YEAR,MONTH,DATE,HOUR,MIN,SEC) VALUES ('%s', %d, '%d', '%d', '%d',%d,%d,%d)" 
	        		  ,name, cost , formattedTime.get(Calendar.YEAR),formattedTime.get(Calendar.MONTH),formattedTime.get(Calendar.DATE),
	        		  formattedTime.get(Calendar.HOUR_OF_DAY),formattedTime.get(Calendar.MINUTE),formattedTime.get(Calendar.SECOND));
	          
	
	          stmt.executeUpdate(sql);
	          
	          
	          //結束
	          stmt.close();
	          db.commit();
	          db.close();
	          
	          
	        } catch ( Exception e ) {
	          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	          System.exit(0);
	        }
	        System.out.println("Records created successfully");
	    	
	    }
	  
	
	
	
	
	public ArrayList<Object> search(String time,String eq, int value)
		{
		 Connection db = null;
		    Statement stmt = null;
		    try {
		      Class.forName("org.sqlite.JDBC");
		      db = DriverManager.getConnection("jdbc:sqlite:test.db");
		      db.setAutoCommit(false);
		      
		      
	          stmt = db.createStatement();

		      
	          String sql =  String.format("SELECT * FROM COMPANY WHERE %s %s %d",time,eq,value);
		      ResultSet rs = stmt.executeQuery( sql );
	          
		      
		      ArrayList<Object> rows = new ArrayList<>();

	          
		      while ( rs.next() ) {
		    	  
		    	  
		    	 ArrayList<Object> row = new ArrayList<>();
		    	 
		    	 row.add( rs.getInt("ID")  );
		    	 row.add(rs.getString("NAME") );
		    	 row.add(rs.getInt("COST") );
		    	 row.add(rs.getInt("YEAR"));
		    	 row.add(rs.getInt("MONTH")+1);
		    	 row.add(rs.getInt("DATE"));
		    	 row.add(rs.getInt("HOUR"));
		    	 row.add(rs.getInt("MIN"));
		    	 row.add(rs.getInt("SEC"));
		    	 rows.add(row);

		         System.out.println();
		    	  
		      }
		      
		      
		      
		      stmt.close();
		      db.close();
		      System.out.println("Operation done successfully");
			  return rows; 
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
			  return null; 
		    }
		    
		      
	}
	    

	
	
	
	
	
	
	
	
	
	
	
	
	

	
	  public void edit(String item, Object value,int id)
	  {
		  
	    Connection db = null;
	    Statement stmt = null;
	    

	    try {
	    	
	      Class.forName("org.sqlite.JDBC");
	      db = DriverManager.getConnection("jdbc:sqlite:test.db");
	      db.setAutoCommit(false);


	      
	      String sql;
	      
	      stmt = db.createStatement();
	      if (item == "NAME") {
	      sql = String.format("UPDATE COMPANY set %s = '%s' where ID = %d;",item,value,id);
	      }else{sql = String.format("UPDATE COMPANY set %s = %d where ID = %d;",item,value,id);
	      }
	      stmt.executeUpdate(sql);
	      db.commit();

	     
	      
	   
	      stmt.close();
	      db.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Operation done successfully");
	  }
	  
	  
	  
	  
	  
	  
	  

	    public void delet(int id)
	    {
	    	
	      Connection db = null;
	      Statement stmt = null;
	      try {
	        Class.forName("org.sqlite.JDBC");
	        db = DriverManager.getConnection("jdbc:sqlite:test.db");
	        db.setAutoCommit(false);


	        stmt = db.createStatement();
	       
	        String sql = String.format("DELETE from COMPANY where ID= %d;",id);

	        stmt.executeUpdate(sql);
	        db.commit();

	        
	        stmt.close();
	        db.close();
	        
	      } catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	      }
	      System.out.println("Operation done successfully");
	    }
	  
	  
	  
	  
	  
	  
	  
	  
	  
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	








