package com.modsmanager;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.createAndShowGUI();
        });
    }
}
