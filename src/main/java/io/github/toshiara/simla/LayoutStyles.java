package io.github.toshiara.simla;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;


public final class LayoutStyles {
    // ==========
    // Fonts
    // ==========
    private static final Font FONT_NORMAL = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
    private static final Font FONT_TIME = new Font(Font.MONOSPACED, Font.PLAIN, 54);
    private static final Font FONT_RESPONSE = new Font(Font.SANS_SERIF, Font.PLAIN, 36);
    private static final Font FONT_SPEED = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
    private static final Font FONT_SLIDER = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

    private static final Font FONT_TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 18);

    // ==========
    // Colors
    // ==========
    private static final Color BG_MAIN = new Color(245, 245, 245);
    private static final Color BG_PANEL = new Color(230, 230, 230);
    private static final Color FG_TEXT = Color.DARK_GRAY;

    // ==========
    // Global (CSS tag selector)
    // ==========
    public static void applyGlobal() {
        UIManager.put("Button.font", FONT_NORMAL);
        UIManager.put("Label.font", FONT_NORMAL);
        UIManager.put("ComboBox.font", FONT_NORMAL);
        UIManager.put("Slider.font", FONT_SLIDER);

        UIManager.put("Panel.background", BG_MAIN);
    }

    // ==========
    // Component styles (CSS class)
    // ==========
    public static void styleComboBox(JComboBox<?> c) {
        c.setFont(FONT_NORMAL);
    }

    public static void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setFont(FONT_NORMAL);
    }

    public static void styleTimeLabel(JLabel b) {
        b.setFont(FONT_TIME);
    }

    public static void styleResponseLabel(JLabel b) {
        b.setFont(FONT_RESPONSE);
    }

    public static void stylePanel(JPanel p) {
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private LayoutStyles() {} // Preventing instantiation
}

