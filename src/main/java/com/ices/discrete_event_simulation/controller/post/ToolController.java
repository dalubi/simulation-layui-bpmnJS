package com.ices.discrete_event_simulation.controller.post;

import com.ices.discrete_event_simulation.controller.webflow.ActivitiEditLifeCycle;
import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.mapper.*;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ToolController {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    InstructionListsizeMapper instructionListsizeMapper;

    @Autowired
    InstructionListgetMapper instructionListgetMapper;

    @Autowired
    InstructionListclearMapper instructionListclearMapper;

    @Autowired
    InstructionListaddMapper instructionListaddMapper;

    @Autowired
    InstructionMapgetMapper instructionMapgetMapper;

    @Autowired
    InstructionMapputMapper instructionMapputMapper;

    @Autowired
    InstructionContainskeyMapper instructionContainskeyMapper;

    @Autowired
    InstructionContainsvalueMapper instructionContainsvalueMapper;

    @Autowired
    InstructionCreatestringbuilderMapper instructionCreatestringbuilderMapper;

    @Autowired
    InstructionAppendMapper instructionAppendMapper;

    @Autowired
    InstructionTostringMapper instructionTostringMapper;

    @Autowired
    InstructionStringtointegerMapper instructionStringtointegerMapper;

    @Autowired
    InstructionTypeconversionMapper instructionTypeconversionMapper;

    @Autowired
    InstructionListgetrandomMapper instructionListgetRandomMapper;

    //listSize  完成
    @RequestMapping(value = "/inst/listSize",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_listName(
            @RequestParam("listName")String listName,
            @RequestParam("outName")String outName
    ){
        //插入指令
        InstructionListsize instructionListsize = new InstructionListsize();
        instructionListsize.setListName(listName);
        instructionListsize.setOutName(outName);
        instructionListsizeMapper.insert(instructionListsize);

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
        int instructionId = instructionListsizeMapper.maxId();
        sequence = sequence+"listsize,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //listGet  完成
    @RequestMapping(value = "/inst/listGet",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_listGet(
            @RequestParam("listName")String listName,
            @RequestParam("objectName")String objectName,
            @RequestParam("fillInformation")String fillInformation,
            @RequestParam("accordingParameter")String accordingParameter,
            @RequestParam("outName")String outName
    ){
        //插入指令
        InstructionListget instructionListget = new InstructionListget();
        instructionListget.setListName(listName);
        instructionListget.setOutName(outName);
        instructionListgetMapper.insert(instructionListget);

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
        int instructionId = instructionListgetMapper.maxId();
        sequence = sequence+"listget,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //listGetRandom 完成
    @RequestMapping(value = "/inst/listGetRandom",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_listGetRandom(
            @RequestParam("listName")String listName,
            @RequestParam("objectName")String objectName,
            @RequestParam("fillInformation")String fillInformation,
            @RequestParam("outName")String outName
    ){
        //插入指令
        InstructionListgetrandom instructionListgetrandom = new InstructionListgetrandom();
        instructionListgetrandom.setFillInformation(fillInformation);
        instructionListgetrandom.setListName(listName);
        instructionListgetrandom.setObjectName(objectName);
        instructionListgetrandom.setOutName(outName);

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

        int instructionId = instructionListgetRandomMapper.maxId();
        sequence = sequence+"listgetrandom,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //listclear
    @RequestMapping(value = "/inst/listclear",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_listclear(
            @RequestParam("listName")String listName
    ){
        //插入指令
        InstructionListclear instructionListclear = new InstructionListclear();
        instructionListclear.setListName(listName);
        instructionListclearMapper.insert(instructionListclear);


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
        int instructionId = instructionListclearMapper.maxId();
        sequence = sequence+"listclear,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //listAdd  完成
    @RequestMapping(value = "/inst/listAdd",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_listAdd(
            @RequestParam("listName")String listName,
            @RequestParam("objectName")String objectName
    ){
        //插入指令
        InstructionListadd instructionListadd  = new InstructionListadd ();
        instructionListadd.setListName(listName);
        instructionListadd.setObjectName(objectName);
        instructionListaddMapper.insert(instructionListadd);

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
        int instructionId = instructionListaddMapper.maxId();
        sequence = sequence+"listadd,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //MapGet  完成
    @RequestMapping(value = "/inst/MapGet",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_MapGet(
            @RequestParam("key")String key,
            @RequestParam("objectName")String objectName,
            @RequestParam("parameterType")String parameterType,
            @RequestParam("outName")String outName
    ){
        //插入指令
        InstructionMapget instructionMapget  = new InstructionMapget ();
        instructionMapget.setKey(key);
        instructionMapget.setObjectName(objectName);
        instructionMapget.setParameterType(parameterType);
        instructionMapget.setOutName(outName);

        instructionMapgetMapper.insert(instructionMapget);

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
        int instructionId = instructionMapgetMapper.maxId();
        sequence = sequence+"mapget,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //MapPut  完成
    @RequestMapping(value = "/inst/MapPut",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_MapPut(
            @RequestParam("key")String key,
            @RequestParam("value")String value,
            @RequestParam("objectName")String objectName
    ){
        InstructionMapput instructionMapput  = new InstructionMapput ();
        instructionMapput.setKey(key);
        instructionMapput.setObjectName(objectName);
        instructionMapput.setValue(value);
        instructionMapputMapper.insert(instructionMapput);

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
        int instructionId = instructionMapputMapper.maxId();
        sequence = sequence+"mapput,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //containsKey  完成
    @RequestMapping(value = "/inst/containsKey",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_containsKey(
            @RequestParam("key")String key,
            @RequestParam("objectName")String objectName,
            @RequestParam("outName")String outName
    ){
        InstructionContainskey instructionContainskey   = new InstructionContainskey();
        instructionContainskey.setKey(key);
        instructionContainskey.setObjectName(objectName);
        instructionContainskey.setOutName(outName);
        instructionContainskeyMapper.insert(instructionContainskey);

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
        int instructionId = instructionContainskeyMapper.maxId();
        sequence = sequence+"containskey,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //containsValue  完成
    @RequestMapping(value = "/inst/containsValue",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_containsValue(
            @RequestParam("value")String value,
            @RequestParam("objectName")String objectName,
            @RequestParam("outName")String outName
    ){
        InstructionContainsvalue instructionContainsvalue  = new InstructionContainsvalue();
        instructionContainsvalue.setObjectName(objectName);
        instructionContainsvalue.setOutName(outName);
        instructionContainsvalue.setValue(value);
        instructionContainsvalueMapper.insert(instructionContainsvalue);

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
        int instructionId = instructionContainsvalueMapper.maxId();
        sequence = sequence+"containsvalue,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //createStringBuilder  完成
    @RequestMapping(value = "/inst/createStringBuilder",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_createStringBuilder(
            @RequestParam("outName")String outName
    ){
        InstructionCreatestringbuilder instructionCreatestringbuilder  = new InstructionCreatestringbuilder();
        instructionCreatestringbuilder.setOutName(outName);
        instructionCreatestringbuilderMapper.insert(instructionCreatestringbuilder);

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
        int instructionId = instructionCreatestringbuilderMapper.maxId();
        sequence = sequence+"createstringbuilder,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //append  完成
    @RequestMapping(value = "/inst/append",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_append(
            @RequestParam("StringBuilderName")String StringBuilderName,
            @RequestParam("formatInformation")String formatInformation,
            @RequestParam("fillInformation")String fillInformation
    ){
        InstructionAppend instructionAppend   = new InstructionAppend ();
        instructionAppend.setFillInformation(fillInformation);
        instructionAppend.setFormatInformation(formatInformation);
        instructionAppend.setStringBuilderName(StringBuilderName);
        instructionAppendMapper.insert(instructionAppend);

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

        int instructionId = instructionAppendMapper.maxId();
        sequence = sequence+"append,";
        interactionIds=interactionIds+instructionId+",";

        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //toString  完成
    @RequestMapping(value = "/inst/tostring",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_toString(
            @RequestParam("objectName")String objectName,
            @RequestParam("outName")String outName
    ){
        //插入指令
        InstructionTostring instructionTostring  = new InstructionTostring ();
        instructionTostring.setObjectName(objectName);
        instructionTostring.setOutName(outName);
        instructionTostringMapper.insert(instructionTostring);

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

        int instructionId = instructionTostringMapper.maxId();
        sequence = sequence+"tostring,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //IntegerToString
    @RequestMapping(value = "/inst/IntegerToString",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_IntegerToString(
            @RequestParam("listName")String listName,
            @RequestParam("objectType")String objectType,
            @RequestParam("accordingToParameterName")String accordingToParameterName,
            @RequestParam("removeType")String removeType,
            @RequestParam("removeName")String removeName
    ){

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //StringToInteger
    @RequestMapping(value = "/inst/StringToInteger",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_StringToInteger(
            @RequestParam("value")String value,
            @RequestParam("outName")String outName
    ){
        InstructionStringtointeger instructionStringtointeger  = new InstructionStringtointeger ();
        instructionStringtointeger.setOutName(outName);
        instructionStringtointeger.setValue(value);
        instructionStringtointegerMapper.insert(instructionStringtointeger);

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
        int instructionId = instructionStringtointegerMapper.maxId();
        sequence = sequence+"stringtointeger,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //typeConversion
    @RequestMapping(value = "/inst/typeConversion",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_typeConversion(
            @RequestParam("conversionType")String conversionType,
            @RequestParam("information")String information,
            @RequestParam("outName")String outName
    ){
        InstructionTypeconversion instructionTypeconversion  = new InstructionTypeconversion ();
        instructionTypeconversion.setOutName(outName);
        instructionTypeconversion.setInformation(information);
        instructionTypeconversion.setConversionType(conversionType);

        instructionTypeconversionMapper.insert(instructionTypeconversion);

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
        int instructionId = instructionTypeconversionMapper.maxId();
        sequence = sequence+"typeconversion,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }
}
