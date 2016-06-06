
import java.util.*;
import javax.faces.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.bson.conversions.Bson;

@ManagedBean(name = "personBean")
@ViewScoped

public class personsDAO {

    public ArrayList<Person> personList;

    JSONParser jsonParser = new JSONParser();
    Person person;
    MongoCollection<Document> personCollection;

    public personsDAO() throws ParseException, MessagingException {
        
         
        Message[] msg;
        Mail mail = new Mail();
        DBConnection conn = new DBConnection();
        msg = mail.ReadingEmail();

        MailParsing mailprsng = new MailParsing(msg, conn);

        try {
            mailprsng.parseMail();
        } catch (IOException ex) {

        }
        
        MongoClient mongo = new MongoClient("localhost", 27017);

        MongoDatabase db = mongo.getDatabase("sunucuYazilim");

        personCollection = db.getCollection("persons");

        MongoCursor<Document> cursor = personCollection.find().iterator();

     
          personList = new ArrayList<Person>();
        try {
            while (cursor.hasNext()) { // Veri tabanindaki kisi bilgileri cekilerek listede tutulur.
                person = new Person();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(cursor.next().toJson());
                JSONObject jsonObject1 = (JSONObject) jsonParser.parse(jsonObject.get("no").toString());
                String temp = jsonObject1.get("$numberLong").toString();

                long no = Long.parseLong(temp.trim());
                person.setNo(no);
                String name = jsonObject.get("name").toString();
                person.setname(name);
                String surname = jsonObject.get("surname").toString();
                person.setSurname(surname);
                int groupNo = Integer.parseInt(jsonObject.get("groupNo").toString());
                person.setGroupNo(groupNo);
                int grade = Integer.parseInt(jsonObject.get("grade").toString());
                person.setGrade(grade);

                personList.add(person);

            }

        } finally {

            cursor.close();
        }

    }

    public ArrayList<Person> getpersonList() {

        return this.personList;
    }

    public void editAction(Person person) {
        person.setEditable(true);

    }

    public String saveAction(Person person) { // Kisilerin grup bilgilerinin guncellenmesini saglar

        if (person.isEditable() == true) {

            Bson filter = new Document("no", person.getNo());
            Bson newValue = new Document("groupNo", person.getGroupNo());
            Bson updateOprDocument = new Document("$set", newValue);
            personCollection.updateMany(filter, updateOprDocument);

            person.setEditable(false);
        }

        return null;

    }
 
    public String deleteAction(Person person) { // kisileri siler
        Bson deleteDoc = new Document("no", person.getNo());

        personCollection.deleteMany(deleteDoc);

        return "delete";
    }

   

    public String saveGradeAction(Person person) { // Not bilgilerinin sisteme kaydedilmesini saglar

        if (person.isEditable() == true) {

            Bson filter = new Document("no", person.getNo());
            Bson newValue = new Document("grade", person.getGrade());
            Bson updateOprDocument = new Document("$set", newValue);
            personCollection.updateMany(filter, updateOprDocument);

            person.setEditable(false);
        }

        return null;

    }

}
