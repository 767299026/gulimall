package com.lsl.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsl.common.utils.PageUtils;
import com.lsl.common.utils.Query;
import com.lsl.gulimall.product.dao.CategoryDao;
import com.lsl.gulimall.product.entity.CategoryEntity;
import com.lsl.gulimall.product.service.CategoryBrandRelationService;
import com.lsl.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 删除菜单
     * @param list
     */
    @Transactional
    @Override
    public void removeMenuByIds(List<Long> list) {
        //TODO 1.检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(list);
    }

    /**
     * 级联更新分类
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    /**
     * 找到catelogId的完整路径
     * [父/子/孙]
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPaths = findParentPath(catelogId,paths);
        Collections.reverse(parentPaths);
        return parentPaths.toArray(new Long[0]);
    }

    /**
     * 普通分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 树形查询
     * @return
     */
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

    /**
     * 递归寻找子菜单
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity ->
                        categoryEntity.getParentCid().equals(root.getCatId())
                ).map(categoryEntity -> {
                    //递归寻找子菜单
                    categoryEntity.setChildren(getChildrens(categoryEntity, all));
                    return categoryEntity;
                }).sorted(Comparator.comparingInt(
                        categoryEntity -> categoryEntity.getSort() != null ? categoryEntity.getSort() : 0)
                ).collect(Collectors.toList());
        return children;
    }

    /**
     * 递归寻找父Id
     * @param categoryId
     * @param paths
     * @return
     */
    private List<Long> findParentPath(Long categoryId, List<Long> paths) {
        //1.收集当前节点Id
        paths.add(categoryId);
        CategoryEntity byId = this.getById(categoryId);
        //2.继续寻找父节点
        if(!byId.getParentCid().equals(0L)) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }
}