package io.gamekeep;

import com.formdev.flatlaf.FlatLightLaf;
import io.gamekeep.ui.Dashboard;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        JFrame frame = new JFrame("Dashboard");
        frame.setContentPane(new Dashboard().getContentPane());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
