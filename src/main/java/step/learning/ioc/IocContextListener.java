package step.learning.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Слухач (підписник, обробник) події створення контексту -
 * запуску проєкта. Підписується у web.xml
 */
public class IocContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new ServiceModule(),
                new RouterModule(),
                new LoggerModule()
        );
    }
}
