package app.gian.db.informix;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import app.gian.db.BasicDAO;
import app.gian.db.DataBase;
import app.gian.util.BeanUtil;

public class AdjustDAO extends BasicDAO
{
	
	public AdjustDAO(DataBase db)
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
		sql.append(" SELECT *,")
		   .append(" (SELECT username FROM profile WHERE loginid=applier)  modusername ")
		   .append("   FROM adjustmf where compcode='"+compcode+"' " )
		   .append("    AND stat='08'")
		   .append(" AND DATE_FORMAT(modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d')")
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
			System.out.println("AdjustDAO.findListMf() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;		
	}
	/**
	 * Àô«Oµ|
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
		sql.append(" SELECT adjustmf.*,")
		   .append(" (SELECT username FROM profile WHERE loginid=applier)  modusername ")
		   .append("   FROM adjustmf,adjustdf  where adjustmf.compcode='"+compcode+"' " )
		   .append(" AND adjustmf.adnubr = adjustdf.adnubr ")
		   .append("    AND adjustmf.stat='08' ")
		   .append(" AND DATE_FORMAT(adjustmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d')")
		   .append("and adjustdf.locxcode not in('JTB','JTD','JTE')")
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
			System.out.println("AdjustDAO.findListMf() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;		
	}
	
	public List findListDf(String adnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT adjustdf.*,prodinfobf.prodname")
		   .append("   FROM adjustdf,prodinfobf where adjustdf.prodnubr= prodinfobf.prodnubr and adnubr='"+adnubr+"' " )
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
			System.out.println("AdjustDAO.findListDf() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;		
	}
	
	public int findAdjustSum(String adnubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		int qty=0;
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(quantity) qty from adjustdf where adnubr='"+adnubr+"'")   
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
			System.out.println("AdjustDAO.findAdjustSum() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return qty;			   
	}
}
