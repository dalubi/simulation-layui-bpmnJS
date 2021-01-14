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
public class Formdata implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField("PROC_DEF_ID_")
    private String procDefId;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("FORM_KEY_")
    private String formKey;

    @TableField("Control_ID_")
    private String controlId;

    @TableField("Control_VALUE_")
    private String controlValue;


}
