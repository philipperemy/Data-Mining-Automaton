package fr.automated.trading.systems.gui;

import javax.swing.*;

public class ProcessingFrame extends JFrame {

    public ProcessingFrame(String output, int width, int height) {
        Box mainFrameContainer = Box.createVerticalBox();
        JLabel jLabel = new JLabel(output);
        setTitle(Texts.MAIN_FRAME_TITLE);
        setSize(width, height);
        setLocationRelativeTo(null);
        JPanel jPanel = new JPanel();
        setContentPane(jPanel);
        mainFrameContainer.add(jLabel);
        add(mainFrameContainer);
    }


    public ProcessingFrame(String output) {
        this(output, 500, 300);
    }

    public void showFrame() {
        setAlwaysOnTop(true);
        setVisible(true);
    }

    public void hideFrame() {
        setVisible(false);
    }

}
