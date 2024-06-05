package com.appdynamics.monitors.murex;

import com.appdynamics.extensions.ABaseMonitor;
import com.appdynamics.extensions.TasksExecutionServiceProvider;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.util.*;

import static com.appdynamics.monitors.murex.util.Constants.DEFAULT_METRIC_PREFIX;
import static com.appdynamics.monitors.murex.util.Constants.MONITOR_NAME;


public class MurexMonitor extends ABaseMonitor {
    private static final Logger logger = ExtensionsLoggerFactory.getLogger(MurexMonitor.class);
    private MonitorContextConfiguration monitorContextConfiguration;
    public Map<String, ?> configYml = Maps.newHashMap();
    private long startTime;


    //used by Initialize as part of execute
    @Override
    protected void initializeMoreStuff(Map<String, String> args) {
        monitorContextConfiguration = getContextConfiguration();
        configYml = monitorContextConfiguration.getConfigYml();
    }

    @Override
    protected String getDefaultMetricPrefix() {
        return DEFAULT_METRIC_PREFIX;
    }

    @Override
    public String getMonitorName() {
        return MONITOR_NAME;
    }

    @Override
    protected void doRun(TasksExecutionServiceProvider taskExecutor) {

        String scriptPath = (String) configYml.get("scriptPath");
        if (scriptPath == null)
        {
            logger.error("Script Path is invalid");
        }

        String command = (String) configYml.get("command");

        if (command == null)
        {
            logger.error("Command to execute the script is invalid");
        }

        MurexMonitorTask task = new MurexMonitorTask(monitorContextConfiguration, scriptPath, command);
        taskExecutor.submit("Murex Task", task);

    }

    @Override
    protected List<Map<String, ?>> getServers() {
        return new ArrayList<>(Collections.singletonList(new HashMap<>()));
    }
}
