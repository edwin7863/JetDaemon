package app.gian.db.informix;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import app.gian.db.BasicDAO;
import app.gian.db.DataBase;
import app.gian.util.BeanUtil;

public class VoucherDAO extends BasicDAO
{
	
	public VoucherDAO(DataBase db)
	{
		this.db = db;
	}
	
	
	public List findVoucherMfList(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *, ")
		   .append(" (select username from profile where loginid=vouchermf.makerid) as maker ")
		   .append("   FROM vouchermf where compcode='"+compcode+"' " )
		   .append(" AND DATE_FORMAT(modifydate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("VoucherDAO.findVoucherMfList() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;		
	}

	public List findVoucherDfList(String compcode,String voucherno) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *, ")
		   .append(" (SELECT description FROM codedetail where codetype='CV' ")
		   .append(" and transcode=voucherdf.currency) currid ")
		   .append("   FROM voucherdf where compcode='"+compcode+"' " )
		   .append(" AND voucherno ='"+voucherno+"'");
		
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			list = BeanUtil.getDynaBean(rs);
			
		}
		catch (SQLException e)
		{
			System.out.println("VoucherDAO.findVoucherMfList() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;		
	}
}
