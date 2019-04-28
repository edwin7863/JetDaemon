/**
*   �{���W�� : B2Bi.java
*   ���g��� : 2002/08/16
*   ���g�H�� : Jason Chu
*   �{���\�� : �B�zB2Bi�����{�����@��method
*   �Ѧ�XML  : �L
*   Modify log:
**/
package app.gian;

import java.io.*;
import java.util.Hashtable;
// ���JJAXP���O
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
// ���JDOM���O
import org.w3c.dom.*;
import org.xml.sax.SAXException;
// ���J����ഫ�һݪ����O
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

public class B2Bi extends Object
{
	public B2Bi(){}
	
	// �إߤ�� by String fileName
	public static Document newDocument(String fileName)
	       throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		//System.out.println("Parsing Document fileName = "+fileName);
		Document newDoc = builder.parse(fileName);
		return newDoc;
	}
	
	// �إߤ�� by File fileName
	public static Document newDocument(File fileName)
	       throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		System.out.println("Parsing Document fileName = "+fileName);
		Document newDoc = builder.parse(fileName);
		return newDoc;
	}
	
	// �إߪťդ��
	public static Document newDocument()
	       throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		System.out.println("Create new Document...");
		Document newDoc = builder.newDocument();
		return newDoc;
	}
	
	// �x�sXML�ɮר禡 
	public static void save(Node node, Object out) 
		throws TransformerFactoryConfigurationError, 
		TransformerConfigurationException, Exception 
	{
		// �إ߸���ഫ������
		TransformerFactory factory = TransformerFactory.newInstance();
		// �إ��ഫ����
		Transformer trans = factory.newTransformer();
		// �إߨӷ��P�ؼЪ���
		Source source = new DOMSource(node);
		Result result = new StreamResult((File) out);
		//�}�l�ഫ�ÿ�X�ܫ��w�ɮ�, �ó]�w��X�s�X��Big5
		System.out.println("Transfer out XML file..."+out.toString());
		trans.setOutputProperty("encoding", "Big5");
		trans.transform(source, result);
	}
	
	
	//�h�ɵ{��
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
			//�إ߷s��
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
			
			//�ƻs
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