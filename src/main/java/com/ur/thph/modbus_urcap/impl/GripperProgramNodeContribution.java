// GripperProgramNodeContribution.java

package com.ur.thph.modbus_urcap.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.contribution.program.CreationContext;


public class GripperProgramNodeContribution implements ProgramNodeContribution {
    
    private static final String ACTION_KEY = "action";
    private static final String POSITION_KEY = "position";
    private static final String COLOR_KEY = "color";
    private static final String FANSPEED_KEY = "fanspeed";

    private final ProgramAPIProvider apiProvider;
    private final GripperProgramNodeView view;
    private final DataModel model;

    public GripperProgramNodeContribution(
        ProgramAPIProvider apiProvider,
        GripperProgramNodeView view,
        DataModel model,
        CreationContext context
    ) {
        this.apiProvider = apiProvider;
        this.view = view;
        this.model = model;
        // Use the context if needed
    }

    @Override
    public void openView() {
        String action = model.get(ACTION_KEY, "Move Gripper and Wait");
        String position = model.get(POSITION_KEY, "0");
        String color = model.get(COLOR_KEY, "red");
        String fanSpeed = model.get(FANSPEED_KEY, "0");

        view.setAction(action);
        view.setPosition(position);
        view.setColor(color);
        view.setFanSpeed(fanSpeed);
    }

    @Override
    public void closeView() {
        // Do nothing
    }

    @Override
    public String getTitle() {
        String action = model.get(ACTION_KEY, "Action");
        String title = action;

        if (action.equals("Move Gripper and Wait") || action.equals("Move Gripper without Wait")) {
            String position = model.get(POSITION_KEY, "0");
            title += " (" + position + ")";
        } else if (action.equals("Set LED Color")) {
            String color = model.get(COLOR_KEY, "color");
            title += " (" + color + ")";
        } else if (action.equals("Set Fan Speed")) {
            String fanSpeed = model.get(FANSPEED_KEY, "0");
            title += " (" + fanSpeed + ")";
        }
        return title;
    }

    @Override
    public boolean isDefined() {
        String action = model.get(ACTION_KEY, "");
        if (action.isEmpty()) {
            return false;
        }
        if (action.equals("Move Gripper and Wait") || action.equals("Move Gripper without Wait")) {
            String position = model.get(POSITION_KEY, "");
            try {
                Integer.parseInt(position);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (action.equals("Set Fan Speed")) {
            String fanSpeed = model.get(FANSPEED_KEY, "");
            try {
                Integer.parseInt(fanSpeed);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void generateScript(ScriptWriter writer) {
        String action = model.get(ACTION_KEY, "");

        if (action.equals("Move Gripper and Wait")) {
            String position = model.get(POSITION_KEY, "0");
            writer.appendLine("moveGripperWait(" + position + ")");
        } else if (action.equals("Move Gripper without Wait")) {
            String position = model.get(POSITION_KEY, "0");
            writer.appendLine("moveGripperNoWait(" + position + ")");
        } else if (action.equals("Set LED Color")) {
            String color = "\"" + model.get(COLOR_KEY, "red") + "\"";
            writer.appendLine("setLEDColor(" + color + ")");
        } else if (action.equals("Home Gripper")) {
            writer.appendLine("homeGripper()");
        } else if (action.equals("Set Fan Speed")) {
            String fanSpeed = model.get(FANSPEED_KEY, "0");
            writer.appendLine("setFanSpeed(" + fanSpeed + ")");
        }
    }


    public void onActionSelected(String action) {
        model.set(ACTION_KEY, action);
    }

    public void onPositionChanged(String position) {
        model.set(POSITION_KEY, position);
    }

    public void onColorSelected(String color) {
        model.set(COLOR_KEY, color);
    }

    public void onFanSpeedChanged(String speed) {
        model.set(FANSPEED_KEY, speed);
    }
}
