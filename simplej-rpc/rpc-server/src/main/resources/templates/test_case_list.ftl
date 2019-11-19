<label for="name">请选择测试用例</label>
<select class="form-control" id="id_case_list_select">
    <option value="0" selected>请选择测试用例</option>
    <#list caseList as cs>
        <option value="${cs.testCaseId}">${cs.testCaseName}</option>
    </#list>
</select>
