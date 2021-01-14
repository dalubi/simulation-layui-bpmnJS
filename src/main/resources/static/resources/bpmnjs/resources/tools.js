import $ from 'jquery';
const proHost = window.location.protocol + "//" + window.location.host;
const href = window.location.href.split("bpmnjs")[0];
const key = href.split(window.location.host)[1];
const publicurl = proHost + key;
const tools = {
    /**
     * 查看bpmn
     * @param {object} bpmnModeler bpmn对象
     */
    viewBpmn(bpmnModeler,container) {
        //获取xml
        $.ajax({
            url: "http://localhost:8080/bpmnFederate/getBPMNStr",
            type: 'POST',
            data: {},
            dataType:'text',
            success: function (result) {
                //xml数据
                console.log(result);
                var newXmlData = result;
                console.log(newXmlData);
                tools.createDiagram(newXmlData, bpmnModeler, container);
                setTimeout(function () {
                    for (var i in ColorJson) {
                        tools.setColor(ColorJson[i],bpmnModeler)
                    }
                }, 200)
            },
            error: function (err) {
                console.log(err)
            }
        });
    },
    /**
     * 下载bpmn
     * @param {object} bpmnModeler bpmn对象
     */
    downLoad(bpmnModeler) {

    },
    /**
     * 转码xml并下载
     * @param {object} link 按钮
     * @param {string} name 下载名称
     * @param {string} data base64XML
     */
    setEncoded(link, name, data) {
        var encodedData = encodeURIComponent(data);
        if (data) {
            link.addClass('active').attr({
                'href': 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData,
                'download': name
            });
        } else {
            link.removeClass('active');
        }
    },
    /**
     * 保存bpmn对象
     * @param {object} bpmnModeler bpmn对象
     */
    saveBpmn(bpmnModeler) {
        bpmnModeler.saveXML({ format: true }, function (err, xml) {
            if (err) {
                return console.error('保存失败，请重试', err);
            }
            console.log(xml)
            var param={
                "stringBPMN":xml
            }
            $.ajax({
                url: publicurl+'processDefinition/addDeploymentByString',
                type: 'POST',
                dataType:"json",
                data: param,
                //headers:{'Content-Type':'application/json;charset=utf8'},
                success: function (result) {
                    if(result.msg=='成功'){
                        tools.syhide('alert')
                        alert('保存成功')
                    }else{
                        alert(result.msg)
                    }
                },
                error: function (err) {
                    console.log(err)
                }
            });
        });
    },
    upload(bpmnModeler, container) {
        var FileUpload = document.myForm.uploadFile.files[0];
        var fm = new FormData();
        fm.append('processFile', FileUpload);
        $.ajax({
            url: publicurl+'processDefinition/upload',
            type: 'POST',
            data: fm,
            async: false,
            contentType: false, //禁止设置请求类型
            processData: false, //禁止jquery对DAta数据的处理,默认会处理
            success: function (result) {
                var url = publicurl+'bpmn/' + result.obj
                tools.openFromUrl(bpmnModeler, container, url)
            },
            error: function (err) {
                console.log(err)
            }
        });
    },
    /**
     * 打开xml  Url 地址
     * @param {object} bpmnModeler bpmn对象
     * @param {object} container 容器对象
     * @param {string} url url地址
     */
    openFromUrl(bpmnModeler, container, url) {
        $.ajax(url, { dataType: 'text' }).done(async function (xml) {
            try {
                await bpmnModeler.importXML(xml);
                container.removeClass('with-error').addClass('with-diagram');
            } catch (err) {
                console.error(err);
            }
        });
    },
    /**
     * 根据数据设置颜色
     * @param data
     * @returns {Array}
     */
    getByColor(data){
        var ColorJson=[]
        for(var k in data['highLine']){
            var par={
                "name": data['highLine'][k],
                "stroke":"green",
                "fill":"green"
            }
            ColorJson.push(par)
        }
        for(var k in data['highPoint']){
            var par={
                "name": data['highPoint'][k],
                "stroke":"gray",
                "fill":"#eae9e9"

            }
            ColorJson.push(par)
        }
        for(var k in data['iDo']){
            var par={
                "name": data['iDo'][k],
                "stroke":"green",
                "fill":"#a3d68e"
            }
            ColorJson.push(par)
        }
        for(var k in data['waitingToDo']){
            var par={
                "name": data['waitingToDo'][k],
                "stroke":"green",
                "fill":"yellow"
            }
            ColorJson.push(par)
        }
        return ColorJson
    },
    /**
     * 判断是否是数组
     * @param value
     * @returns {arg is Array<any>|boolean}
     */
    isArrayFn(value){
        if (typeof Array.isArray === "function") {
            return Array.isArray(value);
        }else{
            return Object.prototype.toString.call(value) === "[object Array]";
        }
    },
    /**
     * 通过xml创建bpmn
     * @param {string} xml 创建bpms xml
     * @param {object} bpmnModeler bpmn对象
     * @param {object} container 容器对象
     */
    async createDiagram(xml, bpmnModeler, container) {
        try {
            await bpmnModeler.importXML(xml);
            container.removeClass('with-error').addClass('with-diagram');
        } catch (err) {
            container.removeClass('with-diagram').addClass('with-error');
            container.find('.error pre').text(err.message);
            console.error(err);
        }
    },
    /**
     * 通过Json设置颜色
     * @param {object} json json 字符串
     */
    setColor(json,bpmnModeler) {
        var modeling = bpmnModeler.get('modeling');
        var elementRegistry = bpmnModeler.get('elementRegistry')
        var elementToColor = elementRegistry.get(json.name);
        if(elementToColor){
            modeling.setColor([elementToColor], {
                stroke: json.stroke,
                fill: json.fill
            });
        }
    },
    showBpmn(bpmnModeler,container){
        //这里错了，明天再改
        //首先，获取deploymentId和resourceName
        $.ajax({
            url: '/activitiHistory/getParam',
            type: 'GET',
            data: "",
            dataType: 'json',
            contentType: "application/json;charset=utf-8",//发送给后台数据的格式
            success: function (result) {
                //把obj类型转化成json类型的字符串
                let resJSONStr = JSON.stringify(result);
                let resJSON = JSON.parse(resJSONStr);
                let msg = resJSON.obj;
                var param={
                    "deploymentId":msg.deploymentId,
                    "resourceName":msg.resourceName
                };
                var piId = msg.processInstanceId;
                $.ajax({
                    url: '/activitiHistory/gethighLine',
                    type: 'GET',
                    data: "instanceId="+piId,
                    dataType:'json',
                    success: function (result) {
                        var ColorJson=tools.getByColor(result.obj)
                        $.ajax({
                            url: '/processDefinition/getDefinitionXML',
                            type: 'GET',
                            data: param,
                            dataType:'text',
                            success: function (result) {
                                var newXmlData = result;
                                tools.createDiagram(newXmlData, bpmnModeler, container);
                                setTimeout(function () {
                                    for (var i in ColorJson) {
                                        tools.setColor(ColorJson[i],bpmnModeler)
                                    }
                                }, 200)
                            },
                            error: function (err) {
                                console.log(err)
                            }
                        });
                    },
                    error: function (err) {
                        console.log(err)
                    }
                });
            }
        });

    },
};
export default tools