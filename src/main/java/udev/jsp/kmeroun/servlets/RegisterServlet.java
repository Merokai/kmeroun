package udev.jsp.kmeroun.servlets;

import org.hibernate.exception.ConstraintViolationException;
import udev.jsp.kmeroun.dao.UserDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HttpBodyParser;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;
import udev.jsp.kmeroun.utils.PasswordHash;
import udev.jsp.kmeroun.utils.UserRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.Collectors;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRequest.requireJsonParameter(User.class, request, response, (req, res) -> {
            User user = (User)req.getAttribute("jsonObject");
            user.setRole(Role.CUSTOMER);
            try {
                user.setPassword(PasswordHash.getInstance().generateHashedPassword(user.getPassword()));

            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                res.setStatus(500);
                return;
            }
            // User already exists
            if(userDao.get(user.getUsername()) != null) {
                res.setStatus(409);
                return;
            }
            userDao.save(user);
            req.getSession().setAttribute("user", userDao.get(user.getUsername()));
            res.setStatus(200);
        });
    }
}
