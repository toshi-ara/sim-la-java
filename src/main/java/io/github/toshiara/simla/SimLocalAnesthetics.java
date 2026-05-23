package io.github.toshiara.simla;

import io.github.toshiara.variabletimer.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SimLocalAnesthetics extends JFrame {
    private final int APP_WIDTH = 800;
    private final int APP_HEIGHT = 480;

    private Layout widget;
    private String lang = "en";
    private Map<String, String> currentButtonStatus = MultiLang.start;

    // Main timer
    private Timer uiTimer;
    private VariableTimer mainTimer = new VariableTimer();
    private long currentTime = 0;

    // Canvas/Image
    private Image back1;
    private Image back2;
    private ImageCanvasPanel canvasPanel;

    // for Response
    private Timer rollbackTimer;
    private boolean isResponse = false;
    private boolean isProcessing = false;  // prevent double-click

    // Drug parameters/Response
    private StatModel drug;

    // Object to save experimental data
    List<String[]> data = new ArrayList<>();

    // main function
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                // UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ignored) {}

            new SimLocalAnesthetics().setVisible(true);
        });
    }

    // Constructor
    public SimLocalAnesthetics() {
        super("Simulator of Local Anesthetics-" + Version.version);

        // apply styles (must be at first)
        LayoutStyles.applyGlobal();

        setSize(this.APP_WIDTH, this.APP_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        widget = new Layout();
        setContentPane(widget);

        // Canvas/Images
        this.canvasPanel = widget.getCanvasPanel();
        back1 = this.loadImage("/io/github/toshiara/simla/images/back1.png");
        back2 = this.loadImage("/io/github/toshiara/simla/images/back2.png");

        canvasPanel.setImage(back1);
        canvasPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handlePanelClick(e);
            }
        });

        // set various functions
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.bindCallbackFunctions();
        this.setupCloseHandler();

        // set main Timer
        uiTimer = new Timer(Constants.TIMER_INTERVAL, e -> {
            mainTimer.update();
            long time = mainTimer.getSecond();
            if (time == this.currentTime) {
                return;
            }

            this.currentTime = time;
            widget.timeLabel.setText(this.timeFormat(time));
            // Immediately flush the drawing buffer
            Toolkit.getDefaultToolkit().sync();
        });

        // set rollback Timer (from response)
        rollbackTimer = new Timer(Constants.ACTIVE_DURATION, event -> {
            if (this.isResponse) {
                widget.timeLabel.setForeground(Color.BLACK);
                widget.responseLabel.setForeground(Color.BLACK);
                canvasPanel.setImage(back1);
            }
            widget.responseLabel.setText("");
            this.isProcessing = false;
        });
        rollbackTimer.setRepeats(false);

        // set parameters of drugs (initialize)
        drug = new StatModel();

        // header line for CSV file
        data.clear();
        data.add(new String[] {"Time", "Drug", "Response"});

        this.pack();
    }

    // support method to read Image
    private Image loadImage(String path) {
        java.net.URL url = SimLocalAnesthetics.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url).getImage();
        }
        System.err.println("No resouce found: " + path);
        return null;
    }

    // set Events (functions are defined as follows)
    private void bindCallbackFunctions() {
        // Combobox to select Language
        widget.selectLang.addActionListener(e -> this.selectLang());

        // Buttons
        widget.newExpButton.addActionListener(e -> this.newExp());
        widget.startButton.addActionListener(e -> this.startExp());
        widget.saveButton.addActionListener(e -> this.saveData());
        widget.quitButton.addActionListener(e -> this.confirmExit());

        // Slider
        widget.speedSlider.addChangeListener(e -> this.updateSpeed());
    }

    // Close window
    private void setupCloseHandler() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }

    ////////////////////////////////////////
    // Callback functions
    ////////////////////////////////////////

    // select language
    private void selectLang() {
        var selectedLang = MultiLang.lang.get(widget.selectLang.getSelectedItem().toString());
        if (selectedLang == this.lang) {
            return;
        }
        this.lang = selectedLang;

        // change labels (Buttons)
        widget.newExpButton.setText(MultiLang.newexp.get(this.lang));
        widget.startButton.setText(this.currentButtonStatus.get(this.lang));
        widget.saveButton.setText(MultiLang.save.get(this.lang));
        widget.quitButton.setText(MultiLang.quit.get(this.lang));

        // change label (Slider)
        int speed = widget.speedSlider.getValue();
        widget.speedLabel.setText(MultiLang.speed.get(this.lang) + ": x" + speed);
    }

    // New experiment button
    private void newExp() {
        Object[] options = {
            MultiLang.msgYes.get(this.lang),
            MultiLang.msgNo.get(this.lang)
        };
        int confirm = JOptionPane.showOptionDialog(
                this,
                MultiLang.msgNewExp.get(this.lang),
                MultiLang.msgConfirm.get(this.lang),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
                );

        if (confirm != 0 || mainTimer.isRunning()) {
            return;
        }

        // regenerate drug parameters
        drug.setDrugParameter();

        // Initialize (except Language)
        this.currentTime = 0;
        this.currentButtonStatus = MultiLang.start;
        widget.startButton.setText(this.currentButtonStatus.get(this.lang));

        mainTimer.reset();
        widget.timeLabel.setText(" 0:00:00");

        int speed = 1;
        widget.speedLabel.setText(MultiLang.speed.get(this.lang) + ": x" + speed);
        mainTimer.changeSpeed(speed);
        widget.speedSlider.setValue(speed);

        // header line for CSV file
        data.clear();
        data.add(new String[] {"Time", "Drug", "Response"});
    }

    // Start/Pause/Restart button
    private void startExp() {
        if (mainTimer.isRunning()) {
            mainTimer.pause();
            uiTimer.stop();
            widget.timeLabel.setText(this.timeFormat(mainTimer.getSecond()));

            this.currentButtonStatus = MultiLang.restart;
            widget.startButton.setText(this.currentButtonStatus.get(this.lang));
            // Pause => enable Buttons
            widget.selectLang.setEnabled(true);
            widget.newExpButton.setEnabled(true);
            widget.saveButton.setEnabled(true);
            widget.quitButton.setEnabled(true);
        } else {
            mainTimer.start();
            uiTimer.start();

            this.currentButtonStatus = MultiLang.pause;
            widget.startButton.setText(this.currentButtonStatus.get(this.lang));
            // Start/Resart => disable Buttons
            widget.selectLang.setEnabled(false);
            widget.newExpButton.setEnabled(false);
            widget.saveButton.setEnabled(false);
            widget.quitButton.setEnabled(false);
        }
    }

    // Stop button
    private void confirmExit() {
        Object[] options = {
            MultiLang.msgYes.get(this.lang),
            MultiLang.msgNo.get(this.lang)
        };
        int confirm = JOptionPane.showOptionDialog(
                this,
                MultiLang.msgQuit.get(this.lang),
                MultiLang.msgConfirm.get(this.lang),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
                );

        if (confirm == 0) {
            dispose();
            System.exit(0);
        }
    }

    // Change Slider
    private void updateSpeed() {
        int speed = widget.speedSlider.getValue();
        widget.speedLabel.setText(MultiLang.speed.get(this.lang) + ": x" + speed);
        mainTimer.changeSpeed(speed);
    }

    // Save data as CSV
    private void saveData() {
        // generate object to apply language selected currently
        var exporter = new CsvExporter(this.lang);
        exporter.export(this, data);
    }


    // handler to click imagePanel
    private void handlePanelClick(MouseEvent e) {
        if (!mainTimer.isRunning() || this.isProcessing) {
            return;
        }

        int drugType = getDrugTypeIndex(e.getX(), e.getY());
        if (drugType < 0) {
            return;
        }

        double time = mainTimer.getSecond() / 60;
        this.isResponse = drug.isResponse(drugType, time);

        data.add(new String[] {
            String.format("%.1f", time),
            Constants.drugName[drugType],
            this.isResponse ? "1" : "0"
           }
        );
        this.showResponse(this.isResponse);
    }

    private int getDrugTypeIndex(int x, int y) {
        int i;
        for (i = 0; i < Constants.CIRCLES.length; i++) {
            double dX = x - Constants.CIRCLES[i][0];
            double dY = y - Constants.CIRCLES[i][1];

            if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(Constants.RADIUS, 2)) {
                return i;
            }
        }
        return -1;
    }


    ////////////////////////////////////////
    // other function
    ////////////////////////////////////////

    // Display result below Timer
    private void showResponse(boolean isResponse) {
        this.isProcessing= true;

        if (isResponse) {
            canvasPanel.setImage(back2);
            widget.timeLabel.setForeground(Color.RED);
            widget.responseLabel.setForeground(Color.RED);
            widget.responseLabel.setText(MultiLang.withResponse.get(this.lang));
        } else {
            widget.responseLabel.setText(MultiLang.withoutResponse.get(this.lang));
        }
        rollbackTimer.start();
    }

    // format time as 0:00:00
    private String timeFormat(long t) {
        long hours = t / 3600;
        long minutes = (t % 3600) / 60;
        long seconds = t % 60;

        StringBuilder sb = new StringBuilder(8);
        if (hours < 10) sb.append(' ');
        sb.append(hours).append(':');
        if (minutes < 10) sb.append('0');
        sb.append(minutes).append(':');
        if (seconds < 10) sb.append('0');
        sb.append(seconds);

        return sb.toString();
    }


}

