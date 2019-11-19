package com.haven.simplej.rpc.mock.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.rpc.mock.domain.definition.MockInstanceDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * mock服务对应的实例
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "mock_instance", schema = "rpc_center", indexes = { // catalog 定义为分表数
    @Index(name = "mock_id", columnList = "mock_id", unique = true), //
})
public class MockInstance extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * mock配置记录的id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "mock_id", nullable = false, precision = 20, comment = "mock配置记录的id", dataType="bigint")
	private Long mockId;

	/**
     * ip地址
     * @definition varchar(64) DEFAULT '' 
     */
    @ColumnProperty(name = "host", nullable = false, length = 64, comment = "ip地址", dataType="varchar")
	private String host;

	/**
     * tcp端口
     * @definition int(11) DEFAULT '0' 
     */
    @ColumnProperty(name = "port", nullable = false, precision = 10, comment = "tcp端口", dataType="int")
	private Integer port;

	/**
     * http端口
     * @definition int(11) DEFAULT '0' 
     */
    @ColumnProperty(name = "http_port", nullable = false, precision = 10, comment = "http端口", dataType="int")
	private Integer httpPort;

	/**
     * init-初始状态，未生效, valid-有效状态,invalid-无效状态
     * @definition varchar(10) DEFAULT 'init' 
     */
    @ColumnProperty(name = "status", nullable = false, length = 10, comment = "init-初始状态，未生效, valid-有效状态,invalid-无效状态", dataType="varchar")
	private String status;

	public MockInstance (){
		super();
	}
	
	public MockInstance (MockInstance that){
        super(that);
		this.mockId = that.mockId;
		this.host = that.host;
		this.port = that.port;
		this.httpPort = that.httpPort;
		this.status = that.status;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return MockInstanceDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = MockInstanceDefinition.values();
        return defs;
    }
}