package app.gian.db.informix.model;

import app.gian.db.Model;




public class BondnoModel extends Model
{
	protected String invoice  = null;
	protected String bwsnubr  = null;
	protected String deleflag = null;
	protected Number modidate = null;
	
	
    /**
     * Comment�o��Method�s�b���ت��H�ά������ק����.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public String getBwsnubr()
    {
        return bwsnubr;
    }

    /**
     * Comment�o��Method�s�b���ت��H�ά������ק����.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public String getDeleflag()
    {
        return deleflag;
    }

    /**
     * Comment�o��Method�s�b���ت��H�ά������ק����.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public String getInvoice()
    {
        return invoice;
    }

    /**
     * Comment�o��Method�s�b���ت��H�ά������ק����.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public Number getModidate()
    {
        return modidate;
    }

    /**
     * Comment�o��Method�s�b���ت��H�ά������ק����.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param string
     */
    public void setBwsnubr(String string)
    {
        bwsnubr = string;
    }

    /**
     * Comment�o��Method�s�b���ت��H�ά������ק����.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param string
     */
    public void setDeleflag(String string)
    {
        deleflag = string;
    }

    /**
     * Comment�o��Method�s�b���ت��H�ά������ק����.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param string
     */
    public void setInvoice(String string)
    {
        invoice = string;
    }

    /**
     * Comment�o��Method�s�b���ت��H�ά������ק����.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param number
     */
    public void setModidate(Number number)
    {
        modidate = number;
    }

}
