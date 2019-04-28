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

public class SubjectDAO extends BasicDAO
{
	
	public SubjectDAO(DataBase db)
	{
		this.db = db;
	}
	
	
	public List findSubjectList(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ")
		   .append("   FROM subject where compcode='"+compcode+"' " )
		   //.append(" AND modidate >='2018-01-01'")
		   .append(" AND DATE_FORMAT(modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("SubjectDAO.findSubjectList() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;		
	}

}
