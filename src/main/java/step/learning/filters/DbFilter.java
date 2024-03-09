package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.db.DbService;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
public class DbFilter implements Filter {
    private final DbService dbService ;
    private FilterConfig filterConfig;

    @Inject
    public DbFilter(DbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Connection connection = dbService.getConnection();
        if( connection == null ) {
            request.getRequestDispatcher("/static.jsp")
                    .forward(request, response);
        }
        else {
            // те, що підключення не null не гарантує його активності
            // можливо це старий об'єкт від розірваного підключення
            try {
                // connection.isClosed() - не дає правильного результату якщо
                // був розрив з боку БД (не відключення, а розрив)
                // надійний варіант - тестовий запит
                connection.createStatement().execute("SELECT 1");
            }
            catch (SQLException ignored) {
                dbService.closeConnection();
                connection = dbService.getConnection();
                if( connection == null ) {
                    request.getRequestDispatcher("/static.jsp")
                            .forward(request, response);
                    return;
                }
            }
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
/*
Д.З. Створити фільтр, який буде задавати кодування символів
для request та response усіх запитів
(див AuthServlet::doGet)
Поставити фільтр на саме перше місце (кодування не можна змінювати,
якщо вже відбулись читання чи запис до request / response)
* Повторити регулярні шаблони, потрібен шаблон
"усе, окрім запитів на папки /css/, /js/, /upload/, /img/"
 */