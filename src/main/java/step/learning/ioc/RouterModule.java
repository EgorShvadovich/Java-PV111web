package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.servlets.HomeServlet;
import step.learning.servlets.IocServlet;
import step.learning.servlets.PrivacyServlet;
import step.learning.servlets.SignUpServlet;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/"   ).with( HomeServlet.class ) ;
        serve("/ioc").with( IocServlet.class  ) ;
        serve("/signup").with( SignUpServlet.class  ) ;
        serve("/privacy").with( PrivacyServlet.class  ) ;
    }
}
