package udev.jsp.kmeroun.servlets.user;

import udev.jsp.kmeroun.dao.UserDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;
import udev.jsp.kmeroun.utils.HttpBodyParser;
import udev.jsp.kmeroun.utils.PasswordHash;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {
    private static final UserDao userDao = new UserDao();

    /**
     * POST /user/login
     * {@link User} login
     * Require json user as content body
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::login, new UserHasRole(Role.GUEST));
    }

    /**
     * GET /user/login
     * Get logged in {@link User} informations
     * Require logged in CUSTOMER or MANAGER
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::getUserInfo, new UserHasRole(Role.CUSTOMER, Role.MANAGER));
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = User.fromString(HttpBodyParser.parse(request));
        User dbUser = null;

        try {
            dbUser = userDao.getByUsername(user.getUsername());
        } catch (PersistenceException e){
            response.sendError(404);
            return;
        }

        if(dbUser == null || !PasswordHash.getInstance().verifyPassword(user.getPassword(), dbUser.getPassword())) {
            response.sendError(404);
            return;
        }

        request.getSession().setAttribute("user", dbUser);
        getUserInfo(request, response);
    }

    private void getUserInfo(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/userInfo.jsp").forward(req, res);
    }
}
