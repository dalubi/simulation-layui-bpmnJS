package com.ices.discrete_event_simulation.entity;

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
public class StartInformation implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId("federateId")
      private Integer federateId;

    @TableField("isFirst")
    private Integer isFirst;

    @TableField("isExternal")
    private Integer isExternal;

    @TableField("firstTask")
    private Integer firstTask;

    @TableField("hasOwnEvent")
    private Integer hasOwnEvent;

    @TableField("ownEventTask")
    private Integer ownEventTask;

    @TableField("initialInstanceTask")
    private Integer initialInstanceTask;

    @TableField("updateInstanceTask")
    private Integer updateInstanceTask;

    @TableField("listIds")
    private String listIds;

    @TableField("variableIds")
    private String variableIds;

    private String objects;


}
