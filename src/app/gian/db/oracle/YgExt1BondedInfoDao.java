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



public class YgExt1BondedInfoDao extends BasicDAO
{
	
	
	public int insertByList(List list , Connection conn) throws SQLException
	{
		Statement s = null;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into yg_ext1_bonded_info  values ( ")
		   .append(" ?,?,?,?,?,?,?,?,?)");
		
		PreparedStatement ptmt=null;
		ptmt = conn.prepareStatement(sql.toString());
		Iterator itr = list.iterator();
		try 
		{
			while(itr.hasNext())
			{
				HashMap offMap = (HashMap) itr.next();
				ptmt.clearParameters();
				ptmt.setString(1,(String)offMap.get("CI_NO"));
				ptmt.setString(2,(String)offMap.get("PART_NO"));
				ptmt.setString(3,(String)offMap.get("PART_DESC"));
				ptmt.setString(4,(String)offMap.get("BRAND"));
				ptmt.setInt(5,Integer.parseInt((String)offMap.get("EXPORT_DF_LINE_NO NUMBER")));
				ptmt.setString(6,(String)offMap.get("IMPORT_DF"));
				ptmt.setInt(7,Integer.parseInt((String)offMap.get("IMPORT_DF_LINE_NO NUMBER")));
				ptmt.setInt(8,Integer.parseInt((String)offMap.get("QTY")));
				ptmt.setInt(9,Integer.parseInt("0"));
				
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
	
}
