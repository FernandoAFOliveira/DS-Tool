import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class DSListPanel extends JPanel {

    private final DefaultListModel<DataStructure> listModel;
    private final JList<DataStructure> dsList;

    public DSListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        setPreferredSize(new Dimension(250, 0));

        JLabel title = new JLabel("Data Structures", JLabel.CENTER);
        add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        dsList = new JList<>(listModel);
        dsList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.getName());
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 2));

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

        loadAllDataStructures();
    }

    private void loadAllDataStructures() {
        updateList(DataStructureLibrary.getAll());
    }

    public void updateList(List<DataStructure> structures) {
        listModel.clear();

        for (DataStructure ds : structures) {
            listModel.addElement(ds);
        }
    }

    public void setSelectionListener(Consumer<DataStructure> listener) {
        dsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                DataStructure selected = dsList.getSelectedValue();

                if (selected != null) {
                    listener.accept(selected);
                }
            }
        });
    }

    
}