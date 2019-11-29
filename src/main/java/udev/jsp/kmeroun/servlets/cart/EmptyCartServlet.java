package udev.jsp.kmeroun.servlets.cart;

import udev.jsp.kmeroun.dao.CartDao;
import udev.jsp.kmeroun.dao.CartItemDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Cart;
import udev.jsp.kmeroun.models.CartItem;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cart/empty")
public class EmptyCartServlet extends HttpServlet {
    private static final CartDao cartDao = new CartDao();
    private static final CartItemDao cartItemDao = new CartItemDao();

    /** GET /cart/empty
     * Empty the logged in CUSTOMER {@link Cart}
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::emptyCart, new UserHasRole(Role.CUSTOMER));
    }

    private void emptyCart(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Cart cart = cartDao.findCurrent((User) req.getSession().getAttribute("user"));
        if (cart == null) {
            res.sendError(404);
            return;
        }
        try {
            for (CartItem cartItem : cart.getCartItems()) {
                cartItemDao.delete(cartItem);
            }
        } catch (PersistenceException e) {
            res.sendError(500);
            return;
        }
        req.getRequestDispatcher("/cart").forward(req, res);
    }
}
