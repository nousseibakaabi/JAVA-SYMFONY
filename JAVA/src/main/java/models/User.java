package models;

import java.util.Arrays;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private int id;
    private String roles;
    private String email;
    private String address;
    private String password;
    private String name;
    private String question;
    private String answer;
    private String status = "Active";

    public User(String roles, String name, String email, String password, String address, String question, String answer, String status) {
        this.roles = roles;
        this.name = name;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.address = address;
        this.question = question;
        this.answer = answer;
        this.status = status;
    }

    public User(String name, String address, String password, String question, String answer, String status) {
        this.name = name;
        this.address = address;
        this.password = password;
        this.question = question;
        this.answer = answer;
        this.status = status;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoles() {
        return roles;
    }

    // Update the setRoles method to accept a String array
    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roles=" + roles +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z]+$");
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidAddress(String address) {
        return address.matches("^[a-zA-Z0-9\\s,]+$");
    }

    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$") && password.length() >= 6;
    }

    public void hashPassword() {
        this.password = hashFunction(this.password);
    }

    private String hashFunction(String password) {
        return password;  // Replace with actual hashing logic
    }
}
