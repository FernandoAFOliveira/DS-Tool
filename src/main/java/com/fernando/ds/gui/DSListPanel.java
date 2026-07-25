package com.fernando.ds.gui;

import com.fernando.ds.model.DataStructure;
import com.fernando.ds.knowledge.StructureId;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class DSListPanel extends JPanel {

    private final DefaultListModel<DataStructure> listModel;
    private final JList<DataStructure> dsList;
    private boolean updatingList;

    public DSListPanel() {
        this(2, 8, 2);
    }

    DSListPanel(
        int verticalCellPadding,
        int leftCellPadding,
        int rightCellPadding
    ) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        JLabel title = new JLabel("Data Structures", JLabel.CENTER);
        add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        dsList = new JList<>(listModel);
        dsList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.getName());
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(
                verticalCellPadding,
                leftCellPadding,
                verticalCellPadding,
                rightCellPadding
            ));

            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }

            return label;
        });

        dsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(dsList), BorderLayout.CENTER);
    }

    public void updateList(
        List<DataStructure> structures,
        StructureId selectedStructureId
    ) {
        updatingList = true;
        try {
            listModel.clear();
            int selectedIndex = -1;

            for (int index = 0; index < structures.size(); index++) {
                DataStructure dataStructure = structures.get(index);
                listModel.addElement(dataStructure);
                if (dataStructure.getStructureId() == selectedStructureId) {
                    selectedIndex = index;
                }
            }

            if (selectedIndex >= 0) {
                dsList.setSelectedIndex(selectedIndex);
            }
        } finally {
            updatingList = false;
        }
    }

    public void setSelectionListener(Consumer<DataStructure> listener) {
        dsList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && !updatingList) {
                DataStructure selected = dsList.getSelectedValue();

                if (selected != null) {
                    listener.accept(selected);
                }
            }
        });
    }
}
