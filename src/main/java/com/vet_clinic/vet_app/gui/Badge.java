package com.vet_clinic.vet_app.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/** Draws a small coloured rounded square with a letter - used as the icon for each table. */
final class Badge {

    private Badge() { }

    static ImageIcon icon(char letter, Color colour, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(colour);
        g.fillRoundRect(0, 0, size, size, size / 3, size / 3);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.round(size * 0.62f)));
        FontMetrics fm = g.getFontMetrics();
        String s = String.valueOf(Character.toUpperCase(letter));
        g.drawString(s, (size - fm.stringWidth(s)) / 2f, (size - fm.getHeight()) / 2f + fm.getAscent());
        g.dispose();
        return new ImageIcon(img);
    }
}