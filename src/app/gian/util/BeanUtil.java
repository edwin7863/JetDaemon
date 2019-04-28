package app.gian.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.RowSetDynaClass;

public class BeanUtil
{
	
    public static List getDynaBean(ResultSet rs) {
		RowSetDynaClass rsdc;
		List rows = new ArrayList();
        try
        {
            rsdc = new RowSetDynaClass(rs);
			rows = rsdc.getRows(); // Process the rows as desired
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return rows;
    }
   
	public static void copyProperties(Object source, Object target) throws Exception {
		try {
			PropertyUtils.copyProperties(source, target);
		} catch (InvocationTargetException e) {
			throw new Exception("PropertyUtils Exception: " + e.getMessage());
		} catch (Throwable t) {
			throw new Exception("PropertyUtils Exception: " + t.getMessage());
		}
	}    
}
