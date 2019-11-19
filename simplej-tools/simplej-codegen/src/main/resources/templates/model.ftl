package ${package}.${business}.model;

import lombok.*;
import ${package}.${business}.domain.${entity.name};
import java.util.Collection;

/**
 * ${entity.comment}
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ${entity.name}Model extends ${entity.name} {
	private static final long serialVersionUID = 1L;

	private Long start;

	private Integer length;

    private Collection<Long> ids;

    public void setRequestUserId(Long requestUserId){
        //this.setCreateBy(requestUserId);
    }

    public void setRequestUserName(String requestUserName){
        //this.setCreateBy(requestUserName);
    }

}