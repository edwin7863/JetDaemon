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



public class YgBwsCiTempDao extends BasicDAO
{
	
	
	public int updateFlagByCi(String ciNo, String flag , Connection conn) throws SQLException
	{
		Statement s = null;
		int rs = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE YG_BWS_CI_TEMP ")   
		   .append("    SET IMPORT_FLAG = '").append(flag).append("'")
		   .append("       ,IMPORT_DATE = SYSDATE ")
		   .append("  WHERE CI_NO = '").append(ciNo).append("'")
		;
		try
		{			
			s  = conn.createStatement();
			rs = s.executeUpdate(sql.toString());									
		}
		catch (SQLException e)
		{
			System.out.println("YgBwsCiTempDao.updateFlagByCi Error:"+e.getMessage());
			throw e;
		}		
		return rs;			   
	}	
	
	public List findDistinctCIByFlag(String flag , Connection conn) throws SQLException
	{
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT distinct CI_NO ")   
		   .append("   FROM YG_BWS_CI_TEMP " )
		   .append(" WHERE IMPORT_FLAG = '").append(flag).append("'")
		   .append("   AND PL_NUMBER IS NOT NULL ")
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
			System.out.println("YgBwsCiTempDao.findDistinctCIByFlag Error:"+e.getMessage());
			throw e;
		}		
		return list;			   
	}	
	
	
	
	public List findByCi(String ciNo , Connection conn) throws SQLException
	{
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT * ")   
		   .append("   FROM YG_BWS_CI_TEMP " )
		   .append(" where CI_NO = '").append(ciNo).append("'");
		;   		  				 
		try
		{			
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());						
			while(rs.next())
			{
				YgBwsCiTempModel model = new YgBwsCiTempModel();
				model.setCreationDate(rs.getString("CREATION_DATE"));
				model.setImportDate(rs.getString("IMPORT_DATE"));
				model.setImportFlag(rs.getString("IMPORT_FLAG"));
				model.setDeclareType(rs.getString("DECLARE_TYPE"));
				model.setOrgId(rs.getString("ORG_ID"));
				model.setOrgCode(rs.getString("ORG_CODE"));				
				model.setOrgManageNumber(rs.getString("ORG_MANAGE_NUMBER"));
				model.setOrganizationId(rs.getString("ORGANIZATION_ID"));				
				model.setCiNo(rs.getString("CI_NO"));
				model.setCiDate(rs.getString("CI_DATE"));				
				model.setPlNumber(rs.getString("PL_NUMBER"));
				model.setTotalPackage(rs.getString("TOTAL_PACKAGE"));
				model.setCustomerId(rs.getString("CUSTOMER_ID"));
				model.setCustomerNumber(rs.getString("CUSTOMER_NUMBER"));
				model.setCustomerName(rs.getString("CUSTOMER_NAME"));
				model.setCiCustomerTitle(rs.getString("CI_CUSTOMER_TITLE"));
				model.setCustomerManageNumber(rs.getString("CUSTOMER_MANAGE_NUMBER"));
				model.setShipToLocation(rs.getString("SHIP_TO_LOCATION"));
				model.setShipToAddress(rs.getString("SHIP_TO_ADDRESS"));
				model.setShipToContact(rs.getString("SHIP_TO_CONTACT"));
				model.setCiLineNo(rs.getString("CI_LINE_NO"));
				model.setInventoryItemId(rs.getString("INVENTORY_ITEM_ID"));
				model.setItem(rs.getString("ITEM"));
				model.setItemBrand(rs.getString("ITEM_BRAND"));
				model.setCustomerItem(rs.getString("CUSTOMER_ITEM"));
				model.setVendorItem(rs.getString("VENDOR_ITEM"));
				model.setQuantity(rs.getString("QUANTITY"));
				model.setSubinventory(rs.getString("SUBINVENTORY"));	
				model.setOrgUniformNumber(rs.getString("ORG_UNIFORM_NUMBER"));	
				model.setCategory(rs.getString("CATEGORY")==null?" ":rs.getString("CATEGORY"));								
				list.add(model);									
			}	 			
		}
		catch (SQLException e)
		{
			System.out.println("YgBwsCiTempDao.findByFlag Error:"+e.getMessage());
			throw e;
		}		
		return list;
	}
	
	
}
