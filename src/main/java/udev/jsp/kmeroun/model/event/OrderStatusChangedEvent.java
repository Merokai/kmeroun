package udev.jsp.kmeroun.model.event;

import udev.jsp.kmeroun.model.OrderStatus;

import java.util.EventObject;

public class OrderStatusChangedEvent extends EventObject {

    private OrderStatus newStatus;

    public OrderStatusChangedEvent(Object source, OrderStatus newStatus) {
        super(source);

        this.newStatus = newStatus;
    }

    public OrderStatus getNewStatus(){
        return this.newStatus;
    }
}
