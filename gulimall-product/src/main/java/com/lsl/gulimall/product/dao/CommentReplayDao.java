package com.lsl.gulimall.product.dao;

import com.lsl.gulimall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 *
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-08-01 17:39:46
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {

}
