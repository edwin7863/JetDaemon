/*
########################################################################
# SYSTEM  ID:BwsDaemon                                                                                                                                                  #
# PROGRAM ID: DbUtil.java                                                                                                                                               #
# AUTHOR    : Endra Lee                                                                                                                                                     #
########################################################################
# MODIFICATION LOG:                                                                                                                                                       #
#   2008/04/10 Endra Lee Modify  getOracleConnection() to excute     alter session set NLS_LANGUAGE=AMERICAN   # 
#   2008/04/11 Endra Lee Modify  getOracleConnection Information from XML                                                                                                                                                                           #
########################################################################
*/

package app.gian;
import java.io.*;
// 載入JDBC類別
import java.sql.*;
// 載入JAXP類別
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
// 載入DOM類別
import org.w3c.dom.*;
import org.xml.sax.SAXException;


// 載入資料轉換所需的類別

import app.gian.db.DataBase;
import app.gian.db.MisDataBaseImp;



/**
 * @author Jason
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbUtil
{
	private String dbXmlFile;
	/**
	 * 資料庫名稱
	 */
	private String dbName;
	
	/**
	 * Constructor
	 */
	public DbUtil(String dbType, String dbName)
	{
		if (DbType.BIG5_FORMAL.equals(dbType))
		{
			//dbXmlFile = "../refxml/big5_formal_db.xml";
			dbXmlFile = "../JetDaemon/refxml/big5_formal_db.xml";
			//dbXmlFile = "C:/wsad/workspace/ccsBatch/refxml/big5_formal_db.xml";
			
		}
		else if (DbType.BIG5_FORMAL_INNOTRON.equals(dbType))
		{
			dbXmlFile = "../JetDaemon/refxml/big5_formal_dbInno.xml";
		}
		else if (DbType.BIG5_FORMAL_JETCHINA.equals(dbType))
		{
			dbXmlFile = "../JetDaemon/refxml/big5_formal_dbJetchina.xml";
		}
		else if (DbType.BIG5_TEST.equals(dbType))
		{
			
			dbXmlFile = "../JetDaemon/refxml/big5_test_db.xml";
			//dbXmlFile = "C:/wsad/workspace/ccsBatch/refxml/big5_formal_db.xml";
		}
		else if (DbType.BIG5_TEST_INNOTRON.equals(dbType))
		{
			
			dbXmlFile = "../JetDaemon/refxml/big5_test_dbInno.xml";
			//dbXmlFile = "C:/wsad/workspace/ccsBatch/refxml/big5_formal_db.xml";
		}
		else if (DbType.BIG5_TEST_JETCHINA.equals(dbType))
		{
			
			dbXmlFile = "../JetDaemon/refxml/big5_test_dbJetchina.xml";
			//dbXmlFile = "C:/wsad/workspace/ccsBatch/refxml/big5_formal_db.xml";
		}
		//System.out.println("new DbUtil dbXmlFile = " + dbXmlFile);
		this.dbName = dbName;
		//System.out.println("new DbUtil dbName = " + dbName);
	}
	
	public Connection getConnection()
	{
		Connection conn = null;
		try
		{
			Document dataDoc = B2Bi.newDocument(dbXmlFile);
			Element docRoot = dataDoc.getDocumentElement();
			Element dbComm = (Element) dataDoc.getElementsByTagName("dbComm").item(0);
			String driverName = dbComm.getElementsByTagName("driver").item(0).getFirstChild().getNodeValue();
			
			String url = dbComm.getElementsByTagName("url").item(0).getFirstChild().getNodeValue();
			String charset = dbComm.getElementsByTagName("charset").item(0).getFirstChild().getNodeValue();
			String pwd = dbComm.getElementsByTagName("pwd").item(0).getFirstChild().getNodeValue();
			
			NodeList dbList = dataDoc.getElementsByTagName("database");
			Element database = null;
			for (int i = 0 ; i < dbList.getLength() ; i++)
			{
				database = (Element)dbList.item(i);
				String attName = database.getAttribute("name");
				
				if (this.dbName.equals(attName))
					break;
			}
			
			String host = database.getElementsByTagName("host").item(0).getFirstChild().getNodeValue();
			String port = database.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
			String databaseName = database.getElementsByTagName("databaseName").item(0).getFirstChild().getNodeValue();
			String serverName = database.getElementsByTagName("serverName").item(0).getFirstChild().getNodeValue();
			
			StringBuffer connectURL = new StringBuffer();
			//jdbc:informix-sqli://sdb1:777/sdmis:informixserver=sdb1svr
			connectURL.append(url)
				.append(host)
				.append(":")
				.append(port)
				.append("/")
				.append(databaseName)
				.append(":informixserver=")
				.append(serverName)
				.append(charset)
				;
			//System.out.print("connectURL = " + connectURL);
			String user = database.getElementsByTagName("user").item(0).getFirstChild().getNodeValue();
			String password = database.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
			password += pwd;
			//System.out.println(", user = " + user + ", password = " + password);
			try
			{
				System.out.println("get " + this.dbName + " connection begin...");
				Class.forName(driverName);
				conn = DriverManager.getConnection(connectURL.toString(), user, password);
				System.out.println("get " + this.dbName + " connection success...");
			}
			catch (ClassNotFoundException e)
			{
				System.out.print("Database connection no Class = "+e.getMessage());
				System.out.println(e.toString());
			}
			catch (SQLException e)
			{
				System.out.print("Create Database connection error = "+e.getErrorCode()+e.getMessage());
				System.out.println(e.toString());
			}
		}
		catch (Exception e)
		{
			System.out.print("Parsing xml error = "+e.getMessage());
			System.out.println(e.toString());
		}
		return conn;
	}

	public DataBase getDataBase()
	{
		return getDataBase(true);
	}
	
	public static Connection getOracleConnection(String type) throws Exception
	{		
		Connection conn = null;
		try
		{			
			String filePath = "";
			
			//if(DbType.ORACLE_FORMAL.equals(type))
			//	filePath = "../BwsDaemon/refxml/big5_formal_db.xml";
			//else
			//	filePath = "../BwsDaemon/refxml/big5_test_db.xml";
			if(DbType.ORACLE_FORMAL.equals(type))
				filePath = "../JetDaemon/refxml/big5_formal_db.xml";
			else
				filePath = "../JetDaemon/refxml/big5_test_db.xml";
				
			Document dataDoc = B2Bi.newDocument(filePath);
			NodeList dbList = dataDoc.getElementsByTagName("database");
			Element database = null;
			for (int i = 0 ; i < dbList.getLength() ; i++)
			{
				database = (Element)dbList.item(i);
				String attName = database.getAttribute("name");
				if ("ORACLE".equals(attName))
					break;
			}
			
			String oracleHOST = database.getElementsByTagName("host").item(0).getFirstChild().getNodeValue();
			String oraclePORT = database.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
			String oracleSID = database.getElementsByTagName("sid").item(0).getFirstChild().getNodeValue();
			String oracleUser = database.getElementsByTagName("user").item(0).getFirstChild().getNodeValue();
			String oraclePass = database.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
			//String oracleHOST = "10.21.1.241";
			//String oraclePORT = "1525";
			//String oracleSID  = "TEST";
			//String oracleURL="jdbc:oracle:thin:@10.21.1.241:1524:DEV";
   			//String oracleUser="YOSUNBWS"; 
   			//String oraclePass="YOSUNBWS";
			StringBuffer oracleURL = new StringBuffer();
			oracleURL.append("jdbc:oracle:thin:@").append(oracleHOST).append(":").append(oraclePORT).append(":").append(oracleSID);					
			try 
			{
		   		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				conn = DriverManager.getConnection(oracleURL.toString(),oracleUser,oraclePass);
				Statement s  = conn.createStatement();
				s.execute("alter session set NLS_LANGUAGE=AMERICAN");
				s.close();
   			}
   			catch (SQLException e)
   			{
		   		System.out.println("ORACLE ERP Driver Error:"+e.toString());
		   		throw e;
   			}   								
		}
		catch (Exception e)
		{
			System.out.println("Parsing xml error = "+e.getMessage());
			System.out.println(e.toString());
			throw e;
		}
		return conn;
	}
	
	public static Connection getPortalConnection(String type) throws Exception
	{		
		Connection conn = null;
		try
		{			
			String filePath = "";
		
			if(DbType.PORTAL_FORMAL.equals(type))
				filePath = "../BwsDaemon/refxml/big5_formal_db.xml";
			else
				filePath = "../BwsDaemon/refxml/big5_test_db.xml";
			
			Document dataDoc = B2Bi.newDocument(filePath);
			NodeList dbList = dataDoc.getElementsByTagName("database");
			Element database = null;
			for (int i = 0 ; i < dbList.getLength() ; i++)
			{
				database = (Element)dbList.item(i);
				String attName = database.getAttribute("name");
				if ("PORTAL".equals(attName))
					break;
			}
		
			String oracleHOST = database.getElementsByTagName("host").item(0).getFirstChild().getNodeValue();
			String oraclePORT = database.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
			String oracleSID = database.getElementsByTagName("sid").item(0).getFirstChild().getNodeValue();
			String oracleUser = database.getElementsByTagName("user").item(0).getFirstChild().getNodeValue();
			String oraclePass = database.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
			//String oracleHOST = "10.21.1.241";
			//String oraclePORT = "1525";
			//String oracleSID  = "TEST";
			//String oracleURL="jdbc:oracle:thin:@10.21.1.241:1524:DEV";
			//String oracleUser="YOSUNBWS"; 
			//String oraclePass="YOSUNBWS";
			StringBuffer oracleURL = new StringBuffer();
			oracleURL.append("jdbc:oracle:thin:@").append(oracleHOST).append(":").append(oraclePORT).append(":").append(oracleSID);					
			try 
			{
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				conn = DriverManager.getConnection(oracleURL.toString(),oracleUser,oraclePass);
				Statement s  = conn.createStatement();
				s.execute("alter session set NLS_LANGUAGE=AMERICAN");
				s.close();
			}
			catch (SQLException e)
			{
				System.out.println("PORTAL Driver Error:"+e.toString());
				throw e;
			}   								
		}
		catch (Exception e)
		{
			System.out.println("Parsing xml error = "+e.getMessage());
			System.out.println(e.toString());
			throw e;
		}
		return conn;
	}
	
	public DataBase getDataBase(boolean flag)
	{
		MisDataBaseImp db = null;
		try
		{
			Document dataDoc = B2Bi.newDocument(dbXmlFile);
			Element docRoot = dataDoc.getDocumentElement();
			Element dbComm = (Element) dataDoc.getElementsByTagName("dbComm").item(0);
			String driverName = dbComm.getElementsByTagName("driver").item(0).getFirstChild().getNodeValue();
			String url = dbComm.getElementsByTagName("url").item(0).getFirstChild().getNodeValue();
			//String charset = dbComm.getElementsByTagName("charset").item(0).getFirstChild().getNodeValue();
			//String pwd = dbComm.getElementsByTagName("pwd").item(0).getFirstChild() == null ? "" : dbComm.getElementsByTagName("pwd").item(0).getFirstChild().getNodeValue();
			NodeList dbList = dataDoc.getElementsByTagName("database");
			Element database = null;
			for (int i = 0 ; i < dbList.getLength() ; i++)
			{
				database = (Element)dbList.item(i);
				String attName = database.getAttribute("name");
			
				if (this.dbName.equals(attName))
					break;
			}
			
			String host = database.getElementsByTagName("host").item(0).getFirstChild().getNodeValue();
			String port = database.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
			String databaseName = database.getElementsByTagName("databaseName").item(0).getFirstChild().getNodeValue();
			//String serverName = database.getElementsByTagName("serverName").item(0).getFirstChild().getNodeValue();
			String user = database.getElementsByTagName("user").item(0).getFirstChild().getNodeValue();
			String password = database.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
			try
			{
				db =   new MisDataBaseImp(host, port, databaseName, user, password, flag);
			}
			catch (Exception e)
			{
				System.out.println("Create Database error = " + e.toString());
			}
		}
		catch (Exception e)
		{
			System.out.println("Parsing xml error = "+e.getMessage());
			System.out.println(e.toString());
		}
		return db;
	}

	public DataBase getDataBaseInnotron(boolean flag)
	{
		MisDataBaseImp db = null;
		try
		{
			Document dataDoc = B2Bi.newDocument(dbXmlFile);
			Element docRoot = dataDoc.getDocumentElement();
			Element dbComm = (Element) dataDoc.getElementsByTagName("dbComm").item(0);
			String driverName = dbComm.getElementsByTagName("driver").item(0).getFirstChild().getNodeValue();
			String url = dbComm.getElementsByTagName("url").item(0).getFirstChild().getNodeValue();
			//String charset = dbComm.getElementsByTagName("charset").item(0).getFirstChild().getNodeValue();
			//String pwd = dbComm.getElementsByTagName("pwd").item(0).getFirstChild() == null ? "" : dbComm.getElementsByTagName("pwd").item(0).getFirstChild().getNodeValue();
			NodeList dbList = dataDoc.getElementsByTagName("database");
			Element database = null;
			for (int i = 0 ; i < dbList.getLength() ; i++)
			{
				database = (Element)dbList.item(i);
				String attName = database.getAttribute("name");
			
				if (this.dbName.equals(attName))
					break;
			}
			
			String host = database.getElementsByTagName("host").item(0).getFirstChild().getNodeValue();
			String port = database.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
			String databaseName = database.getElementsByTagName("databaseName").item(0).getFirstChild().getNodeValue();
			//String serverName = database.getElementsByTagName("serverName").item(0).getFirstChild().getNodeValue();
			String user = database.getElementsByTagName("user").item(0).getFirstChild().getNodeValue();
			String password = database.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
			try
			{
				db =   new MisDataBaseImp(host, port, databaseName, user, password, flag);
			}
			catch (Exception e)
			{
				System.out.println("Create Database error = " + e.toString());
			}
		}
		catch (Exception e)
		{
			System.out.println("Parsing xml error = "+e.getMessage());
			System.out.println(e.toString());
		}
		return db;
	}
	/**
	 * 測試取得Connection的application
	 */
	public static void main(String[] args)
	{
		
		DbUtil dbTool = new DbUtil(DbType.BIG5_TEST, DbName.DB_BWS);
		//Connection conn = dbTool.getConnection();
		System.out.println("DbUtil main() get db begin ...");
		DataBase db = dbTool.getDataBase();
		System.out.println("DbUtil main() get db success ..." + db);
		try
		{
			getOracleConnection("2");
			
		}
		catch (Exception e)
		{
			System.out.println("close conn error = " + e);
		}
	}
}
