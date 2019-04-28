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



public class CustinfomfDao extends BasicDAO
{
	public CustinfomfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List find(String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  ")   
		   .append("   FROM custinfomf " )
		   .append("  WHERE DATE_FORMAT(modidate,'%Y-%m-%d') =  DATE_FORMAT( '" )
		   .append(transDate)
		   .append("','%Y-%m-%d') ")
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
			System.out.println("CustinfomfDao.find() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findCustAccount(String transDate,String compcode) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT distinct c.*  ")   
		   .append("  FROM fundbillmf mf,custaccount c " )
		   .append(" WHERE mf.cvid=c.custxxid and mf.flag=c.flag and mf.compcode=c.compcode ")
		   .append("  AND DATE_FORMAT(mf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '" )
		   .append(transDate)
		   .append("','%Y-%m-%d') ")
		   .append(" and mf.compcode= '")
		   .append(compcode)
		   .append("'")
		   .append(" and c.compcode= '")
		   .append(compcode)
		   .append("'")
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
			System.out.println("CustinfomfDao.find() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
}