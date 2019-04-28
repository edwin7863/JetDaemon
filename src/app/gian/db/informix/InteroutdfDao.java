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



public class InteroutdfDao extends BasicDAO
{
	public InteroutdfDao(DataBase db) 
	{
			this.db = db;
	}
	
/*	public List findByNumber(String shipnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  ")   
		   .append("   FROM interoutdf where trannubr='"+shipnubr+"'" )
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
			System.out.println("InteroutdfDao.findByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}*/
	
/*	public List findByNumberSaleDist(String trannubr,String shipnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ,(SELECT unitpric FROM shipmatldf WHERE shipnubr='"+shipnubr+"' and prodnubr= d.prodnubr) ounitpric ")
		   .append(" ,quantity*origpric*unitpric dist" )
		   .append(" ,prodname FROM interoutdf d ,prodinfobf b where d.prodnubr=b.prodnubr and  trannubr='"+trannubr+"'" )
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
			System.out.println("InteroutdfDao.findByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}*/
	
	public List findByNumberSale(String trannubr,String shipnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT d.quantity,d.prodnubr,d.trannubr,d.unitpric,d.locxcode,d.avalxqty, ")
		   .append(" d.quantity*d.unitpric amount ,b.prodname,d.seqtnubr,d.origseqtnubr,d.origtrannubr ")
		   .append(" FROM interoutdf d ,prodinfobf b " )
		   .append(" where d.prodnubr=b.prodnubr " )
		   .append(" and d.trannubr='"+trannubr+"' " )
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
			System.out.println("InteroutdfDao.findByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findByNumberRecv(String trannubr,String recvnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT d.quantity,d.prodnubr,d.trannubr,d.unitpric,d.locxcode,d.origxqty,")
		   .append(" d.quantity*d.unitpric amount ,b.prodname,(select d.seqtnubr) as recordno,d.poxxnubr, ")
		   .append(" d.origtrannubr,d.origseqtnubr ")
		   .append(" FROM interoutdf d ,prodinfobf b " )
		   .append(" where d.prodnubr=b.prodnubr " )
		   .append(" and d.trannubr='"+trannubr+"' " )
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
			System.out.println("InteroutdfDao.findByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findByNumberDist(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT d.* ")
		   .append(" ,quantity*unitpric dist" )
		   .append(" ,b.prodname FROM interoutdf d ,prodinfobf b where d.prodnubr=b.prodnubr and  trannubr='"+trannubr+"'" )
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
			System.out.println("InteroutdfDao.findByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public double findDistByNumber(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		double dist=0;
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(quantity*unitpric) dist from interoutdf where trannubr='"+trannubr+"'")   
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			if(rs.next())
				dist=rs.getDouble("dist");
			
			
		}
		catch (SQLException e)
		{
			System.out.println("InteroutdfDao.findDistByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return dist;			   
	}
	
	public List findReturnByNumber(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		double dist=0;
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(quantity*unitpric) amount ,sum(quantity) qty from interoutdf where trannubr='"+trannubr+"'")   
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
			System.out.println("InteroutdfDao.findDistByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public int findSumqtyByNumber(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		int qty=0;
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(quantity) sumqty from interoutdf where trannubr='"+trannubr+"'")   
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			if(rs.next())
				qty=rs.getInt("sumqty");
			
			
		}
		catch (SQLException e)
		{
			System.out.println("InteroutdfDao.findDistByNumber() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return qty;			   
	}
	
	public List findByNumberRetBorrow(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT d.*,(origxqty-quantity) remqty,prodname  ")
		   .append(" FROM interoutdf d,prodinfobf b " )
		   .append(" where d.prodnubr=b.prodnubr ")
		   .append(" and  trannubr='"+trannubr+"'" )
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
			System.out.println("InteroutdfDao.findByNumberRetBorrow() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
}