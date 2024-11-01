// Activator.java
//! https://github.com/EbbeFuglsang/Modbus_URCap

package com.ur.thph.modbus_urcap.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Activator says Modbus Service!");

        ModbusDaemonService modbusDaemonService = new ModbusDaemonService();
        DaemonInstallationNodeService installationService = new DaemonInstallationNodeService(modbusDaemonService);
        GripperProgramNodeService gripperProgramNodeService = new GripperProgramNodeService();

        bundleContext.registerService(DaemonService.class, modbusDaemonService, null);
        bundleContext.registerService(SwingInstallationNodeService.class, installationService, null);
        bundleContext.registerService(SwingProgramNodeService.class, gripperProgramNodeService, null); // Updated line
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Activator says Modbus Service!");
    }
}