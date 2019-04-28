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



public class ReceivexmfDao extends BasicDAO
{
	public ReceivexmfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List find(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *, ")
		   .append(" (SELECT invotype from invodatamf where recvnubr=issunubr) invoicetype , ")
		   .append(" (SELECT invonubr from invodatamf where recvnubr=issunubr) invoice  ")
		   .append("   FROM receivexmf where DATE_FORMAT(modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d') AND compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" and trancode not in('18','19') ")//18為進貨退回入庫單不轉正航
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
			System.out.println("ReceivexmfDao.find() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	/**
	 * 查硬碟的環保稅條件
	 * @param compcode
	 * @param transDateS
	 * @param transDateE
	 * @return
	 * @throws SQLException
	 */
	public List findH(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *, ")
		   .append(" (SELECT invotype from invodatamf where recvnubr=issunubr) invoicetype , ")
		   .append(" (SELECT invonubr from invodatamf where recvnubr=issunubr) invoice  ")
		   .append("   FROM receivexmf where DATE_FORMAT(recvdate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d') ")	   
		   .append(" AND compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" and trancode not in('18','19') and locxcode not in('JTB','JTD','JTE')")//18為進貨退回入庫單不轉正航
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
			System.out.println("ReceivexmfDao.find() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
}