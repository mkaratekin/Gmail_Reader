import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
public class Mail {
    
    Properties props = new Properties();
    
	
	public Message [] ReadingEmail() // Acilmamis mailler d�nd�r�l�r
	{ 
		props.setProperty("mail.store.protocol","imaps");
		
		try
		{ Session session =Session.getInstance(props,null);
		  Store store= session.getStore();
		  store.connect("imap.gmail.com","sunucuyazilim2016@gmail.com","egebilmuh2016");
		  Folder inbox= store.getFolder("INBOX");
		  inbox.open(Folder.READ_WRITE);
		  Message []msg = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN),false));//Okunmamış mailleri çekiyor.
		  inbox.setFlags(msg, new Flags(Flags.Flag.SEEN), true);//Mesajları okunmuş olarak işaretliyor.
		  
		  System.out.println(msg.length);
		 
		  return msg;	  
	
		} 
		  catch (Exception mex)
		{
			mex.printStackTrace();
			return null;
		}
		
	  
	}
        
        public void sendMail(String toMessage)
        {
            props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
                
                Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("sunucuyazilim2016@gmail.com","egebilmuh2016");
			}
		  });
                
                try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("sunucuyazilim2016@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toMessage));
			message.setSubject("SYT Proje");
			message.setText("Merhaba\n"
				+ "Grup bilginiz listeye başarıyla kaydedilmiştir.");

			Transport.send(message);

			System.out.println("Mesaj Yollandı");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
        
        }
        
   

}
