package com.lsl.gulimall.order.dao;

import com.lsl.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 *
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-09-13 14:22:50
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

}
