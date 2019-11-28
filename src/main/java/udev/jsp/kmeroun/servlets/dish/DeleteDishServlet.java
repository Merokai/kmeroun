package udev.jsp.kmeroun.servlets.dish;

import udev.jsp.kmeroun.dao.DishDao;
import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.utils.HttpRequestValidator.HttpRequestValidator;
import udev.jsp.kmeroun.utils.HttpRequestValidator.UserHasRole;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dish/delete")
public class DeleteDishServlet extends HttpServlet {
    private static final DishDao dishDao = new DishDao();

    /**
     * POST /dish/delete
     * Delete an existing {@link Dish}
     * Require logged in MANAGER
     * Require dish "id" PUT parameter
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpRequestValidator.validate(request, response, this::deleteDish, new UserHasRole(Role.MANAGER));
    }

    private void deleteDish(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            dishDao.delete(dishDao.get(Integer.parseInt(request.getParameter("id"))));
        } catch (NumberFormatException e){ // Invalid id
            response.sendError(400);
        } catch (PersistenceException e){ // Database constraint violation
            response.sendError(400);
        }
    }
}
