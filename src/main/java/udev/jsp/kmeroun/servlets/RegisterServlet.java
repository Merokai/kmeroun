package udev.jsp.kmeroun.servlets;

import udev.jsp.kmeroun.dao.UserDao;
import udev.jsp.kmeroun.enums.Role;
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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final UserDao userDao = new UserDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("allo");
        User user = User.fromString(HttpBodyParser.parse(request));
        String hashedPassword = "";
        try {
            hashedPassword = PasswordHash.getInstance().generateHashedPassword(user.getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        user.setPassword(hashedPassword);
        user.setRole(Role.CUSTOMER);

        try{
            userDao.save(user);
            request.getSession().setAttribute("user", userDao.getByUsername(user.getUsername()));
            request.getRequestDispatcher("WEB-INF/userInfo.jsp").forward(request, response);
        } catch( PersistenceException e){
            e.printStackTrace();
            response.sendError(409);
        }
    }
}
