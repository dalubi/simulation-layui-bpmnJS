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
public class EditInformation implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "editId", type = IdType.AUTO)
      private Integer editId;

    @TableField("fromFederateId")
    private Integer fromFederateId;

    @TableField("fromInteractionId")
    private Integer fromInteractionId;

    @TableField("taskId")
    private String taskId;


}
