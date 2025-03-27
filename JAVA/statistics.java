import java.util.*;


public class statistics {

	
	
	
	public int total(String item, int value)
	{
		String strValue = String.valueOf(value);
		
		CRUD tool = new CRUD();
		
		ArrayList<ArrayList<Object>>  row = tool.search(item,"=",strValue);
		int plus = 0;
        for (int i = 0; i < row.size(); i++) 
        {
        	plus += (Integer) row.get(i).get(2);        	
        	
        }
	    System.out.println(item+":"+value+" total:"+plus);

		return plus;
		}
	
	
}
