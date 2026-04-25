package com.fernando.ds.gui;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.library.QuestionLibrary;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;
import com.fernando.ds.model.Preference;

public class QuestionPanel extends JPanel {

    private Consumer<QuestionInfo> questionSelectionListener;
    private JPanel selectedCard = null;
    private BiConsumer<QuestionInfo, Preference> preferenceSelectionListener;
    private BiConsumer<QuestionInfo, Integer> weightSelectionListener;

    public QuestionPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

       JLabel title = new JLabel("Questions", JLabel.CENTER);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0));
            add(title);

        for (QuestionInfo q : QuestionLibrary.getAll()) {

        JPanel row;

        if (q.getType() == QuestionInfo.QuestionType.YES_NO) {
            row = createYesNoRow(q);
        } else if (q.getType() == QuestionInfo.QuestionType.SCALE) {
            row = createSliderRow(q);
        } else {
            throw new IllegalArgumentException("Unknown question type: " + q.getType());
        }

        JPanel card = wrapQuestionCard(row);
        add(card);
        addCardClickBehavior(card, card, q);
        }
        
    }

    public void setPreferenceSelectionListener(BiConsumer<QuestionInfo, Preference> listener) {
        this.preferenceSelectionListener = listener;
    }

    public void setWeightSelectionListener(BiConsumer<QuestionInfo, Integer> listener) {
        this.weightSelectionListener = listener;
    }

    public void setQuestionSelectionListener(Consumer<QuestionInfo> listener) {
        this.questionSelectionListener = listener;
    }

    private JPanel createYesNoRow(QuestionInfo q) {
        JPanel row = new JPanel(new BorderLayout(2, 2));
        row.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JLabel label = createClickableQuestionLabel(q);

        JPanel options = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JRadioButton yes = new JRadioButton("Yes");
        JRadioButton no = new JRadioButton("No");
        JRadioButton any = new JRadioButton("Any", true);

        ButtonGroup group = new ButtonGroup();
        group.add(yes);
        group.add(no);
        group.add(any);

        options.add(yes);
        options.add(no);
        options.add(any);

        row.add(label, BorderLayout.NORTH);
        row.add(options, BorderLayout.CENTER);

        yes.addActionListener(e -> {
            if (preferenceSelectionListener != null) {
                preferenceSelectionListener.accept(q, Preference.YES);
            }
        });

        no.addActionListener(e -> {
            if (preferenceSelectionListener != null) {
                preferenceSelectionListener.accept(q, Preference.NO);
            }
        });

        any.addActionListener(e -> {
            if (preferenceSelectionListener != null) {
                preferenceSelectionListener.accept(q, Preference.ANY);
            }
        });

        return row;
    }

    private JPanel createSliderRow(QuestionInfo q) {
        JPanel row = new JPanel(new BorderLayout(2, 4));
        row.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JLabel label = createClickableQuestionLabel(q);

        JSlider slider = new JSlider(0, 5, 0);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        
        row.add(label, BorderLayout.NORTH);
        row.add(slider, BorderLayout.CENTER);

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                if (weightSelectionListener != null) {
                    weightSelectionListener.accept(q, slider.getValue());
                }

                if (questionSelectionListener != null) {
                    questionSelectionListener.accept(q);
                }
            }
        });

        return row;
    }

    private JLabel createClickableQuestionLabel(QuestionInfo q) {
        JLabel label = new JLabel(q.getShortText());
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }

    private JPanel wrapQuestionCard(JPanel content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        card.add(content, BorderLayout.CENTER);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

        return card;
    }



    private void selectQuestionCard(JPanel card, QuestionInfo q) {
        if (selectedCard != null) {
            selectedCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }

        selectedCard = card;
        selectedCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLUE, 2),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        if (questionSelectionListener != null) {
            questionSelectionListener.accept(q);
        }
    }

    private void addCardClickBehavior(Component component, JPanel card, QuestionInfo q) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectQuestionCard(card, q);
            }
        });

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                addCardClickBehavior(child, card, q);
            }
        }
    }

}