package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Tenant Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("tenant")
public class Tenant extends MicroEntity<Tenant> {

    private static final long serialVersionUID = 1L;

    /**
     * Tenant name
     */
    private String name;
    /**
     * Tenant intro
     */
    private String intro;

}
