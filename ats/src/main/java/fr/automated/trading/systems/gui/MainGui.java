package fr.automated.trading.systems.gui;

import javax.swing.*;
import java.io.File;

public class MainGui {

    public static final String propertiesFilename = "properties/parameters-gui.properties";

    public static void main(String[] args) {

        boolean PropertiesFileExists = (new File(MainGui.propertiesFilename)).exists();
        if (!PropertiesFileExists) {
            JOptionPane.showMessageDialog(null, "Cannot find properties files", "Fatal Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        new MainFrame();
    }
}
