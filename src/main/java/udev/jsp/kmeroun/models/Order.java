package udev.jsp.kmeroun.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.ValueGenerationType;
import org.hibernate.tuple.ValueGenerator;
import udev.jsp.kmeroun.enums.OrderStatus;
import udev.jsp.kmeroun.events.OrderStatusChangedEvent;
import udev.jsp.kmeroun.listeners.OrderStatusListener;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;
import udev.jsp.kmeroun.utils.SerializableArrayList;

import javax.persistence.*;
import javax.swing.event.EventListenerList;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="orders")
public class Order implements Serializable {

    @Id
    @Column(name = "id")
    @JsonIgnore
    private int id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customer_username")
    private User customer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderRow> orderRowList = new SerializableArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "createdOn")
    private LocalDate createdOn;

    @JsonIgnore
    @Transient
    private EventListenerList listeners;

    public Order(){
        listeners = new EventListenerList();
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getCustomer() {
        return customer;
    }

    public void setOrderRowList(List<OrderRow> orderRowList) {
        this.orderRowList = orderRowList;
    }

    public List<OrderRow> getOrderRowList() {
        return orderRowList;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        fireOrderStatusChanged();
    }
    public OrderStatus getStatus() {
        return status;
    }

    public void addListener(OrderStatusListener listener) {
        this.listeners.add(OrderStatusListener.class, listener);
    }

    public void removeListener(OrderStatusListener listener) {
        this.listeners.remove(OrderStatusListener.class, listener);
    }

    private void fireOrderStatusChanged(){
        for(OrderStatusListener listener : listeners.getListeners(OrderStatusListener.class)){
            listener.orderStatusChanged(new OrderStatusChangedEvent(this, getStatus()));
        }
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

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
