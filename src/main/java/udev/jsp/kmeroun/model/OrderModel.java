package udev.jsp.kmeroun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import udev.jsp.kmeroun.model.event.OrderStatusChangedEvent;
import udev.jsp.kmeroun.model.listener.OrderStatusListener;

import javax.swing.event.EventListenerList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderModel{
    private UserModel client;
    private List<DishModel> dishModels;
    private OrderStatus status;
    private LocalDate orderDate;

    @JsonIgnore
    private EventListenerList listeners;


    public OrderModel(){
        this(null, new ArrayList<DishModel>(), OrderStatus.CREATION);
    }

    public OrderModel(UserModel client, List<DishModel> dishModels, OrderStatus status){
        this.client = client;
        this.dishModels = dishModels;
        this.status = status;
        this.listeners = new EventListenerList();
    }
    public void setClient(UserModel client) {
        this.client = client;
    }

    public UserModel getClient() {
        return client;
    }

    public void setDishModels(List<DishModel> dishModels) {
        this.dishModels = dishModels;
    }

    public List<DishModel> getDishModels() {
        return dishModels;
    }

    public void setStatus(OrderStatus statut) {
        this.status = statut;
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
}
