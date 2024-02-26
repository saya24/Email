package spt.mail.send;

import java.io.File;
import java.util.Properties;

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

		String message = "";
		String subject = "Ta-da!";
		String to = "sariya24194@gmail.com";
		String from = "scoupira@gmail.com";

		sendAttach(message,subject,to,from);
	}

	private static void sendAttach(String message, String subject, String to, String from) {

		String host="smtp.gmail.com";

		Properties properties = System.getProperties();
		System.out.println("PROPERTIES "+properties);

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");

		Session session=Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {				
				return new PasswordAuthentication("scoumira@gmail.com", "**********");
			}

		});
		
		session.setDebug(true);

		MimeMessage msg = new MimeMessage(session);
		
		try {

		msg.setFrom(from);

		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		msg.setSubject(subject);

		String path="C:\\Users\\46761\\OneDrive\\Desktop\\pic.jpg";

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
			
			e.printStackTrace();
		}

		msg.setContent(mimeMultipart);

		Transport.send(msg);

		}catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
