package app.gian.db.informix.model;

import app.gian.db.Model;




public class BondstockModel extends Model
{
	protected String prodnubr = null;
	protected String co       = null;
	protected String loc      = null;
	protected Number avlqty   = null;
	protected Number begqty   = null;
	protected Number mtdqty   = null;	
	protected Number lmtdqty  = null;
	
	
	
    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public Number getAvlqty()
    {
        return avlqty;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public Number getBegqty()
    {
        return begqty;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public String getCo()
    {
        return co;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public Number getLmtdqty()
    {
        return lmtdqty;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public String getLoc()
    {
        return loc;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public Number getMtdqty()
    {
        return mtdqty;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @return
     */
    public String getProdnubr()
    {
        return prodnubr;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param number
     */
    public void setAvlqty(Number number)
    {
        avlqty = number;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param number
     */
    public void setBegqty(Number number)
    {
        begqty = number;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param string
     */
    public void setCo(String string)
    {
        co = string;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param number
     */
    public void setLmtdqty(Number number)
    {
        lmtdqty = number;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param string
     */
    public void setLoc(String string)
    {
        loc = string;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param number
     */
    public void setMtdqty(Number number)
    {
        mtdqty = number;
    }

    /**
     * Comment這個Method存在的目的以及相關的修改紀錄.<br>
     * <br>
     * <b>Referenced by : </b>ProjectName<br>
     * <b>Created by    : </b>AuthorName<br>
     * @since Jan 25, 2008
     * @param string
     */
    public void setProdnubr(String string)
    {
        prodnubr = string;
    }

}
