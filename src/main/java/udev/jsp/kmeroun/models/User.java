package udev.jsp.kmeroun.models;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.annotations.NaturalId;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="users")
public class User implements Serializable {

    @Id
    @NaturalId
    @Column(name = "username", unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name="lastName")
    private String lastName;

    @Column(name="firstName")
    private String firstName;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;

    public User(){}

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    @Override
    public String toString() {
        String json = "{}";
        try{
            json = JacksonObjectMapper.getInstance().writeValueAsString(this);
        } catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return json;
    }

    public static User fromString(String json) throws JsonProcessingException {
        return JacksonObjectMapper.getInstance().readValue(json, User.class);
    }
}
