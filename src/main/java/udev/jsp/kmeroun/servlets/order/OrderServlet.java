package udev.jsp.kmeroun.servlets.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.dao.CartDao;
import udev.jsp.kmeroun.dao.OrderDao;
import udev.jsp.kmeroun.enums.OrderStatus;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Order;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;
import udev.jsp.kmeroun.utils.HttpBodyParser;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;
import udev.jsp.kmeroun.utils.SerializableArrayList;

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

    /**
     * POST /order
     * Confirm an {@link Order}
     * Require order object as Json body
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::confirmOrder, new UserHasRole(Role.CUSTOMER));
    }

    /**
     * GET /order
     * Get the current user {@link Order}s
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::getOrder, new UserHasRole(Role.CUSTOMER), new UserHasRole(Role.MANAGER));
    }

    private void confirmOrder(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Order order = new Order();
            order.setOrderType(Order.fromString(HttpBodyParser.parse(req)).getOrderType());
            order.setCreatedOn(LocalDateTime.now());
            order.setStatus(OrderStatus.PREPARATION);
            order.setCart(cartDao.findCurrent((User) req.getSession().getAttribute("user")));
            order.getCart().setOrder(order);
            orderDao.save(order);
        }catch (JsonProcessingException e) {
            res.sendError(400);
        }catch (NoResultException e){
            res.sendError(404);
        }
    }

    private void getOrder(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("user");
        if(user.getRole() == Role.MANAGER){
            try {
                List<Order> orders = orderDao.findAll();
                if (orders == null || orders.size() == 0) {
                    orders = new SerializableArrayList<>();
                }
                req.setAttribute("order", JacksonObjectMapper.getInstance().writeValueAsString(orders));
            } catch(NoResultException e){
                res.sendError(404);
                return;
            }
        } else{
            if(req.getParameterValues("id").length > 0){
                try{
                    int orderId = Integer.parseInt(req.getParameter("id"));
                    Order order = orderDao.get(orderId);
                    if(order.getCart().getCustomer().getId() != user.getId()) {
                        res.sendError(403);
                        return;
                    }
                    req.setAttribute("order", order.toString());
                }catch (NumberFormatException e){
                    res.sendError(400);
                    return;
                }
            }

        }
        req.getRequestDispatcher("WEB-INF/orderInfo.jsp").forward(req, res);
    }
}
