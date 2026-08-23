package com.fernando.ds;

import com.fernando.ds.gui.MainFrame;
import javax.swing.SwingUtilities;

public class GuiMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}