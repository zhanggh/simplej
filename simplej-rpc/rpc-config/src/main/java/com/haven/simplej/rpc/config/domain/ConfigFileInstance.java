package com.haven.simplej.rpc.config.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.rpc.config.domain.definition.ConfigFileInstanceDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * 配置项生效的服务对应的实例
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "config_file_instance", schema = "rpc_config", indexes = { // catalog 定义为分表数
    @Index(name = "cfg_file_id_idx", columnList = "cfg_file_id", unique = true), //
})
public class ConfigFileInstance extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 配置文件记录的id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "cfg_file_id", nullable = false, precision = 20, comment = "配置文件记录的id", dataType="bigint")
	private Long cfgFileId;

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

	public ConfigFileInstance (){
		super();
	}
	
	public ConfigFileInstance (ConfigFileInstance that){
        super(that);
		this.cfgFileId = that.cfgFileId;
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
        return ConfigFileInstanceDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ConfigFileInstanceDefinition.values();
        return defs;
    }
}