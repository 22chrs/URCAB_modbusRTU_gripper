// GripperProgramNodeService.java

package com.ur.thph.modbus_urcap.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.contribution.program.CreationContext;

public class GripperProgramNodeService implements SwingProgramNodeService<GripperProgramNodeContribution, GripperProgramNodeView> {
    @Override
    public String getId() {
        return "GripperProgramNode";
    }

    @Override
    public void configureContribution(ContributionConfiguration configuration) {
        // No special configuration needed
    }

    @Override
    public String getTitle(Locale locale) {
        return "Gripper Control";
    }

    @Override
    public GripperProgramNodeView createView(ViewAPIProvider apiProvider) {
        return new GripperProgramNodeView(apiProvider);
    }

    @Override
    public GripperProgramNodeContribution createNode(
        ProgramAPIProvider apiProvider,
        GripperProgramNodeView view,
        DataModel model,
        CreationContext context
    ) {
        return new GripperProgramNodeContribution(apiProvider, view, model, context);
    }
}