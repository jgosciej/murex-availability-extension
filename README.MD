# AppDynamics Extension for Murex Availability

This extension works only with the standalone machine agent.

## Use Case
Get visibility on murex services running on a host and report their availability to AppDynamics via Custom Analytics Events.
This extension leverage the command line script provided by the customer.

## Prerequisites

 * This extension requires the Java Machine Agent
 * This extension requires the connectivity to the AppDynamics Events Service / Analytics Endpoint
 * You will need one or more Transaction Analytics/APM Peak licenses to consume the data

## Installation

Either [Download the Extension from the latest Github release (target/MurexMonitor-<VERSION>.zip) or Build from Source.

1. Deploy the `MurexMonitor-<VERSION>.zip` file into the `<machine agent home>/monitors` directory.

  `> unzip MurexMonitor-<VERSION>.zip -d <machine agent home>/monitors/`

2. Set up below parameters in `config.yml`:
  ```
  sendDataToEventsService: true

  scriptPath: "" // ex: "/u01/app/murex_app"
    
  # eventsServiceParameters:
    host: "<analytics endpoint> // ex: fra-ana-api.saas.appdynamics.com
    port: 443
    globalAccountName: "<global account name>"
    eventsApiKey: "<api key>"
    useSSL: true

  
  ```

3. Restart the Machine Agent.

## Build from Source

1. Clone this repository
2. Run `mvn -DskipTests clean install`
3. The `MurexMonitor-<VERSION>.zip` file can be found in the `target` directory

## Directory Structure

<table><tbody>
<tr>
<th align = 'left'> Directory/File </th>
<th align = 'left'> Description </th>
</tr>
<tr>
<td class='confluenceTd'> src/main/resources </td>
<td class='confluenceTd'> Contains monitor.xml and config.yml</td>
</tr>
<tr>
<td class='confluenceTd'> src/main/java </td>
<td class='confluenceTd'> Contains source code for the Murex monitoring extension </td>
</tr>
<tr>
<td class='confluenceTd'> src/test/java </td>
<td class='confluenceTd'> Contains test code for the Murex Events monitoring extension </td>
</tr>
<tr>
<td class='confluenceTd'> target </td>
<td class='confluenceTd'> Only obtained when using maven. Run 'maven clean install' to get the distributable .zip file. </td>
</tr>
<tr>
<td class='confluenceTd'> pom.xml </td>
<td class='confluenceTd'> maven build script to package the project (required only if changing Java code) </td>
</tr>
</tbody>
</table>

## Troubleshooting

1. Verify Machine Agent Data: Please start the Machine Agent without the extension and make sure that it reports data. Verify that the machine agent status is UP and it is reporting Hardware Metrics.
2. Config.yml: Validate the file [here](http://www.yamllint.com/)
3. Check Logs: There could be some obvious errors in the machine agent logs. Please take a look.
4. `The config cannot be null` error.
   This usually happenes when on a windows machine in monitor.xml you give config.yaml file path with linux file path separator */*. Use Windows file path separator *\* e.g. *monitors\Monitor\config.yaml*. For Windows, please specify
   the complete path
5. Collect Debug Logs: Edit the file, *<MachineAgent>/conf/logging/log4j.xml* and update the level of the appender *com.appdynamics* and *com.singularity* to debug. Let it run for 5-10 minutes and review the logs.

