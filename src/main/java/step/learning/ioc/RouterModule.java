package step.learning.ioc;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import step.learning.filters.AuthFilter;
import step.learning.filters.DbFilter;
import step.learning.servlets.*;

@Singleton
public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through(AuthFilter.class);
        //filter("/*").through( DbFilter.class );

        //filterRegex("^/(?!css/.*|img/.*|upload/.*).*$").through(DbFilter.class);
        serve("/"   ).with( HomeServlet.class ) ;
        serve("/ioc").with( IocServlet.class  ) ;
        serve("/auth"  ).with( AuthServlet.class    ) ;
        serve("/signup").with( SignUpServlet.class  ) ;
        serve("/privacy").with( PrivacyServlet.class  ) ;
        serve("/news/*"  ).with( NewsServlet.class    ) ;
        serve("/profile/*"  ).with( ProfileServlet.class    ) ;
    }
}
