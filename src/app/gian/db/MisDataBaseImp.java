/**
 * <table border='1'>
 * <tr><td align='right'> SYSTERM ID: </td><td align='left'> # </td></tr>
 * <tr><td align='right'> SUB-SYS ID: </td><td align='left'> # </td></tr>
 * <tr><td align='right'> PROGRAM ID: </td><td align='left'> # </td></tr>
 * <tr><td align='right'> FUNCTION  : </td><td align='left'> MisDataBaseImp </td></tr>
 * <tr><td align='right'> AUTHOR    : </td><td align='left'> Melin Chao </td></tr>
 * <tr><td align='right'> DATE      : </td><td align='left'> 10/01/2004 </td></tr>
 * <tr><td align='right'> TABLE USED: </td><td align='left'> # </td></tr>
 * <tr><td align='right'> FORM  USED: </td><td align='left'> # </td></tr>
 * <tr><td align='right'> FILE  USED: </td><td align='left'> # </td></tr>
 * <tr><td colspan='2'>
 * MODIFICATION LOG:
 * <li></li>
 * </td></tr>
 * </table>
 */
package app.gian.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * @author melin_chao
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MisDataBaseImp implements DataBase
{
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	
	protected DataSource ds = null;
	protected String url = null;
	private Connection conn = null;
	private List connList = new ArrayList();
	public static final int MULITY_CONNECTION = 0;
	public static final int SINGLE_CONNECTION = 1;
	public static final int SINGLE_CONNECTION_AUTOCOMIT_FALSE = 2;
	private int status = 0;

	public MisDataBaseImp(DataSource ds, int status)
	{
		this.ds = ds;
		this.status = status;
	}

	public MisDataBaseImp(DataSource ds, boolean flag)
	{
		if (flag)
		{
			this.status = MULITY_CONNECTION;
		}
		else
		{
			this.status = SINGLE_CONNECTION_AUTOCOMIT_FALSE;
		}
		this.ds = ds;
	}

	public MisDataBaseImp(DataSource ds)
	{
		this(ds, SINGLE_CONNECTION);
	}
	/**
	 * @param host
	 * @param port
	 * @param serverName
	 * @param dbName
	 * @param uid
	 * @param pwd
	 */
	public MisDataBaseImp(String host,String port,String serverName,String dbName,String uid,String pwd,String charset)
	{
		//url :
		this(host, port, serverName, dbName, uid, pwd, true, charset);
	}
	/**
	 * @param host
	 * @param port
	 * @param serverName
	 * @param dbName
	 * @param uid
	 * @param pwd
	 */
	public MisDataBaseImp(String host,String port,String dbName,String uid,String pwd)
	{
		//url :
		this(host, port,  dbName, uid, pwd, true);
	}
	/**
	 * @param host
	 * @param port
	 * @param serverName
	 * @param dbName
	 * @param uid
	 * @param pwd
	 * @param myFlag
	 */
	public MisDataBaseImp(String host,String port,String serverName,String dbName,String uid,String pwd,String apparam,	boolean myFlag)
	{
		//url :
		//this.flag = flag;
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer
			.append("")
			.append("jdbc:informix-sqli://")
			.append(host)
			.append(":")
			.append(port)
			.append("/")
			.append(dbName)
			.append(":INFORMIXSERVER=")
			.append(serverName)
			.append(";NEWLOCALE=zh_tw,zh_tw;NEWCODESET=MS950,big5,57352;")
			.append("user=")
			.append(uid)
			.append(";")
			.append("password=")
			.append(pwd);
		if (apparam != null && !apparam.equals(""))
		{
			urlBuffer.append(apparam);
		}
		this.url = urlBuffer.toString();

		if (myFlag)
		{
			this.status = MULITY_CONNECTION;
		}
		else
		{
			this.status = SINGLE_CONNECTION_AUTOCOMIT_FALSE;
		}
	}

	
	public MisDataBaseImp(String host,String port,String dbName,String uid,String pwd,boolean myFlag)
	{
		//url :
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append("jdbc:mysql://").append(host).append(":").append(port).append("/").append(dbName)
			     .append("?").append("user=").append(uid).append("&").append("password=").append(pwd);
		
		this.url = urlBuffer.toString();
		if (myFlag)
		{
			this.status = SINGLE_CONNECTION;
		}
		else
		{
			this.status = SINGLE_CONNECTION_AUTOCOMIT_FALSE;
		}
	}
	/**
	 * @param host
	 * @param port
	 * @param serverName
	 * @param dbName
	 * @param uid
	 * @param pwd
	 * @param myFlag
	 */
	public MisDataBaseImp(
		String host,
		String port,
		String serverName,
		String dbName,
		String uid,
		String pwd,
		boolean myFlag,
		String charset)
	{
		//url :
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer
			.append("")
			.append("jdbc:informix-sqli://")
			.append(host)
			.append(":")
			.append(port)
			.append("/")
			.append(dbName)
			.append(":INFORMIXSERVER=")
			.append(serverName)
			.append(";NEWLOCALE=zh_tw,zh_tw;NEWCODESET=")
			.append(charset)
			.append(",big5,57352;")
			.append("user=")
			.append(uid)
			.append(";")
			.append("password=")
			.append(pwd);
		this.url = urlBuffer.toString();
		if (myFlag)
		{
			this.status = MULITY_CONNECTION;
		}
		else
		{
			this.status = SINGLE_CONNECTION_AUTOCOMIT_FALSE;
		}
	}
	/**
	 * 當一個flag = false 時,SET Connection AutoCommit 為false,
	 * 並使用commit(),rollback(),close()來管理私有的connection,
	 * @see com.weblink.db.DataBase#getConnection()
	 */
	public Connection getConnection() throws SQLException
	{
		if (this.status == MULITY_CONNECTION || this.conn == null)
		{
			Connection con = null;
			if (ds != null)
			{
				con = ds.getConnection();
			}
			else if (url != null)
			{
				try
				{
					Class.forName(DRIVER);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				con = DriverManager.getConnection(url);
			}
			this.conn = con;
			if (this.status == SINGLE_CONNECTION_AUTOCOMIT_FALSE)
			{
				this.conn.setAutoCommit(false);
			}
			//this.connList.add(con);
		}
		if (this.status == MULITY_CONNECTION)
		{
			this.connList.add(conn);
		}
		return this.conn;
	}

	public void closeConn()
	{
		if (this.status == MULITY_CONNECTION)
		{
			compelClose();
		}
	}

	public void close()
	{
		compelClose();
	}

	/**
	 * 關閉本身提供的SQL Connection 
	 * 
	 */
	public void compelClose()
	{
		/*
		 * 當status == MULITY_CONNECTION時
		 * 新產生的connection 會放在 list 中,一次只關一個,後進先出
		 * 
		 * 當status == SINGLE_CONNECTION || SINGLE_CONNECTION_AUTOCOMIT_FALSE
		 * 直接關掉該connection;
		 */
		if (!connList.isEmpty())
		{
			try
			{
				Connection conn = (Connection) connList.get(connList.size() - 1);
				connList.remove(connList.size() - 1);
				conn.close();
			}
			catch (Exception e)
			{

			}
			finally
			{
				connList.remove(connList.size() - 1);
			}
		}
		else
		{
			if (this.conn != null)
			{
				try
				{
					this.conn.close();
				}
				catch (SQLException e)
				{
					//log4j output
				}
			}
		}
		/*
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (Exception e) {
		        //log4j output
		        } finally {
		            System.out.println(conn);
		        }
		    }
		    */
	}

	public void commit() throws SQLException
	{
		this.conn.commit();
	}

	public void rollback() throws SQLException
	{
		this.conn.rollback();
	}

	/**
	 * Comment這個Method存在的目的以及相關的修改紀錄.<br>
	 * <br>
	 * <b>Referenced by : </b>ProjectName<br>
	 * <b>Created by    : </b>AuthorName<br>
	 * @since Nov 8, 2005
	 * @param i
	 */
	public void setStatus(int i)
	{
		status = i;
	}

}
