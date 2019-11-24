package udev.jsp.kmeroun.servlets;

import udev.jsp.kmeroun.dao.OrderDao;
import udev.jsp.kmeroun.enums.OrderStatus;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Order;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.SerializableArrayList;
import udev.jsp.kmeroun.utils.UserRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private OrderDao orderDao = new OrderDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Confirm order (CREATION -> PREPARATION)
        UserRequest.requireRole(Role.CUSTOMER, request, response, (req, res) -> {
            User user = (User) req.getAttribute("user");
            Order order = orderDao.getCurrentOrder(user.getUsername());
            if(order == null){
                res.setStatus(404);
                return;
            }
            if(order.getStatus() != OrderStatus.CREATION){
                res.setStatus(409);
                return;
            }
            order.setStatus(OrderStatus.PREPARATION);
            orderDao.updateOrder(order);
            res.getWriter().append(order.toString());
            res.setStatus(200);
            res.setContentType("application/json");
        });
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Order in state : CREATION, PREPARATION, WAITING_DELIVERY
        UserRequest.requireRole(Role.CUSTOMER, request, response, (req, res) -> {
            User user = (User) req.getAttribute("user");
            /*
            SerializableArrayList<Order> orderList = orderDao.getOrders();
            orderList.removeIf( order -> {
                return !order.getClient().getUsername().equals(user.getUsername())
                        || order.getStatus() == OrderStatus.BUYER_CANCELLED
                        || order.getStatus() == OrderStatus.SELLER_CANCELLED
                        || order.getStatus() == OrderStatus.DELIVERED;
            });
            if(orderList.size() == 0){
                res.setStatus(404);
                return;
            }
            orderList.sort((a, b) -> {
                return b.getCreatedOn().compareTo(a.getCreatedOn());
            });
            */
            Order order = orderDao.getCurrentOrder(user.getUsername());
            if(order == null){
                res.setStatus(404);
                return;
            }
            res.getWriter().append(order.toString());
            res.setStatus(200);
            res.setContentType("application/json");
        });
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRequest.requireRole(Role.CUSTOMER, request, response, (_req, _res) -> UserRequest.requireJsonParameter(Order.class, _req, _res, (req, res) -> {
            User user = (User) req.getAttribute("user");
            Order order = (Order) req.getAttribute("jsonObject");
            order.setCustomer(user);
            order.setCreatedOn(LocalDate.now());
            order.setStatus(OrderStatus.CREATION);

            Order dbOrder = orderDao.getCurrentOrder(user.getUsername());
            if(dbOrder == null){
                orderDao.saveOrder(order);
            } else{
                order.setId(dbOrder.getId());
                orderDao.updateOrder(order);
            }
            res.getWriter().append(order.toString());
            res.setStatus(200);
            res.setContentType("application/json");
        }));
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Cancel order
        UserRequest.requireRole(Role.CUSTOMER, request, response, (req, res) -> {
            User user = (User) req.getAttribute("user");

            Order order = orderDao.getCurrentOrder(user.getUsername());
            orderDao.deleteOrder(order.getId());
            res.getWriter().append(order.toString());
            res.setStatus(200);
            res.setContentType("application/json");
        });
    }
}
