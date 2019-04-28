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

/** <PRE> PriceFormat����O�@�ӱN�ƭȳ]�w�������w�榡���r��B�z����A
 * ��]�t�N�榡�r����ন�ƭȪ�method</PRE>
 *
 * @author Edward
 * @version 1.0.0
 */
public class PriceFormat extends Object {
    
/** <PRE> �]�w Price �榡�� Default
 * �榡�� ##,###,###.### �t�p���I��T��</PRE>
 */    
    public static String DEFAULT = "##,###,##0.0000";
/** <PRE>�]�wPrice�榡����ƫ��A�榡�A�榡�� ##,###,###</PRE>
 */    
    public static String INTEGER_FORMAT = "##,###,###";
/** <PRE> �]�wPrice�榡�������榡�A�榡�� $ ##,###,###.##
 * �Ʀr�]�t�p���I�U����</PRE>
 */    
    public static String US_DOLLARS_FORAMT = "$ ##,###,##0.00";
/** <PRE> �]�wPrice�榡���s�x���榡�A�榡�� NT$ ##,###,###
 * ���]�t�p���I�H�U���</PRE>
 */    
    public static String NEW_TAIWAN_CURRENCY_FORMAT = "NT$ ##,###,###";
    
    private String pricePattern;
    private DecimalFormat processFormat;
    /** �إߤ@�� PriceFormat ����
 */
    public PriceFormat() {
        super();
        pricePattern = DEFAULT;
    }
    //
/** �إߤ@��PriceFormat����A�ۭqPrice�榡
 * @param priceFormat ���]�w���榡�r��
 */    
    public PriceFormat(String priceFormat) {
        pricePattern = priceFormat;
    }
    //
/** <PRE> �]�w Price �榡</PRE>
 * @param pattern �榡�r��
 */    
    public void setPricePattern(String pattern){
        pricePattern = pattern;        
    }
    //
/** <PRE> �N��J���ƭ�(double ���A)�A�H�]�w���榡�ƦC�A�ǥX�զX�᪺�r��</PRE>
 * @param priceValue �ǤJ���ƭ�
 * @param pattern �榡�r��
 * @return �Ǧ^�ھګ��w�榡�]�w���r��
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
/** <PRE> �N��J���ƭ�(double ���A)�A�H�]�w���榡�ƦC�A�ǥX�զX�᪺�r��</PRE>
 * @param priceValue �ǤJ���ƭ�
 * @return �Ǧ^�ھګ��w�榡�]�w���r��
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
/** <PRE> �N��J���ƭ�(long ���A)�A�H�]�w���榡�ƦC�A�ǥX�զX�᪺�r��</PRE>
 * @param priceValue �ǤJ���ƭ�
 * @param pattern �榡�r��
 * @return �Ǧ^�ھګ��w�榡�]�w���r��
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
/** <PRE> �N��J���ƭ�(double ���A)�A�H�]�w���榡�ƦC�A�ǥX�զX�᪺�r��</PRE>
 * @param priceValue �ǤJ���ƭ�
 * @return �Ǧ^�ھګ��w�榡�]�w���r��
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
/** <PRE> �N�즳�榡���r���ഫ���ثe�]�w�� Price �榡</PRE>
 * @param priceValue �쥻�� Price �榡���r��
 * @param pattern �쥻�榡�w�q���r��
 * @return �Ǧ^�ھڥثe���w�榡�]�w���r��
 */    
    public String format(String priceValue, String pattern){
        try{
            return format(parseDouble(priceValue, pattern), pricePattern);
        }
        catch(Exception e){
            return null;
        }
    }

/** <PRE> �ƭȦr���ഫ���ثe�]�w�� Price �榡</PRE>
 * @param priceValue �ƭȦr��
 * @return �Ǧ^�ھڥثe���w�榡�]�w���r��
 */    
    public String format(String priceValue){
        try{
            return format(parseDouble(priceValue), pricePattern);
        }
        catch(Exception e){
            return null;
        }
    } 
/** <PRE> �N��J���ƭ�(Double ���A)�A�H�]�w���榡�ƦC�A�ǥX�զX�᪺�r��</PRE>
 * @param priceValue �ǤJDouble���A���ƭ�
 * @param pattern ���w��X���榡
 * @return �Ǧ^�@���w�榡�� Price �r��
 */    
    public String format(Double priceValue, String pattern){
        try{
            return format(priceValue.doubleValue(), pattern);
        }
        catch(Exception e){
            return null;
        }
    }
/** <PRE> �N��J���ƭ�(Double ���A)�A�H�]�w���榡�ƦC�A�ǥX�զX�᪺�r��</PRE>
 * @param priceValue ��J Double ���A���ƭ�
 * @return �Ǧ^�@���w�榡�� Price �r��
 */    
    public String format(Double priceValue){
        try{
            return format(priceValue.doubleValue());
        }
        catch(Exception e){
            return null;
        }
    }
/** <PRE> �N��J���ƭ�(BigDecimal ���A)�A�H�]�w���榡�ƦC�A�ǥX�զX�᪺�r��</PRE>
 * @param priceValue �ǤJ BigDecimal ���A���ƭ�
 * @param pattern ���w��X���榡
 * @return �Ǧ^�@���w�榡�� Price �r��
 */    
    public String format(BigDecimal priceValue, String pattern){
        try{
            return format(priceValue.doubleValue(), pattern);
        }
        catch(Exception e){
            return null;
        }
    }
/** <PRE> �N��J���ƭ�(BigDecimal ���A)�A�H�]�w���榡�ƦC�A�ǥX�զX�᪺�r��</PRE>
 * @param priceValue �ǤJ BigDecimal ���A���ƭ�
 * @return �Ǧ^�@���w�榡�� Price �r��
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
/** <PRE> �N�w�Q�榡�r�ꤤ���ƭȨ��X�A�Ǧ^ Number ���A���ƭ�</PRE>
 * @param priceString �쥻�� Price �榡���r��
 * @param pattern �쥻�榡�w�q���r��
 * @return �Ǧ^ Number ���A���ƭ�
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
/** <PRE> �N�w�Q�榡�r�ꤤ���ƭȨ��X�A�Ǧ^ int ���A���ƭ�</PRE>
 * @param priceString �쥻�� Price �榡���r��
 * @param pattern �쥻�榡�w�q���r��
 * @return �Ǧ^ int ���A���ƭ�
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
/** <PRE> �N�w�Q�榡�r�ꤤ���ƭȨ��X�A�Ǧ^ double ���A���ƭ�</PRE>
 * @param priceString �쥻�� Price �榡���r��
 * @param pattern �쥻�榡�w�q���r��
 * @return �Ǧ^ int ���A���ƭ�
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
/** <PRE> �N�w�Q�榡�r�ꤤ���ƭȨ��X�A�Ǧ^ float ���A���ƭ�</PRE>
 * @param priceString �쥻�� Price �榡���r��
 * @param pattern �쥻�榡�w�q���r��
 * @return �Ǧ^ float ���A���ƭ�
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
/** <PRE> �N�w�Q�榡�r�ꤤ���ƭȨ��X�A�Ǧ^ Number ���A���ƭ�
 * (�ϥΪ��󥻨��ثe�]�w���榡��Ķ)</PRE>
 * @param priceString �쥻�� Price �榡���r��
 * @return �Ǧ^ Number ���A���ƭ�
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
/** <PRE> �N�w�Q�榡�r�ꤤ���ƭȨ��X�A�Ǧ^ int ���A���ƭ�
 * (�ϥΪ��󥻨��ثe�]�w���榡��Ķ)</PRE>
 * @param priceString �쥻�� Price �榡���r��
 * @return �Ǧ^ int ���A���ƭ�
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
/** <PRE> �N�w�Q�榡�r�ꤤ���ƭȨ��X�A�Ǧ^ double ���A���ƭ�
 * (�ϥΪ��󥻨��ثe�]�w���榡��Ķ)</PRE>
 * @param priceString �쥻�� Price �榡���r��
 * @return �Ǧ^ double ���A���ƭ�
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
/** <PRE> �N�w�Q�榡�r�ꤤ���ƭȨ��X�A�Ǧ^ float ���A���ƭ�
 * (�ϥΪ��󥻨��ثe�]�w���榡��Ķ)</PRE>
 * @param priceString �쥻�� Price �榡���r��
 * @return �Ǧ^ float ���A���ƭ�
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
