MWF.xDesktop.requireApp("process.Xform", "$Module", null, false);
/** @class Widget 门户的部件组件。
 * @o2cn 部件
 * @example
 * //可以在脚本中获取该组件
 * //方法1：
 * var widget = this.form.get("fieldId"); //获取组件
 * //方法2
 * var widget = this.target; //在组件本身的脚本中获取
 * @extends MWF.xApplication.process.Xform.$Module
 * @o2category FormComponents
 * @o2range {Portal}
 * @hideconstructor
 */
MWF.xApplication.process.Xform.Widget = MWF.APPWidget =  new Class(
    /** @lends MWF.xApplication.process.Xform.Widget# */
    {
    Extends: MWF.APP$Module,
    options: {
        /**
         * 部件的设计已经获取到，但还没有插入html及生成内部组件。
         * @event MWF.xApplication.process.Xform.Widget#beforeModulesLoad
         * @see {@link https://www.yuque.com/o2oa/ixsnyt/hm5uft#i0zTS|组件事件说明}
         */
        /**
         * 部件的设计已经获取到，已经插入html，组件json已经获取到，但未生成内部组件。
         * * @example
         * //获取部件所有组件id
         * var moduleIdList = Object.keys(this.target.widgetData.json.moduleList);
         * @event MWF.xApplication.process.Xform.Widget#modulesLoad
         * @see {@link https://www.yuque.com/o2oa/ixsnyt/hm5uft#i0zTS|组件事件说明}
         */
        /**
         * 部件内部组件加载完成。
         * @example
         * //获取部件所有组件id
         * var moduleIdList = Object.keys(this.target.widgetData.json.moduleList);
         * //获取部件所有组件
         * var moduleList = moduleIdList.map(function(id){
         *     return this.form.get(id, widgetId); //widgetId为当前部件ID，布局组件有可能id冲突，通过widgetId来确定当前部件的组件
         * }.bind(this))
         * @event MWF.xApplication.process.Xform.Widget#afterModulesLoad
         * @see {@link https://www.yuque.com/o2oa/ixsnyt/hm5uft#i0zTS|组件事件说明}
         */
        "moduleEvents": ["load", "queryLoad", "postLoad", "beforeModulesLoad", "modulesLoad", "afterModulesLoad"]
    },

    _loadUserInterface: function(){
        this.node.empty();

        this.modules = [];
        this.moduleList = {};

        this.getWidget(function(){
            this.loadWidget();
        }.bind(this));
    },
    /**
     * @summary 重新加载部件
     * @example
     * this.form.get("fieldId").reload()
     */
    reload: function(){
        this.clean();

        this.getWidget(function(){
            this.loadWidget();
        }.bind(this));
    },
    clean: function(){
        (this.modules || []).each(function(module){
            if (this.form.all[module.json.id]) delete this.form.all[module.json.id];
            if (this.form.forms[module.json.id])delete this.form.forms[module.json.id];
            this.form.modules.erase(module);
        }.bind(this));

        Object.each(this.moduleList || {}, function (module, formKey) {
            delete this.form.json.moduleList[formKey];
        }.bind(this));

        if( this.widgetData && this.widgetData.json.id ){
            var id = this.widgetData.json.id;
            // if( this.form.subformLoaded && this.form.subformLoaded.length ){
            //     this.form.subformLoaded.erase(id);
            // }
            if( this.parentpageIdList && this.parentpageIdList.length){
                this.parentpageIdList.erase(id);
            }
        }

        if( this.json && this.json.id && this.form.widgetModules && this.form.widgetModules[ this.json.id ] ){
            this.form.widgetModules[ this.json.id ] = {};
        }

        this.modules = [];
        this.moduleList = {};

        this.node.empty();
    },
    loadCss: function(){
        if (this.widgetData.json.css && this.widgetData.json.css.code){
            var cssText = this.form.parseCSS(this.widgetData.json.css.code);
            var rex = new RegExp("(.+)(?=\\{)", "g");
            var match;
            var id = this.form.json.id.replace(/\-/g, "");
            while ((match = rex.exec(cssText)) !== null) {
                var prefix = ".css" + id + " ";
                var rule = prefix + match[0];
                cssText = cssText.substring(0, match.index) + rule + cssText.substring(rex.lastIndex, cssText.length);
                rex.lastIndex = rex.lastIndex + prefix.length;
            }

            var styleNode = $("style"+this.form.json.id);
            if (!styleNode){
                var styleNode = document.createElement("style");
                styleNode.setAttribute("type", "text/css");
                styleNode.id="style"+this.form.json.id;
                styleNode.inject(this.form.container, "before");
            }

            if(styleNode.styleSheet){
                var setFunc = function(){
                    styleNode.styleSheet.cssText += cssText;
                };
                if(styleNode.styleSheet.disabled){
                    setTimeout(setFunc, 10);
                }else{
                    setFunc();
                }
            }else{
                var cssTextNode = document.createTextNode(cssText);
                styleNode.appendChild(cssTextNode);
            }
        }
    },
    checkWidgetNested : function( id ){
        if( this.parentpageIdList ){
            return !this.parentpageIdList.contains( id );
        }else{
            return ![ this.form.json.id ].contains( id );
        }
    },
    getParentpageIdList : function(){
        var parentpageIdList;
        if( this.parentpageIdList ){
            parentpageIdList = Array.clone( this.parentpageIdList );
            parentpageIdList.push( this.widgetData.json.id )
        }else{
            parentpageIdList = [ this.form.json.id, this.widgetData.json.id ];
        }
        return parentpageIdList;
    },
    loadWidget: function(){
        if (this.widgetData ){
            if( this.checkWidgetNested( this.widgetData.json.id ) ){
                //this.form.addEvent("postLoad", function(){

                this.fireEvent("beforeModulesLoad");

                this.loadCss();

                this.modules = [];
                this.moduleList = {};

                this.form.widgetModules =  this.form.widgetModules || {};
                var widgetModules = this.form.widgetModules[ this.json.id ] = {};

                var params = this.getPageParamenters();
                if( typeOf(params) === "object" && this.form.Macro && this.form.Macro.environment ){
                    var environment = this.form.Macro.environment;
                    environment.widgetParameters = environment.widgetParameters || {};
                    environment.widgetParameters[ this.json.id ] = params;
                }

                this.node.set("html", this.widgetData.html);

                Object.each(this.widgetData.json.moduleList, function(module, key){
                    var formKey = key;
                    if (this.form.json.moduleList[key]){
                        formKey = this.json.id+"_"+key;
                        var moduleNode = this.node.getElement("#"+key);
                        if (moduleNode) moduleNode.set("id", formKey);
                        module.orgiginalId = key;
                        module.id = formKey;
                    }
                    this.form.json.moduleList[formKey] = module;
                    this.moduleList[formKey] = module;
                }.bind(this));

                this.fireEvent("modulesLoad");

                var moduleNodes = this.form._getModuleNodes(this.node);
                moduleNodes.each(function(node){
                    if (node.get("MWFtype")!=="form"){
                        var _self = this;
                        var json = this.form._getDomjson(node);
                        var module = this.form._loadModule(json, node, function(){
                            this.widget = _self;
                            this.parentpageIdList = _self.getParentpageIdList();
                        });
                        this.form.modules.push(module);
                        this.modules.push(module);

                        widgetModules[ json.orgiginalId || json.id ] = module;
                    }
                }.bind(this));

                this.fireEvent("afterModulesLoad");

                //}.bind(this));
            }else{
                this.form.notice(MWF.xApplication.process.Xform.LP.widgetNestedError, "error");
            }
        }
        if( this.form.widgetLoadedCount ){
            this.form.widgetLoadedCount++;
        }else{
            this.form.widgetLoadedCount = 1
        }
        this.form.checkSubformLoaded();
    },
    getWidget: function(callback){
        var method = (this.form.options.mode !== "Mobile" && !layout.mobile) ? "getWidgetByName" : "getWidgetByNameMobile";
        if (this.json.widgetType==="script"){
            if (this.json.widgetScript && this.json.widgetScript.code){
                var data = this.form.Macro.exec(this.json.widgetScript.code, this);
                if (data){
                    var widgetName, app;
                    if (typeOf(data) === "string") {
                        widgetName = data;
                    } else {
                        if (data.application) app = data.application;
                        if (data.widget) widgetName = data.widget;
                    }
                    if (widgetName) {
                        if (!app)  app = this.form.businessData.pageInfor.portal;
                        o2.Actions.get("x_portal_assemble_surface")[method](widgetName, app, function(json){
                            this.getWidgetData(json.data);
                            if (callback) callback();
                        }.bind(this));
                    }else{
                        if (callback) callback();
                    }
                }else{
                    if (callback) callback();
                }
            }
        }else{
            if (this.json.widgetSelected && this.json.widgetSelected!=="none"){

                var widgetData = (this.form.app.relatedFormMap) ? this.form.app.relatedFormMap[this.json.widgetSelected] : null;
                if (widgetData){
                    this.getWidgetData({"data": widgetData.data});
                    if (callback) callback();

                }else{
                    var app;
                    if (this.json.widgetAppSelected) {
                        app = this.json.widgetAppSelected;
                    } else {
                        app = this.form.businessData.pageInfor.portal;
                    }
                    o2.Actions.get("x_portal_assemble_surface")[method](this.json.widgetSelected, app, function(json){
                        this.getWidgetData(json.data);
                        if (callback) callback();
                    }.bind(this));
                }
            }else{
                if (callback) callback();
            }
        }
    },
    getWidgetData: function(data){
        var widgetDataStr = null;
        //if (this.form.options.mode !== "Mobile" && !layout.mobile){
        //    widgetDataStr = data.data;
        //}else{
        //    widgetDataStr = data.mobileData;
        //}
        widgetDataStr = data.data;
        this.widgetData = null;
        if (widgetDataStr){
            if( this.form.isParseLanguage ){
                var jsonStr = o2.bindJson(MWF.decodeJsonString(widgetDataStr),  {"lp": MWF.xApplication.process.Xform.LP.form});
                this.widgetData = JSON.decode(jsonStr);
            }else{
                this.widgetData = JSON.decode(MWF.decodeJsonString(widgetDataStr));
            }
            this.widgetData.updateTime = data.updateTime;
        }
    },
    /**
     * @summary 获取设计部件时设置的参数
     * @return 设置的参数
     * @example
     * var param = this.form.get("fieldId").getPageParamenters()
     */
    getPageParamenters : function(){
        var params = null;
        if( this.json.parameterType === "map" ){
            params = this.json.parametersMapList;
        }else if( this.json.parameterType === "script" ){
            var code = (this.json.parametersScript) ? this.json.parametersScript.code : "";
            if (code){
                params = this.form.Macro.exec(code, this);
            }
        }
        return params;
    }
});
