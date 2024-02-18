package step.learning.ioc;

import com.google.inject.AbstractModule;
import step.learning.services.hash.HashService;
import step.learning.services.hash.Md5HashService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HashService.class).to(Md5HashService.class);
    }
}
