
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class LeftPanel extends JPanel {

    public LeftPanel(QuestionPanel questionPanel, DSListPanel dsListPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0));

        add(questionPanel, BorderLayout.NORTH);
        add(dsListPanel, BorderLayout.CENTER);
    }
}