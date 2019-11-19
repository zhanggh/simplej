<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>http测试工具页面</title>
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
    <div class="panel-heading"><h2 style="text-align: center">http自助测试工具页面</h2></div>
    <div class="panel-body">
        <div class="container" style="padding: 100px 50px 10px;height: auto;margin:0 auto;border:1px solid #000;">
            <form class="form-horizontal" role="form" id="id_http_form">
                <div class="form-group" style="width: 50%;margin:0 auto;">
                    <div class="input-group" style="margin: 10px 0 10px;">
                        <span class="input-group-addon" id="basic-addon2">用例名称</span>
                        <input type="text" name="testCaseName" class="form-control"
                               placeholder="input caseName if you want to save case"
                               aria-describedby="basic-addon2" id="id_test_case_name">
                    </div>
                </div>
                <div class="form-group" id="id_test_case_list" style="width: 50%;margin:0 auto;">

                </div>
                <div class="form-group" id="id_http_method_list" style="width: 50%;margin:0 auto;">
                    <label for="name">请选择http方法</label>
                    <select class="form-control" id="httpMethod" name="httpMethod">
                        <option value="get" selected>GET</option>
                        <option value="post" selected>POST</option>
                    </select>
                </div>
                <div class="form-group" style="width: 50%;margin:0 auto;">
                    <div class="input-group" style="margin: 10px 0 10px;">
                        <span class="input-group-addon" id="basic-addon2">url地址</span>
                        <input type="text" name="url" class="form-control"
                               placeholder="input url,such as http://localhost/xxxx"
                               aria-describedby="basic-addon2" id="url" required>
                    </div>
                </div>

                <div class="form-group" style="width: 50%;margin:0 auto;">
                    <div class="input-group" style="margin: 10px 0 10px;">
                        <span class="input-group-addon" id="basic-addon2">Content-Type</span>
                        <input type="text" name="contentType" class="form-control"
                               placeholder="input Content-Type,such as application/json"
                               aria-describedby="basic-addon2" id="contentType" required>
                    </div>
                </div>
                <div class="form-group" id="id_headers" style="width: 50%;margin:0 auto;">
                    <label for="headers">http header</label>
                    <textarea class="form-control" rows="3" name="headers" id="headers"
                              placeholder="input header,such as:Cookie: xxxxxx;"></textarea>
                </div>
                <div class="form-group" id="id_body" style="width: 50%;margin:0 auto;">
                    <label for="httpBody">http body</label>
                   <textarea class="form-control" rows="3" name="httpBody" id="httpBody"
                             placeholder="input http body,such as: key1=value1&key2=value2..."></textarea>
                </div>
                <input type="hidden" id="id_methodId" name="methodId" value="http">
                <input type="hidden" id="input_http_response" name="lastResponse">

                <div class="container" style="width: 50%;margin:0 auto;padding: 50px 0px 10px;">
                    <button type="submit" class="btn" id="id_submit">提交</button>
                </div>
            </form>

        </div>
        <div class="container" style="margin:0 auto;border:1px solid #000;">
            <#--<div class="panel-body" style="width: 50%;margin:0 auto;border:1px solid #eb9700;background-color: #fafaeb;">-->
            <#---->
            <#--</div>-->
            <pre style="width: auto;margin:0 auto;" id="id_last_http_repsonse"></pre>
            <pre style="width: auto;margin:0 auto;" id="id_http_repsonse"></pre>
        </div>
    </div>
</div>

<#--<div class="panel-footer" style="position: fixed;bottom: 0;width: 100%;"><p style="text-align: center;">copy right @2019</p></div>-->
</div>
<script type="text/javascript">
    $(function () {
        var methodId = "http";
        loadTestCaseList(methodId);
        //测试用例监听
        $(document).on("change", 'select#id_case_list_select', function () {
            getTestCaseDetail($(this).val());
            $("#id_last_http_repsonse").html("");
        });

        /* 表单验证，提交 */
        $("#id_http_form").validate({
            rules: {},
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {

                $(form).ajaxSubmit({
                    type: 'post', // 提交方式 get/post
                    url: '/http/execute', // 需要提交的 url
                    success: function (data) { // data 保存提交后返回的数据，一般为 json 数据
                        // 此处可对 data 作相关处理
                        if (data.respCode === "0" || data.respCode === "success") {
                            $("#input_http_response").val(data.msg);
                            $("#id_http_repsonse").html("本次http 请求响应信息：<br>" + data.msg)
                            saveTestCase();
                            var methodId = "http";
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

    //获取表单参数
    function getFromParams() {
        var data = {};
        $("body").find("#id_http_form").serializeArray().forEach(function (item) {
            data[item.name] = item.value;   //根据表单元素的name属性来获取数据
        });
        console.log(data);
        return data;
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
                $("#id_last_http_repsonse").html("上一次 http 请求响应信息：<br>" + data)
            } else {
                alert("can not find test case last response");
            }
        });
    }

</script>
</body>
</html>