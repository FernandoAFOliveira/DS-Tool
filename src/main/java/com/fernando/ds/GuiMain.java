package com.fernando.ds;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GuiMain {
    
        public static void main(String[] args) {

        JFrame frame = new JFrame("Data Structure Advisor");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("GUI coming soon...", JLabel.CENTER);
        frame.add(label);

        frame.setVisible(true);
    }

}
