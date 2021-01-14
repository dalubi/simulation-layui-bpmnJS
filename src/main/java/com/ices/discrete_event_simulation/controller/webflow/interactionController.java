package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ices.discrete_event_simulation.entity.Interaction;
import com.ices.discrete_event_simulation.entity.Parameter;
import com.ices.discrete_event_simulation.mapper.InteractionMapper;
import com.ices.discrete_event_simulation.mapper.ParameterMapper;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.AjaxTableResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class interactionController {

    @Autowired
    ParameterMapper parameterMapper;

    //bpmn文件建立别名
    @RequestMapping(value = "/getParameters" ,method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getParametersInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        String federation = globalController.getFederation();

        //有个全局变量，记录究竟是哪个federation被选中了
        //根据federation选中参数
        List<Parameter> parameterList = parameterMapper.selectList(null);

        for(int i=0;i<parameterList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("parameterName",parameterList.get(i).getParameterName());
            element.put("parameterType",parameterList.get(i).getParameterType());
            element.put("parameterId",parameterList.get(i).getParameterId());
            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    @RequestMapping(value = "/addParameter" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse AddParameterByPiece(@RequestBody JSONObject json){
        String parameterName = json.get("parameterName").toString();
        String parameterType = json.get("parameterType").toString();

        Parameter param = new Parameter();
        param.setParameterName(parameterName);
        param.setParameterType(parameterType);
        parameterMapper.insert(param);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"成功添加一条参数信息");
    }

    @RequestMapping(value = "/deleteParameter" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteParameterByName(@RequestBody JSONObject json){
        String parameterName = json.get("parameterName").toString();

        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("federation",globalController.getFederation());
        columnMap.put("parameterName",parameterName);
        parameterMapper.deleteByMap(columnMap);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"删除参数:"+parameterName);
    }

    @Autowired
    InteractionMapper interactionMapper;

    //获取本federation下面所有交互类的信息
    @RequestMapping(value = "/getAllInteractions" ,method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllInteractionsINFO(){

        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        List<Interaction> interactionList = interactionMapper.selectList(null);

        for(int i=0;i<interactionList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("interactionName",interactionList.get(i).getInteractionName());
            element.put("containParameterIds",interactionList.get(i).getContainParameterIds());
            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    @RequestMapping(value = "/modifyInteraction" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse modifyInteraction(@RequestBody JSONObject json){
        String interactionName = json.get("interactionName").toString();
        String containsParameterIds = json.get("containParameterIds").toString();

        //mbp 修改值
        Interaction interaction = new Interaction();
        interaction.setContainParameterIds(containsParameterIds);

        UpdateWrapper<Interaction> wrapper = new UpdateWrapper<>();
        wrapper.eq("interactionName",interactionName);

        int update = interactionMapper.update(interaction, wrapper);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"成功添加一条参数信息");
    }
}
