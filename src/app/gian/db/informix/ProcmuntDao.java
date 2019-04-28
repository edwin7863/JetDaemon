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



public class ProcmuntDao extends BasicDAO
{
	public ProcmuntDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List findListMf(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *,(select username from profile where loginid=procumntmf.regiuser) as modiname, ")
		   .append(" (SELECT vndrabbr FROM supplierbf where vndrcode=procumntmf.vndrcode) as vndrabbr ")
		   .append("   FROM procumntmf where DATE_FORMAT(modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d') AND compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" and trancode='09' and datastat in('06','10','11') ")//借入單
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
			System.out.println("ProcmuntDao.find() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
/**
 * 環保稅版
 * @param compcode
 * @param transDate
 * @return
 * @throws SQLException
 */
	public List findListMfH(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *,(select username from profile where loginid=procumntmf.regiuser) as modiname, ")
		   .append(" (SELECT vndrabbr FROM supplierbf where vndrcode=procumntmf.vndrcode) as vndrabbr ")
		   .append("   FROM procumntmf where DATE_FORMAT(regidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d') AND compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" and trancode='09' and datastat in('06','10','11') ")
		   .append(" and locxcode not in('JTB','JTD','JTE') ")//借入單
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
			System.out.println("ProcmuntDao.find() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findListDf(String poxxnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT procumntdf.prodnubr,procumntdf.quantity,procumntmf.locxcode,prodinfobf.prodname ")
		   .append(" FROM procumntdf,procumntmf,prodinfobf ")
		   .append(" where procumntdf.poxxnubr=procumntmf.poxxnubr ")
		   .append(" and procumntdf.prodnubr=prodinfobf.prodnubr and procumntdf.poxxnubr ='")
		   .append(poxxnubr)
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
			System.out.println("ProcmuntDao.find() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public int findProcSum(String poxxnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		int qty=0;
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(quantity) qty from procumntdf where poxxnubr='"+poxxnubr+"'")   
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			if(rs.next())
				qty=rs.getInt("qty");
			
			
		}
		catch (SQLException e)
		{
			System.out.println("ProcmuntDao.ProcmuntDao() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return qty;			   
	}
}