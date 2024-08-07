package com.modsmanager;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private JComboBox<String> profileDropdown;
    private JButton exportButton;
    private JButton importButton;
    private JButton refreshButton;
    private ExportManager exportManager;
    private ImportManager importManager;
    private ProfileManager profileManager;

    public GUI() {
        profileManager = new ProfileManager();
        exportManager = new ExportManager(profileManager);
        importManager = new ImportManager(profileManager);
    }

    public void createAndShowGUI() {
        // Frame Setup
        frame = new JFrame("Mods Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout(10, 10));

        // Panels Setup
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Profile Dropdown damit alle aufgelistet sind brother.
        JLabel profileLabel = new JLabel("Select Profile: ");
        profileDropdown = new JComboBox<>(profileManager.getProfiles());
        profilePanel.add(profileLabel);
        profilePanel.add(profileDropdown);

        // Refresh Button und sooo
        refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(100, 40));
        refreshButton.addActionListener(e -> {
            profileManager.refreshProfiles();
            profileDropdown.setModel(new DefaultComboBoxModel<>(profileManager.getProfiles()));
        });
        profilePanel.add(refreshButton);

        // Export Button und sooo
        exportButton = new JButton("Export Mods");
        exportButton.setPreferredSize(new Dimension(150, 40));
        exportButton.addActionListener(e -> {
            String profile = (String) profileDropdown.getSelectedItem();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String exportPath = fileChooser.getSelectedFile().getPath();
                exportManager.exportMods(profile, exportPath);
            }
        });

        // Import Button und sooo
        importButton = new JButton("Import Mods");
        importButton.setPreferredSize(new Dimension(150, 40));
        importButton.addActionListener(e -> {
            String profile = (String) profileDropdown.getSelectedItem();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("ZIP files", "zip"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String zipFilePath = fileChooser.getSelectedFile().getPath();
                importManager.importMods(profile, zipFilePath);
            }
        });

        buttonPanel.add(exportButton);
        buttonPanel.add(importButton);

        mainPanel.add(profilePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Display the frame
        frame.setVisible(true);
    }
}
