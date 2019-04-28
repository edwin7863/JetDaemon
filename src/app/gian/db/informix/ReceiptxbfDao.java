package app.gian.db.informix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.gian.db.BasicDAO;
import app.gian.db.DataBase;
import app.gian.db.informix.model.ReceiptxbfModel;
import app.gian.db.oracle.model.YgBwsCiCancelTempModel;
import app.gian.db.oracle.model.YgBwsCiTempModel;
import app.gian.db.oracle.model.YgBwsReceiptTempModel;



public class ReceiptxbfDao extends BasicDAO
{
	public ReceiptxbfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public int insert(YgBwsReceiptTempModel model ) throws SQLException
	{
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		PreparedStatement ptmt=null;
		
		sql.append(" insert into receiptxbf  values ( ")
		   .append(" ?,current,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		Connection conn = getConnection();
		
		try 
		{
			ptmt = conn.prepareStatement(sql.toString());
			ptmt.clearParameters();
			ptmt.setString(1,model.getCreationDate().substring(0,19));
			ptmt.setString(2,model.getImportFlag()==null?" ":model.getImportFlag());
			ptmt.setString(3,model.getDeclareNumber()==null?" ":model.getDeclareNumber());
			ptmt.setString(4,model.getDeclareType()==null?" ":model.getDeclareType());
			ptmt.setInt(5,Integer.parseInt(model.getDeclareLine()==null?"0":model.getDeclareLine()));
			ptmt.setString(6,model.getOrgCode()==null?" ":model.getOrgCode());
			ptmt.setString(7,model.getOrgManageNumber()==null?" ":model.getOrgManageNumber());
			ptmt.setString(8,model.getReceiptNum()==null?" ":model.getReceiptNum());
			ptmt.setString(9,model.getVendorNumber()==null?" ":model.getVendorNumber());
			ptmt.setString(10,model.getVendorName()==null?" ":model.getVendorName());
			ptmt.setString(11,model.getItem()==null?" ":model.getItem());
			ptmt.setInt(12,Integer.parseInt(model.getQuantity()==null?"0":model.getQuantity()));
			ptmt.setDouble(13,Double.parseDouble(model.getAmount()==null?"0":model.getAmount()));
			ptmt.setString(14,model.getSubinventory()==null?" ":model.getSubinventory());
			ptmt.setString(15,model.getProducePlace()==null?" ":model.getProducePlace());
			ptmt.setString(16,model.getTaxNumber()==null?" ":model.getTaxNumber());
			ptmt.setDouble(17,Double.parseDouble(model.getTaxRate()==null?"0":model.getTaxRate()));
			ptmt.setDouble(18,Double.parseDouble(model.getCustomDutyRate()==null?"0":model.getCustomDutyRate()));
			ptmt.setString(19,model.getVendorItem()==null?" ":model.getVendorItem());
			ptmt.executeUpdate();
		} 
		catch (SQLException e) 
		{
			System.out.println(this.getClass() + "insert() error " + e.toString());
			throw e;
		} 
		finally 
		{
			closeConn();
		}
		return cnt;
		
	}
	
	public List findDistinctRcvNoByFlag(String flag ) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT distinct recxnubr , vdrxnubr  , orgxcode, reptnubr , repttype ,subinven,orgxcode ")   
		   .append("   FROM receiptxbf " )
		   .append(" WHERE impxflag = '").append(flag).append("'")
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());						
			while(rs.next())
			{
				ReceiptxbfModel model = new ReceiptxbfModel();
				model.setRecxnubr(rs.getString("recxnubr")==null?"":rs.getString("recxnubr").trim());
				model.setVdrxnubr(rs.getString("vdrxnubr")==null?"":rs.getString("vdrxnubr").trim());
				model.setOrgxcode(rs.getString("orgxcode")==null?"":rs.getString("orgxcode").trim());
				model.setReptnubr(rs.getString("reptnubr")==null?"":rs.getString("reptnubr").trim());
				model.setRepttype(rs.getString("repttype")==null?"":rs.getString("repttype").trim());
				model.setSubinven(rs.getString("subinven")==null?"":rs.getString("subinven").trim());
				model.setOrgxcode(rs.getString("orgxcode")==null?"":rs.getString("orgxcode").trim());
				list.add(model);
			}
		}
		catch (SQLException e)
		{
			System.out.println("ReceiptxbfDao.findDistinctRcvNoByFlag Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	
	public List findByRcvNo(String rcvNo ) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ")   
		   .append("   FROM receiptxbf " )
		   .append(" WHERE recxnubr = '").append(rcvNo).append("'")
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());						
			while(rs.next())
			{
				ReceiptxbfModel model = new ReceiptxbfModel();
				model.setCrtxdate(rs.getString("crtxdate"));
				model.setImpxdate(rs.getString("impxdate"));
				model.setImpxflag(rs.getString("impxflag"));
				model.setReptnubr(rs.getString("reptnubr"));
				model.setRepttype(rs.getString("repttype"));
				model.setReptline(rs.getString("reptline"));
				model.setOrgxcode(rs.getString("orgxcode"));
				model.setOrgmanno(rs.getString("orgmanno"));
				model.setRecxnubr(rs.getString("recxnubr"));
				model.setVdrxnubr(rs.getString("vdrxnubr"));
				model.setVdrxname(rs.getString("vdrxname"));
				model.setItemxxxx(rs.getString("itemxxxx"));
				model.setQuantity(rs.getString("quantity"));
				model.setAmountxx(rs.getString("amountxx"));
				model.setSubinven(rs.getString("subinven"));
				model.setProplace(rs.getString("proplace"));
				model.setTaxxnubr(rs.getString("taxxnubr"));
				model.setTaxxrate(rs.getString("taxxrate"));
				model.setCustrate(rs.getString("custrate"));
				model.setVdrxitem(rs.getString("vdrxitem"));
				list.add(model);
			}
		}
		catch (SQLException e)
		{
			System.out.println("ReceiptxbfDao.findByRcvNo Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findSumByRcvNo(String rcvNo ) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT orgxcode,subinven,itemxxxx,sum(quantity) quantity ")   
		   .append("   FROM receiptxbf " )
		   .append(" WHERE recxnubr = '").append(rcvNo).append("'")
		   .append(" GROUP BY 1,2,3 ")
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());						
			while(rs.next())
			{
				ReceiptxbfModel model = new ReceiptxbfModel();
				
				model.setOrgxcode(rs.getString("orgxcode"));
				model.setSubinven(rs.getString("subinven"));
				model.setItemxxxx(rs.getString("itemxxxx"));
				model.setQuantity(rs.getString("quantity"));
				
				list.add(model);
			}
		}
		catch (SQLException e)
		{
			System.out.println("ReceiptxbfDao.findSumByRcvNo Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public int UpdateFlagByRcvNo(String flag,String rcvNo ) throws SQLException
	{
		Connection conn = null;
		int rst = -1;		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE receiptxbf  ")   
		   .append("    SET impxflag ='" ).append(flag).append("'")
		   .append("  WHERE recxnubr = '").append(rcvNo).append("'")
		;
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rst = s.executeUpdate(sql.toString());						
		
		}
		catch (SQLException e)
		{
			System.out.println("ReceiptxbfDao.UpdateFlagByRcvNo Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return rst;			   
	}		
	
}
