// GripperProgramNodeView.java

package com.ur.thph.modbus_urcap.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;

public class GripperProgramNodeView implements SwingProgramNodeView<GripperProgramNodeContribution> {

    private final ViewAPIProvider apiProvider;
    private ContributionProvider<GripperProgramNodeContribution> contribution;

    private JComboBox<String> actionComboBox;
    private JTextField positionTextField;
    private JComboBox<String> colorComboBox;
    private JTextField fanSpeedTextField;

    private JLabel positionLabel;
    private JLabel colorLabel;
    private JLabel fanSpeedLabel;

    public GripperProgramNodeView(ViewAPIProvider apiProvider) {
        this.apiProvider = apiProvider;
    }

    @Override
    public void buildUI(JPanel panel, ContributionProvider<GripperProgramNodeContribution> provider) {
        this.contribution = provider;
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        KeyboardInputFactory keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();

        // Row 0: Action selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel actionLabel = new JLabel("Action:");
        panel.add(actionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        actionComboBox = new JComboBox<>(new String[] {
            "Move Gripper and Wait",
            "Move Gripper without Wait",
            "Set LED Color",
            "Home Gripper",
            "Set Fan Speed"
        });
        panel.add(actionComboBox, gbc);

        // Row 1: Position input
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        positionLabel = new JLabel("Gripper Position:");
        panel.add(positionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        positionTextField = new JTextField();
        panel.add(positionTextField, gbc);

        // Create keyboard input for position
        final KeyboardNumberInput<Double> positionKeyboardInput = keyboardInputFactory.createDoubleKeypadInput();
        positionKeyboardInput.setErrorValidator(new InputValidator<Double>() {
            @Override
            public boolean isValid(Double value) {
                return value >= 0;
            }

            @Override
            public String getMessage(Double invalidEntry) {
                return "Please enter a non-negative number.";
            }
        });
        positionTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                positionKeyboardInput.show(positionTextField, new KeyboardInputCallback<Double>() {
                    @Override
                    public void onOk(Double value) {
                        positionTextField.setText(String.valueOf(value.intValue()));
                        contribution.get().onPositionChanged(positionTextField.getText());
                    }

                    @Override
                    public void onCancel() {
                        // Do nothing on cancel
                    }
                });
            }
        });

        // Row 2: Color selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        colorLabel = new JLabel("LED Color:");
        panel.add(colorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        colorComboBox = new JComboBox<>(new String[] {
            "red", "green", "blue", "white", "purple", "pink", "orange", "yellow"
        });
        panel.add(colorComboBox, gbc);

        // Row 3: Fan Speed input
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        fanSpeedLabel = new JLabel("Fan Speed:");
        panel.add(fanSpeedLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        fanSpeedTextField = new JTextField();
        panel.add(fanSpeedTextField, gbc);

        // Create keyboard input for fan speed
        final KeyboardNumberInput<Double> fanSpeedKeyboardInput = keyboardInputFactory.createDoubleKeypadInput();
        fanSpeedKeyboardInput.setErrorValidator(new InputValidator<Double>() {
            @Override
            public boolean isValid(Double value) {
                return value >= 0;
            }

            @Override
            public String getMessage(Double invalidEntry) {
                return "Please enter a non-negative number.";
            }
        });
        fanSpeedTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                fanSpeedKeyboardInput.show(fanSpeedTextField, new KeyboardInputCallback<Double>() {
                    @Override
                    public void onOk(Double value) {
                        fanSpeedTextField.setText(String.valueOf(value.intValue()));
                        contribution.get().onFanSpeedChanged(fanSpeedTextField.getText());
                    }

                    @Override
                    public void onCancel() {
                        // Do nothing on cancel
                    }
                });
            }
        });

        // Add action listeners
        actionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contribution.get().onActionSelected((String) actionComboBox.getSelectedItem());
                updateVisibility();
            }
        });

        colorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contribution.get().onColorSelected((String) colorComboBox.getSelectedItem());
            }
        });

        // Initialize the visibility based on the selected action
        updateVisibility();
    }

    private void updateVisibility() {
        String action = (String) actionComboBox.getSelectedItem();
        boolean isMoveGripper = action.contains("Move Gripper");
        positionLabel.setVisible(isMoveGripper);
        positionTextField.setVisible(isMoveGripper);

        boolean isSetLEDColor = action.equals("Set LED Color");
        colorLabel.setVisible(isSetLEDColor);
        colorComboBox.setVisible(isSetLEDColor);

        boolean isSetFanSpeed = action.equals("Set Fan Speed");
        fanSpeedLabel.setVisible(isSetFanSpeed);
        fanSpeedTextField.setVisible(isSetFanSpeed);

        // Revalidate and repaint the panel to reflect changes
        positionLabel.getParent().revalidate();
        positionLabel.getParent().repaint();
    }

    public void setAction(String action) {
        actionComboBox.setSelectedItem(action);
        updateVisibility();
    }

    public void setPosition(String position) {
        positionTextField.setText(position);
    }

    public void setColor(String color) {
        colorComboBox.setSelectedItem(color);
    }

    public void setFanSpeed(String speed) {
        fanSpeedTextField.setText(speed);
    }
}
