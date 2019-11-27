package udev.jsp.kmeroun.servlets;

import org.omg.CORBA.Request;
import udev.jsp.kmeroun.dao.CartDao;
import udev.jsp.kmeroun.dao.DishDao;
import udev.jsp.kmeroun.enums.OrderStatus;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Cart;
import udev.jsp.kmeroun.models.CartItem;
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
import java.util.Collection;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final CartDao cartDao = new CartDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::saveCart, new UserHasRole(Role.CUSTOMER));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::getCart, new UserHasRole(Role.CUSTOMER));
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::deleteCart, new UserHasRole(Role.CUSTOMER));
    }

    private void saveCart(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Cart cart = Cart.fromString(HttpBodyParser.parse(req));

        cart.setCustomer((User) req.getSession().getAttribute("user"));

        try {
            Cart userSavedCart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));
            cartDao.delete(userSavedCart);

        } catch (NullPointerException e){}

        cartDao.save(cart);
    }

    private void getCart(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        try {
            Cart userSavedCart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));
            req.setAttribute("cart", userSavedCart);
            req.getRequestDispatcher("WEB-INF/cartInfo.jsp").forward(req, res);
        } catch (NullPointerException e){
            res.sendError(404);
        }
    }

    private void deleteCart(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Cart userSavedCart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));
            cartDao.delete(userSavedCart);
        } catch (NullPointerException e){
            res.sendError(404);
        }

    }
}
