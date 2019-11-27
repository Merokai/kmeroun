package udev.jsp.kmeroun.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.utils.SerializableArrayList;

import javax.persistence.*;

@Table(name = "carts")
@Entity
public class Cart implements Serializable {

    @Id
    @JsonIgnore
    @Column(name = "cart_id")
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customer_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User customer;

    @JsonIgnore
    @OneToOne(optional = true)
    @JoinColumn(name="order_id")
    private Order order;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name="cart_id")
    private List<CartItem> cartItems = new SerializableArrayList<>();;

    public Cart(){}

    public Cart(User user){
        this.customer = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
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

    public static Cart fromString(String json) throws JsonProcessingException {
        Cart cart = JacksonObjectMapper.getInstance().readValue(json, Cart.class);
        for (CartItem cartItem : cart.getCartItems()) {
            cartItem.setCart(cart);
        }
        return cart;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
