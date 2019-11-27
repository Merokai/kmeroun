package udev.jsp.kmeroun.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "products")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Product implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private int id;


    @Column(name = "product_name", unique = true, nullable = false)
    private String name;

    @Column(name="product_price", nullable = false)
    private double price;

    public Product(){}

    public Product(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}
