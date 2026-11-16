package com.vet_clinic.vet_app.gui.plugins;

import com.vet_clinic.vet_app.ioc.ServiceContainer;
import com.vet_clinic.vet_app.ioc.VetPlugin;
import java.awt.Color;
import java.util.function.Function;
import javax.swing.JInternalFrame;

/** A plug-in that opens one of the internal forms. Each table plug-in extends this. */
public class FramePlugin implements VetPlugin {

    private final String id, name, description, group;
    private final int mnemonic, shortcut;
    private final Color accent;
    private final Function<ServiceContainer, JInternalFrame> formFactory;

    protected FramePlugin(String id, String name, String description, String group, int mnemonic, int shortcut,
                          Color accent, Function<ServiceContainer, JInternalFrame> formFactory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.group = group;
        this.mnemonic = mnemonic;
        this.shortcut = shortcut;
        this.accent = accent;
        this.formFactory = formFactory;
    }

    @Override public String getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getDescription() { return description; }
    @Override public String getGroup() { return group; }
    @Override public int getMnemonic() { return mnemonic; }
    @Override public int getShortcut() { return shortcut; }
    @Override public Color getAccent() { return accent; }

    @Override
    public JInternalFrame createFrame(ServiceContainer container) {
        return formFactory.apply(container);
    }
}