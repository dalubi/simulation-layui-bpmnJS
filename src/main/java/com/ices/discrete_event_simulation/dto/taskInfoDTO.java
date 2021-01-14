package com.ices.discrete_event_simulation.dto;

import lombok.Data;

@Data
public class taskInfoDTO {
    private int idInTask;
    private int idInTable;
    private String instructionName;
    private String instructionDescribe;
}
