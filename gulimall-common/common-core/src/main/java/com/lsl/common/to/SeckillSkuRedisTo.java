package com.lsl.common.to;

//import com.atguigu.gulimall.seckill.vo.SkuInfoVo;

import com.lsl.common.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>Title: SeckillSkuRedisTo</p>
 */
@Data
public class SeckillSkuRedisTo {

    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 商品的秒杀随机码
     */
    private String randomCode;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

    /**
     * sku的详细信息
     */
    private SkuInfoVo skuInfoVo;

    /**
     * 商品秒杀的开始时间
     */
    private Long startTime;

    /**
     * 商品秒杀的结束时间
     */
    private Long endTime;
}
