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
public class InstructionRandomint implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "Id", type = IdType.AUTO)
      private Integer Id;

    @TableField("fromInt")
    private String fromInt;

    @TableField("toInt")
    private String toInt;

    @TableField("outName")
    private String outName;


}
