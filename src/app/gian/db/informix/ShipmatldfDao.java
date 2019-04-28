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



public class ShipmatldfDao extends BasicDAO
{
	public ShipmatldfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List findByNumber(String shipnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT s.*,b.prodname  ")   
		   .append("   FROM shipmatldf s, prodinfobf b where shipnubr='"+shipnubr+"' and s.prodnubr=b.prodnubr" )
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			list = BeanUtil.getDynaBean(rs);
			
		}
		catch (SQLException e)
		{
			System.out.println("ShipmatldfDao.findByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public String findSumByNumber(String shipnubr) throws SQLException
	{
		Connection conn = null;
		String sum="0";
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT sum(quantity) as sum ")   
		   .append("   FROM shipmatldf where shipnubr='"+shipnubr+"'" )
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			if(rs.next())
			{
				sum=rs.getString("sum");
			}
		}
		catch (SQLException e)
		{
			System.out.println("ShipmatldfDao.findSumByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return sum;			   
	}
}