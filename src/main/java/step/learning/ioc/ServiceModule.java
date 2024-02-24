package step.learning.ioc;

import com.google.inject.AbstractModule;
import java.sql.*;

import com.google.inject.Provides;
import step.learning.services.form_parse.CommonsFormParseService;
import step.learning.services.form_parse.FormParseService;
import step.learning.services.hash.HashService;
import step.learning.services.hash.Md5HashService;
import step.learning.services.kdf.HashKdfService;
import step.learning.services.kdf.KdfService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HashService.class).to(Md5HashService.class);
        bind(FormParseService.class).to(CommonsFormParseService.class);
        bind(KdfService.class).to(HashKdfService.class);
    }

//    private Connection connection ;
//    private  Driver mySqlDriver ;
//    @Provides  // методи-провайдери: для кожної точки @Inject Connection .... буде виклик методу
//    private Connection getConnection() {
//        if( connection == null ) {
//            try {
//                mySqlDriver = new com.mysql.cj.jdbc.Driver() ;
//                DriverManager.registerDriver( mySqlDriver ) ;
//                String connectionString = "jdbc:mysql://localhost:3307/java_111" +
//                        "?useUnicode=true&characterEncoding=UTF-8" ;
//                connection = DriverManager.getConnection( connectionString, "user_111", "pass_111" ) ;
//            }
//            catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return connection ;
//    }

//    @Override
//    public void close() throws Exception {
//        if( connection != null ) {
//            connection.close() ;
//        }
//        if( mySqlDriver != null ) {
//            DriverManager.deregisterDriver( mySqlDriver ) ;
//        }
//    }
}


