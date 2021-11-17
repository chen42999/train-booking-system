package personal.ch.trainbookingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Long id;

    private String username;
    private String password;
    private String name;
    private String idNumber;
    private String phone;
    private Long ticketLimit;

    private Long roleId;
    private Role role;

    public Users(String username, String password, String name, String idNumber, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.idNumber = idNumber;
        this.phone = phone;
    }
}
