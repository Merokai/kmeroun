package udev.jsp.kmeroun.servlets.HttpRequestValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HttpRequestValidator {

    static void validate(HttpServletRequest request, HttpServletResponse response, HttpRequestHandler handler, HttpRequestValidator ...validators)  throws ServletException, IOException{
        for (HttpRequestValidator validator : validators) {
            if(!validator.validate(request, response)) return;
        }
        handler.handleRequest(request, response);
    }

    boolean validate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
