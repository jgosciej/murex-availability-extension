package com.appdynamics.monitors.murex;

import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MurexMonitorTest {

    @Test
    public void testMurexEvents(){
        Map<String, String> taskArgs = new HashMap<String, String>();
        taskArgs.put("config-file", "src/test/resources/config.yml");
        try {
            TaskOutput result = new MurexMonitor().execute(taskArgs, null);
            Thread.sleep(20_000);
        } catch (TaskExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}
