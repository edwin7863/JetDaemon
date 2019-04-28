/*
 * Created on 2003/11/12
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package app.gian.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ryan Wu
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface DataBase
{
    public Connection getConnection() throws SQLException;
    public void closeConn();
}
