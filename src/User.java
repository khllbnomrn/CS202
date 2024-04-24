import java.util.ArrayList;
import java.sql.*;

public class User {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String username;
    private int id;
    private String password;
    private ArrayList<Integer> contactsID;

    private Boolean activeStatus=false;

    public User(){}
    public User(String fullName, String phoneNumber, String email, String username,String password) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password=password;
    }

    public User(int id, String fullName, String password,String email, String username,String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.id = id;
        this.password=password;
    }


    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getContactsID() {

        return contactsID;
    }

    public void addContact(int contactID) {
        contactsID.add(contactID);
    }

    public void deleteContact(int contactID) {
        contactsID.remove(contactID);
    }

    public void updateInfo(String fullName, String phoneNumber, String email, String username,String password) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password=password;
    }



    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", id=" + id +
                '}';
    }
}
