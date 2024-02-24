package step.learning.ioc;

import com.google.inject.AbstractModule;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.logging.*;

public class LoggerModule extends AbstractModule {
    @Override
    protected void configure() {
/*        try (InputStream config = Objects.requireNonNull(
                this.getClass()
                .getClassLoader()
                .getResourceAsStream("logging.properties"))){
            LogManager manager = LogManager.getLogManager();
            manager.reset();
            manager.readConfiguration(config);
        }
        catch (Exception ex){
            System.err.println("Logger config error: " + ex.getMessage());
        }*/
        FileHandler fileHandler;
        ConsoleHandler consoleHandler;
        try {
            URL url = this.getClass().getClassLoader().getResource("/");
            assert url != null;
            String filename = url.getPath().substring(1) + "Logger.log";
            fileHandler = new FileHandler(filename, 2000, 5);
// fileHandler.setFormatter();

            consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setEncoding("UTF-8");
        } catch (Exception ex) {
            System.err.println("Logger config error: + ex.getMessage()");
            return;
        }
        LogManager manager = LogManager.getLogManager();
        manager.reset();
        Logger logger = manager.getLogger("");
        logger.addHandler(consoleHandler);
        logger.addHandler(fileHandler);
    }
}
