package com.vet_clinic.vet_app.ioc;

import java.awt.Color;
import javax.swing.JInternalFrame;

/**
 * The contract every plug-in must follow. The main window only knows this interface,
 * never the concrete plug-in classes (Inversion of Control).
 */
public interface VetPlugin {

    /** Unique id, e.g. "owners". Used to stop the same plug-in loading twice. */
    String getId();

    /** Text shown on the menu item, toolbar button and window title. */
    String getName();

    /** Tool-tip text. */
    String getDescription();

    /** Menu group, e.g. "Clients". A separator is drawn between groups. */
    default String getGroup() { return "Other"; }

    /** Mnemonic key for the menu item (KeyEvent.VK_O ...), 0 = none. */
    int getMnemonic();

    /** Key used with Ctrl as the keyboard shortcut (KeyEvent.VK_1 ...), 0 = none. */
    int getShortcut();

    /** Accent colour used for the toolbar badge and the window header. */
    default Color getAccent() { return new Color(0x546E7A); }

    /** Builds the internal window. Dependencies (DAOs) are taken from the container. */
    JInternalFrame createFrame(ServiceContainer container);
}