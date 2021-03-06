package app.gian.batch;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import app.gian.B2Bi;
import app.gian.DbType;
import app.gian.delegate.DataProcDelegate;
import app.gian.util.DateTimeUtil;

public class JetDaemon 
{
	private static String DB_TYPE = DbType.BIG5_FORMAL;
	private static String DB_TYPE_INNO = DbType.BIG5_FORMAL_INNOTRON;
	private static String DB_TYPE_JETCHINA = DbType.BIG5_FORMAL_JETCHINA;
	public JetDaemon() 
	{
		
	};
	public static void main(String []args)throws Exception
	{			
		Connection oracleConn = null;
		Connection portalConn = null;
		try 
		{	
			DataProcDelegate dpDelegate = new DataProcDelegate();
			String today = "";
			String compcode="";
			String filecompcode="";
			String DATATYPE="";
			if(args.length==2)
			{
				if(args[0]!=null)
					today=args[0];
				if(args[1]!=null)
					compcode=args[1];
			}else if(args.length==1)
			{
				if(args[0]!=null)
					compcode=args[0];
				today=new DateTimeUtil(new Date()).getPrevDate();
			}
			
			//測試時用16
			if(compcode.equals("H"))
			{
				filecompcode="18_MRG";
				DATATYPE=DB_TYPE;
			}
			else if(compcode.equals("I"))
			{
				filecompcode="19_MRG";
				DATATYPE=DB_TYPE;
			}
			else if(compcode.equals("J"))
			{
				filecompcode="17_MRG";
				DATATYPE=DB_TYPE;
			}
			else if(compcode.equals("S"))
			{
				filecompcode="20_MRG";
				DATATYPE=DB_TYPE;
			}
			else if(compcode.equals("M"))
			{
				filecompcode="22_MRG";
				DATATYPE=DB_TYPE;
			}
			else if(compcode.equals("W"))
			{
				filecompcode="25_MRG";
				DATATYPE=DB_TYPE;
			}
			else if(compcode.equals("N"))
			{
				filecompcode="26_MRG";
				DATATYPE=DB_TYPE_INNO;
			}			
			else if(compcode.equals("C"))
			{
				filecompcode="28_MRG";
				DATATYPE=DB_TYPE_JETCHINA;
			}

			String transDate=DateTimeUtil.transDateFormatString(today, "yyyy-MM-dd", "MMddyyyy");
			//String filename = "../JetDaemon/"+compcode+"/01"+transDate+"_MRG.OUT";
			String dbXmlFile = "../JetDaemon/refxml/big5_formal_db.xml";
			Document dataDoc = B2Bi.newDocument(dbXmlFile);
			Element docRoot = dataDoc.getDocumentElement();
			Element dbComm = (Element) dataDoc.getElementsByTagName("file").item(0);
			String filepath = dbComm.getElementsByTagName("path").item(0).getFirstChild().getNodeValue();
			String filename = filepath+compcode+"\\"+filecompcode+".OUT";
			String filename2 = filepath+compcode+"\\billsList.OUT";
			String filename3 = filepath+compcode+"\\billsPayList.OUT";
	    	FileWriter fw = new FileWriter(filename);
	    	fw.write("CHT0110010VERIFYNTD\n");
	    	dpDelegate.ProcessAccountDataV2(compcode,today,fw,DATATYPE); //20180531修改
	    	dpDelegate.ProcessCategory(compcode,today,fw,DATATYPE); //20181003新增轉產品分類
			//dpDelegate.ProcessAccountData(compcode,today,"C",fw,DATATYPE);     //01轉出所有會計科目  --客戶 要改為抓subject
 			//dpDelegate.ProcessAccountData(compcode,today,"S",fw,DATATYPE);      //02轉出所有會計科目   --供應商 要改為抓subject
			dpDelegate.ProcessCustData(compcode,today,fw,DATATYPE);     //03轉出所有 custinfomf 客戶主檔資料表   --客戶
 			dpDelegate.ProcessVdrData(compcode,today,fw,DATATYPE);      //04轉出所有 supplierbf 供應商主檔資料表   --供應商
			dpDelegate.ProcessItemData(compcode,today,fw,DATATYPE);     //05轉出所有 prodinfobf 料號主檔   --料品
			dpDelegate.ProcessBorrowIn(compcode,today,fw,DATATYPE);    //07轉出所有 procumntmf 借入單  --借進來的貨品
			dpDelegate.ProcessReceviveData(compcode,today,fw,DATATYPE); //06轉出所有 receivexmf 進貨主檔  --已成立的進貨單
			dpDelegate.ProcessSaleReturn(compcode,today,fw,DATATYPE);   //08轉出所有 shipmatlmf type='00'  --已銷貨但退回的單
			dpDelegate.ProcessAdjustInventry(compcode,today,fw,DATATYPE);   //09轉出調整單
			dpDelegate.ProcessSalesData(compcode,today,fw,DATATYPE);    //10轉出所有 shipmatlmf 銷貨主檔  --已成立的出貨單
			dpDelegate.ProcessRecvReturn(compcode,today,fw,DATATYPE);     //11轉出所有 receivexmf type='18'  --已進貨退出的單
			
			if(compcode.equals("J")||compcode.equals("S")||compcode.equals("N"))
			{
				dpDelegate.ProcessInvoiceDataIn(compcode,today,fw,DATATYPE);  //12轉出進項發票
				dpDelegate.ProcessInvoiceDataOut(compcode,today,fw,DATATYPE); //13轉出銷項發票
			}
			dpDelegate.ProcessSaleDiscount(compcode,today,fw,DATATYPE);   //14轉出所有  interoutmf type='A1'--銷貨折讓
			dpDelegate.ProcessRecvDiscount(compcode,today,fw,DATATYPE);   //15轉出所有 interoutmf type='A8'--進貨折讓  
			dpDelegate.ProcessRetBorrow(compcode,today,fw,DATATYPE);     //16轉出所有借入還出單---19
			dpDelegate.ProcessFundsRecvBill(compcode,today,fw,DATATYPE);     //17轉出所有收款單---
			dpDelegate.ProcessFundsPaymentBill(compcode,today,fw,DATATYPE);     //18轉出所有付款單---
			dpDelegate.ProcessCustFtyAcc(compcode,today,fw,DATATYPE);    //19轉出客戶應收帳款餘額
			//dpDelegate.ProcessVoucher(compcode,today,fw,DATATYPE);//20轉出傳票檔
			fw.close();
			System.out.println("----PROCESS Transdelegate FINISH---");
			FileWriter fw2 = new FileWriter(filename2);
			dpDelegate.offSetBillaccounts(compcode,today,fw2,DATATYPE);    //轉出客戶,供應商已沖帳單據含:銷貨,銷折,銷退,進貨,進折,進退
			fw2.close();
			System.out.println("----PROCESS Billaccounts FINISH---");
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
