package app.gian;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author melin
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StringUtil
{
	public final static String PRICE_DEFAULT_PATTERN = "$ ##,###,###,##0";
	public final static String PRICE_DEFAULT_PATTERN2 = "##,###,###,##0";
	/**
	 * 驗証公司統編時, 所使用的邏輯乘數.
	 */
	private static int[] COMPANY_ID_LOGIC_MULTIPLIER =
		{ 1, 2, 1, 2, 1, 2, 4, 1 };
        
	public static void main(String[] args)
	{
		System.out.println("台北縣235中和市台北縣中和市連              城路384號4f235;0913476333;       ".length());
		/*
		String aa = "台中縣11弄17號3樓-2台中縣台中縣台中縣台中縣台中縣台北縣台灣省";
		List temp = getStringField(aa,30);
		for (Iterator iter = temp.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			System.out.println(element + " : " + getCharLength(element));
		}
		*/
		
		String price = "2323456";
		String pattern = "& ##,###,###.00";
		System.out.println(priceFormat(price));
		System.out.println(priceFormat(price,pattern));
		//----------------------------------ADD SPACE TEST
		String src = "ab";
		String _src = addSpace(src,30);
		System.out.print(_src);
		System.out.println(_src.length());
	}
	
	/**
	 * 計算字串實際byte長度,例如一個中文char算2個bytes
	 * @param src
	 * @return
	 * @author ryan wu
	 * @since 2006.05.29
	 */
	public static int getStringByteSize(String src) {
		List list = new ArrayList();
		int scope = 255;
		int strCount = 0;
		int unit = 0;
		for(int i=0;i<src.length();i++) {
			int k = src.charAt(i);
			unit = 0;
			if ( k > scope ) {
				unit = 2;
			}
			else {
				unit = 1;
			}
			strCount += unit;
		}
		return strCount;
	}
	
	/**
	 * 傳入src原始字串,根據fieldLength的長度,將字串切割成 fieldLength大小的字串List<br>,
	 * 一個中文字算兩個長度,一個英數字算一個長度,mso.m03_address自動分割字串時適用<br>
	 * @param src
	 * @param fieldLength
	 * @return
	 * @since 2006.04.25
	 * @author Ryan Wu
	 * @version 1.0
	 */
	public static List getStringField(String src,int fieldLength) {
		List list = new ArrayList();
		int scope = 255;
		StringBuffer sb = new StringBuffer();
		int strCount = 0;
		int unit = 0;
		for(int i=0;i<src.length();i++) {
			int k = src.charAt(i);
			unit = 0;
			if ( k > scope ) {
				unit = 2;
			}
			else {
				unit = 1;
			}
			strCount += unit;
			if ( strCount > fieldLength ) {
				strCount = unit;
				list.add(sb.toString());
				sb = null;
				sb = new StringBuffer();
				sb.append(src.charAt(i));	
			}
			else {
				sb.append(src.charAt(i));
			}
		}
		list.add(sb.toString());
		return list;
	}
	
	/**
	 * 傳入src原始字串,根據fieldLength的長度,判斷src的長度是否超過fieldLength
	 * @param src
	 * @param fieldLength
	 * @return
	 * @since 2006/12/07
	 * @author Leaf Chi
	 * @version 1.0
	 */
	public boolean overStringLength(String src,int fieldLength) {
		List list = new ArrayList();
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		if(getStringByteSize(src)>fieldLength)
		{
			flag=true;	
		}
		return flag;
	}
	
	/**
	 * @param str
	 * @param delim
	 * @param index
	 * @return
	 */
	public static String getToken(String str, String delim, int index)
	{
		if (str == null)
			return null;
		StringTokenizer st = new StringTokenizer(str, delim);
		int tokenSize = st.countTokens();
		int i = 0;
		String token = null;
		while (st.hasMoreTokens())
		{
			token = st.nextToken();
			if (i == index)
			{
				break;
			}
			i++;
		}
		return token;
	}

	/**
	 * @param num
	 * @return
	 */
	public static String numString(String num)
	{
		int numInt = Integer.parseInt(num);
		StringBuffer numStr = new StringBuffer("0");
		if (numInt < 10)
		{
			numStr.append(String.valueOf(numInt));
		}
		else
			numStr = new StringBuffer(String.valueOf(numInt));
		return numStr.toString();
	}

	/**
	 * @param Content
	 * @return
	 */
	public static String addBr(String Content)
	{
		String line = System.getProperty("line.separator");
		String makeContent = new String();
		StringTokenizer strToken = new StringTokenizer(Content, line);
		while (strToken.hasMoreTokens())
		{
			makeContent += "<BR/>" + strToken.nextToken();
		} //while_end
		return makeContent;
	}

	/**
	 * @param src
	 * @return
	 */
	public static String htmlEscape(String src)
	{
		StringReader in = new StringReader(src);
		StringWriter out = new StringWriter();
		try
		{
			int ch;
			while ((ch = in.read()) != -1)
			{ // while not EOF
				char c = (char) ch;
				switch (c)
				{
					case '<' :
						out.write("&lt;");
						break;
					case '>' :
						out.write("&gt;");
						break;
					case '&' :
						out.write("&amp;");
						break;
					case '"' :
						out.write("&quot;");
						break;
					default :
						out.write(c);
				}
			}
		}
		catch (IOException e)
		{
			System.out.print("Internal error at htmlEscape:" + e.getMessage());
		}
		return out.toString();
	}

	/**
	 * @param src
	 * @return
	 */
	public static String dbEscape(String src)
	{
		StringReader in = null;
		StringWriter out = null;
		if (src != null && !src.equals(""))
		{
			in = new StringReader(src);
			out = new StringWriter();
			try
			{
				int ch;
				while ((ch = in.read()) != -1)
				{ // while not EOF
					char c = (char) ch;
					switch (c)
					{
						case '\'' :
							out.write("''");
							break;
						case '\\' :
							out.write("\\\\");
						default :
							out.write(c);
					}
				}
			}
			catch (IOException e)
			{
				System.out.print(
					"Internal error at htmlEscape:" + e.getMessage());
			}
			return out.toString();
		}
		else
		{
			return " ";
		}
	}

	public static String replaceString2(
		String originalString,
		String findString,
		String replaceString)
	{
		StringBuffer sb = new StringBuffer();
		int i = 0;
		String temp = originalString;
		if (originalString.length() > 0 && findString.length() > 0)
		{
			while (i < temp.length()
				&& (i = temp.indexOf(findString, i)) != -1)
			{
				sb.delete(0, sb.toString().length());
				sb.append(temp.substring(0, i)).append(replaceString).append(
					temp.substring(i + findString.length(), temp.length()));
				temp = sb.toString();
				i += replaceString.length();
			}
			return temp;
		}
		else
			return originalString;
	}

	/**
	 * @param src
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static String replaceString(
		String src,
		String oldString,
		String newString)
	{
		String target = "";
		if (src != null)
		{
			int odx, ndx;
			target = "";
			odx = ndx = 0;

			while (true)
			{
				ndx = src.indexOf(oldString, odx);
				if (ndx >= odx)
				{
					target = target + src.substring(odx, ndx) + newString;
					odx = ndx + oldString.length();
				}
				else if (ndx == -1)
				{
					target = target + src.substring(odx);
					break;
				}
			}
		}

		return target;
	}

	/**
	 * 將換行符號改成&lt;br&rt;,for html
	 * @param str
	 * @return
	 */
	public static String nl2br(String str)
	{
		int idx = -1;
		while ((idx = str.indexOf("\r")) != -1)
		{
			str = str.substring(0, idx) + "" + str.substring(idx + 1);
		}
		while ((idx = str.indexOf("\n")) != -1)
		{
			str = str.substring(0, idx) + "<br>" + str.substring(idx + 1);
		}
		return str;
	}

	/**
	 * @param src
	 * @return
	 */
	public static String convertString(String src)
	{
		String default_string = "";
		return convertString(src, default_string);
	}

	public static String convertString(String src, String isNull)
	{
		if (src == null)
			return isNull;
		else
			return src.trim();
	}

	/**
	 * ex. format='0000'  num='12'<br>
	 * return 0012
	 * @param format
	 * @param num
	 * @return
	 */
	public static String formatIntToString(String format, int num)
	{
		String numStr = String.valueOf(num);
		int colnum = format.length();
		int diffnum = 0;
		if ((diffnum = colnum - numStr.length()) > 0)
		{
			numStr = format.substring(0, diffnum) + numStr;
		}
		return numStr;
	}
	/**
	 *
	 */
	public static int getDelimShowTimes(String orgStr, String delim)
	{
		int pos = 0;
		int count = 0;
		while ((pos = orgStr.indexOf(delim)) != -1)
		{
			count++;
			orgStr = orgStr.substring(pos + 1, orgStr.length());
		}
		return count;
	}

	/**
	 * 判斷一個字串是否為數字<br>
	 * <br>
	 * <b>Referenced by : </b>ProjectName<br>
	 * <b>Created by    : </b>Jason Chu<br>
	 * @since Nov 17, 2004
	 * @param str
	 * @return
	 */
	public static boolean isNaN(String str)
	{
		boolean flag = true;
		try
		{
			int num = Integer.parseInt(str);
		}
		catch (Exception e)
		{
			flag = false;
		}
		return flag;
	}

	/**
	 * 驗証公司統編是否符合現行規則.
	 * <b>Created by    : </b>Ryan Wu<br>
	 * @since 2005.09.02
	 * @param aCompanyId 傳入所要驗証的公司統編.
	 * @return 回傳公司統編是否正確, true 代表正確.
	 */
	public static boolean isCompanyIdValid(String aCompanyId)
	{
		//驗証公司統編時, 所使用的邏輯乘數.
		try
		{
			int aSum = 0;

			for (int i = 0; i < COMPANY_ID_LOGIC_MULTIPLIER.length; i++)
			{
				//公司統編與邏輯乘數相乘.
				int aMultiply =
					Integer.parseInt(aCompanyId.substring(i, i + 1))
						* COMPANY_ID_LOGIC_MULTIPLIER[i];

				//將相乘的結果, 取十位數及個位數相加.
				int aAddition = ((aMultiply / 10) + (aMultiply % 10));

				//如果公司統編的第 7 位是 7 時, 會造成相加結果為 10 的特殊情況, 所以直接以 1 代替進行加總.
				aSum += (aAddition == 10) ? 1 : aAddition;
			}

			//判斷總和的餘數, 假使為 0 公司統編正確回傳 true, 其它值則反之.
			return (aSum % 10 == 0);
		}
		catch (Throwable e)
		{
			//如果 aCompanyId 參數為 null, 或者不是八位數, 或為其它非數值字串, 均傳回 false.
			return false;
		}
	}
	
	/**
	 * 計算字串的長度,一個中文字佔兩個長度,英數字佔一個長度.<br>
	 * <br>
	 * <b>Referenced by : </b>B2B<br>
	 * <b>Created by    : </b>Ryan Wu<br>
	 * @since Sep 2, 2005
	 * @param 
	 * @return
	 */
	public static int getCharLength(String str) {
		byte[] aa = null;
		try
		{
			aa = str.getBytes("MS950");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return aa.length;
	}

	/**
	 * copy from replaceString(), 加入了oldString的長度檢查<br>
	 * <br>
	 * <b>Referenced by : </b>ec<br>
	 * <b>Created by    : </b>Jason Chu<br>
	 * @since Oct 4, 2005
	 * @param src
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static String replaceString3(
		String src,
		String oldString,
		String newString)
	{
		if (src != null)
		{
			int ndx = 0;
			while (src.indexOf(oldString) >= 0)
			{
				ndx = src.indexOf(oldString);
				//System.out.println("ndx = " + ndx);
				if (ndx >= 0)
				{
					src = src.substring(0, ndx) + newString + src.substring(ndx + oldString.length());
					//System.out.println("src = " + src);
				}
			}
		}

		return src;
	}
	
	/**
	 * 傳入字串,取得金額格式化後的內容
	 * @param src
	 * @return
	 */
	public static String priceFormat(String src)
	{
		return priceFormat(src, PRICE_DEFAULT_PATTERN2);
	}
	
	/**
	 * 傳入字串及格式,取得金額格式化後的內容
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static String priceFormat(String src, String pattern)
	{
		String str = null;
		try
		{
			if (src != null && !src.equals("0.000") && !src.equals(""))
			{
				DecimalFormat de = new DecimalFormat(pattern);
				str = de.format(new Double(src));
			}
			else
			{
				str = "";
			}
		}
		catch (Exception e)
		{}
		return str;
	}
	
	/**
	 * 傳入字串及空白,依需要的長度補上空白
	 * 中文字長度算2,半形英文字長度算1
	 * @param src 原始文字
	 * @param length 長度
	 * @return 添加空白後的字串
	 * @author melin
	 */
	public static String addSpace(String src, int length)
	{
		StringWriter out = new StringWriter();
		int srcAsciiLength = getCharLength(src);
		out.write(src);
		for(int i = 0; i < (length - srcAsciiLength); i++)
		{
			out.write(' ');
		} 
		return out.toString();
	}
}