package app.gian.db.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import app.gian.db.BasicDAO;
import app.gian.db.oracle.model.YgBwsCiCancelTempModel;



public class YgExtPoolingListDao extends BasicDAO
{
	
	
	public int insert(String ciNo , Connection conn) throws SQLException
	{
		Statement s = null;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into yg_ext_pooling_list  values ( ")
		   .append(" ?,?,?,?,?,?,?,?)");
		
		PreparedStatement ptmt=null;
		ptmt = conn.prepareStatement(sql.toString());
		
		try 
		{
			
				ptmt.clearParameters();
				ptmt.setString(1,ciNo);
			
				
				int rst = ptmt.executeUpdate();
				cnt = cnt + rst;
				
		} 
		catch (SQLException e) 
		{
			System.out.println(this.getClass() + "insert() error " + e.toString());
			throw e;
		} 
		finally 
		{
			closeConn();
		}
		return cnt;			   
	}	
	
}
