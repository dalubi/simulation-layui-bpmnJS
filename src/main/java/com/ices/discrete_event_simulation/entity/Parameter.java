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
public class Parameter implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "parameterId", type = IdType.AUTO)
      private Integer parameterId;

    @TableField("parameterName")
    private String parameterName;

    @TableField("parameterType")
    private String parameterType;


}
