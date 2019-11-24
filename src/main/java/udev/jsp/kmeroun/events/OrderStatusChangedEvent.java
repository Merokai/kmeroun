package udev.jsp.kmeroun.events;

import udev.jsp.kmeroun.enums.OrderStatus;

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
