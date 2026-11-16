package com.vet_clinic.vet_app.ioc;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A small Inversion of Control container. Which class is used for each interface is
 * decided in ioc.properties, not in the code. Plug-ins ask for an interface (OwnerDao)
 * and the container creates and hands over the configured implementation.
 */
public class ServiceContainer {

    private static final Logger LOGGER = LogManager.getLogger(ServiceContainer.class);

    private final Properties bindings = new Properties();
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public ServiceContainer(String resourceName) {
        try (InputStream in = ServiceContainer.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                LOGGER.error("IOC configuration file {} was not found on the classpath", resourceName);
            } else {
                bindings.load(in);
                LOGGER.info("IOC configuration {} loaded: {} bindings", resourceName, bindings.size());
            }
        } catch (IOException e) {
            LOGGER.error("Could not read the IOC configuration {}", resourceName, e);
        }
    }

    /** Lets code (or a test) plug in a ready-made object instead of the configured class. */
    public synchronized <T> void register(Class<T> type, T instance) {
        instances.put(type, instance);
        LOGGER.debug("Registered {} for {}", instance.getClass().getSimpleName(), type.getSimpleName());
    }

    /** Returns the implementation for the interface, creating it the first time it is asked for. */
    public synchronized <T> T get(Class<T> type) {
        Object found = instances.get(type);
        if (found == null) {
            String className = bindings.getProperty(type.getName());
            if (className == null) {
                throw new IllegalStateException("No implementation is configured for " + type.getName());
            }
            try {
                found = Class.forName(className.trim()).getDeclaredConstructor().newInstance();
                instances.put(type, found);
                LOGGER.info("IOC created {} for {}", found.getClass().getSimpleName(), type.getSimpleName());
            } catch (ReflectiveOperationException e) {
                LOGGER.error("Could not create {} for {}", className, type.getName(), e);
                throw new IllegalStateException("Could not create " + className, e);
            }
        }
        return type.cast(found);
    }
}