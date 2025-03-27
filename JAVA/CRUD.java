import java.sql.*;
import java.util.*;





public class CRUD {
	
	

	public void put(String name, int cost, String year, String month, String date) 
	  {
		
		
		
		  Connection db = null;
		  Statement stmt = null;

		  
		  
		  
		  
		
	      
		  

	    	
          	try {
          		 
  	    	  Class.forName("org.sqlite.JDBC");
  	          db = DriverManager.getConnection(String.format("jdbc:sqlite:%s.db","T1"));
  	          db.setAutoCommit(false);
          		
      	      stmt = db.createStatement();
      


      	      
      	      
      	      
      	      
      	      
      	      

    	      

	          
	          stmt = db.createStatement();
	    
	          //存入db
	          String sql2 =  String.format("INSERT INTO %s (NAME, COST, YEAR,MONTH,DATE) VALUES ('%s', %d, '%s', '%s', '%s')" 
	        		  ,"T1",name, cost , year, month, date);
	   
	          stmt.executeUpdate(sql2);
	          
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
	  
	
	
	
	
	public ArrayList<ArrayList<Object>>  search(String item,String eq, Object value)
		{
		 Connection db = null;
		    Statement stmt = null;
		    try {
		      Class.forName("org.sqlite.JDBC");
		      db = DriverManager.getConnection("jdbc:sqlite:T1.db");
		      db.setAutoCommit(false);
		      
		      
	          stmt = db.createStatement();

		      
	          String sql =  String.format("SELECT * FROM T1 WHERE %s %s %s",item,eq,value);
		      ResultSet rs = stmt.executeQuery( sql );
	          
		      
		      ArrayList<ArrayList<Object>>  rows = new ArrayList<>();

	          
		      while ( rs.next() ) {
		    	  
		    	  
		    	 ArrayList<Object> row = new ArrayList<>();
		    	 
		    	 row.add( rs.getInt("ID")  );
		    	 row.add(rs.getString("NAME") );
		    	 row.add(rs.getInt("COST") );
		    	 row.add(rs.getInt("YEAR"));
		    	 row.add(rs.getInt("MONTH")+1);
		    	 row.add(rs.getInt("DATE"));

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
	      sql = String.format("UPDATE T1 set %s = '%s' where ID = %d;",item,value,id);
	      }else{sql = String.format("UPDATE T1 set %s = %d where ID = %d;",item,value,id);
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
	       
	        String sql = String.format("DELETE from T1 where ID= %d;",id);

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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	








