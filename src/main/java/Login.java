
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.json.simple.parser.ParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Duygu
 */

@ManagedBean(name="loginBean")
@SessionScoped

public class Login {

    private String username;
    private String password;
    DBConnection con=new DBConnection();
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
   

    public String login(String userName, String password) throws ParseException{
    
        boolean result=con.findUser(userName, password);
       
        if(result==true){ // kullanıcı bilgileri dogru girilmisse ana sayfayı 
            // yanlis girilmisse login_fail ekranını gosterir.
            
            return "home";
        }
        else{
        return "login_fail";
        }
    }
    
    public String register(String userName, String password){ // sisteme kullanıcı kaydı yapılır
        // kayıttan sonra kullanıcının sisteme girebilmesi için giriş ekranını dondurur.
        
        con.insertUser(userName, password);
        return "index";
    
    }
    
   
    
}
