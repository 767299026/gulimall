package com.lsl.gulimall.product.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 属性分组Vo
 */
@Data
public class AttrGroupVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    /**
     * 完整分类路径
     */
    private Long[] catelogPath;

}
