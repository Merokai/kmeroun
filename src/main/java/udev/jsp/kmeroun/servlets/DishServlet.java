package udev.jsp.kmeroun.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import udev.jsp.kmeroun.dao.DishDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HttpBodyParser;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;
import udev.jsp.kmeroun.utils.UserRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/dish")
public class DishServlet extends HttpServlet {
    private DishDao dishDao = new DishDao();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().append(dishDao.getDishes().toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRequest.requireRole(Role.MANAGER, request, response, (req, res) -> {
            Dish dish = JacksonObjectMapper.getInstance().readValue(HttpBodyParser.parse(req), Dish.class);
            dishDao.saveDish(dish);
        } );
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRequest.requireRole(Role.MANAGER, request, response, (req, res) -> {
            Dish dish = Dish.fromString(HttpBodyParser.parse(req));
            dishDao.updateDish(dish);
        } );
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserRequest.requireRole(Role.MANAGER, request, response, (req, res) -> {
            Dish dish = JacksonObjectMapper.getInstance().readValue(HttpBodyParser.parse(req), Dish.class);
            dishDao.deleteDish(dish.getId());
        });
    }
}
