package com.wl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName sys_dict
 */
@TableName(value ="sys_dict")
@Data
public class Dict implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 键
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 类型
     */
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}