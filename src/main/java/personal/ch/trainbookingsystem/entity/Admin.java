package personal.ch.trainbookingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Admin {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String idNumber;
    private String phone;

    public Admin() {
    }

    public Admin(String username, String password, String name, String idNumber, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.idNumber = idNumber;
        this.phone = phone;
    }

    public Admin(Long id, String username, String password, String name, String idNumber, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.idNumber = idNumber;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
