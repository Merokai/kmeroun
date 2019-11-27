package udev.jsp.kmeroun.servlets;

import udev.jsp.kmeroun.dao.UserDao;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HttpBodyParser;
import udev.jsp.kmeroun.utils.PasswordHash;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final UserDao userDao = new UserDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        login(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/userInfo.jsp").forward(request, response);
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
        request.getRequestDispatcher("WEB-INF/userInfo.jsp").forward(request, response);
    }
}
