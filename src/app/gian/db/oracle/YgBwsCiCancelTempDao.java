package app.gian.db.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.gian.db.BasicDAO;
import app.gian.db.oracle.model.YgBwsCiCancelTempModel;



public class YgBwsCiCancelTempDao extends BasicDAO
{
	
	
	public int updateFlagByCi(String ciNo, String flag , Connection conn) throws SQLException
	{
		Statement s = null;
		int rs = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE YG_BWS_CI_CANCEL_TEMP ")   
		   .append("    SET IMPORT_FLAG = '").append(flag).append("' ,")
		   .append("        IMPORT_DATE = SYSDATE ")
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
		   .append("   FROM YG_BWS_CI_CANCEL_TEMP " )
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
			System.out.println("YgBwsCiCancelTempDao.findDistinctCIByFlag Error:"+e.getMessage());
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
		   .append("   FROM YG_BWS_CI_CANCEL_TEMP " )
		   .append(" WHERE CI_NO = '").append(ciNo).append("'")
		;   		  				 
		try
		{			
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());						
			while(rs.next())
			{
				YgBwsCiCancelTempModel model = new YgBwsCiCancelTempModel();
				model.setCreationDate(rs.getString("CREATION_DATE"));
				model.setImportDate(rs.getString("IMPORT_DATE"));
				model.setImportFlag(rs.getString("IMPORT_FLAG"));
				model.setOrgId(rs.getString("ORG_ID"));
				model.setOrgCode(rs.getString("ORG_CODE"));
				model.setOrgManageNumber(rs.getString("ORG_MANAGE_NUMBER"));
				model.setCiNo(rs.getString("CI_NO"));
				model.setPlNumber(rs.getString("PL_NUMBER"));
				model.setSuninventory(rs.getString("SUBINVENTORY"));
				list.add(model);									
			}	 			
		}
		catch (SQLException e)
		{
			System.out.println("YgBwsCiCancelTempDao.findByFlag Error:"+e.getMessage());
		}		
		return list;
	}		
}
