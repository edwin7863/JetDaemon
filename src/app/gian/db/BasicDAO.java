package app.gian.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import app.gian.StringUtil;







public class BasicDAO extends Dao
{
	//protected static Category log = Category.getInstance("com.weblink.db");
	protected DataBase db = null;

	protected DataBase getDataBase()
	{
		return this.db;
	}

	protected Connection getConnection() throws SQLException
	{
			return getConnection(this.db);
	}

	public static Connection getConnection(DataBase db) throws SQLException
	{
		return db.getConnection();
	}

	public static String dbEscape(List list)
	{
		StringBuffer buf = new StringBuffer();
		String dot = "";
		for(Iterator itr = list.iterator(); itr.hasNext();)
		{
			Object obj = itr.next();
			if(obj instanceof String)
			{
				buf.append(dot).append(dbEscape((String) obj));
			}
			else if(obj instanceof Number)
			{
				buf.append(dot).append(dbEscape((Number) obj));
			}
			dot = ",";
		}
		return buf.toString();
	}
	
	public static String dbEscape(String value)
	{
		return ("'"+StringUtil.dbEscape(value)+"'");
	}

	public static int dbEscape(Number value,int temp)
	{
		return value.intValue();
	}

	public static int dbEscape(Number value)
	{
		return dbEscape(value,value.intValue());
	}

	public static double dbEscape(Number value,double temp)
	{
		return value.doubleValue();
	}
    
	public void closeConn()
	{
		this.db.closeConn();
	}
}
