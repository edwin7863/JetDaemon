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


public class InvoiceDAO  extends BasicDAO{

	public InvoiceDAO(DataBase db) 
	{
			this.db = db;
	}
	
	/**
	 * 查出進項發票
	 * @return
	 * @throws SQLException
	 */
	public List findInInvoice(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT i.*,s.vndrname,s.unifnubr,s.vndraddr, p.username ") 
		   .append(" FROM invodatamf i,profile p, supplierbf s ")
		   .append(" where i.custxxid = s.vndrcode AND i.regiuser=p.loginid AND inouttype='2' AND i.invotype<>'D' " )
		   .append(" AND i.compcode='").append(compcode)
		   .append("' AND DATE_FORMAT(i.regidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("InvoiceDAO.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;
	}
	
	/**
	 * 查出銷項發票
	 * @return
	 * @throws SQLException
	 */
	public List findOutInvoice(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT i.*,s.custname,s.serialno,s.compaddr, p.username, ") 
		   .append("(select seqt from invxgroup where invxtype=i.invostat and i.inouttype=3 and i.invogrup=groupnumber ")
		   .append(" AND  CAST(substr(i.invonubr,3,9) AS UNSIGNED) >= CAST(substr(begxnubr,3,9) AS UNSIGNED)")
		   .append(" AND  CAST(substr(i.invonubr,3,9) AS UNSIGNED) <= CAST(substr(endxnubr,3,9) AS UNSIGNED)")
		   .append(" AND invxyear = DATE_FORMAT(i.invodate,'%Y')) seqt ")
		   .append(" FROM invodatamf i,profile p, custinfomf s  ")
		   .append(" where i.custxxid = s.custxxid AND i.regiuser=p.loginid AND inouttype='3' AND i.invotype='P' " )
		   .append(" AND i.compcode='").append(compcode)
		   .append("' AND DATE_FORMAT(i.modidate,'%Y-%m-%d') = DATE_FORMAT( '" )
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
			System.out.println("InvoiceDAO.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;
	}
}
