/*
########################################################################
# SYSTEM  ID:BwsDaemon                                                                                                                                                  #
# PROGRAM ID: YgBwsOnHandVDao.java                                                                                                                          #
# AUTHOR    : Endra Lee                                                                                                                                                     #
########################################################################
# MODIFICATION LOG:                                                                                                                                                       #
#                                                                                                                                                                                          #
########################################################################
*/
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
import app.gian.db.oracle.model.YgBwsOnHandVModel;

public class YgBwsOnHandVDao extends BasicDAO
{
	
	public List findErpStock(Connection conn) throws SQLException
	{
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		
		//sql.append("SELECT OOD.OPERATING_UNIT ORG_ID,TRIM(SUBSTR(HOU.NAME,1,3)) ORG_CODE,YOA.ORGANIZATION_ID,			   YOA.INVENTORY_ITEM_ID,MSI.SEGMENT1 ITEM,YOA.SUBINVENTORY_CODE,YOA.ON_HAND_QTY FROM HR_OPERATING_UNITS HOU,ORG_ORGANIZATION_DEFINITIONS OOD,  MTL_SYSTEM_ITEMS_B MSI,YG_OM_AVAILABILITY_V YOA WHERE YOA.ORGANIZATION_ID = MSI.ORGANIZATION_ID AND YOA.INVENTORY_ITEM_ID = MSI.INVENTORY_ITEM_ID AND YOA.ORGANIZATION_ID = OOD.ORGANIZATION_ID   AND OOD.OPERATING_UNIT = HOU.ORGANIZATION_ID   AND YG_BWS_PKG.IS_BOND_ORG(OOD.OPERATING_UNIT) = 'Y'  AND YG_BWS_PKG.IS_BOND_SUBINVENTORY(YOA.ORGANIZATION_ID,YOA.SUBINVENTORY_CODE) = 'Y'");
		
		sql.append(" SELECT * ")   
		   .append("   FROM  YG_BWS_ONHAND_V " )
		;   		  				 
		try
		{			
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			//System.out.println(rs.toString());			
			while(rs.next())
			{
				//System.out.println(rs.getString(1));
				
				YgBwsOnHandVModel model = new YgBwsOnHandVModel();
				model.setOrgId(rs.getString("ORG_ID"));
				model.setOrgCode(rs.getString("ORG_CODE"));				
				model.setOrganizationId(rs.getString("ORGANIZATION_ID"));				
				model.setInventoryItemId(rs.getString("INVENTORY_ITEM_ID"));
				model.setItem(rs.getString("ITEM"));
				model.setOnHandQty(rs.getString("ON_HAND_QTY"));
				model.setSubinventoryCode(rs.getString("SUBINVENTORY_CODE"));		
				list.add(model);
													
			}	 			
		}
		catch (SQLException e)
		{
			System.out.println("YgBwsOnHandVDao.findErpStock Error:"+e.getMessage());
			throw e;
		}		
		return list;
	}
	
	
}
