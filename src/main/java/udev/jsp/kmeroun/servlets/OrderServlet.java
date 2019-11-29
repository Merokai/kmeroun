package udev.jsp.kmeroun.servlets;

import javassist.NotFoundException;
import udev.jsp.kmeroun.dao.CartDao;
import udev.jsp.kmeroun.dao.OrderDao;
import udev.jsp.kmeroun.enums.OrderStatus;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Cart;
import udev.jsp.kmeroun.models.Order;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.UserHasRole;
import udev.jsp.kmeroun.utils.HttpBodyParser;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private static final OrderDao orderDao = new OrderDao();
    private static final CartDao cartDao = new CartDao();

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
        try {
            Order order = new Order();

            order.setCreatedOn(LocalDateTime.now());
            order.setStatus(OrderStatus.PREPARATION);
            order.setCart(cartDao.findCurrent((User) req.getSession().getAttribute("user")));
            order.getCart().setOrder(order);
            orderDao.save(order);

        } catch(NoResultException e){
            res.sendError(404);
        }

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
        // TODO: cancelOrder (PREPARATION -> BUYER_CANCELLED)
    }
}
