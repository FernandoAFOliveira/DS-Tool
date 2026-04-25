package com.fernando.ds;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GuiMain {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Data Structure Advisor");

        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("GUI coming soon...", SwingConstants.CENTER);
        frame.add(label);

        frame.setVisible(true);
    }
}