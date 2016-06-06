import org.bson.Document;


import com.mongodb.*;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import org.bson.conversions.Bson;

import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

public class DBConnection {

	MongoClient mongo = new MongoClient("localhost", 27017);

	MongoDatabase db = mongo.getDatabase("sunucuYazilim");

	MongoCollection<Document> personCollection = db.getCollection("persons");// Grup kişilerini kaydedilği colloction

	MongoCollection<Document> groupIdCollection = db.getCollection("group"); // Otomatik olarak grup no bilgisini ç
                                                                                //çekmek için oluşturuldu
        
        MongoCollection<Document> userCollection=db.getCollection("users"); //  Uygulama kullanıcı bilgilerini tutar

	

	public int getGroupNo() throws ParseException { // Veritabanındaki o anki grup no bilgisini ceker
		int groupNo = 0;
		

		JSONParser jsonParser = new JSONParser();
		MongoCursor<Document> cursor = groupIdCollection.find().iterator();

		try {
			while (cursor.hasNext()) {

				JSONObject jsonObject = (JSONObject) jsonParser.parse(cursor.next().toJson());
				String no = jsonObject.get("groupCounter").toString();
				groupNo = Integer.parseInt(no);

			}

		} finally {

			cursor.close();
		}

		return groupNo;
	}

	public void updateGroupNo(int groupNo) { // mail geldiginde otomatik olarak grup noyu 
                                                //arttırmak için oluşturuldu 
		
		groupNo++;
		groupIdCollection.updateOne(eq("_id", "groupID"),
				new Document("$set", new Document("groupCounter", groupNo)));

	}

	public void savePersons(ArrayList<Person> prsList) // Grup kişilerini kaydeder

	{
		for (int i = 0; i < prsList.size(); i++) {
			Document person = new Document("no", prsList.get(i).getNo()).append("name", prsList.get(i).getName())
					.append("surname", prsList.get(i).getSurname()).append("groupNo", prsList.get(i).getGroupNo())
                                .append("grade",0);
                                
                                        
			personCollection.insertOne(person);

		}

	}

	

	public void closeDB() {

		mongo.close();

	}
        
      
        
        public void insertUser(String userName, String psw){ // uygulama kullanıcı bilgilerini kaydeder
        
                Document user=new Document("userName",userName)
                                          .append("password", psw);
                
                userCollection.insertOne(user);
        
        }
         
        public boolean findUser(String userName,String psw) throws ParseException{ // Sisteme giriş yapmak 
            //isteyen kullanıcıların bilgilerinin sistemde kayıtlı olup olmadığını kontrol eder.
            
            String dbUserName=null;
            String dbPassword=null;
            JSONParser jsonParser = new JSONParser();
            Bson searchQuery=new Document("userName",userName).append("password", psw);
            
           MongoCursor<Document> cursor= userCollection.find(searchQuery).iterator();
           System.out.print(cursor.toString());
           
           try {
			while (cursor.hasNext()) {
                                
				JSONObject jsonObject = (JSONObject) jsonParser.parse(cursor.next().toJson());
				dbUserName = jsonObject.get("userName").toString();
                                dbPassword=jsonObject.get("password").toString();
	

			}

		} finally {

			cursor.close();
		}
           
           if(userName.equals(dbUserName) && psw.equals(dbPassword)){
                return true;
           }
           else 
           {
               return false;
           }
            
            
        }
        
}
