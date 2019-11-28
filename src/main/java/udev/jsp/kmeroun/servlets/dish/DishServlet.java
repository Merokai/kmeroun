package udev.jsp.kmeroun.servlets.dish;

import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.dao.DishDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;
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

    /**
     * POST /dish
     * Create a new {@link Dish}
     * Require Manager role
     * Require Dish as request body content
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::saveDish, new UserHasRole(Role.MANAGER));
    }

    /**
     * GET /dish
     * Get all the {@link Dish}es
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getDishes(request, response);
    }

    private void saveDish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            Dish dish = Dish.fromString(HttpBodyParser.parse(request));
            dishDao.save(dish);
        } catch (JsonProcessingException e){ // Invalid Json
            response.sendError(400);
        } catch (PersistenceException e){ // Database constraint violation
            response.sendError(400);
        }
        getDishes(request, response);
    }

    private void getDishes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            request.setAttribute("dishes", JacksonObjectMapper.getInstance().writeValueAsString(dishDao.findAll()));
            request.getRequestDispatcher("WEB-INF/dishList.jsp").forward(request, response);
        }catch (PersistenceException e){
            response.sendError(500);
        }
    }
}
