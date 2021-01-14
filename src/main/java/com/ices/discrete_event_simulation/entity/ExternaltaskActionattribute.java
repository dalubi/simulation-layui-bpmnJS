package com.ices.discrete_event_simulation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zth
 * @since 2021-01-07
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class ExternaltaskActionattribute implements Serializable {

    private static final long serialVersionUID=1L;

    private String attributeName;

    private String changes;

    @TableField("durationTime")
    private String durationTime;

    private String scopeids;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private Integer externaleventid;


}
