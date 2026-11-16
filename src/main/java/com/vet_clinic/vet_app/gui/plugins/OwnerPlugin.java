package com.vet_clinic.vet_app.gui.plugins;

import com.vet_clinic.vet_app.gui.forms.OwnersFrame;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class OwnerPlugin extends FramePlugin {

    public OwnerPlugin() {
        super("owners", "Owners", "Pet owners (clients) of the clinic", "Clients",
                KeyEvent.VK_O, KeyEvent.VK_1, new Color(0x2E7D6B), OwnersFrame::new);
    }
}