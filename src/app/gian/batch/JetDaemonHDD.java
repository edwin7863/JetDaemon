package app.gian.batch;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import app.gian.B2Bi;
import app.gian.DbType;
import app.gian.delegate.DataProcDelegateHDD;
import app.gian.util.DateTimeUtil;


public class JetDaemonHDD 
{
	private static String ORACLE_DB = DbType.ORACLE_FORMAL;
	private static String PORTAL_DB = DbType.PORTAL_FORMAL;
	
	public JetDaemonHDD() 
	{
		
	};
	//args0->轉單起始日,args1->轉單結束日,args2->轉單公司別
	public static void main(String []args)throws Exception
	{			
		Connection oracleConn = null;
		Connection portalConn = null;
		Calendar  calBase = Calendar.getInstance();
		Calendar  calCmp = Calendar.getInstance();
		try 
		{	
			DataProcDelegateHDD dpDelegate = new DataProcDelegateHDD();
			String today = "";
			String today2 = "";
			String compcode="";
			String filecompcode="";
			if(args.length==3)
			{
				if(args[0]!=null)
					today=args[0];
				if(args[1]!=null)
					today2=args[1];
				if(args[2]!=null)
					compcode=args[2];
			}else if(args.length==1)
			{
				if(args[0]!=null)
					compcode=args[0];
				today=new DateTimeUtil(new Date()).getPrevDate();
			}
			
			//測試時用16
			if(compcode.equals("H"))
				filecompcode="18_MRG"; 
			else if(compcode.equals("I"))
				filecompcode="19_MRG";
			else if(compcode.equals("J"))
				filecompcode="17_MRG";
			else if(compcode.equals("S"))
				filecompcode="20_MRG";
			else if(compcode.equals("M"))
				filecompcode="22_MRG";
			//String transDateS=DateTimeUtil.transDateFormatString(today, "yyyy-MM-dd", "MMddyyyy");
			calBase.setTime( new java.util.Date(today));//start date
			//String transDateE=DateTimeUtil.transDateFormatString(today2, "yyyy-MM-dd", "MMddyyyy");
			calCmp.setTime( new java.util.Date(today2));//end date
			String dbXmlFile = "../JetDaemon/refxml/big5_formal_db.xml";
			Document dataDoc = B2Bi.newDocument(dbXmlFile);
			Element docRoot = dataDoc.getDocumentElement();
			Element dbComm = (Element) dataDoc.getElementsByTagName("file").item(0);
			String filepath = dbComm.getElementsByTagName("path").item(0).getFirstChild().getNodeValue();
			String filename = filepath+compcode+"\\"+filecompcode+".OUT";  
	    	FileWriter fw = new FileWriter(filename);
	    	fw.write("CHT0110010VERIFYNTD\n");
			while(calBase.getTime().before(calCmp.getTime()))//當起始日小於結束日,就抓出日期
			{
				today=DateTimeUtil.transDateFormatString(DateTimeUtil.getDateString(calBase),"yyyy-MM-dd","yyyy-MM-dd");
				calBase.add(Calendar.DATE,1);
		    	//改為轉特定區間的進貨及銷貨檔
				//dpDelegate.ProcessAccountData(compcode,today,"C",fw);     //01轉出所有會計科目  --客戶
	 			//dpDelegate.ProcessAccountData(compcode,today,"S",fw);      //02轉出所有會計科目   --供應商
				//dpDelegate.ProcessCustData(compcode,today,fw);     //03轉出所有 custinfomf 客戶主檔資料表   --客戶
	 			//dpDelegate.ProcessVdrData(compcode,today,fw);      //04轉出所有 supplierbf 供應商主檔資料表   --供應商
				//dpDelegate.ProcessItemData(compcode,today,fw);     //05轉出所有 prodinfobf 料號主檔   --料品
				dpDelegate.ProcessBorrowIn(compcode,today,fw);    //07轉出所有 procumntmf 借入單  --借進來的貨品
				dpDelegate.ProcessReceviveData(compcode,today,fw); //06轉出所有 receivexmf 進貨主檔  --已成立的進貨單
				dpDelegate.ProcessSaleReturn(compcode,today,fw);   //08轉出所有 shipmatlmf type='00'  --已銷貨但退回的單
				dpDelegate.ProcessAdjustInventry(compcode,today,fw);   //09轉出調整單
				dpDelegate.ProcessSalesData(compcode,today,fw);    //10轉出所有 shipmatlmf 銷貨主檔  --已成立的出貨單
				dpDelegate.ProcessRecvReturn(compcode,today,fw);     //11轉出所有 receivexmf type='18'  --已進貨但退出的單
				
				if(compcode.equals("J")||compcode.equals("S"))
				{
					dpDelegate.ProcessInvoiceDataIn(compcode,today,fw);  //12轉出進項發票
					dpDelegate.ProcessInvoiceDataOut(compcode,today,fw); //13轉出銷項發票
				}
				//dpDelegate.ProcessSaleDiscount(compcode,today,fw);   //14轉出所有  interoutmf type='A1'--銷貨折讓
				//dpDelegate.ProcessRecvDiscount(compcode,today,fw);   //15轉出所有 interoutmf type='A8'--進貨折讓  
				dpDelegate.ProcessRetBorrow(compcode,today,fw);     //16轉出所有借入還出單---19
				//dpDelegate.ProcessFundsBill(compcode,today,fw);     //17轉出所有收款單---
			}	
			fw.close();
			System.out.println("----PROCESS FINISH---");
			
		} 
		catch (Exception e) 
		{			
			System.out.println("----PROCESS ERROR---"+e.toString());
		}
		finally
		{
			if(oracleConn!=null) oracleConn.close();	
			if(portalConn!=null) portalConn.close();		
			System.out.println("####FINISH THE BATCH####");		
		}
	}
}
