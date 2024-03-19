package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.UserDao;
import step.learning.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ProfileServlet extends HttpServlet {

    private final UserDao userDao;
    @Inject
    public ProfileServlet(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo().substring(1);

        User authUser = (User) req.getAttribute("auth-user");
        if(authUser != null && (pathInfo.isEmpty() || pathInfo.equals(authUser.getId().toString()))){
            req.setAttribute("profile-user",authUser);
        }
        else{
            req.setAttribute("profile-user",userDao.getUserBuId(pathInfo));
        }

        req.setAttribute("page-body", "profile.jsp");
        req.getRequestDispatcher( "../WEB-INF/_layout.jsp" ).forward(req, resp);
    }
}
