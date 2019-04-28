package app.gian.db.informix;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import app.gian.db.BasicDAO;
import app.gian.db.DataBase;
import app.gian.util.BeanUtil;



public class CostxdaibfDao extends BasicDAO
{
	public CostxdaibfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public String findByProd(String compcode,String prodnubr) throws SQLException
	{
		Connection conn = null;
		String avgxcost = "0";		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT avgxcost  ")   
		   .append("   FROM costxdaibf where compcode='"+compcode+"' and prodnubr='"+prodnubr+"'" )
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			if(rs.next())
			{
				avgxcost = rs.getString(1);
			}
			
		}
		catch (SQLException e)
		{
			System.out.println("CostxdaibfDao.findByProd() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return avgxcost;			   
	}
}