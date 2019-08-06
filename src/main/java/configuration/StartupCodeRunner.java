package configuration;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.discovery.event.ServiceStartedEvent;

import javax.inject.Singleton;

@Singleton
public class StartupCodeRunner implements ApplicationEventListener<ServiceStartedEvent> {

    @Override
    public void onApplicationEvent(final ServiceStartedEvent event) {
//        PojoChecker.of(TripResponseItem.class).check("asd");
    }
}
