package com.ices.discrete_event_simulation.controller.post;

import com.ices.discrete_event_simulation.controller.webflow.ActivitiEditLifeCycle;
import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.mapper.*;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LogicController {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    InstructionIfMapper instructionIfMapper;

    @Autowired
    InstructionElseifMapper instructionElseifMapper;

    @Autowired
    InstructionElseMapper instructionElseMapper;

    @Autowired
    InstructionWhileMapper instructionWhileMapper;

    @Autowired
    InstructionFornumberMapper instructionFornumberMapper;

    @Autowired
    InstructionForeachMapper instructionForeachMapper;

    @Autowired
    InstructionSelectMapper instructionSelectMapper;

    @Autowired
    InstructionBreakMapper instructionBreakMapper;

    @Autowired
    InstructionContinueMapper instructionContinueMapper;

    @Autowired
    InstructionLoopendMapper instructionLoopendMapper;

    @Autowired
    InstructionExecutetaskMapper instructionExecutetaskMapper;


    //if
    @RequestMapping(value = "/inst/if",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_if(@RequestParam("expression")String expression){
        //插入指令 修改1
        InstructionIf If= new InstructionIf();
        If.setExpression(expression);
        instructionIfMapper.insert(If);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionIfMapper.maxId();
        sequence = sequence+"if,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //else if
    @RequestMapping(value = "/inst/elseif",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_elseif(@RequestParam("expression")String expression){
        //插入指令 修改1
        InstructionElseif Elseif= new InstructionElseif();
        Elseif.setExpression(expression);
        instructionElseifMapper.insert(Elseif);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionElseifMapper.maxId();
        sequence = sequence+"elseif,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //else
    @RequestMapping(value = "/inst/else",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_else(){
        //插入指令 修改1
        InstructionElse Else= new InstructionElse();
        instructionElseMapper.insert(Else);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionElseMapper.maxId();
        sequence = sequence+"else,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //while
    @RequestMapping(value = "/inst/while",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_while(@RequestParam("expression")String expression){
        //插入指令 修改1
        InstructionWhile While= new InstructionWhile();
        While.setExpression(expression);
        instructionWhileMapper.insert(While);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionWhileMapper.maxId();
        sequence = sequence+"while,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //fornumber
    @RequestMapping(value = "/inst/fornumber",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_fornumber(@RequestParam("number")String number,
                                          @RequestParam("loopVariableName")String loopVariableName){
        //插入指令 修改1
        InstructionFornumber Fornumber= new InstructionFornumber();
        Fornumber.setLoopVariableName(loopVariableName);
        Fornumber.setNumber(number);
        instructionFornumberMapper.insert(Fornumber);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionFornumberMapper.maxId();
        sequence = sequence+"fornumber,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //foreach
    @RequestMapping(value = "/inst/foreach",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_foreach(@RequestParam("listName")String listName,
                                          @RequestParam("objectName")String objectName,
                                        @RequestParam("outName")String outName){
        //插入指令 修改1
        InstructionForeach Foreach= new InstructionForeach();
        Foreach.setListName(listName);
        Foreach.setObjectName(objectName);
        Foreach.setOutName(outName);
        instructionForeachMapper.insert(Foreach);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionForeachMapper.maxId();
        sequence = sequence+"foreach,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }


    //select
    @RequestMapping(value = "/inst/select",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_select(@RequestParam("InformationName")String InformationName,
                                        @RequestParam("branch1Id")String branch1Id,
                                        @RequestParam("branch2Id")String branch2Id){
        //插入指令 修改1
        InstructionSelect Select= new InstructionSelect();
        Select.setInformationName(InformationName);
        Select.setBranch1Id(branch1Id);
        Select.setBranch2Id(branch2Id);
        instructionSelectMapper.insert(Select);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionSelectMapper.maxId();
        sequence = sequence+"select,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //break
    @RequestMapping(value = "/inst/break",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_break(){
        //插入指令 修改1
        InstructionBreak Break= new InstructionBreak();
        instructionBreakMapper.insert(Break);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionBreakMapper.maxId();
        sequence = sequence+"break,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //continue
    @RequestMapping(value = "/inst/continue",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_continue(){
        //插入指令 修改1
        InstructionContinue Continue= new InstructionContinue();
        instructionContinueMapper.insert(Continue);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionContinueMapper.maxId();
        sequence = sequence+"continue,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //loopend
    @RequestMapping(value = "/inst/loopend",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_loopend(@RequestParam("content")String content){
        //插入指令 修改1
        InstructionLoopend Loopend= new InstructionLoopend();
        Loopend.setContent(content);
        instructionLoopendMapper.insert(Loopend);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionLoopendMapper.maxId();
        sequence = sequence+"loopend,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //executeTask
    @RequestMapping(value = "/inst/executeTask",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_executeTask(@RequestParam("taskId")String taskId){
        //插入指令 修改1
        InstructionExecutetask Executetask= new InstructionExecutetask();
        Executetask.setTaskId(taskId);
        instructionExecutetaskMapper.insert(Executetask);

        //获得taskID
        String taskId2 = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId2);
        String interactionIds = taskMapper.findInstructionIdsById(taskId2);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionExecutetaskMapper.maxId();
        sequence = sequence+"executetask,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId2);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }
}
