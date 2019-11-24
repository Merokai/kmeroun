package udev.jsp.kmeroun.servlets;

import udev.jsp.kmeroun.dao.UserDao;
import udev.jsp.kmeroun.models.Dish;
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRequest.requireJsonParameter(User.class, request, response, (req, res) -> {
            User user = (User)req.getAttribute("jsonObject");

            // Try to authenticate user
            UserDao userDao = new UserDao();
            User dbUser = userDao.get(user.getUsername());

            // username not found or bad password
            if (dbUser == null || !PasswordHash.getInstance().verifyPassword(user.getPassword(), dbUser.getPassword())) {
                res.setStatus(404);
                return;
            }

            // Login OK, set up session attribute
            req.getSession().setAttribute("user", dbUser);
            res.setStatus(200);
            res.setContentType("application/json");
            res.getWriter().append(dbUser.toString());
        });
    }
}
