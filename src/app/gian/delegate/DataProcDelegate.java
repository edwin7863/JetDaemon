package app.gian.delegate;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BasicDynaBean;

import app.gian.DbName;
import app.gian.DbUtil;
import app.gian.db.MisDataBaseImp;
import app.gian.db.informix.*;
import app.gian.util.CommonUtil;
import app.gian.util.DateTimeUtil;

public class DataProcDelegate
{

	private static String DB_NAME = DbName.DB_BWS;
	
	//拋轉傳票檔案
	public void ProcessVoucher(String tcompcode,String transDate,
			FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS VOUCHER DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =null;
		String voucherno ="",trandate="",maker="",makerid="",printer="";

		distList =new VoucherDAO(db).findVoucherMfList(tcompcode,transDate);
    	fw.write("[SngProgID]ChiAccnt.Voucher@"+distList.size()+"\n");
    	fw.write("-1,VoucherNo,FloatCodeNo,MakeDate,VoucherClsID,BillCount,Director,Accounter,Permitter,Maker,States,IsTransfer,isPigeonhole,SelfDefine1,SelfDefine2,OriginalNo,Remark,MakerID,PermitterID,Printed,PrintDate,PrintMan,Printer,CashierID,Cashier,OperationMan,DeptID,IsCarry,VoucherFrmNO,IsShift\n");
    	fw.write("-1,VoucherNo,RowNo,SerNo,DepartID,SubjectID,Summary,DebitCredit,CurrID,ExchRate,SourceAmount,TempAmount,Amount,OffsetFlag,OffsetAmount,ProjectID,Quantity,Price,ObjectID,SelfUnitID,ProdtID,ProdName\n");
    	fw.write("-1,VoucherNo,BillNo,SysName,BillName,TranAmount,FieldName,ProgID,SysID,BillID,IsSourceChange\n");
    	fw.write("-1,OffsetDate,OffsetedNo,OffsetedRowNo,OffsetedCurrID,OffsetedExchRate,CurAmount,OffsetNo,OffSetRowNo,OffsetCurrID,OffsetExchRate,IsInit\n");
    	fw.write("-1,OffsetDate,OffsetNo,OffSetRowNo,OffsetCurrID,OffsetExchRate,CurAmount,OffsetedNo,OffsetedRowNo\n");
    	Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean modelMf = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			
			voucherno = modelMf.get("voucherno")==null?"":modelMf.get("voucherno").toString();
			maker =  modelMf.get("maker")==null?"":modelMf.get("maker").toString();
			makerid =  modelMf.get("makerid")==null?"":modelMf.get("makerid").toString();
			printer = CommonUtil.byteToHexString("HP LaserJet Professional P 1102w");
			trandate = modelMf.get("makedate")==null?"":modelMf.get("makedate").toString();
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate, "yyyy-MM-dd"), "yyyyMMdd");

			line.append("0").append(",") //1. -0			
			.append(voucherno).append(",")//2.VoucherNo
			.append(voucherno).append(",")//3.FloatCodeNo
			.append(trandate).append(",")//4.MakeDate
			.append("2").append(",")//5.VoucherClsID -2
			.append("0").append(",")//6.BillCount -0
			.append("").append(",")//7.Director -blank
			.append("").append(",")//8.Accounter -blank
			.append("").append(",")//9.Permitter -blank
			.append(maker).append(",")//10.Maker 
			.append("0").append(",")//11.States -0
			.append("0").append(",")//12.IsTransfer 
			.append("False").append(",")//13.isPigeonhole
			.append("").append(",")//14.SelfDefine1 -blank
			.append("").append(",")//15.SelfDefine2 -blank
			.append("").append(",")//16.OriginalNo -blank
			.append("").append(",")//17.Remark -blank
			.append(makerid).append(",")//18.MakerID
			.append("").append(",")//19.PermitterID -blank
			.append("True").append(",")//20.Printed
			.append(trandate).append(",")//21.PrintDate
			.append(makerid).append(",")//22.PrintMan
			.append(printer).append(",")//23.Printer
			.append("").append(",")//24.CashierID -blank
			.append("").append(",")//25.Cashier -blank
			.append("").append(",")//26.OperationMan -blank
			.append("A").append(",")//27.DeptID -blank
			.append("0").append(",")//28.IsCarry -blank
			.append("").append(",")//29.VoucherFrmNO
			.append("False")//30.IsShift			
		  	.append("\n")
		;
			fw.write(line.toString());
			
			//以主檔單號查明細檔
			List lineList =null;
			String serial="",subjectid="",summary="",currid="",
					exchrate="",sourceamt="0",amount="0";
			boolean dctype=true;
			lineList =  new VoucherDAO(db).findVoucherDfList(tcompcode,voucherno);
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{					
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();		
				StringBuffer lineLine = new StringBuffer();
				
				voucherno = modelLine.get("voucherno")==null?"":modelLine.get("voucherno").toString();
				serial = modelLine.get("serial")==null?"":modelLine.get("serial").toString();
				subjectid = modelLine.get("subjectid")==null?"":modelLine.get("subjectid").toString();
				summary = modelLine.get("summary")==null?"":modelLine.get("summary").toString();
				summary = CommonUtil.byteToHexString(summary);
				dctype = Boolean.getBoolean(modelLine.get("dctype").toString());
				currid = modelLine.get("currid")==null?"":modelLine.get("currid").toString();
				exchrate = modelLine.get("exchrate")==null?"":modelLine.get("exchrate").toString();
				sourceamt = modelLine.get("sourceamount")==null?"":modelLine.get("sourceamount").toString();
				amount = modelLine.get("amount")==null?"":modelLine.get("amount").toString();
				line.append("1").append(",")//1. //-1             
	            .append(voucherno).append(",")//2.VoucherNo
	            .append(rowno).append(",")//3.RowNo
	            .append(serial).append(",")//4.SerNo
	            .append("A").append(",")//5.DepartID
	            .append("subjectid").append(",")//6.SubjectID
	            .append(summary).append(",")//7.Summary
	            .append(dctype).append(",")//8.DebitCredit  True or False 
	            .append(currid).append(",")//9.CurrID
	            .append(exchrate).append(",")//10.ExchRate
	            .append(sourceamt).append(",")//11.SourceAmount
	            .append(amount).append(",")//12.TempAmount
	            .append(amount).append(",")//13.Amount
	            .append("0").append(",")//14.OffsetFlag
	            .append("0").append(",")//15.OffsetAmount
	            .append("").append(",")//16.ProjectID
	            .append("0").append(",")//17.Quantity
	            .append("0").append(",")//18.Price
	            .append("").append(",")//19.ObjectID
	            .append("").append(",")//20.SelfUnitID
	            .append("").append(",")//21.ProdtID
	            .append("")//22.ProdName	                                                                       
		        .append("\n")                   
		        ;
				fw.write(lineLine.toString());
				rowno++;
			}
		}
		
		db.close();
	}
	
	public void ProcessItemData(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS ITEM DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =  new ProdinfobfDao(db).find(tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String filename = "../JetDaemon/ITEM_"+today+".txt";  
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/03"+ntransDate+"_MRG.OUT"; 
		//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiComm.Product@"+distList.size()+"\n");
    	fw.write("-1,ProdID,SubID,ClassID,BarCodeID,UnitID,Unit,ProdName,InvoProdName,InvoName,EngName,CurrID,SuggestPrice,SalesPriceA,SalesPriceB,SalesPriceC,SalesPriceD,SalesPriceE,StdPrice,PackAmt1,PackUnit1,PackAmt2,PackUnit2,ConverUnit,ConverRate,BusiTaxRate,ImpTaxRate,Excise,InventoryID,BatchUsed,EffectDateUsed,ValidDateUsed,DefValidDay,ProdDesc,ProdForm,BaseInc,MinPurch,SafeStock,AdvanceDays,MaterialWare,OverReceRate,PurchPolicy,IsCheck,MajorSupplier,BOutStockDay,BInStockDay,BPurchDate,BSalesDate,SluggishDays,LatestIndate,LatestOutDate,LatestPurchDate,LatestSalesDate,InvalidDate,Main_Des,CCC_CODE,EngUnit,FOBCurrID,FOBPrice,CY20,CY40,HQ40,CY45,InPackUnit,InPackAmt,OutPackUnit,OutPackAmt,TraBoxQty,TraBoxUnit,VolumeUnit,Volume,NetWeightUnit,NetWeight,GrossWeightUnit,GrossWeigh,MEAMTUnit,MEAMT,ProdPic,BStdCost,BAvgCost,CAvgCost,CTotalCost,BackTaxRate,DataVer,MoreRate,UDef1,UDef2,PriceOfTax,InsurRate,InsurRateEx,BTotalCost,PerDays,UsePerms,StdCost,CheckMethod,CheckProjectID,LevelId,StrictLimit,Long,Width,High,LUnit,MVolume,VUnit,NWeight,NUnit,GWeight,GUnit,Area,AUnit,IsLeftOverMat,IsVirtual,IsOptional,IsConfig,IsCharacter,IsOptionalManual,IsOptionalAuto,ProdCodeRuleType,CodeRule,UseOverReceRate,UsePurseCheck,UseProdtCheck,CanModifyCheck,IsTaxProdt,WareType,MergerNo,UseInCtoms,CtmWeight,CtmUnit,CtmLeftover,CtmLostBegin,CtmLostEnd,UseChar,NotIncMRP,NeedDeclare,DefCheckerID,UseSerial,UseSmallBatch,CostType,GWMatID,TakeMatMultiple,UseAuditing,CommodityID,Price,CtmCurrID,UserUnitConver,UserUnitConver1,UserUnitConver2,UnitConver,CommodityName,standard,CustodyUnitID,LLC\n");
    	fw.write("-1,ProdID,UnitID,RowNO,SUnitID,Quantity,SQuantity,Fconver,AutoConver,Memo,MergeOutState\n");
    	fw.write("-1,ProdID,SaleGrade,UnitID,CurrID,Price,PriceOfTax,MergeOutState,MainRela\n");
    	fw.write("-1,ProdID,UnitID,CurrID,Price,PriceOfTax,MergeOutState,MainRela\n");
    	fw.write("-1,ProdID,CCID,IsMust,SerNO,ValueSource\n");
    	fw.write("-1,ProdID,CCID,CID,IsDefault,IsProhibit,SerNO,Memo\n");
    	fw.write("-1,ProdID,CCID,CharComb,OrderNO,PriorProdID\n");
    	fw.write("-1,BillNo,PicNO,ParentRowNo,ParentSubRowNo,PicID,CurrProdID\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			line.append("0").append(",")                                                                     //A 
			    .append(model.get("prodnubr")==null?"":model.get("prodnubr").toString().trim()).append(",")  //B 
			    .append(model.get("catex1st")==null?"":model.get("catex1st").toString().trim()).append(",")  //C  SubID
			    .append(model.get("chicode")==null?"":model.get("chicode").toString().trim()).append(",")  //D  ClassID
			    .append("").append(",")                                                                      //E
			    .append("").append(",")                                                                      //F
			    .append("PCS").append(",")                                                                   //G
			    .append(model.get("prodname")==null?"":model.get("prodname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")  //H
			    .append(model.get("origname")==null?"":model.get("origname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")  //I
			    .append("").append(",")                                                                      //J
			    .append("").append(",")                                                                      //K
			    .append("USD").append(",")                                                                   //L
			    .append("0").append(",")                                                                     //M
			    .append("0").append(",")                                                                     //N
			    .append("0").append(",")                                                                     //O
			    .append("0").append(",")                                                                     //P
			    .append("0").append(",")                                                              //Q
			    .append("0").append(",")                                                              //R
			    .append("0").append(",")                                                              //S
			    .append("0").append(",")                                                              //T
			    .append("PCS").append(",")                                                            //U
			    .append("0").append(",")                                                              //V
			    .append("PCS").append(",")                                                            //W
			    .append("PCS").append(",")                                                            //X
			    .append("0").append(",")                                                              //Y
			    .append("0.05").append(",")                                                           //Z
			    .append("0").append(",")                                                              //AA
			    .append("0").append(",")                                                              //AB
			    .append("").append(",")                                                               //AC
			    .append("FALSE").append(",")                                                          //AD
			    .append("FALSE").append(",")                                                          //AE
			    .append("FALSE").append(",")                                                          //AF
			    .append("0").append(",")                                                              //AG
			    .append("").append(",")                                                               //AH
			    .append("0").append(",")                                                               //AI
			    .append("0").append(",")                                                               //AJ
			    .append(model.get("minsoqty")==null?"":model.get("minsoqty").toString().trim()).append(",")  //AK
			    .append("FALSE").append(",")                                                          //AL 
			    .append("0").append(",")                                                              //AM
			    .append("0").append(",")                                                              //AN
			    .append("0").append(",")                                                              //AO
			    .append("0").append(",")                                                              //AP
			    .append("FALSE").append(",")                                                          //AQ
			    .append("").append(",")                                                               //AR
			    .append("0").append(",")                                                              //AS
			    .append("0").append(",")                                                              //AT
			    .append("0").append(",")                                                              //AU
			    .append("0").append(",")                                                              //AV
			    .append("0").append(",")                                                              //AW
			    .append("").append(",")                                                               //AX
			    .append("").append(",") //last out                                                    //AY
			    .append("").append(",") //                                                            //AZ
			    .append("").append(",")                                                               //BA
			    .append("0").append(",")                                                              //BB
			    .append("").append(",")                                                               //BC
			    .append("").append(",")                                                               //BD
			    .append("").append(",")                                                               //BE
			    .append("").append(",")                                                               //BF
			    .append("0").append(",")                                                              //BG
			    .append("0").append(",")                                                              //BH
			    .append("0").append(",")                                                              //BI
			    .append("0").append(",")                                                              //BJ
			    .append("0").append(",")                                                              //BK
			    .append("PCS").append(",")                                                            //BL
			    .append("0").append(",")                                                              //BM
			    .append("").append(",")                                                               //BN
			    .append("0").append(",")                                                              //BO
			    .append("0").append(",")                                                              //BP
			    .append("PCS").append(",")                                                            //BQ
			    .append("PCS").append(",")                                                            //BR 
			    .append("0").append(",")                                                              //BS
			    .append("").append(",")                                                               //BT
			    .append("0").append(",")                                                              //BU
			    .append("").append(",")                                                               //BV
			    .append("0").append(",")                                                              //BW
			    .append("").append(",")                                                               //BX
			    .append("").append(",")                                                               //BY
			    .append("").append(",")                                                               //BZ
			    .append("").append(",")                                                               //CA
			    .append("").append(",")                                                               //CB
			    .append("").append(",")                                                               //CC
			    .append("").append(",") //totalcost                                                   //CD
			    .append("0").append(",")                                                              //CE
			    .append("0").append(",")                                                              //CF
			    .append("0").append(",")                                                              //CG
			    .append("").append(",")                                                               //CH
			    .append("").append(",")                                                               //CI
			    .append("FALSE").append(",")                                                          //CJ
			    .append("0").append(",")                                                              //CK
			    .append("0").append(",")                                                              //CL
			    .append("").append(",")                                                               //CM
			    .append("0").append(",")                                                              //CN
			    .append("TRUE").append(",")                                                           //CO
			    .append("").append(",")                                                               //CP
			    .append("1").append(",")                                                              //CQ
			    .append("").append(",")                                                               //CR
			    .append("").append(",")                                                               //CS
			    .append("0").append(",")                                                              //CT
			    .append("0").append(",")                                                              //CU
			    .append("0").append(",")                                                              //CV
			    .append("0").append(",")                                                              //CW
			    .append("CM").append(",")                                                             //CX
			    .append("1").append(",")                                                              //CY
			    .append("PSC").append(",")                                                            //CZ
			    .append("0").append(",")                                                              //DA
			    .append("KG").append(",")                                                             //DB
			    .append("0").append(",")                                                              //DC
			    .append("KG").append(",")                                                             //DD
			    .append("0").append(",")                                                              //DE
			    .append("SCM").append(",")                                                            //DF
			    .append("FALSE").append(",")                                                          //DG 
			    .append("FALSE").append(",")                                                          //DH
			    .append("FALSE").append(",")                                                          //DI
			    .append("FALSE").append(",")                                                          //DJ
			    .append("FALSE").append(",")                                                          //DK
			    .append("FALSE").append(",")                                                          //DL
			    .append("FALSE").append(",")                                                          //DM
			    .append("").append(",")                                                               //DN
			    .append("").append(",")                                                               //DO
			    .append("FALSE").append(",")                                                          //DP
			    .append("FALSE").append(",")                                                          //DQ 
			    .append("FALSE").append(",")                                                          //DR
			    .append("FALSE").append(",")                                                          //DS
			    .append("FALSE").append(",")                                                          //DT
			    .append("0").append(",")                                                              //DU
			    .append("").append(",")                                                               //DV
			    .append("FALSE").append(",")                                                          //DW
			    .append("0").append(",")                                                              //DX
			    .append("").append(",")                                                               //DY 
			    .append("FALSE").append(",")                                                          //DZ
			    .append("0").append(",")                                                              //EA
			    .append("0").append(",")                                                              //EB
			    .append("FALSE").append(",")                                                          //EC
			    .append("FALSE").append(",")                                                          //ED 
			    .append("FALSE").append(",")                                                          //EE 
			    .append("").append(",")                                                               //EF
			    .append("FALSE").append(",")                                                          //EG
			    .append("FALSE").append(",")                                                          //EH
			    .append("2").append(",")                                                              //EI
			    .append("").append(",")                                                               //EJ
			    .append("0").append(",")                                                              //EK
			    .append("FALSE").append(",")                                                          //EL
			    .append("").append(",")                                                               //EM
			    .append("").append(",")                                                               //EN
			    .append("").append(",")                                                               //EO
			    .append("").append(",")                                                               //EP 
			    .append("").append(",")                                                               //EQ
			    .append("").append(",")                                                               //ER 
			    .append("").append(",")                                                               //ES
			    .append("").append(",")                                                               //ET
			    .append("").append(",")                                                               //EU 
			    .append("").append(",")                                                               //EV
			    .append("")//.append(",")                                                             //EW
			    .append("\n") 
			;
			fw.write(line.toString());
		}
		//fw.close();
		db.close();
	}
	
	//產生會計科目2018/05/29修訂
	public void ProcessAccountDataV2(String tcompcode,String transDate,
			FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS ACCOUNTING DATA======");
		Vector data = new Vector();
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =null;
		String subjectid ="";
		String subject ="";
		String title ="";
		String subjectname ="";
		String SubjectEngName="";
		String SubGrpID="";
		String SubClsID="";
		String SurplusDire="";
		String CurrCheck="";
		String CurrID="";
		String parentsubid="";
		distList =new SubjectDAO(db).findSubjectList(tcompcode,transDate);

    	fw.write("[SngProgID]ChiComm.Subject@"+distList.size()+"\n");
    	fw.write("-1,SubjectID,SubjectName,SubjectEngName,IsOffset,OffsetDate,CurrCheck,IsAdjustRate,CurrID,Summary,Description,IsUseSubject,ParentSubID,SubGrpID,SubClsID,IsCurrFund,SubjectType,IsUseProject,IsUseQuantity,Unit,SubLevel,CashBank,SurplusDire,IsCheckDepart,CheckOption,IntraCheck,IsUseProdt\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			
			subjectid = model.get("subjectid").toString();
			subjectname = model.get("subjectname")==null?"":model.get("subjectname").toString().replace(',', ' ').replace('\'', ' ').trim();
			SubjectEngName="";
			SubGrpID=model.get("subgrpid").toString();
			SubClsID=model.get("subclsid").toString();
			parentsubid=model.get("parentsubid").toString();
			SurplusDire="True";
/*			if(AccountType.equals("C"))
			{
				accountxxid = model.get("custxxid").toString();
				accountabbr = model.get("custabbr")==null?"":model.get("custabbr").toString().replace(',', ' ').replace('\'', ' ').trim();
				subject="應收帳款-";
				title="1141";
				SubjectEngName="accounts receivable";
				SubGrpID="1";
				SurplusDire="True";
			}
			else
			{
				accountxxid = model.get("vndrcode").toString();
				accountabbr = model.get("vndrabbr")==null?"":model.get("vndrabbr").toString().replace(',', ' ').replace('\'', ' ').trim();
				subject="應付帳款-";
				title="2141";
				SubjectEngName="accounts payable";
				SubGrpID="2";
				SurplusDire="False";
			}*/
			if(tcompcode.equals("H")||tcompcode.equals("I")||tcompcode.equals("M")||tcompcode.equals("W"))
			{
				CurrCheck="0";
				CurrID="";
			}
			else
			{
				CurrCheck="2";
				CurrID="USD";
			}
			line.append("0").append(",") //A Serial			
			    .append(subjectid).append(",")//B SubjectID
				.append(subjectname).append(",")//C SubjectName
				.append(SubjectEngName).append(",")//D SubjectEngName
				.append("False").append(",")//E IsOffset 
				.append("0").append(",") //F OffsetDate 
				.append(CurrCheck).append(",")//G  CurrCheck 
				.append("False").append(",")//H IsAdjustRate 
				.append(CurrID).append(",")//I CurrID 
				.append("").append(",")//J Summary 
				.append("").append(",")//K Description 
				.append("True").append(",")//L IsUseSubject 
				.append(parentsubid).append(",")//M ParentSubID 
				.append(SubGrpID).append(",")//N SubGrpID 
				.append(SubClsID).append(",")//O SubClsID 
				.append("False").append(",")//P IsCurrFund 
				.append("0").append(",")//Q SubjectType 
				.append("True").append(",")//R IsUseProject 
				.append("False").append(",") //S IsUseQuantity  
				.append("").append(",")//T Unit 
				.append("2").append(",")//U SubLevel 
				.append("0").append(",")//V CashBank 
				.append(SurplusDire).append(",")//W SurplusDire 
				.append("True").append(",")//X IsCheckDepart 
				.append("0").append(",")//Y CheckOption 
				.append("False").append(",")//Z IntraCheck 
				.append("False")//AA IsUseProdt
		  	.append("\n")
		;
			fw.write(line.toString());
		}
		db.close();
	}
	
	//產生會計科目
	public void ProcessAccountData(String tcompcode,String transDate,
			String AccountType,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS ACCOUNTING DATA======");
		Vector data = new Vector();
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =null;
		String accountxxid ="";
		String subject ="";
		String title ="";
		String accountabbr ="";
		String SubjectEngName="";
		String SubGrpID="";
		String SurplusDire="";
		String CurrCheck="";
		String CurrID="";
		if(AccountType.equals("C"))
		{
			distList =new CustinfomfDao(db).find(transDate);
		}
		else
		{
			distList =new supplierbfDao(db).find(transDate);	
		}
    	fw.write("[SngProgID]ChiComm.Subject@"+distList.size()+"\n");
    	fw.write("-1,SubjectID,SubjectName,SubjectEngName,IsOffset,OffsetDate,CurrCheck,IsAdjustRate,CurrID,Summary,Description,IsUseSubject,ParentSubID,SubGrpID,SubClsID,IsCurrFund,SubjectType,IsUseProject,IsUseQuantity,Unit,SubLevel,CashBank,SurplusDire,IsCheckDepart,CheckOption,IntraCheck,IsUseProdt\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			if(AccountType.equals("C"))
			{
				accountxxid = model.get("custxxid").toString();
				accountabbr = model.get("custabbr")==null?"":model.get("custabbr").toString().replace(',', ' ').replace('\'', ' ').trim();
				subject="應收帳款-";
				title="1141";
				SubjectEngName="accounts receivable";
				SubGrpID="1";
				SurplusDire="True";
			}
			else
			{
				accountxxid = model.get("vndrcode").toString();
				accountabbr = model.get("vndrabbr")==null?"":model.get("vndrabbr").toString().replace(',', ' ').replace('\'', ' ').trim();
				subject="應付帳款-";
				title="2141";
				SubjectEngName="accounts payable";
				SubGrpID="2";
				SurplusDire="False";
			}
			if(tcompcode.equals("H")||tcompcode.equals("I")||tcompcode.equals("M")||tcompcode.equals("W"))
			{
				CurrCheck="0";
				CurrID="";
			}
			else
			{
				CurrCheck="2";
				CurrID="USD";
			}
			line.append("0").append(",") //A Serial			
			    .append(title+accountxxid).append(",")//B SubjectID
				.append(subject+accountabbr).append(",")//C SubjectName
				.append(SubjectEngName).append(",")//D SubjectEngName
				.append("False").append(",")//E IsOffset 
				.append("0").append(",") //F OffsetDate 
				.append(CurrCheck).append(",")//G  CurrCheck 
				.append("False").append(",")//H IsAdjustRate 
				.append(CurrID).append(",")//I CurrID 
				.append("").append(",")//J Summary 
				.append("").append(",")//K Description 
				.append("True").append(",")//L IsUseSubject 
				.append(title+"00000").append(",")//M ParentSubID 
				.append(SubGrpID).append(",")//N SubGrpID 
				.append(SubGrpID+"1").append(",")//O SubClsID 
				.append("False").append(",")//P IsCurrFund 
				.append("0").append(",")//Q SubjectType 
				.append("True").append(",")//R IsUseProject 
				.append("False").append(",") //S IsUseQuantity  
				.append("").append(",")//T Unit 
				.append("2").append(",")//U SubLevel 
				.append("0").append(",")//V CashBank 
				.append(SurplusDire).append(",")//W SurplusDire 
				.append("True").append(",")//X IsCheckDepart 
				.append("0").append(",")//Y CheckOption 
				.append("False").append(",")//Z IntraCheck 
				.append("False")//AA IsUseProdt
		  	.append("\n")
		;
			fw.write(line.toString());
		}
		db.close();
	}
	
	//轉入產品分類
	public void ProcessCategory(String tcompcode,String transDate,
			FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS ACCOUNTING DATA======");
		Vector data = new Vector();
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =null;
		String cateId ="";
		String cateName ="";

		String AccInventoryID ="121100000";
		String AccPurchasedID ="512100000";
		String ReturnPurchaseID="512300000";
		String AccSaleID="411100000";
		String AccSaleCostID="511100000";
		String ReturnSaleID="417100000";

		distList =new ProdinfobfDao(db).findCategory(tcompcode,transDate);

    	fw.write("[SngProgID]ChiComm.ProductClass@"+distList.size()+"\n");
    	fw.write("-1,ClassID,ClassName,EngName,AccInventoryID,AccPurchasedID,ReturnPurchaseID,AccSaleID,AccSaleCostID,ReturnSaleID,GiftExpenseID,OtherIncome,OtherExpense,OtherCost,MaterialWarehouseID,DataVer\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			
			cateId = model.get("chicode").toString();
			cateName = model.get("catename")==null?"":model.get("catename").toString().replace(',', ' ').replace('\'', ' ').trim();

			line.append("0").append(",") //A 			
			    .append(cateId).append(",")//B ClassID
				.append(cateName).append(",")//C ClassName
				.append("").append(",")//D EngName
				.append(AccInventoryID).append(",")//E AccInventoryID 
				.append(AccPurchasedID).append(",") //F AccPurchasedID 
				.append(ReturnPurchaseID).append(",")//G  ReturnPurchaseID 
				.append(AccSaleID).append(",")//H AccSaleID 
				.append(AccSaleCostID).append(",")//I AccSaleCostID 
				.append(ReturnSaleID).append(",")//J ReturnSaleID 
				.append("").append(",")//K GiftExpenseID 
				.append("").append(",")//L OtherIncome 
				.append("").append(",")//M OtherExpense 
				.append("").append(",")//N OtherCost 
				.append("0").append(",")//O DataVer 
				
		  	.append("\n")
		;
			fw.write(line.toString());
		}
		db.close();
	}
	
	//是否還要轉匯款帳號??,要把其他詳細的資料轉入
	public void ProcessCustData(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS CUST DATA======");
		Vector data = new Vector();
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =new CustinfomfDao(db).find(transDate);
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/01"+ntransDate+"_MRG.OUT";  
    	//FileWriter fw = new FileWriter(filename);
    	fw.write("[SngProgID]ChiComm.Customer@"+distList.size()+"\n");
    	fw.write("-1,ID,FundsAttribution,FullName,ShortName,ClassID,AreaID,CurrencyID,IsTemp,IsForeign,TaxNo,ChiefName,Capitalization,LinkMan,LinkManProf,Telephone1,Telephone2,Telephone3,MobileTel,FaxNo,Moderm,IndustrialClass,PersonID,Email,WebAddress,ServerID,DealerID,PriceofTax,DirectCust,VIP,VIPLevel,DataVer,MemberCodeNo,MembercodeDate,IdentityNO,MaritalStatus,SexDistinction,Metier,NativePlace,NativeAddress,FamilyAddress,ZipCode,InvoiceHead,GatherOther,CheckOther,InvoTax,UsePerms,SrcID,SrcName,EngFullName,EngShortName,EngLinkMan,EngLinkManProf,EngWayOfRecv,EngWayOfDeliv,AccCommi,AccCommiPaied,AddrID,Memo,TypeOfBillExpire,DaysOfBillExpire,PriceRank,RateOfDiscount,AddField1,AddField2,DeliverAddrID,EngAddrID,DelivZoneNo,AddrOfInvo,EarliestTradeDate,FirstTradeDate,LatelyTradeDate,LatelyReturnDate,FinalTradeDate,InvoiceType,TaxKind,AccReceivable,AccBillRecv,AccAdvRecv,BankAccount,BankID,CreditLevel,AmountQuota,BillQuota,RecvWay,DistDays,DayOfClose,DayOfRecv,UnEnCashQuota,InvoiceStyle,Term,NOChkUnEnCashQuota\n");
    	fw.write("-1,ID,AddrID,Address,ZipCode,LinkMan,Telephone,LinkManProf,FaxNo,WalkAddr,Memo\n");
    	fw.write("-1,CustID,MarkID,MarkName,MainMark\n");
    	fw.write("-1,ID,AccID,AccBankID,AccountID,AccountName,InvalidDate,IsMasterID\n");
    	fw.write("-1,CustomID,RowNo,SerNo,MasterLink,PersonName,ProfTitle,DepartName,Telephone,ExtCode,Mobile,FaxNo,Email,Memo,MergeOutState\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			String custxxid = model.get("custxxid").toString();
			String serialno =" ";
			if(tcompcode.equals("C"))
				serialno = " ";
			else
				serialno = model.get("serialno")==null?" ":model.get("serialno").toString().trim();
			String isFore = "FALSE";
			if(custxxid.startsWith("B"))
				isFore = "TRUE";
			line.append("0").append(",")                                                                  //A 1Serial
			    .append(custxxid).append(",")                                                             //B 2ID
				.append(custxxid).append(",")                                                             //C 3FundsAttribution
				.append(model.get("custname")==null?"":model.get("custname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //D 4FullName
				.append(model.get("custname")==null?"":model.get("custname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //E 5ShortName
				.append(model.get("datatype")==null?" ":model.get("datatype").toString().trim()).append(",") //class id                                                       //F 6ClassID
				.append("").append(",") //area id                                                        //G 7
				.append(model.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,model.get("currency").toString())).append(",")//H 8CurrencyID
				.append("FALSE").append(",")                                                              //I 9IsTemp
				.append(isFore).append(",")                                                               //J 10IsForeign
				.append(serialno).append(",")      //K 11TaxNo
				.append(model.get("compboss")==null?" ":model.get("compboss").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")    //L ChiefName 負責人
				.append("0").append(",")                                                                  //M Capitalization
				.append(model.get("contacct")==null?" ":model.get("contacct").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //N LinkMan
				.append("").append(",")                                                                  //O LinkManProf
				.append(model.get("telxnubr")==null?" ":model.get("telxnubr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //P Telephone1
				.append(model.get("telxnubr2")==null?" ":model.get("telxnubr2").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //Q Telephone2
				.append(model.get("telxnubr3")==null?" ":model.get("telxnubr3").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //R Telephone3
				.append(model.get("mobile")==null?" ":model.get("mobile").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //S MobileTel
				.append(model.get("faxxnubr")==null?" ":model.get("faxxnubr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //T FaxNo
				.append("").append(",")                                                                  //U Moderm
				.append("").append(",")                                                                  //V IndustrialClass
				.append(model.get("salecode")==null?" ":model.get("salecode").toString().trim()).append(",")  //W PersonID
				.append(model.get("mailaddr")==null?"":model.get("mailaddr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                                                                  //X Email
				.append("").append(",")                                                                  //Y WebAddress
				.append("").append(",")                                                                  //Z ServerID
				.append("").append(",")                                                                  //AA DealerID
				.append("FALSE").append(",")                                                              //AB PriceofTax
				.append("FALSE").append(",")                                                              //AC DirectCust
				.append("FALSE").append(",")                                                              //AD VIP
				.append("").append(",")                                                                  //AE VIPLevel
				.append("1").append(",")                                                                  //AF DataVer
				.append("").append(",")                                                                  //AG MemberCodeNo
				.append("0").append(",")                                                                  //AH MembercodeDate
				.append("").append(",")                                                                  //AI IdentityNO
				.append("0").append(",")                                                                  //AJ MaritalStatus
				.append("FALSE").append(",")                                                              //AK SexDistinction
				.append("").append(",")                                                                  //AL Metier
				.append("").append(",")                                                                  //AM NativePlace
				.append("").append(",")                                                                  //AN NativeAddress
				.append("").append(",")                                                                  //AO FamilyAddress
				.append("").append(",")                                                                  //AP ZipCode
				.append(model.get("custname")==null?" ":model.get("custname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //AQ  InvoiceHead   
				.append("").append(",")                                                                  //AR GatherOther  付款方式
				.append("").append(",")                                                                  //AS CheckOther
				.append("TRUE").append(",")                                                               //AT InvoTax
				.append("TRUE").append(",")                                                               //AU UsePerms
				.append("1").append(",")                                                                  //AV SrcID
				.append("").append(",")                                                                  //AW SrcName
				.append(model.get("custname")==null?" ":model.get("custname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                                                                  //AX EngFullName
				.append(model.get("foriname")==null?" ":model.get("foriname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                                                                  //AY EngShortName
				.append("").append(",")                                                                  //AZ EngLinkMan
				.append("").append(",")                                                                  //BA EngLinkManProf 
				.append("").append(",")                                                                  //BB EngWayOfRecv
				.append("").append(",")                                                                  //BC EngWayOfDeliv  
				.append("").append(",")                                                                  //BD AccCommi
				.append("").append(",")                                                                  //BE AccCommiPaied
				.append("1").append(",")                                                                  //BF AddrID
				.append("").append(",")                                                                  //memo BG Memo
				.append("").append(",")                                                                  //BH TypeOfBillExpire
				.append("").append(",")                                                                  //BI DaysOfBillExpire
				.append("").append(",")                                                                  //BJ PriceRank
				.append("").append(",")                                                                  //BK RateOfDiscount
				.append("").append(",")                                                                  //BL AddField1
				.append("").append(",")                                                                  //BM AddField2
				.append("").append(",")                                                                  //BN DeliverAddrID
				.append("").append(",")                                                                  //BO EngAddrID
				.append("").append(",")                                                                  //BP DelivZoneNo
				.append("").append(",")                                                                  //BQ AddrOfInvo
				.append("").append(",")                                                                  //BR EarliestTradeDate
				.append("").append(",")                                                                  //BS FirstTradeDate
				.append("").append(",")                                                                  //BT LatelyTradeDate
				.append("").append(",")                                                                  //BU LatelyReturnDate
				.append("").append(",")                                                                  //BV FinalTradeDate
				.append("31").append(",")                                                                  //BW InvoiceType
				.append("0").append(",")                                                                  //BX TaxKind
				.append("1141"+custxxid).append(",")                                                                  //BY AccReceivable 會計科目
				.append("113100000").append(",")                                                          //BZ AccBillRecv 會計科目
				.append("226100000").append(",")                                                          //CA AccAdvRecv 會計科目
				.append("").append(",")                                                                  //CB BankAccount
				.append("").append(",")                                                                  //CC BankID
				.append("").append(",")                                                                  //CD CreditLevel
				.append("").append(",")                                                                  //CE AmountQuota
				.append("").append(",")                                                                  //CF BillQuota
				.append("").append(",")                                                                  //CG RecvWay
				.append("").append(",")                                                                  //CH DistDays
				.append("").append(",")                                                                  //CI DayOfClose
				.append("").append(",")                                                                  //CJ DayOfRecv
				.append("").append(",")                                                                  //CK UnEnCashQuota
				.append("1").append(",")                                                                  //CL InvoiceStyle
				.append("").append(",")                                                                  //CM Term
				.append("")//.append(",")                                                                  //CN NOChkUnEnCashQuota
				.append("\n")
			;
			line.append("1").append(",")                                                                  //A 
		  	.append(custxxid).append(",")                                                                 //B ID
			.append("1").append(",")                                                                      //C AddrID
			.append(model.get("compaddr")==null?" ":model.get("compaddr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")           //D Address
		  	.append("").append(",")               //E ZipCode
		  	.append(model.get("contacct")==null?" ":model.get("contacct").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")          //F LinkMan
		  	.append(model.get("telxnubr")==null?" ":model.get("telxnubr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")          //G Telephone
		  	.append("").append(",")          //H LinkManProf
		  	.append(model.get("faxxnubr")==null?" ":model.get("faxxnubr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                                                                      //I  FaxNo
		  	.append("").append(",")                                                                      //J WalkAddr
		  	.append("")//.append(",")                                                                      //K Memo
		  	.append("\n")
		;
			fw.write(line.toString());
		}
		//fw.close();
		db.close();
	}
	
	//客戶應收帳款,應付帳款總表
	public void ProcessCustFtyAcc(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS CUST FTYACC DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =new CustinfomfDao(db).findCustAccount(transDate,tcompcode);
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/01"+ntransDate+"_MRG.OUT";  
    	//FileWriter fw = new FileWriter(filename);
    	fw.write("[SngProgID]CHIComm.CustFtyAcc@"+distList.size()+"\n");
    	fw.write("-1,SysID,ID,CurrID,StartExchRate,StartAdvRecv,StartAdvOffseted,StartReceivable,StartOffseted,CurAdvRecv,CurReceivable,StartCommission,StartComOffseted,CurCommission,ShiftAddMoney,ShiftUseMoney,NoChkStartOffSet,NoChkStartAdvOffSet,NoChkStartComOffSet,NoChkCurrRece,NoChkCurrAdv,NoChkCurrCom,NoChkCurrReceOffSet,NoChkCurrAdvOffSet,NoChkCurrComOffSet,NoChkShiftUseMoney,ShiftStartRece,ShiftStartAdv,StartAdvExchRate,StartCommExchRate,LocStartAdvRecv,LocStartAdvOffseted,NoChkLocStartAdvOffSet,LocCurAdvRecv,NoChkLocCurrAdv,LocStartReceivable,LocStartOffseted,NoChkLocStartOffSet,LocCurReceivable,NoChkLocCurrRece,LocStartCommission,LocStartComOffseted,NoChkLocStartComOffSet,LocCurCommission,NoChkLocCurrCom,LocShiftAddMoney,LocShiftUseMoney,NoChkLocShiftUseMoney,LocShiftStartAdv,NoPowerToViewAdvPerms\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			String custxxid = model.get("custxxid").toString();
			String flag = model.get("flag").toString();
			int sysId=0;
			if(flag.equals("G"))
				sysId=0;
			else if(flag.equals("P"))
				sysId=1;
			String isFore = "FALSE";
			if(custxxid.startsWith("B"))
				isFore = "TRUE";
			line.append(sysId).append(",")                                                                  //A 1 -1 0是應收,1是應付
			    .append("2").append(",")                                                             //B 2SysID
				.append(custxxid).append(",")                                                             //C 3ID
				.append(model.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,model.get("currency").toString())).append(",")      //D 4CurrID
				.append(model.get("exchrate")==null?"0":model.get("exchrate").toString().trim()).append(",")      //E 5StartExchRate
				.append("0").append(",")  //F 6StartAdvRecv
				.append("0").append(",") //area id                                                        //G 7StartAdvOffseted
				.append("0").append(",")//H 8StartReceivable
				.append("0").append(",")  //I 9StartOffseted
				.append(model.get("curadvrecv")==null?"0":model.get("curadvrecv").toString().trim()).append(",")   //J 10CurAdvRecv --客戶的預收帳款餘額
				.append(model.get("curreceivable")==null?"0":model.get("curreceivable").toString().trim()).append(",")     //K 11CurReceivable --客戶系統設定幣別的應收帳款餘額
				.append("0").append(",")      //M 12StartCommission
				.append("0").append(",")      //M 13StartComOffseted
				.append("0").append(",")      //N 14CurCommission
				.append("0").append(",")      //O 15ShiftAddMoney
				.append("0").append(",")      //P 16ShiftUseMoney
				.append("0").append(",")      //Q 17NoChkStartOffSet
				.append("0").append(",")      //R 18NoChkStartAdvOffSet
				.append("0").append(",")      //S 19NoChkStartComOffSet
				.append("0").append(",")      //T 20NoChkCurrRece
				.append("0").append(",")      //U 21NoChkCurrAdv
				.append("0").append(",")      //V 22NoChkCurrCom
				.append("0").append(",")      //W 23NoChkCurrReceOffSet
				.append("0").append(",")      //X 24NoChkCurrAdvOffSet
				.append("0").append(",")      //Y 25NoChkCurrComOffSet
				.append("0").append(",")      //Z 26NoChkShiftUseMoney
				.append("0").append(",")      //AA 27ShiftStartRece
				.append("0").append(",")      //AB 28ShiftStartAdv
				.append("0").append(",")      //AC 29StartAdvExchRate
				.append("0").append(",")      //AD 30StartCommExchRate
				.append("0").append(",")      //AE 31LocStartAdvRecv
				.append("0").append(",")      //AF 32LocStartAdvOffseted
				.append("0").append(",")      //AG 33NoChkLocStartAdvOffSet
				.append(model.get("loccuradvrecv")==null?"0":model.get("loccuradvrecv").toString().trim()).append(",")      //AH 34LocCurAdvRecv --本幣的預收帳款餘額
				.append("0").append(",")      //AI 35NoChkLocCurrAdv
				.append("0").append(",")      //AJ 36LocStartReceivable
				.append("0").append(",")      //AK 37LocStartOffseted
				.append("0").append(",")      //AL 38NoChkLocStartOffSet
				.append(model.get("loccurreceivable")==null?"0":model.get("loccurreceivable").toString().trim()).append(",")      //AM 39LocCurReceivable 本幣的應收帳款餘額
				.append("0").append(",")      //AN 40NoChkLocCurrRece
				.append("0").append(",")      //AO 41LocStartCommission
				.append("0").append(",")      //AP 42LocStartComOffseted
				.append("0").append(",")      //AQ  43NoChkLocStartComOffSet 
				.append("0").append(",")      //AR 44LocCurCommission
				.append("0").append(",")      //AS 45NoChkLocCurrCom
				.append("0").append(",")      //AT 46LocShiftAddMoney
				.append("0").append(",")      //AU 47LocShiftUseMoney
				.append("0").append(",")      //AV 48NoChkLocShiftUseMoney
				.append("0").append(",")      //AW 49LocShiftStartAdv
				.append("")                  //AX 50NoPowerToViewAdvPerms
				.append("\n")
			;
			fw.write(line.toString());
		}
		//fw.close();
		db.close();
	}
	
	public void ProcessVdrData(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS VDR DATA======");
		Vector data = new Vector();
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =new supplierbfDao(db).find(transDate);

		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String filename = "../JetDaemon/VENDOR_"+today+".txt";  
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/02"+ntransDate+"_MRG.OUT"; 
		//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiComm.Factory@"+distList.size()+"\n");
    	fw.write("-1,ID,FundsAttribution,ClassID,AreaID,CurrencyID,FullName,IsTemp,IsForeign,TaxNo,ShortName,ChiefName,Capitalization,LinkMan,LinkManProf,Telephone1,Telephone2,Telephone3,MobileTel,PersonID,Moderm,FaxNo,IndustrialClass,Email,WebAddress,IsFactory,PriceofTax,InvoiceHead,GatherOther,CheckOther,InvoTax,UsePerms,PlanPerson,EngFullName,EngShortName,EngLinkMan,EngLinkManProf,EngWayOfRecv,EngWayOfDeliv,AccCommi,AccCommiPaied,AddrID,Memo,TypeOfBillExpire,DaysOfBillExpire,PriceRank,RateOfDiscount,AddField1,AddField2,DeliverAddrID,EngAddrID,DelivZoneNo,AddrOfInvo,EarliestTradeDate,FirstTradeDate,LatelyTradeDate,LatelyReturnDate,FinalTradeDate,InvoiceType,TaxKind,AccReceivable,AccBillRecv,AccAdvRecv,BankAccount,BankID,CreditLevel,AmountQuota,BillQuota,RecvWay,DistDays,DayOfClose,DayOfRecv,UnEnCashQuota,InvoiceStyle,Term,PrepayRate\n");
    	fw.write("-1,ID,AddrID,ZipCode,Address,LinkMan,LinkManProf,Telephone,FaxNo,WalkAddr,Memo\n");
    	fw.write("-1,CustID,MarkID,MarkName,MainMark\n");
    	fw.write("-1,ID,AccID,AccBankID,AccountID,AccountName,InvalidDate,IsMasterID\n");
    	fw.write("-1,CustomID,RowNo,SerNo,MasterLink,PersonName,ProfTitle,DepartName,Telephone,ExtCode,Mobile,FaxNo,Email,Memo,MergeOutState\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			String vndrcode = model.get("vndrcode").toString();
			String isFore = "FALSE";
			if(vndrcode.startsWith("B"))
				isFore = "TRUE";
			line.append("0").append(",")																						//A
			    .append(vndrcode).append(",")																                    //B
				.append(vndrcode).append(",")                                                                                   //C
				.append(model.get("vndrcat")==null?"":model.get("vndrcat").toString()).append(",")                                                                                        //D
				.append("").append(",")                                                                                        //E
				.append(model.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,model.get("currency").toString())).append(",")//F CurrencyID
				.append(model.get("vndrname")==null?"":model.get("vndrname").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                            //G
				.append("FALSE").append(",")                                                                                    //H
				.append(isFore).append(",")                                                                                     //I
				.append("").append(",")                                                                                        //J
				.append(model.get("vndrabbr")==null?"":model.get("vndrabbr").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                            //K
				.append("").append(",")                                                                                        //L
				.append("0").append(",")                                                                                        //M
				.append(model.get("contname")==null?"":model.get("contname").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                            //N
				.append("").append(",")                                                                                        //O
				.append(model.get("telxnubr")==null?"":model.get("telxnubr").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                            //P
				.append("").append(",")                                                                                        //Q
				.append("").append(",")                                                                                        //R 
				.append(model.get("mobile")==null?"":model.get("mobile").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   //S Mobile
				.append("A01").append(",")                                                                                     //T PersonID
				.append("").append(",")                                                                                        //U
				.append("").append(",")                                                                                        //V
				.append("").append(",")                                                                                        //W
				.append("").append(",")                                                                                        //X
				.append("").append(",")                                                                                        //Y
				.append("FALSE").append(",")                                                                                    //Z
				.append("TRUE").append(",")                                                                                     //AA
				.append(model.get("vndrname")==null?"":model.get("vndrname").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                            //AB
				.append("").append(",")                                                                                        //AC
				.append("").append(",")                                                                                        //AD
				.append("FALSE").append(",")                                                                                    //AE
				.append("TRUE").append(",")                                                                                     //AF
				.append("A01").append(",")                                                                                    //AG-PlanPerson
				.append("").append(",")                                                                                        //AH
				.append("").append(",")                                                                                        //AI
				.append("").append(",")                                                                                        //AJ
				.append("").append(",")                                                                                        //AK
				.append("").append(",")                                                                                        //AL
				.append("").append(",")                                                                                        //AM
				.append("").append(",")//AcccComm AN                                                                           //AN           
				.append("").append(",")                                                                                        //AO
				.append("").append(",")                                                                                        //AP
				.append("").append(",")                                                                                        //AQ
				.append("").append(",")                                                                                        //AR
				.append("").append(",")                                                                                        //AS
				.append("").append(",")                                                                                        //AT
				.append("").append(",")                                                                                        //AU
				.append("").append(",")                                                                                        //AV
				.append("").append(",")                                                                                        //AW
				.append("").append(",")                                                                                        //AX
				.append("").append(",")                                                                                        //AY
				.append("").append(",")                                                                                        //AZ
				.append("").append(",")                                                                                        //BA 
				.append("").append(",")                                                                                        //BB
				.append("").append(",")                                                                                        //BC
				.append("").append(",")                                                                                        //BD
				.append("").append(",")                                                                                        //BE
				.append("").append(",")                                                                                        //BF
				.append("21").append(",")                                                                                        //BG
				.append("0").append(",")                                                                                        //BH
				.append("2141"+vndrcode).append(",")                                                                                        //BI
				.append("213100000").append(",")                                                                                        //BJ
				.append("126100000").append(",")                                                                                        //BK
				.append("").append(",")                                                                                        //BL
				.append("").append(",")                                                                                        //BM
				.append("A").append(",")                                                                                        //BN
				.append("0").append(",")                                                                                        //BO
				.append("0").append(",")                                                                                        //BP
				.append("0").append(",")                                                                                        //BQ
				.append("0").append(",")                                                                                        //BR
				.append("31").append(",")                                                                                        //BS
				.append("0").append(",")                                                                                        //BT
				.append("0").append(",")                                                                                        //BU
				.append("1").append(",")                                                                                        //BV InvoStyle
				.append("0").append(",")                                                                                        //BW
				.append("")//.append(",")                                                                                        //BX
				.append("\n")                                                                                                             
			;
			
			line.append("1").append(",")                                                                                        //A                                                                                     
			  	.append(vndrcode.trim()).append(",")                                                                                   //B
				.append("1").append(",")                                                                                        //C
				.append("").append(",")                                                                                         //D
			  	.append(model.get("vndraddr")==null?"":model.get("vndraddr").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                            //E 
			  	.append(model.get("contname")==null?"":model.get("contname").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                            //F     
			  	.append("").append(",")                                                                                        //G
			  	.append(model.get("telxnubr")==null?"":model.get("telxnubr").toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                            //H
			  	.append("").append(",")                                                                                        //I
			  	.append("").append(",")                                                                                        //J
			  	.append("")//.append(",")                                                                                        //K
			  	.append("\n")
			;
			fw.write(line.toString());
		}
		//fw.close();
		db.close();
	}
	
	public void ProcessReceviveData(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS RECEIVE DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		double cvrate=0;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =  new ReceivexmfDao(db).find(tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		CostxdaibfDao costDao = new CostxdaibfDao(db);
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String filename = "../JetDaemon/RECEIVE_"+today+".txt";
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/04"+ntransDate+"_MRG.OUT";
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiStock.Buy@"+distList.size()+"\n");
    	fw.write("-1,SysID,FundBillNo,BillDate,CustFlag,CustID,DueTo,AccMonth,CurrID,ExchRate,Total,Tax,LocalTotal,LocalTax,LocalOffSet,PrepayDay,EncashDay,DepartID,ProjectID,Status,InvoStyle,SalesID,CashPayStyleID,CashPay,LocalCashPay,VisaPayStyleID,VisaPay,LocalVisaPay,OtherPayStyleID,OtherPay,LocOtherPay,AddrID,ZipCode,CustAddress,ContactPerson,LinkManProf,ContactPhone,PriceofTax,MakerID,Maker,VoucherNO,PermitterID,Permitter,UDef1,UDef2,Remark,HasCheck,CostFlag,NoCheckOffSet,NoCheckDisCount,NoChkLocalOffSet,InvoFlag,InvoBillNO,InvoiceNo,TaxType2,SumDist,SignBack,SumAmtATax,SumNum,TaxType,GatherStyle,GatherDelay,CheckStyle,CheckDelay,Detail,GatherOther,CheckOther,SumTransferFee,SumOtherFee,SumFee,BillStatus,ShareType,Payed,PayedLocal,NotPayed,NotPayedLocal,RealPay,RealPayLocal,InCost,ExchProfit,SumCost,DeliverBillNo,MergeOutStat\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BillDate,ProdID,ProdName,Quantity,QuanComb,MLPrice,MLAmount,WareID,CostAvg,QtyRemain,TranType,FromNO,FromRow,TaxRate,TaxAmt,MLDist,NeedUpdate,Price,Discount,Amount,ItemRemark,HaveBatch,HasCheck,SUnitID,SPrice,SQuantity,UnitRelation,EQuantity,EUnitID,EUnitRelation,StdCost,CostForAcc,MLTaxAmt,HasPic,IsOdd,BoxID,BoxQty,HasBox,HasCharacter,MatchNO,HasSerial,ConvertRate,IsGift,Mark,Detail,TransferFee,OtherFee,TotalFee,StkMlAmount,LocalPayment,IsTailPurchase,MergeOutStat\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BatchID,sBatchID,StorageID,SQuantity,Quantity,EQuantity,ProduceDate,ValidDate,ParentRowNO,QtyRemain,FromNO,FromRow,TranType,MLPrice,MLAmount,HasCheck,ItemRemark,HasSerial,ConfigNO,CharComb,CharQty,CharQtyRemain,ProdID,WareID,BillDate\n");
    	fw.write("-1,LCNO,SerNo,FeeDueTo,SourceNO,TypeID,Description,Amount,CurrID,Rate,LocalMny,DueTo,RowNo\n");
    	fw.write("-1,ProdID,CCID,IsMust,SerNO,ValueSource\n");
    	fw.write("-1,LCNO,SerNo,FeeDueTo,TypeID,Description,Amount,CurrID,Rate,LocalMny,SourceNO,DueTo,RowNo\n");
    	fw.write("-1,BillNo,PicNO,ParentRowNo,ParentSubRowNo,PicID,CurrProdID\n");
    	fw.write("-1,BillNO,ParentRowNO,DetailRowNo,RowNO,IsDetail,SerNO,SerialID,FSerialID,TranType,FromNO,FromRow,QtyRemain,Quantity,ProdID,HasCheck,WareID,BatchID,sBatchID,StorageID,ConfigNo\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			String compcode = model.get("compcode")==null?"":model.get("compcode").toString().trim();
			String recvdate = model.get("recvdate")==null?"":model.get("recvdate").toString().trim();
			String payxdate = model.get("payxdate")==null?"":model.get("payxdate").toString().trim();//放付款日
			recvdate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(recvdate, "yyyy-MM-dd"), "yyyyMMdd");
			payxdate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(payxdate, "yyyy-MM-dd"), "yyyyMMdd");
			//加上若轉入的公司別是I-part,Jetone HK,Mouse 就把本幣跟原幣一樣設為USD
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))
		    {
		    	cvrate=1;
		    }
		    else
		    {
		    	cvrate=Double.parseDouble(model.get("cvtxrate")==null?"1":model.get("cvtxrate").toString());
		    }

			String qtyAmt = new ReceivexdfDao(db).findSumByNumber(model.get("recvnubr")==null?"":model.get("recvnubr").toString());
			double origAmt = Double.parseDouble(model.get("invoxamt")==null?"0":model.get("invoxamt").toString());//原幣金額
			double tfeeSum = Double.parseDouble(model.get("tradefee")==null?"0":model.get("tradefee").toString());//原幣貿推費(台幣貿推費)
			double taxAmt=0;
			//一般進口的稅率
			double taxAmtS   = Double.parseDouble(model.get("taxxxamt")==null?"0":model.get("taxxxamt").toString());//原幣稅率			
			//進口時的稅率 imporfee
			double imporfee   = Double.parseDouble(model.get("imporfee")==null?"0":model.get("imporfee").toString());//原幣稅率
			String invostat = model.get("invostat")==null?"":model.get("invostat").toString().trim();//發票類別21-國內,28-進口
			if(invostat.equals("28"))
			{
				taxAmt=new BigDecimal(imporfee).divide(new BigDecimal(cvrate), 2, RoundingMode.HALF_UP).doubleValue();
				//taxAmt=imporfee/cvrate;
			}
			else
			{
				taxAmt=new BigDecimal(taxAmtS).setScale(2, RoundingMode.HALF_UP).doubleValue();
			}
			//tfeeSum/cvrate;
			double origfee =new BigDecimal(tfeeSum/cvrate).setScale(4, RoundingMode.HALF_UP).doubleValue();//原幣貿推費(轉為美金)  取到第四位updated by ashley 2012/11/30
			
			double localTtl = cvrate * origAmt;//本幣金額
			double localTax = cvrate * taxAmt;//本幣稅率
			double localTfee = cvrate * origfee;//本幣貿推費

			//double localNewTtl=Math.round(localTtl+localTfee); //台幣含貿推的總額
			//加上若進貨單價為0時要直接設定unitbase為0
			BigDecimal Bunitbase= new BigDecimal(0);
			BigDecimal localNewTtl= new BigDecimal(localTtl).add(new BigDecimal(localTfee)).setScale(4, RoundingMode.HALF_UP);
			double unitbase=0;
			if(origAmt>0)
			{
				Bunitbase=localNewTtl.divide(new BigDecimal(origAmt), 4, RoundingMode.HALF_UP);//base要取第四位 updated by Ashley 2012/11/30
				//Bunitbase=localNewTtl.divide(new BigDecimal(origAmt)).setScale(2, RoundingMode.HALF_UP);
				unitbase=Bunitbase.doubleValue();
			}

				//unitbase=localNewTtl/origAmt;//原幣的基數
			double origNewTtl=origAmt + origfee;//含推貿費的總額
			double origTtl = taxAmt + origAmt + origfee;//總額=金額+貿推+稅額
			//double tfeeUnit=tfeeSum/Integer.parseInt(qtyAmt);
			String invoicetype =model.get("invoicetype")==null?"":model.get("invoicetype").toString();
			String invoBill="";
			String tmpInvonubr="";
			if(invoicetype.equals("P")||invoicetype.equals("X"))//當為一般發票時報關時
			{
				tmpInvonubr=model.get("invoice")==null?"":model.get("invoice").toString();
				invoBill=tmpInvonubr+"-"+model.get("recvnubr").toString().substring(2,model.get("recvnubr").toString().length());
			}
			//加上備註
			String remark= model.get("remark")==null?"":model.get("remark").toString();
			//課稅類別若為應稅時設為隨單開立其餘都設為空白
			String taxtype=model.get("taxtype")==null?"":model.get("taxtype").toString();
			String invostyle="0";
			String invoflag="0";//是否有開立發票
			
			if(taxtype.equals("0"))
			{
				invostyle="1";
				 invoflag="1";
			}
			//當進貨幣別為台幣時本幣上傳的金額也要四捨五入
			if(model.get("trancurr").equals("N"))
			{
				origNewTtl=Math.round(origNewTtl);
				taxAmt=Math.round(taxAmt);
			}
			//加上若轉入的公司別是I-part,Jetone HK,Mouse 就把本幣跟原幣一樣設為USD
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))
		    {
		    	localNewTtl=new BigDecimal(origNewTtl).setScale(2, RoundingMode.HALF_UP);
		    	localTax=new BigDecimal(taxAmt).setScale(2, RoundingMode.HALF_UP).doubleValue();
		    }
		    else
		    {
				localNewTtl=localNewTtl.setScale(0, RoundingMode.HALF_UP);
				localTax=	new BigDecimal(localTax).setScale(0, RoundingMode.HALF_UP).doubleValue();	
		    }
			line.append("0").append(",")                                                                   //A
			    .append("").append(",")                                                                    //B
			    .append(model.get("recvnubr")==null?"":model.get("recvnubr").toString()).append(",")       //C
			    .append(recvdate).append(",")                                                              //D
			    .append("2").append(",")        //custFlag                                                 //E
			    .append(model.get("vndrcode")==null?"":model.get("vndrcode").toString()).append(",")       //F
			    .append(model.get("vndrcode")==null?"":model.get("vndrcode").toString()).append(",")       //G
			    //.append("").append(",")                                                                  //G
			    .append(recvdate.substring(0,6)).append(",")                                               //H   AccMonth
			    //匯率U-USD,N-NTD
			    .append(model.get("trancurr")==null?"":CommonUtil.getCurrency(tcompcode,model.get("trancurr").toString())).append(",")       //I
			    .append(cvrate).append(",") //J
			    .append(origNewTtl).append(",")                                                            //K OrigTotal
			    .append(taxAmt).append(",")                                                                //L Origtax
			    .append(localNewTtl).append(",")                                                           //M LocalTotal(有含推貿費的總額)
			    .append(localTax).append(",")                                                              //N LocalTAX
			    .append("0").append(",")                                                                   //O
			    .append(payxdate).append(",")                                                              //P PrepayDay
			    .append(payxdate).append(",")                                                              //Q EncashDay
			    .append("A").append(",")                                                                    //R DepartID
			    //.append("").append(",")                                                                  //R 
			    .append("").append(",")                                                                    //S  
			    .append("1").append(",")                                                                   //T
			    .append(invostyle).append(",")                                                 //U InvoStyle **0,1,2,3 --已改好 只有0-空白 跟1-隨單開立
			    .append(model.get("buycode")).append(",")                                                  //V SalesID **暫用W01--已改好
			    .append("").append(",")                                                                    //W
			    .append("0").append(",")                                                                   //X
			    .append("0").append(",")                                                                   //Y
			    .append("").append(",")                                                                    //Z
			    .append("0").append(",")                                                                   //AA
			    .append("0").append(",")                                                                   //AB
			    .append("").append(",")                                                                    //AC
			    .append("0").append(",")                                                                   //AD
			    .append("0").append(",")                                                                   //AE
			    .append("").append(",")                                                                    //AF
			    .append("").append(",")  //zipcode AG                                                      //AG
			    .append("").append(",")                                                                    //AH
			    .append("").append(",")                                                                    //AI
			    .append("").append(",")                                                                    //AJ
			    .append("").append(",")                                                                    //AK
			    .append("True").append(",")                                                                //AL Price of Tax
			    .append("True").append(",")                                                                //AM 
			    //.append(model.get("regiuser")).append(",")                                               //AN MakerID
			    //.append(model.get("reginame")).append(",")                                               //AO Maker
			    .append("").append(",")                                                                    //AN
			    .append("").append(",")                                                                    //AO
			    .append("").append(",")                                                                    //AP
			    .append("").append(",")                                                                    //AQ
			    .append("").append(",")                                                                    //AR
			    .append("").append(",")                                                                    //AS  
			    .append(CommonUtil.byteToHexString(remark)).append(",")                                    //AT  Remark
			    .append("FALSE").append(",") //AU                                                          //AU  HasCheck
			    .append("").append(",")                                                                    //AV
			    .append("0").append(",")                                                                   //AW
			    .append("0").append(",")                                                                   //AX
			    .append("0").append(",")                                                                   //AY
			    .append(invoflag).append(",")                                                             //AZ InvoFlag
			    .append(invoBill).append(",") //BA InvoBillNO
			    .append(model.get("invonubr")==null?"":model.get("invonubr").toString()).append(",")       //BB  InvoiceNo
			    .append(model.get("taxtype")==null?"":model.get("taxtype").toString()).append(",")         //BC  Taxtype2
			   // .append("").append(",")                                                                  //BC  
			    .append("0").append(",")                                                                   //BD
			    .append("FALSE").append(",")                                                               //BE
			    .append(origTtl).append(",") //BF                                                          //BF SumAmtATax**總價含稅
			    .append(qtyAmt).append(",")                                                                //BG SumNum  ***總數量
			    .append(model.get("taxtype")==null?"":model.get("taxtype").toString()).append(",")         //BH  Taxtype1
			    .append("0").append(",")                                                                   //BI
			    .append("0").append(",")                                                                   //BJ
			    .append("0").append(",")                                                                   //BK
			    .append("0").append(",")                                                                   //BL 
			    .append("A").append(",")                                                                  //BM  Detail
			    .append("").append(",")                                                                    //BN
			    .append("").append(",")                                                                    //BO
			    .append("0").append(",")                                                                   //BP
			    .append("0").append(",")                                                                   //BQ
			    .append("0").append(",")                                                                   //BR
			    .append("").append(",")                                                                    //BS
			    .append("").append(",")                                                                    //BT
			    .append("0").append(",")                                                                   //BU
			    .append("0").append(",") //BV                                                              //BV 
			    .append("0").append(",")                                                                   //BW
			    .append("0").append(",")                                                                   //BX
			    .append("0").append(",")                                                                   //BY
			    .append("0").append(",")                                                                   //BZ
			    .append("0").append(",") //CA                                                              //CA
			    .append("0").append(",")                                                                   //CB 
			    .append(localNewTtl).append(",") //CC                                                      //CC
			    .append("").append(",")                                                                    //CD
			    .append("0")                                                                               //CE
			    .append("\n")                       
			;
			fw.write(line.toString());
			List lineList =  new ReceivexdfDao(db).findByNumber(model.get("recvnubr")==null?"":model.get("recvnubr").toString());
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{
				//if(model.get("recvnubr").toString().equals("RC201409220007"))
				//{
				//	System.out.print("");
				//}
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				String avgxcost = costDao.findByProd(compcode,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString() );
				String unitpric = modelLine.get("unitpric")==null?"0":modelLine.get("unitpric").toString();	
				double newprice= (unitbase*Double.parseDouble(unitpric))/cvrate;
				BigDecimal   b   =   new   BigDecimal(newprice);
				newprice=b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue();//取第三位updated by Ashley 2012/11/30
				String quantity= modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString();
				//double lineAmt = Double.parseDouble(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString())*Double.parseDouble(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString());
				//要把unitpric換成新的單價做運算
				double lineAmt = Double.parseDouble(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString())*newprice;
				BigDecimal   c   =   new   BigDecimal(lineAmt);
				lineAmt=c.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
				
				double taxRate = 0;
				if(taxAmt>0)
				{
					taxRate = 0.05;
				}
				double lineTaxAmt = lineAmt * taxRate;
				
				//抓轉單的原始單據是否為借入單Trantype
				String trantype="0";
				String poxxnubr=modelLine.get("fromno")==null?"":modelLine.get("fromno").toString();
				if(!poxxnubr.equals(""))
					trantype="203";
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                 //A
		        .append(modelLine.get("recvnubr")==null?"":modelLine.get("recvnubr").toString()).append(",")     //B 
		        .append(rowno).append(",")                                                                       //C
		        .append(rowno).append(",")                                                                       //D
		        .append(recvdate).append(",")                                                                    //E
		        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")     //F	        
		        .append(modelLine.get("prodname")==null?"":modelLine.get("prodname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")//G 
		        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")     //H
		        .append("0").append(",")                                                                         //I 
		        .append(newprice).append(",")     //J 單價要含貿推費均攤
		        .append(lineAmt).append(",")                                                                     //K
		        .append(modelLine.get("locxcode")==null?"":modelLine.get("locxcode").toString()).append(",")     //L 
		        //.append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")     //M CostAvg**注意應該要帶入product的均價
		        .append(avgxcost).append(",")//avgCost                                                           //M  從成本簿中抓出均價 
		        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")     //N
		        .append(trantype).append(",")                                                                         //O Trantype
		        .append(modelLine.get("fromno")==null?"":modelLine.get("fromno").toString()).append(",")         //P FromNo
		        .append(modelLine.get("fromrow")==null?"0":modelLine.get("fromrow").toString()).append(",")      //Q FromRow
		        .append(taxRate).append(",")                                                                     //R
		        .append(lineTaxAmt).append(",")                                                                  //S
		        .append("0").append(",")                                                                         //T
		        .append("TRUE").append(",")                                                                      //U
		        .append(newprice).append(",")     //V 改成有攤貿推費的新價
		        //.append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")     //V Price
		        //.append("0").append(",")                                                                         //V
		        .append("0").append(",")                                                                         //W
		        .append(CommonUtil.getAmount(newprice, quantity)).append(",")            //X Amount
		        //.append("0").append(",")                                                                         //X
		        .append("").append(",")                                                                          //Y 
		        .append("FALSE").append(",")                                                                      //Z 
		        .append("TRUE").append(",")                                                                      //AA
		        .append("").append(",")                                                                          //AB
		        .append(newprice).append(",")     //AC  SPrice
		        //.append("0").append(",")                                                                         //AC
		        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")     //AD 
		        .append("1").append(",")                                                                          //AE
		        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")     //AF
		        .append("").append(",")                                                                          //AG 
		        .append("0").append(",")//AH                                                                     //AH
		        .append("0").append(",")                                                                         //AI
		        .append(CommonUtil.getAmount(newprice, quantity)).append(",")                                    //AJ  CostForAcc
		        //.append("0").append(",")                                                                       //AJ
		        .append("0").append(",")                                                                         //AK
		        .append("FALSE").append(",")                                                                     //AL
		        .append("FALSE").append(",")                                                                     //AM
		        .append("").append(",")                                                                          //AN
		        .append("0").append(",")                                                                         //AO
		        .append("FALSE").append(",")                                                                     //AP
		        .append("FALSE").append(",")                                                                     //AQ
		        .append("").append(",")                                                                          //AR
		        .append("FALSE").append(",")                                                                     //AS
		        .append("0").append(",")                                                                         //AT
		        .append("FALSE").append(",")                                                                     //AU
		        .append("").append(",")                                                                          //AV
		        .append("").append(",")                                                                          //AW
		        .append("0").append(",")                                                                         //AX
		        .append("0").append(",")                                                                         //AY
		        .append("0").append(",")                                                                         //AZ
		        .append("0").append(",")                                                                         //BA
		        .append("0").append(",")                                                                         //BB
		        .append("FALSE").append(",")                                                                     //BC
		        .append("0")//.append(",")                                                                       //BD
		        .append("\n");
						fw.write(lineLine.toString());
				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}

	//轉出進貨發票資訊
	public void ProcessInvoiceDataIn(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS INVOICE DATA IN======");
		Vector data = new Vector();
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =new InvoiceDAO(db).findInInvoice(tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/08"+ntransDate+"_MRG.OUT";  
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiComm.InvoiceIn@"+distList.size()+"\n");
    	fw.write("-1,InvoBillNo,InvoiceNO,InvoiceDate,SrcSysNO,SrcBillNO,SrcBillFlag,CustomerID,ApplyMonth,InvoiceType,OffsetType,TaxType,OtherVoucher,Amount,TaxAmt,CompanyName,TaxRegNO,ZipCode,UseOrder,IncludeTax,Remark,InvoMonth,Total,InvoRealMonth,Printed,PrintMan,PrintDate,Printer,SpecialTaxType,Address,InvoAddr,InvoPool,InvoPoolNum,IsCancel,IsShowTax,MakerID,Maker\n");
    	fw.write("-1,BillNO,SerNO,Flag,ProdID,Quantity,MLPrice,MLAmount,ProdName,SUnitID\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			//現在進貨發票都是直接存放台幣於系統
			double origAmt = Double.parseDouble(model.get("invoxamt")==null?"0":model.get("invoxamt").toString());
			double origTax = Double.parseDouble(model.get("taxxxamt")==null?"0":model.get("taxxxamt").toString());
			//發票計算匯率先拿掉
			//double localAmt = origAmt * Double.parseDouble(model.get("exchrate")==null?"1":model.get("exchrate").toString());
			//double localTax = origTax * Double.parseDouble(model.get("exchrate")==null?"1":model.get("exchrate").toString());
			double localAmt = origAmt*1;
			double localTax = origTax *1;
			StringBuffer line = new StringBuffer();
			line.append("0").append(",") //A  -1
				 .append(model.get("invonubr")==null?"":model.get("invonubr").toString()).append("-")
				 .append(model.get("issunubr")==null?"":model.get("issunubr").toString()
				 .substring(2,model.get("issunubr").toString().length())).append(","); //B  InvoBillNo
			//invotype=P時印發票號碼,不然清空
			if(model.get("invotype").toString().equals("P"))
					 line.append(model.get("invonubr")==null?"":model.get("invonubr").toString()).append(","); //C  InvoiceNO
				 else
					 line.append("").append(","); //C  InvoiceNO
			line.append(DateTimeUtil.getDatePart(model.get("invodate").toString(), "yyyy", "mm", "dd")).append(",") //D  InvoiceDate
				 .append("2").append(",") //E  SrcSysNO  -->來源:1.財務,2.庫存
				 .append(model.get("issunubr")==null?"":model.get("issunubr").toString()).append(",") //F  SrcBillNO
				 .append("100").append(",") //G  SrcBillFlag  ->100代表進貨憑單 500代表銷貨憑單
				 .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",") //H  CustomerID
				 .append(DateTimeUtil.getDatePart(model.get("invodate").toString(), "yyyy", "mm", "")).append(",") //I  ApplyMonth
				 .append(model.get("invostat")==null?"":model.get("invostat").toString()).append(",") //J  InvoiceType
				 .append("0").append(",") //K  OffsetType
				 .append("0").append(","); //L  TaxType
			//invotype=P時印發票號碼,不然清空
			if(model.get("invotype").toString().equals("X"))
				 line.append(model.get("invonubr")==null?"":model.get("invonubr").toString()).append(","); //C  InvoiceNO
			else
				 line.append("").append(","); //M  OtherVoucher  當為進口報關時,於此填入報關號碼
			line.append(localAmt).append(",") //N  Amount
				 .append(localTax).append(",") //O  TaxAmt
				 .append(model.get("vndrname")==null?"":model.get("vndrname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",") //P  CompanyName
				 .append(model.get("unifnubr")==null?"":model.get("unifnubr").toString()).append(",") //Q  TaxRegNO   統編?
				 .append("").append(",") //R  ZipCode
				 .append("1").append(",") //S  UseOrder
				 .append("False").append(",") //T  IncludeTax
				 .append("").append(",") //U  Remark
				 .append(DateTimeUtil.getDatePart(model.get("invodate").toString(), "yyyy", "mm", "")).append(",") //V  InvoMonth
				 .append(CommonUtil.getTaxTotalAmount(model.get("taxxxamt").toString(), model.get("invoxamt").toString())).append(",") //W  Total
				 .append(DateTimeUtil.getDatePart(model.get("invodate").toString(), "yyyy", "mm", "")).append(",") //X  InvoRealMonth
				 .append("False").append(",") //Y  Printed
				 .append("").append(",") //Z  PrintMan
				 .append("0").append(",") //AA PrintDate
				 .append("").append(",") //AB Printer
				 .append("0").append(",") //AC SpecialTaxType
				 .append(model.get("vndraddr")==null?"":model.get("vndraddr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",") //AD Address 
				 .append("1").append(",") //AE InvoAddr
				 .append("False").append(",") //AF InvoPool
				 .append("1").append(",") //AG InvoPoolNum
				 .append("False").append(",") //AH IsCancel
				 .append("True").append(",") //AI IsShowTax
				 .append(model.get("regiuser")==null?"":model.get("regiuser").toString()).append(",") //AJ MakerID
				 .append(model.get("username")==null?"":model.get("username").toString()) //AK Maker
		  	     .append("\n");
			fw.write(line.toString());
		}
		//fw.close();
		db.close();
	}

	//轉出銷貨發票資訊
	public void ProcessInvoiceDataOut(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS INVOICE DATA OUT======");
		Vector data = new Vector();
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =new InvoiceDAO(db).findOutInvoice(tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/09"+ntransDate+"_MRG.OUT";  
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiComm.InvoiceOut@"+distList.size()+"\n");
    	fw.write("-1,InvoBillNo,InvoiceNO,InvoiceDate,SrcSysNO,SrcBillNO,SrcBillFlag,CustomerID,ApplyMonth,InvoiceType,OffsetType,TaxType,OtherVoucher,Amount,TaxAmt,CompanyName,TaxRegNO,ZipCode,UseOrder,MergeOutStat,IncludeTax,Remark,InvoMonth,Total,InvoRealMonth,Printed,PrintMan,PrintDate,Printer,SpecialTaxType,Address,InvoAddr,InvoPool,InvoPoolNum,IsCancel,IsShowTax,MakerID,Maker\n");
    	fw.write("-1,BillNO,SerNO,Flag,ProdID,Quantity,MLPrice,MLAmount,ProdName,SUnitID\n");
		
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			double origAmt = Double.parseDouble(model.get("invoxamt")==null?"0":model.get("invoxamt").toString());
			double origTax = Double.parseDouble(model.get("taxxxamt")==null?"0":model.get("taxxxamt").toString());
			//發票類型31,32,33  要把33(手開三聯)類型也轉成31
			String invotype = model.get("invotype")==null?"":model.get("invotype").toString();
			String invostat = model.get("invostat")==null?"":model.get("invostat").toString();
			if(invostat.equals("33"))
				invostat="31";
			String taxxflag = model.get("taxxflag")==null?"0":model.get("taxxflag").toString();
			String includetax="True";
			if(taxxflag.equals("0")||taxxflag.equals("4"))
			{
				if(invostat.equals("32"))
				{
					includetax="True";
				}
				else
				{
					includetax="False";
					taxxflag="0";
				}
			}

			//發票計算匯率先拿掉
			//double localAmt = origAmt * Double.parseDouble(model.get("exchrate")==null?"1":model.get("exchrate").toString());
			//double localTax = origTax * Double.parseDouble(model.get("exchrate")==null?"1":model.get("exchrate").toString());
			double localAmt = Math.round(origAmt);
			double localTax = Math.round(origTax);
			if(includetax.equals("True"))
			{
				localAmt=Math.round(localAmt)+Math.round(localTax);
				localTax=0;
			}
			StringBuffer line = new StringBuffer();
			line.append("0").append(",") //A  -1
				 .append(model.get("invonubr")==null?"":model.get("invonubr").toString()).append("-").append(model.get("issunubr")==null?"":model.get("issunubr").toString()).append(",") //B  InvoBillNo
				 .append(model.get("invonubr")==null?"":model.get("invonubr").toString()).append(",") //C  InvoiceNO
				 .append(DateTimeUtil.getDatePart(model.get("invodate").toString(), "yyyy", "mm", "dd")).append(",") //D  InvoiceDate
				 .append("2").append(",") //E  SrcSysNO  -->來源:1.財務,2.庫存
				 .append(model.get("issunubr")==null?"":model.get("issunubr").toString()).append(",") //F  SrcBillNO
				 .append("500").append(",") //G  SrcBillFlag -->100代表進貨憑單 500代表銷貨憑單
				 .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",") //H  CustomerID
				 .append(DateTimeUtil.getDatePart(model.get("invodate").toString(), "yyyy", "mm", "")).append(",") //I  ApplyMonth
				 .append(invostat).append(",") //J  InvoiceType
				 .append("0").append(",") //K  OffsetType
				 .append(taxxflag).append(",") //L  TaxType 發票的課稅類別
				 .append("").append(",") //M  OtherVoucher
				 .append(localAmt).append(",") //N  Amount
				 .append(localTax).append(",") //O  TaxAmt
				 .append(model.get("custname")==null?"":model.get("custname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",") //P  CompanyName
				 .append(model.get("serialno")==null?"":model.get("serialno").toString()).append(",") //Q  TaxRegNO   統編?
				 .append("").append(",") //R  ZipCode
				 .append(model.get("seqt")==null?"":model.get("seqt").toString()).append(",") //S  UseOrder  發票組別使用序號
				 .append("0").append(",") //  MergeOutStat  設為2不可刪發票,設為0可刪發票
				 .append(includetax).append(",") //T  IncludeTax
				 .append("").append(",") //U  Remark
				 .append(DateTimeUtil.getDatePart(model.get("invodate").toString(), "yyyy", "mm", "")).append(",") //V  InvoMonth
				 .append(CommonUtil.getTaxTotalAmount(model.get("taxxxamt").toString(), model.get("invoxamt").toString())).append(",") //W  Total
				 .append(DateTimeUtil.getDatePart(model.get("invodate").toString(), "yyyy", "mm", "")).append(",") //X  InvoRealMonth
				 .append("False").append(",") //Y  Printed
				 .append("").append(",") //Z  PrintMan
				 .append("0").append(",") //AA PrintDate
				 .append("").append(",") //AB Printer
				 .append("0").append(",") //AC SpecialTaxType
				 .append(model.get("compaddr")==null?"":model.get("compaddr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",") //AD Address 
				 .append("1").append(",") //AE InvoAddr
				 .append("False").append(",") //AF InvoPool
				 .append("1").append(","); //AG InvoPoolNum
				 if(invotype.equals("D"))
					 line.append("True").append(","); //AH IsCanceled
				 else
					 line.append("False").append(","); //AH NotCancel
			 line.append("True").append(",") //AI IsShowTax
				 .append(model.get("regiuser")==null?"":model.get("regiuser").toString()).append(",") //AJ MakerID
				 .append(model.get("username")==null?"":model.get("username").toString()) //AK Maker
		  	     .append("\n");
			fw.write(line.toString());
		}
		//fw.close();
		db.close();
	}
	
	public void ProcessSalesData(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS SALES DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList =  new ShipmatlmfDao(db).find1015Type(tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		CostxdaibfDao costDao = new CostxdaibfDao(db);
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String filename = "../JetDaemon/SALES_"+today+".txt";
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/06"+ntransDate+"_MRG.OUT";
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiStock.Sale@"+distList.size()+"\n");
    	fw.write("-1,SysID,FundBillNo,BillDate,CustFlag,CustID,DueTo,AccMonth,CurrID,ExchRate,Total,Tax,LocalTotal,LocalTax,LocalOffSet,OffSet,PrepayDay,EncashDay,DepartID,ProjectID,Status,InvoStyle,SalesID,CashPayStyleID,CashPay,LocalCashPay,VisaPayStyleID,VisaPay,LocalVisaPay,OtherPayStyleID,OtherPay,LocOtherPay,AddrID,ZipCode,CustAddress,ContactPerson,LinkManProf,ContactPhone,PriceofTax,Maker,Permitter,VoucherNO,CVoucherNO,UDef1,UDef2,Remark,MakerID,PermitterID,CostFlag,HasCheck,InvoFlag,InvoBillNO,InvoiceNo,TaxType2,SumDist,InvoIsCancel,ExportState,ExportDate,ExportID,SumAmtATax,SumNum,TaxType,GatherStyle,GatherDelay,CheckStyle,CheckDelay,DataVer,SignBack,Detail,IsSaleCheck,TranSrvSale,GatherOther,CheckOther,MergeOutStat\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BillDate,ProdID,ProdName,Quantity,QuanComb,MLPrice,MLAmount,WareID,CostAvg,QtyRemain,TranType,FromNO,FromRow,TaxRate,TaxAmt,MLDist,Price,Discount,Amount,ItemRemark,NeedUpdate,HaveBatch,HasCheck,SUnitID,SPrice,SQuantity,UnitRelation,CostForAcc,EQuantity,EUnitID,EUnitRelation,StdCost,MLTaxAmt,HasPic,IsOdd,BoxID,BoxQty,HasBox,HasCharacter,MatchNO,CurVersion,HasSerial,ConvertRate,IsGift,CustBillNo,Mark,Detail,TranSrvSale,PriceType\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BatchID,sBatchID,StorageID,SQuantity,Quantity,ProduceDate,ValidDate,ParentRowNO,QtyRemain,FromNO,FromRow,TranType,MLPrice,MLAmount,HasCheck,ItemRemark,CostForAcc,EQuantity,HasSerial,ConfigNO,CharComb,CharQty,CharQtyRemain,ProdID,WareID,BillDate\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BillDate,ProdID,ProdName,SQuantity,SUnitID,Quantity,WareID,CostAvg,QuanComb,MLPrice,MLAmount,ParentRowNO,NeedUpdate,HaveBatch,HasCheck,UnitRelation,CostForAcc,EQuantity,EUnitID,EUnitRelation,ItemRemark,StdCost,Price,SPrice,DisCount,TaxRate,HasSerial\n");
    	fw.write("-1,BillNO,UnucleRowNO,ParentRowNO,RowNO,SerNO,BatchID,sBatchID,StorageID,SQuantity,Quantity,ProduceDate,ValidDate,QtyRemain,FromNO,FromRow,TranType,MLPrice,MLAmount,HasCheck,ItemRemark,EQuantity,HasSerial,ConfigNO,CharComb,CharQty,CharQtyRemain,ProdID,WareID,BillDate\n");
    	fw.write("-1,BillNO,ParentRowNO,RowNO,IsDetail,SerNO,SerialID,FSerialID,TranType,FromNO,FromRow,Quantity,QtyRemain,ProdID,HasCheck,WareID,BatchID,sBatchID,StorageID,ConfigNo,DetailRowNo\n");
    	fw.write("-1,BillNO,ParentRowNO,RowNO,IsDetail,SerNO,SerialID,FSerialID,TranType,FromNO,FromRow,QtyRemain,Quantity,ProdID,HasCheck,WareID,BatchID,sBatchID,StorageID,ConfigNo,DetailRowNo\n");
    	fw.write("-1,BillNO,ParentRowNO,DetailRowNo,RowNO,IsDetail,SerNO,SerialID,FSerialID,TranType,FromNO,FromRow,QtyRemain,Quantity,ProdID,HasCheck,WareID,BatchID,sBatchID,StorageID,ConfigNo\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			double exchrate=0;
			String compcode = model.get("compcode")==null?"":model.get("compcode").toString();
			StringBuffer line = new StringBuffer();
			String trandate = model.get("trandate")==null?"":model.get("trandate").toString();
			String payxdate = model.get("payxdate")==null?"":model.get("payxdate").toString();
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate, "yyyy-MM-dd"), "yyyyMMdd");
			payxdate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(payxdate, "yyyy-MM-dd"), "yyyyMMdd");
			
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))//加上若轉入的公司別是I-part 或Jetone HK 就把本幣跟原幣一樣設為USD
		    {
		    	exchrate=1;
		    }
		    else
		    {
		    	exchrate=Double.parseDouble(model.get("exchrate")==null?"1":model.get("exchrate").toString());
		    }
			String qtyAmt = new ShipmatldfDao(db).findSumByNumber(model.get("shipnubr")==null?"":model.get("shipnubr").toString());
			double taxAmt   = Double.parseDouble(model.get("taxxxamt")==null?"0":model.get("taxxxamt").toString()) ;
			double localTtl = exchrate * Double.parseDouble(model.get("totalamt")==null?"":model.get("totalamt").toString());
		    if(tcompcode.equals("J")||tcompcode.equals("S"))//轉I或H時,本幣不做四捨五入
		    {
				localTtl= Math.round(localTtl);
		    }
			double localTax = exchrate * taxAmt ;
			localTax= Math.round(localTax);
			double origAmt = Double.parseDouble(model.get("totalamt")==null?"0":model.get("totalamt").toString());
			double origTtl = taxAmt + origAmt;
			//加上備註
			String remark= model.get("remark")==null?"":model.get("remark").toString();
			//課稅類別若為應稅時設為隨單開立其餘都設為空白
			String saletype=model.get("saletype")==null?"":model.get("saletype").toString();
		    //若判斷該單為樣本,轉倉,也不傳發票號碼
			String invostyle="0";
			String invoflag="0";//是否有開立發票
			if(saletype.equals("A"))
			{
				invostyle="1";
				invoflag="2";
			}

		    String invoBill="";
		    if(invostyle.equals("1"))
				invoBill=model.get("invonubr")+"-"+model.get("shipnubr");
		    //update by Ashley 20130613修改判斷taxxcode為4時單價一律做含稅- 未稅(true)含稅(false)
		    String taxxcode=model.get("taxxcode")==null?"":model.get("taxxcode").toString();
		    String pot="True";		    	
		    //update by Ashley 20130117稅別若為內含,在上傳時要改為應稅
		    if(taxxcode.equals("4"))
		    {
		    	pot="False";
		    	taxxcode="0";
		    	//double norigAmt=origAmt;
				//BigDecimal   b   =   new   BigDecimal(norigAmt);
				//origAmt=b.divide(new BigDecimal(1.05), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		    	//taxAmt=b.subtract(new BigDecimal(origAmt)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		    	//double nlocalTtl= Math.round(norigAmt*exchrate);
		    	//localTtl= Math.round(origAmt*exchrate);
		    	//localTax=nlocalTtl-localTtl;
		    	if((model.get("trancurr")==null?"":model.get("trancurr").toString()).equals("N"))
		    	{
		    		origAmt=localTtl;
		    		taxAmt=localTax;
		    	}
		    }
		    else
		    {
		    	if((model.get("trancurr")==null?"":model.get("trancurr").toString()).equals("N"))
		    	{
		    		origAmt=localTtl;
		    		taxAmt=localTax;
		    	}
		    }
		    
			line.append("0").append(",")                                                                  //1-1
			    .append("2").append(",")                                                                  //2SysID
			    .append(model.get("shipnubr")==null?"":model.get("shipnubr").toString()).append(",")      //3FundBillNo  
			    .append(trandate).append(",")                                                             //4BillDate
			    .append("1").append(",")        //custFlag                                                //5CustFlag
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      //6CustID
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      //7Due to
			    .append(trandate.substring(0,6)).append(",")                                              //8AccMonth
			    .append(model.get("trancurr")==null?"":CommonUtil.getCurrency(tcompcode,model.get("trancurr").toString())).append(",") //9CurrID
			    .append(exchrate).append(",")                                                             //10ExchRate
			    .append(origAmt).append(",")                                                              //11Total
			    .append(taxAmt).append(",")                                                               //12Tax			                                                                
			    .append(localTtl).append(",")                                                             //13LocalTotal
			    .append(localTax).append(",")                                                             //14LocalTax
			    .append(model.get("localpayed")==null?"0":model.get("localpayed").toString()).append(",")     //15漏掉LocalOffSet 補上本幣已沖款金額
			    .append(model.get("payed")==null?"0":model.get("payed").toString()).append(",")     //增加一個OffSet  補上已沖款金額
			    .append(payxdate).append(",")                                                                  //16PrepayDay
			    .append(payxdate).append(",")                                                                  //17EncashDay
			    .append("A").append(",")                                                                  //18 DepartID
			    .append("").append(",")                                                                   //19 ProjectID
			    .append("1").append(",")                                                                   //20 Status
			    .append(invostyle).append(",")                                                                  //21 InvoStyle-->改成若沒開發票就是空白
			    //.append(model.get("taxxcode")==null?"":model.get("taxxcode").toString()).append(",")      //T
			    .append(model.get("salecode")==null?"":model.get("salecode").toString()).append(",")      //22SalesID
			    .append("").append(",")                                                                   //23CashPayStyleID
			    .append("0").append(",")                                                                  //24CashPay
			    .append("0").append(",")                                                                  //25LocalCashPay 
			    .append("").append(",")                                                                  //26VisaPayStyleID
			    .append("0").append(",")	                                                              //27VisaPay        
			    .append("0").append(",")                                                                  //28LocalVisaPay
			    .append("").append(",")                                                                   //29OtherPayStyleID
			    .append("0").append(",")                                                                  //30OtherPay 
			    .append("0").append(",")                                                                  //31LocOtherPay 
			    .append("1").append(",")                                                                  //32AddrID 地址代碼
			    .append("").append(",")                                                                   //33ZipCode
			    .append(model.get("shipaddr")==null?"":model.get("shipaddr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //34CustAddress
			    .append(model.get("shipname")==null?"":model.get("shipname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //35ContactPerson
			    .append("").append(",")                                                                   //36LinkManProf
			    .append(model.get("shipxtel")==null?"":model.get("shipxtel").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")      //37ContactPhone
			    .append(pot).append(",")                                                              //38PriceofTax 
			    .append(model.get("modiname")==null?"":model.get("modiname").toString()).append(",")      //39Maker       
			    .append("").append(",")                                                                   //40Permitter
			    .append("").append(",")      //41VoucherNO  //傳票
			    .append("").append(",")                                                                   //42CVoucherNO
			    .append("").append(",")                                                                   //43UDef1
			    .append("").append(",")                                                                   //44UDef2
			    .append(CommonUtil.byteToHexString(remark)).append(",")                                   //45Remark 
			    .append("").append(",")                                                                   //46MakerID
			    .append("").append(",")                                                                   //47PermitterID
			    .append("").append(",")                                                                   //48CostFlag
			    .append("True").append(",")                                                               //49 HasCheck
			   // .append(model.get("taxxcode")==null?"":model.get("taxxcode").toString()).append(",")      //50NoCheckOffSet
			   // .append("0").append(",")      //51 NoCheckDisCount
			   // .append("0").append(",")      //52 NoChkLocalOffSet
			    .append(invoflag).append(",")                                                                   //53 InvoFlag
			    .append(invoBill).append(",")                                                              //54 InvoBillNO
			    .append(model.get("invonubr")==null?"":model.get("invonubr").toString()).append(",")      //55 InvoiceNo
			    .append("1").append(",")                                                                  //56TaxType2 
			    .append("0").append(",")                                                                  //57SumDist
			    .append("False").append(",")                                                              //58InvoIsCancel
			    .append("").append(",")  																  //ExportState
			    .append("").append(",")  																  //ExportDate
			    .append("").append(",") 																  //ExportID
			    .append(origTtl).append(",")                                                              //59 SumAmtATax 含稅價格
			    .append(qtyAmt).append(",")                                                               //60 SumNum  總數量
			    .append(taxxcode).append(",")                                                             //61 TaxType 稅別
			    //.append("0").append(",")                                                                  // TaxType
			    .append("0").append(",")                                                                  //62GatherStyle
			    .append("0").append(",")                                                                  //63GatherDelay
			    .append("0").append(",")                                                                  //64CheckStyle
			    .append("0").append(",")                                                                  //65CheckDelay
			    .append("0").append(",")                                                                  //66DataVer
			    .append("False").append(",")                                                              //67SignBack
			    .append("").append(",")                                                                   //68Detail
			    .append("False").append(",")                                                               //69IsSaleCheck
			    .append("False").append(",")                                                              //70TranSrvSale
			    .append("").append(",")                                                                   //71GatherOther
			    .append("").append(",")                                                                   //72CheckOther
			    .append("0")                                                                              //73MergeOutStat
			    .append("\n")                                                                    
			;
			fw.write(line.toString());
			List lineList =  new ShipmatldfDao(db).findByNumber(model.get("shipnubr")==null?"":model.get("shipnubr").toString());
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{
				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next(); 
				
				double lineAmt = Double.parseDouble(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString())*Double.parseDouble(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString());
				double oldLineAmt =lineAmt;
				if(pot.equals("False"))
				{
					lineAmt=oldLineAmt/1.05;
				}
				BigDecimal   b   =   new   BigDecimal(lineAmt);
				lineAmt=b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
				double taxRate = 0;
				if(taxAmt>0)
				{
					taxRate = 0.05;
				}
				double lineTaxAmt = 0;
				if(pot.equals("False"))
				{
					lineTaxAmt = oldLineAmt - lineAmt;
				}else
				{
					 lineTaxAmt = lineAmt * taxRate;
				}
				//String avgxcost = costDao.findByProd(compcode,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString() ); 
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                       //A
				        .append(modelLine.get("shipnubr")==null?"":modelLine.get("shipnubr").toString()).append(",")   //B
				        .append(rowno).append(",")                                                                     //C
				        .append(rowno).append(",")                                                                     //D
				        .append(trandate).append(",")                                                                  //E
				        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")   //F
				        .append(modelLine.get("prodname")==null?"":modelLine.get("prodname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   //G
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")   //H
				        .append("0").append(",")                                                                       //I
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")   //J
				        .append(lineAmt).append(",")     //10000                                                              //K
				        .append(modelLine.get("locxcode")==null?"":modelLine.get("locxcode").toString()).append(",")   //L
				        .append(modelLine.get("avgxcost")==null?"":modelLine.get("avgxcost").toString()).append(",")//avgCost                                                         //M
				        //.append(avgxcost).append(",")//avgCost                                                         //M
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")   //N
				        .append("").append(",")                                                                        //O
				        .append("").append(",")                                                                        //P
				        .append("").append(",")                                                                        //Q
				        .append(taxRate).append(",")                                                                   //R
				        .append(lineTaxAmt).append(",")       //500                                                         //S
				        .append("0").append(",")                                                                       //T
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")//2100   //U     
				        .append("0").append(",")                                                                       //V
				        .append(lineAmt).append(",")   //10000                                                                //W
				        .append("").append(",")                                                                        //X
				        .append("TRUE").append(",")                                                                    //Y
				        .append("FALSE").append(",")                                                                   //Z
				        .append("TRUE").append(",")                                                                    //AA
				        .append("").append(",")                                                                        //AB
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",") // 2100 //AC 
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")   //AD
				        .append("1").append(",")                                                                       //AE 
				        .append("").append(",")                                                                        //AF
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")   //AG
				        .append("").append(",")                                                                        //AH
				        .append("0").append(",")                                                                       //AI
				        .append("0").append(",")                                                                       //AJ 
				        .append("0").append(",")                                                                       //AK
				        .append("FALSE").append(",")                                                                   //AL
				        .append("FALSE").append(",")                                                                   //AM
				        .append("").append(",")                                                                        //AN
				        .append("0").append(",")                                                                       //AO
				        .append("FALSE").append(",")                                                                   //AP
				        .append("FALSE").append(",")                                                                   //AQ
				        .append("").append(",")                                                                        //AR
				        .append("").append(",")                                                                        //AS 
				        .append("FALSE").append(",")                                                                   //AT
				        .append("0").append(",")                                                                       //AU
				        .append("FALSE").append(",")                                                                   //AV
				        .append(modelLine.get("custxxpo")==null?"":modelLine.get("custxxpo").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   //AW CustBillNo 客戶PO
				        .append("").append(",")                                                                        //AX
				        .append("").append(",")                                                                        //AY 
				        .append("FALSE").append(",")                                                                   //AZ
				        .append("0")//.append(",")                                                                       //BA
				        .append("\n")                   
				        ;
						fw.write(lineLine.toString());
				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}
	
	public void ProcessRecvDiscount(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS RECEIVE DISCOUNT DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList = new InteroutmfDao(db).findRecvDist("A8",tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		//CostxdaibfDao costDao = new CostxdaibfDao(db);
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/11"+ntransDate+"_MRG.OUT";
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiStock.InDist@"+distList.size()+"\n");
    	fw.write("-1,DistNO,FromNO,DistBalance,EffectCost,DispBillDate,Status,BillDate,CustFlag,CustomerID,SalesId,DueTo,AccMonth,EncashDay,TaxType,CurrID,ExchRate,Total,Tax,Discount,LocalTotal,LocalTax,DepartID,ProjectID,CashPay,SysID,VisaPay,CashPayStyle,VisaPayStyle,OtherPayStyle,OtherPay,LocOtherPay,InvoStyle,OffSet,LocalOffSet,Maker,Permitter,VoucherNO,UDef1,UDef2,MakerID,PermitterID,Remark,HasCheck,NoCheckOffSet,NoCheckDisCount,NoChkLocalOffSet,CostFlag,InvoFlag,InvoBillNO,PriceOfTax,MergeOutStat\n");
    	fw.write("-1,BillNO,RowNO,SerNO,ProdID,WareID,Quantity,MLPrice,TaxRate,TaxAmt,MLAmount,MLDist,BillDate,FromRow,FromNO,NeedUpdate,ProdName,HaveBatch,Price,Amount,HasCheck,SUnitID,SPrice,SQuantity,UnitRelation,EQuantity,EUnitID,EUnitRelation,CostForAcc,StdCost,ItemRemark,Dist,DistTaxAmt\n");
    	fw.write("-1,BillNO,BatchID,RowNO,SerNO,SQuantity,Quantity,ProduceDate,ValidDate,ParentRowNO,QtyRemain,FromNO,FromRow,TranType,BatchDist,MLDist,HasCheck,ItemRemark,NeedUpdate,EQuantity\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			String locxcode = model.get("locxcode")==null?"":model.get("locxcode").toString();
			StringBuffer line = new StringBuffer();
			String trandate1 = model.get("trandate")==null?"":model.get("trandate").toString();
			String trandate ="";
			//String duexdate = model.get("duexdate")==null?"":model.get("duexdate").toString();
			double cvtxrate=0;
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))//加上若轉入的公司別是I-part 或Jetone HK 就把本幣跟原幣一樣設為USD
		    {
		    	cvtxrate=1;
		    }
		    else
		    {
		    	cvtxrate=Double.parseDouble(model.get("cvtxrate")==null?"1":model.get("cvtxrate").toString());
		    }
			//double cvtxrate = Double.parseDouble(model.get("cvtxrate")==null?"1":model.get("cvtxrate").toString());
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMMdd");
			String accMonth = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMM");
			//duexdate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(duexdate, "yyyy-MM-dd"), "yyyyMMdd");
			//double taxAmt   = Double.parseDouble(model.get("taxxxamt")==null?"":model.get("taxxxamt").toString()) ;
			//double localTtl = Double.parseDouble(model.get("exchrate")==null?"":model.get("exchrate").toString()) * Double.parseDouble(model.get("totalamt")==null?"":model.get("totalamt").toString());
			//double localTax = taxAmt * Double.parseDouble(model.get("exchrate")==null?"":model.get("exchrate").toString());
			//double origAmt = Double.parseDouble(model.get("totalamt")==null?"":model.get("totalamt").toString());
			//double origTtl = taxAmt + origAmt;
			String taxcode=model.get("taxtype2")==null?"":model.get("taxtype2").toString();
			//計算本單總價及稅金
			double distOrig= new InteroutdfDao(db).findDistByNumber(model.get("trannubr")==null?"":model.get("trannubr").toString());
			double distTOrig=0;
			String distLoc = CommonUtil.getAmountRate(String.valueOf(distOrig), String.valueOf(cvtxrate));//計算本幣價
			String disTLoc="0";
			
			if(taxcode.equals("0"))
			{
				distTOrig=distOrig*0.05;
				disTLoc=CommonUtil.getTaxAmount(distLoc,"0.05");
			}
			//加上備註
			String remark= model.get("remarksx")==null?"":model.get("remarksx").toString();
			line.append("0").append(",")                                                                  //A -1
			    .append(model.get("trannubr")==null?"":model.get("trannubr").toString()).append(",")      //B DistNO
			    .append(model.get("agnxnubr")==null?"":model.get("agnxnubr").toString()).append(",")      //C FromNO 
			    .append(0).append(",")                                                                    //D DistBalance
			    .append("True").append(",")        //custFlag                                             //E EffectCost
			    .append(trandate).append(",")                                                             //F DispBillDate    
			    .append("1").append(",")                                                                  //G Status
			    .append(trandate).append(",")                                                             //H BillDate
			    .append("2").append(",")                                                                  //I CustFlag
			    .append(model.get("vndrcode")==null?"":model.get("vndrcode").toString()).append(",")      //J CustomerID
			    .append(model.get("buycode")==null?"":model.get("buycode").toString()).append(",")        //K SalesId
			    .append(model.get("vndrcode")==null?"":model.get("vndrcode").toString()).append(",")      //L DueTo
			    .append(accMonth).append(",")                                                             //M AccMonth
			    .append(trandate).append(",")                                                             //N EncashDay
			    .append(taxcode).append(",")                                                              //O TaxType
			    .append(model.get("trancurr")==null?"":CommonUtil.getCurrency(tcompcode,model.get("trancurr").toString())).append(",")      //P CurrID
			    .append(cvtxrate).append(",")                                                               //Q ExchRate
			    .append(distOrig).append(",")                                                                  //R Total
			    .append(distTOrig).append(",")                                                              //S Tax
			    .append("0").append(",")                                                                  //T Discount
			    .append(distLoc).append(",")                                                                  //U LocalTotal
			    .append(distTOrig).append(",")                                                             //V LocalTax
			    .append("A").append(",")                                                                   //W DepartID
			    .append("").append(",")                                                                   //X ProjectID
			    .append("0").append(",")                                                                  //Y CashPay
			    .append("2").append(",")	                                                              //Z SysID       
			    .append("0").append(",")                                                                  //AA VisaPay
			    .append("").append(",")                                                                   //AB CashPayStyle
			    .append("").append(",")                                                                   //AC VisaPayStyle
			    .append("").append(",")                                                                   //AD OtherPayStyle
			    .append("0").append(",")                                                                  //AE OtherPay
			    .append("0").append(",")                                                                  //AF LocOtherPay
			    .append("0").append(",")                                                                  //AG InvoStyle 發票類型
			    .append("0").append(",")                                                                  //AH OffSet
			    .append("0").append(",")                                                                  //AI LocalOffSet
			    .append(model.get("reginame")==null?"":model.get("reginame").toString()).append(",")      //AJ Maker
			    .append("").append(",")                                                                   //AK Permitter
			    .append("").append(",")      //AL VoucherNO      
			    .append("").append(",")                                                                   //AM UDef1
			    .append("").append(",")                                                                   //AN UDef2
			    .append(model.get("reginame")==null?"":model.get("reginame").toString()).append(",")      //AO MakerID
			    .append("").append(",")      //AP PermitterID  設定是否已覆核
			    .append(CommonUtil.byteToHexString(remark)).append(",")                                   //AQ Remark
			    .append("True").append(",")                                                               //AR HasCheck
			    .append("0").append(",")                                                                  //AS NoCheckOffSet
			    .append("0").append(",")                                                                  //AT NoCheckDisCount
			    .append("0").append(",")                                                                  //AU NoChkLocalOffSet
			    .append("700").append(",")                                                                //AV CostFlag
			    .append("3").append(",")                                                                  //AW InvoFlag
			    .append("").append(",")      //AX InvoBillNO
			    .append("False").append(",")                                                               //AY PriceOfTax
			    .append("0")//MergeOutStat
			    .append("\n")                                                                    
			;
			fw.write(line.toString());
			List lineList =  new InteroutdfDao(db).findByNumberDist(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{
				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				//double lineAmt = Double.parseDouble(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString())*Double.parseDouble(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString());
				//double taxRate = 0;
				//if(taxAmt>0)
				//{
				//	taxRate = 0.05;
				//}
				//double lineTaxAmt = lineAmt * taxRate;
				//String avgxcost = costDao.findByProd(compcode,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString() );
				//算出原單Line總價
				
				double origpric   = Double.parseDouble(modelLine.get("origcost")==null?"0":modelLine.get("origcost").toString()) ;
				double origqty   = Double.parseDouble(modelLine.get("origxqty")==null?"0":modelLine.get("origxqty").toString());
				double origTtl = origpric*origqty;
				double distLine=Double.parseDouble(modelLine.get("dist")==null?"0":modelLine.get("dist").toString());
				//算折讓價
				double disrateLine=0;
				double taxrate=0;
				double taxamt=0;
				if(taxcode.equals("0"))
				{
					taxrate=0.05;
					disrateLine=distLine*taxrate;
					taxamt=origTtl*taxrate;
				}
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                       //A -1
				        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")   //B BillNO
				        .append(rowno).append(",")                                                                     //C RowNO
				        .append(rowno).append(",")                                                                     //D SerNO
				        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")   //E ProdID
				        .append(locxcode).append(",")                                                                  //F WareID
				        .append(origqty).append(",")   //G Quantity
				        .append(origpric).append(",")                                                         //H MLPrice
				        .append(taxrate).append(",")  //I TaxRate
				        .append(taxamt).append(",")                                                                    //J TaxAmt
				        .append(origTtl).append(",")                                                                   //K MLAmount
				        .append(distLine).append(",")                                                                  //L MLDist
				        .append(trandate).append(",")                                                                  //M BillDate
				        .append("1").append(",")                                                                       //N FromRow
				        .append(model.get("agnxnubr")==null?"":model.get("agnxnubr").toString()).append(",")           //O FromNO
				        .append("True").append(",")                                                                    //P NeedUpdate
				        .append("").append(",")                                                                        //Q ProdName
				        .append("False").append(",")                                                                   //R HaveBatch
				        .append(origpric).append(",")                                                         //S Price
				        .append(origTtl).append(",")                                                                   //T Amount
				        .append("True").append(",")                                                                    //U HasCheck     
				        .append("").append(",")                                                                        //V SUnitID
				        .append(origpric).append(",")                                                         //W SPrice
				        .append(modelLine.get("origxqty")==null?"":modelLine.get("origxqty").toString()).append(",")   //X SQuantity
				        .append("1").append(",")                                                                       //Y UnitRelation
				        .append(modelLine.get("origxqty")==null?"":modelLine.get("origxqty").toString()).append(",")   //Z EQuantity
				        .append("").append(",")                                                                        //AA EUnitID
				        .append("0").append(",")                                                                       //AB EUnitRelation
				        .append("0").append(",")                                                                       //AC CostForAcc
				        .append("0").append(",")                                                                       //AD StdCost
				        .append("").append(",")                                                                        //AE ItemRemark
				        .append(distLine).append(",")                                                                  //AF Dist
				        .append(disrateLine)//.append(",")                                                             //AG DistTaxAmt
				        .append("\n")                   
				        ;
						fw.write(lineLine.toString());
				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}
	public void ProcessSaleDiscount(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS SALE DISCOUNT DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList = new InteroutmfDao(db).findSaleDist("A1",tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		CostxdaibfDao costDao = new CostxdaibfDao(db);
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/10"+ntransDate+"_MRG.OUT";
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiStock.OutDist@"+distList.size()+"\n");
    	fw.write("-1,DistNO,FromNO,DistBalance,DispBillDate,Status,BillDate,SalesId,CustFlag,CustomerID,DueTo,AccMonth,EncashDay,TaxType,CurrID,ExchRate,Total,Tax,Discount,LocalTotal,LocalTax,DepartID,ProjectID,CashPay,SysID,VisaPay,CashPayStyle,VisaPayStyle,OtherPayStyle,OtherPay,LocOtherPay,InvoStyle,VoucherNO,MakerID,Maker,PermitterID,Permitter,UDef1,UDef2,Remark,CostFlag,HasCheck,NoCheckOffSet,NoCheckDisCount,NoChkLocalOffSet,InvoFlag,InvoBillNO,PriceOfTax,MergeOutStat\n");
    	fw.write("-1,BillNO,RowNO,SerNO,ProdID,WareID,Quantity,MLPrice,TaxRate,TaxAmt,MLAmount,MLDist,BillDate,FromRow,FromNO,NeedUpdate,ProdName,HaveBatch,Price,Amount,HasCheck,SUnitID,SPrice,SQuantity,UnitRelation,EQuantity,EUnitID,EUnitRelation,CostForAcc,StdCost,ItemRemark,Dist,DistTaxAmt\n");
    	fw.write("-1,BillNO,BatchID,RowNO,SerNO,SQuantity,Quantity,ProduceDate,ValidDate,ParentRowNO,QtyRemain,FromNO,FromRow,TranType,BatchDist,MLDist,HasCheck,ItemRemark,NeedUpdate,EQuantity\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			String locxcode = model.get("locxcode")==null?"":model.get("locxcode").toString();
			StringBuffer line = new StringBuffer();
			String trandate1 = model.get("trandate")==null?"":model.get("trandate").toString();
			String trandate="";
			double cvtxrate=0;
			String taxcode=model.get("taxcode")==null?"":model.get("taxcode").toString();
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMMdd");
			String accMonth = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMM");
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))//加上若轉入的公司別是I-part 或Jetone HK 就把本幣跟原幣一樣設為USD
		    {
		    	cvtxrate=1;
		    }
		    else
		    {
		    	cvtxrate=Double.parseDouble(model.get("cvtxrate")==null?"1":model.get("cvtxrate").toString());
		    }
			//加上備註
			String remark= model.get("remarksx")==null?"":model.get("remarksx").toString();
			//double cvtxrate = Double.parseDouble(model.get("cvtxrate")==null?"1":model.get("cvtxrate").toString());
			//計算本單總價及稅金
			double distOrig= new InteroutdfDao(db).findDistByNumber(model.get("trannubr")==null?"":model.get("trannubr").toString());
			double distTOrig=0;
			
			String distLoc = CommonUtil.getAmountRate(String.valueOf(distOrig), String.valueOf(cvtxrate));//計算本幣價
			String disTLoc="0";
			
			if(taxcode.equals("0"))
			{
				distTOrig=distOrig*0.05;
				disTLoc=CommonUtil.getTaxAmount(distLoc,"0.05");
			}

			line.append("0").append(",")                                                                  //A-1
			    .append(model.get("trannubr")==null?"":model.get("trannubr").toString()).append(",")      //B DistNO
			    .append(model.get("agnxnubr")==null?"":model.get("agnxnubr").toString()).append(",")      //C FromNO 
			    .append(0).append(",")                                                                    //D DistBalance
			    //.append("TRUE").append(",")        //custFlag                                             //E
			    .append(trandate).append(",")                                                             //F DispBillDate     
			    .append("1").append(",")                                                                  //G Status
			    .append(trandate).append(",")                                                             //H BillDate
			    .append(model.get("sales")==null?"":model.get("sales").toString()).append(",")      //I SalesId
			    .append("1").append(",")                                                                  //J CustFlag
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      //K CustomerID
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      //L DueTo
			    .append(accMonth).append(",")                                                             //M AccMonth
			    .append(trandate).append(",")                                                             //N EncashDay
			    .append(taxcode).append(",")        //O TaxType
			    .append(model.get("trancurr")==null?"":CommonUtil.getCurrency(tcompcode,model.get("trancurr").toString())).append(",")      //P CurrID
			    .append(cvtxrate).append(",")      //Q ExchRate
			    .append(distOrig).append(",")                                                                  //R Total
			    .append(distTOrig).append(",")                                                              //S Tax
			    .append("0").append(",")                                                                   //T Discount
			    .append(distLoc).append(",")                                                                  //U LocalTotal
			    .append(disTLoc).append(",")                                                              //V LocalTax
			    .append("A").append(",")                                                                   //W DepartID
			    .append("").append(",")                                                                   //X ProjectID
			    .append("0").append(",")                                                                  //Y CashPay
			    .append("2").append(",")	                                                              //Z SysID       
			    .append("0").append(",")                                                                  //AA VisaPay
			    .append("").append(",")                                                                   //AB CashPayStyle
			    .append("").append(",")                                                                   //AC VisaPayStyle
			    .append("").append(",")                                                                   //AD OtherPayStyle
			    .append("0").append(",")                                                                  //AE OtherPay
			    .append("0").append(",")                                                                  //AF LocOtherPay
			    .append("0").append(",")                                                                  //AG InvoStyle
			    //.append(model.get("payed")==null?"0":model.get("payed").toString()).append(",")           //AH OffSet 沖款金額
			    .append("").append(",")                                                                  //AI VoucherNO 傳票
			    .append(model.get("reginame")==null?"":model.get("reginame").toString()).append(",")      //AJ MakerID
			    .append("").append(",")                                                                   //AK Maker
			    .append("").append(",")                                                                   //AL PermitterID 設定是否已覆核     
			    .append("").append(",")                                                                   //AM Permitter
			    .append("").append(",")                                                                   //AN UDef1
			    .append(model.get("reginame")==null?"":model.get("reginame").toString()).append(",")      //AO UDef2
			    .append(CommonUtil.byteToHexString(remark)).append(",")                                   //AP Remark
			    //.append(model.get("localpayed")==null?"0":model.get("localpayed").toString()).append(",") //AQ LocalOffSet 本幣沖款金額
			    .append("701").append(",")                                                                   //AR CostFlag
			    .append("True").append(",")                                                               //AS HasCheck
			    .append("0").append(",")                                                                  //AT NoCheckOffSet
			    .append("0").append(",")                                                                  //AU NoCheckDisCount
			    .append("0").append(",")                                                                  //AV NoChkLocalOffSet
			    .append("3").append(",")                                                                  //AW InvoFlag
			    .append("").append(",")      //AX InvoBillNO
			    .append("True").append(",")                                                               //AY PriceOfTax
			    .append("0")//MergeOutStat
			    .append("\n")                                                                    
			;
			fw.write(line.toString());
			List lineList =  new InteroutdfDao(db).findByNumberDist(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{
				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				//double lineAmt = Double.parseDouble(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString())*Double.parseDouble(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString());
				//double taxRate = 0;
				//if(taxAmt>0)
				//{
				//	taxRate = 0.05;
				//}
				//double lineTaxAmt = lineAmt * taxRate;
				//String avgxcost = costDao.findByProd(compcode,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString() );
				
				//算出原單Line總價
				
				double origpric   = Double.parseDouble(modelLine.get("origcost")==null?"0":modelLine.get("origcost").toString()) ;
				double origqty   = Double.parseDouble(modelLine.get("origxqty")==null?"0":modelLine.get("origxqty").toString());
				double origTtl = origpric*origqty;
				double distLine=Double.parseDouble(modelLine.get("dist")==null?"0":modelLine.get("dist").toString());
				//算折讓價
				double disrateLine=0;
				double taxrate=0;
				double taxamt=0;
				if(taxcode.equals("0"))
				{
					taxrate=0.05;
					disrateLine=distLine*taxrate;
					taxamt=origTtl*taxrate;
				}
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                       //A -1
				        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")   //B BillNO
				        .append(rowno).append(",")                                                                     //C RowNO
				        .append(rowno).append(",")                                                                     //D SerNO
				        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")   //E ProdID
				        .append(locxcode).append(",")                                                                  //F WareID
				        .append(modelLine.get("origxqty")==null?"":modelLine.get("origxqty").toString()).append(",")   //G Quantity 原單數量
				        .append(origpric).append(",")                                                         //H MLPrice 單價
				        .append(taxrate).append(",")                                                                   //I TaxRate
				        .append(taxamt).append(",")                                                                    //J TaxAmt 原單稅率
				        .append(origTtl).append(",")                                                                   //K MLAmount =Amount
				        .append(distLine).append(",")                                                                  //L MLDist 折讓金額
				        .append(trandate).append(",")                                                                  //M BillDate
				        .append("1").append(",")                                                                       //N FromRow
				        .append(model.get("agnxnubr")==null?"":model.get("agnxnubr").toString()).append(",")           //O FromNO
				        .append("True").append(",")                                                                    //P NeedUpdate
				        .append(modelLine.get("prodname")==null?"":modelLine.get("prodname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   //Q ProdName
				        .append("False").append(",")                                                                   //R HaveBatch
				        .append(origpric).append(",")                                                           //S Price
				        .append(origTtl).append(",")                                                                   //T Amount  原單價格
				        .append("True").append(",")                                                                    //U HasCheck    
				        .append("").append(",")                                                                        //V SUnitID
				        .append(origpric).append(",") //W SPrice  單價
				        .append(modelLine.get("origxqty")==null?"":modelLine.get("origxqty").toString()).append(",")   //X SQuantity
				        .append("1").append(",")                                                                       //Y UnitRelation
				        .append(modelLine.get("origxqty")==null?"":modelLine.get("origxqty").toString()).append(",")   //Z EQuantity
				        .append("").append(",")                                                                        //AA EUnitID
				        .append("0").append(",")                                                                       //AB EUnitRelation
				        .append("0").append(",")                                                                       //AC CostForAcc
				        .append("0").append(",")                                                                       //AD StdCost
				        .append("").append(",")                                                                        //AE ItemRemark
				        .append(distLine).append(",")                                                                  //AF Dist
				        .append(disrateLine)//.append(",")                                                             //AG DistTaxAmt
				        .append("\n")                   
				        ;
						fw.write(lineLine.toString());
				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}
	
	/**
	 *銷貨退回 2012/5/25修改,把課稅類別都改為1空白,發票類型2,也不再乘上稅率 
	 * @param tcompcode
	 * @param transDate
	 * @throws SQLException
	 * @throws IOException
	 */
	public void ProcessSaleReturn(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS SALE RETURN DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList = new InteroutmfDao(db).findSaleReturn("00",tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		//List distList =  new ShipmatlmfDao(db).findByType("10");
		CostxdaibfDao costDao = new CostxdaibfDao(db);
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/05"+ntransDate+"_MRG.OUT";
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiStock.SaleBack@"+distList.size()+"\n");
    	fw.write("-1,SysID,FundBillNo,BillDate,CustFlag,CustID,DueTo,AccMonth,CurrID,ExchRate,Total,Tax,LocalTotal,LocalTax,PrepayDay,EncashDay,DepartID,ProjectID,Status,InvoStyle,SalesID,CashPayStyleID,CashPay,LocalCashPay,VisaPayStyleID,VisaPay,LocalVisaPay,OtherPayStyleID,OtherPay,LocOtherPay,AddrID,ZipCode,CustAddress,ContactPerson,LinkManProf,ContactPhone,PriceofTax,MakerID,Maker,VoucherNO,CVoucherNO,PermitterID,Permitter,UDef1,UDef2,Remark,HasCheck,CostFlag,NoCheckOffSet,NoCheckDisCount,NoChkLocalOffSet,InvoFlag,InvoBillNO,InvoiceNo,TaxType2,SumAmtATax,SumNum,TaxType,GatherStyle,GatherDelay,CheckStyle,CheckDelay,IsDeduct,SignBack,Detail,GatherOther,CheckOther,MergeOutStat\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BillDate,ProdID,ProdName,Quantity,QuanComb,MLPrice,MLAmount,WareID,CostAvg,QtyRemain,TranType,FromNO,FromRow,TaxRate,TaxAmt,MLDist,NeedUpdate,Price,Discount,Amount,ItemRemark,HaveBatch,HasCheck,SUnitID,SPrice,SQuantity,UnitRelation,CostForAcc,EQuantity,EUnitID,EUnitRelation,StdCost,MLTaxAmt,ConvertRate,IsGift,Detail\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BatchID,sBatchID,StorageID,SQuantity,Quantity,ProduceDate,ValidDate,ParentRowNO,QtyRemain,FromNO,FromRow,TranType,MLPrice,MLAmount,HasCheck,ItemRemark,EQuantity,CostAvg,HasSerial,ConfigNO,CharComb,CharQty,ProdID,WareID\n");
    	fw.write("-1,BillNo,PicNO,ParentRowNo,ParentSubRowNo,PicID,VaryType,CurrProdID\n");
    	fw.write("-1,BillNO,ParentRowNO,DetailRowNo,RowNO,IsDetail,SerNO,SerialID,FSerialID,TranType,FromNO,FromRow,QtyRemain,Quantity,ProdID,HasCheck,WareID,BatchID,sBatchID,StorageID,ConfigNo\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			String compcode = model.get("compcode")==null?"":model.get("compcode").toString();
			//String locxcode = model.get("locxcode")==null?"":model.get("locxcode").toString();
			StringBuffer line = new StringBuffer();
			//計算本單總價及稅金

			//double taxAmt   = Double.parseDouble(model.get("taxxxamt")==null?"":model.get("taxxxamt").toString()) ;
			//double origAmt = Double.parseDouble(model.get("totalamt")==null?"":model.get("totalamt").toString());
			String shipnubr=model.get("agnxnubr")==null?"":model.get("agnxnubr").toString();
			//單據幣別
			String trancurr=model.get("trancurr")==null?"":model.get("trancurr").toString();
			BigDecimal origAmt   =new BigDecimal(0);//銷退本金(原幣)			
			BigDecimal origtaxAmt   =new BigDecimal(0);//銷退稅金(原幣)
			//銷退總數量
			String quantity="";
		    double exchrate=0;
		    int origScale=0;//四捨五入的進位點orig
		    int localScale=0;//四捨五入的進位點orig
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))//加上若轉入的公司別是I-part 或Jetone HK 就把本幣跟原幣一樣設為USD
		    {
		    	exchrate=1;
		    	origScale=2;
		    	localScale=2;
		    }
		    else
		    {
		    	exchrate=Double.parseDouble(model.get("exchrate2")==null?"1":model.get("exchrate2").toString());
		    	if(trancurr.equals("U"))
		    		origScale=2;
		    	localScale=0;
		    }
			List sumList =new InteroutdfDao(db).findReturnByNumber(model.get("trannubr")==null?"":model.get("trannubr").toString());
			
			Iterator sumItr = sumList.iterator();
			if(sumItr.hasNext())
			{
				BasicDynaBean modelA = (BasicDynaBean) sumItr.next();
				origAmt=new BigDecimal(modelA.get("amount")==null?"0":modelA.get("amount").toString()).setScale(localScale, BigDecimal.ROUND_HALF_UP);
				quantity=modelA.get("qty")==null?"0":modelA.get("qty").toString();
			}
			 //計算稅金
			//計算稅金  updated at 20120525 將稅別改為1空白
			//String taxcode="0";
			String taxcode=model.get("taxtype2")==null?"":model.get("taxtype2").toString();
			

		    //發票號碼及發票狀態
		    String InvoiceNo=model.get("invonubr")==null?"":model.get("invonubr").toString();
		    String InvoFlag="";
		    if(InvoiceNo.length()==0)//沒有發票所以開立空白
		    	InvoFlag="2";
		    else
		    	InvoFlag="1";

			if(taxcode.equals("0"))
				origtaxAmt=origAmt.multiply(new BigDecimal(0.05)).setScale(origScale, BigDecimal.ROUND_HALF_UP);
			//銷退總金額(原幣)
			BigDecimal origTtl = origAmt.add(origtaxAmt).setScale(origScale);
			//銷退本金(本幣)
			BigDecimal localAmt = origAmt.multiply(new BigDecimal(exchrate)).setScale(localScale, BigDecimal.ROUND_HALF_UP);
			//銷退稅金(本幣)
			BigDecimal localTax = origtaxAmt.multiply(new BigDecimal(exchrate)).setScale(localScale, BigDecimal.ROUND_HALF_UP);
			//銷退總金額(本幣)
			//double localTtl = localAmt+localTax;
			//加上備註
			String remark= model.get("remarksx")==null?"":model.get("remarksx").toString();	
			
			String trandate1 = model.get("trandate")==null?"":model.get("trandate").toString();
			String trandate="";
			String trannubr=model.get("trannubr")==null?"":model.get("trannubr").toString();
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMMdd");
			String accMonth = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMM");
			line.append("0").append(",")                                                                  // -1
			    .append("2").append(",")                                                                  // SysID
			    .append(trannubr).append(",")                                                             // FundBillNo
			    .append(trandate).append(",")                                                             // BillDate
			    .append("1").append(",")                                                                  // CustFlag
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      // CustID
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      // DueTo
			    .append(accMonth).append(",")                                                             // AccMonth
			    .append(model.get("trancurr")==null?"":CommonUtil.getCurrency(tcompcode,model.get("trancurr").toString())).append(",")  // CurrID
			    .append(exchrate).append(",")      // ExchRate
			    .append(origAmt).append(",")      //Total 退回金額
			    .append(origtaxAmt).append(",")      //Tax
			    .append(localAmt).append(",")                                                             //LocalTotal
			    .append(localTax).append(",")                                                             //LocalTax
			    //.append(model.get("localpayed")==null?"0":model.get("localpayed").toString()).append(",") //LocalOffSet  要補進沖款金額
			    .append(trandate).append(",")                                                                   //PrepayDay
			    .append(trandate).append(",")                                                                   //EncashDay
			    .append("A").append(",")                                                                   //DepartID
			    .append("").append(",")                                                                   //ProjectID
			    .append("1").append(",")                                                                  //Status
			    .append(InvoFlag).append(",")                                                            //InvoStyle  2發票空白  1回收作廢
			    .append(model.get("sales")==null?"":model.get("sales").toString()).append(",")      //SalesID
			    .append("").append(",")                                                                  //CashPayStyleID
			    .append("0").append(",")                                                                  //CashPay
			    .append("0").append(",")                                                                  //LocalCashPay
			    .append("").append(",")                                                                  //VisaPayStyleID
			    .append("0").append(",")                                                                  //VisaPay
			    .append("0").append(",")                                                                  //LocalVisaPay
			    .append("").append(",")                                                                   //OtherPayStyleID
			    .append("0").append(",")                                                                  //OtherPay
			    .append("0").append(",")                                                                  //LocOtherPay
			    .append("").append(",")                                                                   //AddrID 送貨地址
			    .append("").append(",")                                                                   //ZipCode
			    .append("").append(",")                                                                   //CustAddress
			    .append(model.get("contactx")==null?"":model.get("contactx").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")  //ContactPerson
			    .append("").append(",")                                                                   //LinkManProf
			    .append("").append(",")                                                                   //ContactPhone
			    .append("True").append(",")                                                              //PriceofTax
			    .append(model.get("regiuser")==null?"":model.get("regiuser").toString()).append(",")       //MakerID
			    .append(model.get("reginame")==null?"":model.get("reginame").toString()).append(",")       //Maker
			    .append("").append(",")                                                                   //VoucherNO
			    .append("").append(",")                                                                   //CVoucherNO
			    .append("").append(",")                                                                   //PermitterID  設定是否已覆核 
			    .append("").append(",")                                                                   //Permitter
			    .append("").append(",")                                                                   //UDef1
			    .append("").append(",")                                                                   //UDef2
			    .append(CommonUtil.byteToHexString(remark)).append(",")                                   //Remark
			    .append("True").append(",")                                                               //HasCheck
			    .append("600").append(",")                                                                  //CostFlag
			    .append("0").append(",")                                                                  //NoCheckOffSet
			    .append("0").append(",")                                                                  //NoCheckDisCount
			    .append("0").append(",")                                                                  //NoChkLocalOffSet
			    .append("0").append(",")                                                                  //InvoFlag
			    .append("").append(",")                                                                   //InvoBillNO
			    .append(InvoiceNo).append(",")                                                            //InvoiceNo 發票號碼
			    .append("0").append(",")                                                                  //TaxType2
			    .append(origTtl).append(",")                                                              //SumAmtATax
			    .append(quantity).append(",")                                                             //SumNum
			    .append(taxcode).append(",")                                                              //TaxType  稅別
			    .append("0").append(",")                                                                  //GatherStyle
			    .append("0").append(",")                                                                  //GatherDelay  貨到幾天
			    .append("0").append(",")                                                                  //CheckStyle
			    .append("0").append(",")                                                                  //CheckDelay
			    .append("False").append(",")                                                              //IsDeduct
			    .append("False").append(",")                                                              //SignBack
			    .append("A").append(",")                                                                   //Detail
			    .append("").append(",")                                                                   //GatherOther
			    .append("").append(",")                                                                   //CheckOther
			    .append("0").append("")                                                                   //MergeOutStat
			    .append("\n")  
			    
			;
			fw.write(line.toString());
			List lineList =  new InteroutdfDao(db).findByNumberSale(model.get("trannubr")==null?"":model.get("trannubr").toString(),shipnubr);
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{
				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				BigDecimal lineAmt   =new BigDecimal(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString()).multiply(new BigDecimal(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString())).setScale(origScale,BigDecimal.ROUND_HALF_UP);//銷退本金(原幣)	
				//double lineAmt = Double.parseDouble()*Double.parseDouble();
				//BigDecimal   b   =   new   BigDecimal(lineAmt);
				//lineAmt=b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
				//來原單據若為新版單據,則抓origtrannubr跟origseqtnubr 舊版抓主檔的shipnubr跟seqtnubr
				//FromNo(原單號) & FromRow(原列號)
				String FromNo="";
				String FromRow="";
				String ToLoc="";//銷貨退回的倉庫
				if(trannubr.indexOf("IN")==-1)//舊版
				{
					FromNo=shipnubr;
					FromRow=modelLine.get("seqtnubr")==null?"":modelLine.get("seqtnubr").toString();
					ToLoc=model.get("recvxloc")==null?"":model.get("recvxloc").toString();
				}
				else//新版
				{
					FromNo=modelLine.get("origtrannubr")==null?"":modelLine.get("origtrannubr").toString();
					FromRow=modelLine.get("origseqtnubr")==null?"":modelLine.get("origseqtnubr").toString();
					ToLoc=modelLine.get("locxcode")==null?"":modelLine.get("locxcode").toString();
				}
				double taxRate = 0;
				if(taxcode.equals("0"))
					taxRate = 0.05;
				
				BigDecimal lineTaxAmt = lineAmt.multiply(new BigDecimal(taxRate)).setScale(origScale,BigDecimal.ROUND_HALF_UP);
				String avgxcost = costDao.findByProd(compcode,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString() ); 
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                       // -1
				        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")   // BillNO
				        .append(rowno).append(",")                                                                     // RowNO ??
				        .append(rowno).append(",")                                                                     // SerNO  ??
				        .append(trandate).append(",")                                                                  // BillDate
				        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")   // ProdID
				        .append(modelLine.get("prodname")==null?"":modelLine.get("prodname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   // ProdName
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")   // Quantity
				        .append("0").append(",")                                                                       // QuanComb
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")   // MLPrice
				        .append(lineAmt).append(",")                                                                   // MLAmount
				        .append(ToLoc).append(",")          // WareID抓入庫倉庫
				        .append(avgxcost).append(",")                                                         // CostAvg 進貨成本,查成本簿
				        .append(modelLine.get("avalxqty")==null?"0":modelLine.get("avalxqty").toString()).append(",")  //QtyRemain	
				        .append("209").append(",")                                                         //TranType
				        .append(FromNo).append(",")                                                                 //FromNo來原單據號碼
				        .append(FromRow).append(",")                                                                //FromRow來原單據列數
				        .append(taxRate).append(",")                                                                //TaxRate 
				        .append(lineTaxAmt).append(",")                                                                 //TaxAmt
				        .append("0").append(",")                                                                        //MLDist
				        .append("True").append(",")                                                                     //NeedUpdate
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")    //Price
				        .append("1").append(",")                                                         //Discount
				        .append(lineAmt).append(",")                                                         //Amount
				        .append("").append(",")                                                         //ItemRemark
				        .append("False").append(",")                                                         //HaveBatch
				        .append("False").append(",")                                                         //HasCheck
				        .append("").append(",")                                                         //SUnitID
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")  //SPrice
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")  //SQuantity			        
				        .append("1").append(",")                                                         //UnitRelation
				        .append("0").append(",")                                                         //CostForAcc
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")  //EQuantity
				        .append("").append(",")                                                            //EUnitID		        
				        .append("0").append(",")                                                           //EUnitRelation
				        .append("0").append(",")                                                           //StdCost
				        .append(lineTaxAmt).append(",")                                                    //MLTaxAmt
				        .append("0").append(",")                                                           //ConvertRate				        
				        .append("False").append(",")                                                       //IsGift
				        .append("")//.append(",")                                                          //Detail
				        .append("\n")                   
				        ;
						fw.write(lineLine.toString());
				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}
	
	
	/**
	 *進貨退出 2012/5/25修改,把課稅類別都改為空白,也不再乘上稅率 
	 * @param tcompcode
	 * @param transDate
	 * @throws SQLException
	 * @throws IOException
	 */
	public void ProcessRecvReturn(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS Recv RETURN DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList = new InteroutmfDao(db).findRecvReturn("18",tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		//List distList =  new ShipmatlmfDao(db).findByType("10");
		CostxdaibfDao costDao = new CostxdaibfDao(db);
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/07"+ntransDate+"_MRG.OUT";
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiStock.BuyBack@"+distList.size()+"\n");
    	fw.write("-1,SysID,FundBillNo,BillDate,CustFlag,CustID,DueTo,AccMonth,CurrID,ExchRate,Total,Tax,LocalTotal,LocalTax,LocalOffSet,PrepayDay,EncashDay,DepartID,ProjectID,Status,InvoStyle,SalesID,CashPayStyleID,CashPay,LocalCashPay,VisaPayStyleID,VisaPay,LocalVisaPay,OtherPayStyleID,OtherPay,LocOtherPay,AddrID,ZipCode,CustAddress,ContactPerson,LinkManProf,ContactPhone,PriceofTax,VoucherNO,MakerID,Maker,PermitterID,Permitter,UDef1,UDef2,Remark,HasCheck,CostFlag,NoCheckOffSet,NoCheckDisCount,NoChkLocalOffSet,InvoFlag,InvoBillNO,InvoiceNo,TaxType2,SumAmtATax,SumNum,TaxType,GatherStyle,GatherDelay,CheckStyle,CheckDelay,IsDeduct,SignBack,Detail,GatherOther,CheckOther,MergeOutStat\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BillDate,ProdID,ProdName,Quantity,QuanComb,MLPrice,MLAmount,WareID,CostAvg,QtyRemain,TranType,FromNO,FromRow,TaxRate,TaxAmt,MLDist,NeedUpdate,Price,Discount,Amount,ItemRemark,HaveBatch,HasCheck,SUnitID,SPrice,SQuantity,UnitRelation,EQuantity,EUnitID,EUnitRelation,StdCost,CostForAcc,MLTaxAmt,ConvertRate,IsGift,Detail\n");
    	fw.write("-1,BillNO,RowNO,SerNO,BatchID,SQuantity,Quantity,ProduceDate,ValidDate,ParentRowNO,QtyRemain,FromNO,FromRow,TranType,MLPrice,MLAmount,HasCheck,ItemRemark,EQuantity\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			String compcode = model.get("compcode")==null?"":model.get("compcode").toString();
			String locxcode = model.get("locxcode")==null?"":model.get("locxcode").toString();
			StringBuffer line = new StringBuffer();
			//計算本單總價及稅金

			//double taxAmt   = Double.parseDouble(model.get("taxxxamt")==null?"":model.get("taxxxamt").toString()) ;
			//double origAmt = Double.parseDouble(model.get("totalamt")==null?"":model.get("totalamt").toString());
			String recvnubr=model.get("agnxnubr")==null?"":model.get("agnxnubr").toString();
			//進退本金(原幣)
			double origAmt=0;
			//進退總數量
			String quantity="";
			double cvrate=0;
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))//加上若轉入的公司別是I-part 或Jetone HK 就把本幣跟原幣一樣設為USD
		    {
		    	cvrate=1;
		    }
		    else
		    {
		    	cvrate=Double.parseDouble(model.get("exchrate2")==null?"1":model.get("exchrate2").toString());
		    }
			List sumList =new InteroutdfDao(db).findReturnByNumber(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator sumItr = sumList.iterator();
			if(sumItr.hasNext())
			{
				BasicDynaBean modelA = (BasicDynaBean) sumItr.next();
				origAmt=Double.parseDouble(modelA.get("amount")==null?"0":modelA.get("amount").toString());
				quantity=modelA.get("qty")==null?"0":modelA.get("qty").toString();
			}
			//計算稅金  updated at 20120525 將稅別改為1空白
			//String taxcode="0";
			String taxcode=model.get("taxtype2")==null?"":model.get("taxtype2").toString();
			//進退稅金(原幣)
			double origtaxAmt   =0;
			if(taxcode.equals("0"))
				origtaxAmt=origAmt*0.05;
			//進退總金額(原幣)
			double origTtl = origtaxAmt + origAmt;
			//進退本金(本幣)
			double localAmt = origAmt * cvrate;
			//進退稅金(本幣)
			double localTax = origtaxAmt * cvrate;
			//進退總金額(本幣)
			double localTtl = localAmt+localTax;
			//加上備註
			String remark= model.get("remarksx")==null?"":model.get("remarksx").toString();
			String trandate1 = model.get("trandate")==null?"":model.get("trandate").toString();
			String trandate="";
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMMdd");
			String accMonth = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMM");
			line.append("0").append(",")                                                                  // -1
			    .append("2").append(",")                                                                  // SysID
			    .append(model.get("trannubr")==null?"":model.get("trannubr").toString()).append(",")      // FundBillNo
			    .append(trandate).append(",")                                                             // BillDate
			    .append("2").append(",")                                                                  // CustFlag
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      // CustID
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      // DueTo
			    .append(accMonth).append(",")                                                             // AccMonth
			    .append(model.get("trancurr")==null?"":CommonUtil.getCurrency(tcompcode,model.get("trancurr").toString())).append(",")  // CurrID
			    .append(cvrate).append(",")      // ExchRate
			    .append(origAmt).append(",")      //Total 退回金額
			    .append(origtaxAmt).append(",")      //Tax
			    .append(localAmt).append(",")                                                             //LocalTotal
			    .append(localTax).append(",")                                                             //LocalTax
			    .append("0").append(",")                                                                  //LocalOffSet
			    .append(trandate).append(",")                                                                   //PrepayDay
			    .append(trandate).append(",")                                                                   //EncashDay
			    .append("A").append(",")                                                                   //DepartID
			    .append("").append(",")                                                                   //ProjectID
			    .append("1").append(",")                                                                  //Status
			    .append("2").append(",")                                                                  //InvoStyle  發票類型2為空白
			    .append(model.get("buycode")==null?"":model.get("buycode").toString()).append(",")      //SalesID
			    .append("").append(",")                                                                  //CashPayStyleID
			    .append("0").append(",")                                                                  //CashPay
			    .append("0").append(",")                                                                  //LocalCashPay
			    .append("").append(",")                                                                  //VisaPayStyleID
			    .append("0").append(",")                                                                  //VisaPay
			    .append("0").append(",")                                                                  //LocalVisaPay
			    .append("").append(",")                                                                   //OtherPayStyleID
			    .append("0").append(",")                                                                  //OtherPay
			    .append("0").append(",")                                                                  //LocOtherPay
			    .append("").append(",")                                                                   //AddrID 送貨地址
			    .append("").append(",")                                                                   //ZipCode
			    .append(model.get("shipaddr")==null?"":model.get("shipaddr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")  //CustAddress
			    .append(model.get("shipname")==null?"":model.get("shipname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")  //ContactPerson
			    .append("").append(",")                                                                   //LinkManProf
			    .append("").append(",")                                                                   //ContactPhone
			    .append("True").append(",")                                                              //PriceofTax
			    .append("").append(",")                                                                   //VoucherNO
			    .append(model.get("regiuser")==null?"":model.get("regiuser").toString()).append(",")       //MakerID
			    .append(model.get("reginame")==null?"":model.get("reginame").toString()).append(",")       //Maker
			    .append("").append(",")                                                                   //PermitterID  設定是否已覆核 
			    .append("").append(",")                                                                   //Permitter
			    .append("").append(",")                                                                   //UDef1
			    .append("").append(",")                                                                   //UDef2
			    .append(CommonUtil.byteToHexString(remark)).append(",")                                   //remark
			    .append("True").append(",")                                                               //HasCheck
			    .append("").append(",")                                                                   //CostFlag
			    .append("0").append(",")                                                                  //NoCheckOffSet
			    .append("0").append(",")                                                                  //NoCheckDisCount
			    .append("0").append(",")                                                                  //NoChkLocalOffSet
			    .append("0").append(",")                                                                  //InvoFlag
			    .append("").append(",")                                                                   //InvoBillNO
			    .append("").append(",")                                                                   //InvoiceNo
			    .append("0").append(",")                                                                  //TaxType2
			    .append(origTtl).append(",")                                                              //SumAmtATax
			    .append(quantity).append(",")                                                             //SumNum
			    .append(taxcode).append(",")                                                              //TaxType 課稅類別
			    .append("0").append(",")                                                                  //GatherStyle
			    .append("0").append(",")                                                                  //GatherDelay  貨到幾天
			    .append("0").append(",")                                                                  //CheckStyle
			    .append("0").append(",")                                                                  //CheckDelay
			    .append("False").append(",")                                                              //IsDeduct
			    .append("False").append(",")                                                              //SignBack
			    .append("A").append(",")                                                                   //Detail
			    .append("").append(",")                                                                   //GatherOther
			    .append("").append(",")                                                                   //CheckOther
			    .append("0").append("")                                                                   //MergeOutStat
			    .append("\n")  
			    
			;
			fw.write(line.toString());
			List lineList =  new InteroutdfDao(db).findByNumberRecv(model.get("trannubr")==null?"":model.get("trannubr").toString(),recvnubr);
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{
				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				
				double lineAmt = Double.parseDouble(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString())*Double.parseDouble(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString());
				BigDecimal   b   =   new   BigDecimal(lineAmt);
				lineAmt=b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
				
				ShipmatlmfDao shipDAO= new ShipmatlmfDao(db);
				//查出原單的總量
				//int origxqty=shipDAO.findOrigxqty(recvnubr,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString(),
				//		modelLine.get("recordno")==null?"0":modelLine.get("recordno").toString());
				//剩下原單可申請的量
				int origxqty=modelLine.get("origxqty")==null?0:Integer.parseInt(modelLine.get("origxqty").toString());
				int avalqty=shipDAO.findOrigxqty(recvnubr,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()
						,modelLine.get("poxxnubr")==null?"":modelLine.get("poxxnubr").toString(), String.valueOf(origxqty), modelLine.get("recordno")==null?"0":modelLine.get("recordno").toString());
				//int avalqty=Integer.parseInt(modelLine.get("origxqty")==null?"0":modelLine.get("origxqty").toString())- Integer.parseInt(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString());
				double taxRate = 0;
				if(taxcode.equals("0"))
					taxRate = 0.05;
				//來原單據若為新版單據,則抓origtrannubr跟origseqtnubr 舊版抓主檔的shipnubr跟seqtnubr
				//FromNo(原單號) & FromRow(原列號)
				String FromNo="";
				String FromRow="";
				if(recvnubr.indexOf("IN")==-1)//舊版
				{
					FromNo=recvnubr;
					FromRow=modelLine.get("recordno")==null?"":modelLine.get("recordno").toString();
				}
				else//新版
				{
					FromNo=modelLine.get("origtrannubr")==null?"":modelLine.get("origtrannubr").toString();
					FromRow=modelLine.get("origseqtnubr")==null?"":modelLine.get("origseqtnubr").toString();
				}
				
				double lineTaxAmt = lineAmt * taxRate;
				String avgxcost = costDao.findByProd(compcode,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString() ); 
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                       // -1
				        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")   // BillNO
				        .append(rowno).append(",")                                                                     // RowNO 
				        .append(rowno).append(",")                                                                     // SerNO  
				        .append(trandate).append(",")                                                                  // BillDate
				        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")   // ProdID
				        .append(modelLine.get("prodname")==null?"":modelLine.get("prodname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   // ProdName
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")   // Quantity
				        .append("0").append(",")                                                                       // QuanComb
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")   // MLPrice
				        .append(lineAmt).append(",")                                                                   // MLAmount
				        .append(modelLine.get("locxcode")==null?"":modelLine.get("locxcode").toString()).append(",")   // WareID倉庫
				        .append(avgxcost).append(",")                                                         // CostAvg 進貨成本,查成本簿
				        .append(avalqty).append(",")  //QtyRemain	
				        .append("210").append(",")                                                         //TranType
				        .append(FromNo).append(",")                                                                 //FromNO
				        .append(FromRow).append(",")                                                                //FromRow
				        .append(taxRate).append(",")      //TaxRate 
				        .append(lineTaxAmt).append(",")                                                                 //TaxAmt
				        .append("0").append(",")                                                                        //MLDist
				        .append("True").append(",")                                                                     //NeedUpdate
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")    //Price
				        .append("1").append(",")                                                         //Discount
				        .append(lineAmt).append(",")                                                         //Amount
				        .append("").append(",")                                                         //ItemRemark
				        .append("False").append(",")                                                         //HaveBatch
				        .append("False").append(",")                                                         //HasCheck
				        .append("").append(",")                                                         //SUnitID
				        .append(modelLine.get("unitpric")==null?"":modelLine.get("unitpric").toString()).append(",")  //SPrice
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")  //SQuantity			        
				        .append("1").append(",")                                                         //UnitRelation
				        .append(modelLine.get("quantity")==null?"":modelLine.get("quantity").toString()).append(",")  //EQuantity
				        .append("").append(",")                                                            //EUnitID		        
				        .append("0").append(",")                                                           //EUnitRelation
				        .append("0").append(",")                                                           //StdCost
				        .append("0").append(",")                                                         //CostForAcc
				        .append(lineTaxAmt).append(",")                                                    //MLTaxAmt
				        .append("0").append(",")                                                           //ConvertRate				        
				        .append("False").append(",")                                                       //IsGift
				        .append("")//.append(",")                                                          //Detail
				        .append("\n")                   
				        ;
						fw.write(lineLine.toString());
				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}
	
	public void ProcessAdjustInventry(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS ADJUST INVENTRY DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		AdjustDAO adjDAO=new AdjustDAO(db);
		List distList = adjDAO.findListMf(tcompcode,transDate);
		//if(distList.size()==0)
		//	return;
		CostxdaibfDao costDao = new CostxdaibfDao(db);
		//String today = DateTimeUtil.DateTimeFormatToString(new Date(), "MMddyyyy");
		//String ntransDate=DateTimeUtil.transDateFormatString(transDate, "yyyy-MM-dd", "MMddyyyy");
		//String filename = "../JetDaemon/"+tcompcode+"/12"+ntransDate+"_MRG.OUT";
    	//FileWriter fw = new FileWriter(filename);
    	//fw.write("CHT0110010VERIFYNTD\n");
    	fw.write("[SngProgID]ChiStock.Adjust@"+distList.size()+"\n");
    	fw.write("-1,BillNo,AdjustDate,AdjustClassID,AdjustStyle,Maker,Permitter,SumQty,SumCost,UDef1,UDef2,Remark,VoucherNO,DepartID,MakerID,PermitterID,DataVer,HasCheck,MergeOutStat\n");
    	fw.write("-1,BillNo,AdjustDate,SubHaveBatch,RowNo,SerNO,BillDate,ProdID,ProdName,Quantity,WareID,QuanComb,Price,Amount,ItemRemark,MLPrice,MLAmount,AdjustStyle,CostAvg,QtyRemain,TaxRate,TaxAmt,NeedUpdate,HaveBatch,CostForAcc,HasCheck,EQuantity,EUnitID,EUnitRelation,SUnitID,SQuantity,UnitRelation,StdCost,BatchID,BQuantity,ProduceDate,ValidDate,ParentRowNO,BMLPrice,BMlAmount,BCostAvg,BCostForAcc,BNeedUpdate,BAdjustStyle,BatchHasCheck,BSQuantity,BEQuantity\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			int sumqty=0;
			StringBuffer line = new StringBuffer();
			String trandate = model.get("modidate")==null?"":model.get("modidate").toString();
			String applydate = model.get("applydate")==null?"":model.get("applydate").toString();
			applydate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(applydate, "yyyy-MM-dd HH:mm:ss"), "yyyyMMdd");
			String adnubr = model.get("adnubr")==null?"":model.get("adnubr").toString();
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate, "yyyy-MM-dd HH:mm:ss"), "yyyyMMdd");
			String reason= model.get("reason")==null?"":model.get("reason").toString();
			sumqty=adjDAO.findAdjustSum(adnubr);
			String adtype=model.get("adtype")==null?"":model.get("adtype").toString();//調整類型
			String adstyle="0";
			if(adtype.equals("A04"))
				adstyle="2";
			line.append("0").append(",")                                                                  //A -1
			    .append(adnubr).append(",")                                                                  //B BillNo
			    .append(trandate).append(",")      														  //C AdjustDate
			    .append(adtype).append(",")        //D AdjustClassID
			    .append(adstyle).append(",")                                                                  //E AdjustStyle
			    .append(model.get("modusername")==null?"":model.get("modusername").toString()).append(",")  //F Maker    
			    .append("").append(",")                                                                  //G Permitter
			    .append(sumqty).append(",")                                                                     //H SumQty
			    .append("0").append(",")                                                                  //I SumCost
			    .append("").append(",")       																//J UDef1
			    .append("").append(",")      															   //K UDef2
			    .append(CommonUtil.byteToHexString(reason)).append(",")   	//L Remark
			    .append("").append(",")                                                             //M VoucherNO
			    .append("A").append(",")                                                             //N DepartID
			    .append(model.get("applier")==null?"":model.get("applier").toString()).append(",")  //O MakerID
			    .append("").append(",")      														  //P PermitterID
			    .append("1").append(",")      															  //Q DataVer
			    .append("True").append(",")                                                                 //R HasCheck			 
			    .append("0")//MergeOutStat
			    .append("\n");
			;
			fw.write(line.toString());
			List lineList =  adjDAO.findListDf(adnubr);
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				//平均成本
				String avgxcost = costDao.findByProd(tcompcode,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString() );
				//成本調整
				BigDecimal cost= new BigDecimal(modelLine.get("avgxcost")==null?"0":modelLine.get("avgxcost").toString());
				BigDecimal newcost=new BigDecimal(0);
				newcost=cost.setScale(4,RoundingMode.HALF_UP);
				
			    /*if(tcompcode.equals("I")||tcompcode.equals("H"))//加上若轉入的公司別是I-part 或Jetone HK 就設定為小數點第二位
			    {
			    	newcost=cost.setScale(2,RoundingMode.HALF_UP);
			    }
			    else
			    {
			    	newcost=cost.setScale(0,RoundingMode.HALF_UP);
			    }*/
				
				//總成本
				int qty=modelLine.get("quantity")==null?0:Integer.parseInt(modelLine.get("quantity").toString());
				double costacc= newcost.multiply(new BigDecimal(qty)).doubleValue();//qty*Double.parseDouble(avgxcost);
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                       //A -1
				        .append(adnubr).append(",")   //B BillNO
				        .append(trandate).append(",")                                                                     //C AdjustDate
				        .append("False").append(",")                                                                     //D SubHaveBatch
				        .append(rowno).append(",")   //E RowNo
				        .append(rowno).append(",")                                                                  //F SerNO
				        .append(trandate).append(",")   //G BillDate
				        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")   //H ProdID
				        .append(modelLine.get("prodname")==null?"":modelLine.get("prodname").toString().toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   //I ProdName
				        .append(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString()).append(",")  //J Quantity
				        .append(modelLine.get("locxcode")==null?"":modelLine.get("locxcode").toString()).append(",")   //K WareID
				        .append("0").append(",")                                                                    //L QuanComb
				        .append(newcost).append(",")                                                                   //M Price --申請價
				        .append(costacc).append(",")                                                                //N Amount --台幣價
				        .append("").append(",")                                                                     //O ItemRemark
				        .append(newcost).append(",")                                                                   //P MLPrice--原幣成本價
				        .append(costacc).append(",")                                                                //Q MLAmount--四捨五入成本價
				        .append(adstyle).append(",")                                                                //R AdjustStyle //2要改
				        .append(avgxcost).append(",")                                                               //S CostAvg  現行平均成本
				        .append("0").append(",")                                                                    //T QtyRemain
				        .append("0").append(",")                                                                    //U TaxRate     
				        .append("0").append(",")                                                                    //V TaxAmt
				        .append("True").append(",")                                                                 //W NeedUpdate
				        .append("False").append(",")                                                          //X HaveBatch
				        .append(costacc).append(",")                                                          //Y CostForAcc  總成本
				        .append("True").append(",")                                                            //Z HasCheck
				        .append(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString()).append(",") //AA EQuantity
				        .append("").append(",")                                                                       //AB EUnitID
				        .append("0").append(",")                                                                       //AC EUnitRelation
				        .append("").append(",")                                                                       //AD SUnitID
				        .append(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString()).append(",") //AE SQuantity
				        .append("1").append(",")                                                                       //AF UnitRelation
				        .append("0").append(",")                                                                     //AG StdCost
				        .append("").append(",")                                                                       //AH BatchID
				        .append("").append(",")                                                                       //AI BQuantity
				        .append("").append(",")                                                                       //AJ ProduceDate
				        .append("").append(",")                                                                        //AK ValidDate
				        .append("").append(",")                                                                       //AL ParentRowNO
				        .append("").append(",")                                                                     //AM BMLPrice
				        .append("").append(",")                                                                        //AN BMlAmount
				        .append("").append(",")                                                                       //AO BCostAvg
				        .append("").append(",")                                                                       //AP BCostForAcc
				        .append("True").append(",")                                                                    //AQ BNeedUpdate
				        .append(adstyle).append(",")                                                                   //AR BAdjustStyle
				        .append("True").append(",")                                                                    //AS BatchHasCheck
				        .append(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString()).append(",") //AT BSQuantity
				        .append(modelLine.get("quantity")==null?"0":modelLine.get("quantity").toString())             //AU BEQuantity
				        .append("\n")
				        ;                  

						fw.write(lineLine.toString());
				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}
	/**
	 * 借入憑單
	 * @param tcompcode
	 * @param transDate
	 * @param fw
	 * @throws SQLException
	 * @throws IOException
	 */
	public void ProcessBorrowIn(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS BorrowIn DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		ProcmuntDao proDAO=new ProcmuntDao(db);
		List distList = proDAO.findListMf(tcompcode,transDate);

    	fw.write("[SngProgID]ChiStock.Borrow@"+distList.size()+"\n");
    	fw.write("-1,BorrowNO,BorrowDate,BorrowType,CustomerID,SignBack,AddrID,ZipCode,CustAddress,BorrowStyleID,CurrID,ExchRate,ContactPerson,ContactPhone,SalesmanID,DeptID,Maker,Permitter,UDef1,UDef2,Remark,SumQty,SumAmt,PermitterID,MakerID,LinkManProf,Detail,PriceofTax,SumTax,SumAmtATax,LocalSumAmt,LocalSumTax,TaxType,HasCheck,CustomerName,CustShortName,TERM,TermContent,TranToFlag\n");
    	fw.write("-1,RowNo,SerNo,BorrowNO,BorrowDate,ProdID,ProdName,WareHouseID,Quantity,QuanComb,Price,DisCount,Amount,HaveBatch,PreReturnDate,ItemRemark,QtyRemain,TranType,Detail,TaxRate,TaxAmt,HasCheck,SUnitID,SPrice,SQuantity,UnitRelation,EQuantity,EUnitID,EUnitRelation,FromNo,FromRow,CloseID,HasCharacter,HasPic,PurchaseNo,PurchaseRow\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			int sumqty=0;
			StringBuffer line = new StringBuffer();
			String trandate = model.get("modidate")==null?"":model.get("modidate").toString();
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate, "yyyy-MM-dd HH:mm:ss"), "yyyyMMdd");
			String poxxnubr = model.get("poxxnubr")==null?"":model.get("poxxnubr").toString();

			String remarksx= model.get("remarksx")==null?"":model.get("remarksx").toString();
			sumqty=proDAO.findProcSum(poxxnubr);

			line.append("0").append(",")                                                                  //A -1
			    .append(model.get("poxxnubr")==null?"":model.get("poxxnubr").toString()).append(",")      //B BorrowNO
			    .append(trandate).append(",")      														  //C BorrowDate
			    .append("2").append(",")                                                                 //D BorrowType
			    .append(model.get("vndrcode")==null?"":model.get("vndrcode").toString()).append(",")                                                                  //E CustomerID
			    .append("False").append(",")  //F SignBack   
			    .append("1").append(",")                                                                  //G AddrID
			    .append("").append(",")                                                               //H ZipCode
			    .append(model.get("vndraddr")==null?"":model.get("vndraddr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                                                                  //I CustAddress
			    .append("1").append(",")       																//J BorrowStyleID
			    .append("USD").append(",")      															   //K CurrID
			    .append("30").append(",")   	//L ExchRate
			    .append(model.get("contname")==null?"":model.get("contname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")                                                             //M ContactPerson
			    .append(model.get("telxnubr")==null?"":model.get("telxnubr").toString()).append(",")                                                             //N ContactPhone
			    .append(model.get("regiuser")==null?"":model.get("regiuser").toString()).append(",")  //O SalesmanID
			    .append("A").append(",")      														  //P DeptID
			    .append(model.get("modiname")==null?"":model.get("modiname").toString()).append(",")      															  //Q Maker
			    .append("").append(",")                                                                 //R Permitter			 
			    .append("").append(",")//S UDef1
			    .append("").append(",")//T UDef2
			    .append(CommonUtil.byteToHexString(remarksx)).append(",")//U Remark
			    .append(sumqty).append(",")//V SumQty			    
			    .append("0").append(",")//W SumAmt
			    .append("").append(",")//X PermitterID
			    .append(model.get("modiuser")==null?"":model.get("modiuser").toString()).append(",")//Y MakerID
			    .append("").append(",")//Z LinkManProf
			    .append("A").append(",")//AA Detail
			    .append("True").append(",")//AB PriceofTax
			    .append("0").append(",")//AC SumTax
			    .append("0").append(",")//AD SumAmtATax
			    .append("0").append(",")//AE LocalSumAmt
			    .append("0").append(",")//AF LocalSumTax
			    .append("0").append(",")//AG TaxType
			    .append("True").append(",")//AH HasCheck
			    .append(model.get("vndrname")==null?"":model.get("vndrname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")//AI CustomerName	
			    .append(model.get("vndrabbr")==null?"":model.get("vndrabbr").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")//AJ CustShortName	
			    .append("0").append(",")//AK TERM	
			    .append("").append(",")//AL TermContent	
			    .append("False")//AM TranToFlag				    
			    .append("\n");
			;
			fw.write(line.toString());
			List lineList =  proDAO.findListDf(poxxnubr);
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				//平均成本
				//String avgxcost = costDao.findByProd(tcompcode,modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString() );
				//總成本
				int qty=modelLine.get("quantity")==null?0:Integer.parseInt(modelLine.get("quantity").toString());
				//double costacc= qty*Double.parseDouble(avgxcost);
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                       //A 1
				        .append(rowno).append(",")                                                                    //B RowNo
				        .append(rowno).append(",")                                                                     //C SerNo
				        .append(poxxnubr).append(",")                                                                     //D BorrowNO
				        .append(trandate).append(",")   //E BorrowDate
				        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")                                                                  //F ProdID
				        .append(modelLine.get("prodname")==null?"":modelLine.get("prodname").toString().toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   //G ProdName
				        .append(modelLine.get("locxcode")==null?"":modelLine.get("locxcode").toString()).append(",")   //H WareHouseID
				        .append(qty).append(",")   //I Quantity
				        .append("0").append(",")  //J QuanComb
				        .append("0").append(",")   //K Price
				        .append("1").append(",")                                                                    //L DisCount
				        .append("0").append(",")                                                                    //M Amount
				        .append("False").append(",")                                                                //N HaveBatch
				        .append(trandate).append(",")                                                               //O PreReturnDate
				        .append("").append(",")                                                                    //P ItemRemark
				        .append("0").append(",")                                                                    //Q QtyRemain
				        .append("0").append(",")                                                                    //R TranType
				        .append("").append(",")                                                               //S Detail
				        .append("0.05").append(",")                                                                    //T TaxRate
				        .append("0").append(",")                                                                    //U TaxAmt     
				        .append("True").append(",")                                                                    //V HasCheck
				        .append("").append(",")                                                                 //W SUnitID
				        .append("0").append(",")                                                          //X SPrice
				        .append(qty).append(",")  //Y SQuantity
				        .append("0").append(",")                                                            //Z UnitRelation
				        .append(qty).append(",") //AA EQuantity
				        .append("").append(",")                                                                       //AB EUnitID
				        .append("0").append(",")                                                                       //AC EUnitRelation
				        .append("").append(",")                                                                       //AD FromNo
				        .append("0").append(",") //AE FromRow
				        .append("").append(",")                                                                       //AF CloseID
				        .append("False").append(",")                                                                     //AG HasCharacter
				        .append("False").append(",")                                                                       //AH HasPic
				        .append("").append(",")                                                                       //AI PurchaseNo
				        .append("0")                                                                                  //AJ PurchaseRow
				        .append("\n")
				        ;
						fw.write(lineLine.toString());
				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}
	
	/**
	 * 借入還出憑單
	 * @param tcompcode
	 * @param transDate
	 * @param fw
	 * @throws SQLException
	 * @throws IOException
	 */
	public void ProcessRetBorrow(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS RetBorrow DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList = new InteroutmfDao(db).findBorrowReturn("19",tcompcode,transDate);

    	fw.write("[SngProgID]ChiStock.RetBorrow@"+distList.size()+"\n");
    	fw.write("-1,ReturnNO,ReturnDate,CustomerID,AddrID,ZipCode,CustAddress,CurrID,ExchRate,ContactPerson,ContactPhone,SalesmanID,DeptID,Maker,Permitter,SumQty,SumAmt,Remark,UDef1,UDef2,PermitterID,MakerID,LinkManProf,SignBack,Detail,HasCheck,BorrowType,CustomerName,CustShortName,TranToFlag\n");
    	fw.write("-1,RowNo,SerNo,ProdID,ProdName,WareHouseID,HaveBatch,ItemRemark,ReturnNO,ReturnDate,TranType,Quantity,FromNO,FromRow,QtyRemain,Detail,CloseID,HasCheck,SUnitID,SQuantity,UnitRelation,EQuantity,EUnitID,EUnitRelation,HasCharacter,HasPic\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			int sumqty=0;
			StringBuffer line = new StringBuffer();
			String trandate = model.get("trandate")==null?"":model.get("trandate").toString();
			trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate, "yyyy-MM-dd"), "yyyyMMdd");
			String trannubr = model.get("trannubr")==null?"":model.get("trannubr").toString();

			String remarksx= model.get("remarksx")==null?"":model.get("remarksx").toString();
			sumqty=new InteroutdfDao(db).findSumqtyByNumber(trannubr);

			line.append("0").append(",")                                                                  //A -1
			    .append(trannubr).append(",")                                                             //B ReturnNO
			    .append(trandate).append(",")      														  //C ReturnDate
			    .append(model.get("custxxid")==null?"":model.get("custxxid").toString()).append(",")      //D CustomerID
			    .append("").append(",")                                                                  //E AddrID
			    .append("").append(",")                                                                   //F ZipCode
			    .append("").append(",")      //G CustAddress
			    .append("USD").append(",")      														  //H CurrID
			    .append("30").append(",")   	                                                          //I ExchRate
			    .append(model.get("contactx")==null?"":model.get("contactx").toString()).append(",")      //J ContactPerson
			    .append("").append(",")                                                                   //K ContactPhone
			    .append(model.get("regiuser")==null?"":model.get("regiuser").toString()).append(",")      //L SalesmanID
			    .append("A").append(",")      														      //M DeptID
			    .append(model.get("reginame")==null?"":model.get("reginame").toString()).append(",")      //N Maker
			    .append("").append(",")                                                                   //O Permitter			 
			    .append(sumqty).append(",")                                                               //P SumQty			    
			    .append("0").append(",")                                                                  //Q SumAmt
			    .append(CommonUtil.byteToHexString(remarksx)).append(",")                                 //R Remark			    
			    .append("").append(",")                                                                   //S UDef1
			    .append("").append(",")                                                                   //T UDef2
			    .append("").append(",")                                                                   //U PermitterID
			    .append(model.get("regiuser")==null?"":model.get("regiuser").toString()).append(",")      //V MakerID
			    .append("").append(",")                                                                   //W LinkManProf
			    .append("False").append(",")                                                              //X SignBack
			    .append("A").append(",")                                                                  //Y Detail
			    .append("True").append(",")                                                               //Z HasCheck
			    .append("2").append(",")                                                                  //AA BorrowType
			    .append(model.get("vndrname")==null?"":model.get("vndrname").toString()).append(",")      //AB CustomerName	
			    .append(model.get("vndrabbr")==null?"":model.get("vndrabbr").toString()).append(",")      //AC CustShortName
			    .append("False")                                                                          //AD TranToFlag				    
			    .append("\n");
			;
			fw.write(line.toString());
			List lineList =new InteroutdfDao(db).findByNumberRetBorrow(trannubr);
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();

				//總成本
				int qty=modelLine.get("quantity")==null?0:Integer.parseInt(modelLine.get("quantity").toString());
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                       //A -1
				        .append(rowno).append(",")                                                                     //B RowNo
				        .append(rowno).append(",")                                                                     //C SerNo
				        .append(modelLine.get("prodnubr")==null?"":modelLine.get("prodnubr").toString()).append(",")   //D ProdID
				        .append(modelLine.get("prodname")==null?"":modelLine.get("prodname").toString().toString().toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")   //E ProdName
				        .append(modelLine.get("locxcode")==null?"":modelLine.get("locxcode").toString()).append(",")   //F WareHouseID
					    .append("False").append(",")                                                                   //G HaveBatch
					    .append("").append(",")                                                                        //H ItemRemark
				        .append(trannubr).append(",")                                                                  //I ReturnNO
				        .append(trandate).append(",")                                                                  //J ReturnDate
				        .append("201").append(",")                                                                       //K TranType
				        .append(qty).append(",")                                                                       //L Quantity
				        .append(modelLine.get("poxxnubr")==null?"":modelLine.get("poxxnubr").toString()).append(",")   //M FromNO
				        .append(modelLine.get("seqtnubr")==null?"":modelLine.get("seqtnubr").toString()).append(",")   //N FromRow
				        .append(modelLine.get("remqty")==null?"0":modelLine.get("remqty").toString()).append(",")      //O QtyRemain
				        .append("").append(",")                                                                        //P Detail
				        .append("").append(",")                                                                        //Q CloseID
				        .append("True").append(",")                                                                    //R HasCheck
				        .append("").append(",")                                                                        //S SUnitID
				        .append(qty).append(",")                                                                       //T SQuantity
				        .append("0").append(",")                                                                       //U UnitRelation
				        .append(qty).append(",")                                                                       //V EQuantity
				        .append("").append(",")                                                                        //W EUnitID
				        .append("0").append(",")                                                                       //X EUnitRelation
				        .append("False").append(",")                                                                    //Y HasCharacter
				        .append("False")                                                                    //Z HasPic
				        .append("\n")
				        ;
						fw.write(lineLine.toString());				
				rowno++;
			}
		}
		//fw.close();
		db.close();
	}
	
	/**
	 * 所有收款記錄沖款檔
	 * @param tcompcode
	 * @param transDate
	 * @param fw
	 * @throws SQLException
	 * @throws IOException
	 */
	public void offSetBillaccounts(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS Billaccounts DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList = new ShipmatlmfDao(db).findBillaccountOffset(tcompcode,transDate);

		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			String offset = model.get("payed")==null?"":model.get("payed").toString();
			String localoffset = model.get("localpayed")==null?"":model.get("localpayed").toString();
			String trannubr = model.get("trannubr")==null?"":model.get("trannubr").toString();

			line.append(offset).append(";")                                                                  //沖款原幣
			    .append(localoffset).append(";")                                                             //沖款本幣
			    .append(trannubr).append(";")      														  //沖款單號				    
			    .append("\r\n");
			;
			fw.write(line.toString());
		}
		db.close();
	}
	
	/***
	 * 收款單傳輸
	 * @param tcompcode
	 * @param transDate
	 * @param fw
	 * @throws SQLException
	 * @throws IOException
	 */
	public void ProcessFundsRecvBill(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS Funds Receive Bill DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList = new FundbillDao(db).findFundbillMf(tcompcode,transDate,"G");
		List lineList =null;
		List lineList2 =null;
		List lineList3 =null;
		List lineList4 =null;
		List lineList7 =null;
		/*
		 * 0-fundbillmf(沖款主檔)+1-fundbilldf(沖款明細)+2-fundbilldf(自訂收入)+3-fundprepayused(取用預收)+7-取用預收延伸
		 * 0-fundbillmf(沖款主檔)+4-fundaddprepay(新增預收)--對應
		 */
		
    	fw.write("[SngProgID]ChiFunds.GatherBill@"+distList.size()+"\n");
    	fw.write("-1,FundBillID,FundBillDate,CustomerID,CurrID,ExchRate,VoucherNo,VoucherAbs,Cash,Visa,BillMoney,Maker,Permitter,SelfDefine1,SelfDefine2,Memo,SumOffset,PrepayUsed,OrigSelfMoney,OrigAddPrepay,OrigPrepayUsed,CashStyleID,VisaStyleID,OtherPayStyleID,OtherPay,IsAllCurrID,PermitterID,MakerID,AccBillList,IsShift,HasCheck,DiscountPer,YearCompressType,DeptFrom,DeptTo,ProjectFrom,ProjectTo,FromDate,ToDate,PrePayFromDate,PrePayToDate,UseDate,UsePrepay,PersonFrom,PersonTo,UsePerson,UseProject,AccMonthFromMonth,AccMonthToMonth,UseAccMonth\n");
    	fw.write("-1,FundBillNo,SerNo,SysID,DeptID,OriginFlag,OriginBillNo,PerfCount,Discount,OffSet,LocalOffSet,LocalDiscount,AccFlag,CurrID,ExchRate,Total,Commision,OffSetMoney,RowNo\n");
    	fw.write("-1,FundBillNo,SerNo,IsLoan,Money,AccSubjectID,Memo,DepartID,RowNo\n");
    	fw.write("-1,FundBillNo,RowNo,SysID,OriginNo,FromOrderNo,UseMoney,CurrID,CustomerID,FundBillDate,FromRowNo,FromDescription,DepartID,LocalUseMoney\n");
    	fw.write("-1,FundBillNo,RowNo,SysID,AddMoney,AddOffseted,NoChkAddOffSet,Description,CurrID,CustomerID,FundBillDate,OrderNO,LocalAddMoney,LocalAddOffseted,NoChkLocAddOffset\n");
    	fw.write("-1,FundBillNo,CheckNo,GetAmount,CheckFlag\n");
    	fw.write("-1,FundBillNo,ParentRowNo,RowNo,DepartID,Discount,LocalDiscount,Offset,LocalOffset,OffsetMoney\n");
    	fw.write("-1,FundBillNo,ParentRowNo,RowNo,DepartID,UseMoney,LocalOffset\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			String trandate1 = model.get("trandate")==null?"":model.get("trandate").toString();
			String trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMMdd");
			String exchrate=model.get("exchrate")==null?"1":model.get("exchrate").toString();
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))//加上若轉入的公司別是I-part 或Jetone HK 就把本幣跟原幣一樣設為USD
		    {
		    	exchrate="1";
		    }
		    //收款幣別
		    String currency=CommonUtil.getCurrency(tcompcode,model.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,model.get("currency").toString()));
			//備註
			String remark=model.get("remark")==null?"":model.get("remark").toString().replace(',', ' ').replace('\'', ' ').trim();
			//收款單的類別
			//int billtype=Integer.parseInt(model.get("billtype").toString());
		line.append("0").append(",")                                                              //0               
		    .append(model.get("trannubr")==null?"":model.get("trannubr").toString()).append(",")  //FundBillID       
		    .append(trandate).append(",")  //FundBillDate     
		    .append(model.get("cvid")==null?"":model.get("cvid").toString()).append(",")          //CustomerID       
		    .append(currency).append(",")//CurrID           
		    .append(exchrate).append(",")//ExchRate         
		    .append("").append(",")                                                              //VoucherNo        
		    .append(model.get("cvname")==null?"1":model.get("cvname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")    //VoucherAbs       
		    .append(model.get("payment1")==null?"0":model.get("payment1").toString()).append(",") //Cash             
		    .append(model.get("payment2")==null?"0":model.get("payment2").toString()).append(",") //Visa             1056.2
		    .append("0").append(",")                                                              //BillMoney        0
		    .append(model.get("maker")==null?"1":model.get("maker").toString()).append(",")      //Maker
		    .append("").append(",")                                                         //Permitter
		    .append("").append(",")                                                         //SelfDefine1
		    .append("").append(",")                                                          //SelfDefine2
		    .append(CommonUtil.byteToHexString(remark)).append(",")                         //Memo             
		    .append(model.get("sumpayment")==null?"0":model.get("sumpayment").toString()).append(",")//SumOffset      1075 /0 
		    .append(model.get("prepayused")==null?"0":model.get("prepayused").toString()).append(",")//PrepayUsed         0/0 
		    .append(model.get("selfmoney")==null?"0":model.get("selfmoney").toString()).append(",")//OrigSelfMoney   18.8/18.8    
		    .append(model.get("addprepay")==null?"0":model.get("addprepay").toString()).append(",")//OrigAddPrepay   1075/1075  
		    .append(model.get("prepayused")==null?"0":model.get("prepayused").toString()).append(",")//OrigPrepayUsed 0
		    .append(model.get("paytype1")==""?"0":model.get("paytype1").toString()).append(",")//CashStyleID         1
		    .append(model.get("paytype2")==""?"0":model.get("paytype2").toString()).append(",")//VisaStyleID         2
		    .append(model.get("paytype3")==""?"0":model.get("paytype3").toString()).append(",")//OtherPayStyleID     3
		    .append(model.get("payment3")==""?"0":model.get("payment3").toString()).append(",") //OtherPay       1075/0     
		    .append("True").append(",")	                                                          //IsAllCurrID         
		    .append("").append(",")                                                            //PermitterID         
		    .append(model.get("modifyuser")==null?"1":model.get("modifyuser").toString()).append(",") //MakerID             
		    .append("").append(",")                                                             //AccBillList         
		    .append("False").append(",")                                                             //IsShift             
		    .append("True").append(",")                                                            //HasCheck            
		    .append("0").append(",")                                                            //DiscountPer         
		    .append("0").append(",")                                                            //YearCompressType    
		    .append("A").append(",")                                                            //DeptFrom            
		    .append("A").append(",")                                                            //DeptTo              
		    .append("").append(",")                                                             //ProjectFrom         
		    .append("").append(",")                                                             //ProjectTo           
		    .append("0").append(",")                                                            //FromDate                
		    .append("0").append(",")                                                            //ToDate              
		    .append("0").append(",")                                                            //PrePayFromDate      
		    .append("0").append(",")                                                            //PrePayToDate        
		    .append("False").append(",")                                              //UseDate   
		    .append("False").append(",")                                              //UsePrepay 是否用預付要改         
		    .append("").append(",")                                                          //PersonFrom          
		    .append("").append(",")                                                         //PersonTo            
		    .append("False").append(",")                                                            //UsePerson           
		    .append("False").append(",")                                                            //UseProject          
		    .append("0").append(",")                                                            //AccMonthFromMonth   
		    .append("0").append(",")                                                            //AccMonthToMonth     
		    .append("False")                                                                         //UseAccMonth      
		    .append("\n")                                                                    
		    ;
			fw.write(line.toString());

			//以主檔單號查一般收款明細檔
			lineList =  new FundbillDao(db).findFundbillDf(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{					
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				//OffSetMoneyt沖款金額計算
				String offsetmoney=modelLine.get("billpayment")==null?"1":modelLine.get("billpayment").toString();//本單沖款金額

				String origflag="";
				if(modelLine.get("systype").toString().equals("10"))
					origflag="500";
				else if(modelLine.get("systype").toString().equals("00"))
					origflag="600";
				else if(modelLine.get("systype").toString().equals("A1"))
					origflag="698";
				
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        .append(modelLine.get("serial")==null?"":modelLine.get("serial").toString()).append(",")    //SerNo          
		        .append("2").append(",")                                                                  //SysID          
		        .append("A").append(",")                                                                  //DeptID         
		        .append(origflag).append(",")                                                                //OriginFlag     
		        .append(modelLine.get("originbillno")==null?"":modelLine.get("originbillno").toString()).append(",")//OriginBillNo   
		        .append("0").append(",")                                                               //PerfCount    
		        .append("0").append(",")                                                                //Discount       
		        .append(modelLine.get("payment")==null?"":modelLine.get("payment").toString()).append(",") //OffSet         
		        .append(modelLine.get("localpayment")==null?"":modelLine.get("localpayment").toString()).append(",")//LocalOffSet    
		        .append("0").append(",")                                                               //LocalDiscount  
		        .append("True").append(",")                                                               //AccFlag        
		        .append(modelLine.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,modelLine.get("currency").toString())).append(",") //CurrID         
		        .append(modelLine.get("exchrate")==null?"1":modelLine.get("exchrate").toString()).append(",") //ExchRate       
		        .append(modelLine.get("totalamount")==null?"1":modelLine.get("totalamount").toString()).append(",") //Total          
		        .append("0").append(",")                                                                //Commision    
		        .append(offsetmoney).append(",")                                                        //OffSetMoney     
		        .append(rowno)                                                               //RowNo                                                           
		        .append("\n")                   
		        ;
				fw.write(lineLine.toString());
				rowno++;
			}
			
			//以主檔單號查詢自行設定
			lineList4 =  new FundbillDao(db).findSelfset(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr4 = lineList4.iterator();
			int rowno2=1;

			while(lineItr4.hasNext())
			{					
				BasicDynaBean modelLine = (BasicDynaBean) lineItr4.next();
				String isloan="";
				if(modelLine.get("isloan").equals("1"))
					isloan="True";
				else
					isloan="False";
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("2").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        .append(modelLine.get("serial")==null?"":modelLine.get("serial").toString()).append(",")    //SerNo          
		        .append(isloan).append(",")   //IsLoan 1-借true,0-貸 false         
		        .append(modelLine.get("amount")==null?"":modelLine.get("amount").toString()).append(",")   //Money         
		        .append(modelLine.get("accsubjectid")==null?"":modelLine.get("accsubjectid").toString()).append(",")   //AccSubjectID   
		        .append(modelLine.get("memo")==null?"":modelLine.get("memo").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")//Memo   
		        .append("A").append(",")                                                               //DepartID    
		        .append(rowno2)                                                               //RowNo                                                     
		        .append("\n")                   
		        ;
				fw.write(lineLine.toString());
				rowno2++;
			}
			
			//以主單號查取用預收->寫出line3
			lineList2 =  new FundbillDao(db).findPrepayuse(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr2 = lineList2.iterator();
			int rowno3 = 1;
			while(lineItr2.hasNext())
			{
				BasicDynaBean modelLine = (BasicDynaBean) lineItr2.next();
				
				String trandateLine = modelLine.get("trandate")==null?"":modelLine.get("trandate").toString();
				trandateLine = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandateLine, "yyyy-MM-dd"), "yyyyMMdd");
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("3").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        .append(rowno3).append(",")     //RowNo
		        //.append(modelLine.get("fromserial")==null?"":modelLine.get("fromserial").toString()).append(",")    //RowNo   -->先改成fromserial 2016/2/19       
		        .append(modelLine.get("sysid")==null?"":modelLine.get("sysid").toString()).append(",")     //SysID          
		        .append(modelLine.get("origtrannubr")==null?"":modelLine.get("origtrannubr").toString()).append(",")     //OriginNo         
		        .append("").append(",")//不傳訂單號碼
		        //.append(modelLine.get("fromordrnubr")==null?"":modelLine.get("fromordrnubr").toString()).append(",")     //FromOrderNo     
		        .append(modelLine.get("usedamount")==null?"":modelLine.get("usedamount").toString()).append(",")//UseMoney   
		        .append(modelLine.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,modelLine.get("currency").toString())).append(",")    //CurrID    
		        .append(modelLine.get("cvid")==null?"":modelLine.get("cvid").toString()).append(",")   //CustomerID       
		        .append(trandateLine).append(",") //FundBillDate         
		        .append(modelLine.get("fromserial")==null?"":modelLine.get("fromserial").toString()).append(",")//FromRowNo    
		        .append(modelLine.get("fromdescription")==null?"":modelLine.get("fromdescription").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")  //FromDescription  
		        .append("A").append(",")  //DepartID        
		        .append(modelLine.get("localusedamount")==null?"":modelLine.get("localusedamount").toString()) //LocalUseMoney                                            
		        .append("\n")
		        ;
				fw.write(lineLine.toString());
				rowno3++;
			}

			//以主單號查取用預收->寫出line7
			lineList7 =  new FundbillDao(db).findPrepayuse(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr7 = lineList7.iterator();
			int rowno7 = 1;
			while(lineItr7.hasNext())
			{
				BasicDynaBean modelLine = (BasicDynaBean) lineItr7.next();
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("7").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        //.append(modelLine.get("fromserial")==null?"":modelLine.get("fromserial").toString()).append(",")    //ParentRowNo          
		        .append(rowno7).append(",")     //ParentRowNo 
		        .append(rowno7).append(",")     //RowNo          
		        .append("A").append(",")     //DepartID         
		        .append(modelLine.get("usedamount")==null?"":modelLine.get("usedamount").toString()).append(",")     //UseMoney     
		        .append(modelLine.get("localusedamount")==null?"":modelLine.get("localusedamount").toString())//LocalOffset                                           
		        .append("\n")
		        ;
				fw.write(lineLine.toString());
				rowno7++;
			}
			//以主檔單號查是否為新增預收單
			lineList3 =  new FundbillDao(db).findAddPrepay(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr3 = lineList3.iterator();
			while(lineItr3.hasNext())
			{				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr3.next();
				String trandateLine = modelLine.get("trandate")==null?"":modelLine.get("trandate").toString();
				trandateLine = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandateLine, "yyyy-MM-dd"), "yyyyMMdd");
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("4").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        .append(modelLine.get("serial")==null?"":modelLine.get("serial").toString()).append(",")    //RowNo          
		        .append(modelLine.get("sysid")==null?"":modelLine.get("sysid").toString()).append(",") //SysID          
		        .append(modelLine.get("addamount")==null?"":modelLine.get("addamount").toString()).append(",") //AddMoney         
		        .append(modelLine.get("usedamount")==null?"":modelLine.get("usedamount").toString()).append(",") //AddOffseted     
		        .append("0").append(",")//NoChkAddOffSet  
		        .append(modelLine.get("description")==null?"":modelLine.get("description").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",") //Description    
		        .append(modelLine.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,modelLine.get("currency").toString())).append(",") //CurrID     
		        .append(modelLine.get("cvid")==null?"":modelLine.get("cvid").toString()).append(",") //CustomerID         
		        .append(trandateLine).append(",")//FundBillDate
		        .append("").append(",")    
		        //.append(modelLine.get("ordrnubr")==null?"":modelLine.get("ordrnubr").toString()).append(",") //OrderNO
		        .append(modelLine.get("localaddamount")==null?"":modelLine.get("localaddamount").toString()).append(",") //LocalAddMoney 
		        .append(modelLine.get("localusedamount")==null?"":modelLine.get("localusedamount").toString()).append(",") //LocalAddOffseted       
		        .append("0") //NoChkLocAddOffset                                            
		        .append("\n")
		        ;
				fw.write(lineLine.toString());
			}
		}
		db.close();
	}//end-function
	
	/***
	 * 付款單傳輸
	 * @param tcompcode
	 * @param transDate
	 * @param fw
	 * @throws SQLException
	 * @throws IOException
	 */
	public void ProcessFundsPaymentBill(String tcompcode,String transDate,FileWriter fw,String db_TYPE) throws SQLException, IOException
	{
		System.out.println("=====START TO PROCESS Funds Payment Bill DATA======");
		DbUtil dbUtil     = null;	
		MisDataBaseImp db = null;
		dbUtil = new DbUtil(db_TYPE, DB_NAME);
		db     = (MisDataBaseImp) dbUtil.getDataBase();
		List distList = new FundbillDao(db).findFundbillMf(tcompcode,transDate,"P");
		List lineList =null;
		List lineList2 =null;
		List lineList3 =null;
		List lineList4 =null;
		List lineList7 =null;
		/*
		 * 0-fundbillmf(沖款主檔)+1-fundbilldf(沖款明細)+2-fundbilldf(自訂收入)+3-fundprepayused(取用預收)+7-取用預收延伸
		 * 0-fundbillmf(沖款主檔)+4-fundaddprepay(新增預收)--對應
		 */
		
    	fw.write("[SngProgID]ChiFunds.PayBill@"+distList.size()+"\n");
    	fw.write("-1,FundBillID,FundBillDate,CustomerID,CurrID,ExchRate,VoucherNo,VoucherAbs,Cash,Visa,BillMoney,Maker,Permitter,SelfDefine1,SelfDefine2,Memo,SumOffset,PrepayUsed,OrigSelfMoney,OrigAddPrepay,OrigPrepayUsed,CashStyleID,VisaStyleID,OtherPayStyleID,OtherPay,IsAllCurrID,PermitterID,MakerID,AccBillList,IsShift,HasCheck,DiscountPer,DeptFrom,DeptTo,ProjectFrom,ProjectTo,FromDate,ToDate,PrePayFromDate,PrePayToDate,UseDate,UsePrepay,PersonFrom,PersonTo,UsePerson,UseProject,AccMonthFromMonth,AccMonthToMonth,UseAccMonth\n");
    	fw.write("-1,FundBillNo,SerNo,SysID,DeptID,OriginFlag,OriginBillNo,PerfCount,Discount,OffSet,LocalOffSet,LocalDiscount,AccFlag,CurrID,ExchRate,Total,Commision,OffSetMoney,RowNo\n");
    	fw.write("-1,FundBillNo,SerNo,IsLoan,Money,AccSubjectID,Memo,DepartID,RowNo\n");
    	fw.write("-1,FundBillNo,RowNo,SysID,OriginNo,FromOrderNo,UseMoney,CurrID,CustomerID,FundBillDate,FromRowNo,FromDescription,DepartID,LocalUseMoney\n");
    	fw.write("-1,FundBillNo,RowNo,SysID,AddMoney,AddOffseted,NoChkAddOffSet,Description,CurrID,CustomerID,FundBillDate,OrderNO,LocalAddMoney,LocalAddOffseted,NoChkLocAddOffset\n");
    	fw.write("-1,FundBillNo,CheckNo,GetAmount,CheckFlag\n");
    	fw.write("-1,FundBillNo,ParentRowNo,RowNo,DepartID,Discount,LocalDiscount,Offset,LocalOffset,OffsetMoney\n");
    	fw.write("-1,FundBillNo,ParentRowNo,RowNo,DepartID,UseMoney,LocalOffset\n");
		Iterator distItr = distList.iterator();
		while(distItr.hasNext())
		{
			BasicDynaBean model = (BasicDynaBean) distItr.next();
			StringBuffer line = new StringBuffer();
			String trandate1 = model.get("trandate")==null?"":model.get("trandate").toString();
			String trandate = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandate1, "yyyy-MM-dd"), "yyyyMMdd");
			String exchrate=model.get("exchrate")==null?"1":model.get("exchrate").toString();
		    if(tcompcode.equals("I")||tcompcode.equals("H")||tcompcode.equals("M"))//加上若轉入的公司別是I-part 或Jetone HK 就把本幣跟原幣一樣設為USD
		    {
		    	exchrate="1";
		    }
		    //付款幣別
		    String currency=CommonUtil.getCurrency(tcompcode,model.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,model.get("currency").toString()));
			//備註
			String remark=model.get("remark")==null?"":model.get("remark").toString().replace(',', ' ').replace('\'', ' ').trim();
			//付款單的類別
			//int billtype=Integer.parseInt(model.get("billtype").toString());
		line.append("0").append(",")                                                              //0               
		    .append(model.get("trannubr")==null?"":model.get("trannubr").toString()).append(",")  //FundBillID       
		    .append(trandate).append(",")  //FundBillDate     
		    .append(model.get("cvid")==null?"":model.get("cvid").toString()).append(",")          //CustomerID       
		    .append(currency).append(",")//CurrID           
		    .append(exchrate).append(",")//ExchRate         
		    .append("").append(",")                                                              //VoucherNo        
		    .append(model.get("cvname")==null?"1":model.get("cvname").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")    //VoucherAbs       
		    .append(model.get("payment1")==null?"0":model.get("payment1").toString()).append(",") //Cash             
		    .append(model.get("payment2")==null?"0":model.get("payment2").toString()).append(",") //Visa             1056.2
		    .append("0").append(",")                                                              //BillMoney        0
		    .append(model.get("maker")==null?"1":model.get("maker").toString()).append(",")      //Maker
		    .append("").append(",")                                                         //Permitter
		    .append("").append(",")                                                         //SelfDefine1
		    .append("").append(",")                                                          //SelfDefine2
		    .append(CommonUtil.byteToHexString(remark)).append(",")                         //Memo             
		    .append(model.get("sumpayment")==null?"0":model.get("sumpayment").toString()).append(",")//SumOffset      1075 /0 
		    .append(model.get("prepayused")==null?"0":model.get("prepayused").toString()).append(",")//PrepayUsed         0/0 
		    .append(model.get("selfmoney")==null?"0":model.get("selfmoney").toString()).append(",")//OrigSelfMoney   18.8/18.8    
		    .append(model.get("addprepay")==null?"0":model.get("addprepay").toString()).append(",")//OrigAddPrepay   1075/1075  
		    .append(model.get("prepayused")==null?"0":model.get("prepayused").toString()).append(",")//OrigPrepayUsed 0
		    .append(model.get("paytype1")==""?"0":model.get("paytype1").toString()).append(",")//CashStyleID         1
		    .append(model.get("paytype2")==""?"0":model.get("paytype2").toString()).append(",")//VisaStyleID         2
		    .append(model.get("paytype3")==""?"0":model.get("paytype3").toString()).append(",")//OtherPayStyleID     3
		    .append(model.get("payment3")==""?"0":model.get("payment3").toString()).append(",") //OtherPay       1075/0     
		    .append("True").append(",")	                                                          //IsAllCurrID         
		    .append("").append(",")                                                            //PermitterID         
		    .append(model.get("modifyuser")==null?"1":model.get("modifyuser").toString()).append(",") //MakerID             
		    .append("").append(",")                                                             //AccBillList         
		    .append("False").append(",")                                                             //IsShift             
		    .append("True").append(",")                                                            //HasCheck            
		    .append("0").append(",")                                                            //DiscountPer         
		    //.append("0").append(",")                                                            //YearCompressType    
		    .append("A").append(",")                                                            //DeptFrom            
		    .append("A").append(",")                                                            //DeptTo              
		    .append("").append(",")                                                             //ProjectFrom         
		    .append("").append(",")                                                             //ProjectTo           
		    .append("0").append(",")                                                            //FromDate                
		    .append("0").append(",")                                                            //ToDate              
		    .append("0").append(",")                                                            //PrePayFromDate      
		    .append("0").append(",")                                                            //PrePayToDate        
		    .append("False").append(",")                                              //UseDate   
		    .append("False").append(",")                                              //UsePrepay 是否用預付要改         
		    .append("").append(",")                                                          //PersonFrom          
		    .append("").append(",")                                                         //PersonTo            
		    .append("False").append(",")                                                            //UsePerson           
		    .append("False").append(",")                                                            //UseProject          
		    .append("0").append(",")                                                            //AccMonthFromMonth   
		    .append("0").append(",")                                                            //AccMonthToMonth     
		    .append("False")                                                                         //UseAccMonth      
		    .append("\n")                                                                    
		    ;
			fw.write(line.toString());

			//以主檔單號查一般收款明細檔
			lineList =  new FundbillDao(db).findFundbillDf(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr = lineList.iterator();
			int rowno = 1;
			while(lineItr.hasNext())
			{					
				BasicDynaBean modelLine = (BasicDynaBean) lineItr.next();
				//OffSetMoneyt沖款金額計算
				String offsetmoney=modelLine.get("billpayment")==null?"1":modelLine.get("billpayment").toString();//本單沖款金額

				String origflag="";
				if(modelLine.get("systype").toString().equals("08"))
					origflag="100";
				else if(modelLine.get("systype").toString().equals("18"))
					origflag="200";
				else if(modelLine.get("systype").toString().equals("A8"))
					origflag="298";
				
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("1").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        .append(modelLine.get("serial")==null?"":modelLine.get("serial").toString()).append(",")    //SerNo          
		        .append("2").append(",")                                                                  //SysID          
		        .append("A").append(",")                                                                  //DeptID         
		        .append(origflag).append(",")                                                                //OriginFlag     
		        .append(modelLine.get("originbillno")==null?"":modelLine.get("originbillno").toString()).append(",")//OriginBillNo   
		        .append("0").append(",")                                                               //PerfCount    
		        .append("0").append(",")                                                                //Discount       
		        .append(modelLine.get("payment")==null?"":modelLine.get("payment").toString()).append(",") //OffSet         
		        .append(modelLine.get("localpayment")==null?"":modelLine.get("localpayment").toString()).append(",")//LocalOffSet    
		        .append("0").append(",")                                                               //LocalDiscount  
		        .append("True").append(",")                                                               //AccFlag        
		        .append(modelLine.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,modelLine.get("currency").toString())).append(",") //CurrID         
		        .append(modelLine.get("exchrate")==null?"1":modelLine.get("exchrate").toString()).append(",") //ExchRate       
		        .append(modelLine.get("totalamount")==null?"1":modelLine.get("totalamount").toString()).append(",") //Total          
		        .append("0").append(",")                                                                //Commision    
		        .append(offsetmoney).append(",")                                                        //OffSetMoney     
		        .append(rowno)                                                               //RowNo                                                           
		        .append("\n")                   
		        ;
				fw.write(lineLine.toString());
				rowno++;
			}
			
			//以主檔單號查詢自行設定
			lineList4 =  new FundbillDao(db).findSelfset(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr4 = lineList4.iterator();
			int rowno2=1;

			while(lineItr4.hasNext())
			{					
				BasicDynaBean modelLine = (BasicDynaBean) lineItr4.next();
				String isloan="";
				if(modelLine.get("isloan").equals("1"))
					isloan="True";
				else
					isloan="False";
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("2").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        .append(modelLine.get("serial")==null?"":modelLine.get("serial").toString()).append(",")    //SerNo          
		        .append(isloan).append(",")   //IsLoan 1-借true,0-貸 false         
		        .append(modelLine.get("amount")==null?"":modelLine.get("amount").toString()).append(",")   //Money         
		        .append(modelLine.get("accsubjectid")==null?"":modelLine.get("accsubjectid").toString()).append(",")   //AccSubjectID   
		        .append(modelLine.get("memo")==null?"":modelLine.get("memo").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")//Memo   
		        .append("A").append(",")                                                               //DepartID    
		        .append(rowno2)                                                               //RowNo                                                     
		        .append("\n")                   
		        ;
				fw.write(lineLine.toString());
				rowno2++;
			}
			
			//以主單號查取用預付->寫出line3
			lineList2 =  new FundbillDao(db).findPrepayuse(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr2 = lineList2.iterator();
			int rowno3 = 1;
			while(lineItr2.hasNext())
			{
				BasicDynaBean modelLine = (BasicDynaBean) lineItr2.next();
				
				String trandateLine = modelLine.get("trandate")==null?"":modelLine.get("trandate").toString();
				trandateLine = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandateLine, "yyyy-MM-dd"), "yyyyMMdd");
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("3").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        .append(rowno3).append(",")     //RowNo
		        //.append(modelLine.get("serial")==null?"":modelLine.get("serial").toString()).append(",")    //RowNo          
		        .append(modelLine.get("sysid")==null?"":modelLine.get("sysid").toString()).append(",")     //SysID          
		        .append(modelLine.get("origtrannubr")==null?"":modelLine.get("origtrannubr").toString()).append(",")     //OriginNo         
		        .append("").append(",")//不傳訂單號碼
		        //.append(modelLine.get("fromordrnubr")==null?"":modelLine.get("fromordrnubr").toString()).append(",")     //FromOrderNo     
		        .append(modelLine.get("usedamount")==null?"":modelLine.get("usedamount").toString()).append(",")//UseMoney   
		        .append(modelLine.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,modelLine.get("currency").toString())).append(",")    //CurrID    
		        .append(modelLine.get("cvid")==null?"":modelLine.get("cvid").toString()).append(",")   //CustomerID       
		        .append(trandateLine).append(",") //FundBillDate         
		        .append(modelLine.get("fromserial")==null?"":modelLine.get("fromserial").toString()).append(",")//FromRowNo    
		        .append(modelLine.get("fromdescription")==null?"":modelLine.get("fromdescription").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",")  //FromDescription  
		        .append("A").append(",")  //DepartID        
		        .append(modelLine.get("localusedamount")==null?"":modelLine.get("localusedamount").toString()) //LocalUseMoney                                            
		        .append("\n")
		        ;
				fw.write(lineLine.toString());
				rowno3++;
			}

			//以主單號查取用預付->寫出line7
			lineList7 =  new FundbillDao(db).findPrepayuse(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr7 = lineList7.iterator();
			int rowno7 = 1;
			while(lineItr7.hasNext())
			{
				BasicDynaBean modelLine = (BasicDynaBean) lineItr7.next();
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("7").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        //.append(modelLine.get("fromserial")==null?"":modelLine.get("fromserial").toString()).append(",")    //ParentRowNo          
		        .append(rowno7).append(",")     //RowNo
		        .append(rowno7).append(",")     //RowNo          
		        .append("A").append(",")     //DepartID         
		        .append(modelLine.get("usedamount")==null?"":modelLine.get("usedamount").toString()).append(",")     //UseMoney     
		        .append(modelLine.get("localusedamount")==null?"":modelLine.get("localusedamount").toString())//LocalOffset                                           
		        .append("\n")
		        ;
				fw.write(lineLine.toString());
				rowno7++;
			}
			//以主檔單號查是否為新增預收單
			lineList3 =  new FundbillDao(db).findAddPrepay(model.get("trannubr")==null?"":model.get("trannubr").toString());
			Iterator lineItr3 = lineList3.iterator();
			while(lineItr3.hasNext())
			{				
				BasicDynaBean modelLine = (BasicDynaBean) lineItr3.next();
				String trandateLine = modelLine.get("trandate")==null?"":modelLine.get("trandate").toString();
				trandateLine = DateTimeUtil.DateTimeFormatToString(DateTimeUtil.StringParseToDate(trandateLine, "yyyy-MM-dd"), "yyyyMMdd");
				StringBuffer lineLine = new StringBuffer();
				lineLine.append("4").append(",")                                                                    //-1             
		        .append(modelLine.get("trannubr")==null?"":modelLine.get("trannubr").toString()).append(",")//FundBillNo     
		        .append(modelLine.get("serial")==null?"":modelLine.get("serial").toString()).append(",")    //RowNo          
		        .append(modelLine.get("sysid")==null?"":modelLine.get("sysid").toString()).append(",") //SysID          
		        .append(modelLine.get("addamount")==null?"":modelLine.get("addamount").toString()).append(",") //AddMoney         
		        .append(modelLine.get("usedamount")==null?"":modelLine.get("usedamount").toString()).append(",") //AddOffseted     
		        .append("0").append(",")//NoChkAddOffSet  
		        .append(modelLine.get("description")==null?"":modelLine.get("description").toString().replace(',', ' ').replace('\'', ' ').trim()).append(",") //Description    
		        .append(modelLine.get("currency")==null?"":CommonUtil.getCurrency(tcompcode,modelLine.get("currency").toString())).append(",") //CurrID     
		        .append(modelLine.get("cvid")==null?"":modelLine.get("cvid").toString()).append(",") //CustomerID         
		        .append(trandateLine).append(",")//FundBillDate    
		        .append("").append(",")
		        //.append(modelLine.get("ordrnubr")==null?"":modelLine.get("ordrnubr").toString()).append(",") //OrderNO
		        .append(modelLine.get("localaddamount")==null?"":modelLine.get("localaddamount").toString()).append(",") //LocalAddMoney 
		        .append(modelLine.get("localusedamount")==null?"":modelLine.get("localusedamount").toString()).append(",") //LocalAddOffseted       
		        .append("0") //NoChkLocAddOffset                                            
		        .append("\n")
		        ;
				fw.write(lineLine.toString());
			}
		}
		db.close();
	}//end-function
}//end-class