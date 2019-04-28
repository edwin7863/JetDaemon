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
	//args0->���_�l��,args1->��浲����,args2->��椽�q�O
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
			
			//���ծɥ�16
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
			while(calBase.getTime().before(calCmp.getTime()))//��_�l��p�󵲧���,�N��X���
			{
				today=DateTimeUtil.transDateFormatString(DateTimeUtil.getDateString(calBase),"yyyy-MM-dd","yyyy-MM-dd");
				calBase.add(Calendar.DATE,1);
		    	//�אּ��S�w�϶����i�f�ξP�f��
				//dpDelegate.ProcessAccountData(compcode,today,"C",fw);     //01��X�Ҧ��|�p���  --�Ȥ�
	 			//dpDelegate.ProcessAccountData(compcode,today,"S",fw);      //02��X�Ҧ��|�p���   --������
				//dpDelegate.ProcessCustData(compcode,today,fw);     //03��X�Ҧ� custinfomf �Ȥ�D�ɸ�ƪ�   --�Ȥ�
	 			//dpDelegate.ProcessVdrData(compcode,today,fw);      //04��X�Ҧ� supplierbf �����ӥD�ɸ�ƪ�   --������
				//dpDelegate.ProcessItemData(compcode,today,fw);     //05��X�Ҧ� prodinfobf �Ƹ��D��   --�ƫ~
				dpDelegate.ProcessBorrowIn(compcode,today,fw);    //07��X�Ҧ� procumntmf �ɤJ��  --�ɶi�Ӫ��f�~
				dpDelegate.ProcessReceviveData(compcode,today,fw); //06��X�Ҧ� receivexmf �i�f�D��  --�w���ߪ��i�f��
				dpDelegate.ProcessSaleReturn(compcode,today,fw);   //08��X�Ҧ� shipmatlmf type='00'  --�w�P�f���h�^����
				dpDelegate.ProcessAdjustInventry(compcode,today,fw);   //09��X�վ��
				dpDelegate.ProcessSalesData(compcode,today,fw);    //10��X�Ҧ� shipmatlmf �P�f�D��  --�w���ߪ��X�f��
				dpDelegate.ProcessRecvReturn(compcode,today,fw);     //11��X�Ҧ� receivexmf type='18'  --�w�i�f���h�X����
				
				if(compcode.equals("J")||compcode.equals("S"))
				{
					dpDelegate.ProcessInvoiceDataIn(compcode,today,fw);  //12��X�i���o��
					dpDelegate.ProcessInvoiceDataOut(compcode,today,fw); //13��X�P���o��
				}
				//dpDelegate.ProcessSaleDiscount(compcode,today,fw);   //14��X�Ҧ�  interoutmf type='A1'--�P�f����
				//dpDelegate.ProcessRecvDiscount(compcode,today,fw);   //15��X�Ҧ� interoutmf type='A8'--�i�f����  
				dpDelegate.ProcessRetBorrow(compcode,today,fw);     //16��X�Ҧ��ɤJ�٥X��---19
				//dpDelegate.ProcessFundsBill(compcode,today,fw);     //17��X�Ҧ����ڳ�---
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
