<label for="name">选择请求方法</label>
<select class="form-control" id="id_methods_list_select"  name="methodId">
    <option value="0" selected>请选择测试方法</option>
    <#list methods as method>
        <option value="${method.methodId}">${method.methodName}</option>
    </#list>
</select>
