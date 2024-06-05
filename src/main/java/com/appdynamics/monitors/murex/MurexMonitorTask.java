package com.appdynamics.monitors.murex;

import com.appdynamics.extensions.AMonitorTaskRunnable;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.appdynamics.monitors.murex.util.Constants.SCHEMA_NAME;

public class MurexMonitorTask implements AMonitorTaskRunnable {

    private static final Logger logger = ExtensionsLoggerFactory.getLogger(MurexMonitor.class);
    private MonitorContextConfiguration monitorContextConfiguration;
    private String scriptPath;
    private String command;
    private Map<String, ?> configYml = Maps.newHashMap();

    public MurexMonitorTask(MonitorContextConfiguration monitorContextConfiguration, String scriptPath, String command) {
        this.monitorContextConfiguration = monitorContextConfiguration;
        this.scriptPath = scriptPath;
        this.command = command;
    }

    @Override
    public void onTaskComplete() {
        logger.info("Completed the Murex Monitoring task");

    }

    @Override
    public void run() {
        try {
            createMurexSchema();
            populateAndPublishEvents();
        } catch (Exception ex) {
            logger.error("Murex monitoring task failed with exception: ", ex);
        }

    }

    private void createMurexSchema() {

        configYml = monitorContextConfiguration.getConfigYml();
        String schemaBody = (String) configYml.get("eventsSchemaDefinition");
        try {
            if (monitorContextConfiguration.getContext().getEventsServiceDataManager().retrieveSchema(SCHEMA_NAME).contains("nodeName")) {
                logger.info("Schema: {} already exists", SCHEMA_NAME);
            } else {
                logger.info("Creating Schema {}", SCHEMA_NAME);
                monitorContextConfiguration.getContext().getEventsServiceDataManager().createSchema(SCHEMA_NAME, schemaBody);
            }
        } catch (Exception ex) {
            logger.error("Error encountered while creating schema for murex {}");
        }
    }

    private void populateAndPublishEvents() throws Exception {
        List<MurexEvent> events = runScriptAndParseOutput();
        if (events.size() != 0) {
            List<String> preparedEvents = prepareEventsForPublishing(events);
            monitorContextConfiguration.getContext().getEventsServiceDataManager().publishEvents(SCHEMA_NAME, preparedEvents);

        } else {
            logger.info("No events to publish");
        }

    }

    private List<MurexEvent> runScriptAndParseOutput() {
        List<MurexEvent> events = new ArrayList<>();
        String nodeName = getHostName();
        List<String> commandParts = Arrays.asList(command.split("\\s+"));
        ProcessBuilder processBuilder = new ProcessBuilder(commandParts);

        processBuilder.directory(new java.io.File(scriptPath));
        processBuilder.redirectErrorStream(true);
        boolean foundRunningServices = false;

        try {
            Process process = processBuilder.start();
            logger.info("The script with command " + commandParts + " in directory " + scriptPath + " was successfully launched.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean startProcessing = false;
            String previousLine = null;


            while ((line = reader.readLine()) != null) {
                if (!startProcessing) {
                    // Skip lines until the line containing "Found running service(s) :" is found
                    if (line.contains("Found running service(s) :")) {
                        startProcessing = true; // Start processing from the next line

                    }
                    continue;
                }

                // Process lines after "Found running service(s) :"
                if (previousLine != null) {
                    // Combine the previous line with the current line
                    String combinedLine = previousLine + " " + line;
                    MurexEvent event = parseMurexEvent(combinedLine, nodeName);
                    if (event != null) {
                        events.add(event);
                        foundRunningServices = true;
                    }
                    previousLine = null;
                } else {
                    previousLine = line;
                }
            }


            int exitCode = process.waitFor();

            if (foundRunningServices) {
                logger.info("Found running services.");
            }

            if (exitCode == 0) {
                logger.info("The script completed successfully.");
            } else {
                logger.error("The script exited with error code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("An error occurred while running the script.", e);
        }


        return events;
    }

    private static MurexEvent parseMurexEvent(String combinedLine, String nodeName) {
        Pattern pattern = Pattern.compile("^(.*?) infos :\\s+UID: .*?PID: (\\d+) CPUTIME: (\\S+) STIME: (\\S+)$");
        Matcher matcher = pattern.matcher(combinedLine);

        if (matcher.find()) {
            String serviceName = matcher.group(1).trim();
            String processId = matcher.group(2).trim();
            String cpuTime = matcher.group(3).trim();
            String startTime = matcher.group(4).trim();
            return new MurexEvent(nodeName, serviceName, processId, cpuTime, startTime);
        }
        return null;
    }


    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    private static ArrayList<String> prepareEventsForPublishing(List<MurexEvent> eventsToBePublished) {
        ArrayList<String> events = new ArrayList<String>();
        ObjectMapper mapper = new ObjectMapper();
        for (MurexEvent murexEvent : eventsToBePublished) {
            try {
                events.add(mapper.writeValueAsString(murexEvent));

            } catch (Exception ex) {
                logger.error("Error encountered while publishing MurexEvent {} - ", murexEvent, ex);
            }
        }
        return events;

    }
}

