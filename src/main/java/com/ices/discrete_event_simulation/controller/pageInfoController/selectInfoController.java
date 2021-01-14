package com.ices.discrete_event_simulation.controller.pageInfoController;

import com.ices.discrete_event_simulation.entity.Federate;
import com.ices.discrete_event_simulation.entity.FederateObject;
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
}
