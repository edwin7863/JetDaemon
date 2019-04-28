package app.gian;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * SYSTERM ID: UTIL
 * SUB-SYS ID:
 * PROGRAM ID: WiiDateTime
 * FUNCTION  : 日期相關工具
 * AUTHOR    : Melin Chao
 * DATE      : 04/22/2003
 * TABLE USED:
 * FORM  USED:
 * FILE  USED:

 * MODIFICATION LOG:
 * ->MM/DD/YYYY, UserName, Log
 * ->08/07/2003, Melin Chao, Add a method:transDateFormatString
 */

public class DateTimeUtil
{
    private Date d;
    private Calendar c = null;
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        //System.out.println(DateTimeUtil.getYearDiff(new Date(), 1975));
        System.out.println(DateTimeUtil.getBcDate("093/01/05","/"));
		Date issDate = DateTimeUtil.StringParseToDate("2005-03-12 00:00:00","yyyy-MM-dd HH:mm:ss");
		System.out.println(issDate);      
    }

    /**
     * @param compareDate1
     * @param compareYear
     * @return
     */
    public static int getYearDiff(Date compareDate1, int compareYear)
    {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(compareDate1);
        return c1.get(Calendar.YEAR) - compareYear;
    }

    /**
     *
     */
    public DateTimeUtil()
    {
        d = new Date();
        c = Calendar.getInstance();
        c.setTime(d);
    }

    /**
     * @param d
     */
    public DateTimeUtil(Date d)
    {
        c = Calendar.getInstance();
        c.setTime(d);
    }

    /**
     * @return
     */
    public int getYearInt()
    {
        return c.get(Calendar.YEAR);
    }

    /**
     * @return
     */
    public int getMonthInt()
    {
        return 1 + c.get(Calendar.MONTH);
    }

    /**
     * @return
     */
    public int getDayInt()
    {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return
     */
    public int getHourInt()
    {
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @return
     */
    public int getMinuteInt()
    {
        return c.get(Calendar.MINUTE);
    }

    /**
     * @return
     */
    public int getSecondInt()
    {
        return c.get(Calendar.SECOND);
    }

    /**
     * @return
     */
    //YYYY-MM-DD
    public String getDateString()
    {
        StringBuffer dateStr = new StringBuffer();
        dateStr.append(getYearInt() + "-");
        if (getMonthInt() < 10)
        {
            dateStr.append("0" + getMonthInt() + "-");
        }
        else
        {
            dateStr.append(getMonthInt() + "-");
        }
        if (getDayInt() < 10)
        {
            dateStr.append("0" + getDayInt());
        }
        else
        {
            dateStr.append(getDayInt());

        }
        return dateStr.toString();
    }

    /**
     * @param delim
     * @return
     */
    public String getDateString(String delim)
    {
        StringBuffer dateStr = new StringBuffer();
        dateStr.append(getYearInt() + delim);
        if (getMonthInt() < 10)
        {
            dateStr.append("0" + getMonthInt() + delim);
        }
        else
        {
            dateStr.append(getMonthInt() + delim);
        }
        if (getDayInt() < 10)
        {
            dateStr.append("0" + getDayInt());
        }
        else
        {
            dateStr.append(getDayInt());

        }
        return dateStr.toString();
    }

    /**
     * @return
     */
    //HH:MM:SS
    public String getTimeString()
    {
        StringBuffer timeStr = new StringBuffer();
        if (getHourInt() < 10)
        {
            timeStr.append("0" + getHourInt() + ":");
        }
        else
        {
            timeStr.append(getHourInt() + ":");
        }
        if (getMinuteInt() < 10)
        {
            timeStr.append("0" + getMinuteInt() + ":");
        }
        else
        {
            timeStr.append(getMinuteInt() + ":");
        }
        if (getSecondInt() < 10)
        {
            timeStr.append("0" + getSecondInt());
        }
        else
        {
            timeStr.append(getSecondInt());

        }
        return timeStr.toString();
    }

    /**
     * @param delim
     * @return
     */
    public String getTimeString(String delim)
    {
        StringBuffer timeStr = new StringBuffer();
        if (getHourInt() < 10)
        {
            timeStr.append("0" + getHourInt() + delim);
        }
        else
        {
            timeStr.append(getHourInt() + delim);
        }
        if (getMinuteInt() < 10)
        {
            timeStr.append("0" + getMinuteInt() + delim);
        }
        else
        {
            timeStr.append(getMinuteInt() + delim);
        }
        if (getSecondInt() < 10)
        {
            timeStr.append("0" + getSecondInt());
        }
        else
        {
            timeStr.append(getSecondInt());

        }
        return timeStr.toString();
    }

    /**
     * @param date
     * @return
     */
    //=====  SimpleDateFormat =====
    //2002-03-24 12:25:30
    public static String DateTimeFormatToString(java.util.Date date)
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * @param date
     * @param pattern
     * @return
     * Example
     * 2002-03-24 12:34:46 星期日("yyyy-MM-dd HH:mm:ss E")
     */
    public static String DateTimeFormatToString(
        java.util.Date date,
        String pattern)
    {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * @param date
     * @param pattern
     * @param locale
     * @return DateFormat
     *
     * Example
     * 2002-03-24 12:34:46 星期日		("yyyy-MM-dd HH:mm:ss E")(Locale.TAIWAN)
     * 2002-Mar-24 12:54:50 Sun		("yyyy-MMM-dd HH:mm:ss E")(Locale.US)
     * 2002-March-24 12:56:25 Sunday ("yyyy-MMMM-dd HH:mm:ss EEEE")(Locale.US)
     */
    public static String DateTimeFormatToString(
        java.util.Date date,
        String pattern,
        Locale locale)
    {
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    /**
     * @param dateStr
     * @param pattern
     * @return
     */
    //===== 透過 SimpleDateFormat 傳入日期時間字串 而取得 java.util.Date 物件 =====
    public static java.util.Date StringParseToDate(
        String dateStr,
        String pattern)
    {
        try
        {
            return new SimpleDateFormat(pattern).parse(dateStr);
        }
        catch (Exception e)
        {
            return new java.util.Date(0);
        }
    }


    /** 透過 Calendar 物件 來取得所想要的 java.util.Date 物件
     * Calendar cal = Calendar.getInstance();
     * cal.set(int field, int value);
     * cal.set(int year, int month, int date);
     * cal.set(int year, int month, int date, int hour, int minute);
     * cal.set(int year, int month, int date, int hour, int minute, int second);
     * Date cal.getTime();
     */

    public String getNextMonth()
    {
        return getNextMonth(1);
    }

    public String getNextMonth(int differ)
    {
        c.set(Calendar.MONTH, (getMonthInt() -1) + differ);
        return getDateString();
    }

	/**
     * 以今天的日期為base 算前後的日期
	 * @return
	 */
    public String getNextDate()
    {
        c.set(Calendar.DAY_OF_MONTH, getDayInt() + 1);
        return getDateString();
    }

    /**
     * @param differ
     * @return
     */
    public String getNextDate(int differ)
    {
        c.set(Calendar.DAY_OF_MONTH, getDayInt() + differ);
        return getDateString();
    }

    /**
     * @return
     */
    public String getPrevDate()
    {
        c.set(Calendar.DAY_OF_MONTH, getDayInt() - 1);
        return getDateString();
    }

    /**
     * @param differ
     * @return
     * 取得 differ 天前的日期
     */
    public String getPrevDate(int differ)
    {
        c.set(Calendar.DAY_OF_MONTH, getDayInt() - differ);
        return getDateString();
    }

	/**
	 * 取得 距離月底的天數
	 */
	public int getDaysToLastDayOfTheMonth()
	{
		int intYear = this.getYearInt();
		int intMonth = this.getMonthInt();
		int intDay   = this.getDayInt();
		int lastDay = 0 ;
		if ( (intMonth == 1) || (intMonth == 3) || (intMonth == 5) || (intMonth == 7) || (intMonth == 8)  || (intMonth == 10) || (intMonth == 12))
		{
			   lastDay = 31;
		}
		if ( (intMonth == 4) || (intMonth == 6) || (intMonth == 9) || (intMonth == 11))
		{
			   lastDay = 30;
		}
		if ( (intMonth == 2))
		{
			   if (intYear%4==0)
			   {
					if (intYear%400==0)
					{
						lastDay = 28;
					}
					else
					{
						lastDay = 29;
					}
			   }
			   else
			   {
					lastDay = 28;
			   }
		}
		return lastDay - intDay ;
	}

    /**
	 * @param dateString
	 * @param oldPattern
	 * @param newPattern
	 * @return
	 */
	public static String transDateFormatString(String dateString,String oldPattern,String newPattern)
    {
        Date myDate = DateTimeUtil.StringParseToDate(dateString,oldPattern);
        String newString = DateTimeUtil.DateTimeFormatToString(myDate,newPattern);
        return newString;
    }

    /**
     *  @param yyymmdd 民國 YYMMDD
     *  @return cal 西元 java.util.Date
     */
    public static Date getBcDate(String yyymmdd,String delim) {
        int year = 0;
        int mon = 0;
        int date = 0;
        if ( delim == null || delim.equals("")) {
            year = Integer.parseInt(yyymmdd.substring(0,3));
            mon = Integer.parseInt(yyymmdd.substring(3,5));
            date = Integer.parseInt(yyymmdd.substring(5,yyymmdd.length()));
        } else {
            year = Integer.parseInt(yyymmdd.substring(0,3));
            mon = Integer.parseInt(yyymmdd.substring(4,6));
            date = Integer.parseInt(yyymmdd.substring(7,yyymmdd.length()));
        }

        if ( year >= 100 ) {
            year =  year + 11;
        } else {
            year = year - 100 + 11;
        }
        if ( mon > 0 ) {
            mon --;
        }
        year += 2000;
        Calendar cal =  new java.util.GregorianCalendar();
        cal.set(year,mon,date);
        return cal.getTime();
    }
    
	/**
	 * 取得兩日期差距的天數<br>
	 * <br>	 
	 * <b>Created by    : </b>Endra Lee<br>
	 * @since 11/29/2005
	 * @param fromDate
	 * @param toDate
	 * @return long	 
	 */    
    public static int getDaysBetween2Date(String fromDate ,String toDate)
    {				
		Date dateF = StringParseToDate(fromDate, "yyyy-MM-dd");	  
 	    Date dateT = StringParseToDate(toDate  , "yyyy-MM-dd");
	    long dateRange = dateT.getTime() - dateF.getTime();
	    int  days = (int) (dateRange/(1000 * 3600 * 24 )) ; 
	    return days;
    }	
    
    
}
