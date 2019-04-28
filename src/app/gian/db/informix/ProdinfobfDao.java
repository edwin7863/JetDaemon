package app.gian.db.informix;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;


import app.gian.db.BasicDAO;
import app.gian.db.DataBase;
import app.gian.util.BeanUtil;



public class ProdinfobfDao extends BasicDAO
{
	public ProdinfobfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List<DynaBean> find(String compcode,String transDate ) throws SQLException
	{
		Connection conn = null;
		List<DynaBean> list = new ArrayList<DynaBean>();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT prodinfobf.* , ")
		   .append("(select chicode from catestrubf where catestrubf.catecode =prodinfobf.catex1st ) chicode ");		 
		sql.append(" FROM prodinfobf " )
		   .append(" WHERE DATE_FORMAT(prodinfobf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("ProdinfobfDao.find() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List<DynaBean> findCategory(String compcode,String transDate ) throws SQLException
	{
		Connection conn = null;
		List<DynaBean> list = new ArrayList<DynaBean>();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT catecodebf.catename,catestrubf.chicode ")		 
		   .append(" FROM catecodebf,catestrubf  " )
		   .append(" WHERE catecodebf.catecode=catestrubf.catecode")
		   .append(" AND DATE_FORMAT(catecodebf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("ProdinfobfDao.findCategory() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
}