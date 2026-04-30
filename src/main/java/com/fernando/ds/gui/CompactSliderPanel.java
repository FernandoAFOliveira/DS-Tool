package com.fernando.ds.gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.IntConsumer;

public class CompactSliderPanel extends JPanel {

    private final String label;
    private final int min;
    private final int max;

    private int value;
    private final JButton valueButton;
    private IntConsumer changeListener;

    public CompactSliderPanel(String label, int min, int max, int initialValue) {
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = initialValue;

        setLayout(new BorderLayout(4, 0));

        JButton downButton = new JButton("◀");
        JButton upButton = new JButton("▶");

        valueButton = new JButton();
        updateText();

        downButton.addActionListener(e -> setValue(value - 1));
        upButton.addActionListener(e -> setValue(value + 1));

        add(downButton, BorderLayout.WEST);
        add(valueButton, BorderLayout.CENTER);
        add(upButton, BorderLayout.EAST);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
    }

    public void setValue(int newValue) {
        if (newValue < min || newValue > max) {
            return;
        }

        value = newValue;
        updateText();

        if (changeListener != null) {
            changeListener.accept(value);
        }
    }

    public int getValue() {
        return value;
    }

    public void setChangeListener(IntConsumer listener) {
        this.changeListener = listener;
    }

    private void updateText() {
        valueButton.setText(label + ": " + value);
    }
}