package udev.jsp.kmeroun.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="dishes")
public class Dish implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id", unique = true)
    private int id;

    @Column(name="name", unique = true)
    private String name;

    @Column(name="price")
    private double price;

    public Dish(){}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public static Dish fromString(String json) throws JsonProcessingException {
        return JacksonObjectMapper.getInstance().readValue(json, Dish.class);
    }
}
