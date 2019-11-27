package udev.jsp.kmeroun.models;

import java.time.LocalDate;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import udev.jsp.kmeroun.enums.OrderStatus;

import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.persistence.*;

@Table(name = "orders")
@Entity
public class Order implements Serializable {

    @Id
    @JsonIgnore
    @Column(name = "order_id")
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus status;

    @Column(name = "order_creation", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdOn;

    public Order(){
        super();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
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

    public static Order fromString(String json) throws JsonProcessingException {
        return JacksonObjectMapper.getInstance().readValue(json, Order.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
