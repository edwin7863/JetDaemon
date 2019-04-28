/*
 * JMail.java
 *
 * Created on 2000�~12��20��, �U�� 1:55
 */

package app.gian.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import app.gian.DbName;
import app.gian.DbType;
import app.gian.DbUtil;
import app.gian.db.MisDataBaseImp;
import app.gian.db.informix.model.CixclosebfModel;
import app.gian.delegate.DataProcDelegate;






public class MailUtil extends Object 
{
	private static String DB_TYPE = DbType.BIG5_FORMAL;	
	private static String DB_NAME = DbName.DB_BWS;
	
	
	
	
	


	
    private String to;
    private String from;
    private String subject;
    private String content;
    private String smtp;
    private MimeMessage msg;
    private Properties props = new Properties();
    private Multipart mp = new MimeMultipart();
	//private String HOST = "10.36.1.26";
	private String HOST = "TPENOTES1.yosungroup.com";
	
	
    /** �إߤ@�� JMail ���� */
    public MailUtil() {
        super();
    }
    /** �إߤ@�� JMail ����ó]�w To, From, Subject, Content, SMTP */
    public MailUtil(String To, String From, String Subject, String Content, String Smtp){
        to = To;
        from = From;
        subject = Subject;
        content = Content;
        smtp = Smtp;
        setHost(Smtp);
        setFrom(From);
        setTo(To);    
        setSubject(Subject);
        setContent(Content);
    }
    /** �إߤ@�� JMail ����ó]�w To, From, Subject, Content, SMTP */
    public MailUtil(String To[], String From, String Subject, String Content, String Smtp){
        //to = To;
        from = From;
        subject = Subject;
        content = Content;
        smtp = Smtp;
        setHost(Smtp);
        setFrom(From);
        setTo(To);    
        setSubject(Subject);
        setContent(Content);
    }    
    /** �]�w�~�H�l��D�� */
    public void setHost(String Host){
        try{ 
            props.put("mail.smtp.host",Host);
            Session session = Session.getDefaultInstance(props,null);
            msg = new MimeMessage(session);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    /** �]�w�H��� */
    public void setFrom(String From){
        try{
            msg.setFrom(new InternetAddress(From));
        }
        catch(Exception e){
            e.toString();
        }
    }
    /** �]�w���H�� */
    public void setTo(String To){
        try{
            InternetAddress[] address = {new InternetAddress(To)};
            msg.setRecipients(Message.RecipientType.TO, address);
        }
        catch(Exception e){
            e.toString();
        }
    }
    /** �]�w���H�� */
	public void setTo(String To[])
	{
		 try
		 {
			 int size = 0;
			 int count = 0;

			 size=To.length;
			 InternetAddress address[] = new InternetAddress[size];
			 for(count=0 ; count <size ; count++)
			 {
				 address[count] = new InternetAddress(To[count]);
			 }
			 msg.setRecipients(Message.RecipientType.TO, address);
		 }
		 catch(Exception e)
		 {
			 System.out.println(e.toString());
		 }
	 }
    /** �]�w�l��D�D */
    public void setSubject(String Subject){
        try{
            msg.setSubject(Subject,"big5");
        }
        catch(Exception e){
            e.toString();
        }
    }
    /** �]�w�l�󤺮e */
    public void setContent(String msg){
        try{
            //MimeBodyPart Mcont1 = new MimeBodyPart();
            //Mcont1.setContent(Msg,"text/plain;charset=\"big5\"");
            
            MimeBodyPart Mcont = new MimeBodyPart();
            Mcont.setContent(msg,"text/html;charset=\"big5\"");
            mp.addBodyPart(Mcont);
        }
        catch(Exception e){
            e.toString();
        }
    }
    /** �[�J���e�ɮ� */
    public void attachFile(String filename)
    {
        try
        {
            MimeBodyPart mbpf = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(filename);
            mbpf.setDataHandler(new DataHandler(fds));
            mbpf.setFileName(fds.getName());
            mp.addBodyPart(mbpf);
        }
        catch(Exception e)
        {
            e.toString();
        }
    }
    
	public void setBcc(String Bcc)
	{
		try
		{
			InternetAddress address[] = {
				new InternetAddress(Bcc)
			};
			msg.setRecipients(javax.mail.Message.RecipientType.BCC, address);
		}
		catch(Exception e)
		{
			System.out.println("JMail.setBcc error : " + e.toString());
		}
	}

	public void setBcc(String Bcc[])
	{
		try
		{
			int size = 0;
			int count = 0;
			size = Bcc.length;
			InternetAddress address[] = new InternetAddress[size];
			for(count = 0; count < size; count++)
				address[count] = new InternetAddress(Bcc[count]);

			msg.setRecipients(javax.mail.Message.RecipientType.BCC, address);
		}
		catch(Exception e)
		{
			System.out.println("JMail.setBcc[] error : " + e.toString());
		}
	}


    /** �H�oE-mail 
        �p�G���\�A�Ǧ^ 0 �A���ѫh�Ǧ^ -1 */
    public int sendMail(){
        try{
            msg.setContent(mp);
            msg.setSentDate(new Date());
            Transport.send(msg);
            return 0;
        }
        catch(Exception e){
            System.out.println(e.toString());
            return -1;
        }
    }
    
    
	public void sendMailWithAttachment(String[] To,String sysName ,String fileName)
	{
		String from="System Admin<endra_lee@sertek.com.tw>";
		StringBuffer subject = new StringBuffer();
		StringBuffer content = new StringBuffer();
		subject.append(sysName+"�G");
		content.append(sysName+"�G<br>");
		this.setHost(HOST);  
		this.setFrom(from);
		this.setTo(To);
		//this.setBcc("endra_lee@sertek.com.tw");
		this.setSubject(subject.toString());
		this.setContent(content.toString());
		this.attachFile(fileName);
		try
		{
			if (this.sendMail() == 0)
			{
				System.out.println("Sending mail exception successfully!!");
			}
			else
			{
				System.out.println("Sending mail exception failed!!");
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void sendMailWithContentList(String[] To,String sysName ,List list)
	{
		String from="System Admin<endra_lee@sertek.com.tw>";
		StringBuffer subject = new StringBuffer();
		StringBuffer content = new StringBuffer();
		subject.append(sysName+"�t�ΰT���G");
		content.append(sysName+"�t�ΰT���G<br>");
		Iterator itr = list.iterator();
		while(itr.hasNext())
		{
			String cntl = (String) itr.next();
			content.append(cntl).append("<br>");
		}
		this.setHost(HOST);  
		this.setFrom(from);
		this.setTo(To);
		this.setSubject(subject.toString());
		this.setContent(content.toString());
		try
		{
			if (this.sendMail() == 0)
			{
				System.out.println("Sending mail exception successfully!!");
			}
			else
			{
				System.out.println("Sending mail exception failed!!");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	private String today(boolean timeflag) {
		java.util.Date javadate = new java.util.Date();
		if(timeflag){
			java.sql.Timestamp timestamp = new java.sql.Timestamp(javadate.getTime());
			return timestamp.toString().substring(0, timestamp.toString().indexOf("."));
		}
		else{
			java.sql.Date date = new java.sql.Date(javadate.getTime());
			return date.toString();
		}
	}
	
	
	

}
