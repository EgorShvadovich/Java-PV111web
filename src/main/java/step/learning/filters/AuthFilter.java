package step.learning.filters;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AuthFilter implements Filter {
    private FilterConfig filterConfig;
    private final Logger logger;
    @Inject
    public AuthFilter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession session = req.getSession();
        if(req.getParameterMap().containsKey("logout")){
            session.removeAttribute("auth-user");
            ((HttpServletResponse)servletResponse).sendRedirect(req.getRequestURI());
            return;
        }


        User user =(User) session.getAttribute("auth-user");
        if(user != null){
            req.setAttribute("auth-user",user);
            logger.log(Level.INFO,"AuthFilter: user authorized");
        }
        else{
            logger.log(Level.INFO,"AuthFilter: user unauthorized");
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
       this.filterConfig = null;
    }
}
