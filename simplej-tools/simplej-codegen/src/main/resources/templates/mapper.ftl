<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.${business}.mapper.${entity.name}Mapper">

    <select id="get" resultType="${package}.${business}.model.${entity.name}Model">
        SELECT
        <include refid="query_fields" />
        FROM ${entity.schema}.${entity.tableName} t
        WHERE id = ${"#{"}id}
	</select>

	<select id="count" resultType="int">
		SELECT COUNT(1) AS count FROM ${entity.schema}.${entity.tableName} t
		<include refid="query_where" />
	</select>
	
	<select id="query" resultType="${package}.${business}.model.${entity.name}Model">
        SELECT
		<include refid="query_fields" />
		FROM ${entity.schema}.${entity.tableName} t
		<include refid="query_where" />
		ORDER BY t.id
		<if test="start != null and length != null">
    	LIMIT ${"#{"}start}, ${"#{"}length}
       </if>
	</select>
	
	<insert id="create" parameterType="${package}.${business}.model.${entity.name}Model" keyProperty="id" useGeneratedKeys="true">
		INSERT INTO `${entity.schema}.${entity.tableName}` SET
		<include refid="save_fields" />
		<if test="create_time == null">
			`create_time` = NOW(),
		</if>
		<if test="update_time == null">
			`update_time` = NOW()
		</if>
	</insert>
	
	<update id="update">
		UPDATE `${entity.schema}.${entity.tableName}` SET
		<include refid="save_fields" />
		<if test="update_time == null">
			`update_time` = NOW()
		</if>
		WHERE `id` = ${"#{"}id}
	</update>
	
	<delete id="remove">
        update `${entity.schema}.${entity.tableName}` set is_deleted = 1 WHERE `id` IN
        <foreach collection="ids" item="id" open="(" separator="," close=")">${"#{"}id}</foreach>
  </delete>

	<sql id="query_where">
		WHERE 1=1<#list entity.fields as fd>
		<if test="${fd.name} != null<#if fd.type == "String"> and !${fd.name}.isEmpty()</#if>">AND t.`${fd.columnName}` = ${"#{"}${fd.name}}</if></#list>
	</sql>

	<sql id="query_fields"><#list entity.fields as fd>
		t.`${fd.columnName}` AS ${fd.name}<#if fd_has_next>,</#if></#list>
	</sql>

	<sql id="save_fields"><#list entity.fields as fd><#if fd.name != "id" && fd.name != "createTime" && fd.name != "updateTime">
		<if test="${fd.name} != null">`${fd.columnName}` = ${"#{"}${fd.name}},</if></#if></#list>
	</sql>

</mapper>