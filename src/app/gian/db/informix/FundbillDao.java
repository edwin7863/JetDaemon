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

public class FundbillDao extends BasicDAO
{
	public FundbillDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List findFundbillMf(String compcode,String transDate,String flag) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		if(flag.equals("G"))//À³¦¬
		{	
			sql.append(" SELECT mf.*,(select custname from custinfomf where custxxid=mf.cvid ) cvname,(select p.username) maker,  ")
			   .append(" if(mf.addprepay>0,'4',if(mf.selfmoney>0,'2',if((select count(trannubr)from fundprepayuse where origtrannubr=mf.trannubr)>0,'3','1'))) billtype ")
			   .append(" FROM fundbillmf mf,profile p" )
			   .append(" WHERE mf.modifyuser=p.loginid ")
			   .append(" AND DATE_FORMAT(mf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
			   .append(transDate)
			   .append("','%Y-%m-%d') and mf.compcode = '")
			   .append(compcode)
			   .append("' and flag= '")
			   .append(flag)
			   .append("'")
			;
		}
		else  //À³¥I
		{
			sql.append(" SELECT mf.*,(select vndrname from supplierbf where vndrcode=mf.cvid ) cvname,(select p.username) maker,  ")
			   .append(" if(mf.addprepay>0,'4',if(mf.selfmoney>0,'2',if((select count(trannubr)from fundprepayuse where origtrannubr=mf.trannubr)>0,'3','1'))) billtype ")
			   .append("   FROM fundbillmf mf, profile p" )
			   .append("  WHERE mf.modifyuser=p.loginid ")
			   .append("    AND DATE_FORMAT(mf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
			   .append(transDate)
			   .append("','%Y-%m-%d') and mf.compcode = '")
			   .append(compcode)
			   .append("' and mf.flag= '")
			   .append(flag)
			   .append("'")
			;
		}
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			list = BeanUtil.getDynaBean(rs);
			
		}
		catch (SQLException e)
		{
			System.out.println("FundbillDao.findFundbillMf() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findFundbillDf(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  ")   
		   .append("   FROM fundbilldf " )
		   .append("  WHERE trannubr ='")
		   .append(trannubr)
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
			System.out.println("FundbillDao.findFundbillDf() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findSelfset(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  ")   
		   .append("   FROM fundselfset " )
		   .append("  WHERE trannubr ='")
		   .append(trannubr)
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
			System.out.println("FundbillDao.findSelfset() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findAddPrepay(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  ")   
		   .append("   FROM fundaddprepay " )
		   .append("  WHERE trannubr ='")
		   .append(trannubr)
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
			System.out.println("FundbillDao.findAddPrepay() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findPrepayuse(String trannubr) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  ")   
		   .append("   FROM fundprepayuse " )
		   .append("  WHERE trannubr ='")
		   .append(trannubr)
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
			System.out.println("FundbillDao.findPrepayuse() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
}