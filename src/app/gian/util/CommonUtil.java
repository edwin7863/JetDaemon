package app.gian.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CommonUtil {
	
    public static String stringToHexString(String str)
    {
        String hexString = "";
        for (int i = 0; i < str.length(); i++) {
            int ch = (int) str.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString = hexString + strHex;
        }
        return hexString.toUpperCase();
    }
    
    public static String byteToHexString(String strb)
    {
    	byte[] b=strb.getBytes();
        String str = "";
        for (int i=0;i<b.length;i++)
        {
	        if ((b[i] & 0xff) < 16) {
	            str = "0";
	        }
	        str = str + Integer.toHexString(b[i] & 0xff);
        }
        return str.toUpperCase();
    }
    
    // decode hex string to normal string.
    public static String hexToString(String str)
    {
        byte[] baKeyword = new byte[str.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            str = new String(baKeyword, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    // decode hex string to byte.
    public static byte hexToByte(String str)
    {
        return (byte) Integer.parseInt(str, 16);
    }

    // transfer integer to byte array.
    public static byte[] integerToBytes(int intValue)
    {
        byte[] result = new byte[4];
        result[0] = (byte) ((intValue & 0xFF000000) >> 24);
        result[1] = (byte) ((intValue & 0x00FF0000) >> 16);
        result[2] = (byte) ((intValue & 0x0000FF00) >> 8);
        result[3] = (byte) ((intValue & 0x000000FF));
        return result;
    }

    // byte array to integer.
    public static int bytesToInteger(byte[] byteVal)
    {
        int result = 0;
        for (int i = 0; i < byteVal.length; i++) {
            int tmpVal = (byteVal[i] << (8 * (3 - i)));
            switch (i) {
                case 0:
                    tmpVal = tmpVal & 0xFF000000;
                    break;
                case 1:
                    tmpVal = tmpVal & 0x00FF0000;
                    break;
                case 2:
                    tmpVal = tmpVal & 0x0000FF00;
                    break;
                case 3:
                    tmpVal = tmpVal & 0x000000FF;
                    break;
            }
            result = result | tmpVal;
        }
        return result;
    }
	public static String getCurrency(String compcode,String currency)
	{
		if(currency.equals("U"))
		{
			currency="USD";
		}
		else if(currency.equals("N"))
		{
			if(compcode.equals("C"))//嘲そ
			{
				currency ="RMB";
			}
			else
			{
				currency ="NTD";
			}
		}

		return currency;
	}
	/**璸衡虫基*计秖
	 * @author admin Ashley
	 *@param unitpric -虫基
	 *@param quantity -计秖 
	 */
	public static String getAmount(String unitpric,String quantity)
	{
		BigDecimal nunitpric;
		if(isNullorEmpty(unitpric.trim()))
			nunitpric= new BigDecimal(0);
		else
			nunitpric= new BigDecimal(unitpric);
		
		BigDecimal nquantity;
		if(isNullorEmpty(quantity.trim()))
			nquantity= new BigDecimal(0);
		else
			nquantity= new BigDecimal(quantity);

		BigDecimal amount =nunitpric.multiply(nquantity).setScale(4, RoundingMode.HALF_UP);
		return amount.toString();
	}
	
	/**璸衡虫基*计秖
	 * @author admin Ashley
	 *@param unitpric -虫基
	 *@param quantity -计秖 
	 */
	public static String getAmount(double unitpric,String quantity)
	{
		BigDecimal nunitpric;
		nunitpric=new BigDecimal(unitpric);
		//if(isNullorEmpty(unitpric.trim()))
		//	nunitpric= new BigDecimal(0);
		//else
		//	nunitpric= new BigDecimal(unitpric);
		
		BigDecimal nquantity;
		if(isNullorEmpty(quantity.trim()))
			nquantity= new BigDecimal(0);
		else
			nquantity= new BigDecimal(quantity);

		BigDecimal amount =nunitpric.multiply(nquantity).setScale(4, RoundingMode.HALF_UP);
		return amount.toString();
	}
	
	/**
	 * 璸衡羆基籔祙肂
	 * @param taxamount
	 * @param amount
	 * @return
	 */
	public static String getTaxTotalAmount(String taxamount,String amount)
	{
		
		BigDecimal amt;
		if(isNullorEmpty(amount.trim()))
			amt= new BigDecimal(0);
		else
			amt= new BigDecimal(amount);
		
		BigDecimal tamt;
		if(isNullorEmpty(taxamount.trim()))
			tamt= new BigDecimal(0);
		else
			tamt= new BigDecimal(taxamount);

		amt =amt.add(tamt).setScale(4, RoundingMode.HALF_UP);
		return amt.toString();
	}
	
	/**璸衡羆基*祙瞯肚祙肂
	 * @author admin Ashley
	 *@param amount -羆基
	 *@param taxrate -祙瞯 
	 */
	public static String getTaxAmount(String amount,String taxrate)
	{
		BigDecimal amt;
		if(isNullorEmpty(amount.trim()))
			amt= new BigDecimal(0);
		else
			amt= new BigDecimal(amount);
		
		BigDecimal trate;
		if(isNullorEmpty(taxrate.trim()))
			trate= new BigDecimal(0);
		else
			trate= new BigDecimal(taxrate);

		amt =amt.multiply(trate).setScale(4, RoundingMode.HALF_UP);
		return amt.toString();
	}

	/**璸衡羆基*蹲瞯肚羆基
	 * @author admin Ashley
	 *@param amount -羆基
	 *@param taxrate -祙瞯 
	 */
	public static String getAmountRate(String amount,String taxrate)
	{
		BigDecimal amt;
		if(isNullorEmpty(amount.trim()))
			amt= new BigDecimal(0);
		else
			amt= new BigDecimal(amount);
		
		BigDecimal trate;
		if(isNullorEmpty(taxrate.trim()))
			trate= new BigDecimal(0);
		else
			trate= new BigDecimal(taxrate);

		amt =amt.multiply(trate).setScale(4, RoundingMode.HALF_UP);
		return amt.toString();
	}
	
	public static boolean isNullorEmpty(Object src)
	{
		if(src==null)
			return true;
		if(src.toString().equals(""))
			return true;
		return false;
	}
	//ㄌ酚╰参砞﹚祙ъ羆基肚
	//public static String getSumAmtATax(String price,String taxtype)
	//{
//
	//}
}
