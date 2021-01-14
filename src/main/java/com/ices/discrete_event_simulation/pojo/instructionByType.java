package com.ices.discrete_event_simulation.pojo;

public class instructionByType {
    private String instructionEng;
    private String instructionChn;
    private String uploadUrl;

    public instructionByType(String instructionEng, String instructionChn, String uploadUrl) {
        this.instructionEng = instructionEng;
        this.instructionChn = instructionChn;
        this.uploadUrl = uploadUrl;
    }

    public String getInstructionEng() {
        return instructionEng;
    }

    public void setInstructionEng(String instructionEng) {
        this.instructionEng = instructionEng;
    }

    public String getInstructionChn() {
        return instructionChn;
    }

    public void setInstructionChn(String instructionChn) {
        this.instructionChn = instructionChn;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
}
