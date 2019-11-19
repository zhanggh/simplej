package ${package}.${business}.mapper;

import com.haven.simplej.db.base.BaseMapper;
import org.springframework.stereotype.Repository;

import ${package}.${business}.model.${entity.name}Model;

/**
 * ${entity.comment} DAO Mapper
 */
@Repository
public interface ${entity.name}Mapper extends BaseMapper<${entity.name}Model> {
}