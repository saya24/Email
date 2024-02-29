package spt.mail.send;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class App {
	public static void main(String[] args) {		
		while(true) {		
			Scanner sc= new Scanner(System.in);
	
			System.out.print("Enter message: ");
			String message = sc.nextLine();
			
			System.out.println("Enter subject: ");
			String subject = sc.nextLine();
							
			System.out.print("Do you want to send an attachment Y/N? ");
			String answer = sc.nextLine();
			String path = "";
			
			//only handles attachments if user enters Y
			if(answer.equals("Y")) {				
				System.out.print("Enter filepath for attachment:  ");
				path= sc.nextLine();
			}		
			final String path2 = path;
			
			boolean go = true;
	        List<String> toEmails = new ArrayList<String>();
	        while(go) {
	            System.out.println("Enter to email, 'stop' to stop: ");
	            String to = sc.nextLine();
	            if(to.equals("stop")) {
	                go = false;
	            }
	            else {
	                toEmails.add(to);
	            }
	        }

			System.out.println("Enter from email: ");
			String from = sc.nextLine();
			
			System.out.println("Enter password: ");
			String password = sc.nextLine();
			
			Calendar c = Calendar.getInstance();
			
			System.out.print("Enter hour: ");  
			int hour= sc.nextInt(); 
			
			System.out.print("Enter minute: ");  
			int minute= sc.nextInt();  

			c.set(Calendar.HOUR_OF_DAY, hour);
			c.set(Calendar.MINUTE, minute);
			c.set(Calendar.SECOND, 00);			
			System.out.println("Email schedule for: " +hour +":" +minute);
			
			//create new thread to call sendEmail function at the mentioned time
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
			    @Override
			    public void run() {		
			    	for(String to : toEmails)
	                {
	                    sendEmail(message,subject,to,from,password, path2);
	                }
				}
			}, c.getTime(), 86400000);				
		}
	}

	private static void sendEmail(String message, String subject, String to, String from, String password, String path) {
		//sets the properties
		String host="smtp.gmail.com";	
		Properties properties = System.getProperties();	
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		//creates a password authentication object based on the email and password
		Session session=Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {		
				return new PasswordAuthentication(from, password);
			}	
		});
		
		//removes all the comments in the console
		session.setDebug(false);	
		MimeMessage msg = new MimeMessage(session);
		
		try {
		msg.setFrom(from);
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setSubject(subject);			
		msg.setText(message);								
		
		//if there's no file path or the user didn't choose to send attachment
		//then it skips the attachment part
		if(!path.equals("")) {
			try {
				MimeMultipart mm = setAttachmentToEmail(msg, message, path);
				msg.setContent(mm);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}	
		Transport.send(msg);
		System.out.println("Email sent.");
		
		}catch (Exception e) {
			System.out.println(e.toString());
			System.out.println("The email could not be sent.");
		}	
	}
	
	//handles attachment if user entered the path
	//returns a MimeMultipart object 
	private static MimeMultipart setAttachmentToEmail(MimeMessage msg, String message, String path) {	
			MimeMultipart mimeMultipart = new MimeMultipart();	
			MimeBodyPart textMime = new MimeBodyPart();			
			MimeBodyPart fileMime = new MimeBodyPart();
			
			try {				
				textMime.setText(message);
				
				File file=new File(path);
				fileMime.attachFile(file);
								
				mimeMultipart.addBodyPart(textMime);
				mimeMultipart.addBodyPart(fileMime);
	
			} catch (Exception e) {				
				System.out.println(e.toString());
				System.out.println("Attachment Error.");
			}
		return mimeMultipart;	
	}
}