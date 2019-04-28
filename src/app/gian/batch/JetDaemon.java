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
			
			//���ծɥ�16
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
	    	dpDelegate.ProcessAccountDataV2(compcode,today,fw,DATATYPE); //20180531�ק�
	    	dpDelegate.ProcessCategory(compcode,today,fw,DATATYPE); //20181003�s�W�ಣ�~����
			//dpDelegate.ProcessAccountData(compcode,today,"C",fw,DATATYPE);     //01��X�Ҧ��|�p���  --�Ȥ� �n�אּ��subject
 			//dpDelegate.ProcessAccountData(compcode,today,"S",fw,DATATYPE);      //02��X�Ҧ��|�p���   --������ �n�אּ��subject
			dpDelegate.ProcessCustData(compcode,today,fw,DATATYPE);     //03��X�Ҧ� custinfomf �Ȥ�D�ɸ�ƪ�   --�Ȥ�
 			dpDelegate.ProcessVdrData(compcode,today,fw,DATATYPE);      //04��X�Ҧ� supplierbf �����ӥD�ɸ�ƪ�   --������
			dpDelegate.ProcessItemData(compcode,today,fw,DATATYPE);     //05��X�Ҧ� prodinfobf �Ƹ��D��   --�ƫ~
			dpDelegate.ProcessBorrowIn(compcode,today,fw,DATATYPE);    //07��X�Ҧ� procumntmf �ɤJ��  --�ɶi�Ӫ��f�~
			dpDelegate.ProcessReceviveData(compcode,today,fw,DATATYPE); //06��X�Ҧ� receivexmf �i�f�D��  --�w���ߪ��i�f��
			dpDelegate.ProcessSaleReturn(compcode,today,fw,DATATYPE);   //08��X�Ҧ� shipmatlmf type='00'  --�w�P�f���h�^����
			dpDelegate.ProcessAdjustInventry(compcode,today,fw,DATATYPE);   //09��X�վ��
			dpDelegate.ProcessSalesData(compcode,today,fw,DATATYPE);    //10��X�Ҧ� shipmatlmf �P�f�D��  --�w���ߪ��X�f��
			dpDelegate.ProcessRecvReturn(compcode,today,fw,DATATYPE);     //11��X�Ҧ� receivexmf type='18'  --�w�i�f�h�X����
			
			if(compcode.equals("J")||compcode.equals("S")||compcode.equals("N"))
			{
				dpDelegate.ProcessInvoiceDataIn(compcode,today,fw,DATATYPE);  //12��X�i���o��
				dpDelegate.ProcessInvoiceDataOut(compcode,today,fw,DATATYPE); //13��X�P���o��
			}
			dpDelegate.ProcessSaleDiscount(compcode,today,fw,DATATYPE);   //14��X�Ҧ�  interoutmf type='A1'--�P�f����
			dpDelegate.ProcessRecvDiscount(compcode,today,fw,DATATYPE);   //15��X�Ҧ� interoutmf type='A8'--�i�f����  
			dpDelegate.ProcessRetBorrow(compcode,today,fw,DATATYPE);     //16��X�Ҧ��ɤJ�٥X��---19
			dpDelegate.ProcessFundsRecvBill(compcode,today,fw,DATATYPE);     //17��X�Ҧ����ڳ�---
			dpDelegate.ProcessFundsPaymentBill(compcode,today,fw,DATATYPE);     //18��X�Ҧ��I�ڳ�---
			dpDelegate.ProcessCustFtyAcc(compcode,today,fw,DATATYPE);    //19��X�Ȥ������b�ھl�B
			//dpDelegate.ProcessVoucher(compcode,today,fw,DATATYPE);//20��X�ǲ���
			fw.close();
			System.out.println("----PROCESS Transdelegate FINISH---");
			FileWriter fw2 = new FileWriter(filename2);
			dpDelegate.offSetBillaccounts(compcode,today,fw2,DATATYPE);    //��X�Ȥ�,�����Ӥw�R�b��ڧt:�P�f,�P��,�P�h,�i�f,�i��,�i�h
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
