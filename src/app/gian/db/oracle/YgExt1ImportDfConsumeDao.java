package app.gian.db.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import app.gian.db.BasicDAO;
import app.gian.db.oracle.model.YgBwsCiCancelTempModel;



public class YgExt1ImportDfConsumeDao extends BasicDAO
{
	
	
	public int insertByList(List list , Connection conn) throws SQLException
	{
		Statement s = null;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into YG_EXT1_IMPORT_DF_CONSUME  ")
		   .append( "(ID,ORG_ID,IS_BWS,CREATION_DATE,CREATED_BY,LAST_UPDATE_DATE,LAST_UPDATED_BY,")
		   .append("  CI_NO,CI_LINE_NO,VENDOR_PART_NO,YOSUN_PART_NO,IMPORT_DF_NO,IMPORT_DF_LINE_NO,CONSUME_QTY,YG_BRAND_CODE)")
		    .append("values (?,?,?,SYSDATE,?,SYSDATE,?,?,?,?,?,?,?,?,?)");
		
		PreparedStatement ptmt=null;
		ptmt = conn.prepareStatement(sql.toString());
		Iterator itr = list.iterator();
		try 
		{
			while(itr.hasNext())
			{
				HashMap offMap = (HashMap) itr.next();
				int id = this.findNextSeq(conn);
				ptmt.clearParameters();
				ptmt.setInt(1,id);
				ptmt.setInt(2,Integer.parseInt(offMap.get("ORG_ID").toString().trim()));
				ptmt.setString(3,"Y");
				ptmt.setInt(4,-3);
				ptmt.setInt(5,-3);
				ptmt.setString(6,offMap.get("CI_NO").toString().trim());
				ptmt.setInt(7,Integer.parseInt(offMap.get("CI_LINE_NO").toString().trim()));
				ptmt.setString(8,offMap.get("VENDOR_PART_NO").toString().trim());
				ptmt.setString(9,offMap.get("YOSUN_PART_NO").toString().trim());
				ptmt.setString(10,offMap.get("IMPORT_DF_NO").toString().trim());
				ptmt.setInt(11,Integer.parseInt((String)offMap.get("IMPORT_DF_LINE_NO")));
				ptmt.setInt(12,Integer.parseInt((String)offMap.get("CONSUME_QTY")));
				ptmt.setString(13,offMap.get("YG_BRAND_CODE").toString().trim());
				int rst = ptmt.executeUpdate();
				cnt = cnt + rst;
			}	
		} 
		catch (SQLException e) 
		{
			System.out.println(this.getClass() + "insertByList() error " + e.toString());
			throw e;
		} 
		return cnt;			   
	}	
	
	public int findNextSeq( Connection conn) throws SQLException
	{
		int next = 0;		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select YG_EXT1_IMPORT_DF_CONSUME_S.Nextval from dual")   
		;
		try
		{			
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());						
			if(rs.next())
			{
				next = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			System.out.println("YgExt1ImportDfConsumeDao.findNextSeq Error:"+e.getMessage());
			throw e;
		}		
		return next;			   
	}	
	
}
