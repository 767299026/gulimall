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

    /**
     * 普通分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 树形查询
     * @return
     */
    List<CategoryEntity> listWithTree();

    /**
     * 删除菜单
     * @param list
     */
    void removeMenuByIds(List<Long> list);

    /**
     * 找到catelogId的完整路径
     * [父/子/孙]
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 级联更新所有分类
     * @param category
     */
    void updateCascade(CategoryEntity category);
}

