package io.github.toshiara.simla;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;


public class Layout extends JPanel {

    // ===== public UI parts =====
    public JComboBox<String> selectLang;
    public JButton newExpButton;
    public JButton startButton;
    public JButton saveButton;
    public JButton quitButton;

    public JLabel timeLabel;
    public JLabel responseLabel;

    public JSlider speedSlider;
    public JLabel speedLabel;
    private final int SLIDER_MAX = 10;

    public ImageCanvasPanel canvasPanel;
    private final int IMAGE_WIDTH = 500;
    private final int IMAGE_HEIGHT = 310;

    // Constructor
    public Layout() {
        setLayout(new BorderLayout());

        add(this.createLeftPanel(), BorderLayout.WEST);
        add(this.createRightPanel(), BorderLayout.CENTER);
    }

    // Left
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        LayoutStyles.stylePanel(panel);

        // Language
        selectLang = new JComboBox<>(MultiLang.lang.keySet().toArray(String[]::new));
        LayoutStyles.styleComboBox(selectLang);
        Dimension pref = selectLang.getPreferredSize();
        selectLang.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        selectLang.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        newExpButton = new JButton(MultiLang.newexp.get("en"));
        startButton = new JButton(MultiLang.start.get("en"));
        saveButton = new JButton(MultiLang.save.get("en"));
        quitButton = new JButton(MultiLang.quit.get("en"));

        LayoutStyles.styleButton(newExpButton);
        LayoutStyles.styleButton(startButton);
        LayoutStyles.styleButton(saveButton);
        LayoutStyles.styleButton(quitButton);

        this.setComponentMaxHeight(newExpButton);
        this.setComponentMaxHeight(startButton);
        this.setComponentMaxHeight(saveButton);
        this.setComponentMaxHeight(quitButton);

        newExpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Time
        timeLabel = new JLabel(" 0:00:00");
        LayoutStyles.styleTimeLabel(timeLabel);
        this.setComponentMaxHeight(timeLabel);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Response
        responseLabel = new JLabel("");
        LayoutStyles.styleResponseLabel(responseLabel);
        responseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // placement
        panel.add(Box.createVerticalStrut(20));
        panel.add(selectLang);
        panel.add(Box.createVerticalStrut(20));
        panel.add(newExpButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(saveButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(quitButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(timeLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(responseLabel);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // Right
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        LayoutStyles.stylePanel(panel);

        // Slider
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setOpaque(false);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        speedLabel = new JLabel("Speed: x1");
        speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        speedSlider = new JSlider(1, SLIDER_MAX, 1);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        sliderPanel.add(speedLabel);
        sliderPanel.add(speedSlider);

        // Canvas (base)
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new GridBagLayout());
        basePanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        basePanel.setBackground(Color.WHITE);

        // Canvas (overlay)
        canvasPanel = new ImageCanvasPanel();
        canvasPanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        canvasPanel.setBackground(Color.WHITE);

        // placement
        panel.add(sliderPanel, BorderLayout.NORTH);
        panel.add(basePanel, BorderLayout.CENTER);
        basePanel.add(canvasPanel);

        return panel;
    }

    private void setComponentMaxHeight(JComponent comp) {
        Dimension pref = comp.getPreferredSize();
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
    }

    public ImageCanvasPanel getCanvasPanel() {
        return this.canvasPanel;
    }
}

