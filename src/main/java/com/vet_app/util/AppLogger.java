package com.vet_app.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** Sends log records to the console (INFO and above) and to vet_app.log (everything). */
public final class AppLogger {

    private static boolean configured = false;

    private AppLogger() {
    }

    public static synchronized Logger get(Class<?> owner) {
        if (!configured) {
            configure();
            configured = true;
        }
        return Logger.getLogger(owner.getName());
    }

    private static void configure() {
        Logger root = Logger.getLogger("com.vetapp");
        root.setUseParentHandlers(false);
        root.setLevel(Level.ALL);

        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(Level.INFO);
        console.setFormatter(new SimpleFormatter());
        root.addHandler(console);

        try {
            FileHandler file = new FileHandler("vet_app.log", true);
            file.setLevel(Level.ALL);
            file.setFormatter(new SimpleFormatter());
            root.addHandler(file);
        } catch (IOException e) {
            root.log(Level.WARNING, "File logging is not available", e);
        }
    }
}