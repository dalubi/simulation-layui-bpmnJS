package com.ices.discrete_event_simulation.cyf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class pathVariable {

    @Value("${file.showbpmnpath}")
    private String showbpmnpath;

    @Value("${file.processespath}")
    private String processespath;

    @Value("${file.processesfilefolder}")
    private String processesfilefolder;

    public String getShowbpmnpath() {
        return showbpmnpath;
    }

    public void setShowbpmnpath(String showbpmnpath) {
        this.showbpmnpath = showbpmnpath;
    }

    public String getProcessespath() {
        return processespath;
    }

    public void setProcessespath(String processespath) {
        this.processespath = processespath;
    }

    public String getProcessesfilefolder() {
        return processesfilefolder;
    }

    public void setProcessesfilefolder(String processesfilefolder) {
        this.processesfilefolder = processesfilefolder;
    }
}
