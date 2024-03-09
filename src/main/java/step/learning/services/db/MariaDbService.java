package step.learning.services.db;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class MariaDbService implements DbService {
    private Connection connection ;
    private Driver mySqlDriver ;
    private final Logger logger;
    @Inject
    public MariaDbService(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Connection getConnection() {
        if( connection == null ) {
            // демонстрація роботи з ресурсами (папка resources)
            // вміст папки входить у deploy у складі папки target/classes
            // звернутись до цієї папки можна через завантажувач класів
            // сам завантажувач можна взяти з будь-якого об'єкту
            JsonObject dbConfig;
            try( InputStream reader = this.getClass().getClassLoader().getResourceAsStream("app_settings.json")){
                dbConfig = JsonParser.parseString( readStreamToEnd(
                                Objects.requireNonNull(reader) ) )
                        .getAsJsonObject()
                        .getAsJsonObject("db")
                        .getAsJsonObject("MariaDB");
                mySqlDriver = new com.mysql.cj.jdbc.Driver() ;
                DriverManager.registerDriver( mySqlDriver ) ;
                String connectionString = String.format( Locale.UK,
                        "jdbc:%s://%s:%d/%s?%s",
                        dbConfig.get("protocol").getAsString(),
                        dbConfig.get("host").getAsString(),
                        dbConfig.get("port").getAsInt(),
                        dbConfig.get("scheme").getAsString(),
                        dbConfig.get("params").getAsString()
                ) ;
                connection = DriverManager.getConnection(
                        connectionString,
                        dbConfig.get("user").getAsString(),
                        dbConfig.get("password").getAsString()
                ) ;
            }
            catch (Exception ex) {
                logger.log( Level.SEVERE, ex.getMessage() );
            }
        }
        return connection ;
    }
    @Override
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (mySqlDriver != null) {
                DriverManager.deregisterDriver(mySqlDriver);
            }
        }
        catch (SQLException ex) {
            logger.log( Level.WARNING, ex.getMessage() );
        }
        connection = null;
    }
    private String readStreamToEnd( InputStream inputStream ) throws IOException {
        final byte[] buffer = new byte[32 * 1024];  // 32k
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream() ;
        int len ;
        while( ( len = inputStream.read( buffer ) ) > -1 ) {
            byteBuilder.write( buffer, 0, len ) ;
        }
        return byteBuilder.toString() ;
    }
}