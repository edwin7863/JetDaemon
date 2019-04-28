package app.gian.db.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.gian.db.BasicDAO;
import app.gian.db.oracle.model.YgBwsCiCancelTempModel;
import app.gian.db.oracle.model.YgBwsCiTempModel;
import app.gian.db.oracle.model.YgBwsReceiptTempModel;



public class YgBwsReceiptDao extends BasicDAO
{
	
	public int updateFlagByRcvNo(String rcvNo, String flag , Connection conn) throws SQLException
	{
		Statement s = null;
		int rs = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE YG_BWS_RECEIPT_TEMP ")   
		   .append("    SET IMPORT_FLAG = '").append(flag).append("'")
		   .append("      , IMPORT_DATE = SYSDATE ")
		   .append("  WHERE RECEIPT_NUM = '").append(rcvNo).append("'")
		;
		try
		{			
			s  = conn.createStatement();
			rs = s.executeUpdate(sql.toString());									
		}
		catch (SQLException e)
		{
			System.out.println("YgBwsReceiptDao.updateFlagByRcvNo Error:"+e.getMessage());
			throw e;
		}		
		return rs;			   
	}	
	
	public List findDistinctRcvNoByFlag(String flag , Connection conn) throws SQLException
	{
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT distinct RECEIPT_NUM ")   
		   .append("   FROM YG_BWS_RECEIPT_TEMP " )
		   .append(" WHERE IMPORT_FLAG = '").append(flag).append("'")
		;
		try
		{			
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());						
			while(rs.next())
			{
				list.add(rs.getString(1));
			}
		}
		catch (SQLException e)
		{
			System.out.println("YgBwsReceiptDao.findDistinctRcvNoByFlag Error:"+e.getMessage());
			throw e;
		}		
		return list;			   
	}	
	
	
	public List findByRcvNo(String rcvNo , Connection conn) throws SQLException
	{
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT * ")   
		   .append("   FROM YG_BWS_RECEIPT_TEMP " )
		   .append("  WHERE RECEIPT_NUM = '").append(rcvNo).append("'")
		   .append("    AND IMPORT_FLAG ='N' ")
		;   		  				 
		try
		{			
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());						
			while(rs.next())
			{
				YgBwsReceiptTempModel model = new YgBwsReceiptTempModel();
				model.setCreationDate(rs.getString("CREATION_DATE"));
				model.setImportDate(rs.getString("IMPORT_DATE"));
				model.setImportFlag(rs.getString("IMPORT_FLAG"));
				model.setDeclareNumber(rs.getString("DECLARE_NUMBER"));
				model.setDeclareType(rs.getString("DECLARE_TYPE"));
				model.setDeclareLine(rs.getString("DECLARE_LINE"));								
				model.setOrgCode(rs.getString("ORG_CODE"));								
				model.setOrgManageNumber(rs.getString("ORG_MANAGE_NUMBER"));				
				model.setReceiptNum(rs.getString("RECEIPT_NUM"));				
				model.setVendorNumber(rs.getString("VENDOR_NUMBER"));								
				model.setVendorName(rs.getString("VENDOR_NAME"));				
				model.setItem(rs.getString("ITEM"));
				model.setVendorItem(rs.getString("VENDOR_ITEM"));
				model.setQuantity(rs.getString("QUANTITY"));				
				model.setAmount(rs.getString("AMOUNT"));
				model.setSubinventory(rs.getString("SUBINVENTORY"));				
				model.setProducePlace(rs.getString("PRODUCE_PLACE"));				
				model.setTaxNumber(rs.getString("TAX_NUMBER"));				
				model.setTaxRate(rs.getString("TAX_RATE"));
				model.setCustomDutyRate(rs.getString("CUSTOM_DUTY_RATE"));				
				list.add(model);									
			}	 			
		}
		catch (SQLException e)
		{
			System.out.println("YgBwsReceiptTempDao.findByFlag Error:"+e.getMessage());
		}		
		return list;
	}
	
	
	
	
}
