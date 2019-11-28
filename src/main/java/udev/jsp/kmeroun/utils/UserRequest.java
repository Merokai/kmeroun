package udev.jsp.kmeroun.utils;

import udev.jsp.kmeroun.enums.Role;
import udev.jsp.kmeroun.models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserRequest {

    void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    static void requireAuthentication(HttpServletRequest request, HttpServletResponse response, UserRequest userRequest) throws ServletException, IOException{
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){ // Not authenticated
            response.setStatus(401);
            return;
        }
        request.setAttribute("user", user);
        userRequest.handleRequest(request, response);
    }

    static void requireRole(Role role, HttpServletRequest request, HttpServletResponse response, UserRequest userRequest) throws ServletException, IOException{
        requireAuthentication(request, response, (req, res) -> {
            User user = (User) request.getAttribute("user");
            if(user.getRole() != role){ // Not authorized
                res.setStatus(403);
                return;
            }
            userRequest.handleRequest(req, res);
        });
    }

    static void requireParameters(String[] parameterNames, HttpServletRequest request, HttpServletResponse response, UserRequest userRequest) throws ServletException, IOException{
        for(String parameterName : parameterNames){
            if(request.getParameter(parameterName) == null){
                response.setStatus(400);
                return;
            }
        }
        userRequest.handleRequest(request, response);
    }

    static void requireParameter(String parameterName, HttpServletRequest request, HttpServletResponse response, UserRequest userRequest) throws ServletException, IOException{
        requireParameters(new String[]{parameterName}, request, response, userRequest);
    }

    static void requireBody(HttpServletRequest request, HttpServletResponse response, UserRequest userRequest) throws ServletException, IOException{
        String body = HttpBodyParser.parse(request);
        if (body == null || body.length() == 0){
            response.setStatus(400);
            return;
        }
        request.setAttribute("httpBody", body);
        userRequest.handleRequest(request, response);
    }

    static <T> void requireJsonParameter(Class<T> valueType, HttpServletRequest request, HttpServletResponse response, UserRequest userRequest) throws ServletException, IOException{
        requireBody(request, response, (req, res) -> {
            T object = JacksonObjectMapper.getInstance().readValue((String)req.getAttribute("httpBody"), valueType);
            if(object == null){
                res.setStatus(400);
                return;
            }
            req.removeAttribute("httpBody");
            req.setAttribute("jsonObject", object);
            userRequest.handleRequest(req, res);
        });
    }






}
