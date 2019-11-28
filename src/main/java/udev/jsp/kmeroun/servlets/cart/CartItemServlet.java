package udev.jsp.kmeroun.servlets.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.dao.CartDao;
import udev.jsp.kmeroun.dao.CartItemDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Cart;
import udev.jsp.kmeroun.models.CartItem;
import udev.jsp.kmeroun.models.Product;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;
import udev.jsp.kmeroun.utils.HttpBodyParser;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@WebServlet("/cart/item")
public class CartItemServlet extends HttpServlet {
    private static final CartDao cartDao = new CartDao();
    private static final CartItemDao cartItemDao = new CartItemDao();

    /**
     * POST /cart/item
     * Update, insert or remove an existing CartItem
     * Require Product "id" parameter
     * Require {@link CartItem} content as request body
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::updateCartItem, new UserHasRole(Role.CUSTOMER));
    }

    private void updateCartItem(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        // Retrieving user cart
        Cart cart = null;
        try {
            cart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));
        }catch(NoResultException e) {
            res.sendError(500);
            return;
        }

        // Forging CartItem object
        Product product = new Product();
        CartItem cartItem = null;
        try {
            product.setId(Integer.parseInt(req.getParameter("id")));
            cartItem = CartItem.fromString(HttpBodyParser.parse(req));
        } catch(NumberFormatException | JsonProcessingException e) {
            res.sendError(400);
            return;
        }

        cartItem.setProduct(product);
        cartItem.setCart(cart);

        // Try to update the cartItem, or create it
        try{
            if(!updateCartItemIfPresent(req, res, cart, cartItem)){
                cartItemDao.save(cartItem);
            }
        } catch (PersistenceException e){
            res.sendError(500);
            return;
        }
        req.getRequestDispatcher("/cart").forward(req, res);
    }

    private boolean updateCartItemIfPresent(HttpServletRequest req, HttpServletResponse res, Cart cart, CartItem cartItem) throws IOException, ServletException {
        // If the product is present in the cartItem, quantity is updated
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getId() == cartItem.getId()) {
                item.setQuantity(cartItem.getQuantity());
                if(cartItem.getQuantity() > 0) {
                    cartItemDao.update(item);
                } else {
                    cartItemDao.delete(item);
                }
                return true;
            }
        }
        return false;
    }
}
