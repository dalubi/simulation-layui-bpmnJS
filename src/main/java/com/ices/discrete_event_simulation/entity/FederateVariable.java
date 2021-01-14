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
public class FederateVariable implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "variableId", type = IdType.AUTO)
      private Integer variableId;

    @TableField("variableType")
    private String variableType;

    @TableField("variableName")
    private String variableName;

    @TableField("isStatic")
    private Integer isStatic;

    @TableField("InitialValue")
    private String InitialValue;

    @TableField("federate")
    private String federate;

}
