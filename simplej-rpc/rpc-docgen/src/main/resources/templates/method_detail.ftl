<html>
<head>
    <title>rpc在线文档</title>
    <head>
        <style type="text/css">
            table, table tr th, table tr td {
                border: 1px solid #0fefef
            }

            body {
                padding: 10px;
            }

            table {
                margin: 5px;
                border-collapse: collapse;
                padding: 5px;
                width: 88%
            }

            thead {
                background-color: deepskyblue
            }

            pre {
                background-color: #CFCFCF;
                margin: 5px;
                padding: 37px;
                width: 80%;
                border: dashed 1px #000;
            }
        </style>
    </head>
<body>
<div>
    <h3 style="text-align: center">接口文档详情</h3>
    <h4>服务接口：${serviceMeta.serviceName}</h4>
    <h4>服务描述：${serviceMeta.serviceDoc}</h4>
    <h4>服务版本：${serviceMeta.version}</h4>
    <hr>
</div>
<a name="${methodMeta.methodId}"><h3>接口方法：${serviceMeta.serviceName}.${methodMeta.methodName}</h3></a>
<h4>功能：${methodMeta.methodDoc!}</h4>
<h4>methodId：${methodMeta.methodId!}</h4>
<#if methodMeta.createTime != ""><h4>创建时间：${methodMeta.createTime}</h4></#if>
<h4>作者：${methodMeta.author!}</h4>
<table>
    <thead>
    <tr>
        <th colspan="6" style="text-align: left">请求参数</th>
    </tr>
    <tr>
        <td>参数名</td>
        <td>数据类型</td>
        <td>是否必填</td>
        <td>最大长度</td>
        <td>默认值</td>
        <td>描述</td>
    </tr>
    </thead>
    <tbody>
    <#list methodMeta.params as param>
        <tr>
            <td><#if param.structFlag==1><a href="../model/${param.structId}.html" target="f_method_detail">${param
                .name}</a>
                <#else>${param.name}</#if></td>
            <td><#if param.structFlag==1><a href="../model/${param.structId}.html" target="f_method_detail">${param
                .dataType}</a>
                <#else>${param.dataType}</#if></td>
            <td><#if param.required>是<#else>否</#if></td>
            <td><#if param.maxLen !=0>{param.maxLen}</#if></td>
            <td>${param.defaultVale!}</td>
            <td>${param.paramDoc!}</td>
        </tr>
    </#list>


    </tbody>
</table>
<br>
<br>
<table>
    <thead>
    <tr>
        <th colspan="6" style="text-align: left">响应参数</th>
    </tr>
    <tr>
        <td>参数名</td>
        <td>数据类型</td>
        <td>是否必填</td>
        <td>最大长度</td>
        <td>默认值</td>
        <td>描述</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>respCode</td>
        <td>String</td>
        <td>是</td>
        <td></td>
        <td>SUCCESS</td>
        <td>响应码，仅http请求的情况下返回该字段，普通rpc调用不需要关心</td>
    </tr>
    <tr>
        <td>respMsg</td>
        <td>String</td>
        <td>是</td>
        <td></td>
        <td>SUCCESS</td>
        <td>响应信息，仅http请求的情况下返回该字段，普通rpc调用不需要关心</td>
    </tr>
    <tr>
        <td><#if methodMeta.methodReturn.returnParamName??>${methodMeta.methodReturn.returnParamName!}<#else>
                msg
            </#if></td>
        <td><#if methodMeta.methodReturn.structFlag==1>
                <a href="../model/${methodMeta.methodReturn.structId}.html"
                   target="f_method_detail">${methodMeta.methodReturn.returnType!}
                    <#if methodMeta.methodReturn.genericTypeMetas??>&lt;
                        <#list methodMeta.methodReturn.genericTypeMetas
                        as genType>
                            <#if genType.structFlag == 1>
                                <a href="../model/${genType.structId}.html" target="f_method_detail">
                                    ${genType.genericTypeName}</a>
                            <#else >${genType.genericTypeName}
                            </#if>
                            ,
                        </#list>&gt;</#if></a>

            <#else>${methodMeta.methodReturn.returnType!}<#if methodMeta.methodReturn.genericTypeMetas??>&lt;
                <#list methodMeta.methodReturn.genericTypeMetas
                as genType>
                    <#if genType.structFlag == 1>
                        <a href="../model/${genType.structId}.html" target="f_method_detail">
                            ${genType.genericTypeName}</a>
                    <#else >${genType.genericTypeName}
                    </#if>
                    ,
                </#list>&gt;</#if></#if>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td>远程方法执行的结果内容</td>

    </tr>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="6">说明：<br>
            1.http请求的时候，响应报文是以json响应，调用远程的方法响应值放到了msg属性里面，具体可参考下面案例<br>
            2.rpc调用的时候，获取返回值时，不需考虑msg，与平常本地方法调用一样即可
        </td>
    </tr>
    </tfoot>
</table>

<br>
<br>
<br>
<p>使用案例：</p>
<pre>
rpc客户端调用例子：<br><br>
第一步：把目标服务提供的rpc-api 的maven配置加上，如：<br>
&lt;dependency&gt;
    &lt;groupId&gt;${mavenCfgModel.groupId}&lt;/groupId&gt;
    &lt;artifactId&gt;${mavenCfgModel.artifactId}&lt;/artifactId&gt;
    &lt;version&gt;${mavenCfgModel.version}&lt;/version&gt;
&lt;/dependency&gt;
<br><br>
第二步：远程调用测试类
<br>
@Slf4j
public class ${shortServiceName}Test {

    private ${shortServiceName} service;

    @Before
    public void before() {
        //静态路由的访问方式（直接指定目的服务ip端口)
        System.setProperty(RpcConstants.RPC_APP_SERVER_HOST_KEY, "127.0.0.1");
        System.setProperty(RpcConstants.RPC_APP_SERVER_PORT_KEY, "8899");

        //通过本地代理proxy自动发现服务自动转发的方式（即service mesh网络架构)
        //        System.setProperty(RpcConstants.RPC_PROXY_HOST_KEY,"127.0.0.1");
        //        System.setProperty(RpcConstants.RPC_PROXY_PORT_KEY,"8086");
        service = ServiceProxy.create().setInterfaceClass(${shortServiceName}.class).build();
    }

    @Test
    public void ${methodMeta.methodName}Test(){
        //rpc调用，${methodMeta.methodDoc!}
    ${methodMeta.methodReturn.returnType!} resp = service.${methodMeta.methodName}(xxxx,xxxx,...);
        System.out.println("response:" + JSON.toJSONString(resp));
    }
}
</pre>
<br><br>
<pre>
http客户端调用例子：<br><br>
@Slf4j
public class ${shortServiceName}HttpTest {

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
    public void ${methodMeta.methodName}Test(){
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
</body>
</html>
