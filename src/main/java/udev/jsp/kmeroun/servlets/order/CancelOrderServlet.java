package udev.jsp.kmeroun.servlets.order;

import udev.jsp.kmeroun.dao.OrderDao;
import udev.jsp.kmeroun.enums.OrderStatus;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Order;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order/cancel")
public class CancelOrderServlet extends HttpServlet {
    private static final OrderDao orderDao = new OrderDao();

    /**
     * POST /order/cancel
     * Cancel the given {@link Order}
     * Require POST parameter id
     * Require role CUSTOMER to cancel user owned order
     * Require role MANAGER to cancel any "PREPARATION, WAITING_DELIVERY" order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::cancelOrder, new UserHasRole(Role.CUSTOMER, Role.MANAGER));
    }

    private void cancelOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        Order order = orderDao.get(Integer.parseInt(request.getParameter("id")));
        if(order == null){
            response.sendError(404);
            return;
        }

        // Order can be cancelled only for PREPARATION and WAITING_DELIVERY status
        if(order.getStatus() != OrderStatus.PREPARATION && order.getStatus() != OrderStatus.WAITING_DELIVERY){
            response.sendError(400);
            return;
        }

        if(user.getRole() == Role.MANAGER) {
            order.setStatus(OrderStatus.SELLER_CANCELLED);
        } else if( order.getCart().getCustomer().getId() == user.getId()){
            order.setStatus(OrderStatus.BUYER_CANCELLED);
        } else{
            response.sendError(403);
            return;
        }
        response.sendRedirect("order");
    }
}
