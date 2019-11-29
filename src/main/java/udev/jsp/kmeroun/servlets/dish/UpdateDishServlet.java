package udev.jsp.kmeroun.servlets.dish;

import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.dao.DishDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;
import udev.jsp.kmeroun.utils.HttpBodyParser;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dish/update")
public class UpdateDishServlet extends HttpServlet {
    private static final DishDao dishDao = new DishDao();

    /**
     * POST /dish/update
     * Update an existing {@link Dish}
     * Require logged in MANAGER
     * Require dish "id" PUT parameter
     * Require dish as json body content
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::updateDish, new UserHasRole(Role.MANAGER));
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
}
