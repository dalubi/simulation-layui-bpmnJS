package com.ices.discrete_event_simulation.entity;

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
public class InsideSelect implements Serializable {

    private static final long serialVersionUID=1L;

      private Integer id;

    @TableField("bran1Info")
    private String bran1Info;

    @TableField("bran2Info")
    private String bran2Info;

    private String iscomplete;


}
