package com.lsl.gulimall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttrRespVO extends AttrVo{

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;


}
