package udev.jsp.kmeroun.servlets;

import udev.jsp.kmeroun.dao.DishDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.servlets.HttpRequestValidator.UserHasRole;
import udev.jsp.kmeroun.utils.HttpBodyParser;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/dish")
public class DishServlet extends HttpServlet {
    private static final DishDao dishDao = new DishDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, (req, res) ->{
                saveDish(request, response);
        }, new UserHasRole(Role.MANAGER));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getDishes(request, response);

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, (req, res) ->{
                updateDish(req, res);
        }, new UserHasRole(Role.MANAGER));
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, (req, res) ->{
                deleteDish(req, res);
        }, new UserHasRole(Role.MANAGER));
    }

    private Role getCurrentUserRole(HttpServletRequest request) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return Role.GUEST;
        }
        return user.getRole();
    }

    private void saveDish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Dish dish = Dish.fromString(HttpBodyParser.parse(request));
        try{
            dishDao.save(dish);
        } catch(PersistenceException e){
            response.sendError(409);
        }
    }

    private void updateDish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Dish dish = Dish.fromString(HttpBodyParser.parse(request));
        try{
            dishDao.update(dish);
        } catch(PersistenceException e){
            response.sendError(409);
        }
    }

    private void deleteDish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Dish dish = Dish.fromString(HttpBodyParser.parse(request));
        try{
            dishDao.delete(dish);
        } catch(PersistenceException e){
            response.sendError(404);
        }
    }

    private void getDishes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("dishes", JacksonObjectMapper.getInstance().writeValueAsString(dishDao.findAll()));
        request.getRequestDispatcher("WEB-INF/dishList.jsp").forward(request, response);
    }
}
