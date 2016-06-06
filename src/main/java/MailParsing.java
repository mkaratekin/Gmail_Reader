import javax.mail.*;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;



public class MailParsing {

	DBConnection conn;

	ArrayList<Person> prsList;

	Message[] msg;

	String[] contents;

	String[] items = { "SYT", "2. proje" }; //Maillerin aranan mailler olduğunu saptamamız icin gerekli keywordler
        Mail mail;

	public MailParsing(Message[] msg, DBConnection conn) // Constructor
	{

		this.msg = msg;
		contents = new String[msg.length];
		this.conn = conn;
                mail=new Mail();
	}
        
        //Gmailden alınan mailleri kontrol ederek DB ye kaydeder.
	public void parseMail() throws MessagingException, IOException, ParseException {

		for (int i = 0; i < msg.length; i++) {
			
			Address[] in = msg[i].getFrom();

			for (Address address : in) {
				System.out.println("FROM:" + address.toString());

			}

			Multipart mp = (Multipart) msg[i].getContent();
			BodyPart bp = mp.getBodyPart(0);
			System.out.println("SEND DATE:" + msg[i].getSentDate());
			System.out.println("SUBJECT" + msg[i].getSubject());

			contents[i] = (String) bp.getContent();
			System.out.println("CONTENT:" + bp.getContent());

			if (controlMail(contents[i], items) == true) {
				prsList = findPerson(contents[i]);
				conn.savePersons(prsList);//Kişiler db ye kaydedilir.
                                mail.sendMail(in[0].toString());//Kişi db ye kaydedildikten sonra okundu maili yollanır.
				conn.updateGroupNo(conn.getGroupNo());
			
				

			} else
				System.out.println("calismadi");

		}
        
		conn.closeDB();
	}
        //Mailin icerdigi kelimelere göre arma yapılır ve dogru mail olup olmadıgı belirlenir.
	public static boolean controlMail(String content, String[] items) {
		int counter = 0;

		for (int i = 0; i < items.length; i++) {
			if (content.contains(items[i]))

			{
				counter++;

			}

		}

		if (counter == items.length) {
			return true;
		} else
			return false;

	}
        //Maildeki grup üyelerini parse ederek bir Person listesine atıyor.
	public ArrayList<Person> findPerson(String content) throws ParseException {
		ArrayList<Person> personList = new ArrayList<Person>();
		
		String[] rowsInMail = content.split("\n");
		int groupNo = conn.getGroupNo();//DB den en son indexlenmis grup numarasını alır
		groupNo++;//Grup numarası 1 artırılarak ardısık bir grupNo elde edilmiş olur.
		long gecici;
        String temp;
		for (int i = 2; i < rowsInMail.length; i++) {//mailde 3. satırdan itibaren kişileri alıp parse ediyorç
			Person person = new Person();
			String[] noAndName = rowsInMail[i].split("-");
			String[] nameAndSurname = noAndName[1].split("\\s+");
			temp=noAndName[0].trim().toString();
			
			gecici=Long.parseLong(temp);
			
			person.setNo(gecici);
			person.setname(nameAndSurname[0].trim());
			person.setSurname(nameAndSurname[1].trim());
			person.setGroupNo(groupNo);
			personList.add(person);

		}

		return personList;

	}

}
