package udev.jsp.kmeroun.utils.HttpRequestValidator;

import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class UserHasRole implements HttpRequestValidator {

    private final Collection<Role> roles;

    public UserHasRole(Role ...roles){
        this.roles = Arrays.asList(roles);
    }

    @Override
    public boolean validate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null && roles.contains(Role.GUEST) || user != null && roles.contains((user).getRole())){
            return true;
        }
        response.sendError(403);
        return false;
    }
}
