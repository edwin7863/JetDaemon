/*
########################################################################
# SYSTEM  ID:BwsDaemon                                                                                                                                                  #
# PROGRAM ID: DbType.java                                                                                                                                             #
# AUTHOR    : Endra Lee                                                                                                                                                     #
########################################################################
# MODIFICATION LOG:                                                                                                                                                       # 
#   2008/04/11 Endra Lee Modify  getOracleConnection Information from XML                                                                 #
########################################################################
*/
package app.gian;

public class DbType
{
	/**
	 * 定義是正式主機或測試主機
	 */
	public final static String BIG5_FORMAL = "BIG5_FORMAL";
	public final static String BIG5_FORMAL_INNOTRON = "BIG5_FORMAL_INNO";
	public final static String BIG5_FORMAL_JETCHINA = "BIG5_FORMAL_JETCHINA";
	public final static String BIG5_TEST = "BIG5_TEST";
	public final static String BIG5_TEST_INNOTRON = "BIG5_TEST_INNO";
	public final static String BIG5_TEST_JETCHINA = "BIG5_TEST_JETCHINA";
	public final static String ORACLE_FORMAL = "1";
	public final static String ORACLE_TEST = "2";
	public final static String PORTAL_FORMAL = "3";
	public final static String PORTAL_TEST = "4";
	
}
