package udev.jsp.kmeroun.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="orderRows")
public class OrderRow implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    @JsonIgnore
    private int id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="dish_id")
    private Dish dish;

    @Column(name = "quantity")
    private int quantity;

    public OrderRow(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public static OrderRow fromString(String json) throws JsonProcessingException {
        return JacksonObjectMapper.getInstance().readValue(json, OrderRow.class);
    }
}
