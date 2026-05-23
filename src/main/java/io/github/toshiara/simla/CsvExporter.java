package io.github.toshiara.simla;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class CsvExporter {
    private final String lang;

    public CsvExporter(String lang) {
        this.lang = lang;
    }

    /**
     * Show dialog to save data as CSV, and save data to file.
     *
     * @param parentFrame Parent JFrame
     * @param csvData CSV data to save (array list for each row)
     */
    public void export(JFrame parentFrame, List<String[]> csvData) {

        // Set current language texts to UIManager
        //  BEFORE instantiating JFileChooser
        UIManager.put("FileChooser.fileNameLabelText",
                MultiLang.msgFilename.get(this.lang));
        UIManager.put("FileChooser.filesOfTypeLabelText",
                MultiLang.msgFiletype.get(this.lang));
        UIManager.put("FileChooser.saveButtonText",
                MultiLang.msgSave.get(this.lang));
        UIManager.put("FileChooser.cancelButtonText",
                MultiLang.msgCancel.get(this.lang));
        UIManager.put("FileChooser.acceptAllFileFilterText",
                MultiLang.fileFilterAll.get(this.lang));

        try {
            // Create the instance after UIManager configurations
            //  to apply selected language properly
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(MultiLang.filechooserSaveCsv.get(this.lang));

            // Set CSV filter
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    MultiLang.fileFilterCsv.get(this.lang), "csv");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Autocomplete extension
                if (!filePath.toLowerCase().endsWith(".csv")) {
                    fileToSave = new File(filePath + ".csv");
                }

                // Confirm overwrite
                if (fileToSave.exists()) {
                    Object[] options = {
                        MultiLang.msgYes.get(this.lang),
                        MultiLang.msgNo.get(this.lang)
                    };
                    int confirm = JOptionPane.showOptionDialog(
                            parentFrame,
                            MultiLang.msgOverwiteConfirm.get(this.lang),
                            MultiLang.titleOverwiteConfirm.get(this.lang),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                            );
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                saveToFile(parentFrame, fileToSave, csvData);
            }
        } finally {
            // Reset UIManager keys to null
            //  to restore system defaults and prevent memory leaks
            UIManager.put("FileChooser.fileNameLabelText", null);
            UIManager.put("FileChooser.filesOfTypeLabelText", null);
            UIManager.put("FileChooser.saveButtonText", null);
            UIManager.put("FileChooser.cancelButtonText", null);
            UIManager.put("FileChooser.acceptAllFileFilterText", null);
        }
    }

    private void saveToFile(JFrame parentFrame, File file, List<String[]> csvData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String[] row : csvData) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
            JOptionPane.showMessageDialog(parentFrame,
                    MultiLang.msgSaveSuccess.get(this.lang) + ": \n" +
                    file.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parentFrame,
                    MultiLang.msgSaveFailed.get(this.lang),
                    MultiLang.msgError.get(this.lang),
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
