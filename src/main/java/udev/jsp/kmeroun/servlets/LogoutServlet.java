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

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRequest.requireAuthentication(request, response, (req, res) -> {
            req.getSession().invalidate();
            res.setStatus(200);
        });
    }
}
