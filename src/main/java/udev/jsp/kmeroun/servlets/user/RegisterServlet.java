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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@WebServlet("/user/register")
public class RegisterServlet extends HttpServlet {
    private static final UserDao userDao = new UserDao();

    /**
     * POST /user/register
     * Create a new ${@link User}
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::createUser, new UserHasRole(Role.GUEST));

    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = User.fromString(HttpBodyParser.parse(request));

        try {
            String hashedPassword = PasswordHash.getInstance().generateHashedPassword(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRole(Role.CUSTOMER);userDao.save(user);
            request.getSession().setAttribute("user", userDao.getByUsername(user.getUsername()));
            request.getRequestDispatcher("WEB-INF/userInfo.jsp").forward(request, response);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            response.sendError(500);
            e.printStackTrace();
        } catch( PersistenceException e){
            e.printStackTrace();
            response.sendError(409);
        }
    }
}
