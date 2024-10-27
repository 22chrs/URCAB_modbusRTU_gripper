// GripperProgramNodeView.java

package com.ur.thph.modbus_urcap.impl;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GripperProgramNodeView implements SwingProgramNodeView<GripperProgramNodeContribution> {

    private JComboBox<String> actionComboBox;
    private JTextField positionTextField;
    private JComboBox<String> colorComboBox;
    private JTextField fanSpeedTextField;

    private JLabel positionLabel;
    private JLabel colorLabel;
    private JLabel fanSpeedLabel;

    public GripperProgramNodeView(ViewAPIProvider apiProvider) {
        // Constructor
    }

    @Override
    public void buildUI(JPanel panel, final GripperProgramNodeContribution contribution) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Action selection
        Box actionBox = Box.createHorizontalBox();
        actionBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel actionLabel = new JLabel("Action:");
        actionComboBox = new JComboBox<String>(new String[] {
            "Move Gripper and Wait",
            "Move Gripper without Wait",
            "Set LED Color",
            "Home Gripper",
            "Set Fan Speed"
        });
        actionComboBox.setMaximumSize(new Dimension(200, 30));

        actionBox.add(actionLabel);
        actionBox.add(Box.createHorizontalStrut(10));
        actionBox.add(actionComboBox);

        panel.add(actionBox);
        panel.add(Box.createVerticalStrut(10));

        // Position input
        Box positionBox = Box.createHorizontalBox();
        positionBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        positionLabel = new JLabel("Gripper Position:");
        positionTextField = new JTextField();
        positionTextField.setMaximumSize(new Dimension(200, 30));

        positionBox.add(positionLabel);
        positionBox.add(Box.createHorizontalStrut(10));
        positionBox.add(positionTextField);

        panel.add(positionBox);
        panel.add(Box.createVerticalStrut(10));

        // Color selection
        Box colorBox = Box.createHorizontalBox();
        colorBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        colorLabel = new JLabel("LED Color:");
        colorComboBox = new JComboBox<String>(new String[] {
            "red", "green", "blue", "white", "purple", "pink", "orange", "yellow"
        });
        colorComboBox.setMaximumSize(new Dimension(200, 30));

        colorBox.add(colorLabel);
        colorBox.add(Box.createHorizontalStrut(10));
        colorBox.add(colorComboBox);

        panel.add(colorBox);
        panel.add(Box.createVerticalStrut(10));

        // Fan Speed input
        Box fanSpeedBox = Box.createHorizontalBox();
        fanSpeedBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        fanSpeedLabel = new JLabel("Fan Speed:");
        fanSpeedTextField = new JTextField();
        fanSpeedTextField.setMaximumSize(new Dimension(200, 30));

        fanSpeedBox.add(fanSpeedLabel);
        fanSpeedBox.add(Box.createHorizontalStrut(10));
        fanSpeedBox.add(fanSpeedTextField);

        panel.add(fanSpeedBox);
        panel.add(Box.createVerticalStrut(10));

        // Add action listeners
        actionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contribution.onActionSelected((String) actionComboBox.getSelectedItem());
                updateVisibility();
            }
        });

        positionTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                contribution.onPositionChanged(positionTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                contribution.onPositionChanged(positionTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                contribution.onPositionChanged(positionTextField.getText());
            }
        });

        colorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contribution.onColorSelected((String) colorComboBox.getSelectedItem());
            }
        });

        fanSpeedTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                contribution.onFanSpeedChanged(fanSpeedTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                contribution.onFanSpeedChanged(fanSpeedTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                contribution.onFanSpeedChanged(fanSpeedTextField.getText());
            }
        });

        // Initialize the visibility based on the selected action
        updateVisibility();
    }

    private void updateVisibility() {
        String action = (String) actionComboBox.getSelectedItem();
        if (action.equals("Move Gripper and Wait") || action.equals("Move Gripper without Wait")) {
            positionLabel.setVisible(true);
            positionTextField.setVisible(true);
        } else {
            positionLabel.setVisible(false);
            positionTextField.setVisible(false);
        }

        if (action.equals("Set LED Color")) {
            colorLabel.setVisible(true);
            colorComboBox.setVisible(true);
        } else {
            colorLabel.setVisible(false);
            colorComboBox.setVisible(false);
        }

        if (action.equals("Set Fan Speed")) {
            fanSpeedLabel.setVisible(true);
            fanSpeedTextField.setVisible(true);
        } else {
            fanSpeedLabel.setVisible(false);
            fanSpeedTextField.setVisible(false);
        }
    }

    public void setAction(String action) {
        actionComboBox.setSelectedItem(action);
    }

    public String getAction() {
        return (String) actionComboBox.getSelectedItem();
    }

    public void setPosition(String position) {
        positionTextField.setText(position);
    }

    public String getPosition() {
        return positionTextField.getText();
    }

    public void setColor(String color) {
        colorComboBox.setSelectedItem(color);
    }

    public String getColor() {
        return (String) colorComboBox.getSelectedItem();
    }

    public void setFanSpeed(String speed) {
        fanSpeedTextField.setText(speed);
    }

    public String getFanSpeed() {
        return fanSpeedTextField.getText();
    }
}
