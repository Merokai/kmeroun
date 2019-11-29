package udev.jsp.kmeroun.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.omg.CORBA.Request;
import sun.security.pkcs.ParsingException;
import udev.jsp.kmeroun.dao.CartDao;
import udev.jsp.kmeroun.dao.CartItemDao;
import udev.jsp.kmeroun.dao.DishDao;
import udev.jsp.kmeroun.enums.OrderStatus;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Cart;
import udev.jsp.kmeroun.models.CartItem;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.UserHasRole;
import udev.jsp.kmeroun.utils.HttpBodyParser;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final CartDao cartDao = new CartDao();
    private static final CartItemDao cartItemDao = new CartItemDao();

    /**
     * Get the logged in CUSTOMER cart (and create if not exists)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("je devrais me trouver ici");
        HttpRequestValidator.validate(request, response, this::getCart, new UserHasRole(Role.CUSTOMER));
    }

    /**
     * Delete the logged in CUSTOMER cart and create a new one
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::deleteCart, new UserHasRole(Role.CUSTOMER));
    }

    /**
     * Update an existing CartItem
     * Require CartItem "id" parameter
     * Require CartItem content as request body
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::updateCartItem, new UserHasRole(Role.CUSTOMER));
    }

    /**
     * Add a new CartItem
     * Require CartItem content as request body
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::addCartItem, new UserHasRole(Role.CUSTOMER));
    }


    private void getCart(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Cart cart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));

        if (cart == null) {
            cart = new Cart((User) req.getSession().getAttribute("user"));
            try {
                cartDao.save(cart);
                cart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));
            } catch (PersistenceException e) {
                res.sendError(500);
                return;
            }
        }
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("WEB-INF/cartInfo.jsp").forward(req, res);
    }

    private void deleteCart(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Cart cart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));
        if (cart == null) {
            res.sendError(404);
            return;
        }
        try {
            cartDao.delete(cart.getId());
        } catch (PersistenceException e) {
            res.sendError(500);
            return;
        }
        getCart(req, res);
    }

    private void addCartItem(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        CartItem cartItem = null;
        try{
            cartItem = CartItem.fromString(HttpBodyParser.parse(req));
        }catch(JsonProcessingException e) { // Invalid json body
            res.sendError(400);
            e.printStackTrace();
            return;
        }

        Cart cart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));
        if(cart == null){
            res.sendError(500);
            return;
        }

        cartItem.setCart(cart);

        // Reject if the user's cart already contains a cartItem for the given product
        for (CartItem item : cart.getCartItems()) {
            if (cartItem.getProduct() == null || item.getProduct() == cartItem.getProduct()) {
                res.sendError(400);
                return;
            }
        }

        try {
            cartItemDao.save(cartItem);
        } catch(PersistenceException e){ // Database constraint violation
            e.printStackTrace();
            res.sendError(500);
            return;
        }
        getCart(req, res);
    }

    private void updateCartItem(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        int cartItemid;
        try{
            CartItem cartItem = CartItem.fromString(HttpBodyParser.parse(req));
            cartItem.setId(Integer.parseInt(req.getParameter("id")));
            cartItemDao.update(cartItem);
        } catch(JsonProcessingException e) { // Invalid json body
            res.sendError(400);
            e.printStackTrace();
            return;
        } catch(NumberFormatException e){ // Invalid id parameter
            res.sendError(400);
            e.printStackTrace();
            return;
        } catch(PersistenceException e){ // Database constraint violation
            e.printStackTrace();
            res.sendError(500);
            return;
        }
        getCart(req, res);
    }
}

