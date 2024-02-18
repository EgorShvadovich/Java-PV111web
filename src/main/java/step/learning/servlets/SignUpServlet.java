package step.learning.servlets;

import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<String,String> errorMessages  = (Map<String,String> ) session.getAttribute( "form-status" ) ;
        if( errorMessages != null ) {
            req.setAttribute("errorMessages", errorMessages ) ;
            session.removeAttribute( "form-status" ) ;
        }
        req.setAttribute( "page-body", "signup.jsp" ) ;
        req.getRequestDispatcher( "WEB-INF/_layout.jsp" ).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // API - JSON
        // Web - Redirect
        Map<String,String> errorMessages = new HashMap<>();
        String userName = req.getParameter("user-name");
        boolean symbols = false;
        if(userName == null || "".equals(userName)){
            errorMessages.put("user-name","Не може бути порожнім");
        }
        if(userName.length() == 1){
            errorMessages.put("user-name","Має бути довшим ніж 1 символ");
        }

        Pattern pattern = Pattern.compile("[A-Z][a-z]*");
        Matcher matcher = pattern.matcher(userName);

        if (!matcher.matches()) {
            errorMessages.put("user-name","Перша літера має бути велика,а інші - маленькі");
        }


        HttpSession session = req.getSession();
        session.setAttribute( "form-status", errorMessages );
        resp.sendRedirect( req.getRequestURI() );
    }
}
