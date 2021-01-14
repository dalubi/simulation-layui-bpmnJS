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
public class FederateList implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "listId", type = IdType.AUTO)
      private Integer listId;

    @TableField("objectType")
    private String objectType;

    @TableField("listName")
    private String listName;

    @TableField("federate")
    private String federate;


}
