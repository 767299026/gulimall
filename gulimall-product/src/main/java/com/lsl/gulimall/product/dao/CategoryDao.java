package com.lsl.gulimall.product.dao;

import com.lsl.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 *
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-08-01 17:39:47
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

}
