package udev.jsp.kmeroun.servlets;

import udev.jsp.kmeroun.dao.OrderDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Order;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.UserHasRole;
import udev.jsp.kmeroun.utils.HttpBodyParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private static final OrderDao orderDao = new OrderDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::confirmOrder, new UserHasRole(Role.CUSTOMER));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::getOrder, new UserHasRole(Role.CUSTOMER));
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::cancelOrder, new UserHasRole(Role.CUSTOMER));
    }

    private void confirmOrder(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Order order = Order.fromString(HttpBodyParser.parse(req));

        List<Order> userSavedOrders = orderDao.findCurrent((User) req.getSession().getAttribute("user"));
        for (Order userSavedOrder : userSavedOrders) {
            orderDao.delete(userSavedOrder);
        }
        orderDao.save(order);
    }

    private void getOrder(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        List userSavedOrders = orderDao.findCurrent((User) req.getSession().getAttribute("user"));
        if (userSavedOrders == null || userSavedOrders.size() == 0){
            res.sendError(404);
            return;
        }
        userSavedOrders.forEach((order) -> {
            System.out.println(order.toString());
        });
        req.setAttribute("order", (Order)userSavedOrders.get(0));
        req.getRequestDispatcher("WEB-INF/orderInfo.jsp").forward(req, res);
    }

    private void cancelOrder(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<Order> userSavedOrders = orderDao.findCurrent((User) req.getSession().getAttribute("user"));
        if(userSavedOrders.size() == 0){
            res.sendError(404);
            return;
        }
        orderDao.delete(userSavedOrders.get(0));
    }
}
