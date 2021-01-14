package com.ices.discrete_event_simulation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zth
 * @since 2020-11-05
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class FederateObject implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "objectId", type = IdType.AUTO)
      private Integer objectId;

    @TableField("objectName")
    private String objectName;

    @TableField("InitialId")
    private Integer InitialId;

    @TableField("parameterTypes")
    private String parameterTypes;

    @TableField("parameterNames")
    private String parameterNames;


    @TableField("federate")
    private String federate;


}
