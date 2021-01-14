package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.entity.RuntimeControl;
import com.ices.discrete_event_simulation.mapper.RuntimeControlMapper;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class GenerateXMLController {

    //下载文件
    @RequestMapping(value = "/generateXML/FileDownload" , method = RequestMethod.GET)
    @ResponseBody
    public void downloadXMLFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 下载本地文件
        String fileName = "simulation.xml".toString(); // 文件的默认保存名
        // 把生成的文件读到流中，下载
        InputStream inStream = new FileInputStream("/Users/america/Desktop/Activiti-develop/des_simulation/src/main/resources/FederationBPMN/QueueTheory.xml");// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    RuntimeControlMapper runtimeControlMapper;

    // /post/runtime
    @RequestMapping(value = "/generateXML/runtime" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse RuntimePost(@RequestBody JSONObject json){
        String runtime = json.get("runtime").toString();
        RuntimeControl runtimeControl = new RuntimeControl();
        runtimeControl.setRuntime(Integer.parseInt(runtime));
        runtimeControlMapper.insert(runtimeControl);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"已设置仿真运行时间为"+runtime+"天");
    }
}
