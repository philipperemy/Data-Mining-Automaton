package fr.automated.trading.systems.gui;


import fr.automated.trading.systems.tradingrobot.main.EntryPointGui;

import javax.swing.*;

public class ProcessingThread extends SwingWorker<Object, Object> {

    @Override
    public String doInBackground() {
        EntryPointGui.run(SharedGuiCache.tmpPropertiesFilename);
        return "";
    }

}

