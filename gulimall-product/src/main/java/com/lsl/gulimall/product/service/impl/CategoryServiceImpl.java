package com.lsl.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsl.common.utils.PageUtils;
import com.lsl.common.utils.Query;
import com.lsl.gulimall.product.dao.CategoryDao;
import com.lsl.gulimall.product.entity.CategoryEntity;
import com.lsl.gulimall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    private final CategoryDao categoryDao;

    @Override
    public void removeMenuByIds(List<Long> list) {
        //TODO 1.检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> levelOneMenus = categoryEntities.stream()
                .filter(categoryEntity ->
                        categoryEntity.getParentCid() == 0
                ).map((menu) -> {
                    menu.setChildren(getChildrens(menu, categoryEntities));
                    return menu;
                }).sorted(Comparator.comparingInt(
                        categoryEntity -> categoryEntity.getSort() != null ? categoryEntity.getSort() : 0)
                ).collect(Collectors.toList());
        //2.组装树形结构
        return levelOneMenus;
    }

    //递归寻找子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity ->
                        categoryEntity.getParentCid() == root.getCatId()
                ).map(categoryEntity -> {
                    //递归寻找子菜单
                    categoryEntity.setChildren(getChildrens(categoryEntity, all));
                    return categoryEntity;
                }).sorted(Comparator.comparingInt(
                        categoryEntity -> categoryEntity.getSort() != null ? categoryEntity.getSort() : 0)
                ).collect(Collectors.toList());
        return children;
    }
}