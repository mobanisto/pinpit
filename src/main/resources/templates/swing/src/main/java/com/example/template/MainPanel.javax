package com.example.template;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class MainPanel extends JPanel {

    private int counter = 10;
    private JTextField textField;

    public MainPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.NORTH;

        JButton buttonIncrease = new JButton("increase");
        JButton buttonDecrease = new JButton("decrease");
        textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setEditable(false);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.0;
        add(buttonIncrease, c);
        add(buttonDecrease, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.0;
        add(textField, c);

        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(new JPanel(), c);

        updateText();

        buttonIncrease.addActionListener(e -> {
            counter++;
            updateText();
        });
        buttonDecrease.addActionListener(e -> {
            counter--;
            updateText();
        });
    }

    private void updateText() {
        textField.setText(String.format("%d", counter));
    }

}
