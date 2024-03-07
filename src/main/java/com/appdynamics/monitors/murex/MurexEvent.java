package com.appdynamics.monitors.murex;

public class MurexEvent {
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(String cpuTime) {
        this.cpuTime = cpuTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    private String nodeName;
    private String serviceName;
    private String processId;
    private String cpuTime;
    private String startTime;

    public MurexEvent(String nodeName, String serviceName, String processId, String cpuTime, String startTime) {
        this.nodeName = nodeName;
        this.serviceName = serviceName;
        this.processId = processId;
        this.cpuTime = cpuTime;
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "MurexEvent{" +
                "nodeName='" + nodeName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", processId='" + processId + '\'' +
                ", cpuTime='" + cpuTime + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

}

