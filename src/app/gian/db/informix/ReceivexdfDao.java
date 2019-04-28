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



public class ReceivexdfDao extends BasicDAO
{
	public ReceivexdfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List findByNumber(String recvnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT r.*,b.prodname, ")
		   .append(" (select distinct poxxnubr from receivexdf,receivexmf where receivexdf.recvnubr=receivexmf.recvnubr ")
           .append(" and trancode='09' and receivexdf.recvnubr=r.recvnubr)as fromno, ")//借轉進貨的來源
           .append(" (select porecordno from receivexdf,receivexmf where receivexdf.recvnubr=receivexmf.recvnubr ")
           .append(" and trancode='09' and receivexdf.recvnubr=r.recvnubr and r.recordno=receivexdf.recordno)as fromrow ")//借轉進貨的來源line
		   .append(" FROM receivexdf r,prodinfobf b where recvnubr='"+recvnubr+"' and r.prodnubr=b.prodnubr " )
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
			System.out.println("ReceivexdfDao.findByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	public String findSumByNumber(String recvnubr) throws SQLException
	{
		Connection conn = null;
		String sum="0";
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT sum(quantity) as sum ")   
		   .append("   FROM receivexdf where recvnubr='"+recvnubr+"'" )
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
			System.out.println("ReceivexdfDao.findSumByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return sum;			   
	}
}