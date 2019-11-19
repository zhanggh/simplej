<h4 style="margin: 86px 0 10px;">请求参数:</h4>
<#list paramList as param>
    <#if param.structFlag == 0>
        <#if param.mapFlag==1>
        <#--默认是支持3个输入框-->
            <div class="input-group" style="margin: 10px 0 10px;" id="id_map_input_2">
                <span class="input-group-addon" id="basic-addon2">${param.name}(${param.dataType})</span>
                <input type="text" name="${param.name}.key1" id="${param.name}.key1" class="form-control"
                       placeholder="input ${param.name} key1 of map "
                       aria-describedby="basic-addon2">
                <input type="text" name="${param.name}.value1" id="${param.name}.value1" class="form-control"
                       placeholder="input ${param.name} value1 of map"
                       aria-describedby="basic-addon2">
            </div>
            <div class="input-group" style="margin: 10px 0 10px;" id="id_map_input_1">
                <span class="input-group-addon" id="basic-addon2">${param.name}(${param.dataType})</span>
                <input type="text" name="${param.name}.key2" id="${param.name}.key2" class="form-control"
                       placeholder="input ${param.name} key2 of map "
                       aria-describedby="basic-addon2">
                <input type="text" name="${param.name}.value2" id="${param.name}.value2" class="form-control"
                       placeholder="input ${param.name} value2 of map"
                       aria-describedby="basic-addon2">
            </div>
            <div class="input-group" style="margin: 10px 0 10px;" id="id_map_input_3">
                <span class="input-group-addon" id="basic-addon2">${param.name}(${param.dataType})</span>
                <input type="text" name="${param.name}.key3" id="${param.name}.key3" class="form-control"
                       placeholder="input ${param.name} key3 of map "
                       aria-describedby="basic-addon2">
                <input type="text" name="${param.name}.value3" id="${param.name}.value3" class="form-control"
                       placeholder="input ${param.name} value3 of map"
                       aria-describedby="basic-addon2">
            </div>
        <#elseif param.listFlag==1 || param.arrayFlag ==1>
        <#--默认是支持3个输入框-->
            <div class="input-group" style="margin: 10px 0 10px;" id="id_list_input">
                <span class="input-group-addon" id="basic-addon2">${param.name}(${param.dataType})</span>
                <input type="text" name="${param.name}" id="${param.name}" class="form-control"
                       placeholder="input ${param.name}"
                       aria-describedby="basic-addon2">
                <input type="text" name="${param.name}" id="${param.name}" class="form-control"
                       placeholder="input ${param.name}"
                       aria-describedby="basic-addon2">
                <input type="text" name="${param.name}" id="${param.name}" class="form-control"
                       placeholder="input ${param.name}"
                       aria-describedby="basic-addon2">
            </div>
        <#else >
            <div class="input-group" style="margin: 10px 0 10px;">
                <span class="input-group-addon" id="basic-addon2">${param.name}(${param.dataType})</span>
                <input type="text" name="${param.name}" id="${param.name}" class="form-control"
                       placeholder="input ${param.name}"
                       aria-describedby="basic-addon2"
                        <#if param.required> required </#if>>
            </div>
        </#if>

    <#else>
        <div class="panel-heading"><h5>${param.name}(${param.dataType})</h5></div>
        <#list param.child as child1>
            <#if child1.structFlag == 0>
            <#--非结构体的情况-->
                <div class="input-group" style="margin: 10px 0 10px;">
                    <span class="input-group-addon" id="basic-addon2">${child1.name}(${child1.dataType})</span>
                    <input type="text" name="${param.name}.${child1.name}" class="form-control"
                           placeholder="input ${child1.name}"
                           id="${param.name}_${child1.name}"
                           aria-describedby="basic-addon2" <#if child1.required> required
                            </#if>>
                </div>
            <#else >
                <div class="panel-heading"><h5>${child1.name}(${child1.dataType})</h5></div>
                <#list child1.child as child2>
                    <#if child2.structFlag == 0>
                    <#--非结构体的情况-->
                        <div class="input-group" style="margin: 10px 0 10px;">
                            <span class="input-group-addon" id="basic-addon2">${child.name}(${child.dataType})</span>
                            <input type="text" name="${param.name}.${child1.name}.${child2.name}" class="form-control"
                                   placeholder="input ${child2.name}"
                                   id="${param.name}_${child1.name}_${child2.name}"
                                   aria-describedby="basic-addon2" <#if child2.required> required
                                    </#if>>
                        </div>
                    </#if>
                </#list>
            </#if>
        </#list>
    </#if>
</#list>