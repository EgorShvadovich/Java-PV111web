package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.UserDao;
import step.learning.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class AuthServlet extends HttpServlet {
    private final UserDao userDao;

    @Inject
    public AuthServlet(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        JsonObject res = new JsonObject() ;
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        /*
        Д.З. Реалізувати поглиблену валідацію одержаних даних автентифікації
        Використати шаблони перевірки (регулярні шаблони)
        * Додати можливість автентифікації як за поштою, так і за телефоном
          (який параметр прийде)
         */
        if(email == null) {
            res.addProperty("status", "error");
            res.addProperty("message", "Missing required data: 'email'");
        }
        else if(password == null) {
            res.addProperty("status", "error");
            res.addProperty("message", "Missing required data: 'password'");
        }
        else {
            User user = userDao.getUserByCredentials(email, password);
            if( user == null ) {
                res.addProperty("status", "error");
                res.addProperty("message", "Credentials rejected");
            }
            else {
                res.addProperty("status", "success");
                res.addProperty("message", "Page reload expected");

                HttpSession session = req.getSession();
                session.setAttribute("auth-user",user);

                //res.add("data", gson.toJsonTree(user));
            }
        }
        resp.getWriter().print(
                new Gson().toJson(res)
        );
    }
}
/*
Відмінності API                | від MVC
- один URL, багато методів     | - багато URL, метод один (два)
    GET /auth                  |  GET /home/index
    POST /auth                 |  GET /home/privacy
    PUT /auth                  |
- на виході "сирі" дані,       | - на виході - HTML
    частіше за все JSON        |
- на вході: URL дані,          | - на вході як правило нічого,
    заголовки, тіло. Традиція  |    або дані форм. Традиція
    REST - єдина форма для     |    slug - дані в шляху URL
    запитів та відповідей      |     /shop/product/123
 */