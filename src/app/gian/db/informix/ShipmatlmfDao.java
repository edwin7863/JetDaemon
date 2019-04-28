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



public class ShipmatlmfDao extends BasicDAO
{
	public ShipmatlmfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List findByType(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  ")   
		   .append(" FROM shipmatlmf where trancode='"+type+"' AND shipxstat='05'" )
		   .append("  AND compcode='")
		   .append(compcode)
		   .append("'")
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
			System.out.println("ShipmatlmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	/***
	 * 用來查銷貨為:內銷,外銷,三角貿易
	 * @param type 10,15
	 * @param compcode
	 * @param transDate
	 * @return
	 * @throws SQLException
	 */
	public List find1015Type(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT shipmatlmf.* ,(select invostat from invodatamf where invonubr=shipmatlmf.invonubr and inouttype=3) as invostat, ") 
		   .append(" (select username from profile where loginid=shipmatlmf.modiuser) as modiname, ")
		   .append(" 0 as payed, 0 as localpayed ")
		   .append(" FROM shipmatlmf " )
		   .append(" where shipmatlmf.trancode in('10','13','14','15','1B','1C') AND shipmatlmf.shipxstat in('05','06') ")
		   .append(" AND shipmatlmf.compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" AND DATE_FORMAT(shipmatlmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d')")
		;
		/*
		sql.append(" SELECT shipmatlmf.* ,(select invostat from invodatamf where invonubr=shipmatlmf.invonubr and inouttype=3) as invostat, ") 
		   .append(" (select username from profile where loginid=shipmatlmf.modiuser) as modiname, ")
		   .append(" billaccount.payed,billaccount.localpayed ")
		   .append(" FROM shipmatlmf,billaccount where shipmatlmf.shipnubr = billaccount.trannubr " )
		   .append(" AND shipmatlmf.trancode in('10','13','14','15','1B') AND shipmatlmf.shipxstat in('05','06') ")
		   .append(" AND shipmatlmf.compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" AND (DATE_FORMAT(shipmatlmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d')")
		   .append(" OR  DATE_FORMAT(billaccount.paymodidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d'))")
		;
		*/

		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			list = BeanUtil.getDynaBean(rs);
			
		}
		catch (SQLException e)
		{
			System.out.println("ShipmatlmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	/***
	 * 查銷貨,銷折,銷退的每日沖款紀錄offset,localoffset 寫回正航vba 
	 * @param 
	 * @param compcode
	 * @param transDate
	 * @return
	 * @throws SQLException
	 */
	public List findBillaccountOffset(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT trannubr,payed,localpayed ")
		   .append(" FROM billaccount " )
		   .append(" where datastat <>'99' ")
		   .append(" AND compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" AND DATE_FORMAT(paymodidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d') ")
		   //.append(" AND DATE_FORMAT(paymodidate,'%Y-%m-%d') between  DATE_FORMAT( '2018/10/01")
		   //.append("','%Y-%m-%d') and DATE_FORMAT('")
		   //.append(transDate)
		   //.append("','%Y-%m-%d') ")

		;
		/*
		sql.append(" SELECT shipmatlmf.* ,(select invostat from invodatamf where invonubr=shipmatlmf.invonubr and inouttype=3) as invostat, ") 
		   .append(" (select username from profile where loginid=shipmatlmf.modiuser) as modiname, ")
		   .append(" 0 as payed, 0 as localpayed ")
		   .append(" FROM shipmatlmf " )
		   .append(" where shipmatlmf.trancode in('10','13','14','15','1B') AND shipmatlmf.shipxstat in('05','06') ")
		   .append(" AND shipmatlmf.compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" AND DATE_FORMAT(shipmatlmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d')")
		;*/
		try
		{			
			conn = getConnection();
			s  = conn.createStatement();
			rs = s.executeQuery(sql.toString());
			list = BeanUtil.getDynaBean(rs);
			
		}
		catch (SQLException e)
		{
			System.out.println("ShipmatlmfDao.findBillaccountOffset() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	/***
	 * 用來查硬碟環保稅銷貨為:內銷,外銷,三角貿易
	 * @param type 10,15
	 * @param compcode
	 * @param transDate
	 * @return
	 * @throws SQLException
	 */
	public List find1015TypeH(String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ,(select invostat from invodatamf where invonubr=shipmatlmf.invonubr and inouttype=3) as invostat,  ") 
		   .append(" (select username from profile where loginid=shipmatlmf.modiuser) as modiname ")
		   .append(" FROM shipmatlmf where trancode in('10','15') AND shipxstat in('05','06') " )
		   .append(" AND compcode='")
		   .append(compcode)
		   .append("'")
		   .append(" AND DATE_FORMAT(modidate,'%Y-%m-%d') = DATE_FORMAT( '")
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
			System.out.println("ShipmatlmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	/***
	 * 算出原單未結的量,但不查進貨折讓,銷貨折讓
	 * @param shipnubr  原單單號
	 * @param prodnubr  產品料號
	 * @param poxxnubr  客戶料號
	 * @param origxqty  原單數量
	 * @param seqtnubr  
	 * @return
	 * @throws SQLException
	 */
	public int findOrigxqty(
			String shipnubr,
			String prodnubr,
			String poxxnubr,
			String origxqty,
			String seqtnubr)
			throws SQLException {
			//ShipmatlmfModelExt model = null;
			List list = new ArrayList();
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			StringBuffer sql = new StringBuffer();
			int totalnet = 0;
			sql
				.append("select sum(quantity+quantxs2) as sumqty")
				.append("  from interoutdf a,interoutmf b ")
				.append(" where a.trannubr=b.trannubr ")
				.append("   and b.datastat in ('00','01','02','03','04','05','06')  ")
				.append("   and INSTR(a.trannubr, 'AR')=0  and INSTR(a.trannubr, 'AS')=0")
				.append("   and b.agnxnubr = ")
				.append(dbEscape(shipnubr))
				.append("   and a.prodnubr = ")
				.append(dbEscape(prodnubr));

			if (poxxnubr.trim().length() > 0) 
			{
				sql.append(" and a.poxxnubr = ").append(dbEscape(poxxnubr));
			}
			if (seqtnubr.trim().length() > 0) 
			{
				sql.append(" and a.seqtnubr= ").append(dbEscape(seqtnubr));
			}
			try 
			{
				conn = getConnection();
				st = conn.createStatement();
				//System.out.println("sql===>" + sql.toString());
				rs = st.executeQuery(sql.toString());
				if (rs.next()) 
				{
					if (rs.getString("sumqty") != null) 
					{
						//System.out.println("已申請量:" + rs.getString("sumqty"));
						totalnet = Integer.parseInt(origxqty) - rs.getInt("sumqty");
					} 
					else 
					{
						totalnet = Integer.parseInt(origxqty);
					}
				}
			} 
			catch (SQLException e) 
			{
				System.out.println(this.getClass() + "findOrigxqty() error " + e.toString());
				throw e;
			} 
			finally 
			{
				closeConn();
			}
			return totalnet;
		}
	
	/***
	 * 算出原單的量,但不查進貨折讓,銷貨折讓
	 * @param shipnubr  原單單號
	 * @param prodnubr  產品料號
	 * @param poxxnubr  客戶料號
	 * @param origxqty  原單數量
	 * @param seqtnubr  
	 * @return
	 * @throws SQLException
	 */
	public int findOrigxqty(
			String recvnubr,
			String prodnubr,
			String seqtnubr)
			throws SQLException {
			//ShipmatlmfModelExt model = null;
			List list = new ArrayList();
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			StringBuffer sql = new StringBuffer();
			int totalnet = 0;
			sql
				.append(" select sum(quantity) as sumqty")
				.append("   from receivexdf  ")
				.append("  where recvnubr= ")
				.append(dbEscape(recvnubr))
				.append("  and prodnubr =  ")
				.append(dbEscape(prodnubr))
				.append("  and recordno= ")
				.append(dbEscape(seqtnubr));				

			try 
			{
				conn = getConnection();
				st = conn.createStatement();
				//System.out.println("sql===>" + sql.toString());
				rs = st.executeQuery(sql.toString());
				if (rs.next()) 
				{
					if (rs.getString("sumqty") != null) 
					{
						return Integer.parseInt(rs.getString("sumqty"));
					} 
					else 
					{
						return 0;
					}
				}
			} 
			catch (SQLException e) 
			{
				System.out.println(this.getClass() + "findOrigxqty() error " + e.toString());
				throw e;
			} 
			finally 
			{
				closeConn();
			}
			return 0;
		}
}