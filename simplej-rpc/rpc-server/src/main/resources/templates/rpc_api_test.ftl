<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>rpc service测试页面</title>
    <script src="/webjars/jquery/3.1.1/dist/jquery.min.js"></script>
    <script src="/webjars/jquery-form/4.2.2/dist/jquery.form.min.js"></script>
    <script src="/webjars/jquery-validation/1.19.0/dist/jquery.validate.js"></script>
    <script src="/webjars/jquery-validation/1.19.0/dist/localization/messages_zh.js"></script>
    <script src="/webjars/bootstrap/3.3.7/dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/webjars/bootstrap/3.3.7/dist/css/bootstrap.min.css"/>
    <style type="text/css">

    </style>
</head>
<body>
<#--<div class="page-header">-->
<#--<h1 style="text-align: center">-->
<#--</h1>-->
<#--</div>-->
<div class="panel panel-default">
    <div class="panel-heading"><h2 style="text-align: center">rpc service自助测试工具页面</h2></div>
    <div class="panel-body">
        <div class="container" style="padding: 100px 50px 10px;height: auto;margin:0 auto;border:1px solid #000;">
            <form class="form-horizontal" role="form" id="id_rpc_form">
                <div class="form-group" style="width: 50%;margin:0 auto;">
                    <div class="input-group" style="margin: 10px 0 10px;">
                        <span class="input-group-addon" id="basic-addon2">用例名称</span>
                        <input type="text" name="testCaseName" class="form-control"
                               placeholder="input caseName if you want to save case"
                               aria-describedby="basic-addon2" id="id_test_case_name">
                    </div>
                </div>
                <div class="form-group" id="id_test_case_list" style="width: 50%;margin:0 auto;">
                    <#--参数列表div-->
                </div>
                <div class="form-group" style="width: 50%;margin:0 auto;">
                    <label for="name">选择service列表</label>
                    <select class="form-control" id="id_serviceList_select" name="serviceName">
                        <option value="0" selected>请选择测试服务</option>
                        <#list serviceList as service>
                            <option value="${service.serviceName}">${service.serviceName}-${service.version}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-group" id="id_methods_list" style="width: 50%;margin:0 auto;">
                    <#--方法列表div，勿动-->
                </div>
                <div class="form-group" id="id_params_list" style="width: 50%;margin:0 auto;">
                    <#--参数列表div-->
                </div>
                <input type="hidden" id="input_rpc_response" name="lastResponse">
                <div class="container" style="width: 50%;margin:0 auto;padding: 50px 0px 10px;">
                    <button type="submit" class="btn" id="id_submit">提交</button>
                </div>
            </form>

        </div>
        <div class="container" style="margin:0 auto;border:1px solid #000;">
            <#--<div class="panel-body" style="width: 50%;margin:0 auto;border:1px solid #eb9700;background-color: #fafaeb;">-->
            <#---->
            <#--</div>-->
            <pre style="width: auto;margin:0 auto;" id="id_last_rpc_repsonse"></pre>
            <pre style="width: auto;margin:0 auto;" id="id_rpc_repsonse"></pre>
        </div>
        <div class="container" style="margin:0 auto;border:1px solid #000;display: none" id="id_client_demo">

            <br>
            <p>使用案例：</p>
            <pre>
                rpc客户端调用例子：<br><br>
                远程rpc调用测试类
                <br>
                @Slf4j
                public class <span class="c_short_service_name"></span>Test {

                    private <span class="c_short_service_name"></span> service;

                    @Before
                    public void before() {
                        //静态路由的访问方式（直接指定目的服务ip端口)
                        System.setProperty(RpcConstants.RPC_APP_SERVER_HOST_KEY, "127.0.0.1");
                        System.setProperty(RpcConstants.RPC_APP_SERVER_PORT_KEY, "8899");

                        //通过本地代理proxy自动发现服务自动转发的方式（即service mesh网络架构)
                        //        System.setProperty(RpcConstants.RPC_PROXY_HOST_KEY,"127.0.0.1");
                        //        System.setProperty(RpcConstants.RPC_PROXY_PORT_KEY,"8086");
                        service = ServiceProxy.create().setInterfaceClass(<span class="c_short_service_name"></span>.class).build();
                    }

                    @Test
                    public void <span class="c_short_method_name"></span>Test(){
                        //rpc调用， <span id="id_method_doc"></span>
                        <span id="id_method_return_type"></span> resp = service.<span
                        class="c_short_method_name"></span>(xxxx,xxxx,...);
                        System.out.println("response:" + JSON.toJSONString(resp));
                    }
                }
            </pre>
                            <br><br>
            <pre>
                http客户端调用例子：<br><br>
                @Slf4j
                public class <span class="c_short_service_name"></span>HttpTest {

                    private HttpExecuter httpClient;

                    private String url = "http://%s:%s/rpc/execute";

                    @Before
                    public void before() {
                        //ip和端口可以是指定的目标服务的ip和端口，也可以是本地proxy的ip和端口（proxy会自动路由）
                        String host = "127.0.0.1";
                        String port = "8899";

                        //访问目标地址
                        url=String.format(url,host,port);
                        httpClient = HttpExecuter.create().setMimetype(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                                    .setSocketTimeout(5000).build();

                    }

                    @Test
                    public void <span class="c_short_method_name"></span>Test(){
                        //http调用
                        //请求参数
                        Map<String, String> paramMap = Maps.newHashMap();
                        paramMap.put("xxx","yyyyy");//远程方法的参数

                        //请求头部，必须
                        Map<String, String> header = Maps.newHashMap();
                        header.put("namespace","zzzzz");//必填,每个模块（不是指实例）都应该有一个域（或者说命名空间，如：www.simplej.payment.com）
                        header.put("serviceVersion","1.0V");//必填,每个服务都有对应的版本号
                        header.put("methodId","xxxxxxxxxxx");//必填,该methodId可以唯一的标志某个service服务下的某个特定的方法
                        //
                        //http请求的时候，响应报文是以json响应，调用远程的方法响应值放到了respBody属性里面
                        byte[] resp = httpClient.postForm(url,paramMap,header);
                        JSONObject jsonObject = JSON.parseObject(new String(resp));
                        String value = jsonObject.getString("msg");
                        //真正的远程方法返回值
                        System.out.println("response:" + value);
                    }
                }
            </pre>
        </div>
    </div>
</div>

<#--<div class="panel-footer" style="position: fixed;bottom: 0;width: 100%;"><p style="text-align: center;">copy right @2019</p></div>-->
</div>
<script type="text/javascript">
    $(function () {
        $("#id_serviceList_select").val("0");
        $("select#id_serviceList_select").change(function () {
            $("#id_rpc_repsonse").html("");
            $("#id_methods_list").load('/rpc/query/methods?serviceName=' + $(this).val());
            var longSerivceName = $(this).val();
            var start = longSerivceName.lastIndexOf(".");
            $(".c_short_service_name").html(longSerivceName.substr(start+1));
        });
        $(document).on("change", 'select#id_methods_list_select', function () {
            $("#id_rpc_repsonse").html("");
            //加载方法对应的参数页
            $("#id_params_list").load('/rpc/query/params?methodId=' + $(this).val());
            //加载测试用例信息
            loadTestCaseList($(this).val());
            $("#id_last_rpc_repsonse").html("");
            //加载方法元信息
            getMethodInfo($(this).val());
            $("#id_client_demo").show();
        });
        //测试用例监听
        $(document).on("change", 'select#id_case_list_select', function () {
            getTestCaseDetail($(this).val());
            $("#id_last_rpc_repsonse").html("");
        });

        //
        /* 表单验证，提交 */
        $("#id_rpc_form").validate({
            rules: {},
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {

                $(form).ajaxSubmit({
                    type: 'post', // 提交方式 get/post
                    url: '/rpc/execute', // 需要提交的 url
                    success: function (data) { // data 保存提交后返回的数据，一般为 json 数据
                        // 此处可对 data 作相关处理
                        if (data.respCode === "0" || data.respCode === "success") {
                            // alert(data.respCode);
                            // var msg = JSON.stringify(data.msg);
                            $("#id_rpc_repsonse").html("本次rpc 请求响应信息：<br>" + data.respMsg)
                            $("#input_rpc_response").val(data.respMsg);
                            //保存测试用例
                            saveTestCase();
                            var methodId = $("#id_methods_list_select").val();
                            loadTestCaseList(methodId);
                        } else {
                            alert(data.respMsg);
                        }
                    }
                });
            }
        });
    });

    //保存测试用例
    function saveTestCase() {
        var data = {};
        data = getFromParams();
        $.post("/rpc/case/save", data, function (result) {
            console.log("post case success")
        });

    }

    //加载测试用例列表
    function loadTestCaseList(methodId) {
        $.get('/rpc/case/list?methodId=' + methodId, function (data, status) {
            if (data != "fail") {
                $("#id_test_case_list").html(data);
            } else {
                $("#id_test_case_list").html("");
            }
        });
    }

    //获取表单参数
    function getFromParams() {
        var data = {};
        $("body").find("#id_rpc_form").serializeArray().forEach(function (item) {
            data[item.name] = item.value;   //根据表单元素的name属性来获取数据
        });
        console.log(data);
        return data;
    }
    //获取方法信息
    function getMethodInfo(methodId) {
        $.get('/rpc/method/info?methodId=' + methodId, function (data, status) {
            if (data.respCode === "0" || data.respCode === "success") {
                $(".c_short_method_name").html(data.msg.methodName);
                $("#id_method_doc").html(data.msg.methodDoc);
                $("#id_method_return_type").html(data.msg.methodReturn.returnType);
            } else {
                alert(data.respMsg);
            }
        });
    }

    /**
     * 获取测试用例详情
     */
    function getTestCaseDetail(caseId) {
        $.get('/rpc/case/detail?testCaseId=' + caseId, function (data, status) {
            if (data != "fail") {
                var jsonObj = eval('(' + data + ')');
                // alert(jsonObj);
                for (var key in jsonObj) {
                    $("#" + key).val(jsonObj[key]);
                }
                getLastRpcResponse(caseId);
            } else {
                alert("can not find test case detail");
            }
        });
    }
    //获取测试用例的上一次测试结果
    function getLastRpcResponse(caseId) {
        $.get('/rpc/case/response?testCaseId=' + caseId, function (data, status) {
            if (data != "fail") {
                $("#id_last_rpc_repsonse").html("上一次 rpc 请求响应信息：<br>" + data)
            } else {
                alert("can not find test case detail");
            }
        });
    }
</script>
</body>
</html>