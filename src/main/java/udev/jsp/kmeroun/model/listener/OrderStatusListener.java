package udev.jsp.kmeroun.model.listener;

import udev.jsp.kmeroun.model.event.OrderStatusChangedEvent;

import java.util.EventListener;

public interface OrderStatusListener extends EventListener {
    void orderStatusChanged(OrderStatusChangedEvent event);

}
