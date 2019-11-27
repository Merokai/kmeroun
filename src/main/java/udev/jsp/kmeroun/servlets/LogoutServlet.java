package udev.jsp.kmeroun.servlets;

import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.UserHasRole;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutServlet")
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, (req, res) -> {
            request.getSession().invalidate();
        }, new UserHasRole(Role.CUSTOMER, Role.MANAGER));
    }
}
