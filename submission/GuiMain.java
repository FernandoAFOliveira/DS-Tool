/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: GuiMain.java
 * 
 * GuiMain is the graphical entry point of the program.
 * It launches the Swing-based user interface.
 *
 * SwingUtilities.invokeLater is used to ensure the GUI is created
 * on the Event Dispatch Thread, which is required for thread safety.
 */

import javax.swing.SwingUtilities;

public class GuiMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}