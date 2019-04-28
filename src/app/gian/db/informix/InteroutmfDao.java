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



public class InteroutmfDao extends BasicDAO
{
	public InteroutmfDao(DataBase db) 
	{
			this.db = db;
	}
	
	public List findRecvDist(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT interoutmf.*,(SELECT vndrcode FROM receivexmf WHERE recvnubr=agnxnubr)  vndrcode ,")
		   .append(" (SELECT cvtxrate FROM receivexmf WHERE recvnubr=interoutmf.agnxnubr)  cvtxrate, ")
		   .append(" (SELECT buycode FROM receivexmf WHERE recvnubr=interoutmf.agnxnubr)  buycode, ")
		   .append(" (if(receivexmf.invostat='28','3',receivexmf.taxtype)) taxtype2, ")
		   .append(" (SELECT username FROM profile WHERE loginid=interoutmf.regiuser)  reginame, ")
		   .append(" (SELECT invonubr FROM receivexmf WHERE recvnubr=interoutmf.agnxnubr)  invonubr ")
		   .append("   FROM interoutmf,receivexmf where interoutmf.agnxnubr= receivexmf.recvnubr and interoutmf.trancode='"+type+"' " )
		   .append("    AND interoutmf.datastat='05' AND interoutmf.compcode= '")
		   .append(compcode)
		   .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("InteroutmfmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findSaleDist(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		   
		sql.append(" SELECT interoutmf.*,")
		   .append(" (SELECT taxxcode FROM shipmatlmf WHERE shipnubr=agnxnubr)  taxcode, ")
		   .append(" (SELECT exchrate FROM shipmatlmf WHERE shipnubr=agnxnubr)  cvtxrate, ")
		   .append(" (SELECT invonubr FROM shipmatlmf WHERE shipnubr=agnxnubr)  invonubr, ")
		   .append(" (SELECT username FROM profile WHERE loginid=interoutmf.regiuser)  reginame, ")
		   .append(" (SELECT salecode FROM shipmatlmf WHERE shipnubr=agnxnubr)  sales,  ")
		   .append(" billaccount.payed,billaccount.localpayed ")
		   .append(" FROM interoutmf ,billaccount where interoutmf.trannubr=billaccount.trannubr " )
		   .append(" AND trancode='"+type+"' ")
		   .append(" AND interoutmf.datastat='05' AND interoutmf.compcode= '")
		   .append(compcode)
		   .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		   .append(transDate)
		   .append("','%Y-%m-%d')")
		  // .append(" OR DATE_FORMAT(billaccount.paymodidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		  // .append(transDate)
		  // .append("','%Y-%m-%d'))")
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
			System.out.println("InteroutmfDao.findSaleDist() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	//銷貨退回
	public List findSaleReturn(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		 
		 sql.append(" SELECT distinct interoutmf.*,")
		    .append(" if(interoutmf.exchrate is null,(select exchrate from shipmatlmf where shipnubr=interoutmf.agnxnubr),interoutmf.exchrate) exchrate2 ,")
		    .append(" if(interoutmf.taxtype is null ,(select taxxcode from shipmatlmf where shipnubr=interoutmf.agnxnubr),interoutmf.taxtype) taxtype2, ")
		    .append("  (SELECT salecode FROM custinfomf WHERE custxxid=interoutmf.custxxid limit 1 ) sales, ")//(業務抓第一筆)
		    .append("  (SELECT username FROM profile WHERE loginid=interoutmf.regiuser)  reginame ")
		    .append("  FROM interoutmf ,interoutdf ")
		    .append("  where interoutmf.trannubr= interoutdf.trannubr " )
		    .append("    AND interoutmf.trancode='"+type+"' ")
		    .append("    AND interoutmf.datastat='06'")
		    .append("    AND interoutmf.compcode= '")
		    .append(compcode)
		    .append("' AND (DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		    .append(transDate)
		    .append("','%Y-%m-%d'))")
			//.append(" UNION ALL ")
		;
		 //新的銷退
/*		 sql.append(" SELECT interoutmf.*,(interoutmf.exchrate) exchrate,(interoutmf.contactx) shipname,(interoutmf.regiuser) buycode,")
		    .append("  (SELECT username FROM profile WHERE profile.loginid=interoutmf.regiuser)  reginame, ")
		    .append("  (SELECT vndraddr FROM supplierbf WHERE supplierbf.vndrcode=interoutmf.custxxid)  shipaddr, ")
		    .append(" (interoutmf.taxtype) taxtype2 ")
		    .append("  FROM interoutmf  ")
		    .append("  where  interoutmf.trancode='"+type+"' " )
		    .append("    AND interoutmf.datastat='06'")
		    .append("    AND interoutmf.compcode= '")
		    .append(compcode)
		    .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("InteroutmfDao.findSaleReturn() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	//進貨退出
	public List findRecvReturn(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		 //舊的進退
		 sql.append(" SELECT interoutmf.*,")
		    .append("  (receivexmf.cvtxrate) exchrate2,")
		    .append("  receivexmf.shipname,receivexmf.buycode, ")
		    .append("  (SELECT username FROM profile WHERE profile.loginid=interoutmf.regiuser)  reginame, ")
		    .append("  (SELECT vndraddr FROM supplierbf WHERE supplierbf.vndrcode=interoutmf.custxxid)  shipaddr, ")
		    .append(" (if(receivexmf.invostat='28','3',receivexmf.taxtype)) taxtype2 ")
		    .append("  FROM interoutmf ,receivexmf ")
		    .append("  where interoutmf.agnxnubr= receivexmf.recvnubr and interoutmf.trancode='"+type+"' " )
		    .append("    AND interoutmf.datastat='06' and (trantype is null or trantype ='') ")
		    .append("    AND interoutmf.compcode= '")
		    .append(compcode)
		    .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		    .append(transDate)
		    .append("','%Y-%m-%d')")
		    .append(" UNION ALL ")
		;
		 //新的進退
		 sql.append(" SELECT interoutmf.*,(interoutmf.exchrate) exchrate2,(interoutmf.contactx) shipname,(interoutmf.regiuser) buycode,")
		    .append("  (SELECT username FROM profile WHERE profile.loginid=interoutmf.regiuser)  reginame, ")
		    .append("  (SELECT vndraddr FROM supplierbf WHERE supplierbf.vndrcode=interoutmf.custxxid)  shipaddr, ")
		    .append(" (interoutmf.taxtype) taxtype2 ")
		    .append("  FROM interoutmf  ")
		    .append("  where  interoutmf.trancode='"+type+"' and trantype='2' " )
		    .append("    AND interoutmf.datastat='06'")
		    .append("    AND interoutmf.compcode= '")
		    .append(compcode)
		    .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("InteroutmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findBorrowReturn(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		 
		 sql.append(" SELECT interoutmf.*,supplierbf.vndrname,supplierbf.vndrabbr, ")
		 	.append("  (SELECT username FROM profile WHERE profile.loginid=interoutmf.regiuser)  reginame ")
		    .append("  FROM interoutmf,supplierbf  ")
		    .append("  where interoutmf.datastat='06' AND trancode='19'")
		    .append("    AND supplierbf.vndrcode=interoutmf.custxxid ")
		    .append("    AND interoutmf.compcode= '")
		    .append(compcode)
		    .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("InteroutmfmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	/**
	 * 環保稅版
	 * @param type
	 * @param compcode
	 * @param transDate
	 * @return
	 * @throws SQLException
	 */
	public List findSaleReturnH(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		 
		 sql.append(" SELECT interoutmf.*,")
		    .append("  shipmatlmf.exchrate ,shipmatlmf.invonubr,shipmatlmf.taxxcode, ")
		    .append("  shipmatlmf.shipaddr, shipmatlmf.shipname,shipmatlmf.addrseqt, ")
		    .append("  (SELECT salecode FROM shipmatlmf WHERE shipnubr=agnxnubr) sales, ")
		    .append("  (SELECT username FROM profile WHERE loginid=regiuser)  reginame ")
		    .append("  FROM interoutmf ,shipmatlmf ")
		    .append("  where interoutmf.agnxnubr= shipmatlmf.shipnubr and interoutmf.trancode='"+type+"' " )
		    .append("    AND interoutmf.datastat='06'")
		    .append("    AND interoutmf.compcode= '")
		    .append(compcode)
		    .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		    .append(transDate)
		    .append("','%Y-%m-%d')")
		    .append(" and interoutmf.locxcode not in('JTB','JTD','JTE') ")
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
			System.out.println("InteroutmfmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findRecvReturnH(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		 
		 sql.append(" SELECT interoutmf.*,")
		    .append("  receivexmf.cvtxrate ,receivexmf.invonubr,")
		    .append("  receivexmf.shipname,receivexmf.buycode, ")
		    .append("  (SELECT username FROM profile WHERE profile.loginid=interoutmf.regiuser)  reginame, ")
		    .append("  (SELECT vndraddr FROM supplierbf WHERE supplierbf.vndrcode=interoutmf.custxxid)  shipaddr, ")
		    .append(" (if(receivexmf.invostat='28','3',receivexmf.taxtype)) taxtype ")
		    .append("  FROM interoutmf ,receivexmf ")
		    .append("  where interoutmf.agnxnubr= receivexmf.recvnubr and interoutmf.trancode='"+type+"' " )
		    .append("    AND interoutmf.datastat='06'")
		    .append("    AND interoutmf.compcode= '")
		    .append(compcode)
		    .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
		    .append(transDate)
		    .append("','%Y-%m-%d')")
		    .append("and interoutmf.locxcode not in('JTB','JTD','JTE')")
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
			System.out.println("InteroutmfmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
	
	public List findBorrowReturnH(String type,String compcode,String transDate) throws SQLException
	{
		Connection conn = null;
		List list = new ArrayList();		
		Statement s = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		 
		 sql.append(" SELECT interoutmf.*,supplierbf.vndrname,supplierbf.vndrabbr, ")
		 	.append("  (SELECT username FROM profile WHERE profile.loginid=interoutmf.regiuser)  reginame ")
		    .append("  FROM interoutmf,supplierbf  ")
		    .append("  where interoutmf.datastat='06' AND trancode='19'")
		    .append("    AND supplierbf.vndrcode=interoutmf.custxxid ")
		    .append("    AND interoutmf.compcode= '")
		    .append(compcode)
		    .append("' AND DATE_FORMAT(interoutmf.modidate,'%Y-%m-%d') =  DATE_FORMAT( '")
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
			System.out.println("InteroutmfmfDao.findByType() Error:"+e.getMessage());
			throw e;
		}		
		finally
		{
			closeConn();
		}
		return list;			   
	}
}