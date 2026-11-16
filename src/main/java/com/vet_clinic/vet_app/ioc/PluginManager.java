package com.vet_clinic.vet_app.ioc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Finds and loads plug-ins. Two sources:
 *  1. plugins.properties - a list of class names, created by reflection (the built-in plug-ins).
 *  2. JAR files - loaded while the program is running with a URLClassLoader + ServiceLoader.
 */
public class PluginManager {

    private static final Logger LOGGER = LogManager.getLogger(PluginManager.class);

    private final List<VetPlugin> plugins = new ArrayList<>();
    private final List<Consumer<VetPlugin>> listeners = new ArrayList<>();
    private final List<URLClassLoader> loaders = new ArrayList<>();
    private final Set<String> loadedJars = new HashSet<>();

    /** The main window uses this to add a menu item as soon as a plug-in appears. */
    public void addListener(Consumer<VetPlugin> listener) {
        listeners.add(listener);
    }

    public List<VetPlugin> getPlugins() {
        return List.copyOf(plugins);
    }

    /** Reads plugins.properties (plugin.1=com.example.MyPlugin ...) and creates each class. */
    public int loadFromConfig(String resourceName) {
        Properties props = new Properties();
        try (InputStream in = PluginManager.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                LOGGER.error("Plug-in configuration file {} was not found", resourceName);
                return 0;
            }
            props.load(in);
        } catch (IOException e) {
            LOGGER.error("Could not read the plug-in configuration {}", resourceName, e);
            return 0;
        }

        List<String> keys = new ArrayList<>(props.stringPropertyNames());
        keys.removeIf(k -> !k.startsWith("plugin."));
        keys.sort(Comparator.comparingInt(PluginManager::numberOf));

        int count = 0;
        for (String key : keys) {
            String className = props.getProperty(key).trim();
            try {
                VetPlugin plugin = (VetPlugin) Class.forName(className).getDeclaredConstructor().newInstance();
                if (register(plugin, "configuration")) {
                    count++;
                }
            } catch (ReflectiveOperationException | ClassCastException e) {
                LOGGER.error("Could not load the plug-in class {}", className, e);
            }
        }
        LOGGER.info("{} plug-ins loaded from {}", count, resourceName);
        return count;
    }

    /** Loads every .jar in a folder (used at start-up and by Plug-ins > Reload). */
    public int loadFromFolder(File folder) {
        File[] jars = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
        if (jars == null) {
            LOGGER.info("Plug-in folder {} does not exist - no external plug-ins loaded", folder.getAbsolutePath());
            return 0;
        }
        int count = 0;
        for (File jar : jars) {
            count += loadFromJar(jar);
        }
        return count;
    }

    /** Loads the plug-ins inside one JAR at runtime. Returns how many new plug-ins were added. */
    public int loadFromJar(File jar) {
        if (!loadedJars.add(jar.getAbsolutePath())) {
            LOGGER.info("{} was already loaded", jar.getName());
            return 0;
        }
        int count = 0;
        try {
            URLClassLoader loader = new URLClassLoader(new URL[]{jar.toURI().toURL()},
                    PluginManager.class.getClassLoader());
            loaders.add(loader);
            for (VetPlugin plugin : ServiceLoader.load(VetPlugin.class, loader)) {
                if (register(plugin, jar.getName())) {
                    count++;
                }
            }
            LOGGER.info("{} plug-in(s) loaded from {}", count, jar.getName());
        } catch (IOException | ServiceConfigurationError e) {
            loadedJars.remove(jar.getAbsolutePath());
            LOGGER.error("Could not load plug-ins from {}", jar.getName(), e);
        }
        return count;
    }

    private boolean register(VetPlugin plugin, String source) {
        for (VetPlugin existing : plugins) {
            if (existing.getId().equals(plugin.getId())) {
                LOGGER.warn("Plug-in id '{}' from {} is already loaded - skipped", plugin.getId(), source);
                return false;
            }
        }
        plugins.add(plugin);
        LOGGER.info("Plug-in '{}' ({}) loaded from {}", plugin.getName(), plugin.getClass().getName(), source);
        listeners.forEach(l -> l.accept(plugin));
        return true;
    }

    private static int numberOf(String key) {
        try {
            return Integer.parseInt(key.substring("plugin.".length()));
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
}