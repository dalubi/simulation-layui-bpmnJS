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
public class InstructionSelect implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "Id", type = IdType.AUTO)
      private Integer Id;

    @TableField("InformationName")
    private String InformationName;

    @TableField("branch1Id")
    private String branch1Id;

    @TableField("branch2Id")
    private String branch2Id;


}
