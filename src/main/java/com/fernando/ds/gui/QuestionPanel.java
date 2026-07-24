package com.fernando.ds.gui;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.library.QuestionLibrary;
import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class QuestionPanel extends JPanel {

    private Consumer<QuestionInfo> questionSelectionListener;
    private JPanel selectedCard = null;
    private BiConsumer<QuestionInfo, Preference> preferenceSelectionListener;
    private BiConsumer<QuestionInfo, Integer> weightSelectionListener;
    private JRadioButton duplicateYesButton;
    private JRadioButton duplicateNoButton;
    private JRadioButton duplicateAnyButton;
    private BiConsumer<QuestionInfo, RemovalOrder> removalOrderSelectionListener;
    private final Map<QuestionInfo.QuestionId, JRadioButton> yesButtons =
        new EnumMap<>(QuestionInfo.QuestionId.class);
    private final Map<QuestionInfo.QuestionId, JRadioButton> noButtons =
        new EnumMap<>(QuestionInfo.QuestionId.class);
    private final Map<QuestionInfo.QuestionId, JRadioButton> preferenceAnyButtons =
        new EnumMap<>(QuestionInfo.QuestionId.class);
    private final Map<QuestionInfo.QuestionId, CompactSliderPanel> slidersByQuestion =
        new EnumMap<>(QuestionInfo.QuestionId.class);
    private final Map<RemovalOrder, JRadioButton> removalOrderButtons =
        new EnumMap<>(RemovalOrder.class);

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
        } else if (q.getType() == QuestionInfo.QuestionType.REMOVAL_ORDER) {
            row = createRemovalOrderRow(q);
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

    public void setRemovalOrderSelectionListener(BiConsumer<QuestionInfo, RemovalOrder> listener) {
        this.removalOrderSelectionListener = listener;
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
        yesButtons.put(q.getId(), yes);
        noButtons.put(q.getId(), no);
        preferenceAnyButtons.put(q.getId(), any);

        if (q.getId() == QuestionInfo.QuestionId.DUPLICATES) {
            duplicateYesButton = yes;
            duplicateNoButton = no;
            duplicateAnyButton = any;
        }

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

        CompactSliderPanel slider = new CompactSliderPanel(q.getShortText(), 0, 5, 5);
        slidersByQuestion.put(q.getId(), slider);

        slider.setChangeListener(value -> {
            if (weightSelectionListener != null) {
                weightSelectionListener.accept(q, value);
            }

            if (questionSelectionListener != null) {
                questionSelectionListener.accept(q);
            }
        });

        row.add(slider, BorderLayout.CENTER);

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

    public void applyAnswers(DSRequirements answers) {
        selectPreference(
            QuestionInfo.QuestionId.KEY_VALUE,
            answers.getKeyValuePreference()
        );
        selectPreference(
            QuestionInfo.QuestionId.DUPLICATES,
            answers.getDuplicatePreference()
        );
        selectPreference(
            QuestionInfo.QuestionId.SORTED,
            answers.getSortedPreference()
        );
        selectPreference(
            QuestionInfo.QuestionId.INDEXED,
            answers.getIndexedPreference()
        );

        slidersByQuestion.get(QuestionInfo.QuestionId.LOOKUP)
            .setValueSilently(answers.getLookupWeight());
        slidersByQuestion.get(QuestionInfo.QuestionId.ADD_DELETE)
            .setValueSilently(answers.getAddDeleteWeight());
        slidersByQuestion.get(QuestionInfo.QuestionId.MEMORY)
            .setValueSilently(answers.getMemoryWeight());
        removalOrderButtons.get(answers.getRemovalOrderPreference())
            .setSelected(true);
        setDuplicateQuestionEnabled(
            answers.getKeyValuePreference() != Preference.YES
        );
    }

    private void selectPreference(
        QuestionInfo.QuestionId questionId,
        Preference preference
    ) {
        JRadioButton button = switch (preference) {
            case YES -> yesButtons.get(questionId);
            case NO -> noButtons.get(questionId);
            case ANY -> preferenceAnyButtons.get(questionId);
        };
        button.setSelected(true);
    }

    public void clearSelectedQuestion() {
        if (selectedCard != null) {
            selectedCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
            selectedCard = null;
        }
    }

    public void setDuplicateQuestionEnabled(boolean enabled) {
        if (duplicateYesButton != null) {
            duplicateYesButton.setEnabled(enabled);
        }

        if (duplicateNoButton != null) {
            duplicateNoButton.setEnabled(enabled);
        }

        if (duplicateAnyButton != null) {
            duplicateAnyButton.setEnabled(enabled);
            if (!enabled) {
                duplicateAnyButton.setSelected(true);
            }
        }
    }

    private JPanel createRemovalOrderRow(QuestionInfo q) {
        JPanel row = new JPanel(new BorderLayout(2, 2));
        row.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JLabel label = createClickableQuestionLabel(q);

        JPanel options = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));

        JRadioButton any = new JRadioButton("Any", true);
        JRadioButton fifo = new JRadioButton("FIFO");
        JRadioButton lifo = new JRadioButton("LIFO");
        JRadioButton deque = new JRadioButton("DE");
        JRadioButton priority = new JRadioButton("PRI");

        ButtonGroup group = new ButtonGroup();
        group.add(any);
        group.add(fifo);
        group.add(lifo);
        group.add(deque);
        group.add(priority);

        removalOrderButtons.put(RemovalOrder.ANY, any);
        removalOrderButtons.put(RemovalOrder.FIFO, fifo);
        removalOrderButtons.put(RemovalOrder.LIFO, lifo);
        removalOrderButtons.put(RemovalOrder.DOUBLE_ENDED, deque);
        removalOrderButtons.put(RemovalOrder.PRIORITY, priority);

        options.add(any);
        options.add(fifo);
        options.add(lifo);
        options.add(deque);
        options.add(priority);

        row.add(label, BorderLayout.NORTH);
        row.add(options, BorderLayout.CENTER);

        any.addActionListener(e -> {
            if (removalOrderSelectionListener != null) {
                removalOrderSelectionListener.accept(q, RemovalOrder.ANY);
            }
            if (questionSelectionListener != null) {
                questionSelectionListener.accept(q);
            }
        });

        fifo.addActionListener(e -> {
            if (removalOrderSelectionListener != null) {
                removalOrderSelectionListener.accept(q, RemovalOrder.FIFO);
            }
            if (questionSelectionListener != null) {
                questionSelectionListener.accept(q);
            }
        });

        lifo.addActionListener(e -> {
            if (removalOrderSelectionListener != null) {
                removalOrderSelectionListener.accept(q, RemovalOrder.LIFO);
            }
            if (questionSelectionListener != null) {
                questionSelectionListener.accept(q);
            }
        });

        deque.addActionListener(e -> {
            if (removalOrderSelectionListener != null) {
                removalOrderSelectionListener.accept(q, RemovalOrder.DOUBLE_ENDED);
            }
            if (questionSelectionListener != null) {
                questionSelectionListener.accept(q);
            }
        });

        priority.addActionListener(e -> {
            if (removalOrderSelectionListener != null) {
                removalOrderSelectionListener.accept(q, RemovalOrder.PRIORITY);
            }
            if (questionSelectionListener != null) {
                questionSelectionListener.accept(q);
            }
        });

        return row;
    }

}