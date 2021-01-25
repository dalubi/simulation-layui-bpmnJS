package com.ices.discrete_event_simulation.controller.pageInfoController;

import com.ices.discrete_event_simulation.entity.Federate;
import com.ices.discrete_event_simulation.entity.FederateList;
import com.ices.discrete_event_simulation.entity.FederateObject;
import com.ices.discrete_event_simulation.mapper.FederateListMapper;
import com.ices.discrete_event_simulation.mapper.FederateMapMapper;
import com.ices.discrete_event_simulation.mapper.FederateMapper;
import com.ices.discrete_event_simulation.mapper.FederateObjectMapper;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class selectInfoController {

    @Autowired
    FederateMapper federateMapper;


    //获取federate的名字
    @RequestMapping(value = "/federateObjectPage/getAllFederateNames",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getAllFederateNames() {
        List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

        List<Federate> federates = federateMapper.selectList(null);
        for(Federate f:federates){
            HashMap<String, Object> element = new HashMap<>();
            element.put("federate",f.getFederateName());
            listMap.add(element);
        }

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap);
    }


    @Autowired
    FederateObjectMapper federateObjectMapper;

    //获取federateObject的名字
    @RequestMapping(value = "/selectInfo/getAllFederateObjects",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getAllFederateObjects() {
        List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

        List<FederateObject> federateObjects = federateObjectMapper.selectList(null);
        for(FederateObject fo:federateObjects){
            HashMap<String, Object> element = new HashMap<>();
            element.put("objectName",fo.getObjectName());
            listMap.add(element);
        }

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap);
    }

    @Autowired
    FederateListMapper federateListMapper;

    //获取federateList的名字
    @RequestMapping(value = "/selectInfo/getAllFederateLists",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getAllFederateLists() {
        List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

        List<FederateList> federateLists = federateListMapper.selectList(null);
        for(FederateList fl:federateLists){
            HashMap<String, Object> element = new HashMap<>();
            element.put("listName",fl.getListName());
            listMap.add(element);
        }

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap);
    }

    //获取federateObject的名字
    @RequestMapping(value = "/selectInfo/getAllFederateObjectsWithId",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getAllFederateObjectsWithId() {
        List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

        List<FederateObject> federateObjects = federateObjectMapper.selectList(null);
        for(FederateObject fo:federateObjects){
            HashMap<String, Object> element = new HashMap<>();
            element.put("objectName",fo.getObjectName());
            element.put("objectId",fo.getObjectId());
            listMap.add(element);
        }

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap);
    }


}
