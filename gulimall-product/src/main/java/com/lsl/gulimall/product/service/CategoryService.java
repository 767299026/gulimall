package com.lsl.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsl.common.utils.PageUtils;
import com.lsl.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-08-01 17:39:47
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> list);
}

