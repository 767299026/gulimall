package com.lsl.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsl.common.utils.PageUtils;
import com.lsl.common.utils.Query;
import com.lsl.gulimall.product.dao.BrandDao;
import com.lsl.gulimall.product.dao.CategoryBrandRelationDao;
import com.lsl.gulimall.product.dao.CategoryDao;
import com.lsl.gulimall.product.entity.BrandEntity;
import com.lsl.gulimall.product.entity.CategoryBrandRelationEntity;
import com.lsl.gulimall.product.entity.CategoryEntity;
import com.lsl.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    /**
     * 保存关联信息
     * @param categoryBrandRelation
     */
    @Transactional
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        //1.获取id
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        //2.id查询实体
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        //3.设置对应名称
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        //4.保存
        this.save(categoryBrandRelation);
    }

    /**
     * 关联更新品牌信息
     * @param brandId
     * @param name
     */
    @Transactional
    @Override
    public void updateBrand(Long brandId, String name) {
        //1.创建对象,包含需要更新的字段(即有值)
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setBrandId(brandId);
        entity.setBrandName(name);
        //2.更新所有Id = 指定id的行值
        this.update(entity, new LambdaUpdateWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId,brandId));
    }

    /**
     * 关联更新分类信息
     * @param catId
     * @param name
     */
    @Transactional
    @Override
    public void updateCategory(Long catId, String name) {
        //1.创建对象,包含需要更新的字段(即有值)
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setCatelogId(catId);
        entity.setCatelogName(name);
        //2.更新所有Id = 指定id的行值
        this.update(entity, new LambdaUpdateWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getCatelogId,catId));
    }

    /**
     * 普通分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );
        return new PageUtils(page);
    }

}