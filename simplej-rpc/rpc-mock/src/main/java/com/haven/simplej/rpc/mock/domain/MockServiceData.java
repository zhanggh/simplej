package com.haven.simplej.rpc.mock.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.rpc.mock.domain.definition.MockServiceDataDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.sql.Timestamp;
/**
 * 远程服务方法模拟结果信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "mock_service_data", schema = "rpc_center", indexes = { // catalog 定义为分表数
    @Index(name = "idx_namespace_method_id", columnList = "namespace, method_id", unique = true), //
})
public class MockServiceData extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 自增主键
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "timeout", nullable = false, precision = 20, comment = "自增主键", dataType="bigint")
	private Long timeout;

	/**
     * 所属的命名空间
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "namespace", nullable = false, length = 128, comment = "所属的命名空间", dataType="varchar")
	private String namespace;

	/**
     * 服务名
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "service_name", nullable = false, length = 128, comment = "服务名", dataType="varchar")
	private String serviceName;

	/**
     * 方法名
     * @definition varchar(1024) DEFAULT '' 
     */
    @ColumnProperty(name = "method_name", nullable = false, length = 1024, comment = "方法名", dataType="varchar")
	private String methodName;

	/**
     * 方法id
     * @definition varchar(64) DEFAULT '' 
     */
    @ColumnProperty(name = "method_id", nullable = false, length = 64, comment = "方法id", dataType="varchar")
	private String methodId;

	/**
     * 方法参数类型
     * @definition varchar(512) DEFAULT '' 
     */
    @ColumnProperty(name = "param_type", nullable = false, length = 512, comment = "方法参数类型", dataType="varchar")
	private String paramType;

	/**
     * 返回方法参数类型
     * @definition varchar(512) DEFAULT '' 
     */
    @ColumnProperty(name = "return_type", nullable = false, length = 512, comment = "返回方法参数类型", dataType="varchar")
	private String returnType;

	/**
     * 响应信息json
     * @definition varchar(4096) DEFAULT '' 
     */
    @ColumnProperty(name = "response", nullable = false, length = 4096, comment = "响应信息json", dataType="varchar")
	private String response;

	/**
     * 规则
     * @definition varchar(4096) DEFAULT '' 
     */
    @ColumnProperty(name = "rule", nullable = false, length = 4096, comment = "规则", dataType="varchar")
	private String rule;

	/**
     * 服务版本号
     * @definition varchar(30) DEFAULT '0' 
     */
    @ColumnProperty(name = "version", nullable = false, length = 30, comment = "服务版本号", dataType="varchar")
	private String version;

	/**
     * 生效时间
     * @definition timestamp(3) DEFAULT '1970-01-02 00:00:00.000' 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ColumnProperty(name = "effective_time", nullable = false, comment = "生效时间", dataType="timestamp")
	private Timestamp effectiveTime;

	/**
     * 失效时间
     * @definition timestamp(3) DEFAULT '2038-01-02 00:00:00.000' 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ColumnProperty(name = "expire_time", nullable = false, comment = "失效时间", dataType="timestamp")
	private Timestamp expireTime;

	/**
     * init-初始状态，未生效, valid-有效状态,invalid-无效状态
     * @definition varchar(10) DEFAULT 'init' 
     */
    @ColumnProperty(name = "status", nullable = false, length = 10, comment = "init-初始状态，未生效, valid-有效状态,invalid-无效状态", dataType="varchar")
	private String status;

	public MockServiceData (){
		super();
	}
	
	public MockServiceData (MockServiceData that){
        super(that);
		this.timeout = that.timeout;
		this.namespace = that.namespace;
		this.serviceName = that.serviceName;
		this.methodName = that.methodName;
		this.methodId = that.methodId;
		this.paramType = that.paramType;
		this.returnType = that.returnType;
		this.response = that.response;
		this.rule = that.rule;
		this.version = that.version;
		this.effectiveTime = that.effectiveTime;
		this.expireTime = that.expireTime;
		this.status = that.status;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return MockServiceDataDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = MockServiceDataDefinition.values();
        return defs;
    }
}