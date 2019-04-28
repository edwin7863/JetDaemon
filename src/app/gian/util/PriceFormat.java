/*
########################################################################
# SYSTEM  ID:BwsDaemon                                                                                                                                                  #
# PROGRAM ID:PriceFormat.java                                                                                                                                       #
# AUTHOR    : Endra Lee                                                                                                                                                     #
########################################################################
# MODIFICATION LOG:                                                                                                                                                       #
########################################################################
*/
package app.gian.util;

import java.text.*;
import java.math.*;

/** <PRE> PriceFormat物件是一個將數值設定成為指定格式的字串處理物件，
 * 亦包含將格式字串反轉成數值的method</PRE>
 *
 * @author Edward
 * @version 1.0.0
 */
public class PriceFormat extends Object {
    
/** <PRE> 設定 Price 格式為 Default
 * 格式為 ##,###,###.### 含小數點後三位</PRE>
 */    
    public static String DEFAULT = "##,###,##0.0000";
/** <PRE>設定Price格式為整數型態格式，格式為 ##,###,###</PRE>
 */    
    public static String INTEGER_FORMAT = "##,###,###";
/** <PRE> 設定Price格式為美元格式，格式為 $ ##,###,###.##
 * 數字包含小數點下後兩位</PRE>
 */    
    public static String US_DOLLARS_FORAMT = "$ ##,###,##0.00";
/** <PRE> 設定Price格式為新台幣格式，格式為 NT$ ##,###,###
 * 不包含小數點以下位數</PRE>
 */    
    public static String NEW_TAIWAN_CURRENCY_FORMAT = "NT$ ##,###,###";
    
    private String pricePattern;
    private DecimalFormat processFormat;
    /** 建立一個 PriceFormat 物件
 */
    public PriceFormat() {
        super();
        pricePattern = DEFAULT;
    }
    //
/** 建立一個PriceFormat物件，自訂Price格式
 * @param priceFormat 欲設定的格式字串
 */    
    public PriceFormat(String priceFormat) {
        pricePattern = priceFormat;
    }
    //
/** <PRE> 設定 Price 格式</PRE>
 * @param pattern 格式字串
 */    
    public void setPricePattern(String pattern){
        pricePattern = pattern;        
    }
    //
/** <PRE> 將輸入的數值(double 型態)，以設定的格式排列，傳出組合後的字串</PRE>
 * @param priceValue 傳入的數值
 * @param pattern 格式字串
 * @return 傳回根據指定格式設定的字串
 */    
    public String format(double priceValue, String pattern){
        try{
            String returnFormat = "";
            processFormat = new DecimalFormat(pattern);
            returnFormat = processFormat.format(priceValue);
            return returnFormat;                    
        }
        catch(Exception e){
            return null;
        }
    }
    //
/** <PRE> 將輸入的數值(double 型態)，以設定的格式排列，傳出組合後的字串</PRE>
 * @param priceValue 傳入的數值
 * @return 傳回根據指定格式設定的字串
 */    
    public String format(double priceValue){
        try{
            String returnFormat = "";
            returnFormat = format(priceValue,pricePattern);
            return returnFormat;
        }
        catch(Exception e){
            return null;
        }
    }
    //
/** <PRE> 將輸入的數值(long 型態)，以設定的格式排列，傳出組合後的字串</PRE>
 * @param priceValue 傳入的數值
 * @param pattern 格式字串
 * @return 傳回根據指定格式設定的字串
 */    
    public String format(long priceValue, String pattern){
        try{
            String returnFormat = "";
            processFormat = new DecimalFormat(pattern);
            returnFormat = processFormat.format(priceValue);
            return returnFormat;                    
        }
        catch(Exception e){
            return null;
        }
    }
    //
/** <PRE> 將輸入的數值(double 型態)，以設定的格式排列，傳出組合後的字串</PRE>
 * @param priceValue 傳入的數值
 * @return 傳回根據指定格式設定的字串
 */    
    public String format(long priceValue){
        try{
            String returnFormat = "";
            returnFormat = format(priceValue, pricePattern);
            return returnFormat;
        }
        catch(Exception e){
            return null;
        }
    } 
    //
/** <PRE> 將原有格式的字串轉換成目前設定的 Price 格式</PRE>
 * @param priceValue 原本的 Price 格式的字串
 * @param pattern 原本格式定義的字串
 * @return 傳回根據目前指定格式設定的字串
 */    
    public String format(String priceValue, String pattern){
        try{
            return format(parseDouble(priceValue, pattern), pricePattern);
        }
        catch(Exception e){
            return null;
        }
    }

/** <PRE> 數值字串轉換成目前設定的 Price 格式</PRE>
 * @param priceValue 數值字串
 * @return 傳回根據目前指定格式設定的字串
 */    
    public String format(String priceValue){
        try{
            return format(parseDouble(priceValue), pricePattern);
        }
        catch(Exception e){
            return null;
        }
    } 
/** <PRE> 將輸入的數值(Double 型態)，以設定的格式排列，傳出組合後的字串</PRE>
 * @param priceValue 傳入Double型態的數值
 * @param pattern 指定輸出的格式
 * @return 傳回一指定格式的 Price 字串
 */    
    public String format(Double priceValue, String pattern){
        try{
            return format(priceValue.doubleValue(), pattern);
        }
        catch(Exception e){
            return null;
        }
    }
/** <PRE> 將輸入的數值(Double 型態)，以設定的格式排列，傳出組合後的字串</PRE>
 * @param priceValue 輸入 Double 型態的數值
 * @return 傳回一指定格式的 Price 字串
 */    
    public String format(Double priceValue){
        try{
            return format(priceValue.doubleValue());
        }
        catch(Exception e){
            return null;
        }
    }
/** <PRE> 將輸入的數值(BigDecimal 型態)，以設定的格式排列，傳出組合後的字串</PRE>
 * @param priceValue 傳入 BigDecimal 型態的數值
 * @param pattern 指定輸出的格式
 * @return 傳回一指定格式的 Price 字串
 */    
    public String format(BigDecimal priceValue, String pattern){
        try{
            return format(priceValue.doubleValue(), pattern);
        }
        catch(Exception e){
            return null;
        }
    }
/** <PRE> 將輸入的數值(BigDecimal 型態)，以設定的格式排列，傳出組合後的字串</PRE>
 * @param priceValue 傳入 BigDecimal 型態的數值
 * @return 傳回一指定格式的 Price 字串
 */    
    public String format(BigDecimal priceValue){
        try{
            return format(priceValue.doubleValue());
        }
        catch(Exception e){
            return null;
        }
    }
    
