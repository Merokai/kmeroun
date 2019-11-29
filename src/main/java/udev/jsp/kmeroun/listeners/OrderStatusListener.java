package udev.jsp.kmeroun.listeners;

import udev.jsp.kmeroun.events.OrderStatusChangedEvent;

import java.util.EventListener;

public interface OrderStatusListener extends EventListener {
    void orderStatusChanged(OrderStatusChangedEvent event);

}
