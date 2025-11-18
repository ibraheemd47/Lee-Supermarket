import PresentationLayer.GUI.SupplierGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    public JPanel mainPanel;
    private JButton supplierManagerButton;
    private JTextField helloPanel;
    private JTextField choseYourRoleTextField;
    private JButton buttoButton;

    private JFrame currentFrame; 
    public MainForm(JFrame frame) {
        this.currentFrame = frame;

        supplierManagerButton.addActionListener(e -> {
            new SupplierGUI().setVisible(true);
            currentFrame.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("My GUI App");
            MainForm mainForm = new MainForm(frame);
            frame.setContentPane(mainForm.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
