package udev.jsp.kmeroun.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.dao.DishDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Dish;
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

    /**
     * Create a new dish
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
     * Get all the dishes
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getDishes(request, response);

    }

    /**
     * Update an existing dish
     * Require logged in MANAGER
     * Require dish "id" PUT parameter
     * Require dish as json body content
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::updateDish, new UserHasRole(Role.MANAGER));
    }

    /**
     * Delete an existing dish
     * Require logged in MANAGER
     * Require dish "id" DELETE parameter
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::deleteDish, new UserHasRole(Role.MANAGER));
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
    }

    private void updateDish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            Dish dish = Dish.fromString(HttpBodyParser.parse(request));
            dish.setId(Integer.parseInt(request.getParameter("id")));
            dishDao.update(dish);
        } catch (NumberFormatException e){ // Invalid id
            response.sendError(400);
        } catch (JsonProcessingException e){ // Invalid Json
            response.sendError(400);
        } catch (PersistenceException e){ // Database constraint violation
            response.sendError(400);
        }
    }

    private void deleteDish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            dishDao.delete(Integer.parseInt(request.getParameter("id")));
        } catch (NumberFormatException e){ // Invalid id
            response.sendError(400);
        } catch (PersistenceException e){ // Database constraint violation
            response.sendError(400);
        }
    }

    private void getDishes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("dishes", JacksonObjectMapper.getInstance().writeValueAsString(dishDao.findAll()));
        request.getRequestDispatcher("WEB-INF/dishList.jsp").forward(request, response);
    }
}
