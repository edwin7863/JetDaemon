/**
*   程式名稱 : B2Bi.java
*   撰寫日期 : 2002/08/16
*   撰寫人員 : Jason Chu
*   程式功能 : 處理B2Bi相關程式的共用method
*   參考XML  : 無
*   Modify log:
**/
package app.gian;

import java.io.*;
import java.util.Hashtable;
// 載入JAXP類別
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
// 載入DOM類別
import org.w3c.dom.*;
import org.xml.sax.SAXException;
// 載入資料轉換所需的類別
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

public class B2Bi extends Object
{
	public B2Bi(){}
	
	// 建立文件 by String fileName
	public static Document newDocument(String fileName)
	       throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		//System.out.println("Parsing Document fileName = "+fileName);
		Document newDoc = builder.parse(fileName);
		return newDoc;
	}
	
	// 建立文件 by File fileName
	public static Document newDocument(File fileName)
	       throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		System.out.println("Parsing Document fileName = "+fileName);
		Document newDoc = builder.parse(fileName);
		return newDoc;
	}
	
	// 建立空白文件
	public static Document newDocument()
	       throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		System.out.println("Create new Document...");
		Document newDoc = builder.newDocument();
		return newDoc;
	}
	
	// 儲存XML檔案函式 
	public static void save(Node node, Object out) 
		throws TransformerFactoryConfigurationError, 
		TransformerConfigurationException, Exception 
	{
		// 建立資料轉換的實體
		TransformerFactory factory = TransformerFactory.newInstance();
		// 建立轉換物件
		Transformer trans = factory.newTransformer();
		// 建立來源與目標物件
		Source source = new DOMSource(node);
		Result result = new StreamResult((File) out);
		//開始轉換並輸出至指定檔案, 並設定輸出編碼為Big5
		System.out.println("Transfer out XML file..."+out.toString());
		trans.setOutputProperty("encoding", "Big5");
		trans.transform(source, result);
	}
	
	
	//搬檔程式
	public static void moveFiles(File oldFile, File newFile) throws IOException
	{
		int rslt;
		FileWriter fw = null;
		FileReader fr = null;
		BufferedWriter bw = null;
		BufferedReader br = null;
	
		//FileInputStream fis = null;
		//FileOutputStream fos = null;
		try
		{
			//建立新檔
			if(!newFile.isFile())
			{
				if(!newFile.createNewFile())
				{
					throw new IOException("I can't Create the New File!");
				}
			}
			else
			{
				newFile.delete();
				if(!newFile.createNewFile())
				{
					System.out.println("kks");
					throw new IOException("I can't Create the New File!");
				}
			}
			
			if(!oldFile.isFile())
			{
				throw new IOException("I can't find the upload File!" + oldFile.toString());
			}
			
			bw = new BufferedWriter(new FileWriter(newFile));
			br = new BufferedReader(new FileReader(oldFile));
			//fis = new FileInputStream(oldFile);
			//fos = new FileOutputStream(newFile);
			
			//複製
			//while((rslt = fis.read()) != -1)
			while((rslt = br.read()) != -1)
			{
				//fos.write(rslt);
				bw.write(rslt);
			}
			//fos.write(-1);
			//fos.flush();
			//fis.close();
			//fos.close();
			bw.flush();
			br.close();
			bw.close();
			
			if(!oldFile.delete())
			{
				throw new IOException("I can't Delete the Old File!");
			}
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		finally
		{
			//if(fis != null) fis.close();
			//if(fos != null) fos.close();
			if(br != null) br.close();
			if(bw != null) bw.close();
		}
	}
	
	/**
	 * copy from com.weblink.util.StringUtil.dbEscape()
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
}