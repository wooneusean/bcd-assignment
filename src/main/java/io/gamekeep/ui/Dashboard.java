package io.gamekeep.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard {
    private JPanel contentPane;
    private JTextField textField1;
    private JButton button1;

    public Dashboard() {
        button1.addActionListener(e -> JOptionPane.showMessageDialog(null, textField1.getText()));
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
