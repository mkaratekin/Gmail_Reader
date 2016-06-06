
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "person")
@RequestScoped
public class Person { // Sisteme kayıtlı kişi bilgilerini tutar. 

    private long no;
    private String name;
    private String surname;
    private int groupNo;
    private int grade;
    boolean editable;

    
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
    
  
    public long getNo() {
        return no;
    }

    public void setNo(long value) {
        this.no = value;
    }

    public String getName() {
        return name;
    }

    public void setname(String value) {
        this.name = value;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String value) {
        this.surname = value;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int value) {
        this.groupNo = value;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
