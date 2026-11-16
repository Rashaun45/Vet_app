package com.vet_clinic.vet_app.gui;

import com.vet_clinic.vet_app.ioc.PluginManager;
import com.vet_clinic.vet_app.ioc.ServiceContainer;
import java.awt.Color;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Start-up class for the graphical interface. Run this one. */
public class VetApp {

    // Must stay the FIRST thing in the class: the GUI writes to logs/gui.log
    static {
        System.setProperty("log.name", "gui");
    }

    private static final Logger LOGGER = LogManager.getLogger(VetApp.class);

    public static void main(String[] args) {
        LOGGER.info("=== Vet Clinic GUI starting ===");

        // Anything that goes wrong inside a button click ends up here instead of vanishing.
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
            LOGGER.error("Uncaught exception in thread {}", thread.getName(), error);
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Something went wrong:\n" + error.getMessage() + "\n\nDetails are in logs/gui.log",
                    "Unexpected error", JOptionPane.ERROR_MESSAGE));
        });

        SwingUtilities.invokeLater(() -> {
            useNimbus();
            ServiceContainer ioc = new ServiceContainer("ioc.properties");
            PluginManager plugins = new PluginManager();
            plugins.loadFromConfig("plugins.properties");     // built-in plug-ins (class names)
            plugins.loadFromFolder(new File("plugins"));       // plug-in JARs found at start-up
            new MainWindow(ioc, plugins).setVisible(true);
        });
    }

    private static void useNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("nimbusBase", new Color(0x2F5D62));
            UIManager.put("nimbusSelectionBackground", new Color(0x3C8D86));
        } catch (Exception e) {
            LOGGER.warn("Could not apply the Nimbus look and feel - using the default", e);
        }
    }
}