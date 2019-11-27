package udev.jsp.kmeroun.models;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;
import udev.jsp.kmeroun.utils.PasswordHash;

import javax.annotation.Generated;
import javax.persistence.*;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@Entity
@Table(name="users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "user_unique_name", unique = true, nullable = false)
    private String username;

    @Column(name = "user_password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "user_lastname", nullable = false)
    private String lastname;

    @Column(name = "user_firstname", nullable = false)
    private String firstname;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;

    public User(){}

    public User(String username, String password, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        try {
            this.password = PasswordHash.getInstance().generateHashedPassword(password);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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
