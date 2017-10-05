package mydistributedwebapp;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

import java.io.IOException;

public class HelloWorldServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello buddy from servlet ");
    }
    
	private final static Logger log = Logger.getLogger(RequestParserServlet.class.getName());
}