package udev.jsp.kmeroun.servlets.cart;

import udev.jsp.kmeroun.dao.CartDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.*;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;

import javax.persistence.PersistenceException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final CartDao cartDao = new CartDao();

    /**
     * GET /cart
     * Get the logged in CUSTOMER {@link Cart} (and create if not exists)
     * @param request
     * @param response;
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::getCart, new UserHasRole(Role.CUSTOMER));
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
}

