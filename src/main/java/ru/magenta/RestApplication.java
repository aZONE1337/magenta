package ru.magenta;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();

    public void RestEasyServices() {
        singletons.add(new CalculatorResource());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
