package com.lsl.gulimall.order.dao;

import com.lsl.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 *
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-09-13 14:22:50
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

}
