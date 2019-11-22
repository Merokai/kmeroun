package udev.jsp.kmeroun;

import udev.jsp.kmeroun.model.Database;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/App")
public class App extends HttpServlet{
	Database db;
	ObjectMapper objMapper;
	public App() {
		super();
		db = Database.getInstance();
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{	
		ObjectMapper objMapper = new ObjectMapper();
		//String test= objMapper.writeValueAsString(Arrays.asList(db.getData().entrySet()));
		String test= objMapper.writeValueAsString(db.getDishModelList());
		System.out.println(test);
		res.setStatus(200);
		res.setContentType("application/json");
		res.getWriter().write("<pre>" + test + "<pre>" );
	}
}