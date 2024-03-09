package step.learning.ioc;

import com.google.inject.AbstractModule;
import java.sql.*;

import com.google.inject.Provides;
import step.learning.services.db.DbService;
import step.learning.services.db.MariaDbService;
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
        bind(DbService.class).to(MariaDbService.class);
    }


}


