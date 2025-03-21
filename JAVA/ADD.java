import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.*;


public class ADD {

	
	public static void main( String args[] )
	  {
		
		ArrayList<Object> gpt_req = new ArrayList<Object>();
		
		//對話儲存放這邊
		
        gpt_req.addAll(Arrays.asList("cookie",10,TimeZone.getTimeZone("Asia/Taipei")));
		
		
	    Connection c = null;
	    Statement stmt = null;
	    try {
	    	
	    		
	    	  Class.forName("org.sqlite.JDBC");
	          c = DriverManager.getConnection("jdbc:sqlite:test.db");
	          c.setAutoCommit(false);
	          
	          
	          
	          stmt = c.createStatement();
	    


	            // 設置 SimpleDateFormat 和 TimeZone
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            TimeZone tz = (TimeZone) gpt_req.get(2);  // 顯式轉換 gpt_req.get(2) 為 TimeZone
	            sdf.setTimeZone(tz);  // 設置 SimpleDateFormat 使用 TimeZone

	            // 獲取當前時間並格式化
	            String formattedTime = sdf.format(new Date()); 

	          
	          
	          
	          String sql =  String.format("INSERT INTO COMPANY (ID, NAME, COST, TIME) VALUES ( %d , '%s', %d, '%s')" , 0 , gpt_req.get(0) , gpt_req.get(1) , formattedTime );
	          
	
	          stmt.executeUpdate(sql);
	          
	          
	          
	          
	          
	          
	          stmt.close();
	          c.commit();
	          c.close();
	        } catch ( Exception e ) {
	          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	          System.exit(0);
	        }
	        System.out.println("Records created successfully");
	    	
	    }
	  
	    

}