    //
/** <PRE> 將已被格式字串中的數值取出，傳回 Number 型態的數值</PRE>
 * @param priceString 原本的 Price 格式的字串
 * @param pattern 原本格式定義的字串
 * @return 傳回 Number 型態的數值
 */    
    public Number parseNumber(String priceString, String pattern){
        try{
            Number parseNumber;
            processFormat = new DecimalFormat(pattern);
            parseNumber = processFormat.parse(priceString);
            return parseNumber;
        }
        catch(Exception e){
            return null;
        }
    }
    //
/** <PRE> 將已被格式字串中的數值取出，傳回 int 型態的數值</PRE>
 * @param priceString 原本的 Price 格式的字串
 * @param pattern 原本格式定義的字串
 * @return 傳回 int 型態的數值
 */    
    public int parseInt(String priceString, String pattern){
        try{
            Number parseNumber;
            parseNumber = parseNumber(priceString, pattern);
            return parseNumber.intValue();
        }
        catch(Exception e){
            return 0;
        }
    }
    //
/** <PRE> 將已被格式字串中的數值取出，傳回 double 型態的數值</PRE>
 * @param priceString 原本的 Price 格式的字串
 * @param pattern 原本格式定義的字串
 * @return 傳回 int 型態的數值
 */    
    public double parseDouble(String priceString, String pattern){
        try{
            Number parseNumber;
            parseNumber = parseNumber(priceString, pattern);
            return parseNumber.doubleValue();
        }
        catch(Exception e){
            return 0;
        }
    }
    //
/** <PRE> 將已被格式字串中的數值取出，傳回 float 型態的數值</PRE>
 * @param priceString 原本的 Price 格式的字串
 * @param pattern 原本格式定義的字串
 * @return 傳回 float 型態的數值
 */    
    public float parseFloat(String priceString, String pattern){
        try{
            Number parseNumber;
            parseNumber = parseNumber(priceString, pattern);
            return parseNumber.floatValue();
        }
        catch(Exception e){
            return 0;
        }
    }
    //
/** <PRE> 將已被格式字串中的數值取出，傳回 Number 型態的數值
 * (使用物件本身目前設定的格式解譯)</PRE>
 * @param priceString 原本的 Price 格式的字串
 * @return 傳回 Number 型態的數值
 */    
    public Number parseNumber(String priceString){
        try{
            return parseNumber(priceString,pricePattern);
        }
        catch(Exception e){
            return null;
        }
    }
    //
/** <PRE> 將已被格式字串中的數值取出，傳回 int 型態的數值
 * (使用物件本身目前設定的格式解譯)</PRE>
 * @param priceString 原本的 Price 格式的字串
 * @return 傳回 int 型態的數值
 */    
    public int parseInt(String priceString){
        try{
            return parseInt(priceString, pricePattern);
        }
        catch(Exception e){
            return 0;
        }
    }
    //
/** <PRE> 將已被格式字串中的數值取出，傳回 double 型態的數值
 * (使用物件本身目前設定的格式解譯)</PRE>
 * @param priceString 原本的 Price 格式的字串
 * @return 傳回 double 型態的數值
 */    
    public double parseDouble(String priceString){
        try{
            return parseDouble(priceString, pricePattern);
        }
        catch(Exception e){
            return 0;
        }
    }
    //
/** <PRE> 將已被格式字串中的數值取出，傳回 float 型態的數值
 * (使用物件本身目前設定的格式解譯)</PRE>
 * @param priceString 原本的 Price 格式的字串
 * @return 傳回 float 型態的數值
 */    
    public float parseFloat(String priceString){
        try{
            return parseFloat(priceString ,pricePattern);
        }
        catch(Exception e){
            return 0;
        }
    }


}
